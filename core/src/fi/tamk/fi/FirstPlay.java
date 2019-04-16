package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;

public class FirstPlay {
    private MainGame game;
    private RoomParent curRoom;
    private Stage stage;
    private Skin finalSkin;
    private I18NBundle localize;
    private UtilDialog utilDialog;
    private String name;
    private boolean firstPlayTime;
    private boolean firstPlayTimeFight;

    private Dialog whoAmI;
    private int hallCounter = 0;
    private int fightCounter = 0;

    FirstPlay(final MainGame game, String room, RoomParent curRoom) {
        this.game = game;
        this.curRoom = curRoom;
        stage = game.getStage();
        finalSkin = game.getFinalSkin();
        localize = game.getLocalize();
        utilDialog = game.getDialog();
        name = game.getPlayerName();
        firstPlayTime = game.isFirstPlayTime();
        firstPlayTimeFight = game.isFirstPlayTimeFight();

        if (firstPlayTime && room.equals("hall")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    hallInstructionsName();
                }
            }, 1);
        }
        if (firstPlayTimeFight && room.equals("fight")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    fightAllInstructions();
                }
            }, 1);
        }
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
                if (hallCounter < hallGuide.length) hallAllInstructions();
                else game.setFirstPlayTime(false);
            }
        });
    }

    private void fightAllInstructions() {
        final String[] fightGuide = new String[] {
                "This game has turn based combat.",
                "If you don't know how that works, it's your own fault.",
                "L2P"};

        final Dialog dialog = utilDialog.createInstructionsDialog(fightGuide[fightCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                fightCounter++;
                dialog.remove();
                if (fightCounter < fightGuide.length) fightAllInstructions();
                else {
                    curRoom.tutorialFinished();
                    game.setfirstPlayTimeFight(false);
                }
            }
        });
    }
}
