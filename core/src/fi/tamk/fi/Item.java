package fi.tamk.fi;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.HashMap;

public class Item {

    private final I18NBundle localize;

    public final String name = "name";
    public final String description = "description";
    public final String damage = "damage";
    public final String usedInHall = "usedInHall";
    public final String isSkill = "isSkill";
    public final String price = "price";

    public final String BOMB, POTION, DOUBLE_STEPS;

    private final String[] allItems;
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
        localize = game.getLocalize();

        BOMB = localize.get("BOMB");
        POTION = localize.get("POTION");
        DOUBLE_STEPS = localize.get("DOUBLE_STEPS");

        allItems = new String[] {BOMB, POTION, DOUBLE_STEPS};

        mapItems = new HashMap<String, HashMap<String,Object>>();
        itemBomb();
        itemPotion();
        itemDoubleSteps();
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
        map.put(description, localize.get("bombDesc"));
        map.put(damage, 50);
        map.put(usedInHall, false);
        map.put(isSkill, false);
        map.put(price, 5);

        mapItems.put((String) map.get(name), map);
    }

    private void itemPotion() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, POTION);
        map.put(description, localize.get("potionDesc"));
        map.put(damage, -50);
        map.put(usedInHall, false);
        map.put(isSkill, false);
        map.put(price, 10);

        mapItems.put((String) map.get(name), map);
    }

    private void itemDoubleSteps() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DOUBLE_STEPS);
        map.put(description, localize.get("doubleStepsDesc"));
        map.put(damage, 0);
        map.put(usedInHall, true);
        map.put(isSkill, false);
        map.put(price, 15);

        mapItems.put((String) map.get(name), map);
    }

    public String[] getAllItems() {
        return allItems;
    }
}
