package fi.tamk.fi;

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

    private ImageButton buttonStats;
    private ImageButton buttonInventory;
    private ImageButton buttonSettings;
    private ImageButton buttonExit;

    // Headers
    private Label header;
    private Label name;
    private Label allSteps;
    private Label buyedItems;
    private Label ownedSkills;
    private Label bossesDefeated;

    private Label labelPlayerName;
    private Label labelBuyedItems;
    private Label labelAllSteps;
    private Label labelOwnedSkills;
    private Label labelBossesDefeated;

    private float labelWidth = 600;
    private float spaceBetween = 30;
    private int ownedSkillAmount;

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

        ownedSkillsAmount();
        createStatsDialog();
        createHeader();
        createPlayerName();
        createAllSteps();
        createBuyedItems();
        createOwnedSkills();
        createBossesDefeated();
        createMenuButtons();
        createExitButton();
        stage.addActor(statsDialog);
    }

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

    private void createStatsDialog() {
        statsDialog = new Dialog("", finalSkin, "stats");
        statsDialog.setMovable(false);
        statsDialog.setKeepWithinStage(false);
        statsDialog.setPosition(0,0);
        statsDialog.setSize(game.pixelWidth, game.pixelHeight);
    }

    private void createHeader() {
        header = new Label(localize.get("stats"), finalSkin, "big");
        header.setPosition(60,
                statsDialog.getHeight() - 200);
        header.setSize(1435, header.getPrefHeight());
        header.setAlignment(1);

        statsDialog.addActor(header);
    }

    private void createPlayerName() {
        name = new Label(localize.get("name") + ":", finalSkin);
        name.setPosition(header.getX() + 110, header.getY() - 150);
        name.setSize(labelWidth, name.getPrefHeight());
        name.setAlignment(Align.right);
        statsDialog.addActor(name);

        labelPlayerName = new Label(playerName, finalSkin);
        labelPlayerName.setPosition(name.getX(Align.right) + spaceBetween, name.getY());
        labelPlayerName.setSize(labelWidth, name.getPrefHeight());
        labelPlayerName.setAlignment(Align.left);
        statsDialog.addActor(labelPlayerName);
    }

    private void createAllSteps() {
        allSteps = new Label(localize.get("steps") + ":", finalSkin);
        allSteps.setPosition(name.getX(), name.getY() - 100);
        allSteps.setSize(labelWidth, allSteps.getPrefHeight());
        allSteps.setAlignment(Align.right);
        statsDialog.addActor(allSteps);

        labelAllSteps = new Label(String.valueOf((int)stepAllCount), finalSkin);
        labelAllSteps.setPosition(allSteps.getX(Align.right) + spaceBetween, allSteps.getY());
        labelAllSteps.setSize(labelWidth, allSteps.getPrefHeight());
        labelAllSteps.setAlignment(Align.left);
        statsDialog.addActor(labelAllSteps);
    }

    private void createBuyedItems() {
        buyedItems = new Label(localize.get("buyedItems") + ":", finalSkin);
        buyedItems.setPosition(name.getX(), allSteps.getY() - 100);
        buyedItems.setSize(labelWidth, buyedItems.getPrefHeight());
        buyedItems.setAlignment(Align.right);
        statsDialog.addActor(buyedItems);

        labelBuyedItems = new Label(String.valueOf(buyedItemsCounter), finalSkin);
        labelBuyedItems.setPosition(buyedItems.getX(Align.right) + spaceBetween, buyedItems.getY());
        labelBuyedItems.setSize(labelWidth, labelBuyedItems.getPrefHeight());
        labelBuyedItems.setAlignment(Align.left);
        statsDialog.addActor(labelBuyedItems);
    }

    private void createOwnedSkills() {
        ownedSkills = new Label(localize.get("ownedSkills") + ":", finalSkin);
        ownedSkills.setPosition(name.getX(), buyedItems.getY() - 100);
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

    private void createBossesDefeated() {
        bossesDefeated = new Label(localize.get("bossesDefeated") + ":", finalSkin);
        bossesDefeated.setPosition(name.getX(), ownedSkills.getY() - 100);
        bossesDefeated.setSize(labelWidth, bossesDefeated.getPrefHeight());
        bossesDefeated.setAlignment(Align.right);
        statsDialog.addActor(bossesDefeated);

        labelBossesDefeated = new Label(String.valueOf(fightsWon), finalSkin);
        labelBossesDefeated.setPosition(bossesDefeated.getX(Align.right) + spaceBetween, bossesDefeated.getY());
        labelBossesDefeated.setSize(labelWidth, labelBossesDefeated.getPrefHeight());
        labelBossesDefeated.setAlignment(Align.left);
        statsDialog.addActor(labelBossesDefeated);
    }

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
}
