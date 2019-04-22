package fi.phstudios.robotmayhem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.HashMap;

public class UtilPowerUp {
    // Retrieve game variables
    private MainGame game;
    private final I18NBundle localize;
    private SpriteBatch batch;
    private UtilDialog dialog;
    private String lan;
    private Files files;
    private Skills skills;
    private Item items;
    private Bosses bosses;
    private Stage stage;
    private Skin finalSkin;
    private Texture background, popup;

    // Initialize class's own variables
    private float backgroundX, backgroundY, popupX, popupY;
    private int MONEY = 0, HALL_ITEM = 1, BATTLE_ITEM = 2, SKILL = 3;
    private int LEFT = 0, MIDDLE = 1, RIGHT = 2;
    private int[] pupTypes = new int[] {-1, -1, -1};
    private String chosenName;
    private String[] spawnedPowerups = new String[] {"", "", ""};
    private int chosenType, moneyAmount;
    private boolean powerUpChosen;
    private Group powerups = new Group();
    private Group confirmation = new Group();
    private Group descriptionBox = new Group();

    /**
     * Initialize all the basic values.
     * @param game used for retrieving variables
     */
    UtilPowerUp(MainGame game) {
        this.game = game;
        localize = game.getLocalize();
        lan = game.getLanguage();
        files = game.getFiles();
        skills = game.getSkills();
        items = game.getItems();
        bosses = game.getBosses();
        batch = game.getBatch();
        stage = game.getStage();
        dialog = game.getDialog();
        background = files.powerUpBg;
        popup = files.powerUpPopup;
        finalSkin = files.finalSkin;

        // Spawn powerups and add everything to stage
        spawnRandomPowerUp(LEFT);
        spawnRandomPowerUp(MIDDLE);
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

    /**
     * Draw everything in correct order.
     */
    public void update() {
        batch.begin();
        drawBackground();
        drawChoosePowerUp();
        powerups.draw(batch,1f);
        descriptionBox.draw(batch,1f);
        confirmation.draw(batch, 1f);
        batch.end();
    }

    /**
     * Draws background.
     */
    private void drawBackground() {
        batch.draw(background, backgroundX, backgroundY,
                background.getWidth(), background.getHeight());
    }

    /**
     * Draw choose Powerup text centered.
     */
    private void drawChoosePowerUp() {
        final GlyphLayout layout = new GlyphLayout(finalSkin.getFont("font-large"),
                localize.get("choosePowerup"));
        final float fontX = game.pixelWidth/2 - layout.width / 2;
        final float fontY = game.pixelHeight -240 - layout.height / 2;
        finalSkin.getFont("font-large").draw(batch, layout, fontX, fontY);
    }

    /**
     * Spawn random powerup in correct position. Item 3 can be the same type as item 1
     * or 2.
     * @param pos left, middle or right
     */
    private void spawnRandomPowerUp(int pos) {
        String name, description;
        while (true) {
            pupTypes[pos] = MathUtils.random(0, 2);
            // If first and second are same type, randomize again
            if (pupTypes[0] == pupTypes[1]) continue;

            if (pupTypes[pos] == MONEY) {
                name = "money";
                int extra = 0;
                if (game.getDifficulty().equals(game.HARD)) extra = 5;
                moneyAmount = MathUtils.random(15 + extra, 20 + extra);
                description = String.valueOf(moneyAmount) + " " + localize.get("shinyCoins");
                break;
            } else {
                // Select random item
                name = items.selectRandomItem();
                HashMap<String, Object> map = items.getItem(name);

                // If randomized item is more expensive than desired max value, continue
                if ((Integer) map.get(items.price) > items.mostExpensivePowerup) continue;

                // If item is spawned already, continue
                boolean newItem = true;
                for (int i = 0; i < 3; i++) {
                    if (pos != i) {
                        if (name.equals(spawnedPowerups[i])) newItem = false;
                    }
                }
                if (!newItem) continue;

                // If random item is not used in correct room, select new one
                boolean hallItem = (Boolean) map.get(items.usedInHall);
                description = localize.get((String) map.get(items.description));
                if (pupTypes[pos] == HALL_ITEM) {
                    if (hallItem) break;
                } else if (pupTypes[pos] == BATTLE_ITEM) {
                    if (!hallItem) break;
                }
            }
        }
        createPowerUp(pos, pupTypes[pos], name, description);
    }

    /**
     * Spawn enemy's skill if player does not own it. Else spawn random powerup.
     */
    private void spawnEnemyPowerUp() {
        String[] bossSkills = game.getCurBossSkills();
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
            spawnRandomPowerUp(RIGHT);
        }

        // If either skill is not owned, spawn boss's skill
        if (chosenSkill != "") {
            String desc = skills.retrieveSkillDescription(chosenSkill);
            createPowerUp(2, SKILL, chosenSkill, desc);
        }
    }

