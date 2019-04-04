package fi.tamk.fi;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

public class MainGame extends Game {
	private SpriteBatch batch;
	private I18NBundle myBundle;

	public final float pixelWidth = 1920f;
	public final float pixelHeight = 1080f;
	public final float gridSize = 1920f / 16f;
	private OrthographicCamera camera;

	private Stage stage;
	private Skin skin;
	private Music backgroundMusic;
	private Music bossMusic;
	private Music[] allMusic;

	// Pools and tiers
	private int[] poolMilestones = new int[] {0, 9000, 18000, 27000, 36000};

	// Settings
	private Preferences settings;
	private float musicVol;
    private boolean clickedOpenSettings = false;

	// Stats
	// Keys (use these to decrease error chance)
	private String keyMoney = "money", keyStepCount = "stepCount", keyStepAllCount = "stepAllCount",
	keyStepBank = "stepBank", keySkill1 = "skill1", keySkill2 = "skill2",
	keyCurrentBoss = "currentBoss", keyFirstPlayTime = "firstPlayTime", keyPool = "pool",
	keyPoolMult = "poolMult", keyInventorySize = "inventorySize", keyInventory = "inventory",
	keyDefeatedBossesSize = "defeatedBossesSize", keyDefeatedBosses = "defeatedBosses";
	// Values
	private int saveTimerAmount = 3600;
	private int saveTimer = saveTimerAmount;
	private Preferences stats;
	private float stepCount, stepBank, stepAllCount;
	private int pool, poolMult, money;
	private String skill1, skill2, currentBoss;
	private boolean firstPlayTime;
	// Stat arrays
	private ArrayList<String> inventory = new ArrayList<String>();
	private int inventorySize; // Needed for loading correct amount of array items
	private ArrayList<String> defeatedBosses = new ArrayList<String>();
	private int defeatedBossesSize;

	// Textures
	private Texture imgBgHall, imgBgBoss, imgTopBar, imgBottomBar, escapeBg, hpBarLeft,
            hpBarRight, powerUpBg, powerUpPopup, itemBg;
	private Animating createAnims = new Animating();
	// RoomGame
	private Animation<TextureRegion> animGameMoving;
    // RoomFight
	private Animation<TextureRegion> animIdle, animSkill, animDefend, animEscape,
            animItem, animHack, animDeath, animTakeHitAnim, animHealthPlusDoT, animHealthMinusDoT,
			animMiss, animCriticalHit;

	// Stepmeter in RoomGame
    private BitmapFont fontSteps;

	// Progressbar
    private TextureAtlas progBarAtlas;
    private Skin progBarSkin;
    private ProgressBar.ProgressBarStyle progBarStyle;
    private float progressBarMilestone;

	// Image button (temporary)
	private BitmapFont font;
	private TextureAtlas buttonsAtlas; //** image of buttons **//
	private Skin buttonSkin; //** images are used as skins of the button **//
	private TextButton.TextButtonStyle style;

	// Dialog
	private UtilDialog dialog;
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
		loadSettings();
		loadStats();
		loadTextures();
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
		createMusic();

		createHackFiles();

		currentBoss = Bosses.selectRandomBoss(); // For testing purposes, remember to remove

