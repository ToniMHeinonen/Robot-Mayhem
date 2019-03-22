package fi.tamk.fi;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;

import java.awt.Color;

public class RoomTestailua extends RoomParent {
    TextureAtlas testButtonAtlas;
    Skin testSkin;

    ImageButton.ImageButtonStyle styleAttack;
    ImageButton.ImageButtonStyle styleShield;
    ImageButton.ImageButtonStyle styleTooltipAttack;
    ImageButton.ImageButtonStyle styleTooltipShield;

    String[] buttonDrawablesOn;
    String[] buttonDrawablesOff;
    String[] tooltips;
    ImageButton.ImageButtonStyle[] buttonStyles;
    ImageButton.ImageButtonStyle[] tooltipStyles;

    int buttonCounter;
    float space = 300f;
    float tooltipDelay = 2;

    com.badlogic.gdx.graphics.Color fontColor = com.badlogic.gdx.graphics.Color.BLACK;
    Label.LabelStyle labelStyle;
    Window.WindowStyle windowStyle;
    String[] allTexts;

    RoomTestailua(MainGame game) {
        super(game);
        createButtonSettings();
        createConstants();
        createButtons();
        // createPlayerDialog();
        // playMusic();

        //createDialogs(0, 200, 200);
        createDialogs("player says fsfds fsdfdsfs", 200, 200);
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

    public void createConstants() {
        testButtonAtlas = new TextureAtlas("testbuttons/testbuttons.pack");
        testSkin = new Skin(testButtonAtlas);

        styleAttack = new ImageButton.ImageButtonStyle();
        styleShield = new ImageButton.ImageButtonStyle();
        buttonStyles = new ImageButton.ImageButtonStyle[] {styleAttack, styleShield};

        styleTooltipAttack = new ImageButton.ImageButtonStyle();
        styleTooltipShield = new ImageButton.ImageButtonStyle();
        tooltipStyles = new ImageButton.ImageButtonStyle[] {styleTooltipAttack, styleTooltipShield};

        tooltips = new String[] {"attack_tooltip", "shield_tooltip"};
        buttonDrawablesOn = new String[] {"button_attack", "button_shield"};
        buttonDrawablesOff = new String[] {"button_attack_off", "button_shield_off"};

        windowStyle = new Window.WindowStyle(fontSteps, fontColor, testSkin.getDrawable("dialog_bg"));
        labelStyle = new Label.LabelStyle(fontSteps, fontColor);
        allTexts = new String[] {"player says blablabla", "enemy says blablabla"};
    }

    // Below two different kind of method creating a dialog.
    // Don't know which one is more flexible?

    // In this one you can set text and dialogposition when creating this method.
    public void createDialogs(String text, int x, int y) {
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        final Dialog dialog = new Dialog("", windowStyle);
        dialog.getContentTable().add(label).prefWidth(400);
        dialog.setPosition(x, y);
        // Is the dialogsize always the same?
        // If it is, I can just use
        // dialog.getBackground().getMinHeight()
        // dialog.getBackground().getMinWidth()
        dialog.setSize(label.getWidth() + 50,label.getHeight()*4f);
        stage.addActor(dialog);
        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.remove();
            }
        });
    }

    // In this one all texts are stored in array and you can choose which one you want use.
    /*
    public void createDialogs(int i, int x, int y) {
            float spaceBetween = 800;
            Label label = new Label(allTexts[i], labelStyle);
            label.setWrap(true);
            final Dialog dialog = new Dialog("", windowStyle);
            dialog.getContentTable().add(label).prefWidth(400);
            dialog.setPosition(x, y);
            dialog.setSize(500,300);
            stage.addActor(dialog);
            dialog.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y){
                    dialog.remove();
                }
            });
    }
    */

    public void createButtons() {
        for (int i = 0; i < buttonDrawablesOn.length; i++) {
            buttonCounter = i;
            buttonStyles[i].up = testSkin.getDrawable(buttonDrawablesOn[i]);
            buttonStyles[i].down = testSkin.getDrawable(buttonDrawablesOff[i]);
            final ImageButton imgBtn = new ImageButton(buttonStyles[i]);
            imgBtn.setPosition(100 + space*i, 100);
            stage.addActor(imgBtn);

            // Default-values: halfTapSquareSize=20, tapCountInterval=0.4f, longPressDuration=1.1f, maxFlingDelay=0.15f.
            imgBtn.addListener(new ActorGestureListener(20,0.4f,0.5f,0.15f) {
                int i = buttonCounter;
                public boolean longPress(Actor actor, float x, float y) {
                    System.out.println("longpress");
                    showTooltip(i);
                    return true;
                }
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    System.out.println("tap");
                    buttonStyles[i].down = testSkin.getDrawable(buttonDrawablesOff[i]);
                }
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    System.out.println("touchup");
                    buttonStyles[i].up = testSkin.getDrawable(buttonDrawablesOn[i]);
                }
            });
        }
    }

    // Show tooltip for 2 seconds(tooltipDelay) and then remove it from the stage.
    public void showTooltip(int index) {
        tooltipStyles[index].up = testSkin.getDrawable(tooltips[index]);
        final ImageButton ttBtn = new ImageButton(tooltipStyles[index]);
        ttBtn.setPosition(100 + space*index, 100*3);
        stage.addActor(ttBtn);
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                ttBtn.remove();
            }
        }, tooltipDelay);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
