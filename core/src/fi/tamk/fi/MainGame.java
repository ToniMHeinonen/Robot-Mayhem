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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
	private Music bossMusic;

	//Settings
	Preferences settings;
	private float musicVol;

	//Stats
	Preferences stats;
	int stepCount;

	// Textures
	private Texture imgBgHall, imgBgBoss, imgTopBar, gamePlayer, progBarEnemy, playerIdle,
			playerAttack, playerDefend, playerItem, playerEscape, playerHack, playerDeath,
			enemyIdle, enemyAttack1, enemyAttack2, enemyAttack3, enemyHack;

	//Stepmeter in RoomGame
    private BitmapFont fontSteps;

	//Progressbar
    private TextureAtlas progBarAtlas;
    private Skin progBarSkin;
    private ProgressBar.ProgressBarStyle progBarStyle;

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
		createProgressBarFiles();
		createStepsFont();
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/fansu_1.mp3"));
		bossMusic = Gdx.audio.newMusic(Gdx.files.internal("music/bossmusic.mp3"));
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

    // Transition doesn't work here yet.
    public void switchToRoomGame() {
		transition();
	    RoomGame room = new RoomGame(this);
        setScreen(room);
    }

	// Transition doesn't work here yet.
    public void switchToRoomFight() {
		transition();
	    RoomFight room = new RoomFight(this);
	    setScreen(room);
    }

    public void createStepsFont() {
        fontSteps = new BitmapFont(Gdx.files.internal("stepfont/stepfont.fnt"),
                Gdx.files.internal("stepfont/stepfont.png"),
                false);
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

	public void createProgressBarFiles() {
	    progBarAtlas = new TextureAtlas("progressbar/testpack.pack");
	    progBarSkin = new Skin();
	    progBarSkin.addRegions(progBarAtlas);

	    progBarStyle = new ProgressBar.ProgressBarStyle();
	    progBarStyle.knob = progBarSkin.getDrawable("test_robot");
	    progBarStyle.background = progBarSkin.getDrawable("test_bg");
    }

	public void loadTextures() {
		imgBgHall = new Texture("texture/bg_hall_blank.png");
		imgBgBoss = new Texture("texture/bg_hall_boss_blank.png");
		imgTopBar = new Texture("texture/topbar.png");
		gamePlayer = new Texture("texture/player/player.png");
		progBarEnemy = new Texture("texture/progbar_enemy.png");
        playerIdle = new Texture("texture/player/playerIdle.png");
        playerAttack = new Texture("texture/player/playerAttack.png");
        playerDefend = new Texture("texture/player/playerDefend.png");
        playerItem = new Texture("texture/player/playerItem.png");
        playerEscape = new Texture("texture/player/playerEscape.png");
        playerHack = new Texture("texture/player/playerHack.png");
		playerDeath = new Texture("texture/player/playerDeath.png");
		enemyIdle = new Texture("texture/enemy/enemyIdle.png");
		enemyAttack1 = new Texture("texture/enemy/enemyAttack1.png");
		enemyAttack2 = new Texture("texture/enemy/enemyAttack2.png");
		enemyAttack3 = new Texture("texture/enemy/enemyAttack3.png");
		enemyHack = new Texture("texture/enemy/enemyHack.png");
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

	public Music getBossMusic() {
	    return bossMusic;
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

	public Texture getProgBarEnemy() {
	    return progBarEnemy;
    }

	public TextButton.TextButtonStyle getStyle() {
		return style;
	}

    public ProgressBar.ProgressBarStyle getProgBarStyle() {
        return progBarStyle;
    }

    public Texture getPlayerAttack() {
        return playerAttack;
    }

    public Texture getPlayerDefend() {
        return playerDefend;
    }

    public Texture getPlayerItem() {
        return playerItem;
    }

    public Texture getPlayerEscape() {
        return playerEscape;
    }

    public Texture getPlayerHack() {
        return playerHack;
    }

    public Texture getPlayerIdle() {
        return playerIdle;
    }

	public Texture getEnemyIdle() {
		return enemyIdle;
	}

	public Texture getEnemyAttack1() {
		return enemyAttack1;
	}

	public Texture getEnemyAttack2() {
		return enemyAttack2;
	}

	public Texture getEnemyAttack3() {
		return enemyAttack3;
	}

	public BitmapFont getFontSteps() {
	    return fontSteps;
    }

	public Texture getEnemyHack() {
		return enemyHack;
	}

	public Texture getPlayerDeath() {
		return playerDeath;
	}
}
