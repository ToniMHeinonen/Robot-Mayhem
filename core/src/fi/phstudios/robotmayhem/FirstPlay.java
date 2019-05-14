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
    private Stage stage;
    private Skin finalSkin;
    private I18NBundle localize;
    private UtilDialog utilDialog;
    private String name;
    private boolean fightStartFinished, fightAfterHitFinished, fightActionFinished,
            fightHackingFinished, fightPowerupFinished;

    private boolean finalFightStartFinished, finalFightAfterStartFinished,
            finalFightBeforeEndFinished, finalFightEndFinished;

    private Dialog whoAmI;

    private int diaCounter;
    private boolean allowClicking;
    private float clickTimer = 0.75f; // If you change this, also change UtilDialog's timer

    /**
     * Retrieve correct values from main game instance.
     * @param game main game instance
     * @param tutorial which tutorial to start
     */
    FirstPlay(final MainGame game, String tutorial) {
        this.game = game;
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
        } else if (tutorial.equals("settings")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    settingsInstructions();
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
        } else if (tutorial.equals("finalFight")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    finalFightStartInstructions();
                }
            }, 1f);
        } else if (tutorial.equals("newGamePlus")) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    newGamePlusInstructions();
                }
            }, 1f);
        }
    }

    /**
     * Retrieve player's name.
     */
    private void hallInstructionsName() {
        whoAmI = utilDialog.createInstructionsDialog(localize.get("tutFirstPlay1"));
        stage.addActor(whoAmI);
        game.setDialogType(game.DIAL_PLAYER);


        final Input.TextInputListener textInputListener = new Input.TextInputListener() {
            @Override
            public void input(String text) {
                // Only change name and spawn next tutorial if name has not been yet changed
                if (game.getPlayerName() == "") {
                    if (text.length() > 0 && text.length() < 13) {
                        name = text;
                        game.setPlayerName(text);
                        whoAmI.remove();
                        hallAllInstructions();
                        game.setDialogType(game.DIAL_STOP);
                    }
                }
            }

            @Override
            public void canceled() {
            }
        };

        whoAmI.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                /*
                Reset name in case of player enters name and closes the game before finishing the
                tutorial. Without this the game get's stuck in asking name.
                 */
                game.setPlayerName("");
                Gdx.input.getTextInput(textInputListener, "Your name", "", "1-12 characters");
            }
        });
    }

    /**
     * Show game start tutorial.
     */
    private void hallAllInstructions() {

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.format("tutFirstPlay2") + " " + name + "!",
                localize.get("tutFirstPlay3"),
                localize.get("tutFirstPlay4"),
                localize.get("tutFirstPlay5")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show fight start tutorial.
     */
    private void fightStartInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show fight after hit tutorial.
     */
    public void fightAfterHitInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show fight action tutorial.
     */
    public void fightActionInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show fight hacking tutorial.
     */
    public void fightHackingInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show fight powerup tutorial.
     */
    public void fightPowerupInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show inventory tutorial.
     */
    private void inventoryInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_BOX);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show settings tutorial.
     */
    private void settingsInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_BOX);
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutSettings1"),
                localize.get("tutSettings2"),
                localize.get("tutSettings3"),
                localize.get("tutSettings4")};
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
                    game.setDialogType(game.DIAL_STOP);
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) settingsInstructions();
                    else {
                        game.setFirstPlaySettings(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    /**
     * Show bank tutorial.
     */
    private void bankInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show after first won dialogue.
     */
    private void victoryInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show after first pool complete dialogue.
     */
    private void pool1CompleteInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show after second pool complete dialogue.
     */
    private void pool2CompleteInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show after third pool complete dialogue.
     */
    private void pool3CompleteInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show money tutorial.
     */
    private void moneyInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_BOX);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show escape tutorial.
     */
    private void escapeInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_BOX);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show death tutorial.
     */
    private void deathInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_BOX);
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
                    game.setDialogType(game.DIAL_STOP);
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

    /**
     * Show final fight start dialogue.
     */
    private void finalFightStartInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutFinalFightStart1"),
                localize.get("tutFinalFightStart2")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    game.setDialogType(game.DIAL_STOP);
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) finalFightStartInstructions();
                    else {
                        finalFightStartFinished = true;
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    /**
     * Show final fight after start dialogue.
     */
    public void finalFightAfterStartInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutFinalFightAfterStart1"),
                localize.get("tutFinalFightAfterStart2"),
                localize.get("tutFinalFightAfterStart3")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    game.setDialogType(game.DIAL_STOP);
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) finalFightAfterStartInstructions();
                    else {
                        finalFightAfterStartFinished = true;
                        game.setFirstPlayFinalFightStart(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    /**
     * Show final fight before end dialogue.
     */
    public void finalFightBeforeEndInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutFinalFightBeforeEnd1"),
                localize.get("tutFinalFightBeforeEnd2")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    game.setDialogType(game.DIAL_STOP);
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) finalFightBeforeEndInstructions();
                    else {
                        finalFightBeforeEndFinished = true;
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    /**
     * Show final fight end dialogue.
     */
    public void finalFightEndInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutFinalFightEnd1"),
                localize.get("tutFinalFightEnd2")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    game.setDialogType(game.DIAL_STOP);
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) finalFightEndInstructions();
                    else {
                        finalFightEndFinished = true;
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    public void newGamePlusInstructions() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                allowClicking = true;
                game.setDialogType(game.DIAL_PLAYER);
            }
        }, clickTimer);
        final String[] texts = new String[] {
                localize.get("tutNewGamePlus1"),
                localize.get("tutNewGamePlus2"),
                localize.get("tutNewGamePlus3")};

        final Dialog dialog = utilDialog.createInstructionsDialog(texts[diaCounter]);
        stage.addActor(dialog);

        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (allowClicking) {
                    allowClicking = false;
                    game.setDialogType(game.DIAL_STOP);
                    diaCounter++;
                    dialog.remove();
                    if (diaCounter < texts.length) newGamePlusInstructions();
                    else {
                        game.setFirstPlayNewGamePlus(false);
                        diaCounter = 0;
                    }
                }
            }
        });
    }

    /**
     * Used for checking if fight start has finished.
     * @return if finished
     */
    public boolean isFightStartFinished() {
        return fightStartFinished;
    }

    /**
     * Used for checking if fight after hit has finished.
     * @return if finished
     */
    public boolean isFightAfterHitFinished() {
        return fightAfterHitFinished;
    }

    /**
     * Used for checking if fight action has finished.
     * @return if finished
     */
    public boolean isFightActionFinished() {
        return fightActionFinished;
    }

    /**
     * Used for checking if fight hacking has finished.
     * @return if finished
     */
    public boolean isFightHackingFinished() {
        return fightHackingFinished;
    }

    /**
     * Used for checking if fight powerup has finished.
     * @return if finished
     */
    public boolean isFightPowerupFinished() {
        return fightPowerupFinished;
    }

    /**
     * Used for checking if final fight start has finished.
     * @return if finished
     */
    public boolean isFinalFightStartFinished() {
        return finalFightStartFinished;
    }

    /**
     * Used for checking if final fight after start has finished.
     * @return if finished
     */
    public boolean isFinalFightAfterStartFinished() {
        return finalFightAfterStartFinished;
    }

    /**
     * Used for checking if final fight before end has finished.
     * @return if finished
     */
    public boolean isFinalFightBeforeEndFinished() {
        return finalFightBeforeEndFinished;
    }

    /**
     * Used for checking if final fight end has finished.
     * @return if finished
     */
    public boolean isFinalFightEndFinished() {
        return finalFightEndFinished;
    }
}
