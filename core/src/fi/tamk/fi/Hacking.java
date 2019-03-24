package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.security.Key;

import sun.applet.Main;

import static com.badlogic.gdx.Input.Keys.M;
import static fi.tamk.fi.Round.WORLD_HEIGHT;
import static fi.tamk.fi.Round.WORLD_WIDTH;
import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;

/*
Mistakes I found:
- You can't change bodies position by using body.getPosition.set(x,y), you have to
  use body.setTransform(x,y,angle).
- Don't create new shield 60 times a second and delete it at the same frame, it slows down the game
  and it is unnecessary. Just create 1 shield and move that.
- Please use better variable names than "asdf" and "ghjkl", I have no idea what those variables do :D

Next just try to make 1 shield, which speed you can alter by a simple "speed" variable. If you can
get 1 shield working correctly, then we can add the rest of the needed shields later.
 */

public class Hacking extends RoomParent {

    SpriteBatch batch;
    Texture ball;
    public static final float WORLD_WIDTH = 19.20f;
    public static final float WORLD_HEIGHT = 10.80f;
    private OrthographicCamera camera;
    private World world;
    private Body ballBody;
    // ballBody is the body that should do circles
    private Body centerBody;
    // centerBody is just a centerpoint of the circle
    private Box2DDebugRenderer debugRenderer;
    private float radius = 0.5f;
    Vector2 center;
    float speed = 500;
    float length;

    Hacking(MainGame game) {
        super(game);
        create();
    }

    public void create() {
        batch = new SpriteBatch();
        ball = new Texture("test.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        world = new World(new Vector2(0, 0), true);

        ballBody = world.createBody(getDefinitionOfBody());
        centerBody = world.createBody(getDefinitionOfCenterBody());

        debugRenderer = new Box2DDebugRenderer();

        center = centerBody.getPosition();
        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyA = ballBody;
        distanceJointDef.bodyB = centerBody;
        distanceJointDef.length = 3;
        distanceJointDef.frequencyHz = 3;
        distanceJointDef.dampingRatio = 0.1f;

        DistanceJoint distanceJoint = (DistanceJoint) world.createJoint(distanceJointDef);
        length = distanceJoint.getLength();
    }

    private FixtureDef getFixtureDefinition() {
        FixtureDef playerFixtureDef = new FixtureDef();
        playerFixtureDef.density = 1;
        playerFixtureDef.restitution = 1.0f;
        playerFixtureDef.friction = 0.5f;
        CircleShape circleshape = new CircleShape();
        circleshape.setRadius(0.5f);
        playerFixtureDef.shape = circleshape;
        return playerFixtureDef;
    }

    private BodyDef getDefinitionOfBody() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.DynamicBody;
        myBodyDef.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        return myBodyDef;
    }

    private BodyDef getDefinitionOfCenterBody() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.StaticBody;
        myBodyDef.position.set(WORLD_WIDTH / 2.5f, WORLD_HEIGHT / 2.5f);
        return myBodyDef;
    }

    private double accumulator = 0;
    private float TIME_STEP = 1 / 60f;
    private void doPhysicsStep(float deltaTime) {
        float frameTime = deltaTime;
        if(deltaTime > 1 / 4f) {
            frameTime = 1 / 4f;
        }
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, 8, 3);
            accumulator -= TIME_STEP;
        }
    }

    public void movement(float speed, Vector2 center) {

        Vector2 radius = center.cpy().sub(ballBody.getPosition());
        // orig: Vector2 force = radius.rotate90(1).nor().scl(speed);
        Vector2 force = radius.rotate90(1).nor().scl(speed);

        // Below current last tried new one v
        //Vector2 force = radius.rotate90(1).nor().scl(ballBody.getPosition().x, ballBody.getPosition().y);

        //slower(speed - 2); <-- Not yes useful.

        ballBody.setLinearVelocity(force.x, force.y);
    }

    int slow = 0;
    int maxShields = 2;

    @Override
    public void render (float delta) {

        super.render(delta);

        if (!game.haveWeChangedTheRoom) {

            batch.setProjectionMatrix(camera.combined);
            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            debugRenderer.render(world, camera.combined);
            doPhysicsStep(Gdx.graphics.getDeltaTime());
            batch.begin();

            batch.draw(ball,
                    ballBody.getPosition().x - radius,
                    ballBody.getPosition().y - radius,
                    radius, // originX
                    radius, // originY
                    radius * 2, // width
                    radius * 2, // height
                    1.0f, // scaleX
                    1.0f, // scaleY
                    ballBody.getTransform().getRotation() * MathUtils.radiansToDegrees,
                    0, // Start drawing from x = 0
                    0, // Start drawing from y = 0
                    ball.getWidth(), // End drawing x
                    ball.getHeight(), // End drawing y
                    false, // flipX
                    false); // flipY
            //centerBody.setTransform(WORLD_WIDTH / 2.5f, WORLD_HEIGHT / 2.5f, centerBody.getAngle());
            batch.end();
            movement(speed, center);
        }
    }

    // Not useful yet.
    public void slower(float howSlowToGo) {

        for (int counter = 0; counter < howSlowToGo; counter++) {

            speed--;
        }
    }

    @Override
    public void dispose () {
        batch.dispose();
    }
}