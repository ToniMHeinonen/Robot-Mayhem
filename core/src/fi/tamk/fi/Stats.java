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

public class Stats {
    private MainGame game;
    private Skin finalSkin;
    private Stage stage;
    private String room;
    private RoomParent curRoom;
    private I18NBundle localize;

    private Dialog statsDialog;

    private ImageButton buttonStats;
    private ImageButton buttonInventory;
    private ImageButton buttonSettings;
    private ImageButton buttonExit;

    private Label header;
    private Label name;
    private Label allSteps;
    private Label buyedItems;
    private Label ownedSkills;
    private Label bossesDefeated;

    private float labelWidth = 600;

    Stats (MainGame game, String room, RoomParent curRoom) {
        this.game = game;
        this.room = room;
        this.curRoom = curRoom;
        finalSkin = game.getFinalSkin();
        stage = game.getStage();
        localize = game.getLocalize();

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
    }

    private void createAllSteps() {
        allSteps = new Label(localize.get("steps") + ":", finalSkin);
        allSteps.setPosition(name.getX(), name.getY() - 100);
        allSteps.setSize(labelWidth, allSteps.getPrefHeight());
        allSteps.setAlignment(Align.right);
        statsDialog.addActor(allSteps);
    }

    private void createBuyedItems() {
        buyedItems = new Label(localize.get("buyedItems") + ":", finalSkin);
        buyedItems.setPosition(name.getX(), allSteps.getY() - 100);
        buyedItems.setSize(labelWidth, buyedItems.getPrefHeight());
        buyedItems.setAlignment(Align.right);
        statsDialog.addActor(buyedItems);
    }

    private void createOwnedSkills() {
        ownedSkills = new Label(localize.get("ownedSkills") + ":", finalSkin);
        ownedSkills.setPosition(name.getX(), buyedItems.getY() - 100);
        ownedSkills.setSize(labelWidth, ownedSkills.getPrefHeight());
        ownedSkills.setAlignment(Align.right);
        statsDialog.addActor(ownedSkills);
    }

    private void createBossesDefeated() {
        bossesDefeated = new Label(localize.get("bossesDefeated") + ":", finalSkin);
        bossesDefeated.setPosition(name.getX(), ownedSkills.getY() - 100);
        bossesDefeated.setSize(labelWidth, bossesDefeated.getPrefHeight());
        bossesDefeated.setAlignment(Align.right);
        statsDialog.addActor(bossesDefeated);
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
