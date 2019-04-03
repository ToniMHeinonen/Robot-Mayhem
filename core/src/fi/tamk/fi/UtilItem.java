package fi.tamk.fi;

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
        posX = 0;
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

    private void addActors() {
        dialogItems.addActor(scrollAvailableItems);
        dialogItems.addActor(scrollOwnedItems);
        dialogItems.addActor(buttonMoney);
        stage.addActor(dialogItems);
    }
}
