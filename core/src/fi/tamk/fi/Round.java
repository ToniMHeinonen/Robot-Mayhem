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
import com.badlogic.gdx.utils.Timer;

public class Round extends RoomParent {

    enum BodyData {
        SHIELD, BULLET, ENEMY
    }

    private Texture ball;
    public static final float WORLD_WIDTH = 19.20f;
    public static final float WORLD_HEIGHT = 10.80f;
    private World world;
    private Body shieldBody;
    private Body centerBody;
    // centerBody is a centerpoint of the circle and position of the enemy
    private Body bulletBody;
    private Box2DDebugRenderer debugRenderer;
    private float radius = 0.5f;
    private double accumulator = 0;
    private float TIME_STEP = 1 / 60f;
    private Vector2 center;
    private float speed = 5;
    private float pos1x, pos2x, pos3x, pos4x, pos5x, pos6x, pos7x, pos8x, pos9x, pos10x, pos11x,
    pos12x, pos13x, pos14x, pos15x, pos16x;
    private float pos1y, pos2y, pos3y, pos4y, pos5y, pos6y, pos7y, pos8y, pos9y, pos10y, pos11y,
    pos12y, pos13y, pos14y, pos15y, pos16y;

    private Array<Body> shieldBodies = new Array<Body>();
    private Array<DistanceJointDef> distanceJointDefs = new Array<DistanceJointDef>();
    private Array<Body> bulletBodies = new Array<Body>();
    private Array<Body> bodiesToBeDestroyed = new Array<Body>();

    private float widthOfEnemy = WORLD_WIDTH - 3f;
    private float heightOfEnemy = 5f;

    private float widthOfPlayer = 1f;
    private float heightOfPlayer = 5f;

    private FloatArray hackPosX;
    private FloatArray hackPosY;
    private int hackShieldAmount;
    private boolean hackFirstTry;
    private int tier1HackShieldAmount;
    private int tier2HackShieldAmount;
    private int tier3HackShieldAmount;
    private int pool;
    private int poolMult;

    private boolean doMove = true;

    Round(MainGame game) {
        super(game);
        createConstants();
        createPositions();
        createShields();
        createJoints();
        createButtonShoot();
        createButtonSettings();
        createCollisionChecking();
    }

    @Override
    public void render (float delta) {
        super.render(delta);
        batch.setProjectionMatrix(camera.combined);
        debugRenderer.render(world, camera.combined);
        doPhysicsStep(Gdx.graphics.getDeltaTime());
        deleteBodies();
        batch.begin();
        drawBodies(bulletBodies);
        drawBodies(shieldBodies);
        batch.end();
        movement(speed, center);
        checkMovement();
    }

    public void createConstants() {
        ball = new Texture("test.png");
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        world = new World(new Vector2(0, 0), true);
        centerBody = world.createBody(getDefinitionOfCenterBody());
        centerBody.createFixture(getFixtureDefinition());
        centerBody.setUserData(BodyData.ENEMY);
        debugRenderer = new Box2DDebugRenderer();
        center = centerBody.getPosition();
        this.hackPosX = game.getHackPosX();
        this.hackPosY = game.getHackPosY();
        this.hackShieldAmount = game.getHackShieldAmount();
        this.hackFirstTry = game.getHackFirstTry();
        this.tier1HackShieldAmount = game.getTier1HackShieldAmount();
        this.tier2HackShieldAmount = game.getTier2HackShieldAmount();
        this.tier3HackShieldAmount = game.getTier3HackShieldAmount();
        this.pool = game.getPool();
        this.poolMult = game.getPoolMult();
    }

