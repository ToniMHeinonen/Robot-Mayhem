package fi.tamk.fi;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Locale;

public class MainGame extends Game {
	private SpriteBatch batch;
	private I18NBundle localize;
	private Encryptor E = new Encryptor();
	private Files files;
	private Skills skills;
	private Item items;
	private Bosses bosses;
	private Music curMusic;

	public final float pixelWidth = 1920f;
	public final float pixelHeight = 1080f;
	public final float gridSize = 1920f / 16f;
	private OrthographicCamera camera;

	private Stage stage;
	private Skin skin;
	private Skin finalSkin;

	// Pools and tiers
	private int[] poolMilestones = new int[] {0, 9000, 18000, 27000, 36000};

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
	private String keySkill1 = E.encrypt("skill1");
	private String keySkill2 = E.encrypt("skill2");
	private String keyCurrentBoss = E.encrypt("currentBoss");
	private String keyFirstPlayTime = E.encrypt("firstPlayTime");
	private String keyPool = E.encrypt("pool");
	private String keyPoolMult = E.encrypt("poolMult");
	private String keyInventorySize = E.encrypt("inventorySize");
	private String keyInventory = E.encrypt("inventory");
	private String keyDefeatedBossesSize = E.encrypt("defeatedBossesSize");
	private String keyDefeatedBosses = E.encrypt("defeatedBosses");
	private String keyName = E.encrypt("name");
	// Values
	private int saveTimerAmount = 3600;
	private int saveTimer = saveTimerAmount;
	private Preferences prefsStats;
	private SaveAndLoad stats;
	private float stepCount, stepBank, stepAllCount;
	private int pool, poolMult, money;
	private String skill1, skill2, currentBoss, playerName;
	private boolean firstPlayTime;
	// Stat arrays
	private ArrayList<String> inventory;
	private int inventorySize; // Needed for loading correct amount of array items
	private ArrayList<String> defeatedBosses;
	private int defeatedBossesSize;

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
	private Skin testSkin;
	private Label.LabelStyle labelStyle;
	private Label.LabelStyle descriptionLabelStyle;
	private Window.WindowStyle windowStyle;
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

    private boolean resetting;

	@Override
	public void create () {
		files = new Files();
		loadSettings();
		createBundle();
		// Create skills and bosses when the game launches
		skills = new Skills(this);
		bosses = new Bosses(this);
		items = new Item(this);
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, pixelWidth, pixelHeight);
		initStats();
		loadStats();

		createSkinAndStage();
		createProgressBarFiles();
		createDialogConstants();

		createHackFiles();

		//askForName(); add later

		// Switch to first room
		switchToRoomGame();

