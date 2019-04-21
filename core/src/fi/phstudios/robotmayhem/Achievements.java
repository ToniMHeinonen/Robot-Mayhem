package fi.phstudios.robotmayhem;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class Achievements {
    private MainGame game;
    private Skin finalSkin;
    private Stage stage;
    private String room;
    private RoomParent curRoom;
    private I18NBundle localize;
    private float stepAllCount;
    private String lan;
    private boolean finishedGame;

    private Dialog dialogAch;
    private ImageButton btnCancel;

    private String[] achHeaders;
    private String[] achDescriptions;
    private String[] achLocked;

    private float space = 200f;

    Achievements(MainGame game) {
        this.game = game;
        stage = game.getStage();
        finalSkin = game.getFinalSkin();
        stepAllCount = game.getStepAllCount();
        lan = game.getLanguage();
        localize = game.getLocalize();
        finishedGame = game.isFinishedGame();

        createAchDialog();
        createHeadersAndDescriptions();
        checkAchievements();
        createButtons();
        createExitButton();
        stage.addActor(dialogAch);
    }

    private void createAchDialog() {
        dialogAch = new Dialog("", finalSkin, "stats");
        dialogAch.setMovable(false);
        dialogAch.setPosition(0,0);
        dialogAch.setSize(game.pixelWidth, game.pixelHeight);
    }

    private void createHeadersAndDescriptions() {
        achHeaders = new String[] {
                localize.get("sundayWalker"),
                localize.get("finisher"),
                "Achievement 3"};

        achDescriptions = new String[] {
                localize.get("sundayWalkerDesc"),
                localize.get("finisherDesc"),
                "Achievement 3 description (20 steps)"};

        achLocked = new String[] {
                "locked",
                "locked",
                "locked"};
    }

    private void checkAchievements() {
        // Achievement 1 / Sunday Walker
        if (stepAllCount >= 50) achLocked[0] = "unlocked";

        // Achievement 2 / Finisher
        if (finishedGame) achLocked[1] = "unlocked";

        // Achievement 3
        if (stepAllCount >= 20) achLocked[2] = "unlocked";
    }

    private void createButtons() {
        for (int i = 0; i < achHeaders.length; i++) {
            final int btnCounter = i;
            ImageButton imgBtn = new ImageButton(finalSkin, achLocked[i]);
            imgBtn.setPosition(400 + i*space, 400);
            dialogAch.addActor(imgBtn);
            imgBtn.addListener(new ClickListener(){
                int i = btnCounter;
                @Override
                public void clicked(InputEvent event, float x, float y){
                    popupAchievement(i);
                }
            });
        }
    }

    private void popupAchievement(int index) {
        Label label = new Label(achDescriptions[index], finalSkin, "font46");
        label.setWrap(true);
        label.setAlignment(1);

        final Dialog dialog = new Dialog("", finalSkin, "popup_powerup");
        dialog.getContentTable().add(label).prefWidth(720);
        dialog.setPosition(0,0);
        dialog.setSize(game.pixelWidth, game.pixelHeight);

        Label header = new Label(achHeaders[index], finalSkin);
        header.setPosition(game.pixelWidth / 4, game.pixelHeight / 2 + 195);
        header.setSize(960, 100);
        header.setAlignment(1);
        dialog.addActor(header);

        String stringLocked = localize.get("locked");
        if (achLocked[index].equals("unlocked")) stringLocked = localize.get("unlocked");

        Label locked = new Label(stringLocked, finalSkin);
        locked.setPosition(game.pixelWidth / 4, 350);
        locked.setSize(960, 100);
        locked.setAlignment(1);
        dialog.addActor(locked);

        btnCancel = new ImageButton(finalSkin, "cancel_" + lan);
        btnCancel.setPosition(game.pixelWidth / 2 - btnCancel.getWidth()/2, 210);
        dialog.addActor(btnCancel);
        btnCancel.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.remove();
            }
        });

        stage.addActor(dialog);
    }

    private void createExitButton() {
        ImageButton buttonExit = new ImageButton(finalSkin, "x");
        buttonExit.setPosition(1550, 960);
        buttonExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialogAch.remove();
            }
        });

        dialogAch.addActor(buttonExit);
    }
}
