package fi.tamk.fi;

import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;

public class UtilItem {
    private MainGame game;
    private Stage stage;
    private Skin skin;
    private Skin finalSkin;
    private int money;
    private String room;

    private Table tableBuyableItems;
    private Table tableOwnedItems;
    private Dialog dialogItems;
    private Dialog popupBuyableItem;
    private Dialog popupOwnedItem;

    private ScrollPane scrollBuyable;
    private ScrollPane scrollOwned;

    private float posX;
    private float onScreenY;

    private UtilDialog utilDialog;

    private String[] allItems;
    private ArrayList<String> inventory;
    private int[] amounts;

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
        stage = game.getStage();
        skin = game.getSkin();
        finalSkin = game.getFinalSkin();
        money = game.getMoney();
        inventory = game.getInventory();
        allItems = Item.getAllItems();
        utilDialog = game.getDialog();
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
    Table, which contains buyable items.
     */
    private void createBuyableItemsTable() {
        int buyableItems = 0;
        tableBuyableItems = new Table();

        for (int i = 0; i < allItems.length; i++) {
            buttonCounterBuyable = i;
            Label shopItems = new Label(allItems[i], finalSkin);
            tableBuyableItems.add(shopItems).size(525, 75);
            buyableItems++;

            final String stringCost = String.valueOf(Item.getItem(allItems[i]).get("price"));
            Label labelPrice = new Label(stringCost, finalSkin);
            tableBuyableItems.add(labelPrice).size(50, 75).row();

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
    Table, which contains owned items.
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

        // (String.valueOf(amounts[i]), skin);

        for (int i = 0; i < allItems.length; i++) {
            buttonCounterOwned = i;
            if (game.inventoryOrSkillsContains(allItems[i])) {
                Label ownedItem = new Label(allItems[i], finalSkin);
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

    private void createScrollTableOwned(Table table) {
        scrollOwned = new ScrollPane(table, finalSkin);
        scrollOwned.setFadeScrollBars(false);
        scrollOwned.setVisible(true);
        scrollOwned.setVariableSizeKnobs(false);
        scrollOwned.setSize(660, 810);
        scrollOwned.setPosition(825, 75);
        dialogItems.addActor(scrollOwned);
    }

    private void createHeaders() {
        labelShop = new Label("Shop", finalSkin, "big");
        labelShop.setPosition(300, 900);
        dialogItems.addActor(labelShop);

        labelInventory = new Label("Inventory", finalSkin, "big");
        labelInventory.setPosition(labelShop.getX() + 650, labelShop.getY());
        dialogItems.addActor(labelInventory);
    }

    private void showMoney() {
        labelMoney = new Label("Money:\n" + String.valueOf(money), finalSkin);
        labelMoney.setPosition(1650, 100);
        labelMoney.setAlignment(1);
        dialogItems.addActor(labelMoney);
    }

    private void createMenuButtons() {
        buttonSettings = new ImageButton(finalSkin.getDrawable("menu_settings1"));
        buttonSettings.setPosition(1441, 720);
        buttonSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Settings settings = new Settings(game, room);
                dialogItems.remove();
            }
        });

        buttonInventory = new ImageButton(finalSkin, "inventory");
        buttonInventory.setPosition(buttonSettings.getX(),
                buttonSettings.getY() - 150);

        buttonStats = new ImageButton(finalSkin.getDrawable("menu_stats1"));
        buttonStats.setPosition(buttonSettings.getX(),
                buttonInventory.getY() - 150);

        dialogItems.addActor(buttonSettings);
        dialogItems.addActor(buttonInventory);
        dialogItems.addActor(buttonStats);
    }

    /*
    This will open, when player has clicked one of the buyable items.
     */
    private void popupForBuyableItem(final int index) {
        String openedItem = allItems[index];
        String description = String.valueOf(Item.getItem(openedItem).get("description"));
        final String stringCost = String.valueOf(Item.getItem(allItems[index]).get("price"));
        final int price = Integer.valueOf(stringCost);

        popupBuyableItem = utilDialog.createPopupItemAndPowerUp(openedItem, description, "popup_item");

        Label labelPrice = new Label(stringCost, finalSkin, "big");
        labelPrice.setPosition(1350, game.pixelHeight / 2 + 195);
        labelPrice.setSize(80, 100);
        labelPrice.setAlignment(1);
        popupBuyableItem.addActor(labelPrice);

        createBackButton(popupBuyableItem);

        final ImageButton buttonBuy = new ImageButton(finalSkin, "buy_en");
        buttonBuy.setPosition(popupBuyableItem.getWidth()/2 - 400, popupBuyableItem.getHeight()/4 - 60);
        buttonBuy.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (money >= price) {
                    game.decreaseMoney(price);
                    game.addToInventory(allItems[index], false);
                    popupBuyableItem.remove();
                    dialogItems.remove();
                    UtilItem utilItem = new UtilItem(game, room);
                }
            }
        });

        popupBuyableItem.addActor(buttonBuy);
        stage.addActor(popupBuyableItem);
    }

    /*
    This will open, when player has clicked one of owned items.
     */
    private void popupForOwnedItem(final int index) {
        String openedItem = allItems[index];
        String description = String.valueOf(Item.getItem(openedItem).get("description"));
        final String usedInHall = String.valueOf(Item.getItem(allItems[index]).get("usedInHall"));

        popupOwnedItem = utilDialog.createPopupItemAndPowerUp(openedItem, description, "popup_powerup");
        createBackButton(popupOwnedItem);

        /*
        When we have finished our skin, this will be:
        TextButton buttonUse = new TextButton("Use", skin, "StyleUseOn")
        and moved inside the if-statement.
        */
        ImageButton buttonUse = new ImageButton(finalSkin, "use_en");
        buttonUse.setPosition(560, 210);
        if ((room.equals("hall") && usedInHall.equals("true")) ||
            (room.equals("fight") && usedInHall.equals("false"))) {
            buttonUse.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.removeFromInventory(allItems[index]);
                    popupOwnedItem.remove();
                    dialogItems.remove();
                }
            });
        }
        /*
        When we have finished our skin, this will be:
        else {
        TextButton buttonUse = new TextButton("Use", skin, "StyleUseOff")
        }
        And not include listener.
        */

        popupOwnedItem.addActor(buttonUse);
        stage.addActor(popupOwnedItem);
    }

    private void createBackButton(final Dialog dialog) {
        ImageButton buttonBack = new ImageButton(finalSkin, "cancel_en");
        buttonBack.setPosition(dialog.getWidth()/2 + 35, 210);
        dialog.addActor(buttonBack);
        buttonBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.remove();
            }
        });
    }

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
}
