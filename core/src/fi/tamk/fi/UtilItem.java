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

    private Table tableAvailableItems;
    private Table tableOwnedItems;
    private Dialog dialogItems;
    private Dialog popupAvailableItem;
    private Color fontColor;

    private float dialogItemWidth;
    private float dialogItemHeight;
    private float posX;
    private float outOfScreenY;
    private float onScreenY;

    private ScrollPane scrollAvailableItems;
    private ScrollPane scrollOwnedItems;

    private TextButton buttonMoney;

    private String[] allItems;
    int buttonCounter;

    UtilItem(MainGame game) {
        this.game = game;
        batch = game.getBatch();
        stage = game.getStage();
        skin = game.getSkin();
        money = game.getMoney();
        fontColor = game.getFontColor();
        //game.addToInventory("Bomb", false);

        allItems = Item.getAllItems();

        setValues();
        createItemDialog();
        createAvailableItemsTable();
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

    private void createItemDialog() {
        dialogItems = new Dialog("Items", skin);
        dialogItems.setMovable(false);
        dialogItems.setKeepWithinStage(false);
        dialogItems.setPosition(posX, onScreenY);
        dialogItems.setSize(dialogItemWidth, dialogItemHeight);
    }

    private void createAvailableItemsTable() {
        tableAvailableItems = new Table();
        for (int i = 0; i < allItems.length; i++) {
            buttonCounter = i;
            TextButton button0 = new TextButton(allItems[i], skin);
            tableAvailableItems.add(button0).row();
            button0.addListener(new ClickListener(){
                int i = buttonCounter;
                @Override
                public void clicked(InputEvent event, float x, float y){
                    System.out.println(Item.getItem(allItems[i]).get("description"));
                    popupForAvailableItem(i);
                }
            });
        }
        scrollAvailableItems = new ScrollPane(tableAvailableItems, skin);
        scrollAvailableItems.setFadeScrollBars(false);
        scrollAvailableItems.setVisible(true);
        scrollAvailableItems.setSize(dialogItems.getWidth()/4, dialogItems.getHeight()/2);
        scrollAvailableItems.setPosition(dialogItems.getX() + 200,
                dialogItems.getY() + 200);
    }

    private void createOwnedItemsTable() {
        tableOwnedItems = new Table();
        for (int i = 0; i < game.getInventorySize(); i++) {
            if (game.inventoryOrSkillsContains(allItems[i])) {
                TextButton button = new TextButton(allItems[i], skin);
                tableOwnedItems.add(button).row();
            }
        }
        scrollOwnedItems = new ScrollPane(tableOwnedItems, skin);
        scrollOwnedItems.setFadeScrollBars(false);
        scrollOwnedItems.setVisible(true);
        scrollOwnedItems.setSize(dialogItems.getWidth()/4, dialogItems.getHeight()/2);
        scrollOwnedItems.setPosition(dialogItems.getX() + 800,
                dialogItems.getY() + 200);
    }

    private void showMoney() {
        buttonMoney = new TextButton("Money: " + String.valueOf(money), skin);
        buttonMoney.setPosition(dialogItems.getWidth() - 350,
                dialogItems.getHeight() - 200);
    }

    private void popupForAvailableItem(final int index) {
        popupAvailableItem = new Dialog("", skin);
        popupAvailableItem.setSize(dialogItems.getWidth()/2, dialogItems.getHeight()/2);
        popupAvailableItem.setPosition(dialogItems.getWidth()/3, dialogItems.getHeight()/4);
        popupAvailableItem.setMovable(false);

        Label labelOpenedItem = new Label(allItems[index], skin);
        labelOpenedItem.setColor(fontColor);
        labelOpenedItem.setPosition(popupAvailableItem.getWidth()/2,
                popupAvailableItem.getHeight() - labelOpenedItem.getHeight()*3);

        Label labelDescription = new Label(String.valueOf(Item.getItem(allItems[index]).get("description")), skin);
        labelDescription.setColor(fontColor);
        labelDescription.setPosition(labelOpenedItem.getX(), labelOpenedItem.getY() - labelDescription.getHeight()*2);

        final TextButton buttonBuy = new TextButton("Buy", skin);
        buttonBuy.setPosition(popupAvailableItem.getWidth()/2 - 200, popupAvailableItem.getHeight()/4);
        buttonBuy.addListener(new ClickListener(){
            /*
            int i = index;
            String stringCost = String.valueOf(Item.getItem(allItems[i]).get("price"));
            int price = Integer.valueOf(stringCost);
            */
            @Override
            public void clicked(InputEvent event, float x, float y){
                /*
                if (money >= price) {
                    game.decreaseMoney(price);
                    game.addToInventory(allItems[index], false);
                    */
                    popupAvailableItem.remove();
                //}
            }
        });

        TextButton buttonBack = new TextButton("Back", skin);
        buttonBack.setPosition(popupAvailableItem.getWidth()/2 + 100, popupAvailableItem.getHeight()/4);
        buttonBack.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                popupAvailableItem.remove();
            }
        });

        popupAvailableItem.addActor(labelOpenedItem);
        popupAvailableItem.addActor(labelDescription);
        popupAvailableItem.addActor(buttonBuy);
        popupAvailableItem.addActor(buttonBack);
        stage.addActor(popupAvailableItem);
    }

    private void addActors() {
        dialogItems.addActor(scrollAvailableItems);
        dialogItems.addActor(scrollOwnedItems);
        dialogItems.addActor(buttonMoney);
        stage.addActor(dialogItems);
    }
}
