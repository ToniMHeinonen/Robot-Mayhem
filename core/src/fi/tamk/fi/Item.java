package fi.tamk.fi;

import com.badlogic.gdx.math.MathUtils;

import java.util.HashMap;

public class Item {

    public static final String name = "name";
    public static final String description = "description";
    public static final String damage = "damage";
    public static final String usedInHall = "usedInHall";
    public static final String isSkill = "isSkill";

    public static final String BOMB = "Bomb";
    public static final String POTION = "Potion";
    public static final String DOUBLE_STEPS = "Polished Rotor";

    private static String[] allItems = new String[] {BOMB, POTION, DOUBLE_STEPS};
    private static HashMap<String,HashMap<String,Object>> mapItems;

    /* NOTE!
    Everytime you add new item, remember to:
    1. Make a String for it
    2. Add it to the allItems array
    3. Make a new method for it
     */

    /*
    Create items when the game starts.
     */
    public static void createItems() {
        mapItems = new HashMap<String, HashMap<String,Object>>();
        itemBomb();
        itemPotion();
        itemDoubleSteps();
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

    private static void itemBomb() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, BOMB);
        map.put(description, "Halven enemy's health");
        map.put(damage, 50);
        map.put(usedInHall, false);
        map.put(isSkill, false);

        mapItems.put((String) map.get(name), map);
    }

    private static void itemPotion() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, POTION);
        map.put(description, "Heal 50%");
        map.put(damage, -50);
        map.put(usedInHall, false);
        map.put(isSkill, false);

        mapItems.put((String) map.get(name), map);
    }

    private static void itemDoubleSteps() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DOUBLE_STEPS);
        map.put(description, "Gain double steps");
        map.put(damage, 0);
        map.put(usedInHall, true);
        map.put(isSkill, false);

        mapItems.put((String) map.get(name), map);
    }
}
