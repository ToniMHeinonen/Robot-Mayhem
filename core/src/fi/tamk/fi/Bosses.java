package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.HashMap;

public class Bosses {

    private Files files;
    //private I18NBundle localize;
    private Skills skills;
    /*
    Use these, since if you need to change the name to something else, you then only need
    to change it in here.
     */
    public final String name = "name";
    public final String idle = "idle";
    public final String hack = "hack";
    public final String skill = "skill";
    public final String takeHit = "takeHit";
    public final String skillName = "skillName";
    public final String dialogStart = "dialogstart";
    public final String dialogEnd = "dialogend";

    private HashMap<String, HashMap<String,Object>> mapBosses;

    private Animation<TextureRegion> animIdle, animStun, animAttack, animDamage;

    private String curName, curSkillName0, curSkillName1, curSkillName2,
                            curDialogStart, curDialogEnd;

    public final String ROOMBOT = "Roombot";
    public final String COPPER = "Copper";
    public final String ROBBER = "Robber";
    private String[] allBosses = new String[] {ROOMBOT, COPPER, ROBBER};

    /* NOTE!
    Everytime you add new boss, remember to:
    1. Make a String for it
    2. Add it to the allBosses array
    3. Make a new method for it
     */

    /*
    Create bosses when the game starts.
     */
    Bosses(MainGame game) {
        files = game.getFiles();
        skills = game.getSkills();
        mapBosses = new HashMap<String, HashMap<String,Object>>();
        bossRoombot();
        bossCopper();
        bossRobber();
    }

    /*
    Retrieve correct boss map by using string value.
     */
    public HashMap<String, Object> getBoss(String boss) {
        HashMap<String, Object> chosenBoss;

        chosenBoss = mapBosses.get(boss);

        return chosenBoss;
    }

    public String selectRandomBoss() {
        String selected = "";
        int random = MathUtils.random(0, allBosses.length - 1);
        selected = allBosses[random];

        return selected;
    }

    public String[] retrieveBossSkills(String boss) {
        String[] skills = new String[3];
        HashMap<String, Object> bossMap = getBoss(boss);

        for (int i = 0; i < skills.length; i++) {
            skills[i] = (String) bossMap.get(skillName + String.valueOf(i));
        }

        return skills;
    }

    private void bossRobber() {
        curName = ROBBER;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.SUCTION;
        curSkillName2 = skills.DUST;

        curDialogStart = "robberDialogStart";
        curDialogEnd = "robberDialogEnd";

        animIdle = files.a_robberIdle;
        animAttack = files.a_robberAttack;
        animDamage = files.a_robberDamage;
        animStun = files.a_robberStun;

        addToMap();
    }

    private void bossRoombot() {
        curName = ROOMBOT;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.SUCTION;
        curSkillName2 = skills.DUST;
        curDialogStart = "roombotDialogStart";
        curDialogEnd = "roombotDialogEnd";

        animIdle = files.a_roombotIdle;
        animAttack = files.a_roombotAttack;
        animDamage = files.a_roombotDamage;
        animStun = files.a_roombotStun;

        addToMap();
    }

    private void bossCopper() {
        curName = COPPER;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.SHOCK;
        curSkillName2 = skills.DUST;
        curDialogStart = "copperDialogStart";
        curDialogEnd = "copperDialogEnd";

        animIdle = files.a_copperIdle;
        animAttack = files.a_copperAttack;
        animDamage = files.a_copperDamage;
        animStun = files.a_copperStun;

        addToMap();
    }

    private void addToMap() {
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
