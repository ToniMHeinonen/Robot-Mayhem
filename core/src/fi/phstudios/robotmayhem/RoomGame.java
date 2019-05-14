package fi.phstudios.robotmayhem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.HashMap;

public class RoomGame extends RoomParent {

    private Player player;
    private int bgPos; // Background's position used by wrapping
    private float bgSpd; // Cur spd that the background is moving
    private float bgAddSpd = 0.5f; // Amount to add every step
    private final float maxSpd = 15f;
    private float curSteps;
    private float bankSpd;
    private float bankRetrieved;
    private boolean milestoneReached, retrievingSteps, bankHasSteps;
    private ImageButton fightButton;
    private Skin finalSkin;
    private FirstPlay victory, pool1Complete, pool2Complete, pool3Complete, bankTutorial;

    /**
     * Retrieve values from game and create necessary variables.
     * @param game main game instance
     */
    RoomGame(final MainGame game) {
        super(game);
        createProgressBar();
        curSteps = game.getStepCount();
        finalSkin = files.finalSkin;

        player = new Player();

        // Wrapping enables looping
        imgBG = files.imgBgHall;
        imgBG.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        createMenuButton("hall");

        if (game.isFirstPlayTime()) {
            FirstPlay firstPlay = new FirstPlay(game, "firstPlay");
        } else if (game.isFirstPlayNewGamePlus() && game.getGameCompleteCounter() > 0) {
            FirstPlay newGamePlus = new FirstPlay(game, "newGamePlus");
        }
    }

    /**
     * Renders all the frames of the game. Handles player, background and progressbar.
     * @param delta time
     */
    @Override
    public void render(float delta) {
        super.render(delta);

        if (!game.haveWeChangedTheRoom) {
            batch.begin();
            controlBackground();
            drawTopAndBottomBar();
            drawSteps();
            player.update();
            checkTutorialDialogs();
            checkToChangeRoom();
            checkIfBankHasSteps();
            // Set correct milestone in case of difficulty gets changed
            progressBar.setRange(0f, game.getProgressBarMilestone());
            progressBar.setValue(curSteps); // Control progress bar
            calculateBankSpeed();
            retrieveBankSteps();
            batch.end();
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
            endOfRender();
        }
    }

    /**
     * Check if to show tutorial dialog and pause walking.
     */
    private void checkTutorialDialogs() {
        if (game.isFirstPlayTime()) game.setPauseWalking(true);
        else if(game.isFirstPlayNewGamePlus() && game.getGameCompleteCounter() > 0)
        {
            game.setPauseWalking(true);
        }
        else if(game.isFirstPlayBank() && bankTutorial != null) game.setPauseWalking(true);
        else if(game.isFirstPlayVictory() && game.getPoolMult() > 0)
        {
            if (victory == null) victory = new FirstPlay(game, "victory");
            game.setPauseWalking(true);
        }
        else if (game.isFirstPlayPoolComplete1() && game.getPool() == 2)
        {
            if (pool1Complete == null) {
                pool1Complete = new FirstPlay(game, "pool1Complete");
            }
            game.setPauseWalking(true);
        }
        else if (game.isFirstPlayPoolComplete2() && game.getPool() == 3)
        {
            if (pool2Complete == null) {
                pool2Complete = new FirstPlay(game, "pool2Complete");
            }
            game.setPauseWalking(true);
        }
        else if (game.isFirstPlayPoolComplete3() && game.getPool() == 4)
        {
            if (pool3Complete == null) {
                pool3Complete = new FirstPlay(game, "pool3Complete");
            }
            game.setPauseWalking(true);
        }
        else game.setPauseWalking(false);
    }

