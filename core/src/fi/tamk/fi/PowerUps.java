package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class PowerUps extends RoomParent {

    SpriteBatch batch;
    //Rectangle rectangle;
    Texture box;
    int pupA;
    int pupB;
    int pupC;

    Array<Integer> pups;

    PowerUps(MainGame game) {
        super(game);
        create();
    }

    public void create() {

        batch = new SpriteBatch();
        box = new Texture(Gdx.files.internal("box.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        pups = new Array<Integer>();

        for (int i = 0; i < 1; i++) {

            pupA = MathUtils.random(1, 3);
            pups.add(pupA);
        }

        for (int i = 0; i < 1; i++) {

            pupB = MathUtils.random(4, 6);
            pups.add(pupB);
        }

        for (int i = 0; i < 1; i++) {

            pupC = MathUtils.random(7, 9);
            pups.add(pupC);
            System.out.println(pups);
        }
    }

    public void render (float delta) {

        super.render(delta);

        if (!game.haveWeChangedTheRoom) {

            batch.setProjectionMatrix(camera.combined);

            batch.begin();

            int add = box.getWidth();

            batch.draw(box, 500, 100);
            batch.draw(box, 500 + add, 100);
            batch.draw(box, 500 + 2 * add, 100);

            /*rectangle = new Rectangle(rectangle.x = 100, rectangle.y = 100,
                    rectangle.width = 100, rectangle.height = 100);*/

            batch.end();
        }
    }

    public void dispose () {
        batch.dispose();
    }
}
