package fi.phstudios.robotmayhem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
    private boolean fightStartFinished, fightAfterHitFinished, fightActionFinished,
            fightHackingStartFinished;

    private Dialog whoAmI;
    // Olli notice: int default value is 0, it does not have to be declared
    private int hallCounter, fightStartCounter, fightAfterHitCounter, fightActionCounter,
            fightHackingStartCounter, inventoryCounter, bankCounter;

    FirstPlay(final MainGame game, String tutorial, RoomParent curRoom) {
        this.game = game;
        this.curRoom = curRoom;
        stage = game.getStage();
        finalSkin = game.getFinalSkin();
        localize = game.getLocalize();
        utilDialog = game.getDialog();
        name = game.getPlayerName();

        if (tutorial.equals("firstPlay")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    hallInstructionsName();
                }
            }, 1);
        } else if (tutorial.equals("fight")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    fightStartInstructions();
                }
            }, 1);
        } else if (tutorial.equals("inventory")) {
            inventoryInstructions();
        } else if (tutorial.equals("bank")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    bankInstructions();
                }
            }, 0.5f);
        }

    }

    private void hallInstructionsName() {
        whoAmI = utilDialog.createInstructionsDialog(localize.get("tutFirstPlay1"));
        stage.addActor(whoAmI);

        final Input.TextInputListener textInputListener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                if (text.length() > 0 && text.length() < 13) {
                    name = text;
                    game.setPlayerName(text);
                    whoAmI.remove();
                    hallAllInstructions();
                }
            }

            @Override
            public void canceled() {
            }
        };

        whoAmI.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.input.getTextInput(textInputListener, "Your name", "", "1-12 characters");
            }
        });
    }

    private void hallAllInstructions() {
        final String[] hallGuide = new String[] {
                localize.format("tutFirstPlay2") + " " + name + "!",
                localize.get("tutFirstPlay3"),
                localize.get("tutFirstPlay4"),
                localize.get("tutFirstPlay5"),
                localize.get("tutFirstPlay6"),
                localize.get("tutFirstPlay7")};

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
                localize.get("tutFightStart1"),
                localize.get("tutFightStart2")};

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
                localize.get("tutFightAfterHit1"),
                localize.get("tutFightAfterHit2")};

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
                localize.get("tutFightAction1"),
                localize.get("tutFightAction2"),
                localize.get("tutFightAction3"),
                localize.get("tutFightAction4")};

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

    public void fightHackingInstructions() {
        final String[] fightGuide = new String[] {
                localize.get("tutFightHacking1"),
                localize.get("tutFightHacking2"),
                localize.get("tutFightHacking3"),
                localize.get("tutFightHacking4")};

        final Dialog dialog = utilDialog.createInstructionsDialog
                (fightGuide[fightHackingStartCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                fightHackingStartCounter++;
                dialog.remove();
                if (fightHackingStartCounter < fightGuide.length) fightHackingInstructions();
                else {
                    fightHackingStartFinished = true;
                    game.setfirstPlayTimeFight(false);
                }
            }
        });
    }

    private void inventoryInstructions() {
        final String[] fightGuide = new String[] {
                localize.get("tutInventory1"),
                localize.get("tutInventory2"),
                localize.get("tutInventory3"),
                localize.get("tutInventory4")};
        Label label = new Label(fightGuide[inventoryCounter], finalSkin, "font46");
        label.setWrap(true);
        label.setAlignment(1);

        final Dialog dialog = new Dialog("", finalSkin, "skilldescription");
        dialog.getContentTable().add(label).prefWidth(720);
        dialog.setPosition(game.pixelWidth/4, game.pixelHeight/4);
        dialog.setSize(800, 540);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                inventoryCounter++;
                dialog.remove();
                if (inventoryCounter < fightGuide.length) inventoryInstructions();
                else game.setFirstPlayInventory(false);
            }
        });
    }

    private void bankInstructions() {
        final String[] fightGuide = new String[] {
                localize.get("tutBank1"),
                localize.get("tutBank2"),
                localize.get("tutBank3"),
                localize.get("tutBank4"),
                localize.get("tutBank5"),};

        final Dialog dialog = utilDialog.createInstructionsDialog(fightGuide[bankCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                bankCounter++;
                dialog.remove();
                if (bankCounter < fightGuide.length) bankInstructions();
                else {
                    game.setFirstPlayBank(false);
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
