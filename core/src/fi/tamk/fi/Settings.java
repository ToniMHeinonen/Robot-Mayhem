package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class Settings {
    MainGame game;
    private Files files;
    private Skin skin;
    private Stage stage;
    private Skin finalSkin;

    private float settingsWidth;
    private float settingsHeight;
    private float posX;
    private float outOfScreenY;
    private float onScreenY;

    private TextButton closeSettingsButton;
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
    private String[] difficulties = new String[] {"0", "1", "2"};
    private int space = 310;

    // Quit and Reset
    private ImageButton buttonQuit;
    private ImageButton buttonReset;

    // Language
    private ImageButton buttonFi;
    private ImageButton buttonEn;

    // Menu
    private ImageButton buttonSettings;
    private ImageButton buttonInventory;
    private ImageButton buttonStats;

    // Exit
    private ImageButton buttonExit;

    private boolean closeDialog = false;

    Settings(MainGame game) {
        this.game = game;
        files = game.getFiles();
        skin = game.getSkin();
        stage = game.getStage();
        finalSkin = game.getFinalSkin();

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
    }

    public void update() {
        if (!closeDialog && settingsDialog.getY() >= onScreenY) {
            settingsDialog.setY(settingsDialog.getY() - Gdx.graphics.getDeltaTime()*1000);
        }
        if (closeDialog && settingsDialog.getY() < outOfScreenY) {
            settingsDialog.setY(settingsDialog.getY() + Gdx.graphics.getDeltaTime()*1000);
        } if (closeDialog && settingsDialog.getY() >= outOfScreenY) {
            game.setClickedOpenSettings(false);
            settingsDialog.remove();
        }
    }

    private void setValues() {
        settingsWidth = game.pixelWidth / 2;
        settingsHeight = game.pixelHeight / 2;
        //posX = game.pixelWidth / 4;
        //outOfScreenY = game.pixelHeight;
        //onScreenY = game.pixelHeight / 2;
        posX = 0;
        outOfScreenY = 0;
        onScreenY = 0;
    }

    private void createSettingsDialog() {
        settingsDialog = new Dialog("Settings", finalSkin, "settings");
        settingsDialog.setMovable(false);
        settingsDialog.setKeepWithinStage(false);
        settingsDialog.setPosition(posX, outOfScreenY);
        settingsDialog.setSize(game.pixelWidth, game.pixelHeight);
    }

    private void createHeader() {
        header = new Label("Settings", finalSkin, "big");
        header.setPosition(settingsDialog.getWidth() / 3 - 70,
                settingsDialog.getHeight() - 200);

        settingsDialog.addActor(header);
    }

    private void createMusicVolume() {
        musicVolSlider = new Slider(0.1f, 0.9f, 0.1f, false, finalSkin);
        musicVolSlider.setValue(game.getMusicVol());
        musicVolSlider.setPosition(header.getX() - 110,
                header.getY() - 160);
        musicVolSlider.setSize(900, 120);
        musicVolSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                files.musMainTheme.setVolume(musicVolSlider.getValue());
                game.setMusicVol(musicVolSlider.getValue());
            }
        });

        musicVolLabel = new Label("Music:", finalSkin);
        musicVolLabel.setPosition(musicVolSlider.getX() - 205,
                musicVolSlider.getY() + 25);

        settingsDialog.addActor(musicVolSlider);
        settingsDialog.addActor(musicVolLabel);
    }

    private void createSoundVolume() {
        soundVolSlider = new Slider(0.1f, 0.9f, 0.1f, false, finalSkin);
        soundVolSlider.setValue(game.getMusicVol());
        soundVolSlider.setPosition(musicVolSlider.getX(),
                musicVolSlider.getY() - 120);
        soundVolSlider.setSize(900, 120);
        soundVolSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                files.musMainTheme.setVolume(musicVolSlider.getValue());
                game.setMusicVol(musicVolSlider.getValue());
            }
        });

        soundVolLabel = new Label("Sound:", finalSkin);
        soundVolLabel.setPosition(soundVolSlider.getX() - 205,
                soundVolSlider.getY() + 25);

        settingsDialog.addActor(soundVolSlider);
        settingsDialog.addActor(soundVolLabel);
    }

    private void createDifficultyButtons() {
        for (int i = 0; i < difficulties.length; i++) {
            ImageButton difficulty = new ImageButton(finalSkin, "diff" + difficulties[i]);
            difficulty.setPosition(musicVolSlider.getX() + 15 + i* space,
                    soundVolSlider.getY() - 125);
            settingsDialog.addActor(difficulty);
        }
        difficultyLabel = new Label("Difficulty:", finalSkin);
        difficultyLabel.setPosition(soundVolLabel.getX() - 105,
                soundVolLabel.getY() - 120);

        settingsDialog.addActor(difficultyLabel);
    }

    private void createQuitAndResetButtons() {
        buttonQuit = new ImageButton(finalSkin, "quit_en");
        buttonQuit.setPosition(difficultyLabel.getX() - 20,
                120);

        buttonReset = new ImageButton(finalSkin, "reset_en");
        buttonReset.setPosition(buttonQuit.getX() + buttonReset.getWidth(),
                buttonQuit.getY());

        settingsDialog.addActor(buttonQuit);
        settingsDialog.addActor(buttonReset);
    }

    private void createLanguageButtons() {
        buttonFi = new ImageButton(finalSkin, "finnish");
        buttonFi.setPosition(945, 240);

        buttonEn = new ImageButton(finalSkin, "english");
        buttonEn.setPosition(buttonFi.getX(), buttonFi.getY() - 120);

        settingsDialog.addActor(buttonFi);
        settingsDialog.addActor(buttonEn);
    }

    private void createMenuButtons() {
        buttonSettings = new ImageButton(finalSkin, "settings");
        buttonSettings.setPosition(1441, 720);

        buttonInventory = new ImageButton(finalSkin.getDrawable("menu_items1"));
        buttonInventory.setPosition(buttonSettings.getX(),
                buttonSettings.getY() - 150);

        buttonStats = new ImageButton(finalSkin.getDrawable("menu_stats1"));
        buttonStats.setPosition(buttonSettings.getX(),
                buttonInventory.getY() - 150);

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
