package fi.tamk.fi;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.HashMap;

public class Item {

    public final String name = "name";
    public final String description = "description";
    public final String value = "value";
    public final String usedInHall = "usedInHall";
    public final String isSkill = "isSkill";
    public final String price = "price";

    // Fight item specific values
    public final String critBoost = "critBoost";
    public final String missBoost = "missBoost";
    public final String dmgBoost = "dmgBoost";

    public final String BOMB = "BOMB";
    public final String POTION = "POTION";
    public final String DOUBLE_STEPS = "DOUBLE_STEPS";
    public final String OVERCLOCK = "OVERCLOCK";
    public final String LONG_SCOPE = "LONG_SCOPE";
    public final String POWER_POTION = "POWER_POTION";

    private final int baseValue = 5;
    private final int basePrice = 10;
    private ArrayList<String> allItems = new ArrayList<String>();
    private HashMap<String,HashMap<String,Object>> mapItems;

    /* NOTE!
    Everytime you add new item, remember to:
    1. Make a String for it
    2. Make a new method for it
    3. Add it's name and description to MyBundle file
    4. Retrieve it's localized name
    5. Add it to the allItems array
     */

    /*
    Create items when the game starts.
     */
    Item(MainGame game) {
        mapItems = new HashMap<String, HashMap<String,Object>>();
        itemBomb();
        itemPotion();
        itemDoubleSteps();
        itemOverclock();
        itemLongScope();
        itemPowerPotion();
    }

    public HashMap<String, Object> getItem(String item) {
        HashMap<String, Object> chosenItem;

        chosenItem = mapItems.get(item);

        return chosenItem;
    }

    public String selectRandomItem() {
        String selected = "";
        int random = MathUtils.random(0, allItems.size() - 1);
        selected = allItems.get(random);

        return selected;
    }

    private void itemBomb() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, BOMB);
        map.put(description, "bombDesc");
        map.put(value, 50);
        map.put(usedInHall, false);
        map.put(isSkill, false);
        map.put(critBoost, false);
        map.put(missBoost, false);
        map.put(dmgBoost, false);
        map.put(price, 5);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    private void itemPotion() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, POTION);
        map.put(description, "potionDesc");
        map.put(value, -50);
        map.put(usedInHall, false);
        map.put(isSkill, false);
        map.put(critBoost, false);
        map.put(missBoost, false);
        map.put(dmgBoost, false);
        map.put(price, 10);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    private void itemDoubleSteps() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DOUBLE_STEPS);
        map.put(description, "doubleStepsDesc");
        map.put(value, 0);
        map.put(usedInHall, true);
        map.put(isSkill, false);
        map.put(critBoost, false);
        map.put(missBoost, false);
        map.put(dmgBoost, false);
        map.put(price, 15);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    private void itemOverclock() {
        for (int i = 1; i <=3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, OVERCLOCK + index);
            map.put(description, "overclockDesc" + index);
            map.put(value, baseValue * i);
            map.put(usedInHall, false);
            map.put(isSkill, false);
            map.put(critBoost, true);
            map.put(missBoost, false);
            map.put(dmgBoost, false);
            map.put(price, basePrice * i);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    private void itemLongScope() {
        for (int i = 1; i <=3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, LONG_SCOPE + index);
            map.put(description, "longScopeDesc" + index);
            map.put(value, baseValue * i);
            map.put(usedInHall, false);
            map.put(isSkill, false);
            map.put(critBoost, false);
            map.put(missBoost, true);
            map.put(dmgBoost, false);
            map.put(price, basePrice * i);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    private void itemPowerPotion() {
        for (int i = 1; i <=3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, POWER_POTION + index);
            map.put(description, "powerPotionDesc" + index);
            map.put(value, baseValue * i);
            map.put(usedInHall, false);
            map.put(isSkill, false);
            map.put(critBoost, false);
            map.put(missBoost, false);
            map.put(dmgBoost, true);
            map.put(price, basePrice * i);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    public String[] getAllItems() {
        String[] converted = allItems.toArray(new String[0]);
        return converted;
    }
}
