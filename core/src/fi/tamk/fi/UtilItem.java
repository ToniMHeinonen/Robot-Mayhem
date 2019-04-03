package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class UtilItem {
    private MainGame game;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private Texture background;
    private BitmapFont bigFont;
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

    private float backgroundX, backgroundY;

    UtilItem(MainGame game) {
        this.game = game;
        batch = game.getBatch();
        stage = game.getStage();
        skin = game.getSkin();
        background = game.getItemBg();
        bigFont = game.getFontSteps();
        money = game.getMoney();

        backgroundX = game.pixelWidth/2f - background.getWidth()/2f;
        backgroundY = game.pixelHeight/2f - background.getHeight()/2f;

        setValues();
        createItemDialog();
        createAvailableItemsTable();
        createOwnedItemsTable();
        addActors();
    }

    public void update() {
        /*
        batch.begin();
        drawBackground();
        drawMoney();
        drawAvailableItems();
        drawOwnedItems();
        batch.end();
        */
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
        TextButton button1 = new TextButton("item1", skin);
        TextButton button2 = new TextButton("item2", skin);
        TextButton button3 = new TextButton("item3", skin);
        TextButton button4 = new TextButton("item4", skin);
        TextButton button5 = new TextButton("item5", skin);
        TextButton button6 = new TextButton("item6", skin);
        TextButton button7 = new TextButton("item7", skin);
        TextButton button8 = new TextButton("item8", skin);
        tableAvailableItems.add(button1).row();
        tableAvailableItems.add(button2).row();
        tableAvailableItems.add(button3).row();
        tableAvailableItems.add(button4).row();
        tableAvailableItems.add(button5).row();
        tableAvailableItems.add(button6).row();
        tableAvailableItems.add(button7).row();
        tableAvailableItems.add(button8).row();
        scrollAvailableItems = new ScrollPane(tableAvailableItems, skin);
        scrollAvailableItems.setFadeScrollBars(false);
        scrollAvailableItems.setVisible(true);
        scrollAvailableItems.setSize(dialogItems.getWidth()/4, dialogItems.getHeight()/2);
        scrollAvailableItems.setPosition(dialogItems.getX() + 200,
                dialogItems.getY() + 200);
    }

    private void createOwnedItemsTable() {
        tableOwnedItems = new Table();
        TextButton button1 = new TextButton("item1", skin);
        TextButton button2 = new TextButton("item2", skin);
        TextButton button3 = new TextButton("item3", skin);
        TextButton button4 = new TextButton("item4", skin);
        TextButton button5 = new TextButton("item5", skin);
        TextButton button6 = new TextButton("item6", skin);
        TextButton button7 = new TextButton("item6", skin);
        TextButton button8 = new TextButton("item6", skin);
        tableOwnedItems.add(button1).row();
        tableOwnedItems.add(button2).row();
        tableOwnedItems.add(button3).row();
        tableOwnedItems.add(button4).row();
        tableOwnedItems.add(button5).row();
        tableOwnedItems.add(button6).row();
        tableOwnedItems.add(button7).row();
        tableOwnedItems.add(button8).row();
        scrollOwnedItems = new ScrollPane(tableOwnedItems, skin);
        scrollOwnedItems.setFadeScrollBars(false);
        scrollOwnedItems.setVisible(true);
        scrollOwnedItems.setSize(dialogItems.getWidth()/4, dialogItems.getHeight()/2);
        scrollOwnedItems.setPosition(dialogItems.getX() + 800,
                dialogItems.getY() + 200);
    }

    private void addActors() {
        dialogItems.addActor(scrollAvailableItems);
        dialogItems.addActor(scrollOwnedItems);
        stage.addActor(dialogItems);
    }

    /*
    private void drawBackground() {
        batch.draw(background, backgroundX, backgroundY,
                background.getWidth(), background.getHeight());
    }

    private void drawAvailableItems() {
        final GlyphLayout layout = new GlyphLayout(bigFont, "Available items");
        final float fontX = game.pixelWidth / 2 - layout.width;
        final float fontY = game.pixelHeight - 200 - layout.height;
        bigFont.draw(batch, layout, fontX, fontY);
    }

    private void drawOwnedItems() {
        final GlyphLayout layout = new GlyphLayout(bigFont, "Owned items");
        final float fontX = game.pixelWidth / 2 + layout.width/3;
        final float fontY = game.pixelHeight - 200 - layout.height;
        bigFont.draw(batch, layout, fontX, fontY);
    }

    private void drawMoney() {
        final GlyphLayout layout = new GlyphLayout(bigFont, "Money: " +  String.valueOf(money));
        final float fontX = game.pixelWidth / 2 + layout.width;
        final float fontY = game.pixelHeight - 150;
        bigFont.draw(batch, layout, fontX, fontY);
    }
    */
}
