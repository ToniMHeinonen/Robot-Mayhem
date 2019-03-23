package fi.tamk.fi;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class UtilDialog {
    private MainGame game;
    private Stage stage;
    private boolean dialogOn;

    private Label.LabelStyle labelStyle;
    private Window.WindowStyle windowStyle;
    private com.badlogic.gdx.graphics.Color fontColor;

    UtilDialog(MainGame game) {
        this.game = game;
        stage = game.getStage();
        labelStyle = game.getLabelStyle();
        windowStyle = game.getWindowStyle();
        fontColor = game.getFontColor();
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

    public boolean isDialogOn() {
        return dialogOn;
    }
}
