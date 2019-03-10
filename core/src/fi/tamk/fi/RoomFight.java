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

// Modify picture used, x & y (placement), frame cols & frame rows, framespeed, update
// update: pelkkä statetime += ja currentframe ja drawbatch

// Tee: idlaus-animaatio ja animaation vaihto
// omat classit vastustajalle ja pelaajalle

// getterit ja setterit tekstuurin kanssa
public class RoomFight extends RoomParent {

    /* OLD COMMENT
    You don't use this variables in this class plus you already have all of these in your player
    class, since it is extending Animating class (aka inheriting it's variables).

    private Texture examplesheet;
    int frameSpeed;
    TextureRegion[][] tmp;
    TextureRegion[] exampleFrames;
    int COLS = 2;
    int ROWS = 1;
    int width = 100;
    int height = 100;*/

    private Player player;
    private Enemy enemy;

    RoomFight(MainGame game) {

        super(game);
        /* OLD COMMENT
        Handle all your animations within the Player class. Create this "exampleanimation.png" in
        MainGame class and use getters to retrieve it. If you don't know how, then just create it
        in your player class.

        examplesheet = new Texture("exampleanimation.png");
        createAnimation(examplesheet);*/

        player = new Player();
        enemy = new Enemy();
    }

    // OLD COMMENT I moved this method up, to see more clearly that this method belongs to RoomFight class
    @Override
    public void render(float delta) {

        super.render(delta);
        /* OLD COMMENT
        You already have this in Player class.

        stateTime += Gdx.graphics.getDeltaTime();*/


        /* OLD COMMENT
        You don't need these, since RoomParent already does these in super.render(delta);

        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/

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
    class Enemy extends Player {

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
            moving = createAnimation(img, 4, 1);
            startAnimation(moving, 10);

            orange = game.getOrangeTexture();
            green = game.getGreenTexture();

            greenmove = createAnimation(green, 2, 1);
            startAnimation(greenmove, 10);
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
                    orangemove = createAnimation(orange, 2, 1);
                    startAnimation(orangemove, 10);

                    /*
                    Make other button for enemy animations and create it in RoomFight class.
                     */

                    /*
                    You have already created this in enemy constructor.

                    enemy.redmove = createAnimation(enemy.red, 2, 1);*/
                    enemy.startAnimation(enemy.redmove, 10);

                    /*
                    You have already created this in enemy constructor.

                    enemy.yellowmove = createAnimation(enemy.yellow, 2, 1);
                     */
                }
            });
        }

        public void update() {

            stateTime += Gdx.graphics.getDeltaTime() / frameSpeed;
            /* OLD COMMENT
            Change the name "exampleAnimation" to "moving", since that is the only animation
            you have created so far.

            currentFrame = exampleAnimation.getKeyFrame(stateTime, true);*/

            //Great!
            if (isButtonClicked) {
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
            enemy.draw(batch);
        }
    }

    /* OLD COMMENT
    Don't create these again, use them from your Player class, since you are extending animating
    class, which already holds these methods.

    public TextureRegion[] toTextureArray(TextureRegion[][] tr) {
        int fc = this.COLS;
        int fr = this.ROWS;
        TextureRegion [] exampleFrames = new TextureRegion[fc * fr];

        int index = 0;
        for (int i = 0; i < fr; i++) {
            for (int j = 0; j < fc; j++) {
                exampleFrames[index++] = tr[i][j];
            }
        }

        return exampleFrames;
    }
    public Animation<TextureRegion> createAnimation(Texture examplesheet) {

        tmp = TextureRegion.split(examplesheet, examplesheet.getWidth() / COLS,
                examplesheet.getHeight() / ROWS);
        exampleFrames = toTextureArray(tmp);
        exampleAnimation = new Animation(1 / 60f, exampleFrames);

        return exampleAnimation;
    }*/

    /* OLD COMMENT
    You don't need these, also everything regarding animation should be in your Player class

    Animation<TextureRegion> exampleAnimation;
    TextureRegion currentFrame;
    float stateTime = 0.0f;*/


    /* OLD COMMENT
    Don't use this, since Player class already handles drawing using the Animating class's
    draw method.

    public void draw(SpriteBatch batch) { batch.draw(currentFrame, X, Y, width, height); }*/
}
