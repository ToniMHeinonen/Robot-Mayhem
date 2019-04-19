package fi.phstudios.robotmayhem;

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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;

public class Round {

    enum BodyData {
        SHIELD, BULLET, ENEMY
    }

    private Texture shieldTexture;
    private Texture bulletTexture;

    public static final float WORLD_WIDTH = 19.20f;
    public static final float WORLD_HEIGHT = 10.80f;

    private World world;
    private Body shieldBody;
    private Body enemyBody;
    private Body bulletBody;
    private Box2DDebugRenderer debugRenderer;

    private float shieldRadius;
    private float shieldSpeed;
    private float bulletRadius = 0.3f;
    private int bulletSpeed = 5;

    private double accumulator = 0;
    private float TIME_STEP = 1 / 60f;

    private Vector2 center;

    private float pos1x, pos2x, pos3x, pos4x, pos5x, pos6x, pos7x, pos8x, pos9x, pos10x, pos11x,
    pos12x, pos13x, pos14x, pos15x, pos16x;
    private float pos1y, pos2y, pos3y, pos4y, pos5y, pos6y, pos7y, pos8y, pos9y, pos10y, pos11y,
    pos12y, pos13y, pos14y, pos15y, pos16y;

    private Array<Body> shieldBodies = new Array<Body>();
    private Array<DistanceJointDef> distanceJointDefs = new Array<DistanceJointDef>();
    private Array<Body> bulletBodies = new Array<Body>();
    private Array<Body> bodiesToBeDestroyed = new Array<Body>();

    // Position of the shields.
    private float widthOfEnemy = WORLD_WIDTH - 3f;
    private float heightOfEnemy = 5f;

    // Starting position of the bullet.
    private float widthOfPlayer = 1f;
    private float heightOfPlayer = 5f;

    private FloatArray hackPosX;
    private FloatArray hackPosY;
    private int hackShieldAmount;
    private boolean hackFirstTry;
    private int pool1HackShieldAmount;
    private int pool2HackShieldAmount;
    private int pool3HackShieldAmount;
    private int pool;
    private int poolMult;

    private boolean doMove = true;
    private boolean bulletHitShield = false;
    private boolean checkNeighbor = false;

    private float hitPosStartX;
    private float hitPosEndX;
    private float hitPosStartY;
    private float hitPosEndY;

    private SpriteBatch batch;
    private OrthographicCamera camera;
    MainGame game;
    private Skin skin;
    private Stage stage;

    Round(MainGame game) {
        this.game = game;
        batch = game.getBatch();
        camera = game.getCamera();
        skin = game.getSkin();
        stage = game.getStage();
        createConstants();
        setSizeAndSpeed();
        createPositions();
        createShields();
        createJoints();
        createButtonShoot();
        createButtonSettings();
        createCollisionChecking();
    }

    public void update() {
        batch.setProjectionMatrix(camera.combined);
        debugRenderer.render(world, camera.combined);
        doPhysicsStep(Gdx.graphics.getDeltaTime());
        deleteBodies();
        batch.begin();
        drawBodies();
        batch.end();
        movement(shieldSpeed, center);
        checkNeighbor();
        checkMovement();
        checkBulletHitShield();
    }

