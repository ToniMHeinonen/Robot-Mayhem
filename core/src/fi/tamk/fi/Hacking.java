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

public class Hacking extends RoomParent{

    MainGame game;
    SpriteBatch batch;

    TextureAtlas testButtonAtlas;
    Skin testSkin;

    private World world;

    private Box2DDebugRenderer debugRenderer;

    Body shieldBody;

    Texture test = new Texture(Gdx.files.internal("badlogic.jpg"));
    Texture texture = new Texture(Gdx.files.internal("test.png"));

    //private int aliveTimer = 560;
    protected float width = 1000;
    protected float height = 1000;
    private float shieldRadius = 200;

    /* Not sure how to change these (x & y) for the creation of a new shield. Tried modifying
    methods but it ended up making the hacking room completely blank so I undid it all.*/
    float x = 400;
    float y = 200;

    float a = 0;

    private Array<Body> bodiesToBeDestroyed;

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

        bodiesToBeDestroyed = new Array<Body>();
        createConstants();
        create();
    }

    public void create() {

        batch = new SpriteBatch();

        world = new World(new Vector2(0, -0f), true);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        debugRenderer = new Box2DDebugRenderer();

        createShield();

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {

                System.out.println("Contact detected!");
            }

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

    // int plsChill = 0; possibly useless

    public void pleaseWork() {

        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        /* Don't create shield 60 times in a second
        createShield();*/
        //createTest();

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
                        /* Don't remove the body
                        bodiesToBeDestroyed.add(body);*/
                        /* I moved this to a better spot in render
                        checkBodiesToRemove();*/
                }

                if (body.getUserData() == test) {

                    batch.draw(texture,
                            body.getPosition().x,
                            body.getPosition().y,
                            x,
                            y,
                            x,
                            y,
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

    private void checkBodiesToRemove() {
        // Destroy needed bodies
        for (int i = 0; i < bodiesToBeDestroyed.size; i++) {
            Body body = bodiesToBeDestroyed.get(i);
            world.destroyBody(body);
            bodiesToBeDestroyed.removeIndex(i);
            i--;
        }
    }

    int asdf = 0;
    boolean ghjkl = false;
    public void render(float delta) {

        super.render(delta);
        pleaseWork();
        //moveShield();

        // Slows circling slightly.
        if (asdf >= 0 && asdf < 1 && ghjkl == false) {
            moveShield();
            //System.out.println(asdf);
            asdf++;
        } else {

            ghjkl = true;
            //System.out.println(asdf);
            asdf--;
            if (asdf == 0) {

                ghjkl = false;
            }
        }
        checkBodiesToRemove();
    }

    public void createShield() {

        shieldBody = world.createBody(getDefinitionOfBody());
        shieldBody.createFixture(getFixtureDefinition());
        shieldBody.getPosition();
        shieldBody.setUserData(texture);

        shieldBody.applyLinearImpulse(new Vector2(0.0f, 0.0f),
                shieldBody.getWorldCenter(),
                true);
    }

    public void createTest() {

        Body testBody;

        testBody = world.createBody(getDefinitionOfBody());
        testBody.createFixture(getFixtureDefinition());
        testBody.getPosition();
        testBody.setUserData(test);

        testBody.applyLinearImpulse(new Vector2(0.0f, 0.0f),
                testBody.getWorldCenter(),
                true);
    }

    // boolean midPointReached = false; <-- possibly useless

    public void moveShield() {

        if (shieldBody.getPosition().y <= shieldRadius + shieldBody.getPosition().y) {

            /*shieldBody.getPosition().set(x = (float) Math.sqrt(Math.pow(y, 2) + Math.pow(shieldRadius, 2)), y);

            y++;
            x = (float) (Math.sqrt(Math.pow(y, 2) + Math.pow(shieldRadius, 2)));*/

            //for (int counter = 0; counter < 10; counter++){

                shieldBody.setTransform(x = (float) (shieldBody.getPosition().x + shieldRadius * cos(a)),
                        y = (float) (shieldBody.getPosition().y + shieldRadius * sin(a)), 0);

                a++;
                //System.out.println(a);
            //}

        }
    }
}
