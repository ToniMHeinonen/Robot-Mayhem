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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Timer;

import static fi.tamk.fi.Hacking.BodyData.INNERSHIELD;

public class Hacking {

    enum BodyData {
        SHIELD, BULLET, ENEMY, INNERSHIELD
    }

    // Inner circle
    private Array<Body> innerBodyShields = new Array<Body>();
    private Body innerBody;
    private int innerHackShieldAmount;
    private int pool3InnerHackShieldAmount;
    private FloatArray innerPosX;
    private FloatArray innerPosY;
    private float innerShieldRadius = 0.4f;
    private Array<DistanceJointDef> innerDistanceJointDefs = new Array<DistanceJointDef>();
    private float ipos1x, ipos1y, ipos2x, ipos2y, ipos3x, ipos3y, ipos4x, ipos4y, ipos5x, ipos5y,
    ipos6x, ipos6y, ipos7x, ipos7y;
    private boolean checkInnerNeighbor = false;

    private Texture shieldTexture;
    private Texture bulletTexture;
    private Texture innerShieldTexture;

    private final float WORLD_WIDTH = 19.20f;
    private final float WORLD_HEIGHT = 10.80f;
    private final float gridSize = WORLD_WIDTH / 16f;

    private World world;
    private Body shieldBody;
    private Body enemyBody;
    private Body bulletBody;
    private Box2DDebugRenderer debugRenderer;

    private float shieldRadius;
    private float shieldSpeed;
    private float shieldLength;
    // Size of bullet's hitbox
    private float bulletHitboxRadius = 0.3f;
    // Size of bullet's texture
    private float bulletRadius = 1f;
    private float bulletSpeed = 4.2f;

    private double accumulator = 0;
    private float TIME_STEP = 1 / 60f;

    private Vector2 center;

    private float pos1x, pos2x, pos3x, pos4x, pos5x, pos6x, pos7x, pos8x, pos9x, pos10x, pos11x,
            pos12x, pos13x, pos14x, pos15x;
    private float pos1y, pos2y, pos3y, pos4y, pos5y, pos6y, pos7y, pos8y, pos9y, pos10y, pos11y,
            pos12y, pos13y, pos14y, pos15y;

    private Array<Body> shieldBodies = new Array<Body>();
    private Array<DistanceJointDef> distanceJointDefs = new Array<DistanceJointDef>();
    private Array<Body> bulletBodies = new Array<Body>();
    private Array<Body> bodiesToBeDestroyed = new Array<Body>();

    // Position of the shields.
    private float widthOfEnemy = WORLD_WIDTH - gridSize*3.5f;
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

    private boolean bulletHitShield = false;
    private boolean checkNeighbor = false;
    private boolean bulletMissedEnemy, bulletHitEnemy; // RoomFight uses these

    private float hitPosX;
    private float hitPosStartY;
    private float hitPosEndY;

    private FloatArray poolHitAreaX;
    private FloatArray poolHitAreaY;

    private SpriteBatch batch;
    private OrthographicCamera hackingCamera;
    private MainGame game;
    private Files files;
    private Skin testSkin;
    private Stage stage;

    private int destroyedNeighbors = 0;

    Hacking(MainGame game, boolean firstTry) {
        this.game = game;
        this.hackFirstTry = firstTry;
        files = game.getFiles();
        batch = game.getBatch();
        hackingCamera = new OrthographicCamera();
        testSkin = game.getTestSkin();
        stage = game.getStage();
        createConstants();
        setShieldAttributes();
        createPositions();
        createShields();
        createInnerShields();
        createJoints();
        createButtonShoot();
        createCollisionChecking();
    }

    public void update() {
        batch.setProjectionMatrix(hackingCamera.combined);
        doPhysicsStep(Gdx.graphics.getDeltaTime());
        //debugRenderer.render(world, hackingCamera.combined);
        deleteBodies();
        batch.begin();
        drawBodies();
        batch.end();
        movement(shieldSpeed, center);
        checkBulletHitShield();
        checkNeighborMethod();
    }

