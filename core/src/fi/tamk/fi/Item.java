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
    public final String boostType = "boostType";

    public final int NO_BOOST = 0, CRIT_BOOST = 1, MISS_BOOST = 2, DMG_BOOST = 3, ARMOR_BOOST = 4,
    HEAL_BOOST = 5;

    public final String BOMB = "BOMB";
    public final String POTION = "POTION";
    public final String DOUBLE_STEPS = "DOUBLE_STEPS";
    public final String OVERCLOCK = "OVERCLOCK";
    public final String LONG_SCOPE = "LONG_SCOPE";
    public final String POWER_POTION = "POWER_POTION";
    public final String IRON_CASE = "IRON_CASE";
    public final String REPAIR_BOT = "REPAIR_BOT";
    public final String SUPER_VISION = "SUPER_VISION";
    public final String HARDWARE = "HARDWARE";
    public final String DIAMOND_COAT = "DIAMOND_COAT";

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
    Item() {
        mapItems = new HashMap<String, HashMap<String,Object>>();
        itemBomb();
        itemPotion();
        itemDoubleSteps();
        itemOverclock();
        itemLongScope();
        itemPowerPotion();
        itemIronCase();
        itemRepairBot();
        itemSuperVision();
        itemHardware();
        itemDiamondCoat();
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
        map.put(boostType, NO_BOOST);
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
        map.put(boostType, NO_BOOST);
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
        map.put(boostType, NO_BOOST);
        map.put(price, 15);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    private void itemOverclock() {
        int baseValue = 5;
        for (int i = 1; i <=3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, OVERCLOCK + index);
            map.put(description, "overclockDesc" + index);
            map.put(value, baseValue * i);
            map.put(usedInHall, false);
            map.put(isSkill, false);
            map.put(boostType, CRIT_BOOST);
            map.put(price, basePrice * i);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    private void itemLongScope() {
        int baseValue = 5;
        for (int i = 1; i <=3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, LONG_SCOPE + index);
            map.put(description, "longScopeDesc" + index);
            map.put(value, baseValue * i);
            map.put(usedInHall, false);
            map.put(isSkill, false);
            map.put(boostType, MISS_BOOST);
            map.put(price, basePrice * i);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    private void itemPowerPotion() {
        double baseValue = 0.1;
        for (int i = 1; i <=3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, POWER_POTION + index);
            map.put(description, "powerPotionDesc" + index);
            map.put(value, baseValue * i);
            map.put(usedInHall, false);
            map.put(isSkill, false);
            map.put(boostType, DMG_BOOST);
            map.put(price, basePrice * i);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    private void itemIronCase() {
        double[] values = new double[] {0.0, 0.15, 0.34, 0.5};
        for (int i = 1; i <=3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, IRON_CASE + index);
            map.put(description, "ironCaseDesc" + index);
            map.put(value, values[i]);
            map.put(usedInHall, false);
            map.put(isSkill, false);
            map.put(boostType, ARMOR_BOOST);
            map.put(price, basePrice * i);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    private void itemRepairBot() {
        double baseValue = 0.2;
        for (int i = 1; i <=3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, REPAIR_BOT + index);
            map.put(description, "repairBotDesc" + index);
            map.put(value, baseValue * i);
            map.put(usedInHall, false);
            map.put(isSkill, false);
            map.put(boostType, HEAL_BOOST);
            map.put(price, basePrice * i);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    private void itemSuperVision() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SUPER_VISION);
        map.put(description, "superVisionDesc");
        map.put(value, 0.1);
        map.put(usedInHall, true);
        map.put(isSkill, false);
        map.put(boostType, MISS_BOOST);
        map.put(price, 80);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    private void itemHardware() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, HARDWARE);
        map.put(description, "hardwareDesc");
        map.put(value, 0.05);
        map.put(usedInHall, true);
        map.put(isSkill, false);
        map.put(boostType, DMG_BOOST);
        map.put(price, 80);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    private void itemDiamondCoat() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, DIAMOND_COAT);
        map.put(description, "diamondCoatDesc");
        map.put(value, 0.34);
        map.put(usedInHall, true);
        map.put(isSkill, false);
        map.put(boostType, ARMOR_BOOST);
        map.put(price, 80);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    public String[] getAllItems() {
        String[] converted = allItems.toArray(new String[0]);
        return converted;
    }
}
