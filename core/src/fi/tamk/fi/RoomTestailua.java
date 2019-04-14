package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
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
import java.util.ArrayList;

public class RoomTestailua extends RoomParent {
    private Skin testSkin;

    UtilPowerUp utilPowerUp;

    MainGame game;

    private Drawable[] tooltips;
    private Drawable tooltipAttack, tooltipDefend, tooltipItem;

    int attackCooldown;
    int defendCooldown;
    int itemCooldown;
    int[] cooldowns;

    private String[] btn;

    private int buttonCounter;
    private float space = 300f;
    private float tooltipDelay = 2;

    private Hacking hacking;
    private boolean startHack = false;

    private UtilDialog utilDialog;

    RoomTestailua(MainGame game) {
        super(game);
        this.game = game;
        testSkin = game.getTestSkin();
        utilDialog = game.getDialog();
        createButtonSettings();
        //createConstants();
        //createButtons();
        createButtonHacking();
        createButtonHacking2();
        createButtonDialog();
        createButtonPowerup();
        addSkills();
        // playMusic();

        // Added for testing.
        createButtonItemTesting();
    }

    public void createButtonPowerup() {
        final TextButton buttonSettings = new TextButton("PowerUp", skin);
        buttonSettings.setWidth(300f);
        buttonSettings.setHeight(100f);
        buttonSettings.setPosition(game.pixelWidth /2 - buttonSettings.getWidth() /2 + 300,
                (game.pixelHeight/3) *2 - buttonSettings.getHeight() /2);
        stage.addActor(buttonSettings);

        buttonSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                utilPowerUp = new UtilPowerUp(game);
            }
        });
    }

    public void playMusic() {
        // backgroundMusic.setVolume(game.getMusicVol());
        // backgroundMusic.play();
    }

    private void addSkills() {
        TextButton buttonAddSkills = new TextButton("Add2Skills", skin);
        buttonAddSkills.setWidth(300f);
        buttonAddSkills.setHeight(100f);
        buttonAddSkills.setPosition(game.pixelWidth /2 + 200,
                (game.pixelHeight/3) *2 + buttonAddSkills.getHeight()*2);
        stage.addActor(buttonAddSkills);

        buttonAddSkills.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.addToInventory("DUST", true);
                game.addToInventory("SHOCK", true);
            }
        });
    }

    public void createButtonHacking() {
        TextButton buttonHacking = new TextButton("FirstHack", skin);
        buttonHacking.setWidth(300f);
        buttonHacking.setHeight(100f);
        buttonHacking.setPosition(game.pixelWidth /2 - buttonHacking.getWidth() /2,
                (game.pixelHeight/3) *2 + buttonHacking.getHeight());
        stage.addActor(buttonHacking);

        buttonHacking.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                hacking = new Hacking(game, true);
                startHack = true;
            }
        });
    }

    public void createButtonHacking2() {
        TextButton buttonHacking = new TextButton("Hack", skin);
        buttonHacking.setWidth(300f);
        buttonHacking.setHeight(100f);
        buttonHacking.setPosition(game.pixelWidth /2 - buttonHacking.getWidth() /2,
                (game.pixelHeight/3) *2 + buttonHacking.getHeight() * 2);
        stage.addActor(buttonHacking);

        buttonHacking.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                hacking = new Hacking(game, false);
                startHack = true;
            }
        });
    }


    public void createButtonDialog() {
        final TextButton buttonSettings = new TextButton("Dialog", skin);
        buttonSettings.setWidth(300f);
        buttonSettings.setHeight(100f);
        buttonSettings.setPosition(game.pixelWidth /2 - buttonSettings.getWidth() *2,
                (game.pixelHeight/3) *2 - buttonSettings.getHeight() /2);
        stage.addActor(buttonSettings);

        buttonSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Dialog popup = utilDialog.createPopupItemAndPowerUp("Item/PowerUp", "Description " +
                        "Description Description Description Description " +
                        "Descriptionnnn nnn Description Description", "popup_powerup");
                stage.addActor(popup);
            }
        });
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

    /*
    public void createConstants() {
        tooltipAttack = testSkin.getDrawable("tooltip_attack");
        tooltipDefend = testSkin.getDrawable("tooltip_defend");
        tooltipItem = testSkin.getDrawable("tooltip_item");
        tooltips = new Drawable[] {tooltipAttack, tooltipDefend, tooltipItem};
        btn = new String[] {"button_attack", "button_defend", "button_item"};

        attackCooldown = 0;
        defendCooldown = 1;
        itemCooldown = 0;
        cooldowns = new int[] {attackCooldown, defendCooldown, itemCooldown};
    }

    public void createButtons() {
        for (int i = 0; i < btn.length; i++) {
            buttonCounter = i;
            Drawable normal = testSkin.getDrawable(btn[i] + cooldowns[i]);
            Drawable clicked = testSkin.getDrawable(btn[i] + cooldowns[i]);
            if (cooldowns[i] == 0) {
                clicked = testSkin.getDrawable(btn[i] + "_clicked");
            }
            final ImageButton imgButton = new ImageButton(normal, clicked);
            imgButton.setPosition(100 + space * i, 100);
            stage.addActor(imgButton);
            if (cooldowns[i] == 0) {
                imgButton.addListener(new ActorGestureListener(20, 0.4f, 0.5f, 0.15f) {
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
    }
    */

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

                //System.out.println(Item.itemDamage());
                //System.out.println(Item.itemHeal());

                game.switchToRoomItemTest();
            }
        });
    }

    /*
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
    */


    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
        if (startHack) {
            hacking.update();
        }
    }
}