    /**
     * Create powerup buttons.
     * @param pos left, middle or right
     * @param type money, hall item, battle item or skill
     * @param name name of the item or skill or money
     * @param desc description of the selection
     */
    private void createPowerUp(int pos, final int type, final String name, final String desc) {
        float[] xPos = new float[] {465f, 765f, 1065f};
        float[] yPos = new float[] {310, 190, 310};

        // Add name to spawned powerups and localize name
        spawnedPowerups[pos] = name;
        final String localizedName = localize.get(name);

        // Retrieve correct button name
        String button;
        Drawable normal, clicked;
        if (type == MONEY) button = "money";
        else if (type == HALL_ITEM || type == BATTLE_ITEM) button = "ITEM";
        else button = (String) skills.getSkill(name).get(skills.button);
        normal = finalSkin.getDrawable("button_" + button);
        clicked = finalSkin.getDrawable("button_" + button + "_clicked");
        ImageButton btn = new ImageButton(normal, clicked);
        btn.setPosition(xPos[pos],  yPos[pos]);

        powerups.addActor(btn);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Used in add to inventory
                chosenName = name;
                chosenType = type;
                // Show description and name of selection
                createDescription(localizedName, desc);
                createConfirmationButtons();
            }
        });
    }

    /**
     * Draw description for powerup's info.
     * @param name selection name
     * @param text description
     */
    private void createDescription(String name, String text) {
        final Dialog dialog1 = dialog.createPopupItemAndPowerUp(name, text, "popup_powerup");
        descriptionBox.addActor(dialog1);
    }

    /**
     * If powerup is clicked, spawn buttons to select it or go back.
     */
    private void createConfirmationButtons() {
        float margin = 120f;
        float btnX = popupX + margin + 10;
        float btnY = popupY;
        final ImageButton btn = new ImageButton(finalSkin, "confirm_" + lan);
        btn.setPosition(btnX, btnY);
        confirmation.addActor(btn);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addToInventory();
            }
        });

        final ImageButton btn2 = new ImageButton(finalSkin, "cancel_" + lan);
        float btn2X = btn.getX() + margin*4-40;
        float btn2Y = popupY;
        btn2.setPosition(btn2X, btn2Y);
        confirmation.addActor(btn2);

        btn2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                descriptionBox.clear();
                confirmation.clear();
            }
        });
    }

    /**
     * Add selection to inventory.
     */
    private void addToInventory() {
        powerUpChosen = true;
        game.playSound(files.sndChoosePowerUp);
        if (chosenType == MONEY) {
            game.addMoney(moneyAmount);
        } else if (chosenType == SKILL) {
            game.addToInventory(chosenName, true);
        } else {
            game.addToInventory(chosenName, false);
        }
    }

    /**
     * RoomFight uses this to know when powerup selection is finished.
     * @return if powerup is chosen
     */
    public boolean isPowerUpChosen() {
        return powerUpChosen;
    }
}
