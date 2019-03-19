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

    private static Texture playerAttack, playerDefend;
    private static Animating anim = new Animating();
    private static Animation<TextureRegion> attack, defend;

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
        mapAttack.put(animation, attack);
    }

    private static void skillDefend() {
        mapDefend = new HashMap<String, Object>();
        mapDefend.put(name, "defend");
        mapDefend.put(damage, 2.5);
        mapDefend.put(cooldown, 3);
        mapDefend.put(animation, defend);
    }

    private static void createTexturesAndAnimations() {
        playerAttack = new Texture("texture/player/playerAttack.png");
        attack = anim.createAnimation(playerAttack, 3, 1);
        playerDefend = new Texture("texture/player/playerDefend.png");
        defend = anim.createAnimation(playerDefend, 3, 1);
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
