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
    public static final String takeHit = "takeHit";
    public static final String speed = "speed";
    public static final String skillName = "skillName";
    public static final String dialogStart = "dialogstart";
    public static final String dialogEnd = "dialogend";

    private static HashMap<String, HashMap<String,Object>> mapBosses;

    private static int s_idle, s_hack, s_skill, s_takeHit; // Animation speed
    private static Texture t_idle, t_hack, t_skill, t_takeHit;
    private static Animation<TextureRegion> a_idle, a_hack, a_skill, a_takeHit;
    private static Animating anim = new Animating();

    private static String curName, curSkillName0, curSkillName1, curSkillName2,
                            curDialogStart, curDialogEnd;

    public static final String ROOMBOT = "Roombot";
    public static final String COPPER = "Copper";
    public static final String ROBBER = "Robber";
    private static String[] allBosses = new String[] {ROOMBOT, COPPER, ROBBER};

    /* NOTE!
    Everytime you add new boss, remember to:
    1. Make a String for it
    2. Add it to the allBosses array
    3. Make a new method for it
     */

    /*
    Create bosses when the game starts.
     */
    public static void createBosses() {
        mapBosses = new HashMap<String, HashMap<String,Object>>();
        bossRoombot();
        bossCopper();
        bossRobber();
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

    public static String[] retrieveBossSkills(String boss) {
        String[] skills = new String[3];
        HashMap<String, Object> bossMap = getBoss(boss);

        for (int i = 0; i < skills.length; i++) {
            skills[i] = (String) bossMap.get(skillName + String.valueOf(i));
        }

        return skills;
    }

    private static void bossRobber() {
        curName = ROBBER;
        curSkillName0 = Skills.ATTACK;
        curSkillName1 = Skills.SUCTION;
        curSkillName2 = Skills.DUST;

        //curDialogStart = "I WILL ROB YOU NOW " + MainGame.getName() + " !!!";
        curDialogStart = "I WILL ROB YOU NOW!!!";
        curDialogEnd = "Wish I hadn't deleted Subtle.exe...";

        t_idle = new Texture("texture/robber/robber_idle.png");
        a_idle = anim.createAnimation(t_idle, 4, 4);
        s_idle = 8;
        t_hack = new Texture("texture/robber/robber_stun.png");
        a_hack = anim.createAnimation(t_hack, 4, 2);
        s_hack = 8;
        t_skill = new Texture("texture/robber/robber_attack.png");
        a_skill = anim.createAnimation(t_skill, 4, 1);
        s_skill = 8;
        t_takeHit = new Texture("texture/robber/robber_damage.png");
        a_takeHit = anim.createAnimation(t_takeHit, 4, 1);
        s_takeHit = 8;

        addToMap();
    }

    private static void bossRoombot() {
        curName = ROOMBOT;
        curSkillName0 = Skills.ATTACK;
        curSkillName1 = Skills.SUCTION;
        curSkillName2 = Skills.DUST;
        curDialogStart = "Vroom Vroom!";
        //curDialogStart = "Vroom Vroom! " + MainGame.getName();
        curDialogEnd = "Vroom poks!";

        t_idle = new Texture("texture/roombot/roombot_idle.png");
        a_idle = anim.createAnimation(t_idle, 4, 4);
        s_idle = 8;
        t_hack = new Texture("texture/roombot/roombot_stun.png");
        a_hack = anim.createAnimation(t_hack, 4, 2);
        s_hack = 8;
        t_skill = new Texture("texture/roombot/roombot_attack.png");
        a_skill = anim.createAnimation(t_skill, 4, 1);
        s_skill = 8;
        t_takeHit = new Texture("texture/roombot/roombot_damage.png");
        a_takeHit = anim.createAnimation(t_takeHit, 4, 1);
        s_takeHit = 8;

        addToMap();
    }

    private static void bossCopper() {
        curName = COPPER;
        curSkillName0 = Skills.ATTACK;
        curSkillName1 = Skills.SHOCK;
        curSkillName2 = Skills.DUST;
        curDialogStart = "Here, have a 500 ticket!";
        //curDialogStart = "Here, have a 500 ticket " + MainGame.getName() + " !";
        curDialogEnd = "I'll pay your ticket!";

        t_idle = new Texture("texture/copper/copper_idle.png");
        a_idle = anim.createAnimation(t_idle, 4, 4);
        s_idle = 8;
        t_hack = new Texture("texture/copper/copper_stun.png");
        a_hack = anim.createAnimation(t_hack, 4, 2);
        s_hack = 8;
        t_skill = new Texture("texture/copper/copper_attack.png");
        a_skill = anim.createAnimation(t_skill, 4, 1);
        s_skill = 8;
        t_takeHit = new Texture("texture/copper/copper_damage.png");
        a_takeHit = anim.createAnimation(t_takeHit, 4, 1);
        s_takeHit = 8;

        addToMap();
    }

    private static void addToMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, curName);
        map.put(idle, a_idle);
        map.put(hack, a_hack);
        map.put(skill, a_skill);
        map.put(takeHit, a_takeHit);
        map.put(skillName + "0", curSkillName0);
        map.put(skillName + "1", curSkillName1);
        map.put(skillName + "2", curSkillName2);
        map.put(speed + idle, s_idle);
        map.put(speed + hack, s_hack);
        map.put(speed + skill, s_skill);
        map.put(speed + takeHit, s_takeHit);
        map.put(dialogStart, curDialogStart);
        map.put(dialogEnd, curDialogEnd);

        mapBosses.put((String) map.get(name), map);
    }

    public static void dispose() {
        t_idle.dispose();
        t_hack.dispose();
        t_skill.dispose();
        t_takeHit.dispose();
    }
}
