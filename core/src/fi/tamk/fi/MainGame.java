package fi.tamk.fi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;

import javax.xml.soap.Text;

public class MainGame extends Game {
	private SpriteBatch batch;
	private Texture gamePlayer;

	public static final float pixelWidth = 1920f;
	public static final float pixelHeight = 1080f;
	private OrthographicCamera camera;

	private Stage stage;
	private Skin skin;
	private Music backgroundMusic;
	private float musicVol;
	Preferences settings;

	// Added for testing.
	private Texture exampleSheet;
	private Texture green;
	private Texture yellow;
	private Texture red;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, pixelWidth, pixelHeight);

		createSkinAndStage();
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("bgmusic.mp3"));
		settings = Gdx.app.getPreferences("Robot_Mayhem_Settings");
        loadSettings();

		// Added for testing.
		exampleSheet = new Texture("exampleanimation.png");
		green = new Texture("green.png");
		yellow = new Texture("enemy.png");
		red = new Texture("red.png");

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
		backgroundMusic.dispose();
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

	public Music getBackgroundMusic() {
		return backgroundMusic;
	}

	// Added for testing.
	public Texture getOrangeTexture() { return exampleSheet;}
	public Texture getGreenTexture() { return green;}
	public Texture getYellowTexture() { return yellow;}
	public Texture getRedTexture() { return red;}

    public float getMusicVol() {
        return musicVol;
    }

    public void setMusicVol(float musicVol) {
	    if (musicVol > 0.0f && musicVol < 1.0f) {
            this.musicVol = musicVol;
        }
    }

    public void loadSettings() {
		musicVol = settings.getFloat("musicVolume", 0.8f);
    }

    public void saveSettings() {
	    settings.putFloat("musicVolume", musicVol);
	    settings.flush();
    }

    public void createSkinAndStage() {
        stage = new Stage(new FitViewport(pixelWidth, pixelHeight), batch);
        // Skin: https://github.com/czyzby/gdx-skins/tree/master/glassy
        // Check "License" bottom of the page
        // Files in \android\assets:
        // font-big-export.fnt, font-export.fnt, glassy-ui.atlas,
        // glassy-ui.json, glassy-ui.png
        skin = new Skin( Gdx.files.internal("glassy-ui.json") );
    }
}
