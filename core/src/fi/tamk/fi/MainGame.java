package fi.tamk.fi;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Locale;

import javax.xml.soap.Text;

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

	//Image button (temporary)
	private BitmapFont font;
	private TextureAtlas buttonsAtlas; //** image of buttons **//
	private Skin buttonSkin; //** images are used as skins of the button **//
	private TextButton.TextButtonStyle style;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, pixelWidth, pixelHeight);
		createBundle();

		createSkinAndStage();
		createButtonFiles();
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/fansu_1.mp3"));
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

	public void switchToRoomTestailua() {
	    RoomTestailua room = new RoomTestailua(this);
	    setScreen(room);
    }

    public void switchToRoomSettings() {
		RoomSettings room = new RoomSettings(this);
		setScreen(room);
    }

    public void switchToRoomGame() {
	    RoomGame room = new RoomGame(this);
        setScreen(room);
    }

    public void switchToRoomFight() {
	    RoomFight room = new RoomFight(this);
	    setScreen(room);
    }

    public void createButtonFiles() {
		buttonsAtlas = new TextureAtlas("test/button.pack"); //**button atlas image **//
		buttonSkin = new Skin();
		buttonSkin.addRegions(buttonsAtlas); //** skins for on and off **//
		font = new BitmapFont(Gdx.files.internal("test/new.fnt"), false); //** font **//

		style = new TextButton.TextButtonStyle(); //** Button properties **//
		style.up = buttonSkin.getDrawable("buttonOff");
		style.down = buttonSkin.getDrawable("buttonOn");

		style.font = font;
	}

	public void loadTextures() {
		imgBgHall = new Texture("texture/bg_hall_blank.png");
		imgBgBoss = new Texture("texture/bg_hall_boss_blank.png");
		imgTopBar = new Texture("texture/topbar.png");
		exampleSheet = new Texture("texture/exampleanimation.png");
		green = new Texture("texture/green.png");
		yellow = new Texture("texture/enemy.png");
		red = new Texture("texture/red.png");
		gamePlayer = new Texture("texture/player.png");
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

	public TextButton.TextButtonStyle getStyle() {
		return style;
	}
}
