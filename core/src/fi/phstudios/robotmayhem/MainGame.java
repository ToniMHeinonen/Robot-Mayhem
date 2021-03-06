package fi.phstudios.robotmayhem;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class MainGame extends Game {
	private SpriteBatch batch;
	private I18NBundle localize;
	private Encryptor E = new Encryptor();
	private AssetHandler assetHandler;
	private Texture[] splashScreen;
	private int currentSplash, switchSplashTimer = 8;
	private Files files;
	private Skills skills;
	private Item items;
	private Bosses bosses;
	private Music curMusic, curBossMusic;
	private int curRoom, ROOM_GAME = 1, ROOM_FIGHT = 2, ROOM_END = 3;

	public final float pixelWidth = 1920f;
	public final float pixelHeight = 1080f;
	public final float gridSize = 1920f / 16f;
	public final String EASY = "easy", MEDIUM = "medium", HARD = "hard";
	private OrthographicCamera camera;
	private FitViewport fitViewport;

	private Stage stage;
	private Skin skin;
	private Skin finalSkin;

	// Pools and tiers
	private int[] poolMilestones = new int[] {20, 100, 250, 500, 1000, 1500};
	private String[] curBossSkills = new String[3];

	// Settings
	// Keys
	private String keyMusicVol = "musicVol";
	private String keySoundVol = "soundVol";
	private String keyLanguage = "language";
	private String keyDifficulty = "difficulty";
	//Values
	private Preferences settings;
	private float musicVol;
	private float soundVol;
	private String language;
    private boolean clickedOpenSettings = false;
    private String difficulty;

	// Stats
	// Keys (use these to decrease error chance)
	private String keyMoney = E.encrypt("money");
	private String keyStepCount = E.encrypt("stepCount");
	private String keyStepAllCount = E.encrypt("stepAllCount");
	private String keyStepBank = E.encrypt("stepBank");
	private String keyStepBankSize = E.encrypt("stepBankSize");
	private String keySkill1 = E.encrypt("skill1");
	private String keySkill2 = E.encrypt("skill2");
	private String keyCurrentBoss = E.encrypt("currentBoss");

	private String keyFinishedGame = E.encrypt("finishedGame");
	private String keyCheckHard = E.encrypt("checkHard");
	private String keyFinishedGameHard = E.encrypt("finishedGameHard");
	private String keyPool = E.encrypt("pool");
	private String keyPoolMult = E.encrypt("poolMult");
	private String keyBuyedItemsCounter = E.encrypt("buyedItemsCounter");
	private String keyName = E.encrypt("name");
	private String keyFightsWon = E.encrypt("fightsWon");
	private String keyPrevDayGift = E.encrypt("prevDayGift");
	private String keyGameCompleteCounter = E.encrypt("gameCompleteCounter");
	private String keyHardBugFix = E.encrypt("hardBugFix");

	private String keyCritBoost = E.encrypt("critBoost");
	private String keyMissBoost = E.encrypt("missBoost");
	private String keyArmorBoost = E.encrypt("armorBoost");
	private String keyDmgBoost = E.encrypt("dmgBoost");
	private String keyHealBoost = E.encrypt("healBoost");
	private String keyPermanentCritBoost = E.encrypt("permanentCritBoost");
	private String keyPermanentMissBoost = E.encrypt("permanentMissBoost");
	private String keyPermanentArmorBoost = E.encrypt("permanentArmorBoost");
	private String keyPermanentDmgBoost = E.encrypt("permanentDmgBoost");
	private String keyPermanentHealBoost = E.encrypt("permanentHealBoost");
	private String keyReflectiveShield = E.encrypt("reflectiveShield");

	private String keyInventorySize = E.encrypt("inventorySize");
	private String keyInventory = E.encrypt("inventory");
	private String keyBoughtPermanent = E.encrypt("boughtPermanent");
	private String keyBoughtPermanentSize = E.encrypt("boughtPermanentSize");
	private String keyDefeatedBossesSize = E.encrypt("defeatedBossesSize");
	private String keyDefeatedBosses = E.encrypt("defeatedBosses");
	private String keyArrPlayedMusicSize = E.encrypt("arrPlayedMusicSize");
	private String keyArrPlayedMusic = E.encrypt("arrPlayedMusic");

	private String keyFirstPlayTime = E.encrypt("firstPlayTime");
	private String keyFirstPlayTimeFight = E.encrypt("firstPlayTimeFight");
	private String keyFirstPlayInventory = E.encrypt("firstPlayInventory");
	private String keyFirstPlaySettings = E.encrypt("firstPlaySettings");
	private String keyFirstPlayBank = E.encrypt("firstPlayBank");
	private String keyFirstPlayVictory = E.encrypt("firstPlayVictory");
	private String keyFirstPlayPoolComplete1 = E.encrypt("firstplayPoolComplete1");
	private String keyFirstPlayPoolComplete2 = E.encrypt("firstplayPoolComplete2");
	private String keyFirstPlayPoolComplete3 = E.encrypt("firstplayPoolComplete3");
	private String keyFirstPlayMoney = E.encrypt("firstPlayMoney");
	private String keyFirstPlayEscape = E.encrypt("firstPlayEscape");
	private String keyFirstPlayDeath = E.encrypt("firstPlayDeath");
	private String keyFirstPlayNewGamePlus = E.encrypt("firstPlayNewGamePlus");
	private String keyFirstPlayFinalFightStart = E.encrypt("firstPlayFinalFightStart");
	private String keyFirstPlaySkip = E.encrypt("firstPlaySkip");

    private String keyAchievComplete = E.encrypt("achievComplete");
    private String keyAchievCompleteSize = E.encrypt("achievCompleteSize");
    private String keyHasCollected = E.encrypt("hasCollected");
    private String keyResetedGame = E.encrypt("resetedGame");

	// Values
	private int saveTimerAmount = 600;
	private int saveTimer = saveTimerAmount;
	private int stepSaveAmount = 25;
	private int stepSaveCounter = stepSaveAmount;
	private Preferences prefsStats;
	private Preferences prefsAchievs;
	private SaveAndLoad stats;
	private SaveAndLoad achievs;
	private float stepCount, stepBank, stepAllCount, stepBankSize;
	private int pool, poolMult, money, fightsWon, prevDayGift, buyedItemsCounter,
			gameCompleteCounter, hardBugFix;
	private String skill1, skill2, currentBoss, playerName;
	private boolean firstPlayTime, firstPlayTimeFight, firstPlayInventory, firstPlayBank,
			firstPlayVictory, firstPlayPoolComplete1, firstPlayPoolComplete2,
			firstPlayPoolComplete3, firstPlayMoney, firstPlayEscape, firstPlayDeath,
			firstPlayNewGamePlus, firstPlayFinalFightStart, firstPlaySettings, firstPlaySkip,
			reflectiveShield, finishedGame, checkHard, finishedGameHard, resetedGame;
	private int critBoost, missBoost, permaCritBoost, permaMissBoost;
	private float armorBoost, dmgBoost, healBoost, permaArmorBoost, permaDmgBoost, permaHealBoost;
	// Stat arrays
	private ArrayList<String> inventory;
	private int inventorySize; // Needed for loading correct amount of array items
	private ArrayList<String> defeatedBosses;
	private int defeatedBossesSize;
	private ArrayList<Integer> arrPlayedMusic;
	private int arrPlayedMusicSize;
	private ArrayList<String> boughtPermanent;
	private int boughtPermanentSize;
	private ArrayList<String> achievComplete;
	private int achievCompleteSize;
	private ArrayList<String> hasCollected;

	// Stepmeter in RoomGame
    private BitmapFont fontSteps;

	// Progressbar
    private TextureAtlas progBarAtlas;
    private Skin progBarSkin;
    private ProgressBar.ProgressBarStyle progBarStyle;
    private float progressBarMilestone;

	// Dialog
	private static UtilDialog dialog;
	private TextureAtlas testButtonAtlas;
	private Label.LabelStyle labelStyle;
	private Label.LabelStyle descriptionLabelStyle;
	private Window.WindowStyle emptyWindowStyle;
	private com.badlogic.gdx.graphics.Color fontColor = com.badlogic.gdx.graphics.Color.BLACK;
	private BitmapFont descriptionFont;

	// Hacking
    private FloatArray hackPosX;
    private FloatArray hackPosY;
    private FloatArray innerPosX;
    private FloatArray innerPosY;
    private int innerHackShieldAmount;
    private int hackShieldAmount;
    private boolean hackFirstTry = true;
    private final int pool1HackShieldAmount = 7;
    private final int pool2HackShieldAmount = 15;
    private final int pool3HackShieldAmount = 15;
    private final int pool3InnerHackShieldAmount = 7;

    private boolean pauseWalking, assetsLoaded, walkingSkipped;
    private int dialogType;
    public final int DIAL_STOP = 0, DIAL_BOX = 1, DIAL_TALL = 2, DIAL_SMALL = 3, DIAL_PLAYER = 4,
			DIAL_SKILL = 5, DIAL_DEATH = 6;

    private int ramTimer;

    // Stats room uses these to display temporary RoomFight stats
    private int overallBstCrit, overallBstMiss;
    private float overallBstDmg, overallBstArmor, overallBstHeal;

	/**
	 * Initialize these values when the game starts.
	 */
    @Override
	public void create () {
		assetHandler = new AssetHandler();
		batch = new SpriteBatch();
		camera = new OrthographicCamera(pixelWidth, pixelHeight);
		fitViewport = new FitViewport(pixelWidth, pixelHeight, camera);
		/*
		Load these values too in here to get what language the game is using, then the correct
		splash screen can be displayed. These values are also initialized in initAfterRestarting.
		 */
		loadSettings();
		createBundle();
	}

	/**
	 * Initialize these values after files has been loaded.
	 */
	private void initAfterLoaded() {
		assetsLoaded = true;
		files = new Files(assetHandler);

		// All the values below requires files to be initialized
		skills = new Skills(this);
		bosses = new Bosses(this);
		items = new Item();

		createSkinAndStage();
		createProgressBarFiles();
		createDialogConstants();
		createHackFiles();

		initAfterRestarting();
	}

	/**
	 * Initialize these values after restarting the game, mainly used when changing language and
	 * when resetting stats.
	 */
	public void initAfterRestarting() {
		loadSettings();
		createBundle();
		initStats();
		loadStats();
		loadAchievements();
		checkDate();
		chooseNextMilestone();
		// Switch to first room
		switchToRoomGame();
		tempFixOldPermaValues();
	}

    /**
     * Renders all the frames of the room.
     */
	@Override
	public void render () {
		super.render();
		batch.setProjectionMatrix(camera.combined);
		camera.update();
		controlSaveTimer();
		controlSplashScreen();

		//checkRAM(); //For testing RAM usage
	}

    /**
     * Set camera's fitviewport.
     * @param width width of the screen
     * @param height height of the screen
     */
	@Override
	public void resize(int width, int height) {
		fitViewport.update(width, height, true);
	}

    /**
     * Handles what will be disposed.
     */
	@Override
	public void dispose () {
		batch.dispose();
		if (assetsLoaded) {
			stage.dispose();
			fontSteps.dispose();
			descriptionFont.dispose();
			testButtonAtlas.dispose();
			finalSkin.dispose();
			progBarAtlas.dispose();
			progBarSkin.dispose();
			saveStats();
			saveSettings();
		}
		assetHandler.manager.dispose();
	}

	/**
	 * Fix values that have been modified in patch but don't affect old players without this.
	 */
	private void tempFixOldPermaValues() {
		if (permaArmorBoost > 0.2f) permaArmorBoost = 0.2f;
		if (permaHealBoost > 0.2f) permaHealBoost = 0.2f;
	}

    /**
     * Checks RAM-usage.
     */
	private void checkRAM() {
		if (ramTimer > 0) ramTimer--;
		else {
			ramTimer = 180;
			System.out.println(Gdx.app.getJavaHeap());
			System.out.println(Gdx.app.getNativeHeap());
		}
	}

    /**
     * Called when game resets. Clears settings and stats.
     */
	public void resetGame() {
		//settings.clear(); Don't clear them anymore, so you can change language for the tutorial
		saveSettings();
		prefsStats.clear();
		prefsStats.flush();
		resetedGame = true;
		saveAchievements();
		initAfterRestarting();
	}

    /**
     * Select bossmusic.
     */
	private void selectLoadedBossMusic() {
		// If none of the music has been played, choose random music, else choose latest selection
		if (arrPlayedMusicSize == 0) selectRandomBossMusic();
		else curBossMusic = files.allBossMusic[arrPlayedMusicSize-1];

		// If pool is 4, then boss music is always Fabio
		if (pool == 4) curBossMusic = files.musBossFabio;
	}

    /**
     * Select random bossmusic.
     */
	private void selectRandomBossMusic() {
		int all = files.allBossMusic.length;
		if (arrPlayedMusic.size() == all) arrPlayedMusic.clear();
		int random;
		while (true) {
			random = MathUtils.random(0, all - 1);
			if (!arrPlayedMusic.contains(random)) {
				arrPlayedMusic.add(random);
				break;
			}
		}

		curBossMusic = files.allBossMusic[random];
	}

	/**
	 * Check current date. If date is different than saved date, then give random amount of money.
	 */
	private void checkDate() {
		GregorianCalendar calendarG = new GregorianCalendar();
		calendarG.setTime(new Date());
		int currentDate = calendarG.get(Calendar.DAY_OF_YEAR);
		if (currentDate != prevDayGift) {
			prevDayGift = currentDate;
			money += MathUtils.random(10, 15);
		}
	}

	/**
	 * If assets are loading, display splash screen.
	 */
	private void controlSplashScreen() {
		AssetManager man = assetHandler.manager;
		// If Asset Manager has finished loading, initialize game values and move to RoomGame
		if (assetHandler.manager.update()) {
			if (!assetsLoaded) {
				initAfterLoaded();
				// Remove splash screen from memory, it's not needed after this
				man.unload(assetHandler.splashFI1);
				man.unload(assetHandler.splashFI2);
				man.unload(assetHandler.splashFI3);
				man.unload(assetHandler.splashFI4);
				man.unload(assetHandler.splashEN1);
				man.unload(assetHandler.splashEN2);
				man.unload(assetHandler.splashEN3);
				man.unload(assetHandler.splashEN4);
			}
		} else {
			// Check if assets are loaded, then add them to array
			if (splashScreen == null) {
				if (language.equals("fi")) {
					if (man.isLoaded(assetHandler.splashFI1) &&
							man.isLoaded(assetHandler.splashFI2) &&
							man.isLoaded(assetHandler.splashFI3) &&
							man.isLoaded(assetHandler.splashFI4)) {
						splashScreen = new Texture[] {
								man.get(assetHandler.splashFI1), man.get(assetHandler.splashFI2),
								man.get(assetHandler.splashFI3), man.get(assetHandler.splashFI4)};
					}
				} else {
					if (man.isLoaded(assetHandler.splashEN1) &&
							man.isLoaded(assetHandler.splashEN2) &&
							man.isLoaded(assetHandler.splashEN3) &&
							man.isLoaded(assetHandler.splashEN4)) {
						splashScreen = new Texture[] {
								man.get(assetHandler.splashEN1), man.get(assetHandler.splashEN2),
								man.get(assetHandler.splashEN3), man.get(assetHandler.splashEN4)};
					}
				}
			} else {
				// Loop through splash screen images to make an animation
				if (switchSplashTimer > 0) switchSplashTimer--;
				else {
					switchSplashTimer = 8;
					currentSplash++;
					if (currentSplash == splashScreen.length) currentSplash = 0;
				}
				batch.begin();
				batch.draw(splashScreen[currentSplash], 0, 0);
				batch.end();
			}
		}
	}

	boolean haveWeChangedTheRoom = false;

    /**
     * Transition, when changing room.
     */
	public void transition() { haveWeChangedTheRoom = true; }

    /**
     * Called when game switches to hallway.
     */
    public void switchToRoomGame() {
		transition();
		startMusic(files.musMainTheme);
	    RoomGame room = new RoomGame(this);
        setScreen(room);
        curRoom = ROOM_GAME;
        saveStats();
    }

    /**
     * Called when game switches to fight.
     */
    public void switchToRoomFight(boolean skipButton) {
		transition();
		startMusic(curBossMusic);
		RoomFight room = new RoomFight(this);
		setScreen(room);
		curRoom = ROOM_FIGHT;
		saveStats();
		walkingSkipped = skipButton;

    	if (!skipButton) {
    		stepCount = progressBarMilestone / 2;
			stepCount = Math.round(stepCount);

			// Add money for trying fighting
			if (pool == 2) money += MathUtils.random(3, 7);
			else if (pool == 3) money += MathUtils.random(8, 12);
			else if (pool == 4) money += MathUtils.random(13, 17);

			if (!difficulty.equals(HARD)) checkHard = false;
		} else {
    		checkHard = false;
		}
    }

    /**
     * Called when game ends.
     */
	public void switchToRoomEnd() {
		transition();
		startMusic(files.musMainTheme);
		RoomEnd room = new RoomEnd(this);
		setScreen(room);
		curRoom = ROOM_END;
		saveStats();
	}

	/**
	 * If selected music file is not playing, stop the current music and play the selected file.
	 * @param file selected music file
	 */
	private void startMusic(Music file) {
		if (file != curMusic) {
			if (curMusic != null) curMusic.stop();
			playMusic(file);
		}
	}

	/**
	 * Set correct volume for file, loop it and play it.
	 * @param file selected music file
	 */
	public void playMusic(Music file) {
		file.setVolume(musicVol);
		file.setLooping(true);
		file.play();
		curMusic = file;
	}

	/**
	 * Change music volume. Mainly used in settings.
	 * @param musicVol wanted volume
	 */
	public void setMusicVol(float musicVol) {
		this.musicVol = musicVol;
		curMusic.setVolume(musicVol);
	}

	/**
	 * Play selected sound using correct sound volume.
	 * @param file selected sound file
	 */
	public void playSound(Sound file) {
		file.play(soundVol);
	}

	/**
	 * Sets sound volume variable. Mainly used in Settings.
	 * @param soundVol wanted sound volume
	 */
	public void setSoundVol(float soundVol) {
		this.soundVol = soundVol;
	}

    /**
     * Create variables for hacking.
     */
    private void createHackFiles() {
        hackPosX = new FloatArray();
        hackPosY = new FloatArray();
        innerPosX = new FloatArray();
        innerPosY = new FloatArray();
        if (hackFirstTry) {
            if (pool == 1) {
                hackShieldAmount = pool1HackShieldAmount;
            }
            if (pool == 2) {
                hackShieldAmount = pool2HackShieldAmount;
            }
            if (pool == 3) {
                hackShieldAmount = pool3HackShieldAmount;
                innerHackShieldAmount = pool3InnerHackShieldAmount;
            }
        }
    }

    /**
     * Create variables for dialogs.
     */
	private void createDialogConstants() {
		fontSteps = new BitmapFont(Gdx.files.internal("stepfont/stepfont.fnt"),
				Gdx.files.internal("stepfont/stepfont.png"),
				false);
		descriptionFont = new BitmapFont(Gdx.files.internal("descriptionfont/descriptionfont.fnt"),
                Gdx.files.internal("descriptionfont/descriptionfont.png"),
                false);
		testButtonAtlas = new TextureAtlas("testbuttons/actionButtons.pack");
		emptyWindowStyle = new Window.WindowStyle(fontSteps, fontColor, null);
		labelStyle = new Label.LabelStyle(fontSteps, fontColor);
		descriptionLabelStyle = new Label.LabelStyle(descriptionFont, fontColor);
		dialog = new UtilDialog(this);
	}

    /**
     * Create variables for progressbar.
     */
	private void createProgressBarFiles() {
	    progBarAtlas = new TextureAtlas("progressbar/progressbar.pack");
	    progBarSkin = new Skin();
	    progBarSkin.addRegions(progBarAtlas);

	    progBarStyle = new ProgressBar.ProgressBarStyle();
	    progBarStyle.knob = progBarSkin.getDrawable("tripmarker");
	    progBarStyle.background = progBarSkin.getDrawable("tripmeter");
    }

    /**
     * Choose milestone depending on the difficulty.
     */
    private void chooseNextMilestone() {
        // If it's the first boss, make milestone 20
		if (pool == 1) progressBarMilestone = poolMilestones[poolMult];
		else if (pool == 2) progressBarMilestone = 2000;
		else if (pool == 3) progressBarMilestone = 3000;
		else if (pool == 4) progressBarMilestone = 4000;
        if (difficulty.equals(EASY)) progressBarMilestone *= 0.5;
        else if (difficulty.equals(MEDIUM)) progressBarMilestone *= 1;
		else if (difficulty.equals(HARD)) progressBarMilestone *= 1.5;
    }

    /**
     * Called when changing difficulty.
     * @param dif difficulty
     */
    public void changeDifficulty(String dif) {
		difficulty = dif;
		chooseNextMilestone();
	}

	/**
	 * Create bundle for localization. If language is set in settings, retrieve that language.
	 */
    public void createBundle() {
		Locale locale;
		// If language is not set, then the game gets software's default language
		if (language.equals("fi")) locale = new Locale("fi", "FI");
		else if (language.equals("en")) locale = new Locale("", "");
		else locale = Locale.getDefault();

		localize = I18NBundle.createBundle(Gdx.files.internal("MyBundle"),
				locale, "ISO-8859-1");
		// After initializing localize, make language variable the chosen one
		if (locale.getLanguage().equals("fi")) language = "fi";
		else language = "en";
	}

	/**
	 * Change language to finnish and restart the game. Used in settings.
	 */
	public void languageToFIN() {
		Locale locale = new Locale("fi", "FI");
		localize = I18NBundle.createBundle(Gdx.files.internal("MyBundle"),
				locale, "ISO-8859-1");
		language = "fi";
		saveSettings();
		saveStats();
		initAfterRestarting();
	}

	/**
	 * Change language to english and restart the game. Used in settings.
	 */
	public void languageToENG() {
		Locale locale = new Locale("", "");
		localize = I18NBundle.createBundle(Gdx.files.internal("MyBundle"),
				locale, "ISO-8859-1");
		language = "en";
		saveSettings();
		saveStats();
		initAfterRestarting();
	}

	/**
	 * Load settings values.
	 */
	public void loadSettings() {
		settings = Gdx.app.getPreferences("Robot_Mayhem_Settings");
		musicVol = settings.getFloat(keyMusicVol, 0.8f);
		soundVol = settings.getFloat(keySoundVol, 0.8f);
		language = settings.getString(keyLanguage, "");
		difficulty = settings.getString(keyDifficulty, MEDIUM);
    }

	/**
	 * Save settings values.
	 */
	public void saveSettings() {
	    settings.putFloat(keyMusicVol, musicVol);
		settings.putFloat(keySoundVol, soundVol);
	    settings.putString(keyLanguage, language);
	    settings.putString(keyDifficulty, difficulty);
	    settings.flush();
    }

	/**
	 * Save stats every time timer is 0, then reset timer.
	 */
    public void controlSaveTimer() {
		if (saveTimer > 0) saveTimer--;
		else {
			System.out.println("saved");
			saveTimer = saveTimerAmount;
			if (stats != null) saveStats();
		}
	}

	/**
	 * Save stats every time counter is 0, then reset counter.
	 */
	public void controlStepSaveCounter() {
		if (stepSaveCounter > 0) stepSaveCounter--;
		else {
			System.out.println("saved");
			stepSaveCounter = stepSaveAmount;
			if (stats != null) saveStats();
		}
	}

	/**
	 * Load correct preferences for statistics and initialize SaveAndLoad class for handling
	 * loading and saving plus encryption and decryption of file.
	 */
	private void initStats() {
		inventory = new ArrayList<String>();
		defeatedBosses = new ArrayList<String>();
		arrPlayedMusic = new ArrayList<Integer>();
		boughtPermanent = new ArrayList<String>();
		achievComplete = new ArrayList<String>();
		hasCollected = new ArrayList<String>();
		prefsStats = Gdx.app.getPreferences("Robot_Mayhem_Stats");
		stats = new SaveAndLoad(E, prefsStats);
		prefsAchievs = Gdx.app.getPreferences("Robot_Mayhem_Achievements");
		achievs = new SaveAndLoad(E, prefsAchievs);
	}

	/**
	 * Load statistics.
	 */
	public void loadStats() {
		// NOTE: Remember to write 0f instead of 0 to float defValues, otherwise loading crashes
		money = stats.loadValue(keyMoney, 0);
		stepCount = stats.loadValue(keyStepCount, 0f);
		stepAllCount = stats.loadValue(keyStepAllCount, 0f);
		stepBank = stats.loadValue(keyStepBank, 0f);
		stepBankSize = stats.loadValue(keyStepBankSize, 3000f);
		skill1 = stats.loadValue(keySkill1, skills.REPAIR);
		skill2 = stats.loadValue(keySkill2, "");
		currentBoss = stats.loadValue(keyCurrentBoss, bosses.ROOMBOT);
		finishedGame = stats.loadValue(keyFinishedGame, false);
		checkHard = stats.loadValue(keyCheckHard, true);
		finishedGameHard = stats.loadValue(keyFinishedGameHard, false);
		pool = stats.loadValue(keyPool, 1);
		poolMult = stats.loadValue(keyPoolMult, 0);
		playerName = stats.loadValue(keyName, "");
		fightsWon = stats.loadValue(keyFightsWon, 0);
		prevDayGift = stats.loadValue(keyPrevDayGift, -1);
		buyedItemsCounter = stats.loadValue(keyBuyedItemsCounter, 0);
		gameCompleteCounter = stats.loadValue(keyGameCompleteCounter, 0);
		hardBugFix = stats.loadValue(keyHardBugFix, 0);

		// Boosts and item values
		critBoost = stats.loadValue(keyCritBoost, 0);
		missBoost = stats.loadValue(keyMissBoost, 0);
		dmgBoost = stats.loadValue(keyDmgBoost, 0f);
		armorBoost = stats.loadValue(keyArmorBoost, 0f);
		healBoost = stats.loadValue(keyHealBoost, 0f);
		permaCritBoost = stats.loadValue(keyPermanentCritBoost, 0);
		permaMissBoost = stats.loadValue(keyPermanentMissBoost, 0);
		permaDmgBoost = stats.loadValue(keyPermanentDmgBoost, 0f);
		permaArmorBoost = stats.loadValue(keyPermanentArmorBoost, 0f);
		permaHealBoost = stats.loadValue(keyPermanentHealBoost, 0f);
		reflectiveShield = stats.loadValue(keyReflectiveShield, false);

		// Tutorial // REMEMBER TO CHANGE THESE TO TRUE
		firstPlayTime = stats.loadValue(keyFirstPlayTime, true);
		firstPlayTimeFight = stats.loadValue(keyFirstPlayTimeFight, true);
		firstPlayInventory = stats.loadValue(keyFirstPlayInventory, true);
		firstPlaySettings = stats.loadValue(keyFirstPlaySettings, true);
		firstPlayBank = stats.loadValue(keyFirstPlayBank, true);
		firstPlayVictory = stats.loadValue(keyFirstPlayVictory, true);
		firstPlayPoolComplete1 = stats.loadValue(keyFirstPlayPoolComplete1, true);
		firstPlayPoolComplete2 = stats.loadValue(keyFirstPlayPoolComplete2, true);
		firstPlayPoolComplete3 = stats.loadValue(keyFirstPlayPoolComplete3, true);
		firstPlayMoney = stats.loadValue(keyFirstPlayMoney, true);
		firstPlayEscape = stats.loadValue(keyFirstPlayEscape, true);
		firstPlayDeath = stats.loadValue(keyFirstPlayDeath, true);
		firstPlayNewGamePlus = stats.loadValue(keyFirstPlayNewGamePlus, true);
		firstPlayFinalFightStart = stats.loadValue(keyFirstPlayFinalFightStart, true);
		firstPlaySkip = stats.loadValue(keyFirstPlaySkip, true);

		// Load the size of inventory before loading inventory items
		inventorySize = stats.loadValue(keyInventorySize, 0);
		for (int i = 0; i < inventorySize; i++) {
			inventory.add(i, stats.loadValue(keyInventory + String.valueOf(i), ""));
		}
		// Defeated bosses
		defeatedBossesSize = stats.loadValue(keyDefeatedBossesSize, 0);
		for (int i = 0; i < defeatedBossesSize; i++) {
			defeatedBosses.add(i, stats.loadValue(keyDefeatedBosses + String.valueOf(i), ""));
		}
		// Played boss music
		arrPlayedMusicSize = stats.loadValue(keyArrPlayedMusicSize, 0);
		for (int i = 0; i < arrPlayedMusicSize; i++) {
			arrPlayedMusic.add(i, stats.loadValue(keyArrPlayedMusic + String.valueOf(i), 0));
		}
		// Bought permanent items
        boughtPermanentSize = stats.loadValue(keyBoughtPermanentSize, 0);
		for (int i = 0; i < boughtPermanentSize; i++) {
		    boughtPermanent.add(i, stats.loadValue(keyBoughtPermanent + String.valueOf(i), ""));
        }
		selectLoadedBossMusic();

		fixHardAchievement();
	}

	/**
	 * Save statistics.
	 */
	public void saveStats() {
		stats.saveValue(keyMoney, money);
		stats.saveValue(keyStepCount, stepCount);
		stats.saveValue(keyStepAllCount, stepAllCount);
		stats.saveValue(keyStepBank, stepBank);
		stats.saveValue(keyStepBankSize, stepBankSize);
		stats.saveValue(keySkill1, skill1);
		stats.saveValue(keySkill2, skill2);
		stats.saveValue(keyCurrentBoss, currentBoss);
		stats.saveValue(keyFinishedGame, finishedGame);
		stats.saveValue(keyCheckHard, checkHard);
		stats.saveValue(keyFinishedGameHard, finishedGameHard);
		stats.saveValue(keyPool, pool);
		stats.saveValue(keyPoolMult, poolMult);
		stats.saveValue(keyName, playerName);
		stats.saveValue(keyFightsWon, fightsWon);
		stats.saveValue(keyPrevDayGift, prevDayGift);
		stats.saveValue(keyBuyedItemsCounter, buyedItemsCounter);
		stats.saveValue(keyGameCompleteCounter, gameCompleteCounter);
		stats.saveValue(keyHardBugFix, hardBugFix);

		// Boosts and item values
		stats.saveValue(keyCritBoost, critBoost);
		stats.saveValue(keyMissBoost, missBoost);
		stats.saveValue(keyDmgBoost, dmgBoost);
		stats.saveValue(keyArmorBoost, armorBoost);
		stats.saveValue(keyHealBoost, healBoost);
		stats.saveValue(keyPermanentCritBoost, permaCritBoost);
		stats.saveValue(keyPermanentMissBoost, permaMissBoost);
		stats.saveValue(keyPermanentDmgBoost, permaDmgBoost);
		stats.saveValue(keyPermanentArmorBoost, permaArmorBoost);
		stats.saveValue(keyPermanentHealBoost, permaHealBoost);
		stats.saveValue(keyReflectiveShield, reflectiveShield);

		// Tutorial
		stats.saveValue(keyFirstPlayTime, firstPlayTime);
		stats.saveValue(keyFirstPlayTimeFight, firstPlayTimeFight);
		stats.saveValue(keyFirstPlayInventory, firstPlayInventory);
		stats.saveValue(keyFirstPlaySettings, firstPlaySettings);
		stats.saveValue(keyFirstPlayBank, firstPlayBank);
		stats.saveValue(keyFirstPlayVictory, firstPlayVictory);
		stats.saveValue(keyFirstPlayPoolComplete1, firstPlayPoolComplete1);
		stats.saveValue(keyFirstPlayPoolComplete2, firstPlayPoolComplete2);
		stats.saveValue(keyFirstPlayPoolComplete3, firstPlayPoolComplete3);
		stats.saveValue(keyFirstPlayMoney, firstPlayMoney);
		stats.saveValue(keyFirstPlayDeath, firstPlayDeath);
		stats.saveValue(keyFirstPlayEscape, firstPlayEscape);
		stats.saveValue(keyFirstPlayNewGamePlus, firstPlayNewGamePlus);
		stats.saveValue(keyFirstPlayFinalFightStart, firstPlayFinalFightStart);
		stats.saveValue(keyFirstPlaySkip, firstPlaySkip);

		// Save inventory's current size on inventorySize key
		stats.saveValue(keyInventorySize, inventory.size());
		for (int i = 0; i < inventory.size(); i++) {
			stats.saveValue(keyInventory + String.valueOf(i), inventory.get(i));
		}
		// Defeated bosses
		stats.saveValue(keyDefeatedBossesSize, defeatedBosses.size());
		for (int i = 0; i < defeatedBosses.size(); i++) {
			stats.saveValue(keyDefeatedBosses + String.valueOf(i), defeatedBosses.get(i));
		}
		// Played boss music
		stats.saveValue(keyArrPlayedMusicSize, arrPlayedMusic.size());
		for (int i = 0; i < arrPlayedMusic.size(); i++) {
			stats.saveValue(keyArrPlayedMusic + String.valueOf(i), arrPlayedMusic.get(i));
		}
		// Bought permanent items
        stats.saveValue(keyBoughtPermanentSize, boughtPermanent.size());
		for (int i = 0; i < boughtPermanent.size(); i++) {
		    stats.saveValue(keyBoughtPermanent + String.valueOf(i), boughtPermanent.get(i));
        }

		prefsStats.flush();
	}

	/**
	 * Rewards everyone who has completed the game and reached potential steps with hard
	 * achievement. Also resets checkHard if someone's hard run is currently running.
	 */
	private void fixHardAchievement() {
		if (hardBugFix != fightsWon) {
			hardBugFix = fightsWon;
			checkHard = true;
			if (fightsWon >= 19 && stepAllCount >= 56055) finishedGameHard = true;
			// 30, 150, 375, 750, 1500, 2250, 3000..., 4500..., 6000
		}
	}

    /**
     * Loads achievements.
     */
	public void loadAchievements() {
        // Change default-value when adding new achievements.
        achievCompleteSize = stats.loadValue(keyAchievCompleteSize, 12);
        for (int i = 0; i < achievCompleteSize; i++) {
            achievComplete.add(i, achievs.loadValue(keyAchievComplete + String.valueOf(i), "locked"));
            hasCollected.add(i, achievs.loadValue(keyHasCollected + String.valueOf(i), "false"));
        }
        resetedGame = achievs.loadValue(keyResetedGame, false);
    }

    /**
     * Saves achievements.
     */
    public void saveAchievements() {
        achievs.saveValue(keyAchievCompleteSize, achievComplete.size());
        for (int i = 0; i < achievComplete.size(); i++) {
            achievs.saveValue(keyAchievComplete + String.valueOf(i), achievComplete.get(i));
            achievs.saveValue(keyHasCollected + String.valueOf(i), hasCollected.get(i));
        }
        achievs.saveValue(keyResetedGame, resetedGame);
	    prefsAchievs.flush();
    }

    /**
     * Called when boss is defeated.
     */
	public void bossDefeated() {
		defeatedBosses.add(currentBoss);

		stepCount = 0; // Reset step count
		int extra = 0;
		if (difficulty.equals(HARD)) extra = 5;
		money += MathUtils.random(5 + extra, 10 + extra);
		fightsWon ++;
		hardBugFix = fightsWon;
		poolMult++;

		// Reset boost values
		critBoost = 0;
		missBoost = 0;
		dmgBoost = 0;
		armorBoost = 0;
		healBoost = 0;

		currentBoss = bosses.selectRandomBoss(); // Randomize new boss
		selectRandomBossMusic(); // Randomize new song

		// If all the bosses of the pool defeated, reset bosses, else add number++
		if (poolMult < bosses.poolBossesSize) {
			while (defeatedBosses.contains(currentBoss)) {
				currentBoss = bosses.selectRandomBoss();
			}
		} else {
			defeatedBosses.clear();
			pool++;
			poolMult = 0;
			// If last pool, then Fabio remains
			if (pool == 4) {
				poolMult = bosses.poolBossesSize-1;
				currentBoss = bosses.FABIO;
				curBossMusic = files.musBossFabio;
			}
		}
		// Add later code what happen after Fabio is defeated

		if (pool < 5) switchToRoomGame();
		else gameFinished();

		chooseNextMilestone();
	}

	/**
	 * Do everything what happens after game is finished here.
	 */
	private void gameFinished() {
		// Reset necessary values
		pool = 1;
		poolMult = 0;
		defeatedBosses.clear();
		currentBoss = bosses.selectRandomBoss(); // Randomize new boss
		selectRandomBossMusic(); // Randomize new song

		// Add to game complete counter
		gameCompleteCounter++;

		// Dialogues
		firstPlayVictory = true;
		firstPlayPoolComplete1 = true;
		firstPlayPoolComplete2 = true;
		firstPlayPoolComplete3 = true;
		firstPlayFinalFightStart = true;

		// Achievements
		finishedGame = true;
		if (checkHard) finishedGameHard = true;
		checkHard = true;

		// Change to RoomEnd
		switchToRoomEnd();
	}

    /**
     * Called when adding item/skill to inventory.
     * @param name name of the item/skill
     * @param isSkill check, if it's skill
     */
	public void addToInventory(String name, boolean isSkill) {
		// If it's skill and either skill1 or 2 is empty, add it instantly
		if (isSkill) {
			if (skill1.equals("")) skill1 = name;
			else if (skill2.equals("")) skill2 = name;
			else inventory.add(name);
		} else {
			inventory.add(name);
		}
		saveStats();
	}

    /**
     * Called when removing items from inventory
     * @param name name of the item
     */
	public void removeFromInventory(String name) {
        inventory.remove(name);
        saveStats();
    }

    /**
     * Check if inventory contains specific item/skill.
     * @param name name of the item/skill
     * @return return true/false
     */
	public boolean inventoryOrSkillsContains(String name) {
		boolean contains = false;

		if (inventory.contains(name)) contains = true;
		if (skill1.equals(name) || skill2.equals(name)) contains = true;

		return contains;
	}

    /**
     * Called, when player buys permanent item.
     * @param name name of the item
     */
	public void addToBoughtPermanent(String name) {
	    boughtPermanent.add(name);
	    saveStats();
    }

    /**
     * Creates skin and stage.
     */
    private void createSkinAndStage() {
        stage = new Stage(fitViewport, batch);

		skin = files.skin;
		finalSkin = files.finalSkin;
    }

    /**
     * Receive steps on Desktop, if milestone is not reached, else add them to stepBank.
     */
    public void simulateStep() {
		controlStepSaveCounter();
    	if (!pauseWalking) {
			stepAllCount++;
			if (stepCount < progressBarMilestone) this.stepCount++;
			else if (stepBank < stepBankSize) stepBank++;
		}
	}

    /**
     * Receive steps on Android, if milestone is not reached, else add them to stepBank.
     * @param stepCount stepCount
     */
	public void receiveSteps(float stepCount) {
		controlStepSaveCounter();
		if (!pauseWalking) {
			stepAllCount++;
			if (this.stepCount < progressBarMilestone && curRoom == ROOM_GAME) this.stepCount++;
			else stepBank++;
		}
	}

    /**
     * Deleted steps from bank, when retrieving them on RoomGame.
     * @param amount amount of the steps
     */
	public void retrieveFromBank(float amount) {
		stepBank -= amount;
		if (stepBank < 0) stepBank = 0;
	}

    /**
     * Called when adding money.
     * @param amount amount of money
     */
	public void addMoney(int amount) {
		money += amount;
	}

    /**
     * Called when decreasing money.
     * @param amount amount of money
     */
	public void decreaseMoney(int amount) {
	    money -= amount;
    }

    /**
     * Add critical boost.
     * @param amount amount of boost
     */
    public void addCritBoost(int amount) {
		critBoost += amount;
	}

    /**
     * Add miss boost
     * @param amount amount of boost
     */
	public void addMissBoost(int amount) {
		missBoost += amount;
	}

    /**
     * Add damage boost
     * @param amount amount of boost
     */
	public void addDmgBoost(double amount) {
		dmgBoost += amount;
	}

    /**
     * Add armor boost
     * @param amount amount of boost
     */
	public void addArmorBoost(double amount) {
		armorBoost += amount;
	}

    /**
     * Add heal boost
     * @param amount amount of boost
     */
	public void addHealBoost(double amount) {
		healBoost += amount;
	}

    /**
     * Add permacrit boost
     * @param amount amount of boost
     */
	public void addPermaCritBoost(int amount) {
		permaCritBoost += amount;
	}

    /**
     * Add permamiss boost
     * @param amount amount of boost
     */
	public void addPermaMissBoost(int amount) {
		permaMissBoost += amount;
	}

    /**
     * Add permadmg boost
     * @param amount amount of boost
     */
	public void addPermaDmgBoost(double amount) {
		permaDmgBoost += amount;
	}

    /**
     * Add perma-armor boost
     * @param amount amount of boost
     */
	public void addPermaArmorBoost(double amount) {
		permaArmorBoost += amount;
	}

    /**
     * Add permaheal boost
     * @param amount amount of boost
     */
	public void addPermaHealBoost(double amount) {
		permaHealBoost += amount;
	}

    /**
     * Increase stepbank size
     * @param amount amount of size
     */
	public void increaseStepBankSize(float amount) {
		stepBankSize += amount;
	}

	/*
	GETTERS AND SETTERS
	 */

    /**
     * Set boss skills
     * @param skill0 skill0
     * @param skill1 skill1
     * @param skill2 skill2
     */
	public void setCurBossSkills (String skill0, String skill1, String skill2) {
		curBossSkills[0] = skill0;
		curBossSkills[1] = skill1;
		curBossSkills[2] = skill2;
	}

    /**
     * Get boss's current skills
     * @return skills
     */
	public String[] getCurBossSkills() {
		return curBossSkills;
	}

    /**
     * Set pause walking
     * @param pauseWalking pausewalking
     */
	public void setPauseWalking(boolean pauseWalking) {
		this.pauseWalking = pauseWalking;
	}

    /**
     * Get pause walking
     * @return pausewalking
     */
	public boolean isPauseWalking() {
		return pauseWalking;
	}

    /**
     * Get critical boost
     * @return critical boost
     */
	public int getCritBoost() {
		int wholeBoost = critBoost + permaCritBoost;
		return wholeBoost;
	}

    /**
     * Get miss boost
     * @return miss boost
     */
	public int getMissBoost() {
		int wholeBoost = missBoost + permaMissBoost;
		return wholeBoost;
	}

    /**
     * Get damage boost
     * @return damage boost
     */
	public float getDmgBoost() {
		float wholeBoost = dmgBoost + permaDmgBoost;
		return wholeBoost;
	}

    /**
     * Get armor boost
     * @return armor boost
     */
	public float getArmorBoost() {
		float wholeBoost = armorBoost + permaArmorBoost;
		return wholeBoost;
	}

    /**
     * Get heal boost
     * @return heal boost
     */
	public float getHealBoost() {
		float wholeBoost = healBoost + permaHealBoost;
		return wholeBoost;
	}

    /**
     * Get overall critical
     * @return overall critical boost
     */
	public int getOverallBstCrit() {
		return overallBstCrit;
	}

    /**
     * Set overall critical
     * @param overallBstCrit overall critical boost
     */
	public void setOverallBstCrit(int overallBstCrit) {
		this.overallBstCrit = overallBstCrit;
	}

    /**
     * Get overall miss
     * @return overall miss boost
     */
	public int getOverallBstMiss() {
		return overallBstMiss;
	}

    /**
     * Set overall miss
     * @param overallBstMiss overall miss boost
     */
	public void setOverallBstMiss(int overallBstMiss) {
		this.overallBstMiss = overallBstMiss;
	}

    /**
     * Get overall damage
     * @return overall damage boost
     */
	public float getOverallBstDmg() {
		return overallBstDmg;
	}

    /**
     * Set overall damage
     * @param overallBstDmg overall damage boost
     */
	public void setOverallBstDmg(double overallBstDmg) {
		Float converted = (float) overallBstDmg;
		this.overallBstDmg = converted;
	}

    /**
     * Get overall armor
     * @return overall boost damage
     */
	public float getOverallBstArmor() {
		return overallBstArmor;
	}

    /**
     * Set overall armor
     * @param overallBstArmor overall armor boost
     */
	public void setOverallBstArmor(double overallBstArmor) {
		Float converted = (float) overallBstArmor;
		this.overallBstArmor = converted;
	}

    /**
     * Get overall heal
     * @return overall heal boost
     */
	public float getOverallBstHeal() {
		return overallBstHeal;
	}

    /**
     * Set overall heal
     * @param overallBstHeal overall heal boost
     */
	public void setOverallBstHeal(double overallBstHeal) {
		Float converted = (float) overallBstHeal;
		this.overallBstHeal = converted;
	}

    /**
     * Get batch
     * @return batch
     */
	public SpriteBatch getBatch() {
		return batch;
	}

    /**
     * Get camera
     * @return camera
     */
	public OrthographicCamera getCamera() {
		return camera;
	}

    /**
     * Get stage
     * @return stage
     */
	public Stage getStage() {
		return stage;
	}

    /**
     * Get skin
     * @return skin
     */
	public Skin getSkin() {
		return skin;
	}

    /**
     * Get musicvol
     * @return musicvol
     */
	public float getMusicVol() {
		return musicVol;
	}

    /**
     * Get localize
     * @return localize
     */
	public I18NBundle getLocalize() {
		return localize;
	}

    /**
     * Get progressbarstyle
     * @return progbarstyle
     */
	public ProgressBar.ProgressBarStyle getProgBarStyle() {
        return progBarStyle;
    }

    /**
     * Get fontsteps
     * @return fontsteps
     */
	public BitmapFont getFontSteps() {
	    return fontSteps;
    }

    /**
     * Get descriptionfont
     * @return descriptionfont
     */
    public BitmapFont getDescriptionFont() {
	    return descriptionFont;
    }

    /**
     * Get stepcount
     * @return stepcount
     */
	public float getStepCount() {
		return stepCount;
	}

    /**
     * Set stepcount
     * @param stepCount stepcount
     */
	public void setStepCount(float stepCount) {
		this.stepCount = stepCount;
	}

    /**
     * Get stepbank
     * @return stepbank
     */
	public float getStepBank() {
		return stepBank;
	}

    /**
     * Set stepbank
     * @param stepBank stepbank
     */
	public void setStepBank(float stepBank) {
		this.stepBank = stepBank;
	}

    /**
     * Get stepallcount
     * @return stepallcount
     */
	public float getStepAllCount() {
		return stepAllCount;
	}

    /**
     * Get skill1
     * @return skill1
     */
	public String getSkill1() {
		return skill1;
	}

    /**
     * Set skill1
     * @param skill1 skill1
     */
	public void setSkill1(String skill1) {
		this.skill1 = skill1;
	}

    /**
     * Get skill2
     * @return skill2
     */
	public String getSkill2() {
		return skill2;
	}

    /**
     * Set playername
     * @param playerName playername
     */
	public void setPlayerName(String playerName) {
	    this.playerName = playerName;
    }

    /**
     * Get playername
     * @return playername
     */
	public String getPlayerName() {
		return playerName;
	}

    /**
     * Set skill2
     * @param skill2 skill2
     */
	public void setSkill2(String skill2) {
		this.skill2 = skill2;
	}

    /**
     * Set firstplaytime
     * @param firstPlayTime firstplaytime
     */
	public void setFirstPlayTime(boolean firstPlayTime) {
	    this.firstPlayTime = firstPlayTime;
    }

    /**
     * Get firstplaytime
     * @return firstplaytime
     */
	public boolean isFirstPlayTime() {
		return firstPlayTime;
	}

    /**
     * Set firstPlayTimeFight
     * @param firstPlayTimeFight firstPlayTimeFight
     */
	public void setfirstPlayTimeFight(boolean firstPlayTimeFight) {
	    this.firstPlayTimeFight = firstPlayTimeFight;
    }

    /**
     * Get firstPlayTimeFight
     * @return firstPlayTimeFight
     */
    public boolean isFirstPlayTimeFight() {
	    return firstPlayTimeFight;
    }

    /**
     * Set firstPlayInventory
     * @param firstPlayInventory firstPlayInventory
     */
    public void setFirstPlayInventory(boolean firstPlayInventory) {
	    this.firstPlayInventory = firstPlayInventory;
    }

    /**
     * Get firstPlayInventory
     * @return firstPlayInventory
     */
    public boolean isFirstPlayInventory() {
	    return firstPlayInventory;
    }

	/**
	 * Get firstPlayInventory
	 * @return firstPlayInventory
	 */
    public boolean isFirstPlaySettings() {
		return firstPlaySettings;
	}

	/**
	 * Set firstPlayInventory
	 * @param firstPlaySettings firstPlaySettings
	 */
	public void setFirstPlaySettings(boolean firstPlaySettings) {
		this.firstPlaySettings = firstPlaySettings;
	}

	/**
     * Get firstPlayBank
     * @return firstPlayBank
     */
	public boolean isFirstPlayBank() {
		return firstPlayBank;
	}

    /**
     * Set firstPlayBank
     * @param firstPlayBank firstPlayBank
     */
	public void setFirstPlayBank(boolean firstPlayBank) {
		this.firstPlayBank = firstPlayBank;
	}

    /**
     * Get firstPlayVictory
     * @return firstPlayVictory
     */
	public boolean isFirstPlayVictory() {
		return firstPlayVictory;
	}

    /**
     * Set firstPlayVictory
     * @param firstPlayVictory firstPlayVictory
     */
	public void setFirstPlayVictory(boolean firstPlayVictory) {
		this.firstPlayVictory = firstPlayVictory;
	}

    /**
     * Get firstPlayPoolComplete1
     * @return firstPlayPoolComplete1
     */
	public boolean isFirstPlayPoolComplete1() {
		return firstPlayPoolComplete1;
	}

    /**
     * Set firstPlayPoolComplete1
     * @param firstPlayPoolComplete1 firstPlayPoolComplete1
     */
	public void setFirstPlayPoolComplete1(boolean firstPlayPoolComplete1) {
		this.firstPlayPoolComplete1 = firstPlayPoolComplete1;
	}

    /**
     * Get firstPlayPoolComplete2
     * @return firstPlayPoolComplete2
     */
	public boolean isFirstPlayPoolComplete2() {
		return firstPlayPoolComplete2;
	}

    /**
     * Set firstPlayPoolComplete2
     * @param firstPlayPoolComplete2 firstPlayPoolComplete2
     */
	public void setFirstPlayPoolComplete2(boolean firstPlayPoolComplete2) {
		this.firstPlayPoolComplete2 = firstPlayPoolComplete2;
	}

    /**
     * Get firstPlayPoolComplete3
     * @return firstPlayPoolComplete3
     */
	public boolean isFirstPlayPoolComplete3() {
		return firstPlayPoolComplete3;
	}

    /**
     * Set firstPlayPoolComplete3
     * @param firstPlayPoolComplete3 firstPlayPoolComplete3
     */
	public void setFirstPlayPoolComplete3(boolean firstPlayPoolComplete3) {
		this.firstPlayPoolComplete3 = firstPlayPoolComplete3;
	}

    /**
     * Get firstPlayMoney
     * @return firstPlayMoney
     */
    public boolean isFirstPlayMoney() {
        return firstPlayMoney;
    }

    /**
     * Set firstPlayMoney
     * @param firstPlayMoney firstPlayMoney
     */
    public void setFirstPlayMoney(boolean firstPlayMoney) {
        this.firstPlayMoney = firstPlayMoney;
    }

    /**
     * Get firstPlayEscape
     * @return firstPlayEscape
     */
	public boolean isFirstPlayEscape() {
		return firstPlayEscape;
	}

    /**
     * Set firstPlayEscape
     * @param firstPlayEscape firstPlayEscape
     */
	public void setFirstPlayEscape(boolean firstPlayEscape) {
		this.firstPlayEscape = firstPlayEscape;
	}

    /**
     * Get firstPlayDeath
     * @return firstPlayDeath
     */
	public boolean isFirstPlayDeath() {
		return firstPlayDeath;
	}

    /**
     * Set firstPlayDeath
     * @param firstPlayDeath firstPlayDeath
     */
	public void setFirstPlayDeath(boolean firstPlayDeath) {
		this.firstPlayDeath = firstPlayDeath;
	}

	/**
	 * Get firstPlayNewGamePlus
	 * @return if still true
	 */
	public boolean isFirstPlayNewGamePlus() {
		return firstPlayNewGamePlus;
	}

	/**
	 * Set firstPlayNewGamePlus
	 * @param firstPlayNewGamePlus set value
	 */
	public void setFirstPlayNewGamePlus(boolean firstPlayNewGamePlus) {
		this.firstPlayNewGamePlus = firstPlayNewGamePlus;
	}

	/**
	 * Get if it's first time playing final fight
	 * @return if still true
	 */
	public boolean isFirstPlayFinalFightStart() {
		return firstPlayFinalFightStart;
	}

	/**
	 * Set if it's first time playing final fight
	 * @param firstPlayFinalFightStart
	 */
	public void setFirstPlayFinalFightStart(boolean firstPlayFinalFightStart) {
		this.firstPlayFinalFightStart = firstPlayFinalFightStart;
	}

	/**
	 * Gets firstPlaySkip.
	 * @return firsPlaySkip
	 */
	public boolean isFirstPlaySkip() {
		return firstPlaySkip;
	}

	/**
	 * Sets firstPlaySkip
	 * @param firstPlaySkip value
	 */
	public void setFirstPlaySkip(boolean firstPlaySkip) {
		this.firstPlaySkip = firstPlaySkip;
	}

	/**
     * Set finishedGame
     * @param finishedGame finishedGame
     */
	public void setFinishedGame(boolean finishedGame) {
	    this.finishedGame = finishedGame;
    }

    /**
     * Get finishedGame
     * @return finishedGame
     */
    public boolean isFinishedGame() {
	    return finishedGame;
    }

    /**
     * Set checkHard
     * @param checkHard checkHard
     */
    public void setCheckHard(boolean checkHard) {
	    this.checkHard = checkHard;
    }

    /**
     * Get checkHard
     * @return checkHard
     */
    public boolean isCheckHard() {
	    return checkHard;
    }

    /**
     * Get finishedGameHard
     * @return finishedGameHard
     */
    public boolean isFinishedGameHard() {
	    return finishedGameHard;
    }

    /**
     * Get resetedGame
     * @return resetedGame
     */
    public boolean isResetedGame() {
	    return resetedGame;
    }

    /**
     * Get testButtonAtlas
     * @return testButtonAtlas
     */
	public TextureAtlas getTestButtonAtlas() {
		return testButtonAtlas;
	}

    /**
     * Get labelStyle
     * @return labelStyle
     */
	public Label.LabelStyle getLabelStyle() {
		return labelStyle;
	}

    /**
     * Get descriptionLabelStyle
     * @return descriptionLabelStyle
     */
	public Label.LabelStyle getDescriptionLabelStyle() {
	    return descriptionLabelStyle;
    }

    /**
     * Get fontcolor
     * @return fontcolor
     */
	public Color getFontColor() {
		return fontColor;
	}

    /**
     * Get utilDialog
     * @return dialog
     */
	public UtilDialog getDialog() {
		return dialog;
	}

    /**
     * Set hackPosX
     * @param hackPosX hackPosX
     */
	public void setHackPosX(FloatArray hackPosX) {
	    this.hackPosX = hackPosX;
    }

    /**
     * Set hackPosY
     * @param hackPosY hackPosY
     */
    public void setHackPosY(FloatArray hackPosY) {
	    this.hackPosY = hackPosY;
    }

    /**
     * Set innerPosX
     * @param innerPosX innerPosX
     */
    public void setInnerPosX(FloatArray innerPosX) {
	    this.innerPosX = innerPosX;
    }

    /**
     * Set innerPosY
     * @param innerPosY innerPosY
     */
    public void setInnerPosY(FloatArray innerPosY) {
	    this.innerPosY = innerPosY;
    }

    /**
     * Set innerHackShieldAmount
     * @param innerHackShieldAmount innerHackShieldAmount
     */
    public void setInnerHackShieldAmount(int innerHackShieldAmount) {
	    this.innerHackShieldAmount = innerHackShieldAmount;
    }

    /**
     * Set hackShieldAmount
     * @param hackShieldAmount hackShieldAmount
     */
    public void setHackShieldAmount(int hackShieldAmount) {
	    this.hackShieldAmount = hackShieldAmount;
    }

    /**
     * Set hackFirstTry
     * @param hackFirstTry hackFirstTry
     */
    public void setHackFirstTry(boolean hackFirstTry) {
	    this.hackFirstTry = hackFirstTry;
    }

    /**
     * Get hackFirstTry
     * @return hackFirstTry
     */
    public boolean getHackFirstTry() {
	    return hackFirstTry;
    }

    /**
     * Get hackPosX
     * @return hackPosX
     */
    public FloatArray getHackPosX() {
        return hackPosX;
    }

    /**
     * Get hackPosY
     * @return hackPosY
     */
    public FloatArray getHackPosY() {
        return hackPosY;
    }

    /**
     * Get innerPosX
     * @return innerPosX
     */
    public FloatArray getInnerPosX() {
	    return innerPosX;
    }

    /**
     * Get innerPosY
     * @return innerPosY
     */
    public FloatArray getInnerPosY() {
	    return innerPosY;
    }

    /**
     * Get hackShieldAmount
     * @return hackShieldAmount
     */
    public int getHackShieldAmount() {
	    return hackShieldAmount;
    }

    /**
     * Get innerHackShieldAmount
     * @return innerHackShieldAmount
     */
    public int getInnerHackShieldAmount() {
	    return innerHackShieldAmount;
    }

    /**
     * Get pool1HackShieldAmount
     * @return pool1HackShieldAmount
     */
    public int getPool1HackShieldAmount() {
	    return pool1HackShieldAmount;
    }

    /**
     * Get pool2HackShieldAmount
     * @return pool2HackShieldAmount
     */
    public int getPool2HackShieldAmount() {
	    return pool2HackShieldAmount;
    }

    /**
     * Get pool3HackShieldAmount
     * @return pool3HackShieldAmount
     */
    public int getPool3HackShieldAmount() {
	    return pool3HackShieldAmount;
    }

    /**
     * Get pool3 innerHackShieldAmount
     * @return innerHackShieldAmount
     */
    public int getPool3InnerHackShieldAmount() {
	    return pool3InnerHackShieldAmount;
    }

	/**
     * Get pool
     * @return pool
     */
	public int getPool() {
		return pool;
	}

    /**
     * Get poolMult
     * @return poolMult
     */
	public int getPoolMult() {
		return poolMult;
	}

    /**
     * Get emptyWindowStyle
     * @return emptyWindowsStyle
     */
	public Window.WindowStyle getEmptyWindowStyle() {
		return emptyWindowStyle;
	}

    /**
     * Get clickedOpenSettings
     * @return clickedOpenSettings
     */
    public boolean getClickedOpenSettings() {
        return clickedOpenSettings;
    }

    /**
     * Set clickedOpenSettings
     * @param clickedOpenSettings clickedOpenSettings
     */
    public void setClickedOpenSettings(boolean clickedOpenSettings) {
        this.clickedOpenSettings = clickedOpenSettings;
    }

    /**
     * Get currentBoss
     * @return currentBoss
     */
	public String getCurrentBoss() {
		return currentBoss;
	}

    /**
     * Get money
     * @return money
     */
    public int getMoney() {
	    return money;
    }

    /**
     * Get inventorySize
     * @return inventorySize
     */
    public int getInventorySize() {
	    return inventorySize;
    }

    /**
     * Get progressBarMileStone
     * @return progressBarMileStone
     */
	public float getProgressBarMilestone() {
		return progressBarMilestone;
	}

    /**
     * Get inventory
     * @return inventory
     */
    public ArrayList<String> getInventory() {
        return inventory;
    }

    /**
     * Get boughtPermanent
     * @return boughtPermanent
     */
    public ArrayList<String> getBoughtPermanent() {
	    return boughtPermanent;
    }

    /**
     * Get finalSkin
     * @return finalSkin
     */
    public Skin getFinalSkin() {
	    return finalSkin;
    }

    /**
     * Get files
     * @return files
     */
	public Files getFiles() {
		return files;
	}

    /**
     * Get skills
     * @return skills
     */
	public Skills getSkills() {
		return skills;
	}

    /**
     * Get items
     * @return items
     */
	public Item getItems() {
		return items;
	}

    /**
     * Get bosses
     * @return bosses
     */
	public Bosses getBosses() {
		return bosses;
	}

    /**
     * Get language
     * @return language
     */
	public String getLanguage() {
		return language;
	}

    /**
     * Set language
     * @param language language
     */
	public void setLanguage(String language) {
	    this.language = language;
    }

    /**
     * Set difficulty
     * @param difficulty difficulty
     */
    public void setDifficulty(String difficulty) {
	    this.difficulty = difficulty;
    }

    /**
     * Get difficulty
     * @return difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }

    /**
     * Set buyedItemsCounter
     * @param buyedItemsCounter buyedItemsCounter
     */
    public void setBuyedItemsCounter(int buyedItemsCounter) {
	    this.buyedItemsCounter = buyedItemsCounter;
    }

    /**
     * Get buyedItemsCounter
     * @return buyedItemsCounter
     */
    public int getBuyedItemsCounter() {
	    return buyedItemsCounter;
    }

    /**
     * Get fightsWon
     * @return fightsWon
     */
    public int getFightsWon() {
	    return fightsWon;
    }

    /**
     * Get reflectiveShield
     * @return reflectiveShield
     */
	public boolean isReflectiveShield() {
		return reflectiveShield;
	}

    /**
     * Set reflectiveShield
     * @param reflectiveShield reflectiveShield
     */
	public void setReflectiveShield(boolean reflectiveShield) {
		this.reflectiveShield = reflectiveShield;
	}

    /**
     * Get soundVol
     * @return soundVol
     */
	public float getSoundVol() {
	    return soundVol;
    }

    /**
     * Set achievement
     * @param i i
     * @param state state
     */
    public void setAchievement(int i, String state) {
	    achievComplete.set(i, state);
    }

    /**
     * Get achievementArray
     * @return achievComplete
     */
    public ArrayList<String> getAchievComplete() {
	    return achievComplete;
    }

    /**
     * Set if player has collected achievement
     * @param i i
     * @param state state
     */
    public void setHasCollected(int i, String state) {
	    hasCollected.set(i, state);
    }

    /**
     * Get hasCollectedArray
     * @return hasCollected
     */
    public ArrayList<String> getHasCollected() {
	    return hasCollected;
    }

	/**
	 * Get how many times game has been completed.
	 * @return how many times game has been completed
	 */
    public int getGameCompleteCounter() {
		return gameCompleteCounter;
	}

	/**
	 * Sets what type of dialog is on.
	 * @param dialogType type of dialog
	 */
	public void setDialogType(int dialogType) {
		this.dialogType = dialogType;
	}

	/**
	 * Gets the type of dialog.
	 * @return type of dialog
	 */
	public int getDialogType() {
		return dialogType;
	}

	/**
	 * Checks if walking has been skipped.
	 * @return if skipped
	 */
	public boolean isWalkingSkipped() {
		return walkingSkipped;
	}
}
