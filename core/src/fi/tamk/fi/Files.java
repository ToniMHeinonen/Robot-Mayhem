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
            dotMinus, dotPlus, physicalHit, physicalHitLow, skillHit, skillHitLow;
    public final Animation<TextureRegion> animHealthPlusDoT, animHealthMinusDoT,
            animMiss, animCriticalHit, animHealing, animDoTPlus, animDoTMinus, animPhysicalHit,
            animPhysicalHitLow, animSkillHit, animSkillHitLow;

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

    // Copier textures and animations
    private final Texture t_copierIdle, t_copierAttack, t_copierDamage, t_copierStun;
    public final Animation<TextureRegion> a_copierIdle, a_copierAttack, a_copierDamage,
            a_copierStun;

    // Other textures
    public final Texture imgBgHall, imgBgBoss, imgTopBar, imgBottomBar, escapeBg, hpBarLeft,
            hpBarRight, powerUpBg, powerUpPopup, itemBg, retrieveStepsBg, dotArrowUp, dotArrowDown;

    // Skins
    public final Skin skin, finalSkin;

    // Music
    public final Music[] allBossMusic;
    public final Music musMainTheme, musBossRobo, musBossFuturistic, musBossSankari;

    // Sound effects fight general
    public final Sound sndHealOverTime, sndFastHeal, sndLoseToBoss, sndPlayerMiss,
            sndPlayerCriticalHit, sndEnemyMiss, sndEnemyCriticalHit, sndDamageOverTime,
            sndBreakShield, sndHackSuccessful, sndChoosePowerUp, sndPowerUpPopup,
            sndMetallicHit;

    // Sound effects skills
    public final Sound sndDustThrow, sndDefend, sndSuction;

    // Sound effect other
    public final Sound  sndMilestoneAchieved, sndPurchaseItem, sndUseItem, sndNotification;

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
        dotMinus = manager.get("texture/skills/dotNegative.png");
        dotPlus = manager.get("texture/skills/dotPositive.png");
        physicalHit = manager.get("texture/skills/physicalHit.png");
        physicalHitLow = manager.get("texture/skills/physicalHitLow.png");
        skillHit = manager.get("texture/skills/skillHit.png");
        skillHitLow = manager.get("texture/skills/skillHitLow.png");
        // Other
        imgBgHall = manager.get("texture/bg_hall1.png");
        imgBgBoss = manager.get("texture/bg_hall1_boss.png");
        imgTopBar = manager.get("texture/topbar.png");
        imgBottomBar = manager.get("texture/bottombar.png");
        escapeBg = manager.get("texture/escapeBackground.png");
        hpBarLeft = manager.get("texture/hpbar_left.png");
        hpBarRight = manager.get("texture/hpbar_right.png");
        powerUpBg = manager.get("texture/powerUpBg.png");
        powerUpPopup = manager.get("texture/powerUpPopup.jpg");
        itemBg = manager.get("texture/itemBg.jpg");
        retrieveStepsBg = manager.get("texture/retrieveStepsBg.png");
        dotArrowUp = manager.get("texture/status_positive.png");
        dotArrowDown = manager.get("texture/status_negative.png");

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
        animHealthPlusDoT = createAnims.createAnimation(healthPlus, 4, 1);
        animHealthMinusDoT = createAnims.createAnimation(healthMinus, 4, 1);
        animCriticalHit = createAnims.createAnimation(criticalHit, 4, 1);
        animMiss = createAnims.createAnimation(miss, 4, 1);
        animHealing = createAnims.createAnimation(healing, 4, 1);
        animDoTPlus = createAnims.createAnimation(dotPlus, 4, 1);
        animDoTMinus = createAnims.createAnimation(dotMinus, 4, 1);
        animPhysicalHit = createAnims.createAnimation(physicalHit, 4, 1);
        animPhysicalHitLow = createAnims.createAnimation(physicalHitLow, 4, 1);
        animSkillHit = createAnims.createAnimation(skillHit, 4, 1);
        animSkillHitLow = createAnims.createAnimation(skillHitLow, 4, 1);

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
        // Copier
        t_copierIdle = manager.get("texture/copier/copier_idle.png");
        t_copierAttack = manager.get("texture/copier/copier_attack.png");
        t_copierDamage = manager.get("texture/copier/copier_damage.png");
        t_copierStun = manager.get("texture/copier/copier_stun.png");
        a_copierIdle = createAnims.createAnimation(t_copierIdle, 4, 4);
        a_copierAttack = createAnims.createAnimation(t_copierAttack, 4, 1);
        a_copierDamage = createAnims.createAnimation(t_copierDamage, 4, 1);
        a_copierStun = createAnims.createAnimation(t_copierStun, 4, 2);

        /*
        Skins
         */
        skin = manager.get("glassy-ui.json");
        finalSkin = manager.get("finalskin/finalskin.json");

        /*
        Music
         */
        musMainTheme = manager.get("music/mainTheme.mp3");
        musBossRobo = manager.get("music/bossRobo.mp3");
        musBossFuturistic = manager.get("music/bossFuturistic.mp3");
        musBossSankari = manager.get("music/bossSankari.mp3");

        // Remember to add all new music here
        allBossMusic = new Music[] {musBossRobo, musBossFuturistic, musBossSankari};

        /*
        Sound effects
         */
        // Fight general
        sndFastHeal = manager.get("sound/fightGeneral/FastHeal.mp3");
        sndHealOverTime = manager.get("sound/fightGeneral/HealOverTime.mp3");
        sndLoseToBoss = manager.get("sound/fightGeneral/LoseToBoss.mp3");
        sndPlayerMiss = manager.get("sound/fightGeneral/MissedHit.mp3");
        sndPlayerCriticalHit = manager.get("sound/fightGeneral/CriticalHit.mp3");
        sndEnemyMiss = manager.get("sound/fightGeneral/EnemyMiss.mp3");
        sndEnemyCriticalHit = manager.get("sound/fightGeneral/ReceiveCriticalHit.mp3");
        sndDamageOverTime = manager.get("sound/fightGeneral/ReceiveDot.mp3");
        sndBreakShield = manager.get("sound/fightGeneral/BreakFireballOrb.mp3");
        sndHackSuccessful = manager.get("sound/fightGeneral/HitBossWithSuccessfulHack.mp3");
        sndChoosePowerUp = manager.get("sound/fightGeneral/PickPowerUpOrItem.mp3");
        sndPowerUpPopup = manager.get("sound/fightGeneral/PowerUpsPopUp.mp3");
        sndMetallicHit = manager.get("sound/fightGeneral/MetallicHit.mp3");
        // Skills
        sndDustThrow = manager.get("sound/skills/DustThrow.mp3");
        sndSuction = manager.get("sound/skills/Suction.mp3");
        sndDefend = manager.get("sound/skills/ShieldAbilitySound.mp3");
        // Other
        sndMilestoneAchieved = manager.get("sound/MileStoneAchieved.mp3");
        sndPurchaseItem = manager.get("sound/PurchaseItemFromShop.mp3");
        sndUseItem = manager.get("sound/GeneralItemUse.mp3");
        sndNotification = manager.get("sound/GeneralNotification.mp3");
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
        manager.load("texture/skills/dotNegative.png", Texture.class);
        manager.load("texture/skills/dotPositive.png", Texture.class);
        manager.load("texture/skills/physicalHit.png", Texture.class);
        manager.load("texture/skills/physicalHitLow.png", Texture.class);
        manager.load("texture/skills/skillHit.png", Texture.class);
        manager.load("texture/skills/skillHitLow.png", Texture.class);
        // Other
        manager.load("texture/bg_hall1.png", Texture.class);
        manager.load("texture/bg_hall1_boss.png", Texture.class);
        manager.load("texture/topbar.png", Texture.class);
        manager.load("texture/bottombar.png", Texture.class);
        manager.load("texture/escapeBackground.png", Texture.class);
        manager.load("texture/hpbar_left.png", Texture.class);
        manager.load("texture/hpbar_right.png", Texture.class);
        manager.load("texture/powerUpBg.png", Texture.class);
        manager.load("texture/powerUpPopup.jpg", Texture.class);
        manager.load("texture/itemBg.jpg", Texture.class);
        manager.load("texture/retrieveStepsBg.png", Texture.class);
        manager.load("texture/status_negative.png", Texture.class);
        manager.load("texture/status_positive.png", Texture.class);
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
        manager.load("texture/copier/copier_idle.png", Texture.class);
        manager.load("texture/copier/copier_attack.png", Texture.class);
        manager.load("texture/copier/copier_damage.png", Texture.class);
        manager.load("texture/copier/copier_stun.png", Texture.class);

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
        manager.load("music/bossSankari.mp3", Music.class);

        /*
        Sound effects
         */
        // Fight general
        manager.load("sound/fightGeneral/FastHeal.mp3", Sound.class);
        manager.load("sound/fightGeneral/HealOverTime.mp3", Sound.class);
        manager.load("sound/fightGeneral/LoseToBoss.mp3", Sound.class);
        manager.load("sound/fightGeneral/MissedHit.mp3", Sound.class);
        manager.load("sound/fightGeneral/CriticalHit.mp3", Sound.class);
        manager.load("sound/fightGeneral/EnemyMiss.mp3", Sound.class);
        manager.load("sound/fightGeneral/ReceiveCriticalHit.mp3", Sound.class);
        manager.load("sound/fightGeneral/ReceiveDot.mp3", Sound.class);
        manager.load("sound/fightGeneral/BreakFireballOrb.mp3", Sound.class);
        manager.load("sound/fightGeneral/HitBossWithSuccessfulHack.mp3", Sound.class);
        manager.load("sound/fightGeneral/PickPowerUpOrItem.mp3", Sound.class);
        manager.load("sound/fightGeneral/PowerUpsPopUp.mp3", Sound.class);
        manager.load("sound/fightGeneral/MetallicHit.mp3", Sound.class);
        // Skills
        manager.load("sound/skills/DustThrow.mp3", Sound.class);
        manager.load("sound/skills/Suction.mp3", Sound.class);
        manager.load("sound/skills/ShieldAbilitySound.mp3", Sound.class);
        // Other
        manager.load("sound/MileStoneAchieved.mp3", Sound.class);
        manager.load("sound/PurchaseItemFromShop.mp3", Sound.class);
        manager.load("sound/GeneralItemUse.mp3", Sound.class);
        manager.load("sound/GeneralNotification.mp3", Sound.class);
    }
}
