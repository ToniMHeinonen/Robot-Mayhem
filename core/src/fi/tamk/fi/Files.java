package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Files {
    // Player textures
    public static final Texture playerIdle, playerAttack, playerDefend, playerItem, playerEscape,
    playerHack, playerDeath, playerTakeHit, healthPlus, healthMinus, criticalHit, miss, healing,
    dotMinus, dotPlus, playerGameMoving;

    // Other textures
    public static final Texture imgBgHall, imgBgBoss, imgTopBar, imgBottomBar, escapeBg, hpBarLeft,
            hpBarRight, powerUpBg, powerUpPopup, itemBg;

    // Player animations
    private static final Animating createAnims = new Animating();
    public static final Animation<TextureRegion> animIdle, animSkill, animDefend, animEscape,
            animItem, animHack, animDeath, animTakeHitAnim, animHealthPlusDoT, animHealthMinusDoT,
            animMiss, animCriticalHit, animHealing, animDoTPlus, animDoTMinus, animGameMoving;

    // Music
    public static final Music[] allMusic;
    public static final Music musMainTheme, musBossRobo, musBossFuturistic;

    static{
        /*
        Textures
         */
        // Player
        playerIdle = new Texture("texture/player/player_idle.png");
        playerAttack = new Texture("texture/player/player_attack.png");
        playerDefend = new Texture("texture/player/player_defend.png");
        playerItem = new Texture("texture/player/player_item.png");
        playerEscape = new Texture("texture/player/player_flee.png");
        playerHack = new Texture("texture/player/player_hack.png");
        playerDeath = new Texture("texture/player/player_stun.png");
        playerTakeHit = new Texture("texture/player/player_damage.png");
        healthPlus = new Texture("texture/skills/plusHealth.png");
        healthMinus = new Texture("texture/skills/minusHealth.png");
        criticalHit = new Texture("texture/skills/criticalHit.png");
        miss = new Texture("texture/skills/miss.png");
        healing = new Texture("texture/skills/healing.png");
        dotMinus = new Texture("texture/skills/dotMinus.png");
        dotPlus = new Texture("texture/skills/dotPlus.png");
        playerGameMoving = new Texture("texture/player/player_move.png");
        // Other
        imgBgHall = new Texture("texture/bg_hall1.png");
        imgBgBoss = new Texture("texture/bg_hall1_boss.png");
        imgTopBar = new Texture("texture/topbar.png");
        imgBottomBar = new Texture("texture/bottombar.png");
        escapeBg = new Texture("texture/escapeBackground.png");
        hpBarLeft = new Texture("texture/hpbar_left.png");
        hpBarRight = new Texture("texture/hpbar_right.png");
        powerUpBg = new Texture("texture/powerUpBg.jpg");
        powerUpPopup = new Texture("texture/powerUpPopup.jpg");
        itemBg = new Texture("texture/itemBg.jpg");

        /*
        Animations
         */
        animIdle = createAnims.createAnimation(playerIdle, 4, 4);
        animSkill = createAnims.createAnimation(playerAttack, 4, 1);
        animDefend = createAnims.createAnimation(playerDefend, 4, 2);
        animEscape = createAnims.createAnimation(playerEscape, 4, 1);
        animItem = createAnims.createAnimation(playerItem, 4, 1);
        animHack = createAnims.createAnimation(playerHack, 4, 2);
        animDeath = createAnims.createAnimation(playerDeath, 4, 2);
        animTakeHitAnim = createAnims.createAnimation(playerTakeHit, 4, 1);
        animHealthPlusDoT = createAnims.createAnimation(healthPlus, 3, 1);
        animHealthMinusDoT = createAnims.createAnimation(healthMinus, 3, 1);
        animCriticalHit = createAnims.createAnimation(criticalHit, 4, 1);
        animMiss = createAnims.createAnimation(miss, 4, 1);
        animHealing = createAnims.createAnimation(healing, 4, 1);
        animDoTPlus = createAnims.createAnimation(dotPlus, 4, 1);
        animDoTMinus = createAnims.createAnimation(dotMinus, 4, 1);
        animGameMoving = createAnims.createAnimation(playerGameMoving, 4, 1);

        /*
        Music
         */
        musMainTheme = Gdx.audio.newMusic(Gdx.files.internal("music/mainTheme.mp3"));
        musMainTheme.setLooping(true);
        musBossRobo = Gdx.audio.newMusic(Gdx.files.internal("music/bossRobo.mp3"));
        musBossRobo.setLooping(true);
        musBossFuturistic = Gdx.audio.newMusic(Gdx.files.internal("music/bossFuturistic.mp3"));
        musBossFuturistic.setLooping(true);
        // Remember to add all new music here
        allMusic = new Music[] {musMainTheme, musBossRobo, musBossFuturistic};
    }

    public static void dispose() {

    }
}
