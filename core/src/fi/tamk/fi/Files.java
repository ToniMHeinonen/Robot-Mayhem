package fi.tamk.fi;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Files {
    public final AssetManager manager = new AssetManager();
    private final Animating createAnims = new Animating();

    // Player textures and animations
    private final Texture playerIdle, playerAttack, playerDefend, playerItem, playerEscape,
    playerHack, playerDeath, playerTakeHit, healthPlus, healthMinus, criticalHit, miss, healing,
    dotMinus, dotPlus, playerGameMoving;
    public final Animation<TextureRegion> animIdle, animSkill, animDefend, animEscape,
            animItem, animHack, animDeath, animTakeHitAnim, animHealthPlusDoT, animHealthMinusDoT,
            animMiss, animCriticalHit, animHealing, animDoTPlus, animDoTMinus, animGameMoving;

    // Roombot textures and animations
    private final Texture t_roombotIdle, t_roombotSkill, t_roombotDamage, t_roombotHack;
    public final Animation<TextureRegion> a_roombotIdle, a_roombotSkill, a_roombotDamage,
                                        a_roombotHack;

    // Other textures
    public final Texture imgBgHall, imgBgBoss, imgTopBar, imgBottomBar, escapeBg, hpBarLeft,
            hpBarRight, powerUpBg, powerUpPopup, itemBg;

    // Music
    public final Music[] allMusic;
    public final Music musMainTheme, musBossRobo, musBossFuturistic;

    Files () {
        loadAssets();
        manager.finishLoading();

        /*
        Textures and animations
         */
        // Player
        playerIdle = manager.get("texture/player/player_idle.png");
        playerAttack = manager.get("texture/player/player_attack.png");
        playerDefend = manager.get("texture/player/player_defend.png");
        playerItem = manager.get("texture/player/player_item.png");
        playerEscape = manager.get("texture/player/player_flee.png");
        playerHack = manager.get("texture/player/player_hack.png");
        playerDeath = manager.get("texture/player/player_stun.png");
        playerTakeHit = manager.get("texture/player/player_damage.png");
        playerGameMoving = manager.get("texture/player/player_move.png");
        // Skills
        healthPlus = manager.get("texture/skills/plusHealth.png");
        healthMinus = manager.get("texture/skills/minusHealth.png");
        criticalHit = manager.get("texture/skills/criticalHit.png");
        miss = manager.get("texture/skills/miss.png");
        healing = manager.get("texture/skills/healing.png");
        dotMinus = manager.get("texture/skills/dotMinus.png");
        dotPlus = manager.get("texture/skills/dotPlus.png");
        // Other
        imgBgHall = manager.get("texture/bg_hall1.png");
        imgBgBoss = manager.get("texture/bg_hall1_boss.png");
        imgTopBar = manager.get("texture/topbar.png");
        imgBottomBar = manager.get("texture/bottombar.png");
        escapeBg = manager.get("texture/escapeBackground.png");
        hpBarLeft = manager.get("texture/hpbar_left.png");
        hpBarRight = manager.get("texture/hpbar_right.png");
        powerUpBg = manager.get("texture/powerUpBg.jpg");
        powerUpPopup = manager.get("texture/powerUpPopup.jpg");
        itemBg = manager.get("texture/itemBg.jpg");

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
        Bosses Textures and animations
         */
        // Roombot
        t_roombotIdle = manager.get("texture/roombot/roombot_idle.png");
        t_roombotSkill = manager.get("texture/roombot/roombot_attack.png");
        t_roombotDamage = manager.get("texture/roombot/roombot_damage.png");
        t_roombotHack = manager.get("texture/roombot/roombot_stun.png");
        a_roombotIdle = createAnims.createAnimation(t_roombotIdle, 4, 4);
        a_roombotSkill = createAnims.createAnimation(t_roombotSkill, 4, 1);
        a_roombotDamage = createAnims.createAnimation(t_roombotDamage, 4, 1);
        a_roombotHack = createAnims.createAnimation(t_roombotHack, 4, 2);
        // Copper

        /*
        Music
         */
        musMainTheme = manager.get("music/mainTheme.mp3");
        musMainTheme.setLooping(true);
        musBossRobo = manager.get("music/bossRobo.mp3");
        musBossRobo.setLooping(true);
        musBossFuturistic = manager.get("music/bossFuturistic.mp3");
        musBossFuturistic.setLooping(true);
        // Remember to add all new music here
        allMusic = new Music[] {musMainTheme, musBossRobo, musBossFuturistic};
    }

    private void loadAssets() {
        /*
        Textures
         */
        // Player
        manager.load("texture/player/player_idle.png", Texture.class);
        manager.load("texture/player/player_attack.png", Texture.class);
        manager.load("texture/player/player_defend.png", Texture.class);
        manager.load("texture/player/player_item.png", Texture.class);
        manager.load("texture/player/player_flee.png", Texture.class);
        manager.load("texture/player/player_hack.png", Texture.class);
        manager.load("texture/player/player_stun.png", Texture.class);
        manager.load("texture/player/player_damage.png", Texture.class);
        manager.load("texture/player/player_move.png", Texture.class);
        // Skills
        manager.load("texture/skills/plusHealth.png", Texture.class);
        manager.load("texture/skills/minusHealth.png", Texture.class);
        manager.load("texture/skills/criticalHit.png", Texture.class);
        manager.load("texture/skills/miss.png", Texture.class);
        manager.load("texture/skills/healing.png", Texture.class);
        manager.load("texture/skills/dotMinus.png", Texture.class);
        manager.load("texture/skills/dotPlus.png", Texture.class);
        // Other
        manager.load("texture/bg_hall1.png", Texture.class);
        manager.load("texture/bg_hall1_boss.png", Texture.class);
        manager.load("texture/topbar.png", Texture.class);
        manager.load("texture/bottombar.png", Texture.class);
        manager.load("texture/escapeBackground.png", Texture.class);
        manager.load("texture/hpbar_left.png", Texture.class);
        manager.load("texture/hpbar_right.png", Texture.class);
        manager.load("texture/powerUpBg.jpg", Texture.class);
        manager.load("texture/powerUpPopup.jpg", Texture.class);
        manager.load("texture/itemBg.jpg", Texture.class);
        // Bosses
        manager.load("texture/roombot/roombot_idle.png", Texture.class);
        manager.load("texture/roombot/roombot_attack.png", Texture.class);
        manager.load("texture/roombot/roombot_damage.png", Texture.class);
        manager.load("texture/roombot/roombot_stun.png", Texture.class);

        /*
        Music
         */
        manager.load("music/mainTheme.mp3", Music.class);
        manager.load("music/bossRobo.mp3", Music.class);
        manager.load("music/bossFuturistic.mp3", Music.class);
    }
}
