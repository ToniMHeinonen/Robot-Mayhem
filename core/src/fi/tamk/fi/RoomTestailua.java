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

    protected static String name;
    protected static Preferences prefs;

    RoomTestailua(MainGame game) {
        super(game);
        testSkin = game.getTestSkin();
        utilDialog = game.getDialog();
        createButtonSettings();
        createConstants();
        createButtons();
        createButtonHacking();
        createButtonHacking2();
        createButtonDialog();
        // playMusic();

        // Testing final skin.
        createFinalSkinButtons();

        // Added for testing.
        createButtonItemTesting();

        // Name testing.
        askForName();
        dialog.createDialog("player says fsfds fsdfdsfs");
    }

    // Methods for name start.
    public class MyTextInputListener implements Input.TextInputListener {
        @Override
        public void input (String text) {
            boolean legal = setName(text);
            if (!legal) {
                askForName();
            }
        }

        @Override
        public void canceled () {
            askForName();
        }
    }

    public void askForName() {
        MyTextInputListener listener = new MyTextInputListener();
        Gdx.input.getTextInput(listener, "Enter name", "", "Max 10 characters");
    }

    // Next up code for the name:
    public boolean setName(String n) {
        boolean legal = true;
        prefs = Gdx.app.getPreferences("FreeGamePreferences");
        ArrayList<String> names = new ArrayList<String>();

        if (name != null) {

            dialog.createDialog(name + "Auygaiuygdsiuya");
        } else if (n.length() <= 10 && !n.equals("")) {
            this.name = n;
            dialog.createDialog(name);
            prefs.putString("name", "Nobody");
            names.add(name);
        } else {
            legal = false;
        }
        prefs.flush();
        return legal;
    }

    // Methods for name end.

    public void playMusic() {
        // backgroundMusic.setVolume(game.getMusicVol());
        // backgroundMusic.play();
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
                        "Description Description Description Description Description Description " +
                        "Descriptionnnn nnn Description Description Description");
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

    // Testing the final skin.
    public void createFinalSkinButtons() {
        TextButton button1 = new TextButton("Final Skin 1", finalSkin, "item");
        button1.setWidth(300f);
        button1.setHeight(100f);
        button1.setPosition(game.pixelWidth /2 + button1.getWidth(),
                (game.pixelHeight/3) *2 - button1.getHeight() *2);
        stage.addActor(button1);
        button1.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Dialog d = new Dialog("", finalSkin, "item");
                d.setPosition(game.pixelWidth/2, game.pixelHeight/2);
                d.setSize(300, 300);
                d.text("Testing 123");
                stage.addActor(d);
            }
        });

        TextButton button2 = new TextButton("Final Skin 2", finalSkin);
        button2.setPosition(game.pixelWidth /2 + button2.getWidth(),
                (game.pixelHeight/3) *2);
        stage.addActor(button2);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        stage.draw();
        if (startHack) {
            hacking.update();
        }
    }

    public static Preferences getPrefs() {
        return prefs;
    }
    public static String getName() { return name; }
}
