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

    private Dialog dialogAch;
    private ImageButton btnSteps;

    Achievements(MainGame game) {
        this.game = game;
        stage = game.getStage();
        finalSkin = game.getFinalSkin();
        stepAllCount = game.getStepAllCount();

        createAchDialog();
        createStepAch();
        createExitButton();
        stage.addActor(dialogAch);
    }

    private void createAchDialog() {
        dialogAch = new Dialog("", finalSkin, "stats");
        dialogAch.setMovable(false);
        dialogAch.setPosition(0,0);
        dialogAch.setSize(game.pixelWidth, game.pixelHeight);
    }

    private void createStepAch() {
        if (stepAllCount < 10) {
            btnSteps = new ImageButton(finalSkin, "ach_steps_locked");
        } else {
            btnSteps = new ImageButton(finalSkin, "ach_steps");
        }
        btnSteps.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                popupStepAch();
            }
        });
        btnSteps.setPosition(300, 300);
        dialogAch.addActor(btnSteps);
    }

    private void popupStepAch() {
        Label label = new Label("Took 10 steps.", finalSkin, "font46");
        label.setWrap(true);
        label.setAlignment(1);

        final Dialog dialog = new Dialog("", finalSkin, "popup_powerup");
        dialog.getContentTable().add(label).prefWidth(720);
        dialog.setPosition(0,0);
        dialog.setSize(game.pixelWidth, game.pixelHeight);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.remove();
            }
        });
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
