package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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

    ImageButton buttonAttack;
    ImageButton buttonShield;
    ImageButton.ImageButtonStyle styleAttack;
    ImageButton.ImageButtonStyle styleShield;

    TextureRegion tooltipShield;
    TextureRegion tooltipAttack;

    int animationCounter = 50;
    boolean inAnimation = false;

    boolean pressLongAttack;
    boolean pressLongShield;

    int tooltipTimer = 50;

    RoomTestailua(MainGame game) {
        super(game);
        createButtonSettings();
        createConstants();
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

    public void animationUpdate() {
        if (inAnimation) {
            animationCounter--;
        }
        if (animationCounter <= 0) {
            inAnimation = false;
            animationCounter = 50;
        }
    }

    public void createConstants() {
        testButtonAtlas = new TextureAtlas("testbuttons/testbuttons.pack");
        testSkin = new Skin(testButtonAtlas);

        styleAttack = new ImageButton.ImageButtonStyle();
        styleShield = new ImageButton.ImageButtonStyle();

        tooltipShield = new TextureRegion(testButtonAtlas.findRegion("shield_tooltip"));
        tooltipAttack = new TextureRegion(testButtonAtlas.findRegion("attack_tooltip"));
    }

    public void attackButton() {
        styleAttack.up = testSkin.getDrawable("button_attack");
        // Maybe not necessary..?
        // styleAttack.down = testSkin.getDrawable("button_attack_clicked");

        buttonAttack = new ImageButton(styleAttack);
        buttonAttack.setPosition(game.pixelWidth/3, game.pixelHeight/4);

        // Button is available to click only if there is no animation going on.
        if (!inAnimation) {
            buttonAttack.addListener(new ActorGestureListener() {
                public boolean longPress(Actor actor, float x, float y) {
                    pressLongAttack = true;
                    return true;
                }
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    inAnimation = true;
                    System.out.println("attack");
                }
            });
        } else {
            // If there is animation going on, button is "greyed out" and unavailable to click.
            styleAttack.up = testSkin.getDrawable("button_attack_off");
        }

        stage.addActor(buttonAttack);
    }

    public void shieldButton() {
        styleShield.up = testSkin.getDrawable("button_shield");
        // Maybe not necessary..?
        // styleShield.down = testSkin.getDrawable("button_shield_clicked");

        buttonShield = new ImageButton(styleShield);
        buttonShield.setPosition(game.pixelWidth/2, game.pixelHeight/4);

        // Button is available to click only if there is no animation going on.
        if (!inAnimation) {
            buttonShield.addListener(new ActorGestureListener() {
                public boolean longPress(Actor actor, float x, float y) {
                    pressLongShield = true;
                    return true;
                }
                public void tap(InputEvent event, float x, float y, int count, int button) {
                    inAnimation = true;
                    System.out.println("shield");
                }
            });
        } else {
            // If there is animation going on, button is "greyed out" and unavailable to click.
            styleShield.up = testSkin.getDrawable("button_shield_off");
        }

        stage.addActor(buttonShield);
    }

    // Check if user has "longpressed" buttons and draw tooltips.
    public void checkTooltip() {
        if (pressLongAttack) {
            tooltipTimer--;
            batch.draw(tooltipAttack, buttonAttack.getX(),
                    game.pixelHeight/4 + buttonAttack.getHeight());
        }

        if (pressLongShield) {
            tooltipTimer--;
            batch.draw(tooltipShield,buttonShield.getX(),
                    game.pixelHeight/4 + buttonShield.getHeight());
        }

        if (tooltipTimer <= 0) {
            pressLongAttack = false;
            pressLongShield = false;
            tooltipTimer = 50;
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        attackButton();
        shieldButton();
        animationUpdate();
        batch.begin();
        checkTooltip();
        batch.end();
    }
}
