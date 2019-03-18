package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.security.Key;

import sun.applet.Main;

public class Hacking {

    MainGame game;
    SpriteBatch batch;
    private World world;
    Body shieldBody;

    Texture texture = new Texture(Gdx.files.internal("badlogic.jpg"));

    private int aliveTimer = 560;
    protected float width = 1000;
    protected float height = 1000;
    private float shieldRadius;

    public static Array<Body> shields;

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

    Hacking() {

        this.game = game;

        world = new World(new Vector2(0, -0f), true);
        shieldBody = world.createBody(getDefinitionOfBody());
        shieldBody.createFixture(getFixtureDefinition());
        shieldBody.setUserData("shield");
    }

    public void render() {

        world.getBodies(shields);
    }
}
