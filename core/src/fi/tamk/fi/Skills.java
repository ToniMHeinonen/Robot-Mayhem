package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.HashMap;

public class Skills {

    private Files files;
    private I18NBundle localize;

    public final String name = "name";
    public final String description = "description";
    public final String damage = "damage";
    public final String critChance = "critChance";
    public final String missChance = "missChance";
    public final String damageOverTime = "damageOverTime";
    public final String damageOverTimeTurns = "damageOverTimeTurns";
    public final String dotPurePercent = "dotPurePercent";
    public final String cooldown = "cooldown";
    public final String hitAnimation = "hitAnimation";
    public final String sound = "sound";
    public final String button = "button";

    // These values are used by actionbuttons (every button will probably not have it's own button,
    // so some of these values are going to be removed)
    private final String buttonAttack = "ATTACK";
    private final String buttonDefend = "DEFEND";
    private final String buttonItem = "ITEM";
    private final String buttonRepair = "REPAIR";
    private final String buttonShock = "SHOCK";
    private final String buttonFire = "FIRE";
    private final String buttonSuction = "SUCTION";
    private final String buttonDust = "DUST";

    public final String ATTACK, DEFEND, ITEM, REPAIR, SHOCK, FIRE, SUCTION, DUST;
    private final String[] allSkills;

    private final int defCrit = 10; // Default crit chance percent

    private Animation<TextureRegion> physicalHit;

    private HashMap<String,HashMap<String,Object>> mapSkills;

    Skills(MainGame game) {
        localize = game.getLocalize();
        files = game.getFiles();

        // Get correct texts for skills, depending on the language
        ATTACK = localize.get("ATTACK");
        DEFEND = localize.get("DEFEND");
        ITEM = localize.get("ITEM");
        REPAIR = localize.get("REPAIR");
        SHOCK = localize.get("SHOCK");
        FIRE = localize.get("FIRE");
        SUCTION = localize.get("SUCTION");
        DUST = localize.get("DUST");

        allSkills = new String[] {ATTACK, DEFEND, ITEM, REPAIR, SHOCK, FIRE, SUCTION, DUST};

        mapSkills = new HashMap<String, HashMap<String,Object>>();
        loadSkillAnimations();
        skillAttack();
        skillDefend();
        skillItem();
        skillRepair();
        skillShock();
        skillFire();
        skillSuction();
        skillDust();
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
    - button = The name which is used for retrieving correct button from skin
     */

    /*
    Retrieve correct skill map by using string value.
     */
    public HashMap<String, Object> getSkill(String skill) {
        HashMap<String, Object> chosenSkill;

        chosenSkill = mapSkills.get(skill);

        return chosenSkill;
    }

    public String retrieveSkillDescription(String skill) {
        String desc = "";
        HashMap<String, Object> skillMap = getSkill(skill);

        desc = (String) skillMap.get(description);

        return desc;
    }

    private void loadSkillAnimations() {
        physicalHit = files.animPhysicalHit;
    }

    private void skillAttack() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, ATTACK);
        map.put(description, localize.get("attackDesc"));
        map.put(damage, 1.0);
        map.put(critChance, defCrit);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 0);
        map.put(hitAnimation, physicalHit);
        map.put(sound, null);
        map.put(button, buttonAttack);

        mapSkills.put((String) map.get(name), map);
    }

    private void skillDefend() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DEFEND);
        map.put(description, localize.get("defendDesc"));
        map.put(damage, 0.0);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 3);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, buttonDefend);

        mapSkills.put((String) map.get(name), map);
    }

    private void skillItem() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, ITEM);
        map.put(description, localize.get("itemDesc"));
        map.put(damage, 0.0);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 0);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, buttonItem);

        mapSkills.put((String) map.get(name), map);
    }

    private void skillRepair() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, REPAIR);
        map.put(description, localize.get("repairDesc"));
        map.put(damage, 0.0);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, -15.0);
        map.put(damageOverTimeTurns, 2);
        map.put(dotPurePercent, true);
        map.put(cooldown, 2);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, buttonRepair);

        mapSkills.put((String) map.get(name), map);
    }

    private void skillShock() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SHOCK);
        map.put(description, localize.get("shockDesc"));
        map.put(damage, 3.0);
        map.put(critChance, defCrit);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 3);
        map.put(hitAnimation, physicalHit);
        map.put(sound, null);
        map.put(button, buttonShock);

        mapSkills.put((String) map.get(name), map);
    }

    private void skillFire() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, FIRE);
        map.put(description, localize.get("fireDesc"));
        map.put(damage, 0.0);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 20.0);
        map.put(damageOverTimeTurns, 3);
        map.put(dotPurePercent, true);
        map.put(cooldown, 2);
        map.put(hitAnimation, null);
        map.put(sound, null);
        map.put(button, buttonFire);

        mapSkills.put((String) map.get(name), map);
    }

    private void skillSuction() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SUCTION);
        map.put(description, localize.get("suctionDesc"));
        map.put(damage, 1.5);
        map.put(critChance, defCrit + 10);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 2);
        map.put(hitAnimation, physicalHit);
        map.put(sound, files.sndSuction);
        map.put(button, buttonSuction);

        mapSkills.put((String) map.get(name), map);
    }

    private void skillDust() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DUST);
        map.put(description, localize.get("dustDesc"));
        map.put(damage, 0.0);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 1.0);
        map.put(damageOverTimeTurns, 2);
        map.put(dotPurePercent, false);
        map.put(cooldown, 3);
        map.put(hitAnimation, null);
        map.put(sound, files.sndDustThrow);
        map.put(button, buttonDust);

        mapSkills.put((String) map.get(name), map);
    }

    public String[] getAllSkills() {
        return allSkills;
    }
}
