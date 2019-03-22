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

	public final float pixelWidth = 1920f;
	public final float pixelHeight = 1080f;
	private OrthographicCamera camera;

	private Stage stage;
	private Skin skin;
	private Music backgroundMusic;
	private Music bossMusic;

	//Settings
	private Preferences settings;
	private float musicVol;

	//Stats
	private int saveTimerAmount = 3600;
	private int saveTimer = saveTimerAmount;
	private Preferences stats;
	private int stepCount, stepBank, stepAllCount, playerMaxHp;
	private String skill1, skill2;
	private boolean firstPlayTime;

	// Textures
	private Texture imgBgHall, imgBgBoss, imgTopBar, gamePlayer, playerIdle,
			playerItem, playerEscape, playerHack, playerDeath, escapeBg, healthBar;

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
		// Create skills and bosses when the game launches
		Skills.createSkills();
		Bosses.createBosses();

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
		controlSaveTimer();
	}

	@Override
	public void dispose () {
		batch.dispose();
		gamePlayer.dispose();
		stage.dispose();
		skin.dispose();
		backgroundMusic.dispose();
		Bosses.dispose();
		Skills.dispose();
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

    public void switchToHacking() {
		transition();
		Hacking room = new Hacking(this);
		setScreen(room);
	}

    private void createStepsFont() {
        fontSteps = new BitmapFont(Gdx.files.internal("stepfont/stepfont.fnt"),
                Gdx.files.internal("stepfont/stepfont.png"),
                false);
    }

    private void createButtonFiles() {
		buttonsAtlas = new TextureAtlas("test/button.pack"); //**button atlas image **//
		buttonSkin = new Skin();
		buttonSkin.addRegions(buttonsAtlas); //** skins for on and off **//
		font = new BitmapFont(Gdx.files.internal("test/new.fnt"), false); //** font **//

		style = new TextButton.TextButtonStyle(); //** Button properties **//
		style.up = buttonSkin.getDrawable("buttonOff");
		style.down = buttonSkin.getDrawable("buttonOn");

		style.font = font;
	}

	private void createProgressBarFiles() {
	    progBarAtlas = new TextureAtlas("progressbar/progressbar.pack");
	    progBarSkin = new Skin();
	    progBarSkin.addRegions(progBarAtlas);

	    progBarStyle = new ProgressBar.ProgressBarStyle();
	    progBarStyle.knob = progBarSkin.getDrawable("tripmarker");
	    progBarStyle.background = progBarSkin.getDrawable("tripmeter");
    }

	private void loadTextures() {
		imgBgHall = new Texture("texture/bg_hall_blank.png");
		imgBgBoss = new Texture("texture/bg_hall_boss_blank.png");
		imgTopBar = new Texture("texture/topbar.png");
		gamePlayer = new Texture("texture/player/player.png");
        playerIdle = new Texture("texture/player/playerIdle.png");
        playerItem = new Texture("texture/player/playerItem.png");
        playerEscape = new Texture("texture/player/playerEscape.png");
        playerHack = new Texture("texture/player/playerHack.png");
		playerDeath = new Texture("texture/player/playerDeath.png");
		escapeBg = new Texture("texture/escapeBackground.png");
		healthBar = new Texture("texture/healthBar.png");
	}

	private void createBundle() {
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

    public void controlSaveTimer() {
		if (saveTimer > 0) saveTimer--;
		else saveTimer = saveTimerAmount; saveStats();
	}

	public void loadStats() {
		stats = Gdx.app.getPreferences("Robot_Mayhem_Stats");
		stepCount = stats.getInteger("stepCount", 0);
		stepAllCount = stats.getInteger("stepAllCount", 0);
		stepBank = stats.getInteger("stepBank", 0);
		playerMaxHp = stats.getInteger("playerMaxHp", 10);
		skill1 = stats.getString("skill1", "");
		skill2 = stats.getString("skill2", "");
		firstPlayTime = stats.getBoolean("firstPlayTime", true);
	}

	public void saveStats() {
		stats.putInteger("stepCount", stepCount);
		stats.putInteger("stepAllCount", stepAllCount);
		stats.putInteger("stepBank", stepBank);
		stats.putInteger("playerMaxHp", playerMaxHp);
		stats.putString("skill1", skill1);
		stats.putString("skill2", skill2);
		stats.putBoolean("firstPlayTime", firstPlayTime);
		stats.flush();
	}

    private void createSkinAndStage() {
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

	public TextButton.TextButtonStyle getStyle() {
		return style;
	}

    public ProgressBar.ProgressBarStyle getProgBarStyle() {
        return progBarStyle;
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

	public BitmapFont getFontSteps() {
	    return fontSteps;
    }

	public Texture getPlayerDeath() {
		return playerDeath;
	}

	public Texture getEscapeBg() {
		return escapeBg;
	}

	public Texture getHealthBar() {
		return healthBar;
	}

	public int getStepCount() {
		return stepCount;
	}

	public int getStepBank() {
		return stepBank;
	}

	public int getStepAllCount() {
		return stepAllCount;
	}

	public int getPlayerMaxHp() {
		return playerMaxHp;
	}

	public void setPlayerMaxHp(int playerHp) {
		this.playerMaxHp = playerHp;
	}

	public String getSkill1() {
		return skill1;
	}

	public void setSkill1(String skill1) {
		this.skill1 = skill1;
	}

	public String getSkill2() {
		return skill2;
	}

	public void setSkill2(String skill2) {
		this.skill2 = skill2;
	}

	public boolean isFirstPlayTime() {
		return firstPlayTime;
	}
}
