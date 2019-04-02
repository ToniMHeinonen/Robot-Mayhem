package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import java.util.HashMap;

public class Bosses {
    /*
    Use these, since if you need to change the name to something else, you then only need
    to change it in here.
     */
    public static final String name = "name";
    public static final String idle = "idle";
    public static final String hack = "hack";
    public static final String skill = "skill";
    public static final String speed = "speed";
    public static final String skillName = "skillName";
    public static final String dialogStart = "dialogstart";
    public static final String dialogEnd = "dialogend";

    private static HashMap<String, HashMap<String,Object>> mapBosses;

    private static int s_idle, s_hack, s_skill; // Animation speed
    private static Texture t_idle, t_hack, t_skill;
    private static Animation<TextureRegion> a_idle, a_hack, a_skill;
    private static Animating anim = new Animating();

    private static String curName, curSkillName0, curSkillName1, curSkillName2,
                            curDialogStart, curDialogEnd;

    public static final String ROOMBOT = "Roombot";
    private static String[] allBosses = new String[] {ROOMBOT};

    /*
    Create bosses when the game starts.
     */
    public static void createBosses() {
        mapBosses = new HashMap<String, HashMap<String,Object>>();
        bossRoombot();
    }

    /*
    Retrieve correct boss map by using string value.
     */
    public static HashMap<String, Object> getBoss(String boss) {
        HashMap<String, Object> chosenBoss;

        chosenBoss = mapBosses.get(boss);

        return chosenBoss;
    }

    public static String selectRandomBoss() {
        String selected = "";
        int random = MathUtils.random(0, allBosses.length - 1);
        selected = allBosses[random];

        return selected;
    }

    private static void bossRoombot() {
        curName = ROOMBOT;
        curSkillName0 = Skills.ATTACK;
        curSkillName1 = Skills.SUCTION;
        curSkillName2 = Skills.DUST;
        curDialogStart = "Vroom Vroom!";
        curDialogEnd = "Vroom poks!";

        t_idle = new Texture("texture/enemy/enemyIdle.png");
        a_idle = anim.createAnimation(t_idle, 3, 1);
        s_idle = 30;
        t_hack = new Texture("texture/enemy/enemyHack.png");
        a_hack = anim.createAnimation(t_hack, 3, 1);
        s_hack = 30;
        t_skill = new Texture("texture/enemy/enemyAttack1.png");
        a_skill = anim.createAnimation(t_skill, 3, 1);
        s_skill = 30;

        addToMap();
    }

    private static void addToMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, curName);
        map.put(idle, a_idle);
        map.put(hack, a_hack);
        map.put(skill, a_skill);
        map.put(skillName + "0", curSkillName0);
        map.put(skillName + "1", curSkillName1);
        map.put(skillName + "2", curSkillName2);
        map.put(speed + idle, s_idle);
        map.put(speed + hack, s_hack);
        map.put(speed + skill, s_skill);
        map.put(dialogStart, curDialogStart);
        map.put(dialogEnd, curDialogEnd);

        mapBosses.put((String) map.get(name), map);
    }

    public static void dispose() {
        t_idle.dispose();
        t_hack.dispose();
        t_skill.dispose();
    }
}