    public void createPositions() {
        if (hackFirstTry) {
            hackPosX.clear();
            hackPosY.clear();
            if (pool == 1) {
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
        }
    }

    public void createShields() {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.DynamicBody;
        for (int i = 0; i < hackShieldAmount; i++) {
            myBodyDef.position.set(hackPosX.get(i), hackPosY.get(i));
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
        for (int i = 0; i < hackShieldAmount; i++) {
            distanceJointDef.bodyA = shieldBodies.get(i);
            distanceJointDefs.add(distanceJointDef);

            DistanceJoint distanceJoint = (DistanceJoint) world.createJoint(distanceJointDefs.get(i));
        }
    }

    public void checkMovement() {
        int i = 0;
        if (!doMove) {
            for (Body body : shieldBodies) {
                if (body.getUserData() != null) {
                    Vector2 vel = body.getLinearVelocity();
                    vel.x = 0f;
                    vel.y = 0f;
                    body.setLinearVelocity(vel);
                }
            }
            for (Body body : shieldBodies) {
                if (body.getUserData() != null) {
                    hackPosX.set(i, body.getPosition().x);
                    hackPosY.set(i, body.getPosition().y);
                    i++;
                }
            }
            game.setHackShieldAmount(hackShieldAmount);
            game.setHackPosX(hackPosX);
            game.setHackPosY(hackPosY);
            game.setHackFirstTry(false);
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
            bulletBody.createFixture(getFixtureDefinition());
            bulletBody.setUserData(BodyData.BULLET);
            bulletBody.applyLinearImpulse(new Vector2(6, 0), bulletBody.getWorldCenter(), true);
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
                    if (hackShieldAmount == 8 || hackShieldAmount == 16) {
                        int index = shieldBodies.indexOf(body1, true);
                        checkNeighbor(index);
                    }
                    bodiesToBeDestroyed.add(body2);
                    bodiesToBeDestroyed.add(body1);
                    hackShieldAmount--;
                    doMove = false;
                }

                if (body2.getUserData() == BodyData.SHIELD && body1.getUserData() == BodyData.BULLET) {
                    if (hackShieldAmount == 8 || hackShieldAmount == 16) {
                        int index = shieldBodies.indexOf(body2, true);
                        checkNeighbor(index);
                    }
                    bodiesToBeDestroyed.add(body1);
                    bodiesToBeDestroyed.add(body2);
                    hackShieldAmount--;
                    doMove = false;
                }

                if (body1.getUserData() == BodyData.BULLET && body2.getUserData() == BodyData.ENEMY) {
                    game.setHackFirstTry(true);
                    bodiesToBeDestroyed.add(body1);
                    game.setHackShieldAmount(tier1HackShieldAmount);
                }

                if (body2.getUserData() == BodyData.BULLET && body1.getUserData() == BodyData.ENEMY) {
                    game.setHackFirstTry(true);
                    bodiesToBeDestroyed.add(body2);
                    game.setHackShieldAmount(tier1HackShieldAmount);
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

    private void checkNeighbor(int index) {
        if (index < (hackShieldAmount - 1)) {
            index += 1;
        }
        if (index == 0) {
            index++;
        }
        if (shieldBodies.get(index).getUserData() != null) {
            bodiesToBeDestroyed.add(shieldBodies.get(index));
        }
    }

    private FixtureDef getFixtureDefinition() {
        FixtureDef playerFixtureDef = new FixtureDef();
        playerFixtureDef.density = 1;
        playerFixtureDef.restitution = 0f;
        playerFixtureDef.friction = 0f;
        CircleShape circleshape = new CircleShape();
        circleshape.setRadius(radius);
        playerFixtureDef.shape = circleshape;
        return playerFixtureDef;
    }

    private BodyDef getDefinitionOfCenterBody() {
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

    private void movement(float speed, Vector2 center) {
        for (Body body : shieldBodies) {
            if (body.getUserData() != null) {
                Vector2 radius = center.cpy().sub(body.getPosition());
                Vector2 force = radius.rotate90(1).nor().scl(speed);
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

    public void drawBodies(Array<Body> bodies) {
        for (Body body : bodies) {
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
    }

    @Override
    public void dispose () {
        world.dispose();
    }
}
