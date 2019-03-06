package fi.tamk.fi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainGame extends Game {
	SpriteBatch batch;
	Texture img;

	public static final float pixelWidth = 1920f;
	public static final float pixelHeight = 1080f;
	private OrthographicCamera camera;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, pixelWidth, pixelHeight);
		img = new Texture("badlogic.jpg");

		// Swith to first room
		RoomFight room = new RoomFight(this);
		setScreen(room);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public SpriteBatch getBatch() {
		return batch;
	}

    public OrthographicCamera getCamera() {
        return camera;
    }
}
