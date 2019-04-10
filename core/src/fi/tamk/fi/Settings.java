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
    private int space = 300;

    // Quit and Reset
    private ImageButton buttonQuit;
    private ImageButton buttonReset;

    // Language
    private ImageButton buttonFi;
    private ImageButton buttonEn;

    private boolean closeDialog = false;

    Settings(MainGame game) {
        this.game = game;
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
        createCloseButton();
        createSettingsRoomButton();
        addActors();
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
        header.setPosition(settingsDialog.getWidth() / 3 - 70, settingsDialog.getHeight() - 200);
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
                game.getMusMainTheme().setVolume(musicVolSlider.getValue());
                game.setMusicVol(musicVolSlider.getValue());
            }
        });

        musicVolLabel = new Label("Music:", finalSkin);
        musicVolLabel.setPosition(musicVolSlider.getX() - 205,
                musicVolSlider.getY() + 25);
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
                game.getMusMainTheme().setVolume(musicVolSlider.getValue());
                game.setMusicVol(musicVolSlider.getValue());
            }
        });

        soundVolLabel = new Label("Sound:", finalSkin);
        soundVolLabel.setPosition(soundVolSlider.getX() - 205,
                soundVolSlider.getY() + 25);
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
    }

    private void createQuitAndResetButtons() {
        buttonQuit = new ImageButton(finalSkin, "quit_en");
        buttonQuit.setPosition(difficultyLabel.getX() - 20,
                120);

        buttonReset = new ImageButton(finalSkin, "reset_en");
        buttonReset.setPosition(buttonQuit.getX() + buttonReset.getWidth(),
                buttonQuit.getY());
    }

    private void createLanguageButtons() {
        buttonFi = new ImageButton(finalSkin, "finnish");
        buttonFi.setPosition(945, 240);

        buttonEn = new ImageButton(finalSkin, "english");
        buttonEn.setPosition(buttonFi.getX(), buttonFi.getY() - 120);
    }

    private void createSettingsRoomButton() {
        settingsRoomButton = new TextButton("SettingsRoom", skin);
        settingsRoomButton.setPosition(settingsDialog.getWidth() / 2 - settingsRoomButton.getWidth(),
                settingsDialog.getHeight()/2 - settingsRoomButton.getHeight() * 2);
        settingsRoomButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.switchToRoomSettings();
            }
        });
    }

    private void createCloseButton() {
        closeSettingsButton = new TextButton("Close", skin);
        closeSettingsButton.setPosition(settingsDialog.getWidth() / 2 + closeSettingsButton.getWidth()/2,
                settingsDialog.getHeight()/2 - closeSettingsButton.getHeight() * 2);
        closeSettingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.saveSettings();
                settingsDialog.remove();
            }
        });
    }

    private void addActors() {
        settingsDialog.addActor(header);
        settingsDialog.addActor(musicVolSlider);
        settingsDialog.addActor(musicVolLabel);
        settingsDialog.addActor(soundVolSlider);
        settingsDialog.addActor(soundVolLabel);
        settingsDialog.addActor(difficultyLabel);
        settingsDialog.addActor(buttonQuit);
        settingsDialog.addActor(buttonReset);
        settingsDialog.addActor(buttonFi);
        settingsDialog.addActor(buttonEn);
        settingsDialog.addActor(closeSettingsButton);
        settingsDialog.addActor(settingsRoomButton);
        stage.addActor(settingsDialog);
    }
}
