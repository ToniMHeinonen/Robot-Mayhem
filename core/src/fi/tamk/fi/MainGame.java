package fi.tamk.fi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class MainGame extends Game {
	private SpriteBatch batch;
	private Texture gamePlayer;

	public static final float pixelWidth = 1920f;
	public static final float pixelHeight = 1080f;
	private OrthographicCamera camera;

	private Stage stage;
	private Skin skin;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, pixelWidth, pixelHeight);
		stage = new Stage(new FitViewport(pixelWidth, pixelHeight), batch);
		// Skin: https://github.com/czyzby/gdx-skins/tree/master/glassy
		// Check "License" bottom of the page
		// Files in \android\assets:
		// font-big-export.fnt, font-export.fnt, glassy-ui.atlas,
		// glassy-ui.json, glassy-ui.png
		skin = new Skin( Gdx.files.internal("glassy-ui.json") );

		loadTextures();

		// Swith to first room
		RoomSettings room = new RoomSettings(this);
		setScreen(room);
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose () {
		batch.dispose();
		gamePlayer.dispose();
		stage.dispose();
		skin.dispose();
	}

	public void loadTextures() {
		gamePlayer = new Texture("player.png");
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public Texture getGamePlayer() {
		return gamePlayer;
	}

	public Stage getStage() {
		return stage;
	}

	public Skin getSkin() {
		return skin;
	}
}
