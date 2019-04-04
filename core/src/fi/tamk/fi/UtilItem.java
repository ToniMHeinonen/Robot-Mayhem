package fi.tamk.fi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class UtilItem {
    private MainGame game;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private int money;

    private Table tableBuyableItems;
    private Table tableOwnedItems;
    private Dialog dialogItems;
    private Dialog popupBuyableItem;
    private Dialog popupOwnedItem;
    private Color fontColor;

    private float dialogItemWidth;
    private float dialogItemHeight;
    private float posX;
    private float outOfScreenY;
    private float onScreenY;

    private TextButton buttonMoney;

    private String[] allItems;
    private int buttonCounterBuyable;
    private int buttonCounterOwned;

    private int bombAmount;
    private int potionAmount;
    private int polishedRotorAmount;

    private int[] amounts = new int[] {bombAmount, potionAmount, polishedRotorAmount};

    private String room;

    UtilItem(MainGame game, String room) {
        this.game = game;
        batch = game.getBatch();
        stage = game.getStage();
        skin = game.getSkin();
        money = game.getMoney();
        fontColor = game.getFontColor();
        //game.addToInventory("Bomb", false);
        //game.addToInventory("Polished Rotor", false);

        allItems = Item.getAllItems();

        this.room = room;

        setValues();
        createItemDialog();
        createBuyableItemsTable();
        createOwnedItemsTable();
        showMoney();
        addActors();
    }

    public void update() {
    }

    private void setValues() {
        dialogItemWidth = game.pixelWidth / 1.2f;
        dialogItemHeight = game.pixelHeight / 1.2f;
        posX = 200;
        outOfScreenY = game.pixelHeight;
        onScreenY = 0;
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
    Table, which contains all items.
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
        createScrollTable(tableBuyableItems, 0, 200);
    }

    /*
    Table, which contains owned items.
     */
    private void createOwnedItemsTable() {
        tableOwnedItems = new Table();
        for (int i = 0; i < allItems.length; i++) {
            buttonCounterOwned = i;
            if (game.inventoryOrSkillsContains(allItems[i])) {
                TextButton button = new TextButton(allItems[i], skin);
                tableOwnedItems.add(button).size(400, 100).row();
                button.addListener(new ClickListener(){
                    int i = buttonCounterOwned;
                    @Override
                    public void clicked(InputEvent event, float x, float y){
                        popupForOwnedItem(i);
                    }
                });
            }
        }
        createScrollTable(tableOwnedItems, 600, 200);
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

    private void showMoney() {
        buttonMoney = new TextButton("Money: " + String.valueOf(money), skin);
        buttonMoney.setPosition(dialogItems.getWidth() - 350,
                dialogItems.getHeight() - 200);
    }

    /*
    This will open, when player has clicked one of the buyable items.
     */
    private void popupForBuyableItem(final int index) {
        popupBuyableItem = new Dialog("", skin);
        popupBuyableItem.setSize(dialogItems.getWidth()/2, dialogItems.getHeight()/2);
        popupBuyableItem.setPosition(dialogItems.getWidth()/3, dialogItems.getHeight()/4);
        popupBuyableItem.setMovable(false);

        createCommonVariables(index, popupBuyableItem);

        final TextButton buttonBuy = new TextButton("Buy", skin);
        buttonBuy.setPosition(popupBuyableItem.getWidth()/2 - 200, popupBuyableItem.getHeight()/4);
        buttonBuy.addListener(new ClickListener(){
            int i = index;
            String stringCost = String.valueOf(Item.getItem(allItems[i]).get("price"));
            int price = Integer.valueOf(stringCost);
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (money >= price) {
                    game.decreaseMoney(price);
                    game.addToInventory(allItems[index], false);
                    popupBuyableItem.remove();
                    dialogItems.remove();
                }
            }
        });

        popupBuyableItem.addActor(buttonBuy);
        stage.addActor(popupBuyableItem);
    }

    /*
    This will open, when player has clicked one of owned items.
     */
    private void popupForOwnedItem(int index) {
        popupOwnedItem = new Dialog("", skin);
        popupOwnedItem.setSize(dialogItems.getWidth()/2, dialogItems.getHeight()/2);
        popupOwnedItem.setPosition(dialogItems.getWidth()/3, dialogItems.getHeight()/4);
        popupOwnedItem.setMovable(false);

        final String usedInHall = String.valueOf(Item.getItem(allItems[index]).get("usedInHall"));
        final int i = index;

        createCommonVariables(index, popupOwnedItem);

        TextButton buttonUse = new TextButton("Use", skin);
        buttonUse.setPosition(popupOwnedItem.getWidth()/2 - 200, popupOwnedItem.getHeight()/4);
        buttonUse.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (room == "hall" && usedInHall == "true") {
                    System.out.println(allItems[i] + " can be used in hall");
                }
                if (room == "fight" && usedInHall == "false") {
                    System.out.println(allItems[i] + " can be used in fight");
                }
            }
        });

        popupOwnedItem.addActor(buttonUse);
        stage.addActor(popupOwnedItem);
    }

    /*
    Common variables: Opened itemtext, description of item and back-button.
     */
    private void createCommonVariables(int index, final Dialog dialog) {
        Label labelOpenedItem = new Label(allItems[index], skin);
        labelOpenedItem.setColor(fontColor);
        labelOpenedItem.setPosition(dialog.getWidth()/2,
                dialog.getHeight() - labelOpenedItem.getHeight()*3);

        Label labelDescription = new Label(String.valueOf(Item.getItem(allItems[index]).get("description")), skin);
        labelDescription.setColor(fontColor);
        labelDescription.setPosition(labelOpenedItem.getX(), labelOpenedItem.getY() - labelDescription.getHeight()*2);

        TextButton buttonBack = new TextButton("Back", skin);
        buttonBack.setPosition(dialog.getWidth()/2 + 100, dialog.getHeight()/4);
        buttonBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.remove();
            }
        });

        dialog.addActor(labelOpenedItem);
        dialog.addActor(labelDescription);
        dialog.addActor(buttonBack);
    }

    private void addActors() {
        dialogItems.addActor(buttonMoney);
        stage.addActor(dialogItems);
    }
}
