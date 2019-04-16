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
    private Skin finalSkin;

    UtilDialog(MainGame game) {
        this.game = game;
        stage = game.getStage();
        finalSkin = game.getFinalSkin();
    }

    public void createDialog(String text, String style) {
        dialogOn = true;
        float areaWidth = 780f;
        float areaHeight = 540f;
        float x = game.pixelWidth/2 - areaWidth/2;
        float y = 300f;

        Label label = new Label(text, finalSkin);
        label.setWrap(true);
        label.setAlignment(1);

        final Dialog dialog = new Dialog("", finalSkin, style);
        dialog.getContentTable().add(label).prefWidth(areaWidth - 80);
        dialog.setPosition(x, y);
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
    public Dialog createPopupItemAndPowerUp(String header, String description, String style) {
        String fontSize = "big";
        if (header.length() >= 17) {
            fontSize = "default";
        }

        Dialog dialog = new Dialog("", finalSkin, style);
        dialog.setMovable(false);
        dialog.setSize(game.pixelWidth, game.pixelHeight);
        dialog.setPosition(0, 0);

        Label labelHeader = new Label(header, finalSkin, fontSize);
        labelHeader.setPosition(game.pixelWidth / 4, game.pixelHeight / 2 + 195);
        labelHeader.setSize(960, 100);
        labelHeader.setAlignment(1);

        Label labelDescription = new Label(description, finalSkin);
        labelDescription.setWrap(true);
        labelDescription.setWidth(960);
        labelDescription.setHeight(360);
        labelDescription.setPosition(labelHeader.getX(),
                labelHeader.getY() - 370);
        labelDescription.setAlignment(1);

        dialog.addActor(labelHeader);
        dialog.addActor(labelDescription);

        return dialog;
    }

    public void showSkillName(String text, String skillStyle) {
        skillNameOn = true;
        float areaSize = 800f;
        float x = game.pixelWidth/2 - areaSize/2;
        float y = game.pixelHeight - 350f;

        Label label = new Label(text, finalSkin);
        label.setWrap(true);
        label.setAlignment(1);

        final Dialog dialog = new Dialog("", finalSkin, skillStyle);
        dialog.getContentTable().add(label).prefWidth(areaSize);
        dialog.setSize(areaSize,label.getHeight()*2);
        dialog.setPosition(x,y);
        stage.addActor(dialog);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                skillNameOn = false;
                dialog.remove();
            }
        }, 1f);
    }

    public boolean isDialogOn() {
        return dialogOn;
    }

    public boolean isSkillNameOn() {
        return skillNameOn;
    }
}
