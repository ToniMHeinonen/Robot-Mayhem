package fi.phstudios.robotmayhem;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.HashMap;

public class Skills {

    private MainGame game;
    private Files files;
    private I18NBundle localize;

    public final String name = "name";
    public final String description = "description";
    public final String damage = "damage";
    public final String dmgPurePercent = "dmgPurePercent";
    public final String critChance = "critChance";
    public final String missChance = "missChance";
    public final String damageOverTime = "damageOverTime";
    public final String damageOverTimeTurns = "damageOverTimeTurns";
    public final String dotPurePercent = "dotPurePercent";
    public final String cooldown = "cooldown";
    public final String hitAnimation = "hitAnimation";
    public final String sound = "sound";
    public final String button = "button";
    public final String boostSelf = "boostSelf";
    public final String boostType = "boostType";
    public final String boostValue = "boostValue";

    // Notice 5% on miss and crit is 5 and in every other boost 0.05
    public final int BOOST_NONE = 0, BOOST_CRIT = 1, BOOST_MISS = 2, BOOST_DMG = 3, BOOST_ARMOR = 4,
    BOOST_HEAL = 5;

    // Names for localization
    public final String ATTACK = "ATTACK";
    public final String DEFEND = "DEFEND";
    public final String REFLECT = "REFLECT";
    public final String ITEM = "ITEM";
    public final String REPAIR = "REPAIR";
    // Roombot
    public final String SUCTION = "SUCTION";
    public final String DUST = "DUST";
    public final String BUCKET = "BUCKET";
    public final String SOAP = "SOAP";
    // Copier
    public final String DEJA_VU = "DEJA_VU";
    public final String FLASH = "FLASH";
    public final String COPYCAT = "COPYCAT";
    public final String BLACK_INK = "BLACK_INK";
    // Robber
    public final String HIJACK = "HIJACK";
    public final String PICKPOCKET = "PICKPOCKET";
    public final String MISCHIEF = "MISCHIEF";
    public final String SHADOWSTEP = "SHADOWSTEP";
    // PC
    public final String VIRUS = "VIRUS";
    public final String POPUP = "POPUP";
    public final String TROJAN = "TROJAN";
    public final String REBOOT = "REBOOT";
    // Copper
    public final String SPEEDING = "SPEEDING";
    public final String LOW_SPEED = "LOW_SPEED";
    public final String THE_LAW = "THE_LAW";
    public final String ORDER = "ORDER";
    // Baller
    public final String SHINE = "SHINE";
    public final String DIAMOND_ROLL = "DIAMOND_ROLL";
    public final String COIN_FLIP = "COIN_FLIP";
    public final String ENRICHMENT = "ENRICHMENT";
    // Fabio
    public final String RUSTIFY = "RUSTIFY";
    public final String OVERPOWER = "OVERPOWER";
    public final String MAYHEM = "MAYHEM";
    public final String VR = "VR";

    // Names for buttons
    public final String btnAttack = "ATTACK";
    public final String btnDefend = "DEFEND";
    public final String btnItem = "ITEM";
    public final String btnSkill = "SKILL";
    public final String btnHeal = "HEAL";

    private final ArrayList<String> allSkills = new ArrayList<String>();

    private final int defCrit = 10; // Default crit chance percent
    private final int defMiss = 5; // Default miss chance percent

    private Animation<TextureRegion> physicalHit, skillHit;

    private HashMap<String,HashMap<String,Object>> mapSkills;

    /**
     * Create maps for skills. Maps are used by other classes to retrieve correct values.
     * @param game main game instance
     */
    Skills(MainGame game) {
        this.game = game;
        files = game.getFiles();

        mapSkills = new HashMap<String, HashMap<String,Object>>();
        loadSkillAnimations();
        skillAttack();
        skillDefend();
        skillReflect();
        skillItem();
        skillRepair();
        // Roombot
        skillSuction();
        skillDust();
        skillBucket();
        skillSoap();
        // Copier
        skillDejaVu();
        skillFlash();
        skillCopycat();
        skillBlackInk();
        // Robber
        skillHijack();
        skillPickpocket();
        skillMischief();
        skillShadowstep();
        // PC
        skillVirus();
        skillPopup();
        skillTrojan();
        skillReboot();
        // Copper
        skillSpeeding();
        skillLowSpeed();
        skillTheLaw();
        skillOrder();
        // Baller
        skillShine();
        skillDiamondRoll();
        skillCoinFlip();
        skillEnrichment();
        // Fabio
        skillRustify();
        skillOverpower();
        skillMayhem();
        skillVR();
    }

