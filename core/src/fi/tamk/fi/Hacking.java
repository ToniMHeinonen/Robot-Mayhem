package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.security.Key;

import sun.applet.Main;

public class Hacking extends RoomParent{

    MainGame game;
    SpriteBatch batch;

    TextureAtlas testButtonAtlas;
    Skin testSkin;

    private World world;
    Body shieldBody;

    Texture texture = new Texture(Gdx.files.internal("badlogic.jpg"));

    private int aliveTimer = 560;
    protected float width = 1000;
    protected float height = 1000;
    private float shieldRadius;

    public static Array<Body> shields;

    public void create() {

        world = new World(new Vector2(0, -0f), true);
        shieldBody = world.createBody(getDefinitionOfBody());
        shieldBody.createFixture(getFixtureDefinition());
        shieldBody.setUserData("shield");
    }

    protected BodyDef getDefinitionOfBody() {

        BodyDef shieldBody = new BodyDef();

        // It's a body that moves
        shieldBody.type = BodyDef.BodyType.DynamicBody;

        shieldBody.position.set(500, 500);
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
        circleshape.setRadius(5f);

        playerFixtureDef.shape = circleshape;
        return playerFixtureDef;
    }

    Hacking(MainGame game) {

        super(game);

        createConstants();
    }

    public void createConstants() {
        testButtonAtlas = new TextureAtlas("testbuttons/testbuttons.pack");
        testSkin = new Skin(testButtonAtlas);
    }

    public void render(float delta) {

        super.render(delta);
        world.getBodies(shields);

        batch.begin();

        for (Body body : shields) {
            System.out.println(body.getUserData());
            if(body.getUserData() != null) {

                if (body.getUserData() == texture) {

                    System.out.println("This part doesn not work yet.");
                }

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
        batch.end();
    }
}
