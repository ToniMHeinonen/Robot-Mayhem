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
     */

    /**
     * Create items when the game starts.
     */
    Item() {
        mapItems = new HashMap<String, HashMap<String,Object>>();
        itemBankUpgrade();
        itemHealLiquid();
        itemGlitterBomb();
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

    /**
     * Retrieve correct item map.
     * @param item selected item
     * @return map of the item
     */
    public HashMap<String, Object> getItem(String item) {
        HashMap<String, Object> chosenItem;

        chosenItem = mapItems.get(item);

        return chosenItem;
    }

    /**
     * Select random item. Mainly used by UtilPowerUp.
     * @return random item
     */
    public String selectRandomItem() {
        String selected = "";
        int random = MathUtils.random(0, allItems.size() - 1);
        selected = allItems.get(random);

        return selected;
    }

    /**
     * Create BankUpgrade items. Create map for them and add them to allItems array.
     */
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

    /**
     * Create HealLiquid items. Create map for them and add them to allItems array.
     */
    private void itemHealLiquid() {
        double[] values = new double[] {0.0, -15.0, -35.0, -70.0};
        for (int i = 1; i <= 3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, HEAL_LIQUID + index);
            map.put(description, "healLiquidDesc" + index);
            map.put(value, values[i]);
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

    /**
     * Create GlitterBomb items. Create map for them and add them to allItems array.
     */
    private void itemGlitterBomb() {
        double[] damages = new double[] {0.0, 1.0, 1.5, 2.0};
        for (int i = 1; i <= 3; i++) {
            String index = String.valueOf(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put(name, GLITTER_BOMB + index);
            map.put(description, "glitterBombDesc" + index);
            map.put(value, damages[i]);
            map.put(usedInHall, false);
            map.put(isSkill, false);
            map.put(itemType, TYPE_BOMB);
            map.put(boostType, NO_BOOST);
            map.put(price, basePrice * i);
            map.put(isPermanent, false);

            allItems.add((String) map.get(name));
            mapItems.put((String) map.get(name), map);
        }
    }

    /**
     * Create Overclock items. Create map for them and add them to allItems array.
     */
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

    /**
     * Create LongScope items. Create map for them and add them to allItems array.
     */
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

    /**
     * Create PowerPotion items. Create map for them and add them to allItems array.
     */
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

    /**
     * Create IronCase items. Create map for them and add them to allItems array.
     */
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

    /**
     * Create RepairBot items. Create map for them and add them to allItems array.
     */
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

    /**
     * Create SuperVision item. Create map for it and add it to allItems array.
     */
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

    /**
     * Create Experimental item. Create map for it and add it to allItems array.
     */
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

    /**
     * Create Hardware item. Create map for it and add it to allItems array.
     */
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

    /**
     * Create DiamondCoat item. Create map for it and add it to allItems array.
     */
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

    /**
     * Create HealCoolant item. Create map for it and add it to allItems array.
     */
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

    /**
     * Create Reflect item. Create map for it and add it to allItems array.
     */
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

    /**
     * Retrieve all items in an array.
     * @return all items in an array
     */
    public String[] getAllItems() {
        String[] converted = allItems.toArray(new String[0]);
        return converted;
    }
}
