package fi.phstudios.robotmayhem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;

public class Settings {
    private MainGame game;
    private RoomParent curRoom;
    private I18NBundle localize;
    private Files files;
    private Skin skin;
    private Stage stage;
    private Skin finalSkin;
    private String room;
    private String lan;
    private ArrayList<String> inventory;
    private UtilDialog dialog;
    private Skills skills;
    private String difficulty;

    private float posX;
    private float onScreenY;

    private TextButton settingsRoomButton;

    private Dialog settingsDialog;
    private Label header;

    // Music
    private Slider musicVolSlider;
    private Label musicVolLabel;

    // Sound
    private Slider soundVolSlider;
    private Label soundVolLabel;

    // Difficulty
    private Label difficultyLabel;
    private String[] difficulties;
    private ImageButton[] difficultyButtons = new ImageButton[3];
    private int space = 310;

    // Quit and Reset
    private ImageButton buttonQuit;
    private ImageButton buttonReset;
    private Dialog confirmation1;
    private Dialog confirmation2;
    private ImageButton btnYes;
    private ImageButton btnNo;
    private Dialog popupQuit;

    // Language
    private ImageButton buttonFi;
    private ImageButton buttonEn;

    // Menu
    private ImageButton buttonSettings;
    private ImageButton buttonInventory;
    private ImageButton buttonStats;

    // Exit
    private ImageButton buttonExit;

    Settings(MainGame game, String room, RoomParent curRoom) {
        this.game = game;
        this.room = room;
        this.curRoom = curRoom;
        difficulties = new String[] {game.EASY, game.MEDIUM, game.HARD};
        localize = game.getLocalize();
        files = game.getFiles();
        skin = game.getSkin();
        stage = game.getStage();
        finalSkin = game.getFinalSkin();
        lan = game.getLanguage();
        dialog = game.getDialog();
        inventory = game.getInventory();
        skills = game.getSkills();
        difficulty = game.getDifficulty();

        setValues();
        createSettingsDialog();
        createHeader();
        createMusicVolume();
        createSoundVolume();
        createDifficultyButtons();
        createQuitAndResetButtons();
        createLanguageButtons();
        createMenuButtons();
        createExitButton();
        createSettingsRoomButton();
        stage.addActor(settingsDialog);
        System.out.println("Setting-dialog opened from room: " + room);
    }

    private void setValues() {
        posX = 0;
        onScreenY = 0;
    }

    private void createSettingsDialog() {
        settingsDialog = new Dialog("", finalSkin, "settings");
        settingsDialog.setMovable(false);
        settingsDialog.setKeepWithinStage(false);
        settingsDialog.setPosition(posX, onScreenY);
        settingsDialog.setSize(game.pixelWidth, game.pixelHeight);
    }

    private void createHeader() {
        header = new Label(localize.get("settings"), finalSkin, "big");
        header.setPosition(60,
                settingsDialog.getHeight() - 200);
        header.setSize(1435, header.getPrefHeight());
        header.setAlignment(1);

        settingsDialog.addActor(header);
    }

