package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import java.util.HashMap;

public class UtilPowerUp {
    private MainGame game;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private Texture background;
    private BitmapFont bigFont;
    private int MONEY = 0, HALL_ITEM = 1, BATTLE_ITEM = 2;
    private String powerUp[];

    UtilPowerUp(MainGame game) {
        this.game = game;
        batch = game.getBatch();
        stage = game.getStage();
        skin = game.getSkin();
        background = game.getPowerUpBg();
        bigFont = game.getFontSteps();

        powerUp = new String[2];
        spawnRandomPowerUps();
    }

    public void update() {
        batch.begin();
        batch.draw(background, game.pixelWidth/2f - background.getWidth()/2f,
                game.pixelHeight/2f - background.getHeight()/2f,
                background.getWidth(), background.getHeight());
        drawChoosePowerUp();
        batch.end();
        stage.draw();
    }

    private void drawChoosePowerUp() {
        final GlyphLayout layout = new GlyphLayout(bigFont, "Choose PowerUp");
        final float fontX = game.pixelWidth/2 - layout.width / 2;
        final float fontY = game.pixelHeight -200 - layout.height / 2;
        bigFont.draw(batch, layout, fontX, fontY);
    }

    private void spawnRandomPowerUps() {
        int random[] = new int[2];
        String name;
        for (int i = 0; i < 2; i++) {
            while (true) {
                random[i] = MathUtils.random(0, 2);
                if (random[0] == random[1]) continue;

                if (random[i] == MONEY) {
                    name = "Money";
                    break;
                } else {
                    name = Item.selectRandomItem();
                    HashMap<String, Object> map = Item.getItem(name);
                    boolean hallItem = (Boolean) map.get(Item.getUsedInHall());
                    if (random[i] == HALL_ITEM) {
                        if (hallItem) break;
                    } else if (random[i] == BATTLE_ITEM) {
                        if (!hallItem) break;
                    }
                }
            }
            powerUp[i] = name;
            createPowerUp(i);
        }
    }

    private void spawnEnemyPowerUp() {

    }

    private void createPowerUp(int pos) {
        float[] xPos = new float[] {300f, 750f};
        TextButton btn = new TextButton(powerUp[pos], skin);
        btn.setWidth(400f);
        btn.setHeight(400f);
        btn.setPosition(xPos[pos],  game.pixelHeight/4);

        stage.addActor(btn);
    }
}
