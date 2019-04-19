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
        manager.load("texture/pc/pc_idle.png", Texture.class);
        manager.load("texture/pc/pc_attack.png", Texture.class);
        manager.load("texture/pc/pc_damage.png", Texture.class);
        manager.load("texture/pc/pc_stun.png", Texture.class);

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
