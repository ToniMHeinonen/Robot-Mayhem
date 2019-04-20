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
    public final String splashScreenFIN = "texture/splashScreenFIN.jpg";
    public final String splashScreenENG = "texture/splashScreenENG.jpg";

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


    AssetHandler() {
        /*
        Textures
         */
        // Splash Screen
        manager.load(splashScreenFIN, Texture.class);
        manager.load(splashScreenENG, Texture.class);

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
