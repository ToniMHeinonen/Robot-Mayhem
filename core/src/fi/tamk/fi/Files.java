package fi.tamk.fi;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Files {
    public final AssetManager manager = new AssetManager();
    private final Animating createAnims = new Animating();

    // Player textures and animations
    private final Texture playerIdle, playerAttack, playerDefend, playerItem, playerEscape,
    playerHack, playerDeath, playerTakeHit, playerGameMoving;
    public final Animation<TextureRegion> animIdle, animSkill, animDefend, animEscape,
            animItem, animHack, animDeath, animTakeHitAnim, animGameMoving;

    // Skills textures and animations
    private final Texture healthPlus, healthMinus, criticalHit, miss, healing,
            dotMinus, dotPlus, physicalHit;
    public final Animation<TextureRegion> animHealthPlusDoT, animHealthMinusDoT,
            animMiss, animCriticalHit, animHealing, animDoTPlus, animDoTMinus, animPhysicalHit;

    // Roombot textures and animations
    private final Texture t_roombotIdle, t_roombotAttack, t_roombotDamage, t_roombotStun;
    public final Animation<TextureRegion> a_roombotIdle, a_roombotAttack, a_roombotDamage,
                                        a_roombotStun;
    // Robber textures and animations
    private final Texture t_robberIdle, t_robberAttack, t_robberDamage, t_robberStun;
    public final Animation<TextureRegion> a_robberIdle, a_robberAttack, a_robberDamage,
            a_robberStun;

    // Copper textures and animations
    private final Texture t_copperIdle, t_copperAttack, t_copperDamage, t_copperStun;
    public final Animation<TextureRegion> a_copperIdle, a_copperAttack, a_copperDamage,
            a_copperStun;

    // Other textures
    public final Texture imgBgHall, imgBgBoss, imgTopBar, imgBottomBar, escapeBg, hpBarLeft,
            hpBarRight, powerUpBg, powerUpPopup, itemBg;

    // Skins
    public final Skin skin, finalSkin;

    // Music
    public final Music[] allMusic;
    public final Music musMainTheme, musBossRobo, musBossFuturistic;

    // Sound effects
    public final Sound sndHealOverTime, sndFastHeal, sndLoseToBoss, sndMissedHit, sndCriticalHit,
    sndDamageOverTime, sndBreakShield, sndMilestoneAchieved, sndPurchaseItem;

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
        physicalHit = manager.get("texture/skills/physicalHit.png");
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
        animGameMoving = createAnims.createAnimation(playerGameMoving, 4, 1);
        animHealthPlusDoT = createAnims.createAnimation(healthPlus, 3, 1);
        animHealthMinusDoT = createAnims.createAnimation(healthMinus, 3, 1);
        animCriticalHit = createAnims.createAnimation(criticalHit, 4, 1);
        animMiss = createAnims.createAnimation(miss, 4, 1);
        animHealing = createAnims.createAnimation(healing, 4, 1);
        animDoTPlus = createAnims.createAnimation(dotPlus, 4, 1);
        animDoTMinus = createAnims.createAnimation(dotMinus, 4, 1);
        animPhysicalHit = createAnims.createAnimation(physicalHit, 4, 1);

        /*
        Bosses Textures and animations
         */
        // Roombot
        t_roombotIdle = manager.get("texture/roombot/roombot_idle.png");
        t_roombotAttack = manager.get("texture/roombot/roombot_attack.png");
        t_roombotDamage = manager.get("texture/roombot/roombot_damage.png");
        t_roombotStun = manager.get("texture/roombot/roombot_stun.png");
        a_roombotIdle = createAnims.createAnimation(t_roombotIdle, 4, 4);
        a_roombotAttack = createAnims.createAnimation(t_roombotAttack, 4, 1);
        a_roombotDamage = createAnims.createAnimation(t_roombotDamage, 4, 1);
        a_roombotStun = createAnims.createAnimation(t_roombotStun, 4, 2);
        // Robber
        t_robberIdle = manager.get("texture/robber/robber_idle.png");
        t_robberAttack = manager.get("texture/robber/robber_attack.png");
        t_robberDamage = manager.get("texture/robber/robber_damage.png");
        t_robberStun = manager.get("texture/robber/robber_stun.png");
        a_robberIdle = createAnims.createAnimation(t_robberIdle, 4, 4);
        a_robberAttack = createAnims.createAnimation(t_robberAttack, 4, 1);
        a_robberDamage = createAnims.createAnimation(t_robberDamage, 4, 1);
        a_robberStun = createAnims.createAnimation(t_robberStun, 4, 2);
        // Copper
        t_copperIdle = manager.get("texture/copper/copper_idle.png");
        t_copperAttack = manager.get("texture/copper/copper_attack.png");
        t_copperDamage = manager.get("texture/copper/copper_damage.png");
        t_copperStun = manager.get("texture/copper/copper_stun.png");
        a_copperIdle = createAnims.createAnimation(t_copperIdle, 4, 4);
        a_copperAttack = createAnims.createAnimation(t_copperAttack, 4, 1);
        a_copperDamage = createAnims.createAnimation(t_copperDamage, 4, 1);
        a_copperStun = createAnims.createAnimation(t_copperStun, 4, 2);

        /*
        Skins
         */
        skin = manager.get("glassy-ui.json");
        finalSkin = manager.get("finalskin/finalskin.json");

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

        /*
        Sound effects
         */
        sndFastHeal = manager.get("sound/FastHeal.mp3");
        sndHealOverTime = manager.get("sound/HealOverTime.mp3");
        sndLoseToBoss = manager.get("sound/LoseToBoss.mp3");
        sndMissedHit = manager.get("sound/MissedHit.mp3");
        sndCriticalHit = manager.get("sound/ReceiveCriticalHit.mp3");
        sndDamageOverTime = manager.get("sound/ReceiveDot.mp3");
        sndBreakShield = manager.get("sound/BreakFireballOrb.mp3");
        sndMilestoneAchieved = manager.get("sound/MileStoneAchieved.mp3");
        sndPurchaseItem = manager.get("sound/PurchaseItemFromShop.mp3");
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
        manager.load("texture/skills/physicalHit.png", Texture.class);
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
        manager.load("texture/robber/robber_idle.png", Texture.class);
        manager.load("texture/robber/robber_attack.png", Texture.class);
        manager.load("texture/robber/robber_damage.png", Texture.class);
        manager.load("texture/robber/robber_stun.png", Texture.class);
        manager.load("texture/copper/copper_idle.png", Texture.class);
        manager.load("texture/copper/copper_attack.png", Texture.class);
        manager.load("texture/copper/copper_damage.png", Texture.class);
        manager.load("texture/copper/copper_stun.png", Texture.class);

        /*
        Skins
         */
        manager.load("glassy-ui.json", Skin.class);
        manager.load("finalskin/finalskin.json", Skin.class);

        /*
        Music
         */
        manager.load("music/mainTheme.mp3", Music.class);
        manager.load("music/bossRobo.mp3", Music.class);
        manager.load("music/bossFuturistic.mp3", Music.class);

        /*
        Sound effects
         */
        manager.load("sound/FastHeal.mp3", Sound.class);
        manager.load("sound/HealOverTime.mp3", Sound.class);
        manager.load("sound/LoseToBoss.mp3", Sound.class);
        manager.load("sound/MissedHit.mp3", Sound.class);
        manager.load("sound/ReceiveCriticalHit.mp3", Sound.class);
        manager.load("sound/ReceiveDot.mp3", Sound.class);
        manager.load("sound/BreakFireballOrb.mp3", Sound.class);
        manager.load("sound/MileStoneAchieved.mp3", Sound.class);
        manager.load("sound/PurchaseItemFromShop.mp3", Sound.class);
    }
}