    private void createConstants() {
        //this.pool = game.getPool();
        //this.poolMult = game.getPoolMult();

        // Change these to test the effects of different pools/poolmultipliers.
        pool = 1;
        poolMult = 0;
        if (pool == 1) {
            shieldTexture = new Texture("texture/hacking/shield_medium.png");
        } else {
            shieldTexture = new Texture("texture/hacking/shield_small.png");
        }
        innerShieldTexture = new Texture("texture/hacking/shield_medium.png");
        bulletTexture = new Texture("texture/hacking/bullet.png");
        hackingCamera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        world = new World(new Vector2(0, 0), true);
        enemyBody = world.createBody(getDefinitionOfEnemyBody());
        enemyBody.createFixture(getShieldFixtureDefinition(0.5f));
        enemyBody.setUserData(BodyData.ENEMY);
        debugRenderer = new Box2DDebugRenderer();
        center = enemyBody.getPosition();
        this.innerPosX = game.getInnerPosX();
        this.innerPosY = game.getInnerPosY();
        this.hackPosX = game.getHackPosX();
        this.hackPosY = game.getHackPosY();
        this.pool1HackShieldAmount = game.getPool1HackShieldAmount();
        this.pool2HackShieldAmount = game.getPool2HackShieldAmount();
        this.pool3HackShieldAmount = game.getPool3HackShieldAmount();
        this.pool3InnerHackShieldAmount = game.getPool3InnerHackShieldAmount();
    }

    /*
    poolSpeeds & poolSizes = Different shieldspeed and shieldsize in different pool
    increasedSpeed = Added speed to shield depending on the poolmultiplier
    shieldLength = The length between shield and enemy
    poolHitArea = The collision area, when bullet hits shield. For example, in pool3
    it checks if there are shields 1.1f to the right and 1.7f up&down of the collision area.
    Poorly explained, but maybe you get the idea. :D
     */
    private void setShieldAttributes() {
        FloatArray poolSpeeds = new FloatArray();
        poolSpeeds.add(4, 4, 4);

        FloatArray poolSizes = new FloatArray();
        poolSizes.add(0.5f, 0.3f, 0.3f);

        float increasedSpeed = poolMult * 0.2f;

        shieldSpeed = poolSpeeds.get(pool - 1) + increasedSpeed;
        shieldRadius = poolSizes.get(pool - 1);
        //shieldLength = 3f;
        if (pool == 1) {
            shieldLength = 1.9f;
        } else {
            shieldLength = 2.8f;
        }

        // These may have to be adjusted a bit, if we are going to change shieldLength.
        poolHitAreaX = new FloatArray();
        poolHitAreaY = new FloatArray();
        poolHitAreaX.add(2f, 1.1f, 1.1f);
        poolHitAreaY.add(3f, 1.7f, 1.7f);
    }

