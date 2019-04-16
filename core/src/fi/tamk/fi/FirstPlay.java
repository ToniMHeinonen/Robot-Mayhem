package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

public class FirstPlay {
    private MainGame game;
    private Stage stage;
    private Skin finalSkin;
    private I18NBundle localize;
    private UtilDialog utilDialog;
    private String name;

    private Dialog whoAmI;
    int hallCounter = 0;

    FirstPlay(MainGame game) {
        this.game = game;
        stage = game.getStage();
        finalSkin = game.getFinalSkin();
        localize = game.getLocalize();
        utilDialog = game.getDialog();
        name = game.getPlayerName();

        game.setFirstPlayTime(false);

        hallInstructionsName();
    }

    private void hallInstructionsName() {
        whoAmI = utilDialog.createInstructionsDialog("Who am I...");
        stage.addActor(whoAmI);

        final Input.TextInputListener textInputListener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                name = text;
                game.setPlayerName(text);
                whoAmI.remove();
                hallAllInstructions();
            }

            @Override
            public void canceled() {
                whoAmI.remove();
                hallAllInstructions();
            }
        };

        whoAmI.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.input.getTextInput(textInputListener, "Your name", "", "");
            }
        });
    }

    private void hallAllInstructions() {
        final String[] hallGuide = new String[] {
                "Welcome to Robot Mayhem, " + name + "!",
                "First text",
                "Second text",
                "Third text"};

        final Dialog dialog = utilDialog.createInstructionsDialog(hallGuide[hallCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                hallCounter++;
                dialog.remove();
                if (hallCounter < hallGuide.length) {
                    hallAllInstructions();
                }
            }
        });
    }
}
