package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

public class Round extends RoomParent {

    enum BodyData {
        SHIELD, BULLET;
    }

    SpriteBatch batch;
    Texture ball;
    public static final float WORLD_WIDTH = 19.20f;
    public static final float WORLD_HEIGHT = 10.80f;
    private OrthographicCamera camera;
    private World world;
    private Body shieldBody;
    // shieldBody is the body that should do circles
    private Body centerBody;
    // centerBody is a centerpoint of the circle
    private Body bulletBody;
    private Box2DDebugRenderer debugRenderer;
    private float radius = 0.5f;
    private double accumulator = 0;
    private float TIME_STEP = 1 / 60f;
    Vector2 center;
    float speed = 5;

    Array<Body> shieldBodies = new Array<Body>();
    Array<DistanceJointDef> distanceJointDefs = new Array<DistanceJointDef>();
    Array<Body> bulletBodies = new Array<Body>();
    Array<Body> bodiesToBeDestroyed = new Array<Body>();

    float widthOfEnemy = WORLD_WIDTH - 3f;
    float heightOfEnemy = 5f;

    FloatArray posX = new FloatArray();
    FloatArray posY = new FloatArray();

    int shieldAmount = 8;

    Round(MainGame game) {
        super(game);
        create();
        createPositions();
        createShields();
        createJoints();
        createButtonShoot();
        createCollisionChecking();
    }

    // Not funny.. :D
    // There has to be a better method for this.
    public void createPositions() {
        float pos1x = widthOfEnemy;
        float pos1y = heightOfEnemy + 1;
        float pos2x = widthOfEnemy + 1;
        float pos2y = heightOfEnemy;
        float pos3x = widthOfEnemy;
        float pos3y = heightOfEnemy - 1;
        float pos4x = widthOfEnemy - 1;
        float pos4y = heightOfEnemy;

        float pos5x = widthOfEnemy + 0.71f;
        float pos5y = heightOfEnemy + 0.71f;
        float pos6x = widthOfEnemy - 0.71f;
        float pos6y = heightOfEnemy + 0.71f;
        float pos7x = widthOfEnemy - 0.71f;
        float pos7y = heightOfEnemy - 0.71f;
        float pos8x = widthOfEnemy + 0.71f;
        float pos8y = heightOfEnemy - 0.71f;

        float pos9x = widthOfEnemy + 0.92f;
        float pos9y = heightOfEnemy + 0.38f;
        float pos10x = widthOfEnemy + 0.38f;
        float pos10y = heightOfEnemy + 0.92f;
        float pos11x = widthOfEnemy - 0.38f;
        float pos11y = heightOfEnemy + 0.92f;
        float pos12x = widthOfEnemy - 0.92f;
        float pos12y = heightOfEnemy + 0.38f;

        float pos13x = widthOfEnemy - 0.92f;
        float pos13y = heightOfEnemy - 0.38f;
        float pos14x = widthOfEnemy - 0.38f;
        float pos14y = heightOfEnemy - 0.92f;
        float pos15x = widthOfEnemy + 0.38f;
        float pos15y = heightOfEnemy - 0.92f;
        float pos16x = widthOfEnemy + 0.92f;
        float pos16y = heightOfEnemy - 0.38f;

        posX.addAll(pos1x, pos2x, pos3x, pos4x, pos5x, pos6x, pos7x, pos8x,
                pos9x, pos10x, pos11x, pos12x, pos13x, pos14x, pos15x, pos16x);
        posY.addAll(pos1y, pos2y, pos3y, pos4y, pos5y, pos6y, pos7y, pos8y,
                pos9y, pos10y, pos11y, pos12y, pos13y, pos14y, pos15y, pos16y);
    }

