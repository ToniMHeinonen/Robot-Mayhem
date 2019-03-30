package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class Item {

    private static String name = "name";
    private static String damage = "damage";
    private static String cooldown = "cooldown";
    private static String animation = "animation";
    private static String hitAnimation = "hitAnimation";
    private static String speed = "speed";

    private static HashMap<String,HashMap<String,Object>> mapItems;
    private static HashMap<String,Object> mapDamage, mapHeal;

    private static Texture t_Damage, t_Damage_hit, t_Heal;
    private static Animating anim = new Animating();
    private static Animation<TextureRegion> a_Damage, a_Damage_hit, a_Heal;

    // Booleans that can be used later.
    boolean IsItAHallItem;
    boolean IsItASkill;
    boolean Temporary;

    /*
    Create items when the game starts.
     */
    public static void createItems() {

        createDamageAndHeal();
        mapDamage = itemDamage();
        mapHeal = itemHeal();

        mapItems = new HashMap<String, HashMap<String,Object>>();
        mapItems.put((String) mapDamage.get(name), mapDamage);
        mapItems.put((String) mapHeal.get(name), mapHeal);
    }

    public static HashMap<String, Object> getSkill(String skill) {
        HashMap<String, Object> chosenSkill;

        chosenSkill = mapItems.get(skill);

        return chosenSkill;
    }

    protected static HashMap<String, Object> itemDamage() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, "Attack");
        map.put(damage, 5);
        map.put(cooldown, 0);
        map.put(animation, a_Damage);
        map.put(hitAnimation, a_Damage_hit);
        map.put(speed + animation, 30);
        map.put(speed + hitAnimation, 15);
        return map;
    }

    protected static HashMap<String, Object> itemHeal() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, "heal");
        map.put(damage, -5);
        map.put(cooldown, 3);
        map.put(animation, a_Heal);
        map.put(hitAnimation, null);
        map.put(speed + animation, 5);
        map.put(speed + hitAnimation, 0);
        return map;
    }

    private static void createDamageAndHeal() {

        t_Damage = new Texture("texture/player/playerAttack.png");
        a_Damage = anim.createAnimation(t_Damage, 3, 1);
        t_Damage_hit = new Texture("texture/player/playerAttackHit.png");
        a_Damage_hit = anim.createAnimation(t_Damage_hit, 3, 1);

        t_Heal = new Texture("texture/player/playerDefend.png");
        a_Heal = anim.createAnimation(t_Heal, 3, 1);
    }

    public static void dispose() {
        t_Damage.dispose();
        t_Damage_hit.dispose();
        t_Heal.dispose();
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
