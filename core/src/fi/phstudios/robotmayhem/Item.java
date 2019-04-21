package fi.phstudios.robotmayhem;

import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class Item {

    public final String name = "name";
    public final String description = "description";
    public final String value = "value";
    public final String usedInHall = "usedInHall";
    public final String isSkill = "isSkill";
    public final String price = "price";
    public final String itemType = "itemType";
    public final String boostType = "boostType";
    public final String isPermanent = "isPermanent";

    public final int NO_BOOST = 0, CRIT_BOOST = 1, MISS_BOOST = 2, DMG_BOOST = 3, ARMOR_BOOST = 4,
    HEAL_BOOST = 5;

    public final int TYPE_POTION = 1, TYPE_BOMB = 2, TYPE_BOOST = 3, TYPE_BANK = 4, TYPE_UNIQUE = 5;

    public final String BOMB = "BOMB";
    public final String POTION = "POTION";
    public final String BANK_UPGRADE = "BANK_UPGRADE";
    public final String HEAL_LIQUID = "HEAL_LIQUID";
    public final String GLITTER_BOMB = "GLITTER_BOMB";
    public final String OVERCLOCK = "OVERCLOCK";
    public final String LONG_SCOPE = "LONG_SCOPE";
    public final String POWER_POTION = "POWER_POTION";
    public final String IRON_CASE = "IRON_CASE";
    public final String REPAIR_BOT = "REPAIR_BOT";
    public final String SUPER_VISION = "SUPER_VISION";
    public final String EXPERIMENTAL = "EXPERIMENTAL";
    public final String HARDWARE = "HARDWARE";
    public final String DIAMOND_COAT = "DIAMOND_COAT";
    public final String HEAL_COOLANT = "HEAL_COOLANT";
    public final String ITEM_REFLECT = "ITEM_REFLECT";

    private final int basePrice = 10;
    private final int expensive = 80;
    public final int mostExpensivePowerup = basePrice*3; // Used in UtilPowerUp
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
        itemBankUpgrade();
        itemHealLiquid();
        //itemGlitterBomb();
        itemOverclock();
        itemLongScope();
        itemPowerPotion();
        itemIronCase();
        itemRepairBot();
        itemSuperVision();
        itemExperimental();
        itemHardware();
        itemDiamondCoat();
        itemHealCoolant();
        itemReflect();
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

    private void itemBankUpgrade() {
        float baseValue = 1000f;
        for (int i = 1; i <= 3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, BANK_UPGRADE + index);
            map.put(description, "bankUpgradeDesc" + index);
            map.put(value, baseValue*i);
            map.put(usedInHall, true);
            map.put(isSkill, false);
            map.put(itemType, TYPE_BANK);
            map.put(boostType, NO_BOOST);
            map.put(price, basePrice*i);
            map.put(isPermanent, true);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    private void itemHealLiquid() {
        double baseValue = -20.0;
        for (int i = 1; i <= 3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, HEAL_LIQUID + index);
            map.put(description, "healLiquidDesc" + index);
            map.put(value, baseValue*i);
            map.put(usedInHall, false);
            map.put(isSkill, false);
            map.put(itemType, TYPE_POTION);
            map.put(boostType, NO_BOOST);
            map.put(price, basePrice*i);
            map.put(isPermanent, false);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    private void itemGlitterBomb() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, GLITTER_BOMB);
        map.put(description, "glitterBombDesc");
        map.put(value, 50.0);
        map.put(usedInHall, false);
        map.put(isSkill, false);
        map.put(itemType, TYPE_BOMB);
        map.put(boostType, NO_BOOST);
        map.put(price, 5);
        map.put(isPermanent, false);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    private void itemOverclock() {
        int baseValue = 10;
        for (int i = 1; i <=3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, OVERCLOCK + index);
            map.put(description, "overclockDesc" + index);
            map.put(value, baseValue * i);
            map.put(usedInHall, false);
            map.put(isSkill, false);
            map.put(itemType, TYPE_BOOST);
            map.put(boostType, CRIT_BOOST);
            map.put(price, basePrice * i);
            map.put(isPermanent, false);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    private void itemLongScope() {
        int baseValue = 10;
        for (int i = 1; i <=3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, LONG_SCOPE + index);
            map.put(description, "longScopeDesc" + index);
            map.put(value, baseValue * i);
            map.put(usedInHall, false);
            map.put(isSkill, false);
            map.put(itemType, TYPE_BOOST);
            map.put(boostType, MISS_BOOST);
            map.put(price, basePrice * i);
            map.put(isPermanent, false);

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
            map.put(itemType, TYPE_BOOST);
            map.put(boostType, DMG_BOOST);
            map.put(price, basePrice * i);
            map.put(isPermanent, false);

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
            map.put(itemType, TYPE_BOOST);
            map.put(boostType, ARMOR_BOOST);
            map.put(price, basePrice * i);
            map.put(isPermanent, false);

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
            map.put(itemType, TYPE_BOOST);
            map.put(boostType, HEAL_BOOST);
            map.put(price, basePrice * i);
            map.put(isPermanent, false);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    private void itemSuperVision() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, SUPER_VISION);
        map.put(description, "superVisionDesc");
        map.put(value, 20);
        map.put(usedInHall, true);
        map.put(isSkill, false);
        map.put(itemType, TYPE_BOOST);
        map.put(boostType, MISS_BOOST);
        map.put(price, expensive);
        map.put(isPermanent, true);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    private void itemExperimental() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, EXPERIMENTAL);
        map.put(description, "experimentalDesc");
        map.put(value, 20);
        map.put(usedInHall, true);
        map.put(isSkill, false);
        map.put(itemType, TYPE_BOOST);
        map.put(boostType, CRIT_BOOST);
        map.put(price, expensive);
        map.put(isPermanent, true);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    private void itemHardware() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, HARDWARE);
        map.put(description, "hardwareDesc");
        map.put(value, 0.2);
        map.put(usedInHall, true);
        map.put(isSkill, false);
        map.put(itemType, TYPE_BOOST);
        map.put(boostType, DMG_BOOST);
        map.put(price, expensive);
        map.put(isPermanent, true);

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
        map.put(itemType, TYPE_BOOST);
        map.put(boostType, ARMOR_BOOST);
        map.put(price, expensive);
        map.put(isPermanent, true);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    private void itemHealCoolant() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, HEAL_COOLANT);
        map.put(description, "healCoolantDesc");
        map.put(value, 0.3);
        map.put(usedInHall, true);
        map.put(isSkill, false);
        map.put(itemType, TYPE_BOOST);
        map.put(boostType, HEAL_BOOST);
        map.put(price, expensive);
        map.put(isPermanent, true);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    private void itemReflect() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put(name, ITEM_REFLECT);
        map.put(description, "itemReflectDesc");
        map.put(value, 0.0);
        map.put(usedInHall, true);
        map.put(isSkill, false);
        map.put(itemType, TYPE_UNIQUE);
        map.put(boostType, NO_BOOST);
        map.put(price, expensive);
        map.put(isPermanent, true);

        allItems.add((String) map.get(name));
        mapItems.put((String) map.get(name), map);
    }

    public String[] getAllItems() {
        String[] converted = allItems.toArray(new String[0]);
        return converted;
    }
}
