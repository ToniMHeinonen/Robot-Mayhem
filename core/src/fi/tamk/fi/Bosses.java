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
    public final String normalSize = "normalSize";
    public final String idle = "idle";
    public final String hack = "hack";
    public final String skill = "skill";
    public final String takeHit = "takeHit";
    public final String skillName = "skillName";
    public final String dialogStart = "dialogstart";
    public final String dialogEnd = "dialogend";
    public final String attackName = "attackName";

    private HashMap<String, HashMap<String,Object>> mapBosses;

    private Animation<TextureRegion> animIdle, animStun, animAttack, animDamage;

    private String curName, curAttackName, curSkillName0, curSkillName1, curSkillName2,
                            curDialogStart, curDialogEnd;
    private Boolean curNormalSize;

    public final String ROOMBOT = "Roombot";
    public final String COPPER = "Copper";
    public final String ROBBER = "Robber";
    public final String COPIER = "Copier";
    private String[] allBosses = new String[] {ROOMBOT, COPPER, ROBBER, COPIER};

    /* NOTE!
    Everytime you add a new boss, remember to:
    1. Make a String for it
    2. Add it to the allBosses array
    3. Make a new method for it
    4. Add attackName, dialogStart and dialogEnd to bundle
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
        bossCopier();
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
        curAttackName = "robberAttackName";
        curNormalSize = true;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.FIRE;
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
        curAttackName = "roombotAttackName";
        curNormalSize = false;
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
        curAttackName = "copperAttackName";
        curNormalSize = true;
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

    private void bossCopier() {
        curName = COPPER;
        curAttackName = "copierAttackName";
        curNormalSize = true;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.SHOCK;
        curSkillName2 = skills.REPAIR;
        curDialogStart = "copierDialogStart";
        curDialogEnd = "copierDialogEnd";

        animIdle = files.a_copierIdle;
        animAttack = files.a_copierAttack;
        animDamage = files.a_copierDamage;
        animStun = files.a_copierStun;

        addToMap();
    }

    private void addToMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, curName);
        map.put(attackName, curAttackName);
        map.put(normalSize, curNormalSize);
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
