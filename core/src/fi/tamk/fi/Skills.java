package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class Skills {
    /*
    Use these, since if you need to change the name to something else, you then only need
    to change it in here.
     */
    private static String name = "name";
    private static String damage = "damage";
    private static String cooldown = "cooldown";
    private static String animation = "animation";
    private static String hitAnimation = "hitAnimation";
    private static String speed = "speed";

    private static HashMap<String,HashMap<String,Object>> mapSkills;
    private static HashMap<String,Object> mapAttack, mapDefend, mapTest, mapTest2;

    private static Texture t_attack, t_attack_hit, t_defend;
    private static Animating anim = new Animating();
    private static Animation<TextureRegion> a_attack, a_attack_hit, a_defend;

    /*
    Create skills when the game starts.
     */
    public static void createSkills() {
        createTexturesAndAnimations();
        mapAttack = skillAttack();
        mapDefend = skillDefend();
        mapTest = skillTest();
        mapTest2 = skillTest2();

        mapSkills = new HashMap<String, HashMap<String,Object>>();
        mapSkills.put((String) mapAttack.get(name), mapAttack);
        mapSkills.put((String) mapDefend.get(name), mapDefend);
        mapSkills.put((String) mapTest.get(name), mapTest);
        mapSkills.put((String) mapTest2.get(name), mapTest2);
    }

    /*
    Retrieve correct skill map by using string value.
     */
    public static HashMap<String, Object> getSkill(String skill) {
        HashMap<String, Object> chosenSkill;

        chosenSkill = mapSkills.get(skill);

        return chosenSkill;
    }

    private static HashMap<String, Object> skillAttack() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, "Attack");
        map.put(damage, 2.5);
        map.put(cooldown, 0);
        map.put(animation, a_attack);
        map.put(hitAnimation, a_attack_hit);
        map.put(speed + animation, 30);
        map.put(speed + hitAnimation, 15);
        return map;
    }

    private static HashMap<String, Object> skillDefend() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, "Defend");
        map.put(damage, 0);
        map.put(cooldown, 3);
        map.put(animation, a_defend);
        map.put(hitAnimation, null);
        map.put(speed + animation, 5);
        map.put(speed + hitAnimation, 0);
        return map;
    }

    private static HashMap<String, Object> skillTest() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, "Test");
        map.put(damage, 4.0);
        map.put(cooldown, 3);
        map.put(animation, a_attack);
        map.put(hitAnimation, a_attack_hit);
        map.put(speed + animation, 20);
        map.put(speed + hitAnimation, 10);
        return map;
    }

    private static HashMap<String, Object> skillTest2() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, "Test2");
        map.put(damage, 1.0);
        map.put(cooldown, 3);
        map.put(animation, a_attack);
        map.put(hitAnimation, a_attack_hit);
        map.put(speed + animation, 5);
        map.put(speed + hitAnimation, 30);
        return map;
    }

    private static void createTexturesAndAnimations() {
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

    public static String getName() {
        return name;
    }

    public static String getDamage() {
        return damage;
    }

    public static String getCooldown() {
        return cooldown;
    }

    public static String getAnimation() {
        return animation;
    }

    public static String getHitAnimation() {
        return hitAnimation;
    }

    public static String getSpeed() {
        return speed;
    }
}
