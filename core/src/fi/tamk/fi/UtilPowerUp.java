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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.HashMap;

public class UtilPowerUp {
    private MainGame game;
    private SpriteBatch batch;
    private Files files;
    private Skills skills;
    private Item items;
    private Stage stage;
    private Skin skin;
    private Skin finalSkin;
    private Texture background, popup;
    private float backgroundX, backgroundY, popupX, popupY;
    private BitmapFont bigFont;
    private int MONEY = 0, HALL_ITEM = 1, BATTLE_ITEM = 2, SKILL = 3;
    private String chosenName;
    private int chosenType;
    private int moneyAmount;
    private String[] spawnedPowerups = new String[3];
    private UtilDialog dialog;
    private boolean showDesc, powerUpChosen;
    private Group powerups = new Group();
    private Group confirmation = new Group();
    private Group descriptionBox = new Group();

    private Label.LabelStyle descriptionLabelStyle;
    private Window.WindowStyle emptyWindowsStyle;

    UtilPowerUp(MainGame game) {
        this.game = game;
        files = game.getFiles();
        skills = game.getSkills();
        items = game.getItems();
        batch = game.getBatch();
        stage = game.getStage();
        dialog = game.getDialog();
        background = files.powerUpBg;
        popup = files.powerUpPopup;
        finalSkin = files.finalSkin;

        // Create constants for text
        descriptionLabelStyle = game.getDescriptionLabelStyle();
        emptyWindowsStyle = game.getEmptyWindowStyle();
        bigFont = game.getFontSteps();
        skin = game.getSkin();

        // Spawn powerups and add everything to stage
        spawnRandomPowerUps();
        spawnEnemyPowerUp();
        stage.addActor(powerups);
        stage.addActor(descriptionBox);
        stage.addActor(confirmation);

        // Set popup's and background's X and Y
        popupX = game.pixelWidth/2f - popup.getWidth()/2f;
        popupY = game.pixelHeight/2f - popup.getHeight()/2f;
        backgroundX = game.pixelWidth/2f - background.getWidth()/2f;
        backgroundY = game.pixelHeight/2f - background.getHeight()/2f;
    }

    public void update() {
        // Draw in correct order
        batch.begin();
        drawBackground();
        drawChoosePowerUp();
        powerups.draw(batch,1f);
        descriptionBox.draw(batch,1f);
        confirmation.draw(batch, 1f);
        batch.end();
    }

    // Draws background
    private void drawBackground() {
        batch.draw(background, backgroundX, backgroundY,
                background.getWidth(), background.getHeight());
    }

    // Draws Choose PowerUp centered
    private void drawChoosePowerUp() {
        final GlyphLayout layout = new GlyphLayout(finalSkin.getFont("font-large"), "Choose PowerUp");
        final float fontX = game.pixelWidth/2 - layout.width / 2;
        final float fontY = game.pixelHeight -200 - layout.height / 2;
        finalSkin.getFont("font-large").draw(batch, layout, fontX, fontY);
    }

    // Spawn first and second powerup randomly
    private void spawnRandomPowerUps() {
        int random[] = new int[2];
        String name, description;
        for (int i = 0; i < 2; i++) {
            while (true) {
                random[i] = MathUtils.random(0, 2);
                // If first and second are same type, randomize again
                if (random[0] == random[1]) continue;

                if (random[i] == MONEY) {
                    name = "Money";
                    moneyAmount = MathUtils.random(5, 10);
                    description = String.valueOf(moneyAmount) + " shiny coins!";
                    break;
                } else {
                    // Select random item
                    name = items.selectRandomItem();
                    HashMap<String, Object> map = items.getItem(name);
                    // If random item is not used in correct room, select new one
                    boolean hallItem = (Boolean) map.get(items.usedInHall);
                    description = (String) map.get(items.description);
                    if (random[i] == HALL_ITEM) {
                        if (hallItem) break;
                    } else if (random[i] == BATTLE_ITEM) {
                        if (!hallItem) break;
                    }
                }
            }
            createPowerUp(i, random[i], name, description);
        }
    }

