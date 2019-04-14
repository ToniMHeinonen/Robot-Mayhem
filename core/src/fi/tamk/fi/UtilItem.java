package fi.tamk.fi;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;

public class UtilItem {
    private MainGame game;
    private RoomFight.Player player;
    private I18NBundle localize;
    private Stage stage;
    private Skin skin;
    private Skin finalSkin;
    private int money;
    private String room;
    private String lan;
    private Files files;
    private Item items;
    private Skills skills;
    private String fontSize;

    private Table tableBuyableItems;
    private Table tableOwnedItems;
    private Dialog dialogItems;
    private Dialog popupBuyableItem;
    private Dialog popupOwnedItem;
    private Dialog popupOwnedSkill;

    private ScrollPane scrollBuyable;
    private ScrollPane scrollOwned;

    private float posX;
    private float onScreenY;

    private UtilDialog utilDialog;

    private String[] allItems;
    private ArrayList<String> inventory;
    private int[] amounts;
    private String[] allSkills;

    private int buttonCounterBuyable;
    private int buttonCounterOwned;

    private Label labelShop;
    private Label labelInventory;

    // Exit
    ImageButton buttonExit;

    // Money
    Label labelMoney;

    // Menu
    ImageButton buttonSettings;
    ImageButton buttonInventory;
    ImageButton buttonStats;

    UtilItem(MainGame game, String room) {
        this.game = game;
        this.room = room;
        create();
    }

    UtilItem(MainGame game, String room, RoomFight.Player player) {
        this.game = game;
        this.room = room;
        this.player = player;
        create();
    }

    private void create() {
        localize = game.getLocalize();
        stage = game.getStage();
        skin = game.getSkin();
        finalSkin = game.getFinalSkin();
        money = game.getMoney();
        inventory = game.getInventory();
        items = game.getItems();
        skills = game.getSkills();
        allItems = items.getAllItems();
        allSkills = skills.getAllSkills();
        utilDialog = game.getDialog();
        lan = game.getLanguage();
        files = game.getFiles();
        amounts = new int[allItems.length];

        setValues();
        createItemDialog();
        createHeaders();
        createBuyableItemsTable();
        createOwnedItemsTable();
        showMoney();
        createExitButton();
        createMenuButtons();
        stage.addActor(dialogItems);
        System.out.println("Item-dialog opened from room: " + room);
        System.out.println("Inventory size: " + inventory.size());
    }

    public void update() {
    }

    private void setValues() {
        posX = 0;
        onScreenY = 0;
    }

    /*
    The whole area, where are buyable and owned items.
     */
    private void createItemDialog() {
        dialogItems = new Dialog("", finalSkin, "inventory");
        dialogItems.setMovable(false);
        dialogItems.setKeepWithinStage(false);
        dialogItems.setPosition(posX, onScreenY);
        dialogItems.setSize(game.pixelWidth, game.pixelHeight);
    }

    /*
    Table, which contains shop items.
     */
    private void createBuyableItemsTable() {
        int buyableItems = 0;
        tableBuyableItems = new Table();

        for (int i = 0; i < allItems.length; i++) {
            buttonCounterBuyable = i;
            String itemName = localize.get(allItems[i]);
            Label shopItems = new Label(itemName, finalSkin, getFontSize(itemName));
            tableBuyableItems.add(shopItems).size(520, 75);
            buyableItems++;

            final String stringCost = String.valueOf(items.getItem(allItems[i]).get(items.price));
            Label labelPrice = new Label(stringCost, finalSkin);
            tableBuyableItems.add(labelPrice).size(35, 75).row();

            shopItems.addListener(new ClickListener(){
                int i = buttonCounterBuyable;
                @Override
                public void clicked(InputEvent event, float x, float y){
                    popupForBuyableItem(i);
                }
            });
        }

        if (buyableItems > 11) {
            createScrollTableBuyable(tableBuyableItems);
        } else {
            tableBuyableItems.setPosition(410, 845 - (buyableItems-1)*75/2);
            dialogItems.addActor(tableBuyableItems);
        }
    }

    /*
    If there are more than 11 items in shop, it creates scrollbar.
     */
    private void createScrollTableBuyable(Table table) {
        scrollBuyable = new ScrollPane(table, finalSkin);
        scrollBuyable.setFadeScrollBars(false);
        scrollBuyable.setVisible(true);
        scrollBuyable.setVariableSizeKnobs(false);
        scrollBuyable.setSize(660, 810);
        scrollBuyable.setPosition(105, 75);
        dialogItems.addActor(scrollBuyable);
    }

