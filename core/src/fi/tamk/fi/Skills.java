package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.HashMap;

public class Skills {

    public static final String name = "name";
    public static final String description = "description";
    public static final String damage = "damage";
    public static final String critChance = "critChance";
    public static final String missChance = "missChance";
    public static final String damageOverTime = "damageOverTime";
    public static final String damageOverTimeTurns = "damageOverTimeTurns";
    public static final String dotPurePercent = "dotPurePercent";
    public static final String cooldown = "cooldown";
    public static final String hitAnimation = "hitAnimation";
    public static final String hitAnimationSpd = "hitAnimationSpd";

    public static final String ATTACK = "Attack";
    public static final String DEFEND = "Defend";
    public static final String ITEM = "Item";
    public static final String REPAIR = "Self-repair";
    public static final String SHOCK = "Shock";
    public static final String FIRE = "Fire";
    public static final String SUCTION = "Suction";
    public static final String DUST = "Dust throw";

    private static final int defCrit = 10; // Default crit chance percent

    private static String[] allSkills = new String[] {ATTACK, DEFEND, REPAIR, SHOCK, FIRE,
    SUCTION, DUST};

    private static HashMap<String,HashMap<String,Object>> mapSkills;

    private static Texture t_Physical; // Remember to dispose these
    private static Animating anim = new Animating();
    private static Animation<TextureRegion> a_Physical;

    /* NOTE!
    Every time you add new skill, remember to:
    1. Make a String for it
    2. Add it to the allSkills array
    3. Make a new method for it
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
     */

    /*
    Create skills when the game starts.
     */
    public static void createSkills() {
        mapSkills = new HashMap<String, HashMap<String,Object>>();
        createAttackAndDefend();
        skillAttack();
        skillDefend();
        skillItem();
        skillRepair();
        skillShock();
        skillFire();
        skillSuction();
        skillDust();
    }

    /*
    Retrieve correct skill map by using string value.
     */
    public static HashMap<String, Object> getSkill(String skill) {
        HashMap<String, Object> chosenSkill;

        chosenSkill = mapSkills.get(skill);

        return chosenSkill;
    }

    public static String retrieveSkillDescription(String skill) {
        String desc = "";
        HashMap<String, Object> skillMap = getSkill(skill);

        desc = (String) skillMap.get(description);

        return desc;
    }

    private static void skillAttack() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, ATTACK);
        map.put(description, "Light attack that’s usable every turn.");
        map.put(damage, 1.0);
        map.put(critChance, defCrit);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 0);
        map.put(hitAnimation, a_Physical);
        map.put(hitAnimationSpd, 8);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillDefend() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DEFEND);
        map.put(description,
                "Shield protects you completely from damage you’d receive in the next turn.");
        map.put(damage, 0.0);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 3);
        map.put(hitAnimation, null);
        map.put(hitAnimationSpd, 0);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillItem() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, ITEM);
        map.put(description,
                "Buy and use items.");
        map.put(damage, 0.0);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 0);
        map.put(hitAnimation, null);
        map.put(hitAnimationSpd, 0);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillRepair() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, REPAIR);
        map.put(description, "Self-repair heals you slightly over 2 turns.");
        map.put(damage, -30.0);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, -15.0);
        map.put(damageOverTimeTurns, 2);
        map.put(dotPurePercent, true);
        map.put(cooldown, 2);
        map.put(hitAnimation, null);
        map.put(hitAnimationSpd, 0);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillShock() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SHOCK);
        map.put(description, "Shock the opponent with amazing damage!");
        map.put(damage, 3.0);
        map.put(critChance, defCrit);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 3);
        map.put(hitAnimation, a_Physical);
        map.put(hitAnimationSpd, 8);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillFire() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, FIRE);
        map.put(description, "Burn your enemy for 3 turns.");
        map.put(damage, 0.0);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 20.0);
        map.put(damageOverTimeTurns, 3);
        map.put(dotPurePercent, true);
        map.put(cooldown, 2);
        map.put(hitAnimation, null);
        map.put(hitAnimationSpd, 0);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillSuction() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SUCTION);
        map.put(description, "Suctions suckforce causes medium damage to the recipient. " +
                "(has an 10% increased chance to be a critical hit)");
        map.put(damage, 1.5);
        map.put(critChance, defCrit + 10);
        map.put(missChance, 0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(dotPurePercent, false);
        map.put(cooldown, 2);
        map.put(hitAnimation, a_Physical);
        map.put(hitAnimationSpd, 8);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillDust() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DUST);
        map.put(description, "Dust throw releases a big amount of dust inside the robot’s fans" +
                " and causes medium damage to the recipient over two turns.");
        map.put(damage, 0.0);
        map.put(critChance, 0);
        map.put(missChance, 0);
        map.put(damageOverTime, 1.0);
        map.put(damageOverTimeTurns, 2);
        map.put(dotPurePercent, false);
        map.put(cooldown, 3);
        map.put(hitAnimation, null);
        map.put(hitAnimationSpd, 8);

        mapSkills.put((String) map.get(name), map);
    }

    private static void createAttackAndDefend() {
        t_Physical = new Texture("texture/skills/physicalHit.png");
        a_Physical = anim.createAnimation(t_Physical, 4, 1);
    }

    public static void dispose() {
        t_Physical.dispose();
    }
}