		currentBoss = bosses.selectRandomBoss(); // For testing purposes, remember to remove
	}

	@Override
	public void render () {
		super.render();
		controlSaveTimer();
	}

	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();
		fontSteps.dispose();
		descriptionFont.dispose();
		testButtonAtlas.dispose();
		testSkin.dispose();
		progBarAtlas.dispose();
		progBarSkin.dispose();
		files.manager.dispose();
		if (resetting) resetting = false;
		else {
			saveStats();
			saveSettings();
		}
	}

	public void restartGame() {
		dispose();
		create();
	}

	public void resetGame() {
		resetting = true;
		settings.clear();
		settings.flush();
		prefsStats.clear();
		prefsStats.flush();
		restartGame();
	}

	boolean haveWeChangedTheRoom = false;

	public void transition() { haveWeChangedTheRoom = true; }

	public void switchToRoomTestailua() {
		transition();
		startMusic(files.musMainTheme);
	    RoomTestailua room = new RoomTestailua(this);
	    setScreen(room);
    }

    public void switchToRoomSettings() {
		transition();
		startMusic(files.musMainTheme);
		RoomSettings room = new RoomSettings(this);
		setScreen(room);
    }

    public void switchToRoomGame() {
		transition();
		startMusic(files.musMainTheme);
	    RoomGame room = new RoomGame(this);
        setScreen(room);
    }

    public void switchToRoomFight() {
		transition();
		startMusic(files.musBossRobo);
	    RoomFight room = new RoomFight(this);
	    setScreen(room);
    }

	public void switchToPowerUps() {
		transition();
		startMusic(files.musMainTheme);
		PowerUps room = new PowerUps(this);
		setScreen(room);
	}

	public void switchToRoomItemTest() {
		startMusic(files.musMainTheme);
		transition();
		//RoomItemTest room = new RoomItemTest(this);
		//setScreen(room);
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

	private void createDialogConstants() {
		fontSteps = new BitmapFont(Gdx.files.internal("stepfont/stepfont.fnt"),
				Gdx.files.internal("stepfont/stepfont.png"),
				false);
		descriptionFont = new BitmapFont(Gdx.files.internal("descriptionfont/descriptionfont.fnt"),
                Gdx.files.internal("descriptionfont/descriptionfont.png"),
                false);
		testButtonAtlas = new TextureAtlas("testbuttons/actionButtons.pack");
		testSkin = new Skin(testButtonAtlas);
		windowStyle = new Window.WindowStyle(fontSteps, fontColor, testSkin.getDrawable("dialog_bg"));
		emptyWindowStyle = new Window.WindowStyle(fontSteps, fontColor, null);
		labelStyle = new Label.LabelStyle(fontSteps, fontColor);
		descriptionLabelStyle = new Label.LabelStyle(descriptionFont, fontColor);
		dialog = new UtilDialog(this);
	}

	private void createProgressBarFiles() {
	    progBarAtlas = new TextureAtlas("progressbar/progressbar.pack");
	    progBarSkin = new Skin();
	    progBarSkin.addRegions(progBarAtlas);

	    progBarStyle = new ProgressBar.ProgressBarStyle();
	    progBarStyle.knob = progBarSkin.getDrawable("tripmarker");
	    progBarStyle.background = progBarSkin.getDrawable("tripmeter");

	    chooseNextMilestone();
    }

    private void chooseNextMilestone() {
        // If it's the first boss, make milestone 20
        if (pool == 1 && poolMult == 0) progressBarMilestone = 20;
        else progressBarMilestone = poolMilestones[pool] / 3;
    }

	public void createBundle() {
		Locale locale;
		// If language is not either, then the game gets software's default language
		if (language.equals("fi")) locale = new Locale("fi", "FI");
		else if (language.equals("en")) locale = null;
		else locale = Locale.getDefault();

		if (locale != null) {
			localize = I18NBundle.createBundle(Gdx.files.internal("MyBundle"),
					locale, "ISO-8859-1");
			// After initializing localize, make language variable the chosen one
			if (locale.getLanguage().equals("fi")) language = "fi";
			else language = "en";
		} else {
			localize = I18NBundle.createBundle(Gdx.files.internal("MyBundle"),
					"ISO-8859-1");
			language = "en";
		}
	}

	public void languageToFIN() {
		Locale locale = new Locale("fi", "FI");
		localize = I18NBundle.createBundle(Gdx.files.internal("MyBundle"),
				locale, "ISO-8859-1");
		language = "fi";
		saveSettings();
		saveStats();
		restartGame();
	}

	public void languageToENG() {
		localize = I18NBundle.createBundle(Gdx.files.internal("MyBundle"),
				"ISO-8859-1");
		language = "en";
		saveSettings();
		saveStats();
		restartGame();
	}

    public void loadSettings() {
		settings = Gdx.app.getPreferences("Robot_Mayhem_Settings");
		//settings.clear(); // For testing purposes
		//settings.flush(); // Without flushing, clear does not work in Android
		musicVol = settings.getFloat(keyMusicVol, 0.8f);
		soundVol = settings.getFloat(keySoundVol, 0.8f);
		language = settings.getString(keyLanguage, "");
		difficulty = settings.getString(keyDifficulty, "medium");
    }

    public void saveSettings() {
	    settings.putFloat(keyMusicVol, musicVol);
		settings.putFloat(keySoundVol, soundVol);
	    settings.putString(keyLanguage, language);
	    settings.putString(keyDifficulty, difficulty);
	    settings.flush();
    }

    public void controlSaveTimer() {
		if (saveTimer > 0) saveTimer--;
		else saveTimer = saveTimerAmount; saveStats();
	}

	/**
	 * Load correct preferences for statistics and initialize SaveAndLoad class for handling
	 * loading and saving plus encryption and decryption of file.
	 */
	private void initStats() {
		inventory = new ArrayList<String>();
		defeatedBosses = new ArrayList<String>();
		prefsStats = Gdx.app.getPreferences("Robot_Mayhem_Stats");
		//prefsStats.clear(); // For testing purposes
		//prefsStats.flush(); // Without flushing, clear does not work in Android
		stats = new SaveAndLoad(E, prefsStats);
	}

	/**
	 * Load statistics.
	 */
	public void loadStats() {
		// NOTE: Remember to write 0f instead of 0 to float defValues, otherwise loading crashes
		money = stats.loadValue(keyMoney, 20);
		stepCount = stats.loadValue(keyStepCount, 0f);
		stepAllCount = stats.loadValue(keyStepAllCount, 0f);
		stepBank = stats.loadValue(keyStepBank, 0f);
		skill1 = stats.loadValue(keySkill1, skills.REPAIR);
		skill2 = stats.loadValue(keySkill2, "");
		currentBoss = stats.loadValue(keyCurrentBoss, bosses.ROOMBOT);
		firstPlayTime = stats.loadValue(keyFirstPlayTime, true);
		pool = stats.loadValue(keyPool, 1);
		poolMult = stats.loadValue(keyPoolMult, 0);
		playerName = stats.loadValue(keyName, "");

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
	}

	/**
	 * Save statistics.
	 */
	public void saveStats() {
		stats.saveValue(keyMoney, money);
		stats.saveValue(keyStepCount, stepCount);
		stats.saveValue(keyStepAllCount, stepAllCount);
		stats.saveValue(keyStepBank, stepBank);
		stats.saveValue(keySkill1, skill1);
		stats.saveValue(keySkill2, skill2);
		stats.saveValue(keyCurrentBoss, currentBoss);
		stats.saveValue(keyFirstPlayTime, firstPlayTime);
		stats.saveValue(keyPool, pool);
		stats.saveValue(keyPoolMult, poolMult);
		stats.saveValue(keyName, playerName);

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

		prefsStats.flush();
	}

	// Methods for name start.
	public class MyTextInputListener implements Input.TextInputListener {
		@Override
		public void input (String text) {
			boolean legal = setName(text);
			if (!legal) {
				askForName();
			}
		}

		@Override
		public void canceled () {
			askForName();
		}
	}

	public void askForName() {
		MyTextInputListener listener = new MyTextInputListener();
		Gdx.input.getTextInput(listener, "Enter name", "", "Max 10 characters");
	}

	// Next up code for the name:
	public boolean setName(String n) {
		boolean legal = true;

		if (n.length() <= 10 && !n.equals("defaultDodo")) {
			playerName = n;
			dialog.createDialog(playerName + " is your name", "skilldescription");
		} else {
			legal = false;
		}

		/*
		You are flushing stats, even though you have not added anything new in it.
		 */

		// Comment: Thought this saved the name. :( Don't know what does.
		//stats.flush();
		return legal;
	}

	// Methods for name end.

	public void bossDefeated() {
		// defeatedBosses.add(currentBoss); Add when all the bosses exist

		stepCount = 0; // Reset step count
		poolMult++;
		chooseNextMilestone();

		// Add bank steps in roomGame

		currentBoss = bosses.selectRandomBoss(); // Randomize new boss

		// Add when all the bosses exist
		/*while (defeatedBosses.contains(currentBoss)) {
			currentBoss = Bosses.selectRandomBoss();
		}
		// If all the bosses of the pool defeated, reset bosses, else add number++
		if (poolMult < 8) poolMult++;
		else {
			pool++;
			poolMult = 0;
		}
		*/
	}

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

	public void removeFromInventory(String name) {
        inventory.remove(name);
        saveStats();
    }

	public boolean inventoryOrSkillsContains(String name) {
		boolean contains = false;

		if (inventory.contains(name)) contains = true;
		if (skill1.equals(name) || skill2.equals(name)) contains = true;

		return contains;
	}

    private void createSkinAndStage() {
        stage = new Stage(new FitViewport(pixelWidth, pixelHeight), batch);
        /*
        Delete these comments before publishing
         */
        // Skin: https://github.com/czyzby/gdx-skins/tree/master/glassy
        // Check "License" bottom of the page
        // Files in \android\assets:
        // font-big-export.fnt, font-export.fnt, glassy-ui.atlas,
        // glassy-ui.json, glassy-ui.png
        //skin = new Skin( Gdx.files.internal("glassy-ui.json") );
        //finalSkin = new Skin(Gdx.files.internal("finalskin/finalskin.json"));
		skin = files.skin;
		finalSkin = files.finalSkin;
    }

	// Receive steps on Desktop, if milestone is not reached, else add them to stepBank
    public void simulateStep() {
		if (stepCount < progressBarMilestone) this.stepCount++;
		else if (stepBank < 3000) stepBank++;
	}

    // Receive steps on Android, if milestone is not reached, else add them to stepBank
	public void receiveSteps(float stepCount) {
		if (this.stepCount < progressBarMilestone) this.stepCount++;
		else stepBank++;
	}

	// Deleted steps from bank, when retrieving them on RoomGame
	public void retrieveFromBank(float amount) {
		stepBank -= amount;
		if (stepBank < 0) stepBank = 0;
	}

	public void addMoney(int amount) {
		money += amount;
	}

	public void decreaseMoney(int amount) {
	    money -= amount;
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

	public Stage getStage() {
		return stage;
	}

	public Skin getSkin() {
		return skin;
	}

	public float getMusicVol() {
		return musicVol;
	}

	public I18NBundle getLocalize() {
		return localize;
	}

	public ProgressBar.ProgressBarStyle getProgBarStyle() {
        return progBarStyle;
    }

	public BitmapFont getFontSteps() {
	    return fontSteps;
    }

    public BitmapFont getDescriptionFont() {
	    return descriptionFont;
    }

	public float getStepCount() {
		return stepCount;
	}

	public void setStepCount(float stepCount) {
		this.stepCount = stepCount;
	}

	public float getStepBank() {
		return stepBank;
	}

	public float getStepAllCount() {
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

	public String getPlayerName() {
		return playerName;
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

	public Label.LabelStyle getDescriptionLabelStyle() {
	    return descriptionLabelStyle;
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

    public void setInnerPosX(FloatArray innerPosX) {
	    this.innerPosX = innerPosX;
    }

    public void setInnerPosY(FloatArray innerPosY) {
	    this.innerPosY = innerPosY;
    }

    public void setInnerHackShieldAmount(int innerHackShieldAmount) {
	    this.innerHackShieldAmount = innerHackShieldAmount;
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

    public FloatArray getInnerPosX() {
	    return innerPosX;
    }

    public FloatArray getInnerPosY() {
	    return innerPosY;
    }

    public int getHackShieldAmount() {
	    return hackShieldAmount;
    }

    public int getInnerHackShieldAmount() {
	    return innerHackShieldAmount;
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

    public int getPool3InnerHackShieldAmount() {
	    return pool3InnerHackShieldAmount;
    }

	public int getPool() {
		return pool;
	}

	public int getPoolMult() {
		return poolMult;
	}

	public Window.WindowStyle getEmptyWindowStyle() {
		return emptyWindowStyle;
	}

    public boolean getClickedOpenSettings() {
        return clickedOpenSettings;
    }

    public void setClickedOpenSettings(boolean clickedOpenSettings) {
        this.clickedOpenSettings = clickedOpenSettings;
    }

	public String getCurrentBoss() {
		return currentBoss;
	}

    public int getMoney() {
	    return money;
    }

    public int getInventorySize() {
	    return inventorySize;
    }

	public float getProgressBarMilestone() {
		return progressBarMilestone;
	}

    public ArrayList<String> getInventory() {
        return inventory;
    }

    public Skin getFinalSkin() {
	    return finalSkin;
    }

	public Files getFiles() {
		return files;
	}

	public Skills getSkills() {
		return skills;
	}

	public Item getItems() {
		return items;
	}

	public Bosses getBosses() {
		return bosses;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
	    this.language = language;
    }

    public void setDifficulty(String difficulty) {
	    this.difficulty = difficulty;
    }

    public String getDifficulty() {
        return difficulty;
    }
}