    // Spawn random powerup right, if player owns boss's both skills
    private void spawnRandomPowerUpRight() {
        int random;
        String name, description;
        while (true) {
            random = MathUtils.random(0, 2);

            if (random == MONEY) {
                name = "Money";
                moneyAmount = MathUtils.random(5, 10);
                description = String.valueOf(moneyAmount) + " shiny coins!";
                break;
            } else {
                name = items.selectRandomItem();
                HashMap<String, Object> map = items.getItem(name);
                boolean hallItem = (Boolean) map.get(items.usedInHall);
                description = (String) map.get(items.description);
                if (random == HALL_ITEM) {
                    if (hallItem) break;
                } else if (random == BATTLE_ITEM) {
                    if (!hallItem) break;
                }
            }
        }

        // If power up is same as 1 or 2, do this method again
        // (I know, not the best way of doing this)
        if (name == spawnedPowerups[0] || name == spawnedPowerups[1]) {
            spawnRandomPowerUpRight();
        } else {
            // pos 2 equals right
            createPowerUp(2, random, name, description);
        }
    }

    // Spawn enemy's skill if player does not own it
    private void spawnEnemyPowerUp() {
        String[] bossSkills = Bosses.retrieveBossSkills(game.getCurrentBoss());
        // Choose from 1 and 2, since 0 is normal attack
        boolean skill1Owned, skill2Owned;
        String chosenSkill = "";

        // Check if skills are already owned
        skill1Owned = game.inventoryOrSkillsContains(bossSkills[1]);
        skill2Owned = game.inventoryOrSkillsContains(bossSkills[2]);

        if (!skill1Owned && !skill2Owned) {
            // If neither is owned, select randomly
            chosenSkill = bossSkills[MathUtils.random(1, 2)];
        } else if (!skill1Owned && skill2Owned) {
            // If only skill 2 is owned, select skill 1
            chosenSkill = bossSkills[1];
        } else if (skill1Owned && !skill2Owned) {
            // If only skill 1 is owned, select skill 2
            chosenSkill = bossSkills[2];
        } else {
            // If both are owned, spawn random powerup
            spawnRandomPowerUpRight();
        }

        // If either skill is not owned, spawn boss's skill
        if (chosenSkill != "") {
            String desc = skills.retrieveSkillDescription(chosenSkill);
            createPowerUp(2, SKILL, chosenSkill, desc);
        }
    }

    // Create buttons for powerups
    private void createPowerUp(int pos, final int type, final String name, final String desc) {
        float[] xPos = new float[] {450f, 750f, 1050f};
        float[] yPos = new float[] {416, 216, 416};
        spawnedPowerups[pos] = name;
        TextButton btn = new TextButton(name, finalSkin);
        btn.setWidth(420f);
        btn.getLabelCell().width(384);
        btn.getLabel().setWrap(true);
        btn.invalidate();
        btn.setPosition(xPos[pos],  yPos[pos]);

        powerups.addActor(btn);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showDesc = true;
                chosenName = name;
                chosenType = type;
                createDescription(name, desc);
                createConfirmationButtons();
            }
        });
    }

    // Draw description for powerup's info
    private void createDescription(String name, String text) {
        final Dialog dialog1 = dialog.createPopupItemAndPowerUp(name, text, "popup_powerup");
        descriptionBox.addActor(dialog1);
    }

    // If powerup is selected, spawn buttons to select it or go back
    private void createConfirmationButtons() {
        float margin = 120f;
        float btnX = popupX + margin + 10;
        float btnY = popupY;
        final ImageButton btn = new ImageButton(finalSkin, "confirm_en");
        //btn.setWidth(300);
        //btn.setHeight(100);
        btn.setPosition(btnX, btnY);
        confirmation.addActor(btn);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addToInventory();
            }
        });

        final ImageButton btn2 = new ImageButton(finalSkin, "cancel_en");
        //btn2.setWidth(300);
        //btn2.setHeight(100);
        float btn2X = btn.getX() + margin*4-40;
        float btn2Y = popupY;
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

    // Add selection to inventory
    private void addToInventory() {
        powerUpChosen = true;
        files.sndChoosePowerUp.play();
        if (chosenType == MONEY) {
            game.addMoney(moneyAmount);
        } else if (chosenType == SKILL) {
            game.addToInventory(chosenName, true);
        } else {
            game.addToInventory(chosenName, false);
        }
    }

    // RoomFight uses this to know when powerup selection is over
    public boolean isPowerUpChosen() {
        return powerUpChosen;
    }
}
