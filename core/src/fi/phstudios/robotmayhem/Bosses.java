package fi.phstudios.robotmayhem;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Bosses {

    private Files files;
    private Skills skills;
    /*
    Use these, since if you need to change the name to something else, you then only need
    to change it in here. These also lower coding mistake chances.
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

    /**
     * Create bosses when the game starts.
     * @param game main game instance
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
        // Don't count Fabio in all bosses
        allBosses.remove(FABIO);
        // Used for checking pool size in MainGame
        poolBossesSize = allBosses.size();
    }

    /**
     * Retrieve correct boss map by using boss's name.
     * @param boss selected boss
     * @return map of the boss
     */
    public HashMap<String, Object> getBoss(String boss) {
        HashMap<String, Object> chosenBoss;

        chosenBoss = mapBosses.get(boss);

        return chosenBoss;
    }

    /**
     * Selects random boss.
     * @return random boss
     */
    public String selectRandomBoss() {
        String selected = "";
        int random = MathUtils.random(0, allBosses.size() - 1);
        selected = allBosses.get(random);

        return selected;
    }

    /**
     * Create boss Roombot. Change current variables and add them to the map.
     */
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

    /**
     * Create boss Robber. Change current variables and add them to the map.
     */
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

    /**
     * Create boss Copper. Change current variables and add them to the map.
     */
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

    /**
     * Create boss Copier. Change current variables and add them to the map.
     */
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

    /**
     * Create boss PC. Change current variables and add them to the map.
     */
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

    /**
     * Create boss Baller. Change current variables and add them to the map.
     */
    private void bossBaller() {
        curName = BALLER;
        curAttackName = "ballerAttackName";
        curNormalSize = true;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.ENRICHMENT;
        curSkillName2 = skills.DIAMOND_ROLL;
        curSkillName3 = skills.SHINE;
        curSkillName4 = skills.COIN_FLIP;
        curDialogStart = "ballerDialogStart";
        curDialogEnd = "ballerDialogEnd";

        animIdle = files.a_ballerIdle;
        animAttack = files.a_ballerAttack;
        animDamage = files.a_ballerDamage;
        animStun = files.a_ballerStun;

        addToMap();
    }

    /**
     * Create boss Fabio. Change current variables and add them to the map.
     */
    private void bossFabio() {
        curName = FABIO;
        curAttackName = "fabioAttackName";
        curNormalSize = true;
        curSkillName0 = skills.ATTACK;
        curSkillName1 = skills.RUSTIFY;
        curSkillName2 = skills.OVERPOWER;
        curSkillName3 = skills.MAYHEM;
        curSkillName4 = skills.VR;
        curDialogStart = "fabioDialogStart";
        curDialogEnd = "fabioDialogEnd";

        animIdle = files.a_fabioIdle;
        animAttack = files.a_fabioAttack;
        animDamage = files.a_fabioDamage;
        animStun = files.a_fabioStun;

        addToMap();
    }

    /**
     * Add current variables to the map. Also add current name to allBosses array and add map
     * to mapBosses.
     */
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
