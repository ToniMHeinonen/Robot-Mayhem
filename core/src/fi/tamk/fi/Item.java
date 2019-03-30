package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class Item {

    private static String name = "name";
    private static String damage = "damage";
    /* Missing:
    private static String usedInHall = "usedInHall";
    private static String isSkill = "isSkill";
     */

    //(temporary will probably not be needed at all, so let's not add it)

    /* Not needed
    private static String cooldown = "cooldown";
    private static String animation = "animation";
    private static String hitAnimation = "hitAnimation";
    private static String speed = "speed";*/

    private static HashMap<String,HashMap<String,Object>> mapItems;
    private static HashMap<String,Object> mapDamage, mapHeal;

    /* Not needed
    private static Texture t_Damage, t_Damage_hit, t_Heal;
    private static Animating anim = new Animating();
    private static Animation<TextureRegion> a_Damage, a_Damage_hit, a_Heal;*/

    /* Not needed
    Booleans that can be used later.
    boolean IsItAHallItem;
    boolean IsItASkill;
    boolean Temporary;*/

    /*
    Create items when the game starts.
     */
    public static void createItems() {

        // createDamageAndHeal(); Not needed
        mapDamage = itemDamage();
        mapHeal = itemHeal();

        mapItems = new HashMap<String, HashMap<String,Object>>();
        mapItems.put((String) mapDamage.get(name), mapDamage);
        mapItems.put((String) mapHeal.get(name), mapHeal);
    }

    public static HashMap<String, Object> getItem(String item) {
        HashMap<String, Object> chosenItem;

        chosenItem = mapItems.get(item);

        return chosenItem;
    }

    private static HashMap<String, Object> itemDamage() { // Not protected, use private
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, "Attack");
        map.put(damage, 5);
        /* Missing:
        map.put(usedInHall, false);
        map.put(isSkill, false);
         */

        /* Not needed
        map.put(cooldown, 0);
        map.put(animation, a_Damage);
        map.put(hitAnimation, a_Damage_hit);
        map.put(speed + animation, 30);
        map.put(speed + hitAnimation, 15);*/
        return map;
    }

    private static HashMap<String, Object> itemHeal() { // Not protected, use private
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, "heal");
        map.put(damage, -5);
        /* Missing:
        map.put(usedInHall, false);
        map.put(isSkill, false);
         */

        /* Not needed
        map.put(cooldown, 3);
        map.put(animation, a_Heal);
        map.put(hitAnimation, null);
        map.put(speed + animation, 5);
        map.put(speed + hitAnimation, 0);*/
        return map;
    }

    /* Not needed
    private static void createDamageAndHeal() {

        t_Damage = new Texture("texture/player/playerAttack.png");
        a_Damage = anim.createAnimation(t_Damage, 3, 1);
        t_Damage_hit = new Texture("texture/player/playerAttackHit.png");
        a_Damage_hit = anim.createAnimation(t_Damage_hit, 3, 1);

        t_Heal = new Texture("texture/player/playerDefend.png");
        a_Heal = anim.createAnimation(t_Heal, 3, 1);
    }*/

    /* Not needed
    public static void dispose() {
        t_Damage.dispose();
        t_Damage_hit.dispose();
        t_Heal.dispose();
    }*/

    public static String getName() {
        return name;
    }

    public static String getDamage() {
        return damage;
    }



    /* Missing:
    public static String getIsSkill() {
        return isSkill;
    }

    public static String getUsedInHall() {
        return usedInHall;
    }
     */

    /* Not needed
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
    }*/
}
