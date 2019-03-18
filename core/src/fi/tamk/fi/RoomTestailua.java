package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RoomTestailua extends RoomParent {
    TextureAtlas testButtonAtlas;
    Skin testSkin;

    ImageButton.ImageButtonStyle styleAttack;
    ImageButton.ImageButtonStyle styleShield;

    int animationCounter = 50;
    boolean inAnimation = false;

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
    }

    public void attackButton() {
        styleAttack.up = testSkin.getDrawable("button_attack");
        //Maybe not necessary..?
        //styleAttack.down = testSkin.getDrawable("button_attack_clicked");

        ImageButton buttonAttack = new ImageButton(styleAttack);
        buttonAttack.setPosition(game.pixelWidth/3, game.pixelHeight/4);

        // Button is available to click only if there is no animation going on.
        if (!inAnimation) {
            buttonAttack.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
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
        //Maybe not necessary..?
        //styleShield.down = testSkin.getDrawable("button_shield_clicked");

        ImageButton buttonShield = new ImageButton(styleShield);
        buttonShield.setPosition(game.pixelWidth/2, game.pixelHeight/4);

        // Button is available to click only if there is no animation going on.
        if (!inAnimation) {
            buttonShield.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
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

    @Override
    public void render(float delta) {
        super.render(delta);
        attackButton();
        shieldButton();
        animationUpdate();
    }
}
