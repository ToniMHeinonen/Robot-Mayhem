package fi.phstudios.robotmayhem;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Files {
    private final Animating createAnims = new Animating();

    // Game end congratz textures
    public final Texture congratsFI, congratsEN;

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

    // PC textures and animations
    private final Texture t_pcIdle, t_pcAttack, t_pcDamage, t_pcStun;
    public final Animation<TextureRegion> a_pcIdle, a_pcAttack, a_pcDamage,
            a_pcStun;

    // Baller textures and animations
    private final Texture t_ballerIdle, t_ballerAttack, t_ballerDamage, t_ballerStun;
    public final Animation<TextureRegion> a_ballerIdle, a_ballerAttack, a_ballerDamage,
            a_ballerStun;

    // Fabio textures and animations
    private final Texture t_fabioIdle, t_fabioAttack, t_fabioDamage, t_fabioStun;
    public final Animation<TextureRegion> a_fabioIdle, a_fabioAttack, a_fabioDamage,
            a_fabioStun;

    // Other textures
    public final Texture imgBgHall, imgBgBoss, imgTopBar, imgBottomBar, escapeBg, hpBarLeft,
            hpBarRight, powerUpBg, powerUpPopup, itemBg, retrieveStepsBg, dotArrowUp, dotArrowDown;

    // Skins
    public final Skin skin, finalSkin;

    // Music
    public final Music[] allBossMusic;
    public final Music musMainTheme, musBossRobo, musBossFuturistic, musBossSankari, musBossFabio;

    // Sound effects fight general
    public final Sound sndHealOverTime, sndFastHeal, sndLoseToBoss, sndPlayerMiss,
            sndPlayerCriticalHit, sndEnemyMiss, sndEnemyCriticalHit, sndDamageOverTime,
            sndBreakShield, sndHackSuccessful, sndChoosePowerUp, sndPowerUpPopup,
            sndMetallicHit;

    // Sound effects skills
    public final Sound sndDustThrow, sndDefend, sndSuction, sndBlackInk, sndCatThrower, sndCoinFlip,
    sndPCReboot, sndPCPopup, sndMayhem, sndOverpower, sndBucket, sndRustify, sndTrojan, sndWoosh1,
    sndWoosh2, sndWoosh3;

    // Sound effect other
    public final Sound  sndMilestoneAchieved, sndPurchaseItem, sndUseItem, sndNotification;

    Files (AssetHandler asset) {
        AssetManager manager = asset.manager;

        /*
        Textures and animations
         */
        // Game end congrats
        congratsFI = manager.get(asset.congratsFI);
        congratsEN = manager.get(asset.congratsEN);

        // Player
        playerIdle = manager.get(asset.playerIdle);
        playerAttack = manager.get(asset.playerAttack);
        playerDefend = manager.get(asset.playerDefend);
        playerItem = manager.get(asset.playerItem);
        playerEscape = manager.get(asset.playerFlee);
        playerHack = manager.get(asset.playerHack);
        playerDeath = manager.get(asset.playerStun);
        playerTakeHit = manager.get(asset.playerDamage);
        playerGameMoving = manager.get(asset.playerMove);
        // Skills
        healthPlus = manager.get(asset.plusHealth);
        healthMinus = manager.get(asset.minusHealth);
        criticalHit = manager.get(asset.criticalHit);
        miss = manager.get(asset.miss);
        healing = manager.get(asset.healing);
        dotMinus = manager.get(asset.dotNegative);
        dotPlus = manager.get(asset.dotPositive);
        physicalHit = manager.get(asset.physicalHit);
        physicalHitLow = manager.get(asset.physicalHitLow);
        skillHit = manager.get(asset.skillHit);
        skillHitLow = manager.get(asset.skillHitLow);
        // Other
        imgBgHall = manager.get(asset.bgHall1);
        imgBgBoss = manager.get(asset.bgHall1Boss);
        imgTopBar = manager.get(asset.topBar);
        imgBottomBar = manager.get(asset.bottomBar);
        escapeBg = manager.get(asset.escapeBackground);
        hpBarLeft = manager.get(asset.hpBarLeft);
        hpBarRight = manager.get(asset.hpBarRight);
        powerUpBg = manager.get(asset.powerUpBg);
        powerUpPopup = manager.get(asset.powerUpPopup);
        itemBg = manager.get(asset.itemBg);
        retrieveStepsBg = manager.get(asset.retrieveStepsBg);
        dotArrowUp = manager.get(asset.statusPositive);
        dotArrowDown = manager.get(asset.statusNegative);

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
        t_roombotIdle = manager.get(asset.roombotIdle);
        t_roombotAttack = manager.get(asset.roombotAttack);
        t_roombotDamage = manager.get(asset.roombotDamage);
        t_roombotStun = manager.get(asset.roombotStun);
        a_roombotIdle = createAnims.createAnimation(t_roombotIdle, 4, 4);
        a_roombotAttack = createAnims.createAnimation(t_roombotAttack, 4, 1);
        a_roombotDamage = createAnims.createAnimation(t_roombotDamage, 4, 1);
        a_roombotStun = createAnims.createAnimation(t_roombotStun, 4, 2);
        // Robber
        t_robberIdle = manager.get(asset.robberIdle);
        t_robberAttack = manager.get(asset.robberAttack);
        t_robberDamage = manager.get(asset.robberDamage);
        t_robberStun = manager.get(asset.robberStun);
        a_robberIdle = createAnims.createAnimation(t_robberIdle, 4, 4);
        a_robberAttack = createAnims.createAnimation(t_robberAttack, 4, 1);
        a_robberDamage = createAnims.createAnimation(t_robberDamage, 4, 1);
        a_robberStun = createAnims.createAnimation(t_robberStun, 4, 2);
        // Copper
        t_copperIdle = manager.get(asset.copperIdle);
        t_copperAttack = manager.get(asset.copperAttack);
        t_copperDamage = manager.get(asset.copperDamage);
        t_copperStun = manager.get(asset.copperStun);
        a_copperIdle = createAnims.createAnimation(t_copperIdle, 4, 4);
        a_copperAttack = createAnims.createAnimation(t_copperAttack, 4, 1);
        a_copperDamage = createAnims.createAnimation(t_copperDamage, 4, 1);
        a_copperStun = createAnims.createAnimation(t_copperStun, 4, 2);
        // Copier
        t_copierIdle = manager.get(asset.copierIdle);
        t_copierAttack = manager.get(asset.copierAttack);
        t_copierDamage = manager.get(asset.copierDamage);
        t_copierStun = manager.get(asset.copierStun);
        a_copierIdle = createAnims.createAnimation(t_copierIdle, 4, 4);
        a_copierAttack = createAnims.createAnimation(t_copierAttack, 4, 1);
        a_copierDamage = createAnims.createAnimation(t_copierDamage, 4, 1);
        a_copierStun = createAnims.createAnimation(t_copierStun, 4, 2);
        // PC
        t_pcIdle = manager.get(asset.pcIdle);
        t_pcAttack = manager.get(asset.pcAttack);
        t_pcDamage = manager.get(asset.pcDamage);
        t_pcStun = manager.get(asset.pcStun);
        a_pcIdle = createAnims.createAnimation(t_pcIdle, 4, 4);
        a_pcAttack = createAnims.createAnimation(t_pcAttack, 4, 1);
        a_pcDamage = createAnims.createAnimation(t_pcDamage, 4, 1);
        a_pcStun = createAnims.createAnimation(t_pcStun, 4, 2);
        // Baller
        t_ballerIdle = manager.get(asset.ballerIdle);
        t_ballerAttack = manager.get(asset.ballerAttack);
        t_ballerDamage = manager.get(asset.ballerDamage);
        t_ballerStun = manager.get(asset.ballerStun);
        a_ballerIdle = createAnims.createAnimation(t_ballerIdle, 4, 4);
        a_ballerAttack = createAnims.createAnimation(t_ballerAttack, 4, 1);
        a_ballerDamage = createAnims.createAnimation(t_ballerDamage, 4, 1);
        a_ballerStun = createAnims.createAnimation(t_ballerStun, 4, 2);
        // Fabio
        t_fabioIdle = manager.get(asset.fabioIdle);
        t_fabioAttack = manager.get(asset.fabioAttack);
        t_fabioDamage = manager.get(asset.fabioDamage);
        t_fabioStun = manager.get(asset.fabioStun);
        a_fabioIdle = createAnims.createAnimation(t_fabioIdle, 4, 4);
        a_fabioAttack = createAnims.createAnimation(t_fabioAttack, 4, 1);
        a_fabioDamage = createAnims.createAnimation(t_fabioDamage, 4, 1);
        a_fabioStun = createAnims.createAnimation(t_fabioStun, 4, 2);

        /*
        Skins
         */
        skin = manager.get(asset.skin);
        finalSkin = manager.get(asset.finalSkin);

        /*
        Music
         */
        musMainTheme = manager.get(asset.musMainTheme);
        musBossRobo = manager.get(asset.musBossRobo);
        musBossFuturistic = manager.get(asset.musBossFuturistic);
        musBossSankari = manager.get(asset.musBossSankari);
        musBossFabio = manager.get(asset.musBossFabio);

        // Remember to add all new boss music here
        allBossMusic = new Music[] {musBossRobo, musBossFuturistic, musBossSankari};

        /*
        Sound effects
         */
        // Fight general
        sndFastHeal = manager.get(asset.sndFastHeal);
        sndHealOverTime = manager.get(asset.sndHealOverTime);
        sndLoseToBoss = manager.get(asset.sndLoseToBoss);
        sndPlayerMiss = manager.get(asset.sndMissedHit);
        sndPlayerCriticalHit = manager.get(asset.sndCriticalHit);
        sndEnemyMiss = manager.get(asset.sndEnemyMiss);
        sndEnemyCriticalHit = manager.get(asset.sndReceiveCriticalHit);
        sndDamageOverTime = manager.get(asset.sndReceiveDoT);
        sndBreakShield = manager.get(asset.sndBreakFireballOrb);
        sndHackSuccessful = manager.get(asset.sndSuccessfulHack);
        sndChoosePowerUp = manager.get(asset.sndPickPowerupOrItem);
        sndPowerUpPopup = manager.get(asset.sndPowerupsPopup);
        sndMetallicHit = manager.get(asset.sndMetallicHit);
        // Skills
        sndWoosh1 = manager.get(asset.sndWoosh1);
        sndWoosh2 = manager.get(asset.sndWoosh2);
        sndWoosh3 = manager.get(asset.sndWoosh3);
        sndDustThrow = manager.get(asset.sndDustThrow);
        sndSuction = manager.get(asset.sndSuction);
        sndDefend = manager.get(asset.sndShieldAbility);
        sndBlackInk = manager.get(asset.sndBlackInk);
        sndCatThrower = manager.get(asset.sndCatThrower);
        sndCoinFlip = manager.get(asset.sndCoinFlip);
        sndPCReboot = manager.get(asset.sndPCReboot);
        sndPCPopup = manager.get(asset.sndPCPopup);
        sndMayhem = manager.get(asset.sndMayhem);
        sndOverpower = manager.get(asset.sndOverpower);
        sndBucket = manager.get(asset.sndBucket);
        sndRustify = manager.get(asset.sndRustify);
        sndTrojan = manager.get(asset.sndTrojan);
        // Other
        sndMilestoneAchieved = manager.get(asset.sndMilestoneAchieved);
        sndPurchaseItem = manager.get(asset.sndPurchaseItem);
        sndUseItem = manager.get(asset.sndGeneralItemUse);
        sndNotification = manager.get(asset.sndGeneralNotification);
    }
}
