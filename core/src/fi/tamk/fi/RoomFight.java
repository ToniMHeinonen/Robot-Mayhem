package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.xml.soap.Text;

import static com.badlogic.gdx.Input.Keys.X;
import static com.badlogic.gdx.Input.Keys.Y;
import static fi.tamk.fi.MainGame.pixelHeight;
import static fi.tamk.fi.MainGame.pixelWidth;

import static javax.swing.text.html.HTML.Attribute.COLS;
import static javax.swing.text.html.HTML.Attribute.ROWS;

// I comment at least on things that are unclear to me.
// I comment out lines of code that aren't functional yet.

// Modify: picture used, x & y (placement), frame cols & frame rows, framespeed, update
public class RoomFight extends RoomParent {

    private Player player;
    private Enemy enemy;

    RoomFight(MainGame game) {

        super(game);

        player = new Player();
        enemy = new Enemy();
    }

    @Override
    public void render(float delta) {

        super.render(delta);

        player.createActionButton();
        batch.begin();
        player.update();
        batch.end();
    }

    /*
    Don't extend Player since then there can be a lot of conflicts, rather extend Animating class.
    Then create the Enemy it's own update method and do animations related to enemy in there.
    If they later start to have lots of similarities, we can create for example class Objects and
    extend that and then I can modify Animating class so that it does not have to be extended.
     */
    class Enemy extends Animating {

        private Animation<TextureRegion> yellowmove;
        private Animation<TextureRegion> redmove;

        private Texture yellow;
        private Texture red;

        Enemy() {

            X = game.pixelWidth - (player.X + player.width);
            Y = game.pixelHeight/2;

            red = game.getRedTexture();
            yellow = game.getYellowTexture();

            redmove = createAnimation(red, 2, 1);
            yellowmove = createAnimation(yellow, 2,1);
            startAnimation(yellowmove, 50);
            //currentFrame = yellowmove.getKeyFrame(stateTime, true); You don't need this
        }
    }

    /* OLD COMMENT
    Here starts the Player class, you should comment it out like this to make the code more clear
     */
    public class Player extends Animating {
        private Texture img;
        private Animation<TextureRegion> moving;

        private Texture orange;
        private Texture green;

        private Animation<TextureRegion> orangemove;
        private Animation<TextureRegion> greenmove;

        Player() {
            img = game.getGamePlayer(); //You probably don't need this

            X = 100;
            Y = game.pixelHeight/2;

            //Create necessary animations and start the correct one
            //Move to here greenmove = createAnimation(green, 2, 1);
            //Move to here orangemove = createAnimation(orange, 2, 1);

            /* Commented out as this is not used yet
            moving = createAnimation(img, 4, 1);
             */

            orange = game.getOrangeTexture();
            green = game.getGreenTexture();

            orangemove = createAnimation(orange, 2, 1);

            greenmove = createAnimation(green, 2, 1);
            startAnimation(greenmove, 50);
            /*
            You can only start one animation at a time, so either delete startAnimation moving
            or greenmove.
             */
        }

        boolean isButtonClicked = false;

