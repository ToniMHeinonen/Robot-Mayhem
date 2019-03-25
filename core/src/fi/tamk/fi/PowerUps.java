package fi.tamk.fi;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PowerUps extends RoomParent {

    SpriteBatch batch;

    PowerUps(MainGame game) {
        super(game);
        create();
    }

    public void create() {
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);
    }

    public void render (float delta) {

        super.render(delta);

        if (!game.haveWeChangedTheRoom) {

            batch.setProjectionMatrix(camera.combined);

            batch.begin();
            //batch.draw();
            batch.end();
        }
    }

    public void dispose () {
        batch.dispose();
    }
}