    /*
    Creates positions to the shields. If player is trying to hack for the first time, it
    deletes the old positions and put new positions to them depending on pool.
    If player has failed to hack before, it loads the old positions and amount of shields.
     */
    private void createPositions() {
        if (hackFirstTry) {
            hackPosX.clear();
            hackPosY.clear();
            if (pool == 1) {
                hackShieldAmount = pool1HackShieldAmount;
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
            if (pool == 2 || pool == 3) {
                hackShieldAmount = pool2HackShieldAmount;
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
                hackPosX.addAll(pos1x, pos2x, pos3x, pos4x, pos5x, pos6x, pos7x, pos8x,
                        pos9x, pos10x, pos11x, pos12x, pos13x, pos14x, pos15x);
                hackPosY.addAll(pos1y, pos2y, pos3y, pos4y, pos5y, pos6y, pos7y, pos8y,
                        pos9y, pos10y, pos11y, pos12y, pos13y, pos14y, pos15y);
            }
            if (pool == 3) {
                innerPosX.clear();
                innerPosY.clear();
                innerHackShieldAmount = pool3InnerHackShieldAmount;
                ipos1x = widthOfEnemy + 0.5f;
                ipos1y = heightOfEnemy;
                ipos2x = widthOfEnemy + 0.21f;
                ipos2y = heightOfEnemy + 0.21f;
                ipos3x = widthOfEnemy;
                ipos3y = heightOfEnemy + 0.5f;
                ipos4x = widthOfEnemy - 0.21f;
                ipos4y = heightOfEnemy + 0.21f;
                ipos5x = widthOfEnemy - 0.5f;
                ipos5y = heightOfEnemy;
                ipos6x = widthOfEnemy - 0.21f;
                ipos6y = heightOfEnemy - 0.21f;
                ipos7x = widthOfEnemy;
                ipos7y = heightOfEnemy - 0.5f;
                innerPosX.addAll(ipos1x, ipos2x, ipos3x, ipos4x, ipos5x, ipos6x, ipos7x);
                innerPosY.addAll(ipos1y, ipos2y, ipos3y, ipos4y, ipos5y, ipos6y, ipos7y);
                game.setInnerPosX(innerPosX);
                game.setInnerPosY(innerPosY);
            }
            game.setHackPosX(hackPosX);
            game.setHackPosY(hackPosY);
        } else {
            hackPosX = game.getHackPosX();
            hackPosY = game.getHackPosY();
            hackShieldAmount = game.getHackShieldAmount();
            if (pool == 3) {
                innerPosX = game.getInnerPosX();
                innerPosY = game.getInnerPosY();
                innerHackShieldAmount = game.getInnerHackShieldAmount();
            }
        }
    }

    /*
    Creates the shields.
     */
    private void createShields() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.DynamicBody;
        for (int i = 0; i < hackShieldAmount; i++) {
            myBodyDef.position.set(hackPosX.get(i), hackPosY.get(i));
            shieldBody = world.createBody(myBodyDef);
            shieldBody.createFixture(getShieldFixtureDefinition(shieldRadius));
            shieldBody.setUserData(BodyData.SHIELD);
            shieldBodies.add(shieldBody);
        }
    }

    private void createInnerShields() {
        if (pool == 3) {
            BodyDef innerBodyDef = new BodyDef();
            innerBodyDef.type = BodyDef.BodyType.DynamicBody;
            for (int i = 0; i < innerHackShieldAmount; i++) {
                innerBodyDef.position.set(innerPosX.get(i), innerPosY.get(i));
                innerBody = world.createBody(innerBodyDef);
                innerBody.createFixture(getShieldFixtureDefinition(innerShieldRadius));
                innerBody.setUserData(INNERSHIELD);
                innerBodyShields.add(innerBody);
            }
        }
    }

    /*
    Creates the joints between shields and enemy.
     */
    private void createJoints() {
        DistanceJointDef distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyB = enemyBody;
        distanceJointDef.length = shieldLength;
        distanceJointDef.frequencyHz = 3;
        distanceJointDef.dampingRatio = 0.1f;
        for (int i = 0; i < hackShieldAmount; i++) {
            distanceJointDef.bodyA = shieldBodies.get(i);
            distanceJointDefs.add(distanceJointDef);

            DistanceJoint distanceJoint = (DistanceJoint) world.createJoint(distanceJointDefs.get(i));
        }

        if (pool == 3) {
            DistanceJointDef innerDistanceJointDef = new DistanceJointDef();
            innerDistanceJointDef.bodyB = enemyBody;
            innerDistanceJointDef.length = 1.9f;
            innerDistanceJointDef.frequencyHz = 3;
            innerDistanceJointDef.dampingRatio = 0.1f;
            for (int i = 0; i < innerHackShieldAmount; i++) {
                innerDistanceJointDef.bodyA = innerBodyShields.get(i);
                innerDistanceJointDefs.add(innerDistanceJointDef);

                DistanceJoint distanceJoint = (DistanceJoint) world.createJoint(innerDistanceJointDefs.get(i));
            }
        }
    }

