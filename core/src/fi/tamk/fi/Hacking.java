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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.security.Key;

import sun.applet.Main;

import static com.badlogic.gdx.Input.Keys.M;
import static java.lang.Math.cos;
import static java.lang.StrictMath.sin;

public class Hacking extends RoomParent{

    MainGame game;
    SpriteBatch batch;

    TextureAtlas testButtonAtlas;
    Skin testSkin;

    private World world;

    private Box2DDebugRenderer debugRenderer;

    Body shieldBody;

    Texture testEnemy = new Texture(Gdx.files.internal("badlogic.jpg"));
    Texture texture = new Texture(Gdx.files.internal("test.png"));

    //private int aliveTimer = 560;
    protected float width = 1000;
    protected float height = 1000;
    private float shieldRadius = 400;

    /*
    I commented one thing that I saw was wrong, otherwise I don't remember that well how the
    bodies and worlds works, so if you get really stuck with this,m then I will have a better look
     */

    float x = 400;
    float y = 200;

    float a = 0;

    protected BodyDef getDefinitionOfBody() {

        BodyDef shieldBody = new BodyDef();

        // It's a body that moves
        shieldBody.type = BodyDef.BodyType.DynamicBody;

        shieldBody.position.set(x, y);

        System.out.println(x + " " + y);

        return shieldBody;
    }

    private FixtureDef getFixtureDefinition() {
        FixtureDef playerFixtureDef = new FixtureDef();

        // Mass per square meter (kg^m2)
        playerFixtureDef.density = 1.5f;

        // How bouncy object? Very bouncy [0,1]
        playerFixtureDef.restitution = 1.0f;

        // How slipper object? [0,1]
        playerFixtureDef.friction = 0.5f;


        CircleShape circleshape = new CircleShape();
        circleshape.setRadius(50f);

        playerFixtureDef.shape = circleshape;
        return playerFixtureDef;
    }

    Hacking(MainGame game) {

        super(game);

        createConstants();
        create();
    }

    public void create() {

        batch = new SpriteBatch();

        world = new World(new Vector2(0, -0f), true);
        /*shieldBody = world.createBody(getDefinitionOfBody());
        shieldBody.createFixture(getFixtureDefinition());
        shieldBody.getPosition();
        shieldBody.setUserData(texture);

        shieldBody.applyLinearImpulse(new Vector2(0.0f, 0.0f),
                shieldBody.getWorldCenter(),
                true);*/

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        debugRenderer = new Box2DDebugRenderer();

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) { }

            @Override
            public void endContact(Contact contact) { }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) { }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) { }
        });
    }

    public void createConstants() {
        testButtonAtlas = new TextureAtlas("testbuttons/testbuttons.pack");
        testSkin = new Skin(testButtonAtlas);
    }

    Array<Body> shields = new Array<Body>();

    // Doesn't work. :(
    public void pleaseWork() {

        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //createShield();

        //if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {

            createShield();
        //}

        world.getBodies(shields);

        debugRenderer.render(world, camera.combined);

        batch.begin();

        for (Body body : shields) {
            //System.out.println(body.getUserData());
            if(body.getUserData() != null) {

                if (body.getUserData() == texture) {

                    float radius = ((CircleShape) body.getFixtureList().get(0).getShape()).getRadius();
                    Texture texture = (Texture) body.getUserData();

                    batch.draw(texture,
                            body.getPosition().x - radius,
                            body.getPosition().y - radius,
                            radius,
                            radius,
                            radius * 2,
                            radius * 2,
                            1.0f,
                            1.0f,
                            body.getTransform().getRotation() * MathUtils.radiansToDegrees,
                            0,
                            0,
                            texture.getWidth(),
                            texture.getHeight(),
                            false,
                            false);
                }
            }
        }
        batch.end();
    }

    public void render(float delta) {

        super.render(delta);
        /* This causes it to create new world 60 times in a second, you should move this to the
         constructor
        create(); */
        pleaseWork();
        moveShield();
        /*
        Just to make everything clear, you do know that anything that is in the render() method
        gets called 60 times in a second? So if you only want to for example create something once,
        then don't put it in here. Contructor runs only once when you switch to this room.

         My comment: I do know that, but the code wasn't working, so I got desperate and decided
         that this was an acceptable temporary solution. */
    }

    public void createShield() {

        shieldBody = world.createBody(getDefinitionOfBody());
        shieldBody.createFixture(getFixtureDefinition());
        shieldBody.getPosition();
        shieldBody.setUserData(texture);

        shieldBody.applyLinearImpulse(new Vector2(0.0f, 0.0f),
                shieldBody.getWorldCenter(),
                true);

        /*BodyDef myBodyDef = new BodyDef();

        // It's a body that moves
        myBodyDef.type = BodyDef.BodyType.DynamicBody;

        FixtureDef shieldFixtureDef = new FixtureDef();

        // Mass per square meter (kg^m2)
        shieldFixtureDef.density = 9;

        // How bouncy object? Very bouncy [0,1]
        shieldFixtureDef.restitution = 1.0f;

        // How slipper object? [0,1]
        shieldFixtureDef.friction = 0.5f;

        // Create circle shape.�
        CircleShape circleshape = new CircleShape();
        circleshape.setRadius(100f);

        // Add the shape to the fixture
        shieldFixtureDef.shape = circleshape;

        Body shield = world.createBody(myBodyDef);

        shield.createFixture(getFixtureDefinition());
        shield.setUserData(texture);

        shield.applyLinearImpulse(new Vector2(0.0f, 0.0f),
                shield.getWorldCenter(),
                true);*/
    }

    // boolean midPointReached = false; <-- possibly useless

    public void moveShield() {

        if (shieldBody.getPosition().y <= shieldRadius + shieldBody.getPosition().y) {

            //shieldBody.getPosition().set(x = (float) Math.sqrt(Math.pow(y, 2) + Math.pow(shieldRadius, 2)), y);

            // Not sure why using radians do not work. Or my earlier lines of code.
            shieldBody.getPosition().set( x = (float) (shieldBody.getPosition().x + shieldRadius * cos(a)),
                    y = (float) (shieldBody.getPosition().y  + shieldRadius * sin(a)));

                a++;
        }
    }
}
