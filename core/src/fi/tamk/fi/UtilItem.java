package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class UtilItem {
    private MainGame game;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private Texture background;
    private BitmapFont bigFont;
    private int money;

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
    }

    public void update() {
        batch.begin();
        drawBackground();
        drawMoney();
        drawAvailableItems();
        drawOwnedItems();
        batch.end();
    }

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
}
