package fi.tamk.fi;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;

/*
Wow, now this code is looking really great! Though now you will have to explain to me how it works,
since I don't understand all the things that you have used :D.

One more thing though, have you figured out a way for the button to go gray when finger is touching
it and when the finger lets go, then it returns back to normal? If you can make that work, it would
look much more UX friendly.
 */

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
    int styleCounter;
    float space = 300f;
    float buttonDelay = 1;
    float tooltipDelay = 2;

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

        styleTooltipAttack = new ImageButton.ImageButtonStyle();
        styleTooltipShield = new ImageButton.ImageButtonStyle();
        tooltipStyles = new ImageButton.ImageButtonStyle[] {styleTooltipAttack, styleTooltipShield};

        tooltips = new String[] {"attack_tooltip", "shield_tooltip"};
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