    private void createButtonShoot() {
        Drawable normal = testSkin.getDrawable("button_SHOCK");
        Drawable clicked = testSkin.getDrawable("button_clicked");
        final ImageButton btn = new ImageButton(normal, clicked);
        btn.setPosition(game.pixelWidth /2 - btn.getWidth() /2,
                0);
        stage.addActor(btn);

        btn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                fireBullet();
                btn.remove();
            }
        });
    }

    /*
    Allows player to shoot only once.
     */
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

    /*
    Checks the collisions:
    bullet & shield
    bullet & enemy
     */
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

                if (body1.getUserData() == BodyData.INNERSHIELD && body2.getUserData() == BodyData.BULLET) {
                    collisionBulletInnerShield(body1, body2);
                }

                if (body2.getUserData() == BodyData.INNERSHIELD && body1.getUserData() == BodyData.BULLET) {
                    collisionBulletInnerShield(body2, body1);
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

    /*
    hitPosX = Horizontal position of the collision + a little amount to the right.
    hitPosStartY & hitPosEndY = Vertical position of the collision + a little amount to up and down.
    So hitPosX + hitPosStartY + hitPosEndY = area, where could be more shields to be destroyed.

    In "bodiesToBeDestroyed.add(body1 and body2);" it adds the shield and bullet which have collided
    to the bodiesToBeDestroyed-array.

    Then it sets the the value of checkNeighbor to true, so it can check which shields
    are in the collision area in checkNeighborMethod. I tried to combine these two methods, but I
    didn't manage to get it work.
     */
    private void collisionBulletShield(Body body1, Body body2) {
        // body1 = shield
        // body2 = bullet
        hitPosX = body2.getPosition().x + poolHitAreaX.get(pool - 1);
        hitPosStartY = body2.getPosition().y - poolHitAreaY.get(pool - 1);
        hitPosEndY = body2.getPosition().y + poolHitAreaY.get(pool - 1);
        bodiesToBeDestroyed.add(body2);
        bodiesToBeDestroyed.add(body1);

        checkNeighbor = true;
    }

    private void collisionBulletInnerShield(Body body1, Body body2) {
        // body1 = innershield
        // body2 = bullet
        hitPosX = body2.getPosition().x + poolHitAreaX.get(0);
        hitPosStartY = body2.getPosition().y - poolHitAreaY.get(0);
        hitPosEndY = body2.getPosition().y + poolHitAreaY.get(0);
        bodiesToBeDestroyed.add(body2);
        bodiesToBeDestroyed.add(body1);

        checkInnerNeighbor = true;
    }

    /*
    When checkNeighbor has been set to true, it checks which bullets are in the collision
    area and puts them to bodiesToBeDestroyed-array.

    DestroyedNeighbors is used to limit the amount of shields to be destroyed, if there happens
    to be too many shields in the collision area.

    After this, it sets bulletHitShield to true.
     */
    private void checkNeighborMethod() {
        if (checkNeighbor) {
            for (Body body : shieldBodies) {
                if (body.getUserData() != null) {
                    if (body.getPosition().x < hitPosX &&
                        body.getPosition().y > hitPosStartY &&
                        body.getPosition().y < hitPosEndY && destroyedNeighbors < 2) {
                            destroyedNeighbors++;
                            bodiesToBeDestroyed.add(body);
                    }
                }
            }
            bulletHitShield = true;
            checkNeighbor = false;
        }

        if (checkInnerNeighbor) {
            for (Body body : innerBodyShields) {
                if (body.getUserData() != null) {
                    if (body.getPosition().x < hitPosX &&
                            body.getPosition().y > hitPosStartY &&
                            body.getPosition().y < hitPosEndY && destroyedNeighbors < 2) {
                        destroyedNeighbors++;
                        bodiesToBeDestroyed.add(body);
                    }
                }
            }
            bulletHitShield = true;
            checkInnerNeighbor = false;
        }
        destroyedNeighbors = 0;
    }

    /*
    After the program has added shields and bullet to the bodiesToBeDestroyed-array,
    it goes through all the remaining shields and saves their position. It also saves the amount
    of shields left and sets hackFirstTry to false.

    Then it waits for one second and adds the remaining shields to the bodiesToBeDestroyed-array.
     */
    private void checkBulletHitShield() {
        int i = 0;
        int j = 0;
        if (bulletHitShield) {
            game.playSound(files.sndBreakShield);
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

            for (Body body : innerBodyShields) {
                if (body.getUserData() != null) {
                    innerPosX.set(j, body.getPosition().x);
                    innerPosY.set(j, body.getPosition().y);
                    j++;
                }
            }
            game.setInnerHackShieldAmount(j);
            game.setInnerPosX(innerPosX);
            game.setInnerPosY(innerPosY);

            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    for (Body body : shieldBodies) {
                        if (body.getUserData() != null) {
                            bodiesToBeDestroyed.add(body);
                        }
                    }
                    for (Body body : innerBodyShields) {
                        if (body.getUserData() != null) {
                            bodiesToBeDestroyed.add(body);
                        }
                    }
                    bulletMissedEnemy = true;
                }
            }, 1f);
            bulletHitShield = false;
        }
    }

    /*
    When bullet has hit the enemy, it sets the hackFirstTry to true and adds enemy and shields to
    bodiesToBeDestroyed-array.
     */
    private void collisionBulletEnemy(Body body) {
        bodiesToBeDestroyed.add(body);
        bulletHitEnemy = true;
        game.playSound(files.sndHackSuccessful);
        for (Body b : shieldBodies) {
            if (b.getUserData() != null) {
                bodiesToBeDestroyed.add(b);
            }
        }
        for (Body c : innerBodyShields) {
            if (c.getUserData() != null) {
                bodiesToBeDestroyed.add(c);
            }
        }
    }

    private FixtureDef getShieldFixtureDefinition(float r) {
        FixtureDef shieldFixtureDef = new FixtureDef();
        shieldFixtureDef.density = 0f;
        shieldFixtureDef.restitution = 0f;
        shieldFixtureDef.friction = 0f;
        CircleShape circleshape = new CircleShape();
        circleshape.setRadius(r);
        shieldFixtureDef.shape = circleshape;
        return shieldFixtureDef;
    }

    private FixtureDef getBulletFixtureDefinition() {
        FixtureDef bulletFixtureDef = new FixtureDef();
        bulletFixtureDef.density = 1;
        bulletFixtureDef.restitution = 0f;
        bulletFixtureDef.friction = 0f;
        CircleShape circleshape = new CircleShape();
        circleshape.setRadius(0.3f);
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

    /*
    Moves shields. Not exactly sure, how it does it. :D
     */
    private void movement(float shieldSpeed, Vector2 center) {
        for (Body body : shieldBodies) {
            if (body.getUserData() != null) {
                Vector2 radius = center.cpy().sub(body.getPosition());
                Vector2 force = radius.rotate90(1).nor().scl(shieldSpeed);
                body.setLinearVelocity(force.x, force.y);
            }
        }
        for (Body body : innerBodyShields) {
            if (body.getUserData() != null) {
                Vector2 radius = center.cpy().sub(body.getPosition());
                Vector2 force = radius.rotate90(-1).nor().scl(shieldSpeed);
                body.setLinearVelocity(force.x, force.y);
            }
        }
    }

    /*
    Deletes all the bodies that have been added to bodiesToBeDestoyed-array.
     */
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
        for (Body body : innerBodyShields) {
            if (body.getUserData() != null) {
                batch.draw(innerShieldTexture,
                        body.getPosition().x - innerShieldRadius,
                        body.getPosition().y - innerShieldRadius,
                        innerShieldRadius, // originX
                        innerShieldRadius, // originY
                        innerShieldRadius * 2, // width
                        innerShieldRadius * 2, // height
                        1.0f, // scaleX
                        1.0f, // scaleY
                        body.getTransform().getRotation() * MathUtils.radiansToDegrees,
                        0, // Start drawing from x = 0
                        0, // Start drawing from y = 0
                        innerShieldTexture.getWidth(), // End drawing x
                        innerShieldTexture.getHeight(), // End drawing y
                        false, // flipX
                        false); // flipY
            }
        }
        for (Body body : bulletBodies) {
            if (body.getUserData() != null) {
                batch.draw(bulletTexture,
                        body.getPosition().x - 0.6f,
                        body.getPosition().y - bulletRadius/2,
                        bulletRadius, // originX
                        bulletRadius, // originY
                        bulletRadius, // width
                        bulletRadius, // height
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

    public boolean isBulletMissedEnemy() {
        return bulletMissedEnemy;
    }

    public boolean isBulletHitEnemy() {
        return bulletHitEnemy;
    }
}