		// Switch to first room
		switchToRoomGame();
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
		skin.dispose();
		backgroundMusic.dispose();
		bossMusic.dispose();
		Bosses.dispose();
		Skills.dispose();
		saveStats();
	}

	boolean haveWeChangedTheRoom = false;

	public void transition() { haveWeChangedTheRoom = true; }

	public void switchToRoomTestailua() {
		transition();
		startMusic(backgroundMusic);
	    RoomTestailua room = new RoomTestailua(this);
	    setScreen(room);
    }

    public void switchToRoomSettings() {
		transition();
		startMusic(backgroundMusic);
		RoomSettings room = new RoomSettings(this);
		setScreen(room);
    }

    public void switchToRoomGame() {
		transition();
		startMusic(backgroundMusic);
	    RoomGame room = new RoomGame(this);
        setScreen(room);
    }

    public void switchToRoomFight() {
		transition();
		startMusic(bossMusic);
	    RoomFight room = new RoomFight(this);
	    setScreen(room);
    }

	public void switchToPowerUps() {
		transition();
		startMusic(backgroundMusic);
		PowerUps room = new PowerUps(this);
		setScreen(room);
	}

	public void switchToRoomItemTest() {
		startMusic(backgroundMusic);
		transition();
		//RoomItemTest room = new RoomItemTest(this);
		//setScreen(room);
	}

	private void createMusic() {
		backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/fansu_1.mp3"));
		backgroundMusic.setLooping(true);
		bossMusic = Gdx.audio.newMusic(Gdx.files.internal("music/bossmusic.mp3"));
		bossMusic.setLooping(true);

		// Remember to add all new music here
		allMusic = new Music[] {backgroundMusic, bossMusic};
	}

	private void startMusic(Music file) {
		if (!file.isPlaying()) {
			for (Music m : allMusic) {
				if (m.isPlaying()) m.stop();
			}
			file.play();
		}
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
		descriptionFont = new BitmapFont(Gdx.files.internal("descriptionfont/descriptionfont.fnt"),
                Gdx.files.internal("descriptionfont/descriptionfont.png"),
                false);
		testButtonAtlas = new TextureAtlas("testbuttons/testbuttons.pack");
		testSkin = new Skin(testButtonAtlas);
		windowStyle = new Window.WindowStyle(fontSteps, fontColor, testSkin.getDrawable("dialog_bg"));
		emptyWindowStyle = new Window.WindowStyle(fontSteps, fontColor, null);
		labelStyle = new Label.LabelStyle(fontSteps, fontColor);
		descriptionLabelStyle = new Label.LabelStyle(descriptionFont, fontColor);
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

	    chooseNextMilestone();
    }

    private void chooseNextMilestone() {
        // If it's the first boss, make milestone 20
        if (pool == 1 && poolMult == 0) progressBarMilestone = 20;
        else progressBarMilestone = poolMilestones[pool] / 3;
    }

	private void loadTextures() {
		imgBgHall = new Texture("texture/bg_hall1.png");
		imgBgBoss = new Texture("texture/bg_hall1_boss.png");
		imgTopBar = new Texture("texture/topbar.png");
		imgBottomBar = new Texture("texture/bottombar.png");
		Texture gamePlayerMoving = new Texture("texture/player/player_move.png");
        Texture playerIdle = new Texture("texture/player/player_idle.png");
        Texture playerAttack = new Texture("texture/player/player_attack.png");
        Texture playerDefend = new Texture("texture/player/player_defend.png");
        Texture playerItem = new Texture("texture/player/player_item.png");
        Texture playerEscape = new Texture("texture/player/player_flee.png");
        Texture playerHack = new Texture("texture/player/player_hack.png");
        Texture playerDeath = new Texture("texture/player/player_stun.png");
		Texture playerTakeHit = new Texture("texture/player/player_damage.png");
        Texture healthPlus = new Texture("texture/skills/plusHealth.png");
        Texture healthMinus = new Texture("texture/skills/minusHealth.png");
		Texture criticalHit = new Texture("texture/skills/criticalHit.png");
		Texture miss = new Texture("texture/skills/miss.png");
		escapeBg = new Texture("texture/escapeBackground.png");
		hpBarLeft = new Texture("texture/hpbar_left.png");
		hpBarRight = new Texture("texture/hpbar_right.png");
		powerUpBg = new Texture("texture/powerUpBg.jpg");
		powerUpPopup = new Texture("texture/powerUpPopup.jpg");
		itemBg = new Texture("texture/itemBg.jpg");

		// Create animations
		animGameMoving = createAnims.createAnimation(gamePlayerMoving, 4, 1);

		animIdle = createAnims.createAnimation(playerIdle, 8, 2);
		animSkill = createAnims.createAnimation(playerAttack, 4, 1);
		animDefend = createAnims.createAnimation(playerDefend, 8, 1);
        animEscape = createAnims.createAnimation(playerEscape, 4, 1);
        animItem = createAnims.createAnimation(playerItem, 4, 1);
        animHack = createAnims.createAnimation(playerHack, 8, 1);
        animDeath = createAnims.createAnimation(playerDeath, 8, 1);
        animTakeHitAnim = createAnims.createAnimation(playerTakeHit, 4, 1);
        animHealthPlusDoT = createAnims.createAnimation(healthPlus, 3, 1);
        animHealthMinusDoT = createAnims.createAnimation(healthMinus, 3, 1);
        animCriticalHit = createAnims.createAnimation(criticalHit, 4, 1);
		animMiss = createAnims.createAnimation(miss, 4, 1);
	}

	private void createBundle() {
		Locale locale = Locale.getDefault();
		//Locale locale = new Locale("fi", "FI"); USE THIS TO TEST FINNISH VERSION
		myBundle = I18NBundle.createBundle(Gdx.files.internal("MyBundle"),
				locale,"ISO-8859-1");

		//EXAMPLE CODE
		//System.out.println(myBundle.get("test"));
		//String str = myBundle.get("test");
		//System.out.println(str);
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
		stats.clear(); // For testing purposes
		stats.flush(); // Without flushing, clear does not work in Android
		money = stats.getInteger(keyMoney, 0);
		stepCount = stats.getFloat(keyStepCount, 0);
		stepAllCount = stats.getFloat(keyStepAllCount, 0);
		stepBank = stats.getFloat(keyStepBank, 0);
		skill1 = stats.getString(keySkill1, "");
		skill2 = stats.getString(keySkill2, "");
		currentBoss = stats.getString(keyCurrentBoss, "Roombot");
		firstPlayTime = stats.getBoolean(keyFirstPlayTime, true);
		pool = stats.getInteger(keyPool, 1);
		poolMult = stats.getInteger(keyPoolMult, 0);
		System.out.println(stepCount);

		// Load the size of inventory before loading inventory items
		inventorySize = stats.getInteger(keyInventorySize, 0);
		for (int i = 0; i < inventorySize; i++) {
			inventory.add(i, stats.getString(keyInventory + String.valueOf(i), ""));
		}
		// Defeated bosses
		defeatedBossesSize = stats.getInteger(keyDefeatedBossesSize, 0);
		for (int i = 0; i < defeatedBossesSize; i++) {
			defeatedBosses.add(i, stats.getString(keyDefeatedBosses + String.valueOf(i), ""));
		}
	}

	public void saveStats() {
		stats.putInteger(keyMoney, money);
		stats.putFloat(keyStepCount, stepCount);
		stats.putFloat(keyStepAllCount, stepAllCount);
		stats.putFloat(keyStepBank, stepBank);
		stats.putString(keySkill1, skill1);
		stats.putString(keySkill2, skill2);
		stats.putString(keyCurrentBoss, currentBoss);
		stats.putBoolean(keyFirstPlayTime, firstPlayTime);
		stats.putInteger(keyPool, pool);
		stats.putInteger(keyPoolMult, poolMult);

		// Save inventory's current size on inventorySize key
		stats.putInteger(keyInventorySize, inventory.size());
		for (int i = 0; i < inventory.size(); i++) {
			stats.putString(keyInventory + String.valueOf(i), inventory.get(i));
		}
		// Defeated bosses
		stats.putInteger(keyDefeatedBossesSize, defeatedBosses.size());
		for (int i = 0; i < defeatedBosses.size(); i++) {
			stats.putString(keyDefeatedBosses + String.valueOf(i), defeatedBosses.get(i));
		}

		stats.flush();
	}

	public void bossDefeated() {
		// defeatedBosses.add(currentBoss); Add when all the bosses exist

		stepCount = 0; // Reset step count
		poolMult++;
		chooseNextMilestone();

		// Add bank steps in roomGame

		currentBoss = Bosses.selectRandomBoss(); // Randomize new boss

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
			if (skill1 == "") skill1 = name;
			else if (skill2 == "") skill2 = name;
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
		if (skill1 == name || skill2 == name) contains = true;

		return contains;
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

	public void setMusicVol(float musicVol) {
		if (musicVol > 0.0f && musicVol < 1.0f) {
			this.musicVol = musicVol;
		}
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

	public Animation<TextureRegion> getAnimGameMoving() {
		return animGameMoving;
	}

	public Animation<TextureRegion> getAnimIdle() {
        return animIdle;
    }

    public Animation<TextureRegion> getAnimSkill() {
        return animSkill;
    }

    public Animation<TextureRegion> getAnimDefend() {
        return animDefend;
    }

    public Animation<TextureRegion> getAnimEscape() {
        return animEscape;
    }

    public Animation<TextureRegion> getAnimItem() {
        return animItem;
    }

    public Animation<TextureRegion> getAnimHack() {
        return animHack;
    }

    public Animation<TextureRegion> getAnimDeath() {
        return animDeath;
    }

    public Animation<TextureRegion> getAnimTakeHitAnim() {
        return animTakeHitAnim;
    }

    public Animation<TextureRegion> getAnimHealthPlusDoT() {
        return animHealthPlusDoT;
    }

    public Animation<TextureRegion> getAnimHealthMinusDoT() {
        return animHealthMinusDoT;
    }

	public Animation<TextureRegion> getAnimMiss() {
		return animMiss;
	}

	public Animation<TextureRegion> getAnimCriticalHit() {
		return animCriticalHit;
	}

	public TextButton.TextButtonStyle getStyle() {
		return style;
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

	public Texture getEscapeBg() {
		return escapeBg;
	}

	public Texture getHpBarLeft() {
		return hpBarLeft;
	}

	public Texture getHpBarRight() {
		return hpBarRight;
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

	public Texture getPowerUpBg() {
		return powerUpBg;
	}

	public Texture getPowerUpPopup() {
		return powerUpPopup;
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

	public Texture getItemBg() {
	    return itemBg;
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
}
