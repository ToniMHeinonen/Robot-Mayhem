package pepperstep;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;

import java.awt.SystemTray;

public class RoomTestailua extends RoomParent {
    private Skin testSkin;

    private Drawable[] tooltips;
    private Drawable tooltipAttack, tooltipDefend, tooltipItem;

    private String[] btn;

    private int buttonCounter;
    private float space = 300f;
    private float tooltipDelay = 2;

    Hacking hacking;

    boolean startHack = false;

    // Open settings
    Dialog settingsDialog;
    boolean closeDialog = false;
    boolean clickedOpenSettings = false;

    RoomTestailua(MainGame game) {
        super(game);
        testSkin = game.getTestSkin();
        createButtonSettings();
        createButtonStartHack();
        createConstants();
        createButtons();
        // playMusic();
        createOpenSettingsButton();

        // Added for testing.
        createButtonItemTesting();
        dialog.createDialog("player says fsfds fsdfdsfs", 200f, 200f);
    }

    public void playMusic() {
        // backgroundMusic.setVolume(game.getMusicVol());
        // backgroundMusic.play();
    }

    public void createButtonSettings() {
        final TextButton buttonSettings = new TextButton("RoomSettings", skin);
        buttonSettings.setWidth(300f);
        buttonSettings.setHeight(100f);
        buttonSettings.setPosition(game.pixelWidth /2 - buttonSettings.getWidth() /2,
                (game.pixelHeight/3) *2 - buttonSettings.getHeight() /2);
        stage.addActor(buttonSettings);

        buttonSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.switchToRoomSettings();
            }
        });
    }

    public void createButtonStartHack() {
        final TextButton buttonSettings = new TextButton("Start Hacking", skin);
        buttonSettings.setWidth(300f);
        buttonSettings.setHeight(100f);
        buttonSettings.setPosition(game.pixelWidth /2 - buttonSettings.getWidth() /2,
                (game.pixelHeight/3) *2 + buttonSettings.getHeight() /2);
        stage.addActor(buttonSettings);

        buttonSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                startHack = true;
                //hacking = new Hacking(game, game.getHackFirstTry());
            }
        });
    }

    public void createConstants() {
        tooltipAttack = testSkin.getDrawable("tooltip_attack");
        tooltipDefend = testSkin.getDrawable("tooltip_defend");
        tooltipItem = testSkin.getDrawable("tooltip_item");
        tooltips = new Drawable[] {tooltipAttack, tooltipDefend, tooltipItem};
        btn = new String[] {"button_attack", "button_defend", "button_item"};
    }

    public void createButtons() {
        for (int i = 0; i < btn.length; i++) {
            buttonCounter = i;
            Drawable normal = testSkin.getDrawable(btn[i]);
            Drawable clicked = testSkin.getDrawable(btn[i] + "_clicked");
            final ImageButton imgButton = new ImageButton(normal, clicked);
            imgButton.setPosition(100 + space * i, 100);
            stage.addActor(imgButton);
            imgButton.addListener(new ActorGestureListener(20,0.4f,0.5f,0.15f) {
                int i = buttonCounter;
                public boolean longPress(Actor actor, float x, float y) {
                    System.out.println("longpress");
                    showTooltip(i);
                    return true;
                }
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    System.out.println("tap");
                }
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("touchup");
                }
            });
        }
    }

    // Added for item testing.
    public void createButtonItemTesting() {
        final TextButton buttonItemTesting = new TextButton("Item Test", skin);
        buttonItemTesting.setWidth(300f);
        buttonItemTesting.setHeight(100f);
        buttonItemTesting.setPosition(game.pixelWidth /2 - buttonItemTesting.getWidth() /2,
                (game.pixelHeight/3) + buttonItemTesting.getHeight() /2);
        stage.addActor(buttonItemTesting);

        buttonItemTesting.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){

                game.switchToRoomItemTest();
            }
        });
    }

    // Show tooltip for 2 seconds(tooltipDelay) and then remove it from the stage.
    public void showTooltip(int index) {
        final ImageButton ttBtn = new ImageButton(tooltips[index]);
        ttBtn.setPosition(100 + space*index, 100*3);
        stage.addActor(ttBtn);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                ttBtn.remove();
            }
        }, tooltipDelay);
    }

    public void createOpenSettingsButton() {
        final TextButton buttonSettings = new TextButton("Open settings", skin);
        buttonSettings.setWidth(300f);
        buttonSettings.setHeight(100f);
        buttonSettings.setPosition(game.pixelWidth /2 - buttonSettings.getWidth() /2,
                (game.pixelHeight/3) *2 + buttonSettings.getHeight() * 2);
        stage.addActor(buttonSettings);

        buttonSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                createDialog();
                clickedOpenSettings = true;
            }
        });
    }

    // Testing, if dialog could be used to settings. Not finished.
    // Buttons, sliders, texts etc. will be moved to different method.
    public void createDialog() {
        Label text = new Label("Music volume", skin);
        text.setColor(Color.BLACK);
        Slider slider = new Slider(0f, 1f, 0.1f, false, skin);
        TextButton textButton = new TextButton("Close", skin);
        textButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                closeDialog = true;
            }
        });
        settingsDialog = new Dialog("settings", skin);
        settingsDialog.setMovable(false);
        settingsDialog.setPosition(200, -500);
        settingsDialog.setSize(500, 500);
        slider.setPosition(settingsDialog.getWidth()/2,
                settingsDialog.getHeight()/2);
        text.setPosition(settingsDialog.getWidth()/2 - textButton.getWidth()/2,
                settingsDialog.getHeight()/2);
        textButton.setPosition(settingsDialog.getWidth()/2 - textButton.getWidth()/2
                , settingsDialog.getHeight()/2 - textButton.getWidth()/2);
        settingsDialog.addActor(textButton);
        settingsDialog.addActor(text);
        settingsDialog.addActor(slider);
        settingsDialog.setKeepWithinStage(false);
        stage.addActor(settingsDialog);
    }

    public void controlSettings() {
        if (!closeDialog) {
            if (settingsDialog.getY() <= 300) {
                settingsDialog.setY(settingsDialog.getY() + 10);
            }
        }
        if (closeDialog) {
            if (settingsDialog.getY() > -500)
                settingsDialog.setY(settingsDialog.getY() - 10);
            if (settingsDialog.getY() == -500) {
                settingsDialog.remove();
                closeDialog = false;
                clickedOpenSettings = false;
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (startHack) {
            hacking.update();
        }
        if (clickedOpenSettings) {
            controlSettings();
        }
    }
}