    /*
    Table, which contains owned items and skills.
     */
    private void createOwnedItemsTable() {
        tableOwnedItems = new Table();
        int ownedItems = 0;

        // Get the amount of owned items.
        for (int i = 0; i < allItems.length; i++) {
            for (int j = 0; j < inventory.size(); j++) {
                if (inventory.get(j).contains(allItems[i])) {
                    amounts[i]++;
                }
            }
        }

        // Get owned skills, which player isn't using currently.
        for (int i = 0; i < allSkills.length; i++) {
            for (int j = 0; j < inventory.size(); j++) {
                if (inventory.get(j).contains(allSkills[i])) {
                    final int counterSkills = i;
                    String skillName = localize.get(inventory.get(j));
                    Label ownedSkill = new Label(skillName, finalSkin, getFontSize(skillName));
                    tableOwnedItems.add(ownedSkill).size(550, 75).row();
                    ownedItems++;
                    ownedSkill.addListener(new ClickListener(){
                        int i = counterSkills;
                        @Override
                        public void clicked(InputEvent event, float x, float y){
                            popupForOwnedSkill(i);
                        }
                    });
                }
            }
        }

        // (String.valueOf(amounts[i]), skin);

        for (int i = 0; i < allItems.length; i++) {
            buttonCounterOwned = i;
            if (game.inventoryOrSkillsContains(allItems[i])) {
                String itemName = localize.get(allItems[i]);
                Label ownedItem = new Label(itemName, finalSkin, getFontSize(itemName));
                tableOwnedItems.add(ownedItem).size(550, 75).row();
                ownedItems++;

                ownedItem.addListener(new ClickListener(){
                    int i = buttonCounterOwned;
                    @Override
                    public void clicked(InputEvent event, float x, float y){
                        popupForOwnedItem(i);
                    }
                });
            }
        }

        if (ownedItems > 11) {
            createScrollTableOwned(tableOwnedItems);
        } else {
            tableOwnedItems.setPosition(1120, 845 - (ownedItems-1)*75/2);
            dialogItems.addActor(tableOwnedItems);
        }
    }

    /*
    If there are more than 11 items in inventory, it creates scrollbar.
    */
    private void createScrollTableOwned(Table table) {
        scrollOwned = new ScrollPane(table, finalSkin);
        scrollOwned.setFadeScrollBars(false);
        scrollOwned.setVisible(true);
        scrollOwned.setVariableSizeKnobs(false);
        scrollOwned.setSize(660, 810);
        scrollOwned.setPosition(825, 75);
        dialogItems.addActor(scrollOwned);
    }

    /*
    Creates "Shop" and "Inventory" headers.
     */
    private void createHeaders() {
        labelShop = new Label(localize.get("shop"), finalSkin, "big");
        labelShop.setSize(600, labelShop.getPrefHeight());
        labelShop.setPosition(125, 900);
        labelShop.setAlignment(1);
        dialogItems.addActor(labelShop);

        labelInventory = new Label(localize.get("inventory"), finalSkin, "big");
        labelInventory.setSize(600, labelInventory.getPrefHeight());
        labelInventory.setPosition(labelShop.getX() + 720, labelShop.getY());
        labelInventory.setAlignment(1);
        dialogItems.addActor(labelInventory);
    }

    private void showMoney() {
        labelMoney = new Label(localize.get("shopMoney") + String.valueOf(money), finalSkin);
        labelMoney.setPosition(1650, 100);
        labelMoney.setAlignment(1);
        dialogItems.addActor(labelMoney);
    }

