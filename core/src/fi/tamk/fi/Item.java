package fi.tamk.fi;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.I18NBundle;

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

    public final String BOMB = "BOMB";
    public final String POTION = "POTION";
    public final String DOUBLE_STEPS = "DOUBLE_STEPS";
    public final String OVERCLOCK1 = "OVERCLOCK1";
    public final String OVERCLOCK2 = "OVERCLOCK2";
    public final String OVERCLOCK3 = "OVERCLOCK3";
    public final String LONG_SCOPE1 = "LONG_SCOPE1";
    public final String LONG_SCOPE2 = "LONG_SCOPE2";
    public final String LONG_SCOPE3 = "LONG_SCOPE3";

    private final String[] allItems = new String[] {BOMB, POTION, DOUBLE_STEPS, OVERCLOCK1,
            OVERCLOCK2, OVERCLOCK3, LONG_SCOPE1, LONG_SCOPE2, LONG_SCOPE3};
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
        itemOverclock1();
        itemOverclock2();
        itemOverclock3();
        itemLongScope1();
        itemLongScope2();
        itemLongScope3();
    }

    public HashMap<String, Object> getItem(String item) {
        HashMap<String, Object> chosenItem;

        chosenItem = mapItems.get(item);

        return chosenItem;
    }

    public String selectRandomItem() {
        String selected = "";
        int random = MathUtils.random(0, allItems.length - 1);
        selected = allItems[random];

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
        map.put(price, 5);

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
        map.put(price, 10);

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
        map.put(price, 15);

        mapItems.put((String) map.get(name), map);
    }

    private void itemOverclock1() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, OVERCLOCK1);
        map.put(description, "overclock1Desc");
        map.put(value, 5);
        map.put(usedInHall, false);
        map.put(isSkill, false);
        map.put(critBoost, true);
        map.put(missBoost, false);
        map.put(price, 10);

        mapItems.put((String) map.get(name), map);
    }

    private void itemOverclock2() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, OVERCLOCK2);
        map.put(description, "overclock2Desc");
        map.put(value, 10);
        map.put(usedInHall, false);
        map.put(isSkill, false);
        map.put(critBoost, true);
        map.put(missBoost, false);
        map.put(price, 20);

        mapItems.put((String) map.get(name), map);
    }

    private void itemOverclock3() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, OVERCLOCK3);
        map.put(description, "overclock3Desc");
        map.put(value, 15);
        map.put(usedInHall, false);
        map.put(isSkill, false);
        map.put(critBoost, true);
        map.put(missBoost, false);
        map.put(price, 30);

        mapItems.put((String) map.get(name), map);
    }

    private void itemLongScope1() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, LONG_SCOPE1);
        map.put(description, "longScope1Desc");
        map.put(value, 5);
        map.put(usedInHall, false);
        map.put(isSkill, false);
        map.put(critBoost, false);
        map.put(missBoost, true);
        map.put(price, 10);

        mapItems.put((String) map.get(name), map);
    }

    private void itemLongScope2() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, LONG_SCOPE2);
        map.put(description, "longScope2Desc");
        map.put(value, 10);
        map.put(usedInHall, false);
        map.put(isSkill, false);
        map.put(critBoost, false);
        map.put(missBoost, true);
        map.put(price, 20);

        mapItems.put((String) map.get(name), map);
    }

    private void itemLongScope3() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, LONG_SCOPE3);
        map.put(description, "longScope3Desc");
        map.put(value, 15);
        map.put(usedInHall, false);
        map.put(isSkill, false);
        map.put(critBoost, false);
        map.put(missBoost, true);
        map.put(price, 30);

        mapItems.put((String) map.get(name), map);
    }

    public String[] getAllItems() {
        return allItems;
    }
}
