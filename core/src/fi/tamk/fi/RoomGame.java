package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RoomGame extends RoomParent {

    private Texture imgBG;
    private GamePlayer player;
    private int bgPos;
    private float bgSpd;
    private final float maxSpd = 15f;

    RoomGame(MainGame game) {
        super(game);

        player = new GamePlayer();

        //Wrapping enables looping
        imgBG = new Texture("bgPlaceholder.jpg");
        imgBG.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        controlBackground();
        player.update();
        batch.end();
    }

    public void controlBackground() {
        //USE THIS TO TEST THE MOVEMENT
        if( Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) bgSpd += 0.5f;

        //Limit max speed
        if (bgSpd > maxSpd) bgSpd = maxSpd;

        //Add friction to speed
        if (bgSpd > 0f) {
            //4 different values for different speeds
            if (bgSpd > 7.5f) bgSpd -= 0.01f;
            else if (bgSpd > 5f) bgSpd -= 0.008f;
            else if (bgSpd > 2.5f) bgSpd -= 0.007f;
            else bgSpd -= 0.005f;
        } else {
            bgSpd = 0f;
        }

		/*If bgPos + next speed addition goes over image width,
		  then reset counter, because of the int limit (2147483647)*/
        if (bgPos + bgSpd > imgBG.getWidth()) {
            int spdToInt = (int) bgSpd;
            int var = bgPos + spdToInt - imgBG.getWidth();
            bgPos = var;
        } else {
            bgPos += Math.ceil(bgSpd); //Ceil since Int cuts decimals
        }

        //Draw background, srcX handles image looping
        batch.draw(imgBG, 0,0, bgPos, 0, imgBG.getWidth(), imgBG.getHeight());
    }

    /*
    Create class for player
     */
    public class GamePlayer extends Animating {
        private Texture img;
        private Animation<TextureRegion> moving;

        GamePlayer() {
            img = game.getGamePlayer();
            X = 100;
            Y = game.pixelHeight/2;

            //Create necessary animations and start the correct one
            moving = createAnimation(img, 4, 1);
            startAnimation(moving, 10);
        }

        public void update() {
            //If moving, animate sprite
            //Else, return to state 0
            if (bgSpd > 0f) {
                frameSpeed = (int)maxSpd - (int)bgSpd;
                stateTime += Gdx.graphics.getDeltaTime() / frameSpeed;
            } else {
                stateTime = 0f;
            }
            currentFrame = animation.getKeyFrame(stateTime, true);

            draw(batch);
        }
    }
}
