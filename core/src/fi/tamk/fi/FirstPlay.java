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
    private boolean firstPlayTime, firstPlayTimeFight, fightStartFinished, fightAfterHitFinished,
            fightActionFinished, fightHackingStartFinished;

    private Dialog whoAmI;
    // Olli notice: int default value is 0, it does not have to be declared
    private int hallCounter, fightStartCounter, fightAfterHitCounter, fightActionCounter,
            fightHackingStartCounter;

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
                    fightStartInstructions();
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

    private void fightStartInstructions() {
        final String[] fightGuide = new String[] {
                "Ahh it's my fellow friend.",
                "How's the cleaning going?"};

        final Dialog dialog = utilDialog.createInstructionsDialog(fightGuide[fightStartCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                fightStartCounter++;
                dialog.remove();
                if (fightStartCounter < fightGuide.length) fightStartInstructions();
                else {
                    fightStartFinished = true;
                }
            }
        });
    }

    public void fightAfterHitInstructions() {
        final String[] fightGuide = new String[] {
                "That robot is out of his mind...",
                "I'll have to knock some sense into him!"};

        final Dialog dialog = utilDialog.createInstructionsDialog(fightGuide[fightAfterHitCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                fightAfterHitCounter++;
                dialog.remove();
                if (fightAfterHitCounter < fightGuide.length) fightAfterHitInstructions();
                else {
                    fightAfterHitFinished = true;
                }
            }
        });
    }

    public void fightActionInstructions() {
        final String[] fightGuide = new String[] {
                "Select which action to execute.",
                "If you hold the action button it will tell you what it does.",
                "You can also buy and use items from your inventory.",
                "Now let's help my friend!"};

        final Dialog dialog = utilDialog.createInstructionsDialog(fightGuide[fightActionCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                fightActionCounter++;
                dialog.remove();
                if (fightActionCounter < fightGuide.length) fightActionInstructions();
                else {
                    fightActionFinished = true;
                }
            }
        });
    }

    public void fightHackingStartInstructions() {
        final String[] fightGuide = new String[] {
                "Now is my time to hack through it's firewall.",
                "Press the button below to stream my data.",
                "Try to aim inside the ring.",
                "You can do it!"};

        final Dialog dialog = utilDialog.createInstructionsDialog
                (fightGuide[fightHackingStartCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                fightHackingStartCounter++;
                dialog.remove();
                if (fightHackingStartCounter < fightGuide.length) fightHackingStartInstructions();
                else {
                    fightHackingStartFinished = true;
                }
            }
        });
    }

    public boolean isFightStartFinished() {
        return fightStartFinished;
    }

    public boolean isFightAfterHitFinished() {
        return fightAfterHitFinished;
    }

    public boolean isFightActionFinished() {
        return fightActionFinished;
    }

    public boolean isFightHackingStartFinished() {
        return fightHackingStartFinished;
    }
}