    private void createConstants() {
        shieldTexture = new Texture("texture/hacking/shield.png");
        bulletTexture = new Texture("texture/hacking/bullet.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        world = new World(new Vector2(0, 0), true);
        enemyBody = world.createBody(getDefinitionOfEnemyBody());
        enemyBody.createFixture(getShieldFixtureDefinition());
        enemyBody.setUserData(BodyData.ENEMY);
        debugRenderer = new Box2DDebugRenderer();
        center = enemyBody.getPosition();
        this.hackPosX = game.getHackPosX();
        this.hackPosY = game.getHackPosY();
        this.hackFirstTry = game.getHackFirstTry();
        this.pool1HackShieldAmount = game.getPool1HackShieldAmount();
        this.pool2HackShieldAmount = game.getPool2HackShieldAmount();
        this.pool3HackShieldAmount = game.getPool3HackShieldAmount();
        //this.pool = game.getPool();
        //this.poolMult = game.getPoolMult();

        // Change these to test the effects of different pools/poolmultipliers.
        pool = 2;
        poolMult = 0;
    }

    private void setSizeAndSpeed() {
        FloatArray poolSpeeds = new FloatArray();
        poolSpeeds.add(8, 6, 4);

        FloatArray poolSizes = new FloatArray();
        poolSizes.add(1, 0.5f, 0.3f);

        float increasedSpeed = poolMult * 0.2f;

        shieldSpeed = poolSpeeds.get(pool - 1) + increasedSpeed;
        shieldRadius = poolSizes.get(pool - 1);
    }

    private void createPositions() {
        if (hackFirstTry) {
            hackPosX.clear();
            hackPosY.clear();
            if (pool == 1) {
                hackShieldAmount = pool1HackShieldAmount;
                pos1x = widthOfEnemy;
                pos1y = heightOfEnemy + 1;
                pos2x = widthOfEnemy + 1;
                pos2y = heightOfEnemy;
                pos3x = widthOfEnemy;
                pos3y = heightOfEnemy - 1;
                pos4x = widthOfEnemy - 1;
                pos4y = heightOfEnemy;
                hackPosX.addAll(pos1x, pos2x, pos3x, pos4x);
                hackPosY.addAll(pos1y, pos2y, pos3y, pos4y);
            }
            if (pool == 2) {
                hackShieldAmount = pool2HackShieldAmount;
                pos1x = widthOfEnemy + 1;
                pos1y = heightOfEnemy;
                pos2x = widthOfEnemy + 0.71f;
                pos2y = heightOfEnemy + 0.71f;
                pos3x = widthOfEnemy;
                pos3y = heightOfEnemy + 1;
                pos4x = widthOfEnemy - 0.71f;
                pos4y = heightOfEnemy + 0.71f;
                pos5x = widthOfEnemy - 1;
                pos5y = heightOfEnemy;
                pos6x = widthOfEnemy - 0.71f;
                pos6y = heightOfEnemy - 0.71f;
                pos7x = widthOfEnemy;
                pos7y = heightOfEnemy - 1;
                pos8x = widthOfEnemy + 0.71f;
                pos8y = heightOfEnemy - 0.71f;
                hackPosX.addAll(pos1x, pos2x, pos3x, pos4x, pos5x, pos6x, pos7x, pos8x);
                hackPosY.addAll(pos1y, pos2y, pos3y, pos4y, pos5y, pos6y, pos7y, pos8y);
            }
            if (pool == 3) {
                hackShieldAmount = pool3HackShieldAmount;
                pos1x = widthOfEnemy + 1;
                pos1y = heightOfEnemy;
                pos2x = widthOfEnemy + 0.92f;
                pos2y = heightOfEnemy + 0.38f;
                pos3x = widthOfEnemy + 0.71f;
                pos3y = heightOfEnemy + 0.71f;
                pos4x = widthOfEnemy + 0.38f;
                pos4y = heightOfEnemy + 0.92f;
                pos5x = widthOfEnemy;
                pos5y = heightOfEnemy + 1;
                pos6x = widthOfEnemy - 0.38f;
                pos6y = heightOfEnemy + 0.92f;
                pos7x = widthOfEnemy - 0.71f;
                pos7y = heightOfEnemy + 0.71f;
                pos8x = widthOfEnemy - 0.92f;
                pos8y = heightOfEnemy + 0.38f;
                pos9x = widthOfEnemy - 1;
                pos9y = heightOfEnemy;
                pos10x = widthOfEnemy - 0.92f;
                pos10y = heightOfEnemy - 0.38f;
                pos11x = widthOfEnemy - 0.71f;
                pos11y = heightOfEnemy - 0.71f;
                pos12x = widthOfEnemy - 0.38f;
                pos12y = heightOfEnemy - 0.92f;
                pos13x = widthOfEnemy;
                pos13y = heightOfEnemy - 1;
                pos14x = widthOfEnemy + 0.38f;
                pos14y = heightOfEnemy - 0.92f;
                pos15x = widthOfEnemy + 0.71f;
                pos15y = heightOfEnemy - 0.71f;
                pos16x = widthOfEnemy + 0.92f;
                pos16y = heightOfEnemy - 0.38f;
                hackPosX.addAll(pos1x, pos2x, pos3x, pos4x, pos5x, pos6x, pos7x, pos8x,
                        pos9x, pos10x, pos11x, pos12x, pos13x, pos14x, pos15x, pos16x);
                hackPosY.addAll(pos1y, pos2y, pos3y, pos4y, pos5y, pos6y, pos7y, pos8y,
                        pos9y, pos10y, pos11y, pos12y, pos13y, pos14y, pos15y, pos16y);
            }
            game.setHackPosX(hackPosX);
            game.setHackPosY(hackPosY);
        } else {
            hackPosX = game.getHackPosX();
            hackPosY = game.getHackPosY();
            hackShieldAmount = game.getHackShieldAmount();
        }
    }

    private void createShields() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.DynamicBody;
        for (int i = 0; i < hackShieldAmount; i++) {
            myBodyDef.position.set(hackPosX.get(i), hackPosY.get(i));
            shieldBody = world.createBody(myBodyDef);
            shieldBody.createFixture(getShieldFixtureDefinition());
            shieldBody.setUserData(BodyData.SHIELD);
            shieldBodies.add(shieldBody);
        }
    }

