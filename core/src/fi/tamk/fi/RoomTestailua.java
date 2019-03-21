package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;

/*
Try to make the long press shorter, for example half a second.

We need to discuss if we want every button to grey out when something is pressed.Original plan was
to only grey out the button which is pressed and when button is released it returns to normal.

Can you figure out a way to draw all the buttons using just one method? You can check for an example
from the RoomFight class (you need to use arrays).

Is there any way to do the long press check without using booleans?

This code is pretty long for just 2 buttons, is there any way to make it shorter?
 */

public class RoomTestailua extends RoomParent {
    TextureAtlas testButtonAtlas;
    Skin testSkin;

    ImageButton.ImageButtonStyle styleAttack;
    ImageButton.ImageButtonStyle styleShield;
    TextureRegion tooltipShield;
    TextureRegion tooltipAttack;

    String[] buttonDrawablesOn;
    String[] buttonDrawablesOff;
    ImageButton.ImageButtonStyle[] buttonStyles;
    TextureRegion tooltips[];

    int buttonCounter;
    int styleCounter;
    float space = 300f;
    float buttonDelay = 1;

    RoomTestailua(MainGame game) {
        super(game);
        createButtonSettings();
        createConstants();
        createButtons();
        // playMusic();
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

    // Testing buttons starts here

    public void createConstants() {
        testButtonAtlas = new TextureAtlas("testbuttons/testbuttons.pack");
        testSkin = new Skin(testButtonAtlas);

        styleAttack = new ImageButton.ImageButtonStyle();
        styleShield = new ImageButton.ImageButtonStyle();
        buttonStyles = new ImageButton.ImageButtonStyle[] {styleAttack, styleShield};

        tooltipAttack = new TextureRegion(testButtonAtlas.findRegion("attack_tooltip"));
        tooltipShield = new TextureRegion(testButtonAtlas.findRegion("shield_tooltip"));
        tooltips = new TextureRegion[] {tooltipAttack, tooltipShield};

        buttonDrawablesOn = new String[] {"button_attack", "button_shield"};
        buttonDrawablesOff = new String[] {"button_attack_off", "button_shield_off"};
    }

    public void createButtons() {
        for (int i = 0; i < buttonDrawablesOn.length; i++) {
            buttonCounter = i;
            buttonStyles[i].up = testSkin.getDrawable(buttonDrawablesOn[i]);
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
                    pressButton(i);
                    System.out.println("tap");
                }
            });
        }
    }

    // Set button to greyed out for a second(buttonDelay).
    public void pressButton(int index) {
        buttonStyles[index].up = testSkin.getDrawable(buttonDrawablesOff[index]);
        styleCounter = index;
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                buttonStyles[styleCounter].up = testSkin.getDrawable(buttonDrawablesOn[styleCounter]);
            }
        }, buttonDelay);
    }

    public void showTooltip(int index) {
        //batch.draw(tooltips[index], 100 + space*index, 100*2);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