        /*
        Create all the buttons in the RoomFight class and make for example moveAction method in
        Player class which starts the animation, then call the method from the button.
         */
        public  void createActionButton() {
            final TextButton buttonSettings = new TextButton("Action!", skin);
            buttonSettings.setWidth(300f);
            buttonSettings.setHeight(100f);
            buttonSettings.setPosition(game.pixelWidth /2 - buttonSettings.getWidth() /2,
                    (game.pixelHeight/3) *2 - buttonSettings.getHeight() /2);
            stage.addActor(buttonSettings);

            buttonSettings.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    /*
                    You could rename this boolean to for example temporaryAnimation for better
                    readability.
                     */
                    isButtonClicked = true;

                    /*
                    You should create all the animations only in the constructor, then just call
                    startAnimation here.
                    */
                    startAnimation(orangemove, 50);

                    /*
                    Make other button for enemy animations and create it in RoomFight class.
                     */

                    /*
                    You have already created this in enemy constructor.

                    enemy.redmove = createAnimation(enemy.red, 2, 1);*/
                    enemy.startAnimation(enemy.yellowmove, 50);

                    /*
                    You have already created this in enemy constructor.

                    enemy.yellowmove = createAnimation(enemy.yellow, 2, 1);
                     */
                }
            });
        }

        // boolean isItRed = false; Useless. :(
        public void update() {

            stateTime += Gdx.graphics.getDeltaTime() / frameSpeed;

            //Great!

            // Works otherwise but red won't change back. Tried to make it work for a couple of hours.
            if (isButtonClicked) {
                currentFrame = orangemove.getKeyFrame(stateTime, true);
                enemy.currentFrame = enemy.yellowmove.getKeyFrame(stateTime, true);

                if (orangemove.isAnimationFinished(stateTime)) {

                    currentFrame = greenmove.getKeyFrame(stateTime, true);

                    enemy.startAnimation(enemy.redmove, 50);
                    enemy.currentFrame = enemy.redmove.getKeyFrame(stateTime, true);
                }

            } else if (enemy.redmove.isAnimationFinished(stateTime)) {

                currentFrame = greenmove.getKeyFrame(stateTime, true);
                enemy.startAnimation(enemy.yellowmove, 50);
                enemy.currentFrame = enemy.yellowmove.getKeyFrame(stateTime, true);
            } else {

                currentFrame = greenmove.getKeyFrame(stateTime, true);
                enemy.startAnimation(enemy.yellowmove, 50);
                enemy.currentFrame = enemy.yellowmove.getKeyFrame(stateTime, true);
            }

            draw(batch);
            enemy.draw(batch);

            draw(batch);
            enemy.draw(batch);
        }

        // Old code and notes about 'em:
        /*
        if (isButtonClicked) {
                currentFrame = orangemove.getKeyFrame(stateTime, true);
                enemy.currentFrame = enemy.yellowmove.getKeyFrame(stateTime, true);

                if (orangemove.isAnimationFinished(stateTime)) {

                    currentFrame = greenmove.getKeyFrame(stateTime, true);

                    enemy.startAnimation(enemy.redmove, 50);
                    enemy.currentFrame = enemy.redmove.getKeyFrame(stateTime, true);
                }

            } else if (enemy.redmove.isAnimationFinished(stateTime)) {

                currentFrame = greenmove.getKeyFrame(stateTime, true);
                enemy.startAnimation(enemy.yellowmove, 50);
                enemy.currentFrame = enemy.yellowmove.getKeyFrame(stateTime, true);

                isButtonClicked = false;
            } else {

                currentFrame = greenmove.getKeyFrame(stateTime, true);
                enemy.startAnimation(enemy.yellowmove, 50);
                enemy.currentFrame = enemy.yellowmove.getKeyFrame(stateTime, true);
            }
         */

        // Old code as a reference for myself.
        /* if (isButtonClicked) {
                currentFrame = orangemove.getKeyFrame(stateTime, true);
                enemy.currentFrame = enemy.redmove.getKeyFrame(stateTime, true);
                if (greenmove.isAnimationFinished(stateTime) && enemy.yellowmove.isAnimationFinished(stateTime)) {

                    isButtonClicked = false;
                }
            } else {
                currentFrame = greenmove.getKeyFrame(stateTime, true);
                enemy.startAnimation(enemy.yellowmove, 10);
                enemy.currentFrame = enemy.yellowmove.getKeyFrame(stateTime, true);
            }

            draw(batch);
            enemy.draw(batch);*/

        // Fuck this is getting confusing... I'll have to simplify.
            /* G = green, Y = yellow
            && O = orange, R = red

            1. G & Y (default)

            2. Button clicked. {

                3. O & Y

            (isButtonClicked == true &&) O finished: 4
                4. G & R
                make isButtonClicked = false
               }

            R finished (&& isButtonClicked == false):
            5. G & Y

------------------------------------------------------------- */

            /*Old code does:

             (1. G & Y as defaults)
             2. Button is clicked. {

                3. O & R
               }
             4. G & Y
             */
    }
}
