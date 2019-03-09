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
import com.badlogic.gdx.scenes.scene2d.InputListener;

import static com.badlogic.gdx.Input.Keys.X;
import static com.badlogic.gdx.Input.Keys.Y;
import static fi.tamk.fi.MainGame.pixelHeight;
import static fi.tamk.fi.MainGame.pixelWidth;

import static javax.swing.text.html.HTML.Attribute.COLS;
import static javax.swing.text.html.HTML.Attribute.ROWS;

// I comment at least on things that are unclear to me.
// I comment out lines of code that aren't functional yet.

// Modify picture used, x & y (placement), frame cols & frame rows, framespeed, update
// create texture in maingame
// game.getTexturename name;
// update: pelkkä statetime += ja currentframe ja drawbatch

// createAnimation (käytetään konstruktorissa), startAnimation

// Tee: idlaus-animaatio ja animaation vaihto
// omat classit vastustajalle ja pelaajalle

// getterit ja setterit tekstuurin kanssa
public class RoomFight extends RoomParent {

    /*
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

    RoomFight(MainGame game) {

        super(game);
        //Handle all your animations within the Player class
        /*examplesheet = new Texture("exampleanimation.png");
        createAnimation(examplesheet);*/

        player = new Player();
    }

    // I also moved this method up, to see more clearly that this method belongs to RoomFight class
    @Override
    public void render(float delta) {

        super.render(delta);
        /*
        You already have this in Player class.

        stateTime += Gdx.graphics.getDeltaTime();*/


        /*
        You don't need these, since RoomParent already does these in super.render(delta);

        batch.setProjectionMatrix(camera.combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);*/

        batch.begin();
        player.update();
        batch.end();
    }

    public class Player extends Animating {
        private Texture img;
        private Animation<TextureRegion> moving;

        Player() {
            img = game.getGamePlayer();
            X = 100;
            Y = game.pixelHeight/2;

            //Create necessary animations and start the correct one
            moving = createAnimation(img, 4, 1);
            startAnimation(moving, 10);
        }

        public void update() {

            stateTime += Gdx.graphics.getDeltaTime() / frameSpeed;
            /*
            Change the name "exampleAnimation" to "moving", since that is the only animation
            you have created so far.

            currentFrame = exampleAnimation.getKeyFrame(stateTime, true);*/
            draw(batch);
        }
    }

    /*
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

    /*
    You don't need these, also everything regarding animation should be in your Player class

    Animation<TextureRegion> exampleAnimation;
    TextureRegion currentFrame;
    float stateTime = 0.0f;*/


    /*
    Don't use this, since Player class already handles drawing using the Animating class's
    draw method.

    public void draw(SpriteBatch batch) { batch.draw(currentFrame, X, Y, width, height); }*/
}
