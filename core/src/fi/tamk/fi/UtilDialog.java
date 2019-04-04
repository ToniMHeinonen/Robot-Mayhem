package fi.tamk.fi;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

    public void createDialog(String text, float x, float y) {
        dialogOn = true;
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        final Dialog dialog = new Dialog("", windowStyle);
        dialog.getContentTable().add(label).prefWidth(400);
        dialog.setPosition(x, y);
        // dialog.getBackground().getMinHeight()
        // dialog.getBackground().getMinWidth()
        dialog.setSize(label.getWidth() + 50,label.getHeight()*4f);
        stage.addActor(dialog);
        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.remove();
                dialogOn = false;
            }
        });
    }

    // Start of 3 different methods.
    public Label createHeader(String header, float x, float y) {
        Label labelHeader = new Label(header, skin);
        labelHeader.setColor(fontColor);
        labelHeader.setPosition(x, y);

        return labelHeader;
    }

    public Label createDescription(String description, float x, float y) {
        Label labelDescription = new Label(description, skin);
        labelDescription.setWrap(true);
        labelDescription.setWidth(300);
        labelDescription.setHeight(labelDescription.getPrefHeight());
        labelDescription.setColor(fontColor);
        labelDescription.setPosition(x, y);

        return labelDescription;
    }

    public Dialog createNewDialog(float x, float y) {
        Dialog dialog = new Dialog("", skin);
        dialog.setMovable(false);
        dialog.setPosition(x, y);

        return dialog;
    }
    // End of 3 different methods.

    // Start of all-in-one method.
    public Dialog createCombined(String header, float headerX, float headerY,
                                 String description, float descriptionX, float descriptionY,
                                 float dialogX, float dialogY) {

        Dialog dialog = new Dialog("", skin);
        dialog.setMovable(false);
        dialog.setPosition(dialogX, dialogY);

        Label labelHeader = new Label(header, skin);
        labelHeader.setColor(fontColor);
        labelHeader.setPosition(headerX, headerY);

        Label labelDescription = new Label(description, skin);
        labelDescription.setWrap(true);
        labelDescription.setWidth(300);
        labelDescription.setHeight(labelDescription.getPrefHeight());
        labelDescription.setColor(fontColor);
        labelDescription.setPosition(descriptionX, descriptionY);

        dialog.addActor(labelHeader);
        dialog.addActor(labelDescription);

        return dialog;
    }
    // End of all-ine-one method.

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