    /* NOTE!
    Every time you add new skill, remember to:
    1. Make a String for it
    2. Add it's name and description to MyBundle file
    3. Retrieve it's localized name
    4. Make a new method for it
     */

    /* Explanations for values:
    - name = Name of the skill
    - description = Description of the skill
    - damage = The amount of damage it deals when it hits
    - dmgPurePercent = Same logic as in dotPurePercent
    - critChance = The amount of percent chance to deal 1.5x damage
    - missChance = The amount of percent chance to miss an attack
    - damageOverTime (DoT) = The amount of damage it deals before every round when inflicted
    - damageOverTimeTurn = The number of turns the DoT lasts
    - dotPurePercent = If to deal DoT in percents compared to MaxHp (example: value 20.0 deals 20
      percent of the MaxHp) or to deal DoT compared to defaultDamage (example: value 1.5 deal 1.5
      times the defaultDamage (1.5 * defaultDamage)
    - cooldown = How many turns the cooldown lasts
    - hitAnimation = What animation is played when hitting enemy
    - hitAnimationSpd = How fast does the animation move (on default use 8, this variable might be
      deleted in the future)
    - sound = The sound effect what plays on the start of move
    - button = Select correct button type for skill
    - boostSelf = Whether to do the boost to self or to opponent
    - boostType = What kind of boost does the skill do
    - boostValue = How much does the boost affect
     */

    /**
     * Retrieve correct skill map by using skill's name.
     * @param skill to retrieve
     * @return retrieved skill
     */
    public HashMap<String, Object> getSkill(String skill) {
        HashMap<String, Object> chosenSkill;

        chosenSkill = mapSkills.get(skill);

        return chosenSkill;
    }

    /**
     * Retrieve only the description of skills.
     * @param skill selected skill
     * @return description of skill
     */
    public String retrieveSkillDescription(String skill) {
        String desc = "";
        HashMap<String, Object> skillMap = getSkill(skill);

        desc = (String) skillMap.get(description);
        // Get current localize version
        localize = game.getLocalize();
        desc = localize.get(desc);

        return desc;
    }

    /**
     * Retrieve skill hit animations to be used by skills.
     */
    private void loadSkillAnimations() {
        physicalHit = files.animPhysicalHit;
        skillHit = files.animSkillHit;
    }

