package fi.tamk.fi;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;

public class UtilDialog {
    private MainGame game;
    private Stage stage;
    private boolean dialogOn, skillNameOn;
    private Skin skin;

    private Label.LabelStyle labelStyle;
    private Window.WindowStyle windowStyle;
    private com.badlogic.gdx.graphics.Color fontColor;

    UtilDialog(MainGame game) {
        this.game = game;
        stage = game.getStage();
        labelStyle = game.getLabelStyle();
        windowStyle = game.getWindowStyle();
        fontColor = game.getFontColor();
        skin = game.getSkin();
    }

    public void createDialog(String text) {
        dialogOn = true;
        float areaWidth = 1000f;
        float areaHeight = 600f;
        float x = game.pixelWidth/2 - areaWidth/2;
        float y = 300f;
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        label.setAlignment(1);
        final Dialog dialog = new Dialog("", windowStyle);
        dialog.getContentTable().add(label).prefWidth(areaWidth);
        dialog.setPosition(x, y);
        // dialog.getBackground().getMinHeight()
        // dialog.getBackground().getMinWidth()
        dialog.setSize(areaWidth,areaHeight);
        stage.addActor(dialog);
        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.remove();
                dialogOn = false;
            }
        });
    }

    // Start of all-in-one method.
    public Dialog createPopupItemAndPowerUp(String header, String description) {

        Dialog dialog = new Dialog("", skin);
        dialog.setMovable(false);
        dialog.setSize(game.pixelWidth/2,
                game.pixelHeight/2);
        dialog.setPosition(game.pixelWidth/4,
                game.pixelHeight/3);

        Label labelHeader = new Label(header, skin);
        labelHeader.setColor(fontColor);
        labelHeader.setPosition(dialog.getWidth()/2 - labelHeader.getWidth()/2, dialog.getHeight() - 100);

        Label labelDescription = new Label(description, skin);
        labelDescription.setWrap(true);
        labelDescription.setWidth(dialog.getWidth() - 100);
        labelDescription.setHeight(labelDescription.getPrefHeight());
        labelDescription.setColor(fontColor);
        labelDescription.setPosition(dialog.getWidth() - dialog.getWidth() + 50,
                labelHeader.getY() - 100);

        dialog.addActor(labelHeader);
        dialog.addActor(labelDescription);

        return dialog;
    }

    public void showSkillName(String text) {
        skillNameOn = true;
        float areaSize = 800f;
        float x = game.pixelWidth/2 - areaSize/2;
        float y = game.pixelHeight - 350f;
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        label.setAlignment(1);

        final Dialog dialog = new Dialog("", windowStyle);
        dialog.getContentTable().add(label).prefWidth(areaSize);

        // dialog.getBackground().getMinHeight()
        // dialog.getBackground().getMinWidth()
        dialog.setSize(areaSize,label.getHeight()*2);
        dialog.setPosition(x,y);
        stage.addActor(dialog);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                skillNameOn = false;
                dialog.remove();
            }
        }, 2f);
    }

    public boolean isDialogOn() {
        return dialogOn;
    }

    public boolean isSkillNameOn() {
        return skillNameOn;
    }
}
