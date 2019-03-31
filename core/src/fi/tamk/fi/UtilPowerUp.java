package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class UtilPowerUp {
    private MainGame game;
    private SpriteBatch batch;
    private Texture background;
    private BitmapFont bigFont;
    private int type, MONEY = 0, HALL_ITEM = 1, BATTLE_ITEM = 2;

    UtilPowerUp(MainGame game) {
        this.game = game;
        batch = game.getBatch();
        background = game.getPowerUpBg();
        bigFont = game.getFontSteps();
    }

    public void update() {
        batch.begin();
        batch.draw(background, game.pixelWidth/2f - background.getWidth()/2f,
                game.pixelHeight/2f - background.getHeight()/2f,
                background.getWidth(), background.getHeight());
        drawChoosePowerUp();
        batch.end();
    }

    private void drawChoosePowerUp() {
        final GlyphLayout layout = new GlyphLayout(bigFont, "Choose PowerUp");
        final float fontX = game.pixelWidth/2 - layout.width / 2;
        final float fontY = game.pixelHeight -200 - layout.height / 2;
        bigFont.draw(batch, layout, fontX, fontY);
    }
}