    /*
    Creates settings, inventory and stats buttons in the right side of the screen.
     */
    private void createMenuButtons() {
        buttonSettings = new ImageButton(finalSkin.getDrawable("menu_settings1"));
        buttonSettings.setPosition(1440, 720);
        buttonSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Settings settings = new Settings(game, room);
                dialogItems.remove();
            }
        });

        buttonInventory = new ImageButton(finalSkin, "inventory");
        buttonInventory.setPosition(buttonSettings.getX(),
                buttonSettings.getY() - 180);

        buttonStats = new ImageButton(finalSkin.getDrawable("menu_stats1"));
        buttonStats.setPosition(buttonSettings.getX(),
                buttonInventory.getY() - 180);

        dialogItems.addActor(buttonSettings);
        dialogItems.addActor(buttonInventory);
        dialogItems.addActor(buttonStats);
    }

    /*
    This will open, when player has clicked one of the items in shop.
     */
    private void popupForBuyableItem(final int index) {
        String openedItem = allItems[index];
        String itemName = localize.get(allItems[index]);
        String description = String.valueOf(items.getItem(openedItem).get(items.description));
        description = localize.get(description);
        final String stringCost = String.valueOf(items.getItem(allItems[index]).get(items.price));
        final int price = Integer.valueOf(stringCost);

        popupBuyableItem = utilDialog.createPopupItemAndPowerUp(itemName, description, "popup_item");

        Label labelPrice = new Label(stringCost, finalSkin, "big");
        labelPrice.setPosition(1350, game.pixelHeight / 2 + 195);
        labelPrice.setSize(80, 100);
        labelPrice.setAlignment(1);
        popupBuyableItem.addActor(labelPrice);

        createBackButton(popupBuyableItem);

        ImageButton buttonBuy = new ImageButton(finalSkin, "buy_" + lan);
        buttonBuy.setPosition(popupBuyableItem.getWidth()/2 - 400, popupBuyableItem.getHeight()/4 - 60);
        buttonBuy.setDisabled(true);
        if (money >= price) {
            buttonBuy.setDisabled(false);
            buttonBuy.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    files.sndPurchaseItem.play();
                    game.decreaseMoney(price);
                    game.addToInventory(allItems[index], false);
                    popupBuyableItem.remove();
                    dialogItems.remove();
                    UtilItem utilItem;
                    if (room.equals("fight")) utilItem = new UtilItem(game, room, player);
                    else  utilItem = new UtilItem(game, room);
                }
            });
        }

        popupBuyableItem.addActor(buttonBuy);
        stage.addActor(popupBuyableItem);
    }

    /*
    This will open, when player has clicked one of owned items.
     */
    private void popupForOwnedItem(final int index) {
        String openedItem = allItems[index];
        String itemName = localize.get(allItems[index]);
        String description = String.valueOf(items.getItem(openedItem).get(items.description));
        description = localize.get(description);
        final String usedInHall = String.valueOf(items.getItem
                (allItems[index]).get(items.usedInHall));

        popupOwnedItem = utilDialog.createPopupItemAndPowerUp(itemName, description,
                "popup_powerup");
        createBackButton(popupOwnedItem);

        ImageButton buttonUse = new ImageButton(finalSkin, "use_" + lan);
        buttonUse.setPosition(560, 210);
        buttonUse.setDisabled(true);
        if ((room.equals("hall") && usedInHall.equals("true")) ||
            (room.equals("fight") && usedInHall.equals("false"))) {
            buttonUse.setDisabled(false);
            buttonUse.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.removeFromInventory(allItems[index]);
                    popupOwnedItem.remove();
                    dialogItems.remove();
                    // This throws nullPointerException if I use player.selectItem method
                    if (room.equals("fight")) player.selectItem(allItems[index]);
                }
            });
        }

        popupOwnedItem.addActor(buttonUse);
        stage.addActor(popupOwnedItem);
    }

    /**
     * Use item in RoomFight.
     * @param item selected item
     */
    public void selectRoomFightItem(String item) {

    }

    /*
    This will open, when player has clicked one of owned skills.
    Player can change skills only in RoomGame.
     */
    private void popupForOwnedSkill(final int index) {
        String openedSkill = allSkills[index];
        String skillName = localize.get(allSkills[index]);
        String description = skills.retrieveSkillDescription(openedSkill);

        popupOwnedSkill = utilDialog.createPopupItemAndPowerUp(skillName,
                description + " " + localize.get("replaceSkill"),
                "popup_powerup");

        TextButton buttonSkill1 = new TextButton(localize.get(game.getSkill1()),
                finalSkin, "small");
        buttonSkill1.setPosition(470, 210);
        buttonSkill1.setDisabled(true);
        if (room.equals("hall")) {
            buttonSkill1.setDisabled(false);
            buttonSkill1.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.addToInventory(game.getSkill1(), true);
                    game.setSkill1(allSkills[index]);
                    game.removeFromInventory(allSkills[index]);
                    popupOwnedSkill.remove();
                    dialogItems.remove();
                    UtilItem utilItem = new UtilItem(game, room);
                }
            });
        }

        TextButton buttonSkill2 = new TextButton(localize.get(game.getSkill2()),
                finalSkin, "small");
        buttonSkill2.setPosition(820, 210);
        buttonSkill2.setDisabled(true);
        if (room.equals("hall")) {
            buttonSkill2.setDisabled(false);
            buttonSkill2.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.addToInventory(game.getSkill2(), true);
                    game.setSkill2(allSkills[index]);
                    game.removeFromInventory(allSkills[index]);
                    popupOwnedSkill.remove();
                    dialogItems.remove();
                    UtilItem utilItem = new UtilItem(game, room);
                }
            });
        }

        TextButton buttonCancel = new TextButton(localize.get("cancel"), finalSkin, "small");
        buttonCancel.setPosition(1175, 210);
        buttonCancel.setSize(250, 120);
        buttonCancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                popupOwnedSkill.remove();
            }
        });

        popupOwnedSkill.addActor(buttonSkill1);
        popupOwnedSkill.addActor(buttonSkill2);
        popupOwnedSkill.addActor(buttonCancel);
        stage.addActor(popupOwnedSkill);
    }

    /*
    If items or skills are more than 17 characters long, it will put smaller font to them.
     */
    private String getFontSize(String item) {
        fontSize = "default";
        if (item.length() >= 17) {
            fontSize = "small";
        }
        return fontSize;
    }

    private void createBackButton(final Dialog dialog) {
        ImageButton buttonBack = new ImageButton(finalSkin, "cancel_" + lan);
        buttonBack.setPosition(dialog.getWidth()/2 + 35, 210);
        dialog.addActor(buttonBack);
        buttonBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.remove();
            }
        });
    }

    /*
    The X-button in the top-right corner of the screen.
     */
    private void createExitButton() {
        buttonExit = new ImageButton(finalSkin, "x");
        buttonExit.setPosition(1550, 960);
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialogItems.remove();
            }
        });

        dialogItems.addActor(buttonExit);
    }

    /*
    If you add this to item-button, it should open inventory:
    UtilItem utilItem = new UtilItem(game, "fight");
     */
}
