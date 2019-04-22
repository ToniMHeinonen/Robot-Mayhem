package fi.phstudios.robotmayhem;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
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
            curSkillName3, curSkillName4, curDialogStart, curDialogEnd;
    private Boolean curNormalSize;

    public final String ROOMBOT = "Roombot";
    public final String COPPER = "Copper";
    public final String ROBBER = "Robber";
    public final String COPIER = "Copier";
    public final String PC = "PC";
    public final String BALLER = "Baller";
    public final String FABIO = "Fabio";
    private ArrayList<String> allBosses = new ArrayList<String>();
    public final int poolBossesSize;

    /* NOTE!
    Everytime you add a new boss, remember to:
    1. Make a String for it
    2. Make a new method for it
    3. Add attackName, dialogStart and dialogEnd to bundle
     */

    /* SKILL USAGE
    - Pool 1 uses skills 1 and 2
    - Pool 2 uses skills 3 and 4
    - Pool 3 uses skills 2 and 4
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
        bossPC();
        bossBaller();
        bossFabio();
        allBosses.remove(FABIO); // Don't count Fabio in all bosses
        poolBossesSize = allBosses.size();
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
        int random = MathUtils.random(0, allBosses.size() - 1);
        selected = allBosses.get(random);

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

    private void bossRoombot() {
        curName = ROOMBOT;
        curAttackName = "roombotAttackName";
        curNormalSize = false;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.BUCKET;
        curSkillName2 = skills.DUST;
        curSkillName3 = skills.SOAP;
        curSkillName4 = skills.SUCTION;
        curDialogStart = "roombotDialogStart";
        curDialogEnd = "roombotDialogEnd";

        animIdle = files.a_roombotIdle;
        animAttack = files.a_roombotAttack;
        animDamage = files.a_roombotDamage;
        animStun = files.a_roombotStun;

        addToMap();
    }

    private void bossRobber() {
        curName = ROBBER;
        curAttackName = "robberAttackName";
        curNormalSize = true;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.MISCHIEF;
        curSkillName2 = skills.PICKPOCKET;
        curSkillName3 = skills.HIJACK;
        curSkillName4 = skills.SHADOWSTEP;
        curDialogStart = "robberDialogStart";
        curDialogEnd = "robberDialogEnd";

        animIdle = files.a_robberIdle;
        animAttack = files.a_robberAttack;
        animDamage = files.a_robberDamage;
        animStun = files.a_robberStun;

        addToMap();
    }

    private void bossCopper() {
        curName = COPPER;
        curAttackName = "copperAttackName";
        curNormalSize = true;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.ORDER;
        curSkillName2 = skills.SPEEDING;
        curSkillName3 = skills.THE_LAW;
        curSkillName4 = skills.LOW_SPEED;
        curDialogStart = "copperDialogStart";
        curDialogEnd = "copperDialogEnd";

        animIdle = files.a_copperIdle;
        animAttack = files.a_copperAttack;
        animDamage = files.a_copperDamage;
        animStun = files.a_copperStun;

        addToMap();
    }

    private void bossCopier() {
        curName = COPIER;
        curAttackName = "copierAttackName";
        curNormalSize = true;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.DEJA_VU;
        curSkillName2 = skills.BLACK_INK;
        curSkillName3 = skills.FLASH;
        curSkillName4 = skills.COPYCAT;
        curDialogStart = "copierDialogStart";
        curDialogEnd = "copierDialogEnd";

        animIdle = files.a_copierIdle;
        animAttack = files.a_copierAttack;
        animDamage = files.a_copierDamage;
        animStun = files.a_copierStun;

        addToMap();
    }

    private void bossPC() {
        curName = PC;
        curAttackName = "pcAttackName";
        curNormalSize = false;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.POPUP;
        curSkillName2 = skills.VIRUS;
        curSkillName3 = skills.REBOOT;
        curSkillName4 = skills.TROJAN;
        curDialogStart = "pcDialogStart";
        curDialogEnd = "pcDialogEnd";

        animIdle = files.a_pcIdle;
        animAttack = files.a_pcAttack;
        animDamage = files.a_pcDamage;
        animStun = files.a_pcStun;

        addToMap();
    }

    private void bossBaller() {
        curName = BALLER;
        curAttackName = "ballerAttackName";
        curNormalSize = true;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.ATTACK;
        curSkillName2 = skills.ATTACK;
        curSkillName3 = skills.ATTACK;
        curSkillName4 = skills.ATTACK;
        curDialogStart = "ballerDialogStart";
        curDialogEnd = "ballerDialogEnd";

        animIdle = files.a_ballerIdle;
        animAttack = files.a_ballerAttack;
        animDamage = files.a_ballerDamage;
        animStun = files.a_ballerStun;

        addToMap();
    }

    private void bossFabio() {
        curName = FABIO;
        curAttackName = "fabioAttackName";
        curNormalSize = true;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.ATTACK;
        curSkillName2 = skills.ATTACK;
        curSkillName3 = skills.ATTACK;
        curSkillName4 = skills.ATTACK;
        curDialogStart = "fabioDialogStart";
        curDialogEnd = "fabioDialogEnd";

        animIdle = files.a_fabioIdle;
        animAttack = files.a_fabioAttack;
        animDamage = files.a_fabioDamage;
        animStun = files.a_fabioStun;

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
        map.put(skillName + "3", curSkillName3);
        map.put(skillName + "4", curSkillName4);
        map.put(dialogStart, curDialogStart);
        map.put(dialogEnd, curDialogEnd);

        allBosses.add(curName);
        mapBosses.put((String) map.get(name), map);
    }
}
