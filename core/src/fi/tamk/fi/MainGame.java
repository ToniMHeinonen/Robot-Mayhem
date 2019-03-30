package fi.tamk.fi;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Locale;

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
	private int stepCount, stepBank, stepAllCount, pool, poolMult;
	private String skill1, skill2;
	private boolean firstPlayTime;

	// Textures
	private Texture imgBgHall, imgBgBoss, imgTopBar, imgBottomBar, gamePlayer, playerIdle,
			playerItem, playerEscape, playerHack, playerDeath, escapeBg, hpBarLeft, hpBarRight,
			healthPlus, healthMinus;

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

	//Dialog
	private UtilDialog dialog;
	private TextureAtlas testButtonAtlas;
	private Skin testSkin;
	private Label.LabelStyle labelStyle;
	private Window.WindowStyle windowStyle;
	private com.badlogic.gdx.graphics.Color fontColor = com.badlogic.gdx.graphics.Color.BLACK;

	//Hacking
    private FloatArray hackPosX;
    private FloatArray hackPosY;
    private int hackShieldAmount;
    private boolean hackFirstTry = true;
    private final int pool1HackShieldAmount = 4;
    private final int pool2HackShieldAmount = 8;
    private final int pool3HackShieldAmount = 16;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, pixelWidth, pixelHeight);
		createBundle();
		// Create skills and bosses when the game launches
		Skills.createSkills();
		Bosses.createBosses();

		// Create items
		Item.createItems();

		createSkinAndStage();
		createButtonFiles();
		createProgressBarFiles();
		createDialogConstants();
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/fansu_1.mp3"));
		backgroundMusic.setLooping(true);
		bossMusic = Gdx.audio.newMusic(Gdx.files.internal("music/bossmusic.mp3"));
		bossMusic.setLooping(true);
        loadSettings();
        loadStats();

		loadTextures();

		createHackFiles();

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

	public void switchToPowerUps() {
		transition();
		PowerUps room = new PowerUps(this);
		setScreen(room);
	}

	public void switchToRoomItemTest() {

		transition();
		//RoomItemTest room = new RoomItemTest(this);
		//setScreen(room);
	}

    private void createHackFiles() {
        hackPosX = new FloatArray();
        hackPosY = new FloatArray();
        if (hackFirstTry) {
            if (pool == 1) {
                hackShieldAmount = pool1HackShieldAmount;
            }
            if (pool == 2) {
                hackShieldAmount = pool2HackShieldAmount;
            }
            if (pool == 3) {
                hackShieldAmount = pool3HackShieldAmount;
            }
        }
    }

	private void createDialogConstants() {
		fontSteps = new BitmapFont(Gdx.files.internal("stepfont/stepfont.fnt"),
				Gdx.files.internal("stepfont/stepfont.png"),
				false);
		testButtonAtlas = new TextureAtlas("testbuttons/testbuttons.pack");
		testSkin = new Skin(testButtonAtlas);
		windowStyle = new Window.WindowStyle(fontSteps, fontColor, testSkin.getDrawable("dialog_bg"));
		labelStyle = new Label.LabelStyle(fontSteps, fontColor);
		dialog = new UtilDialog(this);
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
		imgBottomBar = new Texture("texture/bottombar.png");
		gamePlayer = new Texture("texture/player/player.png");
        playerIdle = new Texture("texture/player/playerIdle.png");
        playerItem = new Texture("texture/player/playerItem.png");
        playerEscape = new Texture("texture/player/playerEscape.png");
        playerHack = new Texture("texture/player/playerHack.png");
		playerDeath = new Texture("texture/player/playerDeath.png");
		escapeBg = new Texture("texture/escapeBackground.png");
		hpBarLeft = new Texture("texture/hpbar_left.png");
		hpBarRight = new Texture("texture/hpbar_right.png");
		healthPlus = new Texture("texture/skills/plusHealth.png");
		healthMinus = new Texture("texture/skills/minusHealth.png");
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
		skill1 = stats.getString("skill1", "");
		skill2 = stats.getString("skill2", "");
		firstPlayTime = stats.getBoolean("firstPlayTime", true);
		pool = stats.getInteger("pool", 1);
		poolMult = stats.getInteger("poolMult", 0);
	}

	public void saveStats() {
		stats.putInteger("stepCount", stepCount);
		stats.putInteger("stepAllCount", stepAllCount);
		stats.putInteger("stepBank", stepBank);
		stats.putString("skill1", skill1);
		stats.putString("skill2", skill2);
		stats.putBoolean("firstPlayTime", firstPlayTime);
		stats.putInteger("pool", pool);
		stats.putInteger("poolMult", poolMult);
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

	public void nextPool() {
		pool++;
		poolMult = 0;
	}

	public void increasePoolMultiplier() {
		poolMult++;
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

	public Texture getImgBottomBar() {
		return imgBottomBar;
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

	public Texture getHpBarLeft() {
		return hpBarLeft;
	}

	public Texture getHpBarRight() {
		return hpBarRight;
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

	public TextureAtlas getTestButtonAtlas() {
		return testButtonAtlas;
	}

	public Skin getTestSkin() {
		return testSkin;
	}

	public Label.LabelStyle getLabelStyle() {
		return labelStyle;
	}

	public Window.WindowStyle getWindowStyle() {
		return windowStyle;
	}

	public Color getFontColor() {
		return fontColor;
	}

	public UtilDialog getDialog() {
		return dialog;
	}

	public void setHackPosX(FloatArray hackPosX) {
	    this.hackPosX = hackPosX;
    }

    public void setHackPosY(FloatArray hackPosY) {
	    this.hackPosY = hackPosY;
    }

    public void setHackShieldAmount(int hackShieldAmount) {
	    this.hackShieldAmount = hackShieldAmount;
    }

    public void setHackFirstTry(boolean hackFirstTry) {
	    this.hackFirstTry = hackFirstTry;
    }

    public boolean getHackFirstTry() {
	    return hackFirstTry;
    }

    public FloatArray getHackPosX() {
        return hackPosX;
    }

    public FloatArray getHackPosY() {
        return hackPosY;
    }

    public int getHackShieldAmount() {
	    return hackShieldAmount;
    }

    public int getPool1HackShieldAmount() {
	    return pool1HackShieldAmount;
    }

    public int getPool2HackShieldAmount() {
	    return pool2HackShieldAmount;
    }

    public int getPool3HackShieldAmount() {
	    return pool3HackShieldAmount;
    }

	public int getPool() {
		return pool;
	}

	public int getPoolMult() {
		return poolMult;
	}

	public Texture getHealthPlus() {
		return healthPlus;
	}

	public Texture getHealthMinus() {
		return healthMinus;
	}
}
