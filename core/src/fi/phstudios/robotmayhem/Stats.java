package fi.phstudios.robotmayhem;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;

public class Stats {
    private MainGame game;
    private Skin finalSkin;
    private Stage stage;
    private String room;
    private RoomParent curRoom;
    private I18NBundle localize;
    private String playerName;
    private int buyedItemsCounter;
    private float stepAllCount;
    private String skill1;
    private String skill2;
    private ArrayList<String> inventory;
    private Skills skills;
    private String[] allSkills;
    private int fightsWon;

    private Dialog statsDialog;

    private ImageButton buttonStats, buttonInventory, buttonSettings, buttonExit;

    // Headers
    private Label header, name, allSteps, buyedItems, ownedSkills, bossesDefeated,
            boosts, critical, miss, damage, armor, heal;

    // Variables
    private Label labelPlayerName, labelBuyedItems, labelAllSteps, labelOwnedSkills, labelBossesDefeated,
            labelCritical, labelMiss, labelDamage, labelArmor, labelHeal;

    private int critBoost;
    private int missBoost;
    private float dmgBoost;
    private float armorBoost;
    private float healBoost;

    private float labelWidth = 600;
    private float spaceBetween = 30;
    private int ownedSkillAmount;

    /**
     * Initialize all the basic values.
     * @param game used for retrieving variables
     * @param room room, where player is coming from
     * @param curRoom currentroom
     */
    Stats (MainGame game, String room, RoomParent curRoom) {
        this.game = game;
        this.room = room;
        this.curRoom = curRoom;
        finalSkin = game.getFinalSkin();
        stage = game.getStage();
        localize = game.getLocalize();
        playerName = game.getPlayerName();
        buyedItemsCounter = game.getBuyedItemsCounter();
        stepAllCount = game.getStepAllCount();
        skill1 = game.getSkill1();
        skill2 = game.getSkill2();
        inventory = game.getInventory();
        skills = game.getSkills();
        allSkills = skills.getAllSkills();
        fightsWon = game.getFightsWon();
        // If room is fight, retrieve player's current boost values
        if (room.equals("fight")) {
            critBoost = game.getOverallBstCrit();
            missBoost = game.getOverallBstMiss();
            dmgBoost = game.getOverallBstDmg();
            armorBoost = game.getOverallBstArmor();
            healBoost = game.getOverallBstHeal();
        } else {
            critBoost = game.getCritBoost();
            missBoost = game.getMissBoost();
            dmgBoost = game.getDmgBoost();
            armorBoost = game.getArmorBoost();
            healBoost = game.getHealBoost();
        }

        ownedSkillsAmount();
        createStatsDialog();
        createHeader();
        createPlayerName();
        createAllSteps();
        createBuyedItems();
        createOwnedSkills();
        createBossesDefeated();
        createBoosts();
        createMenuButtons();
        createExitButton();
        createAchievementButton();
        stage.addActor(statsDialog);
    }

    /**
     * Get amount of owned skills.
     */
    private void ownedSkillsAmount() {
        // Attack & Defend
        ownedSkillAmount = 2;

        for (int i = 0; i < allSkills.length; i++) {
            if (allSkills[i].equals(skill1)) ownedSkillAmount += 1;
            if (allSkills[i].equals(skill2)) ownedSkillAmount += 1;
            for (int j = 0; j < inventory.size(); j++) {
                if (inventory.get(j).equals(allSkills[i])) ownedSkillAmount += 1;
            }
        }
    }

    /**
     * Create stats-dialog.
     */
    private void createStatsDialog() {
        statsDialog = new Dialog("", finalSkin, "stats");
        statsDialog.setMovable(false);
        statsDialog.setKeepWithinStage(false);
        statsDialog.setPosition(0,0);
        statsDialog.setSize(game.pixelWidth, game.pixelHeight);
    }

    /**
     * Create header.
     */
    private void createHeader() {
        header = new Label(localize.get("stats"), finalSkin, "big");
        header.setPosition(60,
                statsDialog.getHeight() - 200);
        header.setSize(1435, header.getPrefHeight());
        header.setAlignment(1);

        statsDialog.addActor(header);
    }

    /**
     * Create playername.
     */
    private void createPlayerName() {
        name = new Label(localize.get("name") + ":", finalSkin);
        name.setPosition(header.getX() + 110, header.getY() - 100);
        name.setSize(labelWidth, name.getPrefHeight());
        name.setAlignment(Align.right);
        statsDialog.addActor(name);

        labelPlayerName = new Label(playerName, finalSkin);
        labelPlayerName.setPosition(name.getX(Align.right) + spaceBetween, name.getY());
        labelPlayerName.setSize(labelWidth, name.getPrefHeight());
        labelPlayerName.setAlignment(Align.left);
        statsDialog.addActor(labelPlayerName);
    }

