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

public class RoomGame extends RoomParent {

    private Player player;
    private int bgPos; // Background's position used by wrapping
    private float bgSpd; // Cur spd that the background is moving
    private float bgAddSpd = 0.5f; // Amount to add every step
    private final float maxSpd = 15f;
    private float curSteps;
    private float goalSteps;
    private float bankSpd;
    private float bankRetrieved = game.getStepCount();
    private boolean fightSpawned;

    RoomGame(final MainGame game) {
        super(game);
        createProgressBar();
        curSteps = game.getStepCount();
        goalSteps = progressBar.getMaxValue();

        calculateBankSpeed();

        player = new Player();

        // Wrapping enables looping
        imgBG = Files.imgBgHall;
        imgBG.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        createMenuButton();
        //createButtonFight(); // For playtesting
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!game.haveWeChangedTheRoom) {

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
        fightSpawned = true;
        Drawable normal = testSkin.getDrawable("button_attack");
        Drawable clicked = testSkin.getDrawable("button_clicked");
        final ImageButton btn = new ImageButton(normal, clicked);
        btn.setPosition(game.pixelWidth/2 - btn.getWidth()/2,
                game.pixelHeight/2 - btn.getHeight()/2);
        stage.addActor(btn);

        btn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.switchToRoomFight();
            }
        });
    }

    public void drawSteps() {
        String strCurSteps = String.valueOf((int) progressBar.getValue());
        String strGoalSteps = String.valueOf((int) progressBar.getMaxValue());
        fontSteps.draw(batch,strCurSteps + "/" + strGoalSteps,
                50, game.pixelHeight - fontSteps.getXHeight() - 10);
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
        if (curSteps >= goalSteps) {
            if (!fightSpawned) createButtonFight();
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

    /*
    Create class for player
     */
    private class Player {
        private Animation<TextureRegion> idle, moving;
        private Animating anim;
        private float X;
        private float Y;

        Player() {
            anim = new Animating();
            X = game.gridSize;
            Y = game.gridSize*2;

            // Retrieve necessary animations and start the correct one
            idle = Files.animIdle;
            moving = Files.animGameMoving;
            anim.startAnimation(idle, 8);
        }

        public void update() {
            // If moving, animate sprite
            // Else, return to state 0
            if (bgSpd > 0f) {
                if (anim.getAnimation() != moving) anim.startAnimation(moving, 8);
                anim.setFrameSpeed((int)maxSpd - (int)bgSpd);
            } else {
                if (anim.getAnimation() != idle) anim.startAnimation(idle, 8);
            }

            anim.animate();
            anim.draw(batch, X, Y);
        }
    }
}