    private void createJoints() {
        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyB = enemyBody;
        distanceJointDef.length = 3f;
        distanceJointDef.frequencyHz = 3;
        distanceJointDef.dampingRatio = 0.1f;
        for (int i = 0; i < hackShieldAmount; i++) {
            distanceJointDef.bodyA = shieldBodies.get(i);
            distanceJointDefs.add(distanceJointDef);

            DistanceJoint distanceJoint = (DistanceJoint) world.createJoint(distanceJointDefs.get(i));
        }
    }

    private void createButtonShoot() {
            final TextButton buttonShoot = new TextButton("Shoot", skin);
            buttonShoot.setWidth(300f);
            buttonShoot.setHeight(100f);
            buttonShoot.setPosition(game.pixelWidth /2 - buttonShoot.getWidth() /2,
                    (game.pixelHeight/3) - buttonShoot.getHeight() *2);
            stage.addActor(buttonShoot);

            buttonShoot.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    fireBullet();
                }
            });
    }

    private void createButtonSettings() {
        final TextButton buttonShoot = new TextButton("Settings", skin);
        buttonShoot.setWidth(300f);
        buttonShoot.setHeight(100f);
        buttonShoot.setPosition(game.pixelWidth /2 - buttonShoot.getWidth() *2,
                (game.pixelHeight/3) - buttonShoot.getHeight() *2);
        stage.addActor(buttonShoot);

        buttonShoot.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.switchToRoomSettings();
            }
        });
    }

    private void fireBullet() {
        if (bulletBodies.isEmpty()) {
            bulletBody = world.createBody(getDefinitionOfBulletBody());
            bulletBody.setBullet(true);
            bulletBody.createFixture(getBulletFixtureDefinition());
            bulletBody.setUserData(BodyData.BULLET);
            bulletBody.applyLinearImpulse(new Vector2(bulletSpeed, 0), bulletBody.getWorldCenter(), true);
            bulletBodies.add(bulletBody);
        }
    }

    private void createCollisionChecking() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Body body1 = contact.getFixtureA().getBody();
                Body body2 = contact.getFixtureB().getBody();

                if (body1.getUserData() == BodyData.SHIELD && body2.getUserData() == BodyData.BULLET) {
                    collisionBulletShield(body1, body2);
                }

                if (body2.getUserData() == BodyData.SHIELD && body1.getUserData() == BodyData.BULLET) {
                    collisionBulletShield(body2, body1);
                }

                if (body1.getUserData() == BodyData.BULLET && body2.getUserData() == BodyData.ENEMY) {
                    collisionBulletEnemy(body1);
                }

                if (body2.getUserData() == BodyData.BULLET && body1.getUserData() == BodyData.ENEMY) {
                    collisionBulletEnemy(body2);
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

    //Delete bullets and shield.
    //The area of the collision can be changed depending on the pool?
    private void collisionBulletShield(Body body1, Body body2) {
        // body1 = shield
        // body2 = bullet
        hitPosStartX = body1.getPosition().x - 1.5f;
        hitPosEndX = body1.getPosition().x + 1.5f;
        hitPosStartY = body1.getPosition().y - 3;
        hitPosEndY = body1.getPosition().y + 3;
        System.out.println("hitPosStartX: " + hitPosStartX);
        System.out.println("hitPosEndX: " + hitPosEndX);
        System.out.println("hitPosStartY: " + hitPosStartY);
        System.out.println("hitPosEndY: " + hitPosEndY);
        bodiesToBeDestroyed.add(body2);
        bodiesToBeDestroyed.add(body1);

        checkNeighbor = true;
    }

    private void checkNeighbor() {
        if (checkNeighbor) {
            for (Body body : shieldBodies) {
                if (body.getUserData() != null) {
                    if (body.getPosition().x > hitPosStartX && body.getPosition().x < hitPosEndX &&
                            body.getPosition().y > hitPosStartY && body.getPosition().y < hitPosEndY) {
                        bodiesToBeDestroyed.add(body);
                    }
                }
            }
            doMove = false;
            bulletHitShield = true;
        }
    }

    private void collisionBulletEnemy(Body body) {
        doMove = false;
        game.setHackFirstTry(true);
        bodiesToBeDestroyed.add(body);
    }

    // When bullet has hit shield/enemy, shields stop moving.
    private void checkMovement() {
        if (!doMove) {
            for (Body body : shieldBodies) {
                if (body.getUserData() != null) {
                    Vector2 vel = body.getLinearVelocity();
                    vel.x = 0f;
                    vel.y = 0f;
                    body.setLinearVelocity(vel);
                }
            }
        }
    }

    // When bullet has hit a shield,
    // saves positions of shields and puts them in array.
    private void checkBulletHitShield() {
        int i = 0;
        if (bulletHitShield) {
            for (Body body : shieldBodies) {
                if (body.getUserData() != null) {
                    hackPosX.set(i, body.getPosition().x);
                    hackPosY.set(i, body.getPosition().y);
                    i++;
                }
            }
            game.setHackShieldAmount(i);
            game.setHackPosX(hackPosX);
            game.setHackPosY(hackPosY);
            game.setHackFirstTry(false);
            bulletHitShield = false;
        }
    }

    private FixtureDef getShieldFixtureDefinition() {
        FixtureDef shieldFixtureDef = new FixtureDef();
        shieldFixtureDef.density = 1;
        shieldFixtureDef.restitution = 0f;
        shieldFixtureDef.friction = 0f;
        CircleShape circleshape = new CircleShape();
        circleshape.setRadius(shieldRadius);
        shieldFixtureDef.shape = circleshape;
        return shieldFixtureDef;
    }

    private FixtureDef getBulletFixtureDefinition() {
        FixtureDef bulletFixtureDef = new FixtureDef();
        bulletFixtureDef.density = 1;
        bulletFixtureDef.restitution = 0f;
        bulletFixtureDef.friction = 0f;
        CircleShape circleshape = new CircleShape();
        circleshape.setRadius(bulletRadius);
        bulletFixtureDef.shape = circleshape;
        return bulletFixtureDef;
    }

    private BodyDef getDefinitionOfEnemyBody() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.StaticBody;
        myBodyDef.position.set(widthOfEnemy, heightOfEnemy);
        return myBodyDef;
    }

    private BodyDef getDefinitionOfBulletBody() {
        BodyDef bulletBodyDef = new BodyDef();
        bulletBodyDef.type = BodyDef.BodyType.DynamicBody;
        bulletBodyDef.position.set(widthOfPlayer, heightOfPlayer);
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

    private void movement(float shieldSpeed, Vector2 center) {
        for (Body body : shieldBodies) {
            if (body.getUserData() != null) {
                Vector2 radius = center.cpy().sub(body.getPosition());
                Vector2 force = radius.rotate90(1).nor().scl(shieldSpeed);
                body.setLinearVelocity(force.x, force.y);
            }
        }
    }

    private void deleteBodies() {
        for (Body body : bodiesToBeDestroyed) {
            world.destroyBody(body);
        }
        bodiesToBeDestroyed.clear();
    }

    private void drawBodies() {
        for (Body body : shieldBodies) {
            if (body.getUserData() != null) {
                batch.draw(shieldTexture,
                        body.getPosition().x - shieldRadius,
                        body.getPosition().y - shieldRadius,
                        shieldRadius, // originX
                        shieldRadius, // originY
                        shieldRadius * 2, // width
                        shieldRadius * 2, // height
                        1.0f, // scaleX
                        1.0f, // scaleY
                        body.getTransform().getRotation() * MathUtils.radiansToDegrees,
                        0, // Start drawing from x = 0
                        0, // Start drawing from y = 0
                        shieldTexture.getWidth(), // End drawing x
                        shieldTexture.getHeight(), // End drawing y
                        false, // flipX
                        false); // flipY
            }
        }
        for (Body body : bulletBodies) {
            if (body.getUserData() != null) {
                batch.draw(bulletTexture,
                        body.getPosition().x - bulletRadius,
                        body.getPosition().y - bulletRadius,
                        bulletRadius, // originX
                        bulletRadius, // originY
                        bulletRadius * 2, // width
                        bulletRadius * 2, // height
                        1.0f, // scaleX
                        1.0f, // scaleY
                        body.getTransform().getRotation() * MathUtils.radiansToDegrees,
                        0, // Start drawing from x = 0
                        0, // Start drawing from y = 0
                        bulletTexture.getWidth(), // End drawing x
                        bulletTexture.getHeight(), // End drawing y
                        false, // flipX
                        false); // flipY
            }
        }
    }

    public void dispose () {
        world.dispose();
    }
}