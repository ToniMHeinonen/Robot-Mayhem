package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;

public class Bosses {
    /*
    Use these, since if you need to change the name to something else, you then only need
    to change it in here.
     */
    private static String name = "name";
    private static String damage = "damage";
    private static String idle = "idle";
    private static String hack = "hack";
    private static String skill = "skill";

    private static HashMap<String, HashMap<String,Object>> mapBosses;
    private static HashMap<String,Object> mapRoombot, mapTest;

    private static Texture t_idle, t_hack, t_skill1, t_skill2, t_skill3;
    private static Animation<TextureRegion> a_idle, a_hack, a_skill1, a_skill2, a_skill3;
    private static Animating anim = new Animating();

    private static String curName;
    private static double curDmg1, curDmg2, curDmg3;

    /*
    Create bosses when the game starts.
     */
    public static void createBosses() {
        bossRoombot();
        bossTest();

        mapBosses = new HashMap<String, HashMap<String,Object>>();
        mapBosses.put((String) mapRoombot.get(name), mapRoombot);
        mapBosses.put((String) mapTest.get(name), mapTest);
    }

    /*
    Retrieve correct boss map by using string value.
     */
    public static HashMap<String, Object> getBoss(String boss) {
        HashMap<String, Object> chosenBoss;

        chosenBoss = mapBosses.get(boss);

        return chosenBoss;
    }

    private static void bossRoombot() {
        curName = "roombot";
        curDmg1 = 1;
        curDmg2 = 1.5;
        curDmg3 = 2;

        t_idle = new Texture("texture/enemy/enemyIdle.png");
        a_idle = anim.createAnimation(t_idle, 3, 1);
        t_hack = new Texture("texture/enemy/enemyHack.png");
        a_hack = anim.createAnimation(t_hack, 3, 1);
        t_skill1 = new Texture("texture/enemy/enemyAttack1.png");
        a_skill1 = anim.createAnimation(t_skill1, 3, 1);
        t_skill2 = new Texture("texture/enemy/enemyAttack2.png");
        a_skill2 = anim.createAnimation(t_skill2, 3, 1);
        t_skill3 = new Texture("texture/enemy/enemyAttack3.png");
        a_skill3 = anim.createAnimation(t_skill3, 3, 1);

        mapRoombot = new HashMap<String, Object>();
        mapRoombot = addToMap(mapRoombot);

        disposeTextures();
    }

    private static void bossTest() {
        curName = "test";
        curDmg1 = 1;
        curDmg2 = 1.5;
        curDmg3 = 2;

        t_idle = new Texture("texture/enemy/enemyIdle.png");
        a_idle = anim.createAnimation(t_idle, 3, 1);
        t_hack = new Texture("texture/enemy/enemyHack.png");
        a_hack = anim.createAnimation(t_hack, 3, 1);
        t_skill1 = new Texture("texture/enemy/enemyAttack1.png");
        a_skill1 = anim.createAnimation(t_skill1, 3, 1);
        t_skill2 = new Texture("texture/enemy/enemyAttack2.png");
        a_skill2 = anim.createAnimation(t_skill2, 3, 1);
        t_skill3 = new Texture("texture/enemy/enemyAttack3.png");
        a_skill3 = anim.createAnimation(t_skill3, 3, 1);

        mapTest = new HashMap<String, Object>();
        mapTest = addToMap(mapTest);

        disposeTextures();
    }

    private static HashMap<String, Object> addToMap(HashMap<String, Object> map) {
        map.put(name, curName);
        map.put(idle, a_idle);
        map.put(hack, a_hack);
        map.put(skill + "1", a_skill1);
        map.put(damage + "1", curDmg1);
        map.put(skill + "2", a_skill2);
        map.put(damage + "2", curDmg2);
        map.put(skill + "3", a_skill3);
        map.put(damage + "3", curDmg3);

        return map;
    }

    private static void disposeTextures() {
        t_idle.dispose();
        t_hack.dispose();
        t_skill1.dispose();
        t_skill2.dispose();
        t_skill3.dispose();
    }

    public static String getName() {
        return name;
    }

    public static String getDamage() {
        return damage;
    }

    public static String getIdle() {
        return idle;
    }

    public static String getSkill() {
        return skill;
    }

    public static String getHack() {
        return hack;
    }
}
