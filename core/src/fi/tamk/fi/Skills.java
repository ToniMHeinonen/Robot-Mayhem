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

    private static HashMap<String,HashMap<String,Object>> mapSkills;
    private static HashMap<String,Object> mapAttack;
    private static HashMap<String,Object> mapDefend;

    private static Texture t_attack, t_defend;
    private static Animating anim = new Animating();
    private static Animation<TextureRegion> a_attack, a_defend;

    /*
    Create skills when the game starts.
     */
    public static void createSkills() {
        createTexturesAndAnimations();
        skillAttack();
        skillDefend();

        mapSkills = new HashMap<String, HashMap<String,Object>>();
        mapSkills.put((String) mapAttack.get(name), mapAttack);
        mapSkills.put((String) mapDefend.get(name), mapDefend);
    }

    /*
    Retrieve correct skill map by using string value.
     */
    public static HashMap<String, Object> getSkill(String skill) {
        HashMap<String, Object> chosenSkill;

        chosenSkill = mapSkills.get(skill);

        return chosenSkill;
    }

    private static void skillAttack() {
        mapAttack = new HashMap<String, Object>();
        mapAttack.put(name, "attack");
        mapAttack.put(damage, 2.5);
        mapAttack.put(cooldown, 0);
        mapAttack.put(animation, a_attack);
    }

    private static void skillDefend() {
        mapDefend = new HashMap<String, Object>();
        mapDefend.put(name, "defend");
        mapDefend.put(damage, 0);
        mapDefend.put(cooldown, 3);
        mapDefend.put(animation, a_defend);
    }

    private static void createTexturesAndAnimations() {
        t_attack = new Texture("texture/player/playerAttack.png");
        a_attack = anim.createAnimation(t_attack, 3, 1);
        t_defend = new Texture("texture/player/playerDefend.png");
        a_defend = anim.createAnimation(t_defend, 3, 1);

        disposeTextures();
    }

    private static void disposeTextures() {
        t_attack.dispose();
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
}
