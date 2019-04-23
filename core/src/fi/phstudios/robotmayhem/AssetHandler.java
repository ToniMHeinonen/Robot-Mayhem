package fi.phstudios.robotmayhem;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetHandler {
    public final AssetManager manager = new AssetManager();

    /*
    Textures
    */
    // Splash Screen
    public final String splashFI1 = "texture/splash/splash1_fi.jpg";
    public final String splashFI2 = "texture/splash/splash2_fi.jpg";
    public final String splashFI3 = "texture/splash/splash3_fi.jpg";
    public final String splashFI4 = "texture/splash/splash4_fi.jpg";
    public final String splashEN1 = "texture/splash/splash1_en.jpg";
    public final String splashEN2 = "texture/splash/splash2_en.jpg";
    public final String splashEN3 = "texture/splash/splash3_en.jpg";
    public final String splashEN4 = "texture/splash/splash4_en.jpg";

    // Game end congratulations
    public final String congratsFI = "texture/congrats_fi.png";
    public final String congratsEN = "texture/congrats_en.png";

    // Player
    public final String playerIdle = "texture/player/player_idle.png";
    public final String playerAttack = "texture/player/player_attack.png";
    public final String playerDefend = "texture/player/player_defend.png";
    public final String playerItem = "texture/player/player_item.png";
    public final String playerFlee = "texture/player/player_flee.png";
    public final String playerHack = "texture/player/player_hack.png";
    public final String playerStun = "texture/player/player_stun.png";
    public final String playerDamage = "texture/player/player_damage.png";
    public final String playerMove = "texture/player/player_move.png";

    // Skills
    public final String plusHealth = "texture/skills/plusHealth.png";
    public final String minusHealth = "texture/skills/minusHealth.png";
    public final String criticalHit = "texture/skills/criticalHit.png";
    public final String miss = "texture/skills/miss.png";
    public final String healing = "texture/skills/healing.png";
    public final String dotNegative = "texture/skills/dotNegative.png";
    public final String dotPositive = "texture/skills/dotPositive.png";
    public final String physicalHit = "texture/skills/physicalHit.png";
    public final String physicalHitLow = "texture/skills/physicalHitLow.png";
    public final String skillHit = "texture/skills/skillHit.png";
    public final String skillHitLow = "texture/skills/skillHitLow.png";

    // Other
    public final String bgHall1 = "texture/bg_hall1.png";
    public final String bgHall1Boss = "texture/bg_hall1_boss.png";
    public final String topBar = "texture/topbar.png";
    public final String bottomBar = "texture/bottombar.png";
    public final String escapeBackground = "texture/escapeBackground.png";
    public final String hpBarLeft = "texture/hpbar_left.png";
    public final String hpBarRight = "texture/hpbar_right.png";
    public final String powerUpBg = "texture/powerUpBg.png";
    public final String powerUpPopup = "texture/powerUpPopup.jpg";
    public final String itemBg = "texture/itemBg.jpg";
    public final String retrieveStepsBg = "texture/retrieveStepsBg.png";
    public final String statusNegative = "texture/status_negative.png";
    public final String statusPositive = "texture/status_positive.png";

    // Bosses
    public final String roombotIdle = "texture/roombot/roombot_idle.png";
    public final String roombotAttack = "texture/roombot/roombot_attack.png";
    public final String roombotDamage = "texture/roombot/roombot_damage.png";
    public final String roombotStun = "texture/roombot/roombot_stun.png";
    public final String robberIdle = "texture/robber/robber_idle.png";
    public final String robberAttack = "texture/robber/robber_attack.png";
    public final String robberDamage = "texture/robber/robber_damage.png";
    public final String robberStun = "texture/robber/robber_stun.png";
    public final String copperIdle = "texture/copper/copper_idle.png";
    public final String copperAttack = "texture/copper/copper_attack.png";
    public final String copperDamage = "texture/copper/copper_damage.png";
    public final String copperStun = "texture/copper/copper_stun.png";
    public final String copierIdle = "texture/copier/copier_idle.png";
    public final String copierAttack = "texture/copier/copier_attack.png";
    public final String copierDamage = "texture/copier/copier_damage.png";
    public final String copierStun = "texture/copier/copier_stun.png";
    public final String pcIdle = "texture/pc/pc_idle.png";
    public final String pcAttack = "texture/pc/pc_attack.png";
    public final String pcDamage = "texture/pc/pc_damage.png";
    public final String pcStun = "texture/pc/pc_stun.png";
    public final String ballerIdle = "texture/baller/baller_idle.png";
    public final String ballerAttack = "texture/baller/baller_attack.png";
    public final String ballerDamage = "texture/baller/baller_damage.png";
    public final String ballerStun = "texture/baller/baller_stun.png";
    public final String fabioIdle = "texture/fabio/fabio_idle.png";
    public final String fabioAttack = "texture/fabio/fabio_attack.png";
    public final String fabioDamage = "texture/fabio/fabio_damage.png";
    public final String fabioStun = "texture/fabio/fabio_stun.png";

    /*
    Skins
     */
    public final String skin = "glassy-ui.json";
    public final String finalSkin = "finalskin/finalskin.json";

    /*
    Music
     */
    public final String musMainTheme = "music/mainTheme.mp3";
    public final String musBossRobo = "music/bossRobo.mp3";
    public final String musBossFuturistic = "music/bossFuturistic.mp3";
    public final String musBossSankari = "music/bossSankari.mp3";

    /*
    Sound effects
     */
    // Fight general
    public final String sndFastHeal = "sound/fightGeneral/FastHeal.mp3";
    public final String sndHealOverTime = "sound/fightGeneral/HealOverTime.mp3";
    public final String sndLoseToBoss = "sound/fightGeneral/LoseToBoss.mp3";
    public final String sndMissedHit = "sound/fightGeneral/MissedHit.mp3";
    public final String sndCriticalHit = "sound/fightGeneral/CriticalHit.mp3";
    public final String sndEnemyMiss = "sound/fightGeneral/EnemyMiss.mp3";
    public final String sndReceiveCriticalHit = "sound/fightGeneral/ReceiveCriticalHit.mp3";
    public final String sndReceiveDoT = "sound/fightGeneral/ReceiveDot.mp3";
    public final String sndBreakFireballOrb = "sound/fightGeneral/BreakFireballOrb.mp3";
    public final String sndSuccessfulHack = "sound/fightGeneral/HitBossWithSuccessfulHack.mp3";
    public final String sndPickPowerupOrItem = "sound/fightGeneral/PickPowerUpOrItem.mp3";
    public final String sndPowerupsPopup = "sound/fightGeneral/PowerUpsPopUp.mp3";
    public final String sndMetallicHit = "sound/fightGeneral/MetallicHit.mp3";
    // Skills
    public final String sndDustThrow = "sound/skills/DustThrow.mp3";
    public final String sndSuction = "sound/skills/Suction.mp3";
    public final String sndShieldAbility = "sound/skills/ShieldAbilitySound.mp3";
    // Other
    public final String sndMilestoneAchieved = "sound/MileStoneAchieved.mp3";
    public final String sndPurchaseItem = "sound/PurchaseItemFromShop.mp3";
    public final String sndGeneralItemUse = "sound/GeneralItemUse.mp3";
    public final String sndGeneralNotification = "sound/GeneralNotification.mp3";

    AssetHandler() {
        /*
        Textures
         */
        // Splash Screen
        manager.load(splashFI1, Texture.class);
        manager.load(splashFI2, Texture.class);
        manager.load(splashFI3, Texture.class);
        manager.load(splashFI4, Texture.class);
        manager.load(splashEN1, Texture.class);
        manager.load(splashEN2, Texture.class);
        manager.load(splashEN3, Texture.class);
        manager.load(splashEN4, Texture.class);

        // Game end congratulations
        manager.load(congratsFI, Texture.class);
        manager.load(congratsEN, Texture.class);

        // Player
        manager.load(playerIdle, Texture.class);
        manager.load(playerAttack, Texture.class);
        manager.load(playerDefend, Texture.class);
        manager.load(playerItem, Texture.class);
        manager.load(playerFlee, Texture.class);
        manager.load(playerHack, Texture.class);
        manager.load(playerStun, Texture.class);
        manager.load(playerDamage, Texture.class);
        manager.load(playerMove, Texture.class);
        // Skills
        manager.load(plusHealth, Texture.class);
        manager.load(minusHealth, Texture.class);
        manager.load(criticalHit, Texture.class);
        manager.load(miss, Texture.class);
        manager.load(healing, Texture.class);
        manager.load(dotNegative, Texture.class);
        manager.load(dotPositive, Texture.class);
        manager.load(physicalHit, Texture.class);
        manager.load(physicalHitLow, Texture.class);
        manager.load(skillHit, Texture.class);
        manager.load(skillHitLow, Texture.class);
        // Other
        manager.load(bgHall1, Texture.class);
        manager.load(bgHall1Boss, Texture.class);
        manager.load(topBar, Texture.class);
        manager.load(bottomBar, Texture.class);
        manager.load(escapeBackground, Texture.class);
        manager.load(hpBarLeft, Texture.class);
        manager.load(hpBarRight, Texture.class);
        manager.load(powerUpBg, Texture.class);
        manager.load(powerUpPopup, Texture.class);
        manager.load(itemBg, Texture.class);
        manager.load(retrieveStepsBg, Texture.class);
        manager.load(statusNegative, Texture.class);
        manager.load(statusPositive, Texture.class);
        // Bosses
        manager.load(roombotIdle, Texture.class);
        manager.load(roombotAttack, Texture.class);
        manager.load(roombotDamage, Texture.class);
        manager.load(roombotStun, Texture.class);
        manager.load(robberIdle, Texture.class);
        manager.load(robberAttack, Texture.class);
        manager.load(robberDamage, Texture.class);
        manager.load(robberStun, Texture.class);
        manager.load(copperIdle, Texture.class);
        manager.load(copperAttack, Texture.class);
        manager.load(copperDamage, Texture.class);
        manager.load(copperStun, Texture.class);
        manager.load(copierIdle, Texture.class);
        manager.load(copierAttack, Texture.class);
        manager.load(copierDamage, Texture.class);
        manager.load(copierStun, Texture.class);
        manager.load(pcIdle, Texture.class);
        manager.load(pcAttack, Texture.class);
        manager.load(pcDamage, Texture.class);
        manager.load(pcStun, Texture.class);
        manager.load(ballerIdle, Texture.class);
        manager.load(ballerAttack, Texture.class);
        manager.load(ballerDamage, Texture.class);
        manager.load(ballerStun, Texture.class);
        manager.load(fabioIdle, Texture.class);
        manager.load(fabioAttack, Texture.class);
        manager.load(fabioDamage, Texture.class);
        manager.load(fabioStun, Texture.class);

        /*
        Skins
         */
        manager.load(skin, Skin.class);
        manager.load(finalSkin, Skin.class);

        /*
        Music
         */
        manager.load(musMainTheme, Music.class);
        manager.load(musBossRobo, Music.class);
        manager.load(musBossFuturistic, Music.class);
        manager.load(musBossSankari, Music.class);

        /*
        Sound effects
         */
        // Fight general
        manager.load(sndFastHeal, Sound.class);
        manager.load(sndHealOverTime, Sound.class);
        manager.load(sndLoseToBoss, Sound.class);
        manager.load(sndMissedHit, Sound.class);
        manager.load(sndCriticalHit, Sound.class);
        manager.load(sndEnemyMiss, Sound.class);
        manager.load(sndReceiveCriticalHit, Sound.class);
        manager.load(sndReceiveDoT, Sound.class);
        manager.load(sndBreakFireballOrb, Sound.class);
        manager.load(sndSuccessfulHack, Sound.class);
        manager.load(sndPickPowerupOrItem, Sound.class);
        manager.load(sndPowerupsPopup, Sound.class);
        manager.load(sndMetallicHit, Sound.class);
        // Skills
        manager.load(sndDustThrow, Sound.class);
        manager.load(sndSuction, Sound.class);
        manager.load(sndShieldAbility, Sound.class);
        // Other
        manager.load(sndMilestoneAchieved, Sound.class);
        manager.load(sndPurchaseItem, Sound.class);
        manager.load(sndGeneralItemUse, Sound.class);
        manager.load(sndGeneralNotification, Sound.class);
    }
}
