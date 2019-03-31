package fi.tamk.fi;

import com.badlogic.gdx.math.MathUtils;

import java.util.HashMap;

public class Item {

    private static String name = "name";
    private static String description = "description";
    private static String damage = "damage";
    private static String usedInHall = "usedInHall";
    private static String isSkill = "isSkill";

    private static HashMap<String,HashMap<String,Object>> mapItems;
    private static HashMap<String,Object> mapBomb, mapPotion, mapDoubleSteps;

    private static String[] allItems = new String[] {"Bomb", "Potion", "Polished Rotor"};
    private static int BOMB = 0, POTION = 1, DOUBLE_STEPS = 2;

    /*
    Create items when the game starts.
     */
    public static void createItems() {

        mapBomb = itemBomb();
        mapPotion = itemPotion();
        mapDoubleSteps = itemDoubleSteps();

        mapItems = new HashMap<String, HashMap<String,Object>>();
        mapItems.put((String) mapBomb.get(name), mapBomb);
        mapItems.put((String) mapPotion.get(name), mapPotion);
        mapItems.put((String) mapDoubleSteps.get(name), mapDoubleSteps);
    }

    public static HashMap<String, Object> getItem(String item) {
        HashMap<String, Object> chosenItem;

        chosenItem = mapItems.get(item);

        return chosenItem;
    }

    public static String selectRandomItem() {
        String selected = "";
        int random = MathUtils.random(0, allItems.length - 1);
        selected = allItems[random];

        return selected;
    }

    private static HashMap<String, Object> itemBomb() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, allItems[BOMB]);
        map.put(description, "Halven enemy's health");
        map.put(damage, 50);
        map.put(usedInHall, false);
        map.put(isSkill, false);

        return map;
    }

    private static HashMap<String, Object> itemPotion() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, allItems[POTION]);
        map.put(description, "Heal 50%");
        map.put(damage, -50);
        map.put(usedInHall, false);
        map.put(isSkill, false);

        return map;
    }

    private static HashMap<String, Object> itemDoubleSteps() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, allItems[DOUBLE_STEPS]);
        map.put(description, "Gain double steps");
        map.put(damage, 0);
        map.put(usedInHall, true);
        map.put(isSkill, false);

        return map;
    }

    public static String getName() {
        return name;
    }

    public static String getDamage() {
        return damage;
    }

    public static String getIsSkill() {
        return isSkill;
    }

    public static String getUsedInHall() {
        return usedInHall;
    }

    public static String getDescription() {
        return description;
    }
}
