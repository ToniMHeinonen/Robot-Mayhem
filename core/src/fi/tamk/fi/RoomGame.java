package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class RoomGame extends RoomParent {

    private Player player;
    private int bgPos; // Background's position used by wrapping
    private float bgSpd; // Cur spd that the background is moving
    private float bgAddSpd = 0.5f; // Amount to add every step
    private final float maxSpd = 15f;
    private int curSteps;
    private String currentSteps;
    private String goalSteps;

    // Testing
    int testSteps = 0;

    RoomGame(final MainGame game) {
        super(game);
        curSteps = game.getStepCount();

        player = new Player();

        // Wrapping enables looping
        imgBG = game.getImgBgHall();
        imgBG.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        createMenuButton();
        createProgressBar();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!game.haveWeChangedTheRoom) {

            batch.begin();
            controlBackground();
            drawTopAndBottomBar();
            controlProgBar();
            drawSteps();
            player.update();
            batch.end();
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    public void drawSteps() {
        currentSteps = String.valueOf((int) progressBar.getValue());
        goalSteps = String.valueOf((int) progressBar.getMaxValue());
        fontSteps.draw(batch, currentSteps + "/" + goalSteps,
                50, game.pixelHeight - fontSteps.getXHeight() - 10);
    }

    public void controlProgBar() {
        // For desktop
        progressBar.setValue(testSteps);

        // For android
        // progressBar.setValue(curSteps);
    }

    public void controlBackground() {
        // USE THIS TO TEST THE MOVEMENT
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            bgSpd += bgAddSpd;
            // testSteps are for testing in desktop
            testSteps++;
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
        private Texture img;
        private Animation<TextureRegion> moving;
        private Animating anim;
        private float X;
        private float Y;

        Player() {
            img = game.getGamePlayer();
            anim = new Animating();
            X = 100;
            Y = 200;

            // Create necessary animations and start the correct one
            moving = anim.createAnimation(img, 4, 1);
            anim.startAnimation(moving, 10);
        }

        public void update() {
            // If moving, animate sprite
            // Else, return to state 0
            if (bgSpd > 0f) {
                anim.setFrameSpeed((int)maxSpd - (int)bgSpd);
                anim.animate();
            } else {
                anim.setStateTime(0f);
            }

            anim.draw(batch, X, Y);
        }
    }
}
