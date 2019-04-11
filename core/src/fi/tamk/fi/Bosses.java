package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.HashMap;

public class Bosses {

    private static Files files;
    private static I18NBundle localize;
    private static Skills skills;
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

    private static Animation<TextureRegion> animIdle, animStun, animAttack, animDamage;

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
    public static void createBosses(MainGame game) {
        files = game.getFiles();
        localize = game.getLocalize();
        skills = game.getSkills();
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
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.SUCTION;
        curSkillName2 = skills.DUST;

        curDialogStart = localize.get("robberDialogStart");
        curDialogEnd = localize.get("robberDialogEnd");

        animIdle = files.a_robberIdle;
        animAttack = files.a_robberAttack;
        animDamage = files.a_robberDamage;
        animStun = files.a_robberStun;

        addToMap();
    }

    private static void bossRoombot() {
        curName = ROOMBOT;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.SUCTION;
        curSkillName2 = skills.DUST;
        curDialogStart = localize.get("roombotDialogStart");
        curDialogEnd = localize.get("roombotDialogEnd");

        animIdle = files.a_roombotIdle;
        animAttack = files.a_roombotAttack;
        animDamage = files.a_roombotDamage;
        animStun = files.a_roombotStun;

        addToMap();
    }

    private static void bossCopper() {
        curName = COPPER;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.SHOCK;
        curSkillName2 = skills.DUST;
        curDialogStart = localize.get("copperDialogStart");
        curDialogEnd = localize.get("copperDialogEnd");

        animIdle = files.a_copperIdle;
        animAttack = files.a_copperAttack;
        animDamage = files.a_copperDamage;
        animStun = files.a_copperStun;

        addToMap();
    }

    private static void addToMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, curName);
        map.put(idle, animIdle);
        map.put(skill, animAttack);
        map.put(takeHit, animDamage);
        map.put(hack, animStun);
        map.put(skillName + "0", curSkillName0);
        map.put(skillName + "1", curSkillName1);
        map.put(skillName + "2", curSkillName2);
        map.put(dialogStart, curDialogStart);
        map.put(dialogEnd, curDialogEnd);

        mapBosses.put((String) map.get(name), map);
    }
}