    /**
     * Create allsteps.
     */
    private void createAllSteps() {
        allSteps = new Label(localize.get("steps") + ":", finalSkin);
        allSteps.setPosition(name.getX(), name.getY() - 75);
        allSteps.setSize(labelWidth, allSteps.getPrefHeight());
        allSteps.setAlignment(Align.right);
        statsDialog.addActor(allSteps);

        labelAllSteps = new Label(String.valueOf((int)stepAllCount), finalSkin);
        labelAllSteps.setPosition(allSteps.getX(Align.right) + spaceBetween, allSteps.getY());
        labelAllSteps.setSize(labelWidth, allSteps.getPrefHeight());
        labelAllSteps.setAlignment(Align.left);
        statsDialog.addActor(labelAllSteps);
    }

    /**
     * Create buyed items.
     */
    private void createBuyedItems() {
        buyedItems = new Label(localize.get("buyedItems") + ":", finalSkin);
        buyedItems.setPosition(name.getX(), allSteps.getY() - 75);
        buyedItems.setSize(labelWidth, buyedItems.getPrefHeight());
        buyedItems.setAlignment(Align.right);
        statsDialog.addActor(buyedItems);

        labelBuyedItems = new Label(String.valueOf(buyedItemsCounter), finalSkin);
        labelBuyedItems.setPosition(buyedItems.getX(Align.right) + spaceBetween, buyedItems.getY());
        labelBuyedItems.setSize(labelWidth, labelBuyedItems.getPrefHeight());
        labelBuyedItems.setAlignment(Align.left);
        statsDialog.addActor(labelBuyedItems);
    }

    /**
     * Create owned skills.
     */
    private void createOwnedSkills() {
        ownedSkills = new Label(localize.get("ownedSkills") + ":", finalSkin);
        ownedSkills.setPosition(name.getX(), buyedItems.getY() - 75);
        ownedSkills.setSize(labelWidth, ownedSkills.getPrefHeight());
        ownedSkills.setAlignment(Align.right);
        statsDialog.addActor(ownedSkills);

        labelOwnedSkills = new Label(String.valueOf(ownedSkillAmount) + "/" +
                String.valueOf(allSkills.length-1),
                finalSkin);
        labelOwnedSkills.setPosition(ownedSkills.getX(Align.right) + spaceBetween, ownedSkills.getY());
        labelOwnedSkills.setSize(labelWidth, labelOwnedSkills.getPrefHeight());
        labelOwnedSkills.setAlignment(Align.left);
        statsDialog.addActor(labelOwnedSkills);
    }

    /**
     * Create bosses defeated.
     */
    private void createBossesDefeated() {
        bossesDefeated = new Label(localize.get("bossesDefeated") + ":", finalSkin);
        bossesDefeated.setPosition(name.getX(), ownedSkills.getY() - 75);
        bossesDefeated.setSize(labelWidth, bossesDefeated.getPrefHeight());
        bossesDefeated.setAlignment(Align.right);
        statsDialog.addActor(bossesDefeated);

        labelBossesDefeated = new Label(String.valueOf(fightsWon), finalSkin);
        labelBossesDefeated.setPosition(bossesDefeated.getX(Align.right) + spaceBetween, bossesDefeated.getY());
        labelBossesDefeated.setSize(labelWidth, labelBossesDefeated.getPrefHeight());
        labelBossesDefeated.setAlignment(Align.left);
        statsDialog.addActor(labelBossesDefeated);
    }