    public void createShields() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.DynamicBody;
        for (int i = 0; i < shieldAmount; i++) {
            myBodyDef.position.set(posX.get(i), posY.get(i));
            shieldBody = world.createBody(myBodyDef);
            shieldBody.createFixture(getFixtureDefinition());
            shieldBody.setUserData(BodyData.SHIELD);
            shieldBodies.add(shieldBody);
        }
    }

    public void createJoints() {
        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyB = centerBody;
        distanceJointDef.length = 3f;
        distanceJointDef.frequencyHz = 3;
        distanceJointDef.dampingRatio = 0.1f;
        for (int i = 0; i < shieldAmount; i++) {
            distanceJointDef.bodyA = shieldBodies.get(i);
            distanceJointDefs.add(distanceJointDef);

            DistanceJoint distanceJoint = (DistanceJoint) world.createJoint(distanceJointDefs.get(i));
        }
    }

    public void createButtonShoot() {
            final TextButton buttonFight = new TextButton("Shoot", skin);
            buttonFight.setWidth(300f);
            buttonFight.setHeight(100f);
            buttonFight.setPosition(game.pixelWidth /2 - buttonFight.getWidth() /2,
                    (game.pixelHeight/3) - buttonFight.getHeight() *2);
            stage.addActor(buttonFight);

            buttonFight.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    fireBullet();
                }
            });
    }

    public void fireBullet() {
        bulletBody = world.createBody(getDefinitionOfBulletBody());
        bulletBody.setBullet(true);
        bulletBody.createFixture(getFixtureDefinition());
        bulletBody.setUserData(BodyData.BULLET);
        bulletBody.applyLinearImpulse(new Vector2(6, 0), bulletBody.getWorldCenter(), true);
        bulletBodies.add(bulletBody);
    }

    public void checkBulletBoundaries() {
        float xPos = WORLD_WIDTH + 1;
        for (Body body : bulletBodies) {
            if (body.getPosition().x > xPos) {
                bodiesToBeDestroyed.add(body);
            }
        }
    }

    public void createCollisionChecking() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body body1 = contact.getFixtureA().getBody();
                Body body2 = contact.getFixtureB().getBody();
                System.out.println("collision");

                if (body1.getUserData() == BodyData.SHIELD && body2.getUserData() == BodyData.BULLET) {
                    bodiesToBeDestroyed.add(body2);
                }

                if (body2.getUserData() == BodyData.SHIELD && body1.getUserData() == BodyData.BULLET) {
                    bodiesToBeDestroyed.add(body1);
                }
            }
            @Override
            public void endContact(Contact contact) {
            }
            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }
            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    public void create() {
        batch = new SpriteBatch();
        ball = new Texture("test.png");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        world = new World(new Vector2(0, 0), true);
        //shieldBody = world.createBody(getDefinitionOfBody());
        //shieldBody.createFixture(getFixtureDefinition());
        centerBody = world.createBody(getDefinitionOfCenterBody());
        //centerBody.createFixture(getFixtureDefinition());
        debugRenderer = new Box2DDebugRenderer();
        center = centerBody.getPosition();
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

    /*
    private BodyDef getDefinitionOfBody() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.DynamicBody;
        myBodyDef.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        return myBodyDef;
    }
    */

    private BodyDef getDefinitionOfCenterBody() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.StaticBody;
        myBodyDef.position.set(widthOfEnemy, heightOfEnemy);
        return myBodyDef;
    }

    private BodyDef getDefinitionOfBulletBody() {
        BodyDef bulletBodyDef = new BodyDef();
        bulletBodyDef.type = BodyDef.BodyType.DynamicBody;
        bulletBodyDef.position.set(1f, 4f);
        return bulletBodyDef;
    }

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
        for (int i = 0; i < shieldAmount; i++) {
            Vector2 radius = center.cpy().sub(shieldBodies.get(i).getPosition());
            Vector2 force = radius.rotate90(1).nor().scl(speed);
            shieldBodies.get(i).setLinearVelocity(force.x, force.y);
        }
    }

    public void deleteBodies() {
        for (Body body : bodiesToBeDestroyed) {
            world.destroyBody(body);
        }
        bodiesToBeDestroyed.clear();
    }

    @Override
    public void render (float delta) {
        super.render(delta);
        batch.setProjectionMatrix(camera.combined);
        debugRenderer.render(world, camera.combined);
        doPhysicsStep(Gdx.graphics.getDeltaTime());
        batch.begin();
        for (Body body : shieldBodies) {
            if (body.getUserData() != null) {
                batch.draw(ball,
                        body.getPosition().x - radius,
                        body.getPosition().y - radius,
                        radius, // originX
                        radius, // originY
                        radius * 2, // width
                        radius * 2, // height
                        1.0f, // scaleX
                        1.0f, // scaleY
                        body.getTransform().getRotation() * MathUtils.radiansToDegrees,
                        0, // Start drawing from x = 0
                        0, // Start drawing from y = 0
                        ball.getWidth(), // End drawing x
                        ball.getHeight(), // End drawing y
                        false, // flipX
                        false); // flipY
            }
        }
        for (Body body : bulletBodies) {
            if (body.getUserData() != null) {
                batch.draw(ball,
                        body.getPosition().x - radius,
                        body.getPosition().y - radius,
                        radius, // originX
                        radius, // originY
                        radius * 2, // width
                        radius * 2, // height
                        1.0f, // scaleX
                        1.0f, // scaleY
                        body.getTransform().getRotation() * MathUtils.radiansToDegrees,
                        0, // Start drawing from x = 0
                        0, // Start drawing from y = 0
                        ball.getWidth(), // End drawing x
                        ball.getHeight(), // End drawing y
                        false, // flipX
                        false); // flipY
            }
        }
        batch.end();
        movement(speed, center);
        //checkBulletBoundaries();
        deleteBodies();
    }

    @Override
    public void dispose () {
        batch.dispose();
        world.dispose();
    }
}
