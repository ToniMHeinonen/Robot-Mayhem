package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashMap;

public class UtilPowerUp {
    private MainGame game;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private Texture background, popup;
    private float backgroundX, backgroundY, popupX, popupY;
    private BitmapFont bigFont;
    private int MONEY = 0, HALL_ITEM = 1, BATTLE_ITEM = 2;
    private String chosenName;
    private int chosenType;
    private int moneyAmount;
    private UtilDialog dialog;
    private boolean showDesc, powerUpChosen;
    private Group powerups = new Group();
    private Group confirmation = new Group();
    private Group descriptionBox = new Group();

    private Label.LabelStyle labelStyle;
    private Window.WindowStyle emptyWindowsStyle;

    UtilPowerUp(MainGame game) {
        this.game = game;
        batch = game.getBatch();
        stage = game.getStage();
        dialog = game.getDialog();
        skin = game.getSkin();
        background = game.getPowerUpBg();
        popup = game.getPowerUpPopup();
        bigFont = game.getFontSteps();

        labelStyle = game.getLabelStyle();
        emptyWindowsStyle = game.getEmptyWindowStyle();

        spawnRandomPowerUps();
        stage.addActor(powerups);
        stage.addActor(descriptionBox);
        stage.addActor(confirmation);

        popupX = game.pixelWidth/2f - popup.getWidth()/2f;
        popupY = game.pixelHeight/2f - popup.getHeight()/2f;
        backgroundX = game.pixelWidth/2f - background.getWidth()/2f;
        backgroundY = game.pixelHeight/2f - background.getHeight()/2f;
    }

    public void update() {
        stage.act(Gdx.graphics.getDeltaTime());
        batch.begin();
        drawBackground();
        drawChoosePowerUp();
        powerups.draw(batch,1f);
        drawPopup();
        descriptionBox.draw(batch,1f);
        confirmation.draw(batch, 1f);
        batch.end();
    }

    private void drawBackground() {
        batch.draw(background, backgroundX, backgroundY,
                background.getWidth(), background.getHeight());
    }

    private void drawChoosePowerUp() {
        final GlyphLayout layout = new GlyphLayout(bigFont, "Choose PowerUp");
        final float fontX = game.pixelWidth/2 - layout.width / 2;
        final float fontY = game.pixelHeight -200 - layout.height / 2;
        bigFont.draw(batch, layout, fontX, fontY);
    }

    private void drawPopup() {
        if (showDesc) {
            batch.draw(popup, popupX, popupY, popup.getWidth(), popup.getHeight());
        }
    }

    private void spawnRandomPowerUps() {
        int random[] = new int[2];
        String name, description;
        for (int i = 0; i < 2; i++) {
            while (true) {
                random[i] = MathUtils.random(0, 2);
                if (random[0] == random[1]) continue;

                if (random[i] == MONEY) {
                    name = "Money";
                    moneyAmount = MathUtils.random(5, 10);
                    description = String.valueOf(moneyAmount) + " shiny coins!";
                    break;
                } else {
                    name = Item.selectRandomItem();
                    HashMap<String, Object> map = Item.getItem(name);
                    boolean hallItem = (Boolean) map.get(Item.getUsedInHall());
                    description = (String) map.get(Item.getDescription());
                    if (random[i] == HALL_ITEM) {
                        if (hallItem) break;
                    } else if (random[i] == BATTLE_ITEM) {
                        if (!hallItem) break;
                    }
                }
            }
            createPowerUp(i, name, description);
        }
    }

    private void spawnEnemyPowerUp() {

    }

    private void createPowerUp(final int pos, final String name, final String desc) {
        float[] xPos = new float[] {300f, 750f};
        TextButton btn = new TextButton(name, skin);
        btn.setWidth(400f);
        btn.setHeight(400f);
        btn.setPosition(xPos[pos],  game.pixelHeight/5);

        powerups.addActor(btn);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showDesc = true;
                chosenName = name;
                chosenType = pos;
                createDescription(desc);
                createConfirmationButtons();
            }
        });
    }

    private void createDescription(String text) {
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        final Dialog dialog = new Dialog("", emptyWindowsStyle);
        dialog.getContentTable().add(label).prefWidth(popup.getWidth());
        dialog.setPosition(popupX, popupY + popup.getHeight()/2);
        dialog.setSize(popup.getWidth(),popup.getHeight()/2);
        descriptionBox.addActor(dialog);
    }

    private void createConfirmationButtons() {
        float margin = 50f;
        float btnX = popupX + margin;
        float btnY = popupY + margin;
        final TextButton btn = new TextButton("Choose", skin);
        btn.setWidth(300);
        btn.setHeight(100);
        btn.setPosition(btnX, btnY);
        confirmation.addActor(btn);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addToInventory();
            }
        });

        final TextButton btn2 = new TextButton("Back", skin);
        btn2.setWidth(300);
        btn2.setHeight(100);
        float btn2X = popupX + popup.getWidth() - margin - btn2.getWidth();
        float btn2Y = popupY + margin;
        btn2.setPosition(btn2X, btn2Y);
        confirmation.addActor(btn2);

        btn2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                descriptionBox.clear();
                confirmation.clear();
                showDesc = false;
            }
        });
    }

    private void addToInventory() {
        powerUpChosen = true;
        if (chosenType == MONEY) {
            game.addMoney(moneyAmount);
        } else {
            // Add to inventory
        }
    }

    public boolean isPowerUpChosen() {
        return powerUpChosen;
    }
}