    /**
     * Create boosts.
     */
    private void createBoosts() {
        boosts = new Label(localize.get("boosts") + ":", finalSkin);
        boosts.setPosition(name.getX(), bossesDefeated.getY() - 75);
        boosts.setSize(labelWidth, boosts.getPrefHeight());
        boosts.setAlignment(Align.right);
        statsDialog.addActor(boosts);

        damage = new Label(localize.get("damage") + ":", finalSkin, "font38");
        damage.setPosition(name.getX(), boosts.getY() - 50);
        damage.setSize(labelWidth, damage.getPrefHeight());
        damage.setAlignment(Align.right);
        statsDialog.addActor(damage);

        labelDamage = new Label(String.format("%.0f", dmgBoost*100) + " %", finalSkin, "font38");
        labelDamage.setPosition(damage.getX(Align.right) + spaceBetween, damage.getY());
        labelDamage.setSize(labelWidth, labelDamage.getPrefHeight());
        labelDamage.setAlignment(Align.left);
        statsDialog.addActor(labelDamage);

        critical = new Label(localize.get("critical") + ":", finalSkin, "font38");
        critical.setPosition(damage.getX(), damage.getY() - 50);
        critical.setSize(labelWidth, critical.getPrefHeight());
        critical.setAlignment(Align.right);
        statsDialog.addActor(critical);

        labelCritical = new Label(String.valueOf(critBoost) + " %", finalSkin, "font38");
        labelCritical.setPosition(critical.getX(Align.right) + spaceBetween, critical.getY());
        labelCritical.setSize(labelWidth, labelCritical.getPrefHeight());
        labelCritical.setAlignment(Align.left);
        statsDialog.addActor(labelCritical);

        miss = new Label(localize.get("miss")+ ":", finalSkin, "font38");
        miss.setPosition(damage.getX(), critical.getY() - 50);
        miss.setSize(labelWidth, miss.getPrefHeight());
        miss.setAlignment(Align.right);
        statsDialog.addActor(miss);

        labelMiss = new Label("-" + String.valueOf(missBoost) + " %", finalSkin, "font38");
        labelMiss.setPosition(miss.getX(Align.right) + spaceBetween, miss.getY());
        labelMiss.setSize(labelWidth, labelMiss.getPrefHeight());
        labelMiss.setAlignment(Align.left);
        statsDialog.addActor(labelMiss);

        armor = new Label(localize.get("armor") + ":", finalSkin, "font38");
        armor.setPosition(damage.getX(), miss.getY() - 50);
        armor.setSize(labelWidth, armor.getPrefHeight());
        armor.setAlignment(Align.right);
        statsDialog.addActor(armor);

        labelArmor = new Label(String.format("%.0f", armorBoost*100) + " %", finalSkin, "font38");
        labelArmor.setPosition(armor.getX(Align.right) + spaceBetween, armor.getY());
        labelArmor.setSize(labelWidth, labelArmor.getPrefHeight());
        labelArmor.setAlignment(Align.left);
        statsDialog.addActor(labelArmor);

        heal = new Label(localize.get("heal") + ":", finalSkin, "font38");
        heal.setPosition(damage.getX(), armor.getY() - 50);
        heal.setSize(labelWidth, heal.getPrefHeight());
        heal.setAlignment(Align.right);
        statsDialog.addActor(heal);

        labelHeal = new Label(String.format("%.0f", healBoost*100) + " %", finalSkin, "font38");
        labelHeal.setPosition(heal.getX(Align.right) + spaceBetween, heal.getY());
        labelHeal.setSize(labelWidth, labelHeal.getPrefHeight());
        labelHeal.setAlignment(Align.left);
        statsDialog.addActor(labelHeal);
    }

    /**
     * Create menu buttons.
     */
    private void createMenuButtons() {
        buttonSettings = new ImageButton(finalSkin.getDrawable("menu_settings1"));
        buttonSettings.setPosition(1440, 720);
        buttonSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Settings settings = new Settings(game, room, curRoom);
                statsDialog.remove();
            }
        });

        buttonInventory = new ImageButton(finalSkin.getDrawable("menu_items1"));
        buttonInventory.setPosition(buttonSettings.getX(),
                buttonSettings.getY() - 180);
        buttonInventory.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                UtilItem utilItem = new UtilItem(game, room, curRoom);
                statsDialog.remove();
            }
        });

        buttonStats = new ImageButton(finalSkin, "stats");
        buttonStats.setPosition(buttonSettings.getX(),
                buttonInventory.getY() - 180);

        statsDialog.addActor(buttonSettings);
        statsDialog.addActor(buttonInventory);
        statsDialog.addActor(buttonStats);
    }

    /**
     * Create exit-button.
     */
    private void createExitButton() {
        buttonExit = new ImageButton(finalSkin, "x");
        buttonExit.setPosition(1550, 960);
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                statsDialog.remove();
            }
        });

        statsDialog.addActor(buttonExit);
    }

    private void createAchievementButton() {
        ImageButton imgBtn = new ImageButton(finalSkin, "achievement");
        imgBtn.setPosition(1120, 70);
        statsDialog.addActor(imgBtn);
        imgBtn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Achievements achievements = new Achievements(game);
            }
        });
    }
}
