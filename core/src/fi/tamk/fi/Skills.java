package fi.tamk.fi;

import java.util.HashMap;

public class Skills {
    /*
    Use these, since if you need to change the name to something else, you then only need
    to change it in here.
     */
    private static String name = "name";
    private static String damage = "damage";
    private static String maxCooldown = "maxCooldown";
    private static String currentCooldown = "currentCooldown";

    private static HashMap<String,HashMap<String,Object>> mapSkills;
    private static HashMap<String,Object> mapAttack;
    private static HashMap<String,Object> mapDefend;

    /*
    Create skills when the game starts.
     */
    public static void createSkills() {
        skillAttack();
        skillDefend();

        mapSkills = new HashMap<String, HashMap<String,Object>>();
        mapSkills.put((String) mapAttack.get(name), mapAttack);
        mapSkills.put((String) mapDefend.get(name), mapDefend);
    }

    /*
    Retrieve correct skill map by using string value.
     */
    public static HashMap<String, Object> getSkill(String skill) {
        HashMap<String, Object> chosenSkill;

        chosenSkill = mapSkills.get(skill);

        return chosenSkill;
    }

    private static void skillAttack() {
        mapAttack = new HashMap<String, Object>();
        mapAttack.put(name, "attack");
        mapAttack.put(damage, 2.5);
        mapAttack.put(maxCooldown, 0);
        mapAttack.put(currentCooldown, 0);
    }

    private static void skillDefend() {
        mapDefend = new HashMap<String, Object>();
        mapDefend.put(name, "defend");
        mapDefend.put(damage, 2.5);
        mapDefend.put(maxCooldown, 3);
        mapDefend.put(currentCooldown, 0);
    }

    public static String getName() {
        return name;
    }

    public static String getDamage() {
        return damage;
    }

    public static String getMaxCooldown() {
        return maxCooldown;
    }

    public static String getCurrentCooldown() {
        return currentCooldown;
    }
}
