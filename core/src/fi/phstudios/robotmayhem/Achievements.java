package fi.phstudios.robotmayhem;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;

public class Achievements {
    private MainGame game;
    private Skin finalSkin;
    private Stage stage;
    private String room;
    private RoomParent curRoom;
    private I18NBundle localize;
    private float stepAllCount;
    private String lan;
    private boolean finishedGame, finishedGameHard, resetedGame;
    private Item items;
    private String[] allItems;
    private Skills skills;
    private String[] allSkills;
    private String skill1;
    private String skill2;
    private ArrayList<String> inventory;
    private Files files;
    private boolean reflectiveShield;
    private int fightsWon;
    private int buyedItemsCounter;

    private Dialog dialogAch;
    private ImageButton btnCancel, btnCollect;

    private String[] achHeaders;
    private String[] achDescriptions;
    private int[] achMoney;

    private float space = 200f;
    float posX = 370;
    float posY = 600;
    private int permanentCounter, positionCounter;
    int ownedSkillAmount;

    /**
     * Initialize all the basic values.
     * @param game used for retrieving variables
     */
    Achievements(MainGame game) {
        this.game = game;
        items = game.getItems();
        allItems = items.getAllItems();
        stage = game.getStage();
        finalSkin = game.getFinalSkin();
        stepAllCount = game.getStepAllCount();
        lan = game.getLanguage();
        localize = game.getLocalize();
        finishedGame = game.isFinishedGame();
        finishedGameHard = game.isFinishedGameHard();
        skills = game.getSkills();
        allSkills = skills.getAllSkills();
        skill1 = game.getSkill1();
        skill2 = game.getSkill2();
        inventory = game.getInventory();
        files = game.getFiles();
        resetedGame = game.isResetedGame();
        reflectiveShield = game.isReflectiveShield();
        fightsWon = game.getFightsWon();
        buyedItemsCounter = game.getBuyedItemsCounter();

        createAchDialog();
        createHeadersAndDescriptions();
        checkAchievements();
        createButtons();
        createExitButton();
        stage.addActor(dialogAch);
        game.saveAchievements();
    }

    /**
     * Create achievement-dialog.
     */
    private void createAchDialog() {
        dialogAch = new Dialog("", finalSkin, "stats");
        dialogAch.setMovable(false);
        dialogAch.setPosition(0,0);
        dialogAch.setSize(game.pixelWidth, game.pixelHeight);

        Label labelAch = new Label(localize.get("achievements"), finalSkin, "big");
        labelAch.setPosition(60, dialogAch.getHeight() - 200);
        labelAch.setSize(1435, labelAch.getPrefHeight());
        labelAch.setAlignment(1);
        dialogAch.addActor(labelAch);
    }

    /**
     * Create achievements' headers, descriptions and rewards.
     */
    private void createHeadersAndDescriptions() {
        achHeaders = new String[] {
                localize.get("sundayWalker"),
                localize.get("jogger"),
                localize.get("marathonist"),
                localize.get("finisher"),
                localize.get("pepperyWalker"),
                localize.get("materialist"),
                localize.get("jackOfAllTrades"),
                localize.get("cleanSlate"),
                "Robot Mayhem",
                localize.get("crusader"),
                localize.get("tripAroundTheWorld"),
                localize.get("hoarder")};

        achDescriptions = new String[] {
                localize.get("sundayWalkerDesc"),
                localize.get("joggerDesc"),
                localize.get("marathonistDesc"),
                localize.get("finisherDesc"),
                localize.get("pepperyWalkerDesc"),
                localize.get("materialistDesc"),
                localize.get("jackOfAllTradesDesc"),
                localize.get("cleanSlateDesc"),
                localize.get("robotMayhemDesc"),
                localize.get("crusaderDesc"),
                localize.get("tripAroundTheWorldDesc"),
                localize.get("hoarderDesc")};

        achMoney = new int[] {
                5,      // Sunday Walker / 50 steps
                30,     // Jogger / 5 000 steps
                50,     // Marathonist / 10 000 steps
                80,     // Finisher / Finish the game
                100,    // Peppery Walker / Finish the game on hard mode
                50,     // Materialist / Buy every permanent item
                10000,  // Jack Of All Trades / Own every skill
                10,     // Clean Slate / Reset the game
                200,    // Robot Mayhem / Defeat 38 bosses
                50,     // Crusader / 50 000 steps
                150,    // Trip Around The World / 100 000 steps
                123     // Hoarder / Buy 30 items
        };
    }