    private void createMusicVolume() {
        musicVolSlider = new Slider(0.0f, 1.0f, 0.1f, false, finalSkin);
        musicVolSlider.setValue(game.getMusicVol());
        musicVolSlider.setPosition(500,
                header.getY() - 160);
        musicVolSlider.setSize(900, 120);
        musicVolSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setMusicVol(musicVolSlider.getValue());
            }
        });

        musicVolLabel = new Label(localize.get("music"), finalSkin);
        musicVolLabel.setPosition(musicVolSlider.getX() - 365,
                musicVolSlider.getY() + 35);
        musicVolLabel.setSize(368, 62);
        musicVolLabel.setAlignment(Align.right);

        settingsDialog.addActor(musicVolSlider);
        settingsDialog.addActor(musicVolLabel);
    }

    private void createSoundVolume() {
        soundVolSlider = new Slider(0.0f, 1.0f, 0.1f, false, finalSkin);
        soundVolSlider.setValue(game.getSoundVol());
        soundVolSlider.setPosition(musicVolSlider.getX(),
                musicVolSlider.getY() - 120);
        soundVolSlider.setSize(900, 120);
        soundVolSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setSoundVol(soundVolSlider.getValue());
                game.playSound(files.sndNotification);
            }
        });

        soundVolLabel = new Label(localize.get("sound"), finalSkin);
        soundVolLabel.setPosition(musicVolSlider.getX() - 365,
                soundVolSlider.getY() + 35);
        soundVolLabel.setSize(368, 62);
        soundVolLabel.setAlignment(Align.right);

        settingsDialog.addActor(soundVolSlider);
        settingsDialog.addActor(soundVolLabel);
    }

    private void createDifficultyButtons() {
        for (int i = 0; i < difficultyButtons.length; i++) {
            final int difficultyCounter = i;
            difficultyButtons[i] = new ImageButton(finalSkin, difficulties[i]);
            difficultyButtons[i].setPosition(musicVolSlider.getX() + 15 + i* space,
                    soundVolSlider.getY() - 125);
            if (difficulty.equals(difficulties[i])) {
                difficultyButtons[i].setChecked(true);
            }
            difficultyButtons[i].addListener(new ClickListener(){
                int i = difficultyCounter;
                @Override
                public void clicked(InputEvent event, float x, float y){
                    for (ImageButton button : difficultyButtons) {
                        button.setChecked(false);
                    }
                    difficultyButtons[i].setChecked(true);
                    game.changeDifficulty(difficulties[i]);
                    //game.setDifficulty(difficulties[i]);
                }
            });
            settingsDialog.addActor(difficultyButtons[i]);
        }

        difficultyLabel = new Label(localize.get("difficulty"), finalSkin);
        difficultyLabel.setPosition(musicVolSlider.getX() - 365,
                soundVolLabel.getY() - 120);
        difficultyLabel.setSize(368, difficultyLabel.getPrefHeight());
        difficultyLabel.setAlignment(Align.right);

        settingsDialog.addActor(difficultyLabel);
    }

    private void createQuitAndResetButtons() {
        buttonQuit = new ImageButton(finalSkin, "quit_" + lan);
        buttonQuit.setPosition(difficultyLabel.getX() - 20,
                120);
        buttonQuit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                askQuit();
            }
        });

        buttonReset = new ImageButton(finalSkin, "reset_" + lan);
        buttonReset.setPosition(buttonQuit.getX() + buttonReset.getWidth(),
                buttonQuit.getY());
        buttonReset.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                askConfrimation1();
            }
        });

        settingsDialog.addActor(buttonQuit);
        settingsDialog.addActor(buttonReset);
    }

    private void askConfrimation1() {
        confirmation1 = dialog.createPopupItemAndPowerUp(localize.get("reset"),
                localize.get("resetConf1"), "popup_powerup");
        createYesAndNo(confirmation1);
        confirmation1.addActor(btnYes);
        confirmation1.addActor(btnNo);
        settingsDialog.addActor(confirmation1);
        btnYes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                askConfirmation2();
            }
        });

        btnNo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                confirmation1.remove();
            }
        });
    }

    private void askConfirmation2() {
        confirmation2 = dialog.createPopupItemAndPowerUp(localize.get("reset"),
                localize.get("resetConf2"), "popup_powerup");
        createYesAndNo(confirmation2);
        confirmation2.addActor(btnYes);
        confirmation2.addActor(btnNo);
        settingsDialog.addActor(confirmation2);
        btnYes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.resetGame();
            }
        });
        btnNo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                confirmation2.remove();
                confirmation1.remove();
            }
        });
    }

    private void askQuit() {
        popupQuit = dialog.createPopupItemAndPowerUp(localize.get("quit"),
                localize.get("askQuit"), "popup_powerup");
        createYesAndNo(popupQuit);
        popupQuit.addActor(btnYes);
        popupQuit.addActor(btnNo);
        settingsDialog.addActor(popupQuit);

        btnYes.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.saveStats();
                game.saveSettings();
                Gdx.app.exit();
            }
        });

        btnNo.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                popupQuit.remove();
            }
        });
    }

    private void createYesAndNo(Dialog dialog) {
        btnYes = new ImageButton(finalSkin, "confirm_" + lan);
        btnYes.setPosition(dialog.getWidth()/2 - 400, dialog.getHeight()/4 - 55);

        btnNo = new ImageButton(finalSkin, "cancel_" + lan);
        btnNo.setPosition(btnYes.getX() + 445, btnYes.getY());
    }

    private void createLanguageButtons() {
        String finnishStyle = "finnish";
        String englishStyle = "english";
        if (room == "fight") {
            finnishStyle = "finnish_off";
            englishStyle = "english_off";
        }

        buttonFi = new ImageButton(finalSkin, finnishStyle);
        buttonFi.setPosition(945, 240);
        buttonFi.setTouchable(Touchable.disabled);
        if (lan.equals("fi")) {
            buttonFi.setChecked(true);
        }
        if (room == "hall") {
            buttonFi.setTouchable(Touchable.enabled);
            buttonFi.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                game.languageToFIN();
                }
            });
        }

        buttonEn = new ImageButton(finalSkin, englishStyle);
        buttonEn.setPosition(buttonFi.getX(), buttonFi.getY() - 120);
        buttonEn.setTouchable(Touchable.disabled);
        if (lan.equals("en")) {
            buttonEn.setChecked(true);
        }
        if (room == "hall") {
            buttonEn.setTouchable(Touchable.enabled);
            buttonEn.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                game.languageToENG();
                }
            });
        }

        settingsDialog.addActor(buttonFi);
        settingsDialog.addActor(buttonEn);
    }

    private void createMenuButtons() {
        buttonSettings = new ImageButton(finalSkin, "settings");
        buttonSettings.setPosition(1440, 720);

        buttonInventory = new ImageButton(finalSkin.getDrawable("menu_items1"));
        buttonInventory.setPosition(buttonSettings.getX(),
                buttonSettings.getY() - 180);
        buttonInventory.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                UtilItem utilItem = new UtilItem(game, room, curRoom);
                settingsDialog.remove();
            }
        });

        buttonStats = new ImageButton(finalSkin.getDrawable("menu_stats1"));
        buttonStats.setPosition(buttonSettings.getX(),
                buttonInventory.getY() - 180);
        buttonStats.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Stats stats = new Stats(game, room, curRoom);
                settingsDialog.remove();
            }
        });

        settingsDialog.addActor(buttonSettings);
        settingsDialog.addActor(buttonInventory);
        settingsDialog.addActor(buttonStats);
    }

    private void createExitButton() {
        buttonExit = new ImageButton(finalSkin, "x");
        buttonExit.setPosition(1550, 960);
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.saveSettings();
                settingsDialog.remove();
            }
        });

        settingsDialog.addActor(buttonExit);
    }

    private void createSettingsRoomButton() {
        settingsRoomButton = new TextButton("SettingsRoom", skin);
        settingsRoomButton.setPosition(1450, 200);
        settingsRoomButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.switchToRoomSettings();
            }
        });

        settingsDialog.addActor(settingsRoomButton);
    }
}
