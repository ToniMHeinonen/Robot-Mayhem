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
    public static final String damageOverTime = "damageOverTime";
    public static final String damageOverTimeTurns = "damageOverTimeTurns";
    public static final String cooldown = "cooldown";
    public static final String animation = "animation";
    public static final String hitAnimation = "hitAnimation";
    public static final String speed = "speed";

    public static final String ATTACK = "Attack";
    public static final String DEFEND = "Defend";
    public static final String REPAIR = "Self-repair";
    public static final String SHOCK = "Shock";
    public static final String FIRE = "Fire";
    public static final String SUCTION = "Suction";
    public static final String DUST = "Dust throw";

    private static String[] allSkills = new String[] {ATTACK, DEFEND, REPAIR, SHOCK, FIRE,
    SUCTION, DUST};

    private static HashMap<String,HashMap<String,Object>> mapSkills;

    private static Texture t_attack, t_attack_hit, t_defend;
    private static Animating anim = new Animating();
    private static Animation<TextureRegion> a_attack, a_attack_hit, a_defend;

    /*
    Create skills when the game starts.
     */
    public static void createSkills() {
        mapSkills = new HashMap<String, HashMap<String,Object>>();
        createAttackAndDefend();
        skillAttack();
        skillDefend();
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
        map.put(damageOverTime, 0.0); // Not needed
        map.put(damageOverTimeTurns, 0); // Not needed
        map.put(cooldown, 0);
        map.put(animation, a_attack);
        map.put(hitAnimation, a_attack_hit);
        map.put(speed + animation, 30);
        map.put(speed + hitAnimation, 15);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillDefend() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DEFEND);
        map.put(description,
                "Shield protects you completely from damage you’d receive in the next turn.");
        map.put(damage, 0.0); // Not needed
        map.put(damageOverTime, 0.0); // Not needed
        map.put(damageOverTimeTurns, 0); // Not needed
        map.put(cooldown, 3);
        map.put(animation, a_defend);
        map.put(hitAnimation, null);
        map.put(speed + animation, 5);
        map.put(speed + hitAnimation, 0);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillRepair() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, REPAIR);
        map.put(description, "Self-repair heals you slightly over 2 turns.");
        map.put(damage, 0.0);
        map.put(damageOverTime, -15.0);
        map.put(damageOverTimeTurns, 2);
        map.put(cooldown, 2);
        map.put(animation, a_defend);
        map.put(hitAnimation, null);
        map.put(speed + animation, 30);
        map.put(speed + hitAnimation, 0);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillShock() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SHOCK);
        map.put(damage, 4.0);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(cooldown, 3);
        map.put(animation, a_attack);
        map.put(hitAnimation, a_attack_hit);
        map.put(speed + animation, 20);
        map.put(speed + hitAnimation, 10);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillFire() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, FIRE);
        map.put(damage, 0.0);
        map.put(damageOverTime, 20.0);
        map.put(damageOverTimeTurns, 3);
        map.put(cooldown, 2);
        map.put(animation, a_defend);
        map.put(hitAnimation, null);
        map.put(speed + animation, 15);
        map.put(speed + hitAnimation, 0);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillSuction() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SUCTION);
        map.put(description, "Suctions suckforce causes medium damage to the recipient. " +
                "(has an 10% increased chance to be a critical hit)");
        map.put(damage, 1.5);
        map.put(damageOverTime, 0.0);
        map.put(damageOverTimeTurns, 0);
        map.put(cooldown, 2);
        map.put(animation, null);
        map.put(hitAnimation, a_attack_hit);
        map.put(speed + animation, 0);
        map.put(speed + hitAnimation, 30);

        mapSkills.put((String) map.get(name), map);
    }

    private static void skillDust() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DUST);
        map.put(description, "Dust throw releases a big amount of dust inside the robot’s fans" +
                " and causes medium damage to the recipient over two turns.");
        map.put(damage, 0.0);
        map.put(damageOverTime, 30.0);
        map.put(damageOverTimeTurns, 2);
        map.put(cooldown, 3);
        map.put(animation, a_attack);
        map.put(hitAnimation, a_attack_hit);
        map.put(speed + animation, 15);
        map.put(speed + hitAnimation, 10);

        mapSkills.put((String) map.get(name), map);
    }

    private static void createAttackAndDefend() {
        t_attack = new Texture("texture/player/playerAttack.png");
        a_attack = anim.createAnimation(t_attack, 3, 1);
        t_attack_hit = new Texture("texture/player/playerAttackHit.png");
        a_attack_hit = anim.createAnimation(t_attack_hit, 3, 1);
        t_defend = new Texture("texture/player/playerDefend.png");
        a_defend = anim.createAnimation(t_defend, 3, 1);
    }

    public static void dispose() {
        t_attack.dispose();
        t_attack_hit.dispose();
        t_defend.dispose();
    }
}
