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
    private static String skillHit = "skillHit";
    private static String dialogStart = "dialogstart";
    private static String dialogEnd = "dialogend";
    private static String speed = "speed";

    private static HashMap<String, HashMap<String,Object>> mapBosses;
    private static HashMap<String,Object> mapRoombot, mapTest;

    private static int s_idle, s_hack, s_skill1, s_skill2, s_skill3,
                        s_skill1_hit, s_skill2_hit, s_skill3_hit; // Animation speed
    private static Texture t_idle, t_hack, t_skill1, t_skill2, t_skill3, t_skill1_hit,
                            t_skill2_hit, t_skill3_hit;
    private static Animation<TextureRegion> a_idle, a_hack, a_skill1, a_skill2, a_skill3,
                                            a_skill1_hit, a_skill2_hit, a_skill3_hit;
    private static Animating anim = new Animating();

    private static String curName, curDialogStart, curDialogEnd;
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
        curDialogStart = "Vroom Vroom!";
        curDialogEnd = "Vroom poks!";

        t_idle = new Texture("texture/enemy/enemyIdle.png");
        a_idle = anim.createAnimation(t_idle, 3, 1);
        s_idle = 30;
        t_hack = new Texture("texture/enemy/enemyHack.png");
        a_hack = anim.createAnimation(t_hack, 3, 1);
        s_hack = 30;
        t_skill1 = new Texture("texture/enemy/enemyAttack1.png");
        a_skill1 = anim.createAnimation(t_skill1, 3, 1);
        s_skill1 = 30;
        t_skill2 = new Texture("texture/enemy/enemyAttack2.png");
        a_skill2 = anim.createAnimation(t_skill2, 3, 1);
        s_skill2 = 30;
        t_skill3 = new Texture("texture/enemy/enemyAttack3.png");
        a_skill3 = anim.createAnimation(t_skill3, 3, 1);
        s_skill3 = 30;
        t_skill1_hit = new Texture("texture/player/playerAttackHit.png");
        a_skill1_hit = anim.createAnimation(t_skill1_hit, 3, 1);
        s_skill1_hit = 30;
        t_skill2_hit = new Texture("texture/player/playerAttackHit.png");
        a_skill2_hit = anim.createAnimation(t_skill2_hit, 3, 1);
        s_skill2_hit = 30;
        t_skill3_hit = new Texture("texture/player/playerAttackHit.png");
        a_skill3_hit = anim.createAnimation(t_skill3_hit, 3, 1);
        s_skill3_hit = 30;

        mapRoombot = new HashMap<String, Object>();
        mapRoombot = addToMap(mapRoombot);
    }

    private static void bossTest() {
        curName = "test";
        curDmg1 = 1;
        curDmg2 = 1.5;
        curDmg3 = 2;
        curDialogStart = "Test start!";
        curDialogEnd = "Test end!";

        t_idle = new Texture("texture/enemy/enemyIdle.png");
        a_idle = anim.createAnimation(t_idle, 3, 1);
        s_idle = 30;
        t_hack = new Texture("texture/enemy/enemyHack.png");
        a_hack = anim.createAnimation(t_hack, 3, 1);
        s_hack = 30;
        t_skill1 = new Texture("texture/enemy/enemyAttack1.png");
        a_skill1 = anim.createAnimation(t_skill1, 3, 1);
        s_skill1 = 30;
        t_skill2 = new Texture("texture/enemy/enemyAttack2.png");
        a_skill2 = anim.createAnimation(t_skill2, 3, 1);
        s_skill2 = 30;
        t_skill3 = new Texture("texture/enemy/enemyAttack3.png");
        a_skill3 = anim.createAnimation(t_skill3, 3, 1);
        s_skill3 = 30;
        t_skill1_hit = new Texture("texture/player/playerAttackHit.png");
        a_skill1_hit = anim.createAnimation(t_skill1_hit, 3, 1);
        s_skill1_hit = 30;
        t_skill2_hit = new Texture("texture/player/playerAttackHit.png");
        a_skill2_hit = anim.createAnimation(t_skill2_hit, 3, 1);
        s_skill2_hit = 30;
        t_skill3_hit = new Texture("texture/player/playerAttackHit.png");
        a_skill3_hit = anim.createAnimation(t_skill3_hit, 3, 1);
        s_skill3_hit = 30;

        mapTest = new HashMap<String, Object>();
        mapTest = addToMap(mapTest);
    }

    private static HashMap<String, Object> addToMap(HashMap<String, Object> map) {
        map.put(name, curName);
        map.put(idle, a_idle);
        map.put(hack, a_hack);
        map.put(skill + "1", a_skill1);
        map.put(skill + "2", a_skill2);
        map.put(skill + "3", a_skill3);
        map.put(skillHit + "1", a_skill1_hit);
        map.put(skillHit + "2", a_skill2_hit);
        map.put(skillHit + "3", a_skill3_hit);
        map.put(damage + "1", curDmg1);
        map.put(damage + "2", curDmg2);
        map.put(damage + "3", curDmg3);
        map.put(speed + idle, s_idle);
        map.put(speed + hack, s_hack);
        map.put(speed + skill + "1", s_skill1);
        map.put(speed + skill + "2", s_skill2);
        map.put(speed + skill + "3", s_skill3);
        map.put(speed + skillHit + "1", s_skill1_hit);
        map.put(speed + skillHit + "2", s_skill2_hit);
        map.put(speed + skillHit + "3", s_skill3_hit);
        map.put(dialogStart, curDialogStart);
        map.put(dialogEnd, curDialogEnd);

        return map;
    }

    public static void dispose() {
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

    public static String getDialogStart() {
        return dialogStart;
    }

    public static String getDialogEnd() {
        return dialogEnd;
    }

    public static String getSpeed() {
        return speed;
    }

    public static String getSkillHit() {
        return skillHit;
    }
}
