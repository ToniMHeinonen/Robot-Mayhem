package fi.tamk.fi;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    private float bankRetrieved = game.getStepCount();
    private boolean milestoneReached;
    private ImageButton fightButton;
    private Skin finalSkin;

    RoomGame(final MainGame game) {
        super(game);
        createProgressBar();
        curSteps = game.getStepCount();
        finalSkin = files.finalSkin;

        calculateBankSpeed();

        player = new Player();

        // Wrapping enables looping
        imgBG = files.imgBgHall;
        imgBG.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        createMenuButton("hall");
        //createButtonFight(); // For playtesting
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!game.haveWeChangedTheRoom) {
            // Set correct milestone in case of difficulty gets changed
            progressBar.setRange(0f, game.getProgressBarMilestone());
            batch.begin();
            controlBackground();
            drawTopAndBottomBar();
            progressBar.setValue(curSteps); // Control progress bar
            drawSteps();
            player.update();
            retrieveBankSteps();
            checkToChangeRoom();
            batch.end();
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

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
                game.switchToRoomFight();
            }
        });
    }

    public void drawSteps() {
        String strCurSteps = String.valueOf((int) progressBar.getValue());
        String strGoalSteps = String.valueOf((int) progressBar.getMaxValue());
        finalSkin.getFont("font-average").draw(batch,strCurSteps + "/" + strGoalSteps,
                50, game.pixelHeight - finalSkin.getFont("font-average").getXHeight() - 10);
    }

    // Calculates how many steps will be added every frame
    private void calculateBankSpeed() {
        float bank = game.getStepBank();
        float mileStone = game.getProgressBarMilestone();
        // If bank is bigger than mileStone, then count bankSpd using mileStone value
        if (bank > mileStone) {
            bank = mileStone;
        }
        bankSpd = bank / 400;
    }

    private void retrieveBankSteps() {
        // If bank still has steps
        if (game.getStepBank() > 0) {
            // If milestone has not been reached
            if (progressBar.getValue() < progressBar.getMaxValue()) {
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

                // Draw on screen retrieving steps from bank
                int ceiledBank = (int) Math.ceil(game.getStepBank());
                fontSteps.draw(batch, "Retrieving steps\nfrom bank:\n" +
                                String.valueOf(ceiledBank),
                        game.gridSize * 7, game.pixelHeight/2);
            }
        }
    }

    // If milestone has been reached, draw text and
    public void checkToChangeRoom() {
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

    public void controlBackground() {
        // USE THIS TO TEST THE MOVEMENT
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            game.simulateStep();
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

    @Override
    public void selectItem(String selected) {
        player.setUseItem(true);
        game.playSound(files.sndUseItem);
        HashMap<String, Object> item = items.getItem(selected);
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

    }

    /*
    Create class for player
     */
    private class Player {
        private Animation<TextureRegion> idle, moving, item;
        private Animating anim;
        private boolean useItem;
        private float X;
        private float Y;

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

        public void setUseItem(boolean useItem) {
            this.useItem = useItem;
        }
    }
}
