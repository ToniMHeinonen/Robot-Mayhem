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
            fightHackingFinished, fightPowerupFinished;

    private Dialog whoAmI;

    private int diaCounter;
    private boolean allowClicking;
    private float clickTimer = 1f;

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
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    inventoryInstructions();
                }
            }, 0.5f);
        } else if (tutorial.equals("bank")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    bankInstructions();
                }
            }, 0.5f);
        } else if (tutorial.equals("victory")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    victoryInstructions();
                }
            }, 0.5f);
        } else if (tutorial.equals("pool1Complete")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    pool1CompleteInstructions();
                }
            }, 0.5f);
        } else if (tutorial.equals("pool2Complete")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    pool2CompleteInstructions();
                }
            }, 0.5f);
        } else if (tutorial.equals("pool3Complete")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    pool3CompleteInstructions();
                }
            }, 0.5f);
        } else if (tutorial.equals("money")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    moneyInstructions();
                }
            }, 0.5f);
        } else if (tutorial.equals("escape")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    escapeInstructions();
                }
            }, 0.5f);
        } else if (tutorial.equals("death")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    deathInstructions();
                }
            }, 1f);
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
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.format("tutFirstPlay2") + " " + name + "!",
                localize.get("tutFirstPlay3"),
                localize.get("tutFirstPlay4"),
                localize.get("tutFirstPlay5"),
                localize.get("tutFirstPlay6"),
                localize.get("tutFirstPlay7")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) hallAllInstructions();
                    else {
                        game.setFirstPlayTime(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    private void fightStartInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutFightStart1"),
                localize.get("tutFightStart2")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) fightStartInstructions();
                    else {
                        fightStartFinished = true;
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    public void fightAfterHitInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutFightAfterHit1"),
                localize.get("tutFightAfterHit2")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) fightAfterHitInstructions();
                    else {
                        fightAfterHitFinished = true;
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    public void fightActionInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutFightAction1"),
                localize.get("tutFightAction2"),
                localize.get("tutFightAction3"),
                localize.get("tutFightAction4")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) fightActionInstructions();
                    else {
                        fightActionFinished = true;
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    public void fightHackingInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutFightHacking1"),
                localize.get("tutFightHacking2"),
                localize.get("tutFightHacking3"),
                localize.get("tutFightHacking4")};

        final Dialog dialog = utilDialog.createInstructionsDialog
                (texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) fightHackingInstructions();
                    else {
                        fightHackingFinished = true;
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    public void fightPowerupInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutFightPowerUp1"),
                localize.get("tutFightPowerUp2"),
                localize.get("tutFightPowerUp3"),
                localize.get("tutFightPowerUp4")};

        final Dialog dialog = utilDialog.createInstructionsDialog
                (texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) fightPowerupInstructions();
                    else {
                        fightPowerupFinished = true;
                        game.setfirstPlayTimeFight(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    private void inventoryInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutInventory1"),
                localize.get("tutInventory2"),
                localize.get("tutInventory3"),
                localize.get("tutInventory4")};
        Label label = new Label(texts[diaCounter], finalSkin, "font46");
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
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) inventoryInstructions();
                    else {
                        game.setFirstPlayInventory(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    private void bankInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutBank1"),
                localize.get("tutBank2"),
                localize.get("tutBank3"),
                localize.get("tutBank4"),
                localize.get("tutBank5"),};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) bankInstructions();
                    else {
                        game.setFirstPlayBank(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    private void victoryInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutVictory1"),
                localize.get("tutVictory2")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) victoryInstructions();
                    else {
                        game.setFirstPlayVictory(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    private void pool1CompleteInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutPool1Complete1"),
                localize.get("tutPool1Complete2")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) pool1CompleteInstructions();
                    else {
                        game.setFirstPlayPoolComplete1(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    private void pool2CompleteInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutPool2Complete1"),
                localize.get("tutPool2Complete2"),
                localize.get("tutPool2Complete3")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) pool2CompleteInstructions();
                    else {
                        game.setFirstPlayPoolComplete2(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    private void pool3CompleteInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutPool3Complete1"),
                localize.get("tutPool3Complete2"),
                localize.get("tutPool3Complete3")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) pool3CompleteInstructions();
                    else {
                        game.setFirstPlayPoolComplete3(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    private void moneyInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutMoney1"),
                localize.get("tutMoney2"),
                localize.get("tutMoney3"),
                localize.get("tutMoney4")};

        Label label = new Label(texts[diaCounter], finalSkin, "font46");
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
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) moneyInstructions();
                    else {
                        game.setFirstPlayMoney(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    private void escapeInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutEscape1"),
                localize.get("tutEscape2"),
                localize.get("tutEscape3")};

        Label label = new Label(texts[diaCounter], finalSkin, "font46");
        label.setWrap(true);
        label.setAlignment(1);

        final Dialog dialog = new Dialog("", finalSkin, "skilldescription");
        dialog.getContentTable().add(label).prefWidth(720);
        dialog.setSize(800, 540);
        dialog.setPosition(game.pixelWidth/2 - 400, game.pixelHeight/4);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) escapeInstructions();
                    else {
                        game.setFirstPlayEscape(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    private void deathInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutDeath1"),
                localize.get("tutDeath2"),
                localize.get("tutDeath3")};

        Label label = new Label(texts[diaCounter], finalSkin, "font46");
        label.setWrap(true);
        label.setAlignment(1);

        final Dialog dialog = new Dialog("", finalSkin, "skilldescription");
        dialog.getContentTable().add(label).prefWidth(720);
        dialog.setSize(800, 540);
        dialog.setPosition(game.pixelWidth/2 - 400, game.pixelHeight/4);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) deathInstructions();
                    else {
                        game.setFirstPlayDeath(false);
                        diaCounter = 0;
                    }
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

    public boolean isFightHackingFinished() {
        return fightHackingFinished;
    }

    public boolean isFightPowerupFinished() {
        return fightPowerupFinished;
    }
}