    /**
     * Check, if achievements are completed.
     */
    private void checkAchievements() {
        // REMEMBER to change default-value in MainGame -> loadAchievements()

        // Achievement 0 / Sunday Walker / Walk 50 steps
        if (stepAllCount >= 50) game.setAchievement(0, "unlocked");

        // Achievement 1 / Jogger / Walk 5 000 steps
        if (stepAllCount >= 5000) game.setAchievement(1, "unlocked");

        // Achievement 2 / Marathonist / Walk 10 000 steps
        if (stepAllCount >= 10000) game.setAchievement(2, "unlocked");

        // Achievement 3 / Finisher / Finish the game
        if (finishedGame) game.setAchievement(3, "unlocked");

        // Achievement 4 / Peppery Walker / Finish the game on hard mode
        if (finishedGameHard) game.setAchievement(4, "unlocked");

        // Achievement 5 / Materialist / Buy every permanent item
        for (int i = 0; i < allItems.length; i++) {
            String item  = allItems[i];
            if (items.getItem(item).get(items.isPermanent).equals(true)) {
                permanentCounter++;
            }
        }

        if (permanentCounter == game.getBoughtPermanent().size()) {
            game.setAchievement(5, "unlocked");
        }

        // Achievement 6 / Jack Of All Trades / Own every skill
        ownedSkillAmount = 2;
        if (reflectiveShield) ownedSkillAmount += 1;

        for (int i = 0; i < allSkills.length; i++) {
            if (allSkills[i].equals(skill1)) ownedSkillAmount += 1;
            if (allSkills[i].equals(skill2)) ownedSkillAmount += 1;
            for (int j = 0; j < inventory.size(); j++) {
                if (inventory.get(j).equals(allSkills[i])) ownedSkillAmount += 1;
            }
        }

        if (allSkills.length-1 == ownedSkillAmount) game.setAchievement(6, "unlocked");

        // Achievement 7 / Clean Slate / Reset the game.
        if (resetedGame) game.setAchievement(7, "unlocked");

        // Achievement 8 / Robot Mayhem / Defeat 38 bosses.
        if (fightsWon >= 38) game.setAchievement(8, "unlocked");

        // Achievement 9 / Crusader / 50 000 steps.
        if (stepAllCount >= 50000) game.setAchievement(9, "unlocked");

        // Achievement 10 / Trip Around The World / 100 000 steps
        if (stepAllCount >= 100000) game.setAchievement(10, "unlocked");

        // Achievement 11 / Hoarder / Buy 30 items.
        if (buyedItemsCounter >= 30) game.setAchievement(11, "unlocked");
    }

    /**
     * Create achievement buttons.
     */
    private void createButtons() {
        for (int i = 0; i < achHeaders.length; i++) {
            final int btnCounter = i;
            ImageButton imgBtn = new ImageButton(finalSkin, game.getAchievComplete().get(i));
            imgBtn.setPosition(posX + positionCounter*space, posY);
            positionCounter++;
            if (i==3) {
                posY = 400;
                positionCounter = 0;
            }
            if (i==7) {
                posY = 200;
                positionCounter = 0;
            }
            dialogAch.addActor(imgBtn);
            imgBtn.addListener(new ClickListener(){
                int i = btnCounter;
                @Override
                public void clicked(InputEvent event, float x, float y){
                    popupAchievement(i);
                }
            });
        }
    }

    /**
     * This will open, when player has clicked one of the achievements.
     * @param index achievement-index
     */
    private void popupAchievement(final int index) {
        Label label = new Label(achDescriptions[index] + " " + localize.get("reward") + " " +
                String.valueOf(achMoney[index]) + " " + localize.get("shinyCoins"), finalSkin, "font46");
        label.setWrap(true);
        label.setAlignment(1);

        final Dialog dialog = new Dialog("", finalSkin, "popup_powerup");
        dialog.getContentTable().add(label).prefWidth(720);
        dialog.setPosition(0,0);
        dialog.setSize(game.pixelWidth, game.pixelHeight);

        Label header = new Label(achHeaders[index], finalSkin);
        header.setPosition(game.pixelWidth / 4, game.pixelHeight / 2 + 195);
        header.setSize(960, 100);
        header.setAlignment(1);
        dialog.addActor(header);

        String stringLocked = localize.get("locked");
        if (game.getAchievComplete().get(index).equals("unlocked")) stringLocked = localize.get("unlocked");

        Label locked = new Label(stringLocked, finalSkin);
        locked.setPosition(game.pixelWidth / 4, 350);
        locked.setSize(960, 100);
        locked.setAlignment(1);
        dialog.addActor(locked);

        String stringCollect = "collect_" + lan;
        if (game.getHasCollected().get(index).equals("true")) {
            stringCollect = "collected_" + lan;
        }
        btnCollect = new ImageButton(finalSkin, stringCollect);
        btnCollect.setPosition(dialog.getWidth()/2 - 400, dialog.getHeight()/4 - 55);
        btnCollect.setDisabled(true);
        dialog.addActor(btnCollect);
        if (game.getHasCollected().get(index).equals("false") && game.getAchievComplete().get(index).equals("unlocked")) {
            btnCollect.setDisabled(false);
            btnCollect.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setHasCollected(index, "true");
                    game.addMoney(achMoney[index]);
                    game.saveAchievements();
                    game.playSound(files.sndPurchaseItem);
                    dialog.remove();
                }
            });
        }

        btnCancel = new ImageButton(finalSkin, "cancel_" + lan);
        btnCancel.setPosition(btnCollect.getX() + 445, btnCollect.getY());
        dialog.addActor(btnCancel);
        btnCancel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.remove();
            }
        });

        dialogAch.addActor(dialog);
    }

    /**
     * Create exit-button.
     */
    private void createExitButton() {
        ImageButton buttonExit = new ImageButton(finalSkin, "x");
        buttonExit.setPosition(1550, 960);
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.saveAchievements();
                dialogAch.remove();
            }
        });

        dialogAch.addActor(buttonExit);
    }
}
