package fi.tamk.fi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Locale;

import javax.xml.soap.Text;

import static com.badlogic.gdx.graphics.Color.GRAY;
import static com.badlogic.gdx.graphics.Color.rgb888ToColor;

public class MainGame extends Game {
	private SpriteBatch batch;
	private I18NBundle myBundle;

	public static final float pixelWidth = 1920f;
	public static final float pixelHeight = 1080f;
	private OrthographicCamera camera;

	private Stage stage;
	private Skin skin;
	private Music backgroundMusic;

	//Settings
	Preferences settings;
	private float musicVol;

	//Stats
	Preferences stats;
	int stepCount;

	// Textures
	private Texture imgBgHall;
	private Texture imgBgBoss;
	private Texture imgTopBar;
	private Texture gamePlayer;
	private Texture exampleSheet;
	private Texture green;
	private Texture yellow;
	private Texture red;

	//boolean haveWeChangedTheRoom;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, pixelWidth, pixelHeight);
		createBundle();

		createSkinAndStage();
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("fansu_1.mp3"));
        loadSettings();
        loadStats();

		loadTextures();

		// Switch to first room
		switchToRoomSettings();
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
		saveStats();
	}

	boolean haveWeChangedTheRoom = false;

	public void transition() { haveWeChangedTheRoom = true; }

	public void switchToRoomTestailua() {
		transition();
	    RoomTestailua room = new RoomTestailua(this);
	    setScreen(room);
    }

    public void switchToRoomSettings() {
		transition();
		RoomSettings room = new RoomSettings(this);
		setScreen(room);
    }

    public void switchToRoomGame() {
		transition();
	    RoomGame room = new RoomGame(this);
        setScreen(room);
    }

    public void switchToRoomFight() {
		transition();
	    RoomFight room = new RoomFight(this);
	    setScreen(room);
    }

	public void loadTextures() {
		// Added for testing.
		imgBgHall = new Texture("bg_hall_blank.png");
		imgBgBoss = new Texture("bg_hall_boss_blank.png");
		imgTopBar = new Texture("topbar.png");
		exampleSheet = new Texture("exampleanimation.png");
		green = new Texture("green.png");
		yellow = new Texture("enemy.png");
		red = new Texture("red.png");
		gamePlayer = new Texture("player.png");
	}

	public void createBundle() {
		Locale locale = Locale.getDefault();
		//Locale locale = new Locale("fi", "FI"); USE THIS TO TEST FINNISH VERSION
		myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"),
				locale,"ISO-8859-1");

		//EXAMPLE CODE
		System.out.println(myBundle.get("test"));
		String str = myBundle.get("test");
		System.out.println(str);
	}

	// Added for testing.
	public Texture getOrangeTexture() { return exampleSheet;}
	public Texture getGreenTexture() { return green;}
	public Texture getYellowTexture() { return yellow;}
	public Texture getRedTexture() { return red;}

    public void loadSettings() {
		settings = Gdx.app.getPreferences("Robot_Mayhem_Settings");
		musicVol = settings.getFloat("musicVolume", 0.8f);
    }

    public void saveSettings() {
	    settings.putFloat("musicVolume", musicVol);
	    settings.flush();
    }

	public void loadStats() {
		stats = Gdx.app.getPreferences("Robot_Mayhem_Stats");
		stepCount = stats.getInteger("stepCount", 0);
	}

	public void saveStats() {
		stats.putInteger("stepCount", stepCount);
		stats.flush();
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

    public void receiveSteps(int stepCount) {
		this.stepCount++;
	}

	public void setMusicVol(float musicVol) {
		if (musicVol > 0.0f && musicVol < 1.0f) {
			this.musicVol = musicVol;
		}
	}

	/*
	GETTERS
	 */

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

	public float getMusicVol() {
		return musicVol;
	}

	public I18NBundle getMyBundle() {
		return myBundle;
	}

	public Texture getImgBgHall() {
		return imgBgHall;
	}

	public Texture getImgBgBoss() {
		return imgBgBoss;
	}

	public Texture getImgTopBar() {
		return imgTopBar;
	}
}
