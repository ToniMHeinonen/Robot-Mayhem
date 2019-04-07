package fi.tamk.fi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

public class UtilItem {
    private MainGame game;
    private Stage stage;
    private Skin skin;
    private int money;
    private String room;
    private Color fontColor;

    private Table tableBuyableItems;
    private Table tableOwnedItems;
    private Dialog dialogItems;
    private Dialog popupBuyableItem;
    private Dialog popupOwnedItem;

    private float dialogItemWidth;
    private float dialogItemHeight;
    private float posX;
    private float outOfScreenY;
    private float onScreenY;

    private UtilDialog utilDialog;

    private TextButton buttonMoney;
    private TextButton buttonBuyItem;
    private TextButton buttonOwnedItems;
    private TextButton buttonPrice;
    private TextButton buttonAmount;
    private TextButton buttonClose;

    private String[] allItems;
    private ArrayList<String> inventory;
    private int[] amounts;

    private int buttonCounterBuyable;
    private int buttonCounterOwned;

    UtilItem(MainGame game, String room) {
        this.game = game;
        this.room = room;
        stage = game.getStage();
        skin = game.getSkin();
        money = game.getMoney();
        fontColor = game.getFontColor();
        inventory = game.getInventory();
        allItems = Item.getAllItems();
        utilDialog = game.getDialog();
        amounts = new int[allItems.length];

        setValues();
        createItemDialog();
        createBuyableItemsTable();
        createOwnedItemsTable();
        createHeaders();
        showMoney();
        addActors();
        System.out.println("Item-dialog opened from room: " + room);
        System.out.println("Inventory size: " + inventory.size());
    }

    public void update() {
    }

    private void setValues() {
        dialogItemWidth = game.pixelWidth / 1.2f;
        dialogItemHeight = game.pixelHeight / 1.2f;
        posX = 150;
        outOfScreenY = game.pixelHeight;
        onScreenY = game.pixelHeight/9;
    }

    /*
    The whole area, where are buyable and owned items.
     */
    private void createItemDialog() {
        dialogItems = new Dialog("Items", skin);
        dialogItems.setMovable(false);
        dialogItems.setKeepWithinStage(false);
        dialogItems.setPosition(posX, onScreenY);
        dialogItems.setSize(dialogItemWidth, dialogItemHeight);
    }

    /*
    Table, which contains buyable items.
     */
    private void createBuyableItemsTable() {
        tableBuyableItems = new Table();

        for (int i = 0; i < allItems.length; i++) {
            buttonCounterBuyable = i;
            TextButton button0 = new TextButton(allItems[i], skin);
            tableBuyableItems.add(button0).size(400, 100);

            button0.addListener(new ClickListener(){
                int i = buttonCounterBuyable;
                @Override
                public void clicked(InputEvent event, float x, float y){
                    popupForBuyableItem(i);
                }
            });

            String stringCost = String.valueOf(Item.getItem(allItems[i]).get("price"));
            TextButton buttonPrice = new TextButton(stringCost, skin);
            tableBuyableItems.add(buttonPrice).size(100, 100).row();
        }

        createScrollTable(tableBuyableItems, -100, 100);
    }

    /*
    Table, which contains owned items.
     */
    private void createOwnedItemsTable() {
        tableOwnedItems = new Table();

        // Get the amount of owned items.
        for (int i = 0; i < allItems.length; i++) {
            for (int j = 0; j < inventory.size(); j++) {
                if (inventory.get(j).contains(allItems[i])) {
                    amounts[i]++;
                }
            }
        }

        for (int i = 0; i < allItems.length; i++) {
            buttonCounterOwned = i;
            if (game.inventoryOrSkillsContains(allItems[i])) {
                TextButton buttonItem = new TextButton(allItems[i], skin);
                TextButton buttonAmount = new TextButton(String.valueOf(amounts[i]), skin);
                tableOwnedItems.add(buttonItem).size(400, 100);
                tableOwnedItems.add(buttonAmount).size(100, 100).row();

                buttonItem.addListener(new ClickListener(){
                    int i = buttonCounterOwned;
                    @Override
                    public void clicked(InputEvent event, float x, float y){
                        popupForOwnedItem(i);
                    }
                });
            }
        }

        createScrollTable(tableOwnedItems, 600, 100);
    }

    /*
    Scrollpane is used so that the tables can have scrollbar.
     */
    private void createScrollTable(Table table, float x, float y) {
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setVisible(true);
        scrollPane.setSize(dialogItems.getWidth()/2, dialogItems.getHeight()/2);
        scrollPane.setPosition(dialogItems.getX() + x, dialogItems.getY() + y);
        dialogItems.addActor(scrollPane);
    }

    private void createHeaders() {
        buttonBuyItem = new TextButton("Buy item", skin);
        buttonBuyItem.setPosition(dialogItems.getX()+ 100, dialogItems.getY() + 500);

        buttonOwnedItems = new TextButton("Owned items", skin);
        buttonOwnedItems.setPosition(dialogItems.getX() + 750, dialogItems.getY() + 500);

        buttonPrice = new TextButton("Price", skin);
        buttonPrice.setSize(200, 100);
        buttonPrice.setPosition(dialogItems.getX() + 400, dialogItems.getY() + 500);

        buttonAmount = new TextButton("#", skin);
        buttonAmount.setSize(100, 100);
        buttonAmount.setPosition(dialogItems.getX() + 1150, dialogItems.getY() + 500);

        buttonClose = new TextButton("Close", skin);
        buttonClose.setPosition(dialogItems.getX() + 475, dialogItems.getY());
        buttonClose.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialogItems.remove();
            }
        });
    }

    private void showMoney() {
        buttonMoney = new TextButton("Money: " + String.valueOf(money), skin);
        buttonMoney.setPosition(dialogItems.getWidth() - 350,
                dialogItems.getHeight() - 150);
    }

    /*
    This will open, when player has clicked one of the buyable items.
     */
    private void popupForBuyableItem(final int index) {
        String openedItem = allItems[index];
        String description = String.valueOf(Item.getItem(openedItem).get("description"));
        final String stringCost = String.valueOf(Item.getItem(allItems[index]).get("price"));
        final int price = Integer.valueOf(stringCost);

        popupBuyableItem = utilDialog.createPopupItemAndPowerUp(openedItem, description);
        createBackButton(popupBuyableItem);

        final TextButton buttonBuy = new TextButton("Buy", skin);
        buttonBuy.setPosition(popupBuyableItem.getWidth()/2 - 300, popupBuyableItem.getHeight()/4);
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

        popupOwnedItem = utilDialog.createPopupItemAndPowerUp(openedItem, description);
        createBackButton(popupOwnedItem);

        /*
        When we have finished our skin, this will be:
        TextButton buttonUse = new TextButton("Use", skin, "StyleUseOn")
        and moved inside the if-statement.
        */
        TextButton buttonUse = new TextButton("Use", skin);
        buttonUse.setPosition(popupOwnedItem.getWidth()/2 - 300, popupOwnedItem.getHeight()/4);
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
        TextButton buttonBack = new TextButton("Back", skin);
        buttonBack.setPosition(dialog.getWidth()/2, dialog.getHeight()/4);
        dialog.addActor(buttonBack);
        buttonBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.remove();
            }
        });
    }

    private void addActors() {
        dialogItems.addActor(buttonMoney);
        dialogItems.addActor(buttonBuyItem);
        dialogItems.addActor(buttonOwnedItems);
        dialogItems.addActor(buttonPrice);
        dialogItems.addActor(buttonAmount);
        dialogItems.addActor(buttonClose);
        stage.addActor(dialogItems);
    }
}
