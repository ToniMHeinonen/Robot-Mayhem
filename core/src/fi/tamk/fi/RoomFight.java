package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import static fi.tamk.fi.MainGame.pixelHeight;
import static fi.tamk.fi.MainGame.pixelWidth;

import static javax.swing.text.html.HTML.Attribute.COLS;
import static javax.swing.text.html.HTML.Attribute.ROWS;

// I comment at least on things that are unclear to me.
// I comment out lines of code that aren't functional yet.
public class RoomFight extends RoomParent {

    Body body;
    public static World world;

    float startX = pixelWidth / 2;
    float startY = pixelHeight / 2;
    float delta;

    int frameSpeed;

    RoomFight(MainGame game) {

        super(game);

        world = new World(new Vector2(0, 0f), true);
        body = world.createBody(createDynamicBody(true));
        body.setUserData("player");
        examplesheet = new Texture("exampleanimation.png");
        body.setUserData(examplesheet);

        TextureRegion[][] tmp = TextureRegion.split(

                examplesheet,
                examplesheet.getWidth() / COLS,
                examplesheet.getHeight() / ROWS);

        TextureRegion [] frames = transfromTo1D(tmp);
        exampleAnimation = new Animation<TextureRegion>(1/10f, frames);
    }

    //startX = mainGame.worldWidth / game.TILES_AMOUNT_WIDTH * 2 - width/2;
    //startY = mainGame.worldHeight / 2;

    public BodyDef createDynamicBody(boolean notRotate) {
        // Body Definition
        BodyDef myBodyDef = new BodyDef();
        // It's a body that moves
        myBodyDef.type = BodyDef.BodyType.DynamicBody;
        // Initial position is centered up
        // This position is the CENTER of the shape!
        myBodyDef.position.set(startX, startY);
        myBodyDef.fixedRotation = notRotate;
        return myBodyDef;
    }

    private TextureRegion [] transfromTo1D(TextureRegion[][] tmp) {

        TextureRegion [] exampleFrames = new TextureRegion[COLS * ROWS];
        int index = 0;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                exampleFrames[index++] = tmp[i][j];
            }
        }

        return exampleFrames;
    }

    int COLS = 2;
    int ROWS = 1;

    Animation<TextureRegion> exampleAnimation;
    private Texture examplesheet;

    TextureRegion currentFrame;
    float stateTime = 0.0f;

    @Override
    public void render(float delta) {

        super.render(delta);
        stateTime += Gdx.graphics.getDeltaTime();

        currentFrame = exampleAnimation.getKeyFrame(stateTime, true);

        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        Texture badlogic = new Texture("badlogic.jpg");
        batch.draw(badlogic, 0, 0, pixelWidth, pixelHeight);

        update();

        batch.end();
    }

    public void update() {
        frameSpeed = 10;
        //Animation
        stateTime += Gdx.graphics.getDeltaTime() / frameSpeed;
        delta = Gdx.graphics.getDeltaTime();
        if (body.getLinearVelocity().len() > 0.05) {
            currentFrame = exampleAnimation.getKeyFrame(stateTime, true);
        }

        draw();
    }

    int width = 100;
    int height = 100;

    public void draw() {
        batch.draw(currentFrame,
                body.getPosition().x,
                body.getPosition().y,
                width,
                height);
    }
}
