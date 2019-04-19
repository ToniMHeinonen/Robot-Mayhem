package fi.phstudios.robotmayhem;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetHandler {
    public final AssetManager manager = new AssetManager();

    // Splash Screen
    public final String splashScreen = "texture/splashScreen.png";

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

    // Skills

    AssetHandler() {
        /*
        Textures
         */
        // Splash Screen
        manager.load(splashScreen, Texture.class);

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
