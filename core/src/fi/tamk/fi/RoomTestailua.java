package fi.tamk.fi;

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
I have an idea for you, which decreases the need for attackOn and attackOff variables. Scroll down
and have a look. You can apply this to all the buttons and modes (if there is going to be a mode
called for example "button_attack_disabled")
 */

/*
EDIT: Sorry my bad I thought that you did it another way, so the way to correct this is to do
String arrays instead of drawable, I will show you what I mean. I will comment out my modifications,
so you can compare your way and my way.
 */

public class RoomTestailua extends RoomParent {
    private Skin testSkin;

    //EDIT 1: private String[] btn;
    private Drawable[] btnOn, btnOff, tooltips;
    private Drawable attackOn, attackOff, shieldOn, shielfOff, tooltipAttack, tooltipShield;

    private int buttonCounter;
    private float space = 300f;
    private float tooltipDelay = 2;

    RoomTestailua(MainGame game) {
        super(game);
        testSkin = game.getTestSkin();
        createButtonSettings();
        createConstants();
        createButtons();
        // playMusic();
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

    public void createConstants() {
        //EDIT 2: You don't need these variables at all
        attackOn = testSkin.getDrawable("button_attack");
        attackOff = testSkin.getDrawable("button_attack_off");
        shieldOn = testSkin.getDrawable("button_shield");
        shielfOff = testSkin.getDrawable("button_shield_off");
        tooltipAttack = testSkin.getDrawable("attack_tooltip");
        tooltipShield = testSkin.getDrawable("shield_tooltip");
        //EDIT 3: btn = new String[] {"button_attack", "button_shield"};
        btnOn = new Drawable[] {attackOn, shieldOn,};
        btnOff = new Drawable[] {attackOff, shielfOff};
        tooltips = new Drawable[] {tooltipAttack, tooltipShield};
    }

    public void createButtons() {
        for (int i = 0; i < btnOn.length; i++) {
            buttonCounter = i;
            // EDIT 4: Replace this with:
            // final ImageButton imgButton = new ImageButton(testSkin.getDrawable(btn[i]),
            // testSkin.getDrawable(btn[i] + "_off"));

            /*
            OR you can make variables for the buttons and pass them on, like this:

            Drawable on = testSkin.getDrawable(btn[i]);
            Drawable off = testSkin.getDrawable(btn[i] + "_off");
            final ImageButton imgButton = new ImageButton(on, off);
             */
            final ImageButton imgButton = new ImageButton(btnOn[i], btnOff[i]);
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

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