    /**
     * Create skill Attack. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillAttack() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, ATTACK);
        map.put(description, "attackDesc");
        map.put(damage, 1.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit);
        map.put(missChance, defMiss);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 0);
        map.put(hitAnimation, physicalHit);
        map.put(sound, null);
        map.put(button, btnAttack);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Defend. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillDefend() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DEFEND);
        map.put(description, "defendDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 3);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, btnDefend);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Reflect. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillReflect() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, REFLECT);
        map.put(description, "reflectDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 3);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, btnDefend);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Item. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillItem() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, ITEM);
        map.put(description, "itemDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 0);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, btnItem);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Repair. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillRepair() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, REPAIR);
        map.put(description, "repairDesc");
        map.put(damage, -15.0);
        map.put(dmgPurePercent, true);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, -15.0);
        map.put(damageOverTimeTurns, 1);
        map.put(dotPurePercent, true);
        map.put(cooldown, 2);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, btnHeal);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Suction. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillSuction() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SUCTION);
        map.put(description, "suctionDesc");
        map.put(damage, 1.5);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit + 10);
        map.put(missChance, defMiss);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 2);
        map.put(hitAnimation, skillHit);
        map.put(sound, files.sndSuction);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Dust. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillDust() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DUST);
        map.put(description, "dustDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, 0);
        map.put(missChance, defMiss);
        map.put(damageOverTime, 1.0);
        map.put(damageOverTimeTurns, 2);
        map.put(dotPurePercent, false);
        map.put(cooldown, 3);
        map.put(hitAnimation, null);
        map.put(sound, files.sndDustThrow);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Bucket. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillBucket() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, BUCKET);
        map.put(description, "bucketDesc");
        map.put(damage, -40.0);
        map.put(dmgPurePercent, true);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 4);
        map.put(hitAnimation, null);
        map.put(sound, files.sndBucket);
        map.put(button, btnHeal);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Soap. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillSoap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SOAP);
        map.put(description, "soapDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, true);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, -10.0);
        map.put(damageOverTimeTurns, 5);
        map.put(dotPurePercent, true);
        map.put(cooldown, 5);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, btnHeal);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Deja-vu. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillDejaVu() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DEJA_VU);
        map.put(description, "dejaVuDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, true);
        map.put(critChance, 0);
        map.put(missChance, defMiss);
        map.put(damageOverTime, 1.5);
        map.put(damageOverTimeTurns, 2);
        map.put(dotPurePercent, false);
        map.put(cooldown, 3);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Flash. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillFlash() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, FLASH);
        map.put(description, "flashDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, true);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 2);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, false);
        map.put(boostType, BOOST_MISS);
        map.put(boostValue, -10.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Copycat. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillCopycat() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, COPYCAT);
        map.put(description, "copycatDesc");
        map.put(damage, 3.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit + 15);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 4);
        map.put(hitAnimation, skillHit);
        map.put(sound, files.sndCatThrower);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_MISS);
        map.put(boostValue, -25.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill BlackInk. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillBlackInk() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, BLACK_INK);
        map.put(description, "blackInkDesc");
        map.put(damage, 1.5);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit);
        map.put(missChance, -100);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 2);
        map.put(hitAnimation, skillHit);
        map.put(sound, files.sndBlackInk);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Hijack. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillHijack() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, HIJACK);
        map.put(description, "hijackDesc");
        map.put(damage, 1.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit);
        map.put(missChance, defMiss);
        map.put(damageOverTime, -10.0);
        map.put(damageOverTimeTurns, 3);
        map.put(dotPurePercent, true);
        map.put(cooldown, 3);
        map.put(hitAnimation, skillHit);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Pickpocket. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillPickpocket() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, PICKPOCKET);
        map.put(description, "pickpocketDesc");
        map.put(damage, 2.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, 50);
        map.put(missChance, defMiss);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, true);
        map.put(cooldown, 3);
        map.put(hitAnimation, skillHit);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Mischief. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillMischief() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, MISCHIEF);
        map.put(description, "mischiefDesc");
        map.put(damage, 1.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit + 25);
        map.put(missChance, defMiss);
        map.put(damageOverTime, 0.5);
        map.put(damageOverTimeTurns, 2);
        map.put(dotPurePercent, false);
        map.put(cooldown, 3);
        map.put(hitAnimation, skillHit);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Shadowstep. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillShadowstep() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SHADOWSTEP);
        map.put(description, "shadowstepDesc");
        map.put(damage, 2.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit + 30);
        map.put(missChance, defMiss + 25);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, true);
        map.put(cooldown, 2);
        map.put(hitAnimation, skillHit);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Virus. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillVirus() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, VIRUS);
        map.put(description, "virusDesc");
        map.put(damage, 0.5);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit);
        map.put(missChance, defMiss);
        map.put(damageOverTime, 0.5);
        map.put(damageOverTimeTurns, 4);
        map.put(dotPurePercent, false);
        map.put(cooldown, 4);
        map.put(hitAnimation, skillHit);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Popup. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillPopup() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, POPUP);
        map.put(description, "popupDesc");
        map.put(damage, 1.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit);
        map.put(missChance, defMiss - 5);
        map.put(damageOverTime, 0.5);
        map.put(damageOverTimeTurns, 2);
        map.put(dotPurePercent, false);
        map.put(cooldown, 3);
        map.put(hitAnimation, skillHit);
        map.put(sound, files.sndPCPopup);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Trojan. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillTrojan() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, TROJAN);
        map.put(description, "trojanDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, 0);
        map.put(missChance, defMiss);
        map.put(damageOverTime, 0.75);
        map.put(damageOverTimeTurns, 3);
        map.put(dotPurePercent, false);
        map.put(cooldown, 4);
        map.put(hitAnimation, null);
        map.put(sound, files.sndTrojan);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Reboot. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillReboot() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, REBOOT);
        map.put(description, "rebootDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, -50.0);
        map.put(damageOverTimeTurns, 1);
        map.put(dotPurePercent, true);
        map.put(cooldown, 5);
        map.put(hitAnimation, null);
        map.put(sound, files.sndPCReboot);
        map.put(button, btnHeal);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Speeding. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillSpeeding() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SPEEDING);
        map.put(description, "speedingDesc");
        map.put(damage, 3.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit);
        map.put(missChance, 33);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, true);
        map.put(cooldown, 3);
        map.put(hitAnimation, skillHit);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill LowSpeed. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillLowSpeed() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, LOW_SPEED);
        map.put(description, "lowSpeedDesc");
        map.put(damage, 2.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit + 33);
        map.put(missChance, 33);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, true);
        map.put(cooldown, 2);
        map.put(hitAnimation, skillHit);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill The Law. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillTheLaw() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, THE_LAW);
        map.put(description, "theLawDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, true);
        map.put(cooldown, 3);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_MISS);
        map.put(boostValue, 15.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Order. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillOrder() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, ORDER);
        map.put(description, "orderDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, true);
        map.put(cooldown, 3);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_CRIT);
        map.put(boostValue, 15.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Shine. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillShine() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SHINE);
        map.put(description, "shineDesc");
        map.put(damage, 2.5);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit);
        map.put(missChance, 15);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, true);
        map.put(cooldown, 2);
        map.put(hitAnimation, skillHit);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Diamond. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillDiamondRoll() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DIAMOND_ROLL);
        map.put(description, "diamondRollDesc");
        map.put(damage, 1.5);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit);
        map.put(missChance, defMiss);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, true);
        map.put(cooldown, 0);
        map.put(hitAnimation, skillHit);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill CoinFlip. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillCoinFlip() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, COIN_FLIP);
        map.put(description, "coinFlipDesc");
        map.put(damage, 4.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, -100);
        map.put(missChance, 50);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, true);
        map.put(cooldown, 3);
        map.put(hitAnimation, skillHit);
        map.put(sound, files.sndCoinFlip);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Enrichment. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillEnrichment() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, ENRICHMENT);
        map.put(description, "enrichmentDesc");
        map.put(damage, -20.0);
        map.put(dmgPurePercent, true);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, true);
        map.put(cooldown, 2);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, btnHeal);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Rustify. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillRustify() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, RUSTIFY);
        map.put(description, "rustifyDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, true);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, true);
        map.put(cooldown, 5);
        map.put(hitAnimation, null);
        map.put(sound, files.sndRustify);
        map.put(button, btnSkill);
        map.put(boostSelf, false);
        map.put(boostType, BOOST_HEAL);
        map.put(boostValue, -0.5);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Overpower. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillOverpower() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, OVERPOWER);
        map.put(description, "overpowerDesc");
        map.put(damage, 5.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit);
        map.put(missChance, defMiss);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, true);
        map.put(cooldown, 4);
        map.put(hitAnimation, skillHit);
        map.put(sound, files.sndOverpower);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill Mayhem. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillMayhem() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, MAYHEM);
        map.put(description, "mayhemDesc");
        map.put(damage, 0.0);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit);
        map.put(missChance, defMiss);
        map.put(damageOverTime, 2.0);
        map.put(damageOverTimeTurns, 5);
        map.put(dotPurePercent, false);
        map.put(cooldown, 5);
        map.put(hitAnimation, null);
        map.put(sound, files.sndMayhem);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_NONE);
        map.put(boostValue, 0.0);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Create skill VR. Add all the necessary values and add them to map and allSkills array.
     */
    private void skillVR() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, VR);
        map.put(description, "vrDesc");
        map.put(damage, 0.5);
        map.put(dmgPurePercent, false);
        map.put(critChance, defCrit);
        map.put(missChance, defMiss);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 2);
        map.put(hitAnimation, skillHit);
        map.put(sound, null);
        map.put(button, btnSkill);
        map.put(boostSelf, true);
        map.put(boostType, BOOST_DMG);
        map.put(boostValue, 0.25);

        allSkills.add((String) map.get(name));
        mapSkills.put((String) map.get(name), map);
    }

    /**
     * Retrieve all the skills in an array.
     * @return array containing all the skills
     */
    public String[] getAllSkills() {
        String[] converted = allSkills.toArray(new String[0]);
        return converted;
    }
}