    /**
     * When milestone has been reached, create button for switching rooms.
     */
    public void createButtonFight() {
        milestoneReached = true;
        Drawable normal = finalSkin.getDrawable("button_ATTACK");
        Drawable clicked = finalSkin.getDrawable("button_ATTACK_clicked");
        fightButton = new ImageButton(normal, clicked);
        fightButton.setPosition(game.pixelWidth/2 - fightButton.getWidth()/2,
                game.pixelHeight/2 - fightButton.getHeight()/2);
        stage.addActor(fightButton);

        fightButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.switchToRoomFight(false);
            }
        });
    }

    /**
     * Draws current steps and milestone.
     */
    public void drawSteps() {
        String strCurSteps = String.valueOf((int) progressBar.getValue());
        String strGoalSteps = String.valueOf((int) progressBar.getMaxValue());
        Label steps = new Label(strCurSteps + "/" + strGoalSteps, finalSkin);
        steps.setPosition(40, 975);
        steps.setWidth(300);
        steps.setAlignment(1);
        steps.draw(batch, 1);
    }

    /**
     * Calculates how many steps will be added every frame.
     */
    private void calculateBankSpeed() {
        // If walking is not paused
        if (!game.isPauseWalking()) {
            // If bank speed is not yet calculated AND stepBank has steps AND milestone not reached
            if (!retrievingSteps && bankHasSteps && !milestoneReached) {
                retrievingSteps = true;
                // If it's first time that bank steps are retrieved, show dialogue
                if (game.isFirstPlayBank()) {
                    bankTutorial = new FirstPlay(game, "bank");
                }
                // Round stepBank, in case for some reason it's for example 0.432
                game.setStepBank(Math.round(game.getStepBank()));
                // Update bankRetrieved value to match stepCount
                bankRetrieved = game.getStepCount();
                float bank = game.getStepBank();
                float mileStone = game.getProgressBarMilestone();
                // If bank is bigger than mileStone, then count bankSpd using mileStone value
                if (bank > mileStone) {
                    bank = mileStone;
                }
                bankSpd = bank / 200;
            }
        }
    }

    /**
     * Retrieves steps from bank and add them to stepCount.
     */
    private void retrieveBankSteps() {
        // If retrievingSteps is activated
        if (!game.isPauseWalking()) {
            // If bank still has steps and if milestone has not been reached
            if (bankHasSteps && !milestoneReached) {
            /*
            - Retrieve previous bankRetrieved value, for example 2.9 and floor it to value 2
            - Add bankSpd value to retrieved then it's value is for example 3.1
            - Then floor the current retrieved value, which is 3
            - Now if current (3) is bigger than previous (2) then set Step count to 3, which
              causes the player to initialize movement in RoomGame
             */
                double prevBankRetrieved = Math.floor(bankRetrieved);
                bankRetrieved += bankSpd;
                double curBankRetrieved = Math.floor(bankRetrieved);
                game.retrieveFromBank(bankSpd);
                if (curBankRetrieved > prevBankRetrieved) {
                    game.setStepCount(bankRetrieved);
                }
                /*
                If bank does not have anymore steps, add the bankSpd value once more, otherwise
                5 bank steps retrieved will be 4.9999
                 */
                checkIfBankHasSteps();
                if (!bankHasSteps) {
                    bankRetrieved += bankSpd;
                    game.setStepCount(bankRetrieved);
                }

                // Draw on screen retrieving steps from bank
                batch.draw(files.retrieveStepsBg, game.gridSize * 6.5f,
                        game.pixelHeight / 2 - files.retrieveStepsBg.getHeight() / 2);
                int ceiledBank = (int) Math.ceil(game.getStepBank());
                finalSkin.getFont("font-average").draw(batch,
                        localize.get("retrievingSteps") + " " + String.valueOf(ceiledBank),
                        game.gridSize * 8.5f, game.pixelHeight / 2 + 100);
            } else retrievingSteps = false;
        }
    }

    /**
     * If milestone has been reached, create button for switching rooms.
     */
    private void checkToChangeRoom() {
        if (curSteps >= progressBar.getMaxValue()) {
            if (!milestoneReached) {
                createButtonFight();
                game.playSound(files.sndMilestoneAchieved);
            }
        } else {
            /*
            If milestone has been reached, but difficulty has been changed higher after that,
            remove fightButton and reset mileStoneReached.
             */
            if (fightButton != null) {
                fightButton.remove();
                milestoneReached = false;
            }
        }
    }

    /**
     * Checks if bank has steps.
     */
    private void checkIfBankHasSteps() {
        if (game.getStepBank() > 0) bankHasSteps = true;
        else bankHasSteps = false;
    }

    /**
     * Initializes progress bar.
     */
    public void createProgressBar() {
        progressBar = new ProgressBar(0, game.getProgressBarMilestone(),
                1, false, game.getProgBarStyle());
        progressBar.setWidth(progressBarStyle.background.getMinWidth());
        progressBar.setHeight(progressBarStyle.background.getMinHeight());
        progressBarStyle.background.setLeftWidth(22f);
        progressBarStyle.background.setRightWidth(25f);
        progressBar.setPosition(game.pixelWidth / 2 - progressBar.getWidth() / 2,
                game.pixelHeight - progressBar.getHeight());
        stage.addActor(progressBar);
    }

    /**
     * Controls background moving and player's animation speed.
     */
    public void controlBackground() {
        // USE THIS TO TEST THE MOVEMENT
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.simulateStep();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.U)) {
            game.bossDefeated();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            game.switchToRoomEnd();
        }

        // Move every step
        if (curSteps != game.getStepCount()) {
            curSteps = game.getStepCount();
            bgSpd += bgAddSpd;
        }

        // Limit max speed
        if (bgSpd > maxSpd) bgSpd = maxSpd;

        // Add friction to speed
        if (bgSpd > 0f) {
            // 4 different values for different speeds
            if (bgSpd > 7.5f) bgSpd -= 0.01f;
            else if (bgSpd > 5f) bgSpd -= 0.008f;
            else if (bgSpd > 2.5f) bgSpd -= 0.007f;
            else bgSpd -= 0.005f;
        } else {
            bgSpd = 0f;
        }

		/*
		If bgPos + next speed addition goes over image width,
		then reset counter, because of the int limit (2147483647)
		*/
        if (bgPos + bgSpd > imgBG.getWidth()) {
            int spdToInt = (int) bgSpd;
            int var = bgPos + spdToInt - imgBG.getWidth();
            bgPos = var;
        } else {
            bgPos += Math.ceil(bgSpd); // Ceil since Int cuts decimals
        }

        // Draw background, srcX handles image looping
        batch.draw(imgBG, 0,0, bgPos, 0, imgBG.getWidth(), imgBG.getHeight());
    }

    /**
     * Controls what different items do.
     * @param selected used item
     */
    @Override
    public void selectItem(String selected) {
        player.setUseItem(true);
        game.playSound(files.sndUseItem);
        HashMap<String, Object> item = items.getItem(selected);
        int itemType = (Integer) item.get(items.itemType);
        if (itemType == items.TYPE_BOOST) {
            int boost = (Integer) item.get(items.boostType);
            if (boost == items.CRIT_BOOST) {
                int amount = (Integer) item.get(items.value);
                game.addPermaCritBoost(amount);
            } else if (boost == items.MISS_BOOST) {
                int amount = (Integer) item.get(items.value);
                game.addPermaMissBoost(amount);
            } else if (boost == items.DMG_BOOST) {
                double amount = (Double) item.get(items.value);
                game.addPermaDmgBoost(amount);
            } else if (boost == items.ARMOR_BOOST) {
                double amount = (Double) item.get(items.value);
                game.addPermaArmorBoost(amount);
            } else if (boost == items.HEAL_BOOST) {
                double amount = (Double) item.get(items.value);
                game.addPermaHealBoost(amount);
            }
        } else if (itemType == items.TYPE_BANK) {
            float amount = (Float) item.get(items.value);
            game.increaseStepBankSize(amount);
        } else if (itemType == items.TYPE_UNIQUE) {
            if (selected.equals(items.ITEM_REFLECT)) {
                game.setReflectiveShield(true);
            }
        }

    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Create player instance
     */
    private class Player {
        private Animation<TextureRegion> idle, moving, item;
        private Animating anim;
        private boolean useItem;
        private float X;
        private float Y;

        /**
         * Retrieve correct animations and set position.
         */
        Player() {
            anim = new Animating();
            X = game.gridSize;
            Y = game.gridSize*2;

            // Retrieve necessary animations and start the correct one
            idle = files.animIdle;
            moving = files.animGameMoving;
            item = files.animItem;
            anim.startAnimation(idle, 8);
        }

        /**
         * Update this method every frame. Controls player animations.
         */
        public void update() {
            if (useItem) {
                if (anim.getAnimation() != item) anim.startAnimation(item, 8);
                if (item.isAnimationFinished(anim.getStateTime())) useItem = false;
            } else {
                // If moving, animate sprite. Else, return to state 0
                if (bgSpd > 0f) {
                    if (anim.getAnimation() != moving) anim.startAnimation(moving, 8);
                    anim.setFrameSpeed((int) maxSpd - (int) bgSpd);
                } else {
                    if (anim.getAnimation() != idle) anim.startAnimation(idle, 8);
                }
            }

            anim.animate();
            anim.draw(batch, X, Y);
        }

        /**
         * Room uses this to let player know when to do item animation.
         * @param useItem if item was used
         */
        public void setUseItem(boolean useItem) {
            this.useItem = useItem;
        }
    }
}
