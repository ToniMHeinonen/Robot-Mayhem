package fi.phstudios.robotmayhem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RoomFight extends RoomParent {

    /**
     * Enums give simple constants, which decreases the chance for coding mistakes. These handle
     * the state of battle.
     */
    enum State {
        TUTORIAL_START,
        TUTORIAL_AFTER_HIT,
        TUTORIAL_ACTION,
        TUTORIAL_HACKING,
        TUTORIAL_POWERUP,
        FINALFIGHT_START,
        FINALFIGHT_AFTER_START,
        FINALFIGHT_BEFORE_END,
        FINALFIGHT_END,
        START_ROOM,
        DIALOG_START,
        DIALOG_END,
        PLAYER_TURN,
        ENEMY_TURN,
        HACK,
        HACK_SUCCESS,
        HACK_FAILED,
        HACK_RESTORING,
        POWER_UP,
        DEAD,
        ESCAPE,
        CHANGE_ROOM,
        TIMER
    }

    private Texture hpBarLeft = files.hpBarLeft;
    private Texture hpBarRight = files.hpBarRight;
    private Texture imgDoTArrowUp = files.dotArrowUp;
    private Texture imgDoTArrowDown = files.dotArrowDown;
    private Texture imgBg, escapeBg;
    private Animation<TextureRegion> playerHealthBar, enemyHealthBar;
    private Animating animHealthPlayer = new Animating();
    private Animating animHealthEnemy = new Animating();
    private Player player;
    private Enemy enemy;
    private int btnCounter; // Used for button classes to get the correct value
    private State state;
    private boolean escapePopup, spawnHacking, actionButtonsOn, spawnPowerUp;
    private int ARROW_NONE = 0, ARROW_UP = 1, ARROW_DOWN = 2;
    private boolean firstHack = true;
    private int deathTimer = 240;
    private boolean startDeathTimer;
    private ShaderProgram shFlashWhite;
    private FirstPlay tutorial, finalFight;
    private Hacking hacking;
    private UtilPowerUp powerUp;
    private String lan;

    /**
     * Retrieve values from game and create necessary variables.
     * @param game main game instance
     */
    RoomFight(final MainGame game) {
        super(game);
        imgBg = files.imgBgBoss;
        escapeBg = files.escapeBg;
        lan = game.getLanguage();

        if (game.isFirstPlayTimeFight()) {
            state = State.TUTORIAL_START;
            tutorial = new FirstPlay(game, "fight");
        } else if (game.getPool() == 4) {
            // If it's first match against Fabio, show starting dialogue
            if (game.isFirstPlayFinalFightStart()) {
                state = State.FINALFIGHT_START;
                finalFight = new FirstPlay(game, "finalFight");
            } else {
                state = State.START_ROOM;
                finalFight = new FirstPlay(game, "");
            }
        } else {
            state = State.START_ROOM;
        }

        createHealthBars();
        createShader(); // Used for flashing white

        player = new Player();
        enemy = new Enemy();
    }

    /**
     * Render all the frames of the game. Handle player and enemy's update methods.
     * @param delta time
     */
    @Override
    public void render(float delta) {
        super.render(delta);

        if (!game.haveWeChangedTheRoom) {

            universalStateChecks();
            if (tutorial != null) controlTutorial();
            if (finalFight != null) controlFinalFight();

            batch.begin();
            batch.draw(imgBg, 0,0, imgBg.getWidth(), imgBg.getHeight());
            drawTopAndBottomBar();
            drawHP();
            player.update();
            enemy.update();
            escaping();
            batch.end();
            stage.draw(); // Keep before hacking and powerup since they use the same stage
            endOfRender();

            hackingPhase();
            powerUpPhase();
        }
    }

    /**
     * Make player select the correct item from inventory.
     * @param item selected item
     */
    @Override
    public void selectItem(String item) {
        player.selectItem(item);
    }

    /**
     * Handle dialogs and dying.
     */
    private void universalStateChecks() {
        switch (state) {
            // If dialog box has been closed, start the turn
            case DIALOG_START: {
                if (!dialog.isDialogOn()) {
                    if (tutorial != null) enemy.startTurn();
                    else player.startTurn();
                }
                break;
            }
            // If dialog box has been closed, start the
            case DIALOG_END: {
                if (!dialog.isDialogOn()) {
                    state = State.TIMER;
                    if (finalFight == null) {
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                state = State.POWER_UP;
                            }
                        }, 0.5f);
                    }
                }
                break;
            }
            // If dead, wait some time and then exit back to corridor
            case DEAD: {
                if (!startDeathTimer) {
                    startDeathTimer = true;
                    game.playSound(files.sndLoseToBoss);
                    if (game.isFirstPlayDeath()) {
                        FirstPlay death = new FirstPlay(game, "death");
                    }
                }

                if (!game.isFirstPlayDeath()) {
                    if (deathTimer > 0) deathTimer--;
                    else game.switchToRoomGame();
                }
            }
        }
    }

    /**
     * Handle first fight's tutorial.
     */
    private void controlTutorial() {
        switch (state) {
            case TUTORIAL_START: {
                if (tutorial.isFightStartFinished()) {
                    state = State.START_ROOM;
                }
                break;
            }
            case PLAYER_TURN: {
                if (!tutorial.isFightAfterHitFinished()) {
                    state = State.TUTORIAL_AFTER_HIT;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            tutorial.fightAfterHitInstructions();
                        }
                    }, 1);
                } else if (!tutorial.isFightActionFinished()) {
                    if (actionButtonsOn) {
                        state = State.TUTORIAL_ACTION;
                        tutorial.fightActionInstructions();
                    }
                }
                break;
            }
            case TUTORIAL_AFTER_HIT: {
                if (tutorial.isFightAfterHitFinished()) {
                    state = State.PLAYER_TURN;
                }
                break;
            }
            case TUTORIAL_ACTION: {
                if (tutorial.isFightActionFinished()) {
                    state = State.PLAYER_TURN;
                }
                break;
            }
            case HACK: {
                if (!tutorial.isFightHackingFinished()) {
                    state = State.TUTORIAL_HACKING;
                    tutorial.fightHackingInstructions();
                }
                break;
            }
            case TUTORIAL_HACKING: {
                if (tutorial.isFightHackingFinished()) {
                    state = State.HACK;
                }
                break;
            }
            case POWER_UP: {
                if (!tutorial.isFightPowerupFinished()) {
                    state = State.TUTORIAL_POWERUP;
                    tutorial.fightPowerupInstructions();
                }
                break;
            }
            case TUTORIAL_POWERUP: {
                if (tutorial.isFightPowerupFinished()) {
                    state = State.POWER_UP;
                }
                break;
            }
        }
    }

    /**
     * Handle last fights dialogues.
     */
    private void controlFinalFight() {
        switch (state) {
            case FINALFIGHT_START: {
                if (finalFight.isFinalFightStartFinished()) {
                    state = State.START_ROOM;
                }
                break;
            }
            case PLAYER_TURN: {
                // Only do this if it's first time fighting Fabio
                if (game.isFirstPlayFinalFightStart()) {
                    if (!finalFight.isFinalFightAfterStartFinished()) {
                        state = State.FINALFIGHT_AFTER_START;
                        Timer.schedule(new Timer.Task() {
                            @Override
                            public void run() {
                                finalFight.finalFightAfterStartInstructions();
                            }
                        }, 1);
                    }
                }
                break;
            }
            case FINALFIGHT_AFTER_START: {
                if (finalFight.isFinalFightAfterStartFinished()) {
                    state = State.PLAYER_TURN;
                }
                break;
            }
            case HACK_SUCCESS: {
                if (!finalFight.isFinalFightBeforeEndFinished()) {
                    state = State.FINALFIGHT_BEFORE_END;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            finalFight.finalFightBeforeEndInstructions();
                        }
                    }, 1);
                }
                break;
            }
            case FINALFIGHT_BEFORE_END: {
                if (finalFight.isFinalFightBeforeEndFinished()) {
                    state = State.HACK_SUCCESS;
                    enemy.endDialogTimer();
                }
                break;
            }
            case TIMER: {
                if (!finalFight.isFinalFightEndFinished()) {
                    state = State.FINALFIGHT_END;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            finalFight.finalFightEndInstructions();
                        }
                    }, 1);
                }
                break;
            }
            case FINALFIGHT_END: {
                if (finalFight.isFinalFightEndFinished()) {
                    state = State.TIMER;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            state = State.POWER_UP;
                        }
                    }, 0.5f);
                }
                break;
            }
        }
    }

    /**
     * Create a shader to be used for blinking white when taking hit.
     */
    public void createShader() {
        // No idea what these does, I just found this code from google
        String vertexShader = "attribute vec4 a_position; \n" + "attribute vec4 a_color;\n" +
                "attribute vec2 a_texCoord0; \n" + "uniform mat4 u_projTrans; \n" +
                "varying vec4 v_color; \n" + "varying vec2 v_texCoords; \n" +
                "void main() { \n" + "v_color = a_color; \n" + "v_texCoords = a_texCoord0; \n" +
                "gl_Position = u_projTrans * a_position; \n" + "};";

        String fragmentShader = "#ifdef GL_ES\n" + "precision mediump float;\n" +
                "#endif\n" + "varying vec4 v_color;\n" + "varying vec2 v_texCoords;\n" +
                "uniform sampler2D u_texture;\n" + "uniform float grayscale;\n" + "void main()\n" +
                "{\n" + "vec4 texColor = texture2D(u_texture, v_texCoords);\n" +
                "float gray = dot(texColor.rgb, vec3(5, 5, 5));\n" +
                "texColor.rgb = mix(vec3(gray), texColor.rgb, grayscale);\n" +
                " gl_FragColor = v_color * texColor;\n" + "}";

        shFlashWhite = new ShaderProgram(vertexShader, fragmentShader);
        shFlashWhite.pedantic = false; // Without this, the game crashes on android

    }

    /**
     * Dispose hacking values when exiting room.
     */
    @Override
    public void hide() {
        if (hacking != null) hacking.dispose();
        super.hide();
    }

    /**
     * Create animating health bars.
     */
    private void createHealthBars() {
        // Create animations (Reversed since they are in wrong order)
        playerHealthBar = animHealthPlayer.createAnimationReverse(hpBarLeft, 1, 11);
        enemyHealthBar = animHealthEnemy.createAnimationReverse(hpBarRight, 1, 11);
        // "Start" the animation to initialize all the variables
        animHealthPlayer.startAnimation(playerHealthBar, 0);
        animHealthEnemy.startAnimation(enemyHealthBar, 0);
        // Set the frame to be the last one on the sheet
        animHealthPlayer.setStateTime(playerHealthBar.getAnimationDuration());
        animHealthEnemy.setStateTime(enemyHealthBar.getAnimationDuration());
    }

    /**
     * When player's turn starts, spawn buttons.
     */
    private void createButtons() {
        actionButtonsOn = true;
        createMenuButton("fight");
        createEscapeButton();
        player.createActionButtons();
    }

    /**
     * When player has selected action, remove buttons.
     */
    private void removeButtons() {
        actionButtonsOn = false;
        stage.clear();
    }

    /**
     * Controls the escape button's popup.
     */
    private void escaping() {
        if (escapePopup) {
            batch.draw(escapeBg, game.pixelWidth/2 - escapeBg.getWidth()/2f,
                    game.pixelHeight/2 - escapeBg.getHeight()/2f,
                    escapeBg.getWidth(), escapeBg.getHeight());

            Label escapeLabel = new Label(localize.get("escape"), finalSkin);
            escapeLabel.setWrap(true);
            escapeLabel.setWidth(530);
            escapeLabel.setAlignment(1);
            escapeLabel.setPosition(game.pixelWidth/2 - 260, game.pixelHeight/2);
            escapeLabel.draw(batch, 1);

            Label escapeHeader = new Label(localize.get("escapeHeader"), finalSkin, "big");
            escapeHeader.setWidth(530);
            escapeHeader.setAlignment(1);
            escapeHeader.setPosition(game.pixelWidth/2 - 260, game.pixelHeight/2 + 210);
            escapeHeader.draw(batch, 1);
        }
    }

    /**
     * Controls the hacking phase.
     */
    private void hackingPhase() {
        if (state == State.HACK || state == State.TUTORIAL_HACKING) {
            // If balls has not been spawned, spawn them
            if (!spawnHacking) {
                spawnHacking = true;
                // When room starts, firstHack is true, after this it's false
                hacking = new Hacking(game, firstHack);
                firstHack = false;
            }
            hacking.update(); // spin the balls and control shooting

            if (hacking.isBulletHitEnemy()) {
                state = State.HACK_SUCCESS;
                if (finalFight == null) enemy.endDialogTimer();
            } else if (hacking.isBulletMissedEnemy()) {
                state = State.HACK_FAILED;
                spawnHacking = false; // Reset spawnHacking
            }
        }
    }

    /**
     * Controls the powerup phase.
     */
    public void powerUpPhase() {
        if (state == State.POWER_UP) {
            if (!spawnPowerUp) {
                spawnPowerUp = true;
                powerUp = new UtilPowerUp(game);
                game.playSound(files.sndPowerUpPopup);
            }
            powerUp.update();

            if (powerUp.isPowerUpChosen()) {
                state = State.CHANGE_ROOM;
                stage.clear();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        game.bossDefeated();
                    }
                }, 2);
            }
        }
    }

    /**
     * Draws player's and enemy's health bars on top.
     */
    private void drawHP() {
        double div, spot;
        int frame;
        // Get correct frame from player
        div = player.getMaxHp() / (playerHealthBar.getKeyFrames().length-1);
        spot = player.getHp() / div;
        frame = (int) Math.ceil(spot);
        animHealthPlayer.setStateTime(playerHealthBar.getFrameDuration() * frame);

        // Get correct frame from enemy
        div = enemy.getMaxHp() / (enemyHealthBar.getKeyFrames().length-1);
        spot = enemy.getHp() / div;
        frame = (int) Math.ceil(spot);
        animHealthEnemy.setStateTime(enemyHealthBar.getFrameDuration() * frame);

        float x1 = game.gridSize*3;
        float x2 = game.pixelWidth - game.gridSize*3 -
                enemyHealthBar.getKeyFrame(0f).getRegionWidth();
        float yy = game.pixelHeight - 115;
        animHealthPlayer.draw(batch, x1, yy);
        animHealthEnemy.draw(batch, x2, yy);

        // Draw DoT arrows for enemy and player
        float offset = 20;
        x1 = x1 + offset;
        x2 = game.pixelWidth - game.gridSize*4 - offset;
        float size = imgDoTArrowUp.getWidth();

        if (player.getDotArrow() == ARROW_UP) batch.draw(imgDoTArrowUp, x1, yy, size, size);
        else if (player.getDotArrow() == ARROW_DOWN) batch.draw(imgDoTArrowDown, x1, yy, size, size);

        if (enemy.getDotArrow() == ARROW_UP) batch.draw(imgDoTArrowUp, x2, yy, size, size);
        else if (enemy.getDotArrow() == ARROW_DOWN) batch.draw(imgDoTArrowDown, x2, yy, size, size);
    }

    /**
     * Creates the upper left escape button.
     */
    private void createEscapeButton() {
        final ImageButton btn = new ImageButton(finalSkin.getDrawable("button_escape"),
                finalSkin.getDrawable("button_escape_clicked"));
        btn.setPosition(0, game.pixelHeight - 120);
        stage.addActor(btn);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!escapePopup) {
                    escapePopup = true;
                    stage.clear();
                    createYesNo();
                    if (game.isFirstPlayEscape()) {
                        FirstPlay escape = new FirstPlay(game, "escape");
                    }
                }
            }
        });
    }

    /**
     * Creates yes and no buttons for escaping.
     */
    private void createYesNo() {
        final ImageButton btn = new ImageButton(finalSkin, "confirm_" + lan);
        btn.setPosition(game.pixelWidth/2 - 400, game.pixelHeight/4 - 55);
        stage.addActor(btn);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!game.isWalkingSkipped()) {
                    float rounded = Math.round(game.getProgressBarMilestone() * 0.75f);
                    game.setStepCount(rounded);
                }
                state = State.ESCAPE;
                escapePopup = false;
                stage.clear();
            }
        });

        final ImageButton btn2 = new ImageButton(finalSkin,"cancel_" + lan);
        btn2.setPosition(btn.getX() + 445, btn.getY());
        stage.addActor(btn2);

        btn2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escapePopup = false;
                stage.clear();
                createButtons();
            }
        });
    }

    /**
     * Used for checking that enemy and player has completed their action, before moving on.
     * @return if fighter's states are paused
     */
    public boolean fightersTakingDamage() {
        boolean takingDamage = false;

        if (player.isPauseStates() || enemy.isPauseStates()) {
            takingDamage = true;
        }

        return takingDamage;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Parent class of fighters.
     */
    public class Fighters {

        protected float X, Y;
        protected double maxHp, hp, targetHp, hpDecreaseSpd, defaultDmg, dmgAmount, dotAmount,
                        takeDoT;
        protected ArrayList<Double> dotDamage = new ArrayList<Double>();
        protected ArrayList<Integer> dotTurns = new ArrayList<Integer>();
        protected Animating anim = new Animating();
        protected Animating hitAnim = new Animating();
        protected Animating critMissAnim = new Animating();

        protected int turnState, BEFORE = 0, TAKING_DOT = 1, START = 2, WAIT_FOR_ACTION = 3,
                                DOING_ACTION = 4, END_TURN = 5;
        protected int actionState, TEMP_ANIM = 0, HIT_ANIM = 1, LONG_ANIM = 2, MISS_ANIM = 3,
                        HEAL_ANIM = 4, DOT_ANIM = 5, DELAY_ANIM = 6, ITEM_ANIM = 7, END_ACTION = 8;
        protected int skillState, SKILL_RESET = 0, SKILL_DAMAGE = 1, SKILL_HEAL = 2, SKILL_MISS = 3,
                        SKILL_DEFEND = 4, SKILL_ITEM = 5;
        protected boolean flashWhite, hitAnimationRunning, missCritAnimationRunning, dealDoT,
                            dealCriticalHit, dealBoost, dealBoostToSelf;
        protected int dealDoTTurns, dealBoostType;
        protected double dealDoTDamage, dealBoostValue;
        protected float flashTime = 0.25f;
        protected State ifDead;
        protected int ID, PLAYER = 0, ENEMY = 1;
        protected int dotArrow;
        protected Fighters opponent;
        protected int critBoost, missBoost;
        protected double dmgBoost, armorBoost, healBoost;

        protected float positionOffset;
        protected boolean positionIncorrect;
        protected int positionTime = 20; // How long to stay still after hit
        protected int positionTimer = positionTime;
        protected int actionDelay = 20;
        protected int actionTimer = actionDelay;

        protected boolean pauseStates = false;
        protected boolean hpIncorrect = false;
        protected boolean reflectingShield = false;
        protected boolean normalSize = true;

        protected int animSpeed = 8;
        protected Animation<TextureRegion> curAnimation, curHitAnimation, idleAnim, hackAnim,
                skillAnim, takeHitAnim, healthPlus, healthMinus, criticalHitAnim, missAnim,
                dotMinus, dotPlus;
        protected HashMap<String,Integer> cooldowns;
        protected String usedItem, attackName;

        /**
         * Retrieve correct animations.
         */
        Fighters() {
            healthPlus = files.animHealthPlusDoT;
            healthMinus = files.animHealthMinusDoT;
            dotMinus = files.animDoTMinus;
            dotPlus = files.animDoTPlus;
            criticalHitAnim = files.animCriticalHit;
            missAnim = files.animMiss;
        }

        /**
         * Do these at the start of update method.
         */
        protected void updateStart() {
            retrieveOpponent();
            returnPosition();
            hpToTarget();
            checkToPause();
            anim.animate();
            if (hitAnim.getAnimation() != null) hitAnim.animate();
            if (critMissAnim.getAnimation() != null) critMissAnim.animate();
        }

        /**
         * Do these at the end of update method.
         */
        protected void updateEnd() {
            if (flashWhite) batch.setShader(shFlashWhite);
            anim.draw(batch, X + positionOffset, Y);
            drawHitAnimation();
            drawCritOrMissAnimation();
            batch.setShader(null);
        }

        /**
         * This needs to be declared in update event, since it can not be declared at the start.
         * When player gets created, enemy is still null, since player is created before enemy.
         */
        protected void retrieveOpponent() {
            if (ID == PLAYER) {
                if (opponent == null) opponent = enemy;
            } else {
                if (opponent == null) opponent = player;
            }
        }

        /**
         * Turn's agenda.
         */
        protected void controlTurnStates() {
            if (turnState == BEFORE) {
                actionTimer = actionDelay; // reset actionTimer
                if (!dialog.isSkillNameOn()) {
                    inflictDoT();
                }

            } else if (turnState == TAKING_DOT) {
                if (targetHp == hp) {
                    turnState = START;
                    checkIfAlive();
                }

            } else if (turnState == START) {
                decreaseCooldowns();
                turnState = WAIT_FOR_ACTION;

            } else if (turnState == WAIT_FOR_ACTION) {
                if (anim.getAnimation() != idleAnim) startIdle();

            } else if (turnState == DOING_ACTION) {
                controlActionStates();
            } else if (turnState == END_TURN) {
                if (!fightersTakingDamage()) {
                    opponent.startTurn();
                    opponent.checkIfAlive();
                    checkIfAlive();
                }
            }
        }

        /**
         * Control what happens after action has been selected.
         */
        protected void controlActionStates() {
            if (actionState == TEMP_ANIM) {
                // If temp animation is finished, check skillState for type
                if (curAnimation.isAnimationFinished(anim.getStateTime())) {
                    if (skillState == SKILL_DAMAGE) {
                        opponent.startHitAnimation(curHitAnimation);
                        actionState = HIT_ANIM;
                    } else if (skillState == SKILL_HEAL){
                        takeHeal(dmgAmount);
                        startHitAnimation(healthPlus);
                        actionState = HEAL_ANIM;
                    } else if (skillState == SKILL_MISS) {
                        opponent.startMissAnimation();
                        actionState = MISS_ANIM;
                    } else if (skillState == SKILL_ITEM) {
                        useItem();
                    } else {
                        // Else it's only DoT move
                        actionState = END_ACTION;
                    }
                    startIdle();
                }
            } else if (actionState == HIT_ANIM) {
                // Hit animation is drawn on top of enemy
                if (!opponent.isHitAnimationRunning()) {
                    opponent.takeHit(dmgAmount);
                    actionState = END_ACTION;
                    game.playSound(files.sndMetallicHit);
                    if (dealCriticalHit) {
                        dealCriticalHit = false;
                        opponent.startCriticalHitAnimation();
                    }
                }
            } else if (actionState == LONG_ANIM) {
                // Animation lasts until next round
                actionState = END_ACTION;
            } else if (actionState == HEAL_ANIM) {
                if (!isHitAnimationRunning()) {
                    actionState = END_ACTION;
                }
            } else if (actionState == MISS_ANIM) {
                // Miss animation is drawn on top of enemy
                if (!opponent.isMissCritAnimationRunning()) {
                    actionState = END_ACTION;
                }
            } else if (actionState == DOT_ANIM) {
                if (!isHitAnimationRunning() && !opponent.isHitAnimationRunning()) {
                    actionState = DELAY_ANIM;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            actionState = END_ACTION;
                        }
                    }, 0.5f);
                }
            } else if (actionState == END_ACTION) {
                // IF action has ended, check if skill deals dot or boost or that item was used
                if (dealDoT) {
                    dealDoT = false;
                    turnState = DOING_ACTION;
                    actionState = DOT_ANIM;
                    if (dealDoTDamage > 0) {
                        opponent.addDoT(dealDoTTurns, dealDoTDamage); // Damage
                    } else {
                        addDoT(dealDoTTurns, dealDoTDamage); // Healing
                    }
                } else if (dealBoost) {
                    dealBoost = false;
                    turnState = DOING_ACTION;
                    actionState = DOT_ANIM;
                    if (dealBoostToSelf) {
                        inflictBoost(dealBoostType, dealBoostValue);
                    } else {
                        opponent.inflictBoost(dealBoostType, dealBoostValue);
                    }
                } else if (usedItem != null) {
                    if (!fightersTakingDamage()) {
                        usedItem = null;
                        if (opponent.getHp() > 0) turnState = WAIT_FOR_ACTION;
                    }
                } else {
                    turnState = END_TURN;
                }
            }
        }

        /**
         * Controls what different items does.
         */
        protected void useItem() {
            HashMap<String, Object> item = items.getItem(usedItem);
            int itemType = (Integer) item.get(items.itemType);
            if (itemType == items.TYPE_BOOST) {
                int boost = (Integer) item.get(items.boostType);
                if (boost == items.CRIT_BOOST) {
                    int amount = (Integer) item.get(items.value);
                    critBoost += amount;
                    game.addCritBoost(amount);
                    actionState = END_ACTION;
                } else if (boost == items.MISS_BOOST) {
                    int amount = (Integer) item.get(items.value);
                    missBoost += amount;
                    game.addMissBoost(amount);
                    actionState = END_ACTION;
                } else if (boost == items.DMG_BOOST) {
                    double amount = (Double) item.get(items.value);
                    dmgBoost += amount;
                    game.addDmgBoost(amount);
                    actionState = END_ACTION;
                } else if (boost == items.ARMOR_BOOST) {
                    double amount = (Double) item.get(items.value);
                    armorBoost += amount;
                    game.addArmorBoost(amount);
                    actionState = END_ACTION;
                } else if (boost == items.HEAL_BOOST) {
                    double amount = (Double) item.get(items.value);
                    healBoost += amount;
                    game.addHealBoost(amount);
                    actionState = END_ACTION;
                }
            } else if (itemType == items.TYPE_POTION) {
                double amount = (Double) item.get(items.value);
                amount += amount * healBoost;
                takeHeal(amount);
                startHitAnimation(healthPlus);
                actionState = HEAL_ANIM;
            } else if (itemType == items.TYPE_BOMB) {
                double amount = (Double) item.get(items.value);
                dmgAmount = amount * defaultDmg;
                if (opponent.normalSize) curHitAnimation = files.animSkillHit;
                else curHitAnimation = files.animSkillHitLow;
                opponent.startHitAnimation(curHitAnimation);
                actionState = HIT_ANIM;
            }
        }

        /**
         * Before starting turn, reset turnState.
         */
        protected void startTurn() {
            turnState = BEFORE;
            if (ID == PLAYER) state = State.PLAYER_TURN;
            else if (ID == ENEMY) state = State.ENEMY_TURN;
        }

        /**
         * Opponent calls this method when hitting you. It can also show positive and negative
         * dot animations.
         * @param animation which hit animation skill has
         */
        protected void startHitAnimation(Animation<TextureRegion> animation) {
            hitAnimationRunning = true;
            hitAnim.startAnimation(animation, animSpeed);
        }

        /**
         * Draw the animation for as long as it lasts.
         */
        protected void drawHitAnimation() {
            if (hitAnimationRunning) {
                if (hitAnim.getAnimation().isAnimationFinished(hitAnim.getStateTime())) {
                    hitAnimationRunning = false;
                } else {
                    hitAnim.draw(batch, X, Y);
                }
            }
        }

        /**
         * Draws miss animation on top of you. Opponent calls this when missing.
         */
        protected void startMissAnimation() {
            missCritAnimationRunning = true;
            if (ID == PLAYER) game.playSound(files.sndPlayerMiss);
            else game.playSound(files.sndEnemyMiss);
            critMissAnim.startAnimation(missAnim, animSpeed);
        }

        /**
         * Draws critical hit animation on top of you. Opponent calls this when critting.
         */
        protected void startCriticalHitAnimation() {
            missCritAnimationRunning = true;
            if (ID == PLAYER) game.playSound(files.sndPlayerCriticalHit);
            else game.playSound(files.sndEnemyCriticalHit);
            critMissAnim.startAnimation(criticalHitAnim, animSpeed);
        }

        /**
         * Draws hit and miss animations.
         */
        protected void drawCritOrMissAnimation() {
            if (missCritAnimationRunning) {
                if (critMissAnim.getAnimation().isAnimationFinished(critMissAnim.getStateTime())) {
                    missCritAnimationRunning = false;
                } else {
                    critMissAnim.draw(batch, X, Y);
                }
            }
        }

        /**
         * Starts idle animation.
         */
        protected void startIdle() {
            anim.startAnimation(idleAnim, animSpeed);
        }

        /**
         * Starts hacking animation.
         */
        protected void startHack() {
            anim.startAnimation(hackAnim, animSpeed);
        }

        /**
         * If taken hit, then pause so that new actions won't take place.
         */
        protected void checkToPause() {
            if (!hpIncorrect && !positionIncorrect) pauseStates = false;
            else pauseStates = true;
        }

        /**
         * Check if skill deals damage over time. Can deal healing and damage.
         * @param dmg how much damage does DoT do every turn
         * @param turns how many turns does DoT last
         * @param pure whether to deal pure percent
         */
        protected void checkToDealDoT(double dmg, int turns, boolean pure) {
            // If purePercent is true, then dot value 2.0 deals 2 percent of MaxHp
            // otherwise it deals 2.0*defaultDmg
            if (pure) dealDoTDamage = dmg;
            else dealDoTDamage = dmg * defaultDmg;
            dealDoTTurns = turns;
            if (dealDoTDamage != 0) dealDoT = true;
        }

        /**
         * If skill has DoT, it will be added here.
         * @param turns how many turns the DoT lasts
         * @param damage how much damage does the DoT deal
         */
        protected void addDoT(int turns, double damage) {
            // If defending, either do nothing or reflect DoT back, else take DoT
            if (skillState == SKILL_DEFEND) {
                if (reflectingShield) {
                    opponent.addDoT(turns, damage);
                } // else do nothing
            } else {
                //whiteFlash();
                dotTurns.add(turns);
                dotDamage.add(damage);
                calculateNextDoT();
                if (damage > 0) startHitAnimation(dotMinus);
                else if (damage < 0) startHitAnimation(dotPlus);
            }
        }

        /**
         * Calculate next DoT to show the correct status arrow on health and to deal correct
         * amount of DoT damage.
         */
        protected void calculateNextDoT() {
            takeDoT = 0;
            for (int i = 0; i < dotTurns.size(); i++) {
                double damage = dotDamage.get(i);
                if (damage > 0) {
                    // Add opponent's damage boost value
                    damage += damage * opponent.getDmgBoost();
                    // Decrease damage by using your armorBoost
                    damage -= damage * armorBoost;
                } else damage += damage * healBoost;
                takeDoT += damage;
            }

            // Retrieve correct value to be drawn on top of health bar
            if (takeDoT > 0) dotArrow = ARROW_DOWN;
            else if (takeDoT < 0) dotArrow = ARROW_UP;
            else dotArrow = ARROW_NONE;
        }

        /**
         * Add all the DoTs on top of each other and decrease their turn timers
         */
        protected void inflictDoT() {
            calculateNextDoT();

            // Decrease DoT turn timers
            for (int i = 0; i < dotTurns.size(); i++) {
                dotTurns.set(i, dotTurns.get(i) - 1);
                if (dotTurns.get(i) == 0) {
                    dotTurns.remove(i);
                    dotDamage.remove(i);
                    i--;
                }
            }

            // If DoT is over 0, then do damage, if it's under 0, then heal, else start the turn
            if (takeDoT > 0) {
                calcTargetHpSpd(takeDoT);
                startHitAnimation(healthMinus);
                game.playSound(files.sndDamageOverTime);
                turnState = TAKING_DOT;
            } else if (takeDoT < 0) {
                calcTargetHpSpd(takeDoT);
                startHitAnimation(healthPlus);
                game.playSound(files.sndHealOverTime);
                turnState = TAKING_DOT;
            } else {
                turnState = START;
            }

            // Calculate next DoT to show the correct status arrow on health
            calculateNextDoT();
        }

        /**
         * Check if the skill has boost value, then updates instance variables.
         * @param type the type of boost
         * @param value the amount to boost
         */
        protected void checkToDealBoost(int type, double value, boolean self) {
            if (type != skills.BOOST_NONE) {
                dealBoost = true;
                dealBoostToSelf = self;
                dealBoostType = type;
                dealBoostValue = value;
            }
        }

        /**
         * Once skill animations has ended, boost values will be inflicted.
         * @param type which type of boost
         * @param value the boost amount
         */
        protected void inflictBoost(int type, double value) {
            // If defending, either deal nothing or reflect it back
            if (skillState == SKILL_DEFEND) {
                if (reflectingShield) {
                    opponent.inflictBoost(type, value);
                } // else do nothing
            } else {
                if (type == skills.BOOST_CRIT) critBoost += value;
                else if (type == skills.BOOST_MISS) missBoost += value;
                else if (type == skills.BOOST_DMG) dmgBoost += value;
                else if (type == skills.BOOST_ARMOR) armorBoost += value;
                else if (type == skills.BOOST_HEAL) healBoost += value;
                if (value > 0) startHitAnimation(dotPlus);
                else if (value < 0) startHitAnimation(dotMinus);
            }
        }

        /**
         * When taking hit, show hit animation for 1 second.
         */
        protected void changeToTakeHitAnimation() {
            anim.startAnimation(takeHitAnim, animSpeed);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    startIdle();
                }
            }, 1f);
        }

        /**
         * When taken hit, flash white for a certain amount of time.
         */
        protected void whiteFlash() {
            flashWhite = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    flashWhite = false;
                }
            }, flashTime);
        }

        /**
         * When taken hit, take hard knockback.
         */
        protected void knockbackHard() {
            // If target hp is over 0, then move the fighter
            if (targetHp > 0) {
                positionIncorrect = true;
                if (ID == PLAYER) positionOffset = -100f;
                else positionOffset = 100f;
            }
        }

        /**
         * When taken hit, take soft knockback when defending.
         */
        protected void knockbackSoft() {
            // If target hp is over 0, then move the fighter
            if (targetHp > 0) {
                positionIncorrect = true;
                if (ID == PLAYER) positionOffset = -50;
                else positionOffset = 50;
            }
        }

        /**
         * Calculates how fast to decrease hp
         * @param damage how much damage received
         */
        protected void calcTargetHpSpd(double damage) {
            targetHp = hp - damage;
            if (targetHp < 0) targetHp = 0;
            else if (targetHp > 100) targetHp = 100;
            hpDecreaseSpd = (targetHp - hp) / 100;
        }

        /**
         * When taken hit or healed, target hp is lower or higher than hp. This makes the hp bar
         * to move smoothly.
         */
        protected void hpToTarget() {
            if (hp > targetHp) {
                hpIncorrect = true;
                hp += hpDecreaseSpd;
                /*
                I hate that it has to be checked like this, the reason for it is that even
                though I get hpDecreaseSpd by dividing value by 100, it is not really 1 percent
                of the value, it can be 0.000001 off, so it causes hp to go over the target...
                if I would use type Long, it could probably fix it, but since it works like this,
                I don't want to take any more risks.
                */
                if (hp < targetHp || hp == targetHp) {
                    hp = targetHp;
                    hpIncorrect = false;
                }
            } else if (hp < targetHp){
                hpIncorrect = true;
                hp += hpDecreaseSpd;
                if (hp > targetHp || hp == targetHp) {
                    hp = targetHp;
                    hpIncorrect = false;
                }
            }
        }

        /**
         * When taken hit and position is off, return back to original position.
         */
        protected void returnPosition() {
            if (positionIncorrect) {
                if (positionTimer> 0) {
                    positionTimer--;
                } else {
                    if (positionOffset > 0) {
                        positionOffset --;
                    } else if (positionOffset < 0) {
                        positionOffset ++;
                    } else {
                        positionIncorrect = false;
                        positionTimer = positionTime;
                    }
                }
            }
        }

        /**
         * Checks hp after DoT and at the end of the player's and enemy's turn.
         */
        protected void checkIfAlive() {
            if (hp <= 0) {
                state = ifDead;
            }
        }

        /**
         * Checks if skill deals critical hit.
         * @param value critical hit chance
         * @return whether critical hit happened
         */
        protected boolean randomCritChance(int value) {
            value += critBoost;
            boolean critical = false;
            int random = MathUtils.random(1, 100);

            if (random <= value) critical = true;

            return critical;
        }

        /**
         * Checks if skill misses.
         * @param value miss chance
         * @return whether miss happened
         */
        protected boolean randomMissChance(int value) {
            value -= missBoost;
            boolean miss = false;
            int random = MathUtils.random(1, 100);

            if (random <= value) miss = true;

            return miss;
        }

        /**
         * Adds cooldown to skill.
         * @param skill used skill
         * @param amount how much cooldown does the skill have
         */
        protected void addCooldown(String skill, int amount) {
            /*
             +1 since cooldowns get decreased at the beginning of round and we want cooldown "3"
             to last for 3 whole rounds
              */
            cooldowns.put(skill, amount + 1);
        }

        /**
         * At the start of each round, decrease cooldown timers.
         */
        protected void decreaseCooldowns() {
            for (Map.Entry<String, Integer> entry : cooldowns.entrySet()) {
                int value = entry.getValue();
                if (value > 0) {
                    cooldowns.put(entry.getKey(), entry.getValue() - 1);
                }
                //System.out.println(entry.getKey() + " = " + String.valueOf(entry.getValue()));
            }
        }

        /**
         * Take hit, expect if defending. If reflecting shield is unlocked then return the damage
         * back to the opponent.
         * @param damage how much damage opponent dealed
         */
        protected void takeHit(double damage) {
            damage += damage * opponent.getDmgBoost();
            whiteFlash();
            if (skillState == SKILL_DEFEND) {
                if (reflectingShield) {
                    opponent.takeHit(damage);
                } else {
                    knockbackSoft();
                }
            } else {
                damage -= damage * armorBoost;
                knockbackHard();
                calcTargetHpSpd(damage);
                changeToTakeHitAnimation();
            }
        }

        /**
         * Heal yourself for certain amount.
         * @param damage how much to heal
         */
        protected void takeHeal(double damage) {
            damage += damage * healBoost;
            game.playSound(files.sndFastHeal);
            calcTargetHpSpd(damage);
        }

        /**
         * If current skill's sound is null, play random woosh sound.
         */
        protected void playRandomWooshSound() {
            int random = MathUtils.random(1, 3);

            if (random == 1) game.playSound(files.sndWoosh1);
            else if (random == 2) game.playSound(files.sndWoosh2);
            else if (random == 3) game.playSound(files.sndWoosh3);
        }

        /**
         * Health bar uses this.
         * @return amount of hp
         */
        public double getHp() {
            return hp;
        }

        /**
         * Health bar uses this.
         * @return amount of maxHp
         */
        public double getMaxHp() {
            return maxHp;
        }

        /**
         * Opponent uses this to check if it is time to inflict damage and pass the turn
         * @return if hit animation is running
         */
        public boolean isHitAnimationRunning() {
            return hitAnimationRunning;
        }

        /**
         * Opponent uses this to check if miss and crit is over and it is time to pass the turn.
         * @return if miss or crit animation is running
         */
        public boolean isMissCritAnimationRunning() {
            return missCritAnimationRunning;
        }

        /**
         * Used to check if player and enemy has finished their turn on fightersTakingDamage.
         * @return if states are paused
         */
        public boolean isPauseStates() {
            return pauseStates;
        }

        /**
         * Opponent uses this value to know how much damage you inflicted after boost values are
         * calculated.
         * @return how much dmgBoost fighter has
         */
        public double getDmgBoost() {
            return dmgBoost;
        }

        /**
         * Used by room to draw correct arrow on top of health bar.
         * @return which arrow to draw
         */
        public int getDotArrow() {
            return dotArrow;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Create player instance.
     */
    public class Player extends Fighters {

        private Animation<TextureRegion> defendAnim, escapeAnim, itemAnim, deathAnim;
        private HashMap<String,Object> mapAttack, mapDefend;

        private ArrayList<String> descriptions = new ArrayList<String>();
        private ArrayList<HashMap<String,Object>> mapSkills;
        private String[] skillNames;
        private double[][] defaultDamages = new double[][] {{34, 25, 20, 20, 15, 15},
                {20, 20, 15, 15, 10, 10}, {15, 15, 10, 10, 5, 5}, {5, 5, 5, 5, 5, 5}};
        private String[] btnTexts = new String[] {skills.ATTACK, skills.DEFEND, skills.ITEM,
                game.getSkill1(), game.getSkill2()};

        /**
         * Create necessary variables.
         */
        Player() {
            X = game.gridSize;
            Y = game.gridSize*2;
            maxHp = 100;
            hp = maxHp;
            targetHp = hp;
            defaultDmg = defaultDamages[game.getPool() - 1][game.getPoolMult()];
            reflectingShield = game.isReflectiveShield();
            if (reflectingShield) btnTexts[1] = skills.REFLECT;
            ifDead = State.DEAD;
            ID = PLAYER;
            attackName = "playerAttack";

            //Retrieve saved boosts
            critBoost = game.getCritBoost();
            missBoost = game.getMissBoost();
            dmgBoost = game.getDmgBoost();
            armorBoost = game.getArmorBoost();
            healBoost = game.getHealBoost();

            addSkillsToMap();

            // Create maps for attack and defend, create cooldowns
            mapAttack = skills.getSkill(skills.ATTACK);
            mapDefend = skills.getSkill(skills.DEFEND);
            cooldowns = new HashMap<String, Integer>();
            cooldowns.put(btnTexts[1], 0);
            cooldowns.put(skillNames[0], 0);
            cooldowns.put(skillNames[1], 0);

            // Retrieve animations
            idleAnim = files.animIdle;
            skillAnim = files.animSkill;
            defendAnim = files.animDefend;
            takeHitAnim = files.animTakeHitAnim;
            escapeAnim = files.animEscape;
            itemAnim = files.animItem;
            hackAnim = files.animHack;
            deathAnim = files.animDeath;

            anim.startAnimation(idleAnim, animSpeed);
        }

        /**
         * Update this every frame. Controls different states.
         */
        public void update() {
            updateStart();
            updateOverallBoosts();
            if (!pauseStates) {

                if (state == State.PLAYER_TURN) {
                    if (turnState == WAIT_FOR_ACTION) {
                        if (actionTimer > 0) actionTimer--;
                        else if (!actionButtonsOn) createButtons();
                    }
                    controlTurnStates();
                } else if (state == State.HACK) {
                    if (anim.getAnimation() != hackAnim) startHack();
                } else if (state == State.DEAD) {
                    if (anim.getAnimation() != deathAnim) anim.startAnimation(deathAnim, animSpeed);
                } else if (state == State.ESCAPE) {
                    runAway();
                }
            }

            updateEnd();
        }

        /**
         * Iterate through the actions to find the selected action.
         * @param action action which user selected
         */
        public void doAction(String action) {
            // Reset necessary values
            curHitAnimation = null;
            dealDoT = false;
            skillState = SKILL_RESET;
            boolean actionSelected = false;

            if (action.equals(skills.ATTACK))
            {
                skillState = SKILL_DAMAGE;
                action = attackName;
                actionSelected = true;
                playRandomWooshSound();
                curAnimation = skillAnim;
                // If skill misses, skip everything
                boolean miss = randomMissChance((Integer) mapAttack.get(skills.missChance));
                if (miss) skillState = SKILL_MISS;
                else {
                    curHitAnimation = (Animation<TextureRegion>) mapAttack.get(skills.hitAnimation);
                    dmgAmount = defaultDmg;
                    dealCriticalHit = randomCritChance((Integer) mapAttack.get(skills.critChance));
                    if (dealCriticalHit) dmgAmount *= 1.5;
                }

                actionState = TEMP_ANIM;
            }
            else if (action.equals(skills.DEFEND) || action.equals(skills.REFLECT))
            {
                if (cooldowns.get(action) == 0) {
                    actionSelected = true;
                    curAnimation = defendAnim;
                    game.playSound(files.sndDefend);
                    addCooldown(action, (Integer) mapDefend.get(skills.cooldown));
                    skillState = SKILL_DEFEND;
                    actionState = LONG_ANIM;
                }
            }
            else if (action.equals(skills.ITEM))
            {
                UtilItem inventory = new UtilItem(game, "fight", thisRoom);
            }
            else { // It's skill
                for (int i = 0; i < 2; i++) {
                    /*
                    If selected action is this skill, if skill is not empty and if cooldown is 0
                     */
                    if (action.equals(skillNames[i]) && !skillNames[i].equals("") &&
                            cooldowns.get(skillNames[i]) == 0) {
                        actionSelected = true;
                        HashMap<String, Object> skillMap = mapSkills.get(i);
                        addCooldown(skillNames[i],
                                (Integer) skillMap.get(skills.cooldown));
                        curAnimation = skillAnim;

                        // Play sound if not null
                        Sound snd = (Sound) skillMap.get(skills.sound);
                        if (snd == null) playRandomWooshSound();
                        else game.playSound(snd);

                        // If skill misses, skip everything
                        boolean miss = randomMissChance((Integer) skillMap.get(skills.missChance));
                        if (miss) skillState = SKILL_MISS;
                        else {
                            boolean pureDmg = (Boolean) skillMap.get(skills.dmgPurePercent);
                            double damage = (Double) skillMap.get(skills.damage);

                            // Check if skill heals, else do damage
                            if ((Double) skillMap.get(skills.damage) < 0) {
                                skillState = SKILL_HEAL;
                                if (pureDmg) dmgAmount = damage;
                                else dmgAmount = damage * defaultDmg;
                            } else {
                                curHitAnimation =
                                        (Animation<TextureRegion>) skillMap.get(skills.hitAnimation);
                                // If skill does not have animation, then it does not do damage
                                if (curHitAnimation != null) {
                                    skillState = SKILL_DAMAGE;

                                    // If pureDmg, value 20 deals 20% of maxHp, else it deals
                                    // wholeDmg*20
                                    if (pureDmg) dmgAmount = damage;
                                    else dmgAmount = damage * defaultDmg;
                                    // If critical hit, deal 1.5x damage
                                    dealCriticalHit = randomCritChance((Integer)
                                            skillMap.get(skills.critChance));
                                    if (dealCriticalHit) dmgAmount *= 1.5;
                                }
                            }

                            checkToDealDoT((Double) skillMap.get(skills.damageOverTime),
                                    (Integer) skillMap.get(skills.damageOverTimeTurns),
                                    (Boolean) skillMap.get(skills.dotPurePercent));

                            checkToDealBoost((Integer) skillMap.get(skills.boostType),
                                    (Double) skillMap.get(skills.boostValue),
                                    (Boolean) skillMap.get(skills.boostSelf));
                        }

                        actionState = TEMP_ANIM;
                    }
                }
            }

            // If player touched something that was on cooldown, actionSelected remains false
            if (actionSelected) {
                anim.startAnimation(curAnimation, animSpeed);
                turnState = DOING_ACTION;
                removeButtons();
                action = localize.get(action);
                dialog.showSkillName(action, "skillname");

                if (!opponent.normalSize) {
                    if (curHitAnimation == files.animPhysicalHit) {
                        curHitAnimation = files.animPhysicalHitLow;
                    } else if (curHitAnimation == files.animSkillHit) {
                        curHitAnimation = files.animPhysicalHitLow;
                    }
                }
            }
        }

        /**
         * Add current skills to map.
         */
        private void addSkillsToMap() {
            skillNames = new String[] {game.getSkill1(), game.getSkill2()};
            mapSkills = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < 2; i++) {
                if (skillNames[i] != "") mapSkills.add(i, skills.getSkill(skillNames[i]));
            }
        }

        /**
         * Update boost values, so that Stats object knows how much overall boost player has.
         */
        private void updateOverallBoosts() {
            game.setOverallBstCrit(critBoost);
            game.setOverallBstMiss(missBoost);
            game.setOverallBstDmg(dmgBoost);
            game.setOverallBstArmor(armorBoost);
            game.setOverallBstHeal(healBoost);
        }

        /**
         * Run away if escape is chosen.
         */
        private void runAway() {
            if (anim.getAnimation() != escapeAnim) anim.startAnimation(escapeAnim, animSpeed);

            // If player has reached certain amount of x value, change room
            if (X > - 400) {
                X -= Gdx.graphics.getDeltaTime() * 300f;
            } else {
                game.switchToRoomGame();
            }
        }

        /**
         * Change necessary values and start useItem
         * @param item
         */
        public void selectItem(String item) {
            skillState = SKILL_ITEM;
            state = State.PLAYER_TURN;
            usedItem = item; // useItem uses this to know which item was used
            game.playSound(files.sndUseItem);
            curAnimation = itemAnim;
            actionState = TEMP_ANIM;
            anim.startAnimation(curAnimation, animSpeed);
            turnState = DOING_ACTION;
            removeButtons();
            dialog.showSkillName(localize.get(item), "skillname");
        }

        /**
         * Creates correct action buttons and their descriptions.
         */
        public void createActionButtons() {
            float space = game.pixelWidth / 5;
            for (int i = 0; i < btnTexts.length; i++) {
                btnCounter = i;
                // Retrieve description and cooldown for button, using the complete name of skill
                String action, button;
                action = btnTexts[i];
                HashMap<String, Object> skillMap = skills.getSkill(action);

                if (action != "") {
                    descriptions.add(skills.retrieveSkillDescription(action));
                } else {
                    descriptions.add("");
                }
                int cooldown = 0;
                try {
                    cooldown = cooldowns.get(action);
                } catch (Exception e) {
                    // This catch need to exist, otherwise it will crash
                }

                // Retrieve correct button using the keyname of the action
                if (action.equals("")) button = "empty";
                else button = (String) skillMap.get(skills.button);

                Drawable normal, clicked;
                if (button.equals("empty")) {
                    // If button is empty, get empty button
                    normal = finalSkin.getDrawable("button_" + button);
                    clicked = finalSkin.getDrawable("button_" + button);
                } else if (cooldown == 0) {
                    // If cooldown is 0, get correct button for action
                    normal = finalSkin.getDrawable("button_" + button);
                    clicked = finalSkin.getDrawable("button_" + button + "_clicked");
                } else {
                    // else it has cooldown, so retrieve correct cooldown button
                    normal = finalSkin.getDrawable("button_cooldown" + cooldown);
                    clicked = finalSkin.getDrawable("button_cooldown" + cooldown);
                }
                final ImageButton imgButton = new ImageButton(normal, clicked);
                imgButton.setPosition(i*space, 0f);
                stage.addActor(imgButton);
                // If skill does not have cooldown and is not empty, add button listener
                if (cooldown == 0 && !button.equals("empty")) {
                    imgButton.addListener(new ActorGestureListener(20,
                            0.4f, 0.5f, 0.15f) {
                        int i = btnCounter;

                        public boolean longPress(Actor actor, float x, float y) {
                            dialog.createDialog(descriptions.get(i), "skilldescription",
                                    false, game.DIAL_SKILL);
                            return true;
                        }

                        public void tap(InputEvent event, float x, float y, int count, int button) {
                            doAction(btnTexts[i]);
                        }
                    });
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Create enemy instance.
     */
    public class Enemy extends Fighters {

        private String dialogStart, dialogEnd;
        private HashMap<String,Object> mapBoss;

        private double[] damages, damageOverTimes, boostValues;
        private ArrayList<Animation<TextureRegion>> hitAnimList;
        private String[] skillNames;
        private int[] critChances, missChances, cooldownAmount, damageOverTimeTurns, boostTypes;
        private boolean[] dmgPurePercents, dotPurePercents, boostSelves;
        private Sound[] sounds;
        private int showFirstDialogTimer = 60;

        /**
         * Create necessary variables.
         */
        Enemy() {
            retrieveBoss();

            X = game.pixelWidth - game.gridSize -
                    idleAnim.getKeyFrame(0f).getRegionWidth();
            Y = game.gridSize*2;
            maxHp = 100;
            hp = maxHp;
            targetHp = hp;
            defaultDmg = 12.5;
            defaultDmg += 5.0 * game.getGameCompleteCounter();
            ifDead = State.HACK;
            ID = ENEMY;

            cooldowns = new HashMap<String, Integer>();
            cooldowns.put("Skill0", 0);
            cooldowns.put("Skill1", 0);
            cooldowns.put("Skill2", 0);

            anim.startAnimation(idleAnim, animSpeed);
        }

        /**
         * Update this every frame. Controls different states.
         */
        public void update() {
            startDialogTimer();
            updateStart();

            if (!pauseStates) {

                if (state == State.ENEMY_TURN) {
                    if (turnState == WAIT_FOR_ACTION) attack();
                    controlTurnStates();
                } else if (state == State.HACK) {
                    if (anim.getAnimation() != hackAnim) anim.startAnimation(hackAnim, animSpeed);
                } else if (state == State.HACK_FAILED) {
                    // If hacking failed, calculate 3+ percent of maxHp back
                    calcTargetHpSpd(-maxHp/3);
                    state = State.HACK_RESTORING;
                } else if (state == State.HACK_RESTORING) {
                    // If hp has been restored, start idle and player's turn
                    if (targetHp == hp) {
                        startIdle();
                        player.startTurn();
                    }
                } else if (state == State.FINALFIGHT_BEFORE_END) {
                    if (anim.getAnimation() != takeHitAnim)
                        anim.startAnimation(takeHitAnim, animSpeed);
                }
}
            updateEnd();
        }

        /**
         * Retrieve boss's information.
         */
        private void retrieveBoss() {
            mapBoss = bosses.getBoss(game.getCurrentBoss());

            // Use these arrays when selecting skills in attack()
            skillNames = new String[3];
            hitAnimList = new ArrayList<Animation<TextureRegion>>();
            damages = new double[3];
            dmgPurePercents = new boolean[3];
            critChances = new int[3];
            missChances = new int[3];
            cooldownAmount = new int[3];
            damageOverTimes = new double[3];
            damageOverTimeTurns = new int[3];
            dotPurePercents = new boolean[3];
            sounds = new Sound[3];
            boostTypes = new int[3];
            boostValues = new double[3];
            boostSelves = new boolean[3];

            // Retrieve skill names, since bosses have different skill combinations in every pool
            int skillNumber1, skillNumber2;
            if (game.getPool() == 1) {
                skillNumber1 = 1;
                skillNumber2 = 2;
            } else if (game.getPool() == 2) {
                skillNumber1 = 3;
                skillNumber2 = 4;
            } else if (game.getPool() == 3) {
                skillNumber1 = 2;
                skillNumber2 = 4;
            } else {
                // Fabio uses random skills
                skillNumber1 = MathUtils.random(1, 4);
                skillNumber2 = MathUtils.random(1, 4);
                while (skillNumber1 == skillNumber2) {
                    skillNumber2 = MathUtils.random(1, 4);
                }
            }

            skillNames[0] = (String) mapBoss.get(bosses.skillName + String.valueOf(0));
            skillNames[1] = (String) mapBoss.get(bosses.skillName + String.valueOf(skillNumber1));
            skillNames[2] = (String) mapBoss.get(bosses.skillName + String.valueOf(skillNumber2));

            // Powerup uses this to give correct skills
            game.setCurBossSkills(skillNames[0], skillNames[1], skillNames[2]);

            for (int i = 0; i < 3; i++) {
                String skillName = skillNames[i];

                // Retrieve the skills map from Skills class which contains skill values
                HashMap<String, Object> mapSkill = skills.getSkill(skillName);

                // Retrieve hit animation from Skills class
                Animation<TextureRegion> skillHit =
                        (Animation<TextureRegion>) mapSkill.get(skills.hitAnimation);
                hitAnimList.add(skillHit);

                // Retrieve skills's damage
                double dmg = (Double) mapSkill.get(skills.damage);
                damages[i] = dmg;

                // Retrieve if to deal damage pure percent or comparable to defaultDamage
                boolean pureDmg = (Boolean) mapSkill.get(skills.dmgPurePercent);
                dmgPurePercents[i] = pureDmg;

                // Retrieve skill's crit chance percent
                int crit = (Integer) mapSkill.get(skills.critChance);
                critChances[i] = crit;

                // Retrieve skill's miss chance percent
                int miss = (Integer) mapSkill.get(skills.missChance);
                missChances[i] = miss;

                // Retrieve skill's cooldown
                int cd = (Integer) mapSkill.get(skills.cooldown);
                cooldownAmount[i] = cd;

                // Retrieve skills' damage over time
                double dot = (Double) mapSkill.get(skills.damageOverTime);
                damageOverTimes[i] = dot;

                // Retrieve skill's damage over time turns
                int dotTurn = (Integer) mapSkill.get(skills.damageOverTimeTurns);
                damageOverTimeTurns[i] = dotTurn;

                // Retrieve if to deal dot pure percent or comparable to defaultDamage
                boolean pureDot = (Boolean) mapSkill.get(skills.dotPurePercent);
                dotPurePercents[i] = pureDot;

                // Retrieve sound effect
                Sound snd = (Sound) mapSkill.get(skills.sound);
                sounds[i] = snd;

                // Retrieve Boost type, value and self
                int bstType = (Integer) mapSkill.get(skills.boostType);
                boostTypes[i] = bstType;
                double bstVal = (Double) mapSkill.get(skills.boostValue);
                boostValues[i] = bstVal;
                boolean bstSelf = (Boolean) mapSkill.get(skills.boostSelf);
                boostSelves[i] = bstSelf;
            }

            // Retrieve boss's size
            normalSize = (Boolean) mapBoss.get(bosses.normalSize);
            attackName = (String) mapBoss.get(bosses.attackName);

            // Retrieve enemy animations and speed
            idleAnim = (Animation<TextureRegion>) mapBoss.get(bosses.idle);
            skillAnim = (Animation<TextureRegion>) mapBoss.get(bosses.skill);
            hackAnim = (Animation<TextureRegion>) mapBoss.get(bosses.hack);
            takeHitAnim = (Animation<TextureRegion>) mapBoss.get(bosses.takeHit);

            // Retrieve dialog start and end from Boss
            dialogStart = localize.get((String) mapBoss.get(bosses.dialogStart));
            dialogEnd = localize.get((String) mapBoss.get(bosses.dialogEnd));
        }

        /**
         * Selects random skill.
         */
        private void attack() {
            // Wait for timer and skill name box to go down, then select action
            if (actionTimer > 0 || dialog.isSkillNameOn()) {
                actionTimer--;
            } else {
                // Reset necessary values
                curHitAnimation = null;
                dealDoT = false;
                skillState = SKILL_RESET;

                turnState = DOING_ACTION;
                actionState = TEMP_ANIM;
                int R;
                // While skill chosen which is on cooldown, select new one
                while (true) {
                    R = MathUtils.random(0, cooldowns.size() - 1);

                    // In tutorial, do suction, else use random value
                    if (tutorial != null && !tutorial.isFightAfterHitFinished()) {
                        R = 0;
                    }

                    String selSkill = "Skill" + String.valueOf(R);
                    if (cooldowns.get(selSkill) == 0) {
                        addCooldown(selSkill, cooldownAmount[R]);
                        break;
                    }
                }

                // Localize name and show it, if it's attack, then retrieve boss's attackName
                String localizedName;
                if (skillNames[R] == skills.ATTACK) localizedName = localize.get(attackName);
                else localizedName = localize.get(skillNames[R]);
                dialog.showSkillName(localizedName, "skillname_enemy");

                // Play sound if not null
                if (sounds[R] == null) playRandomWooshSound();
                else game.playSound(sounds[R]);

                boolean miss = randomMissChance(missChances[R]);
                if (miss) skillState = SKILL_MISS;
                else {
                    double damage = damages[R];
                    // Check if skill heals, else do damage
                    if (damage < 0) {
                        skillState = SKILL_HEAL;
                        if (dmgPurePercents[R]) dmgAmount = damage;
                        else dmgAmount = damage * defaultDmg;
                    } else {
                        // If skill does not heal, get hitAnimation
                        curHitAnimation = hitAnimList.get(R);
                        // If hitAnimation is null, then it does no damage
                        if (curHitAnimation != null) {
                            skillState = SKILL_DAMAGE;

                            // If pureDmg, value 20 deals 20% of maxHp, else it deal wholeDmg*20
                            if (dmgPurePercents[R]) dmgAmount = damage;
                            else dmgAmount = damage * defaultDmg;
                            // If critical hit, deal 1.5x damage
                            dealCriticalHit = randomCritChance(critChances[R]);
                            if (dealCriticalHit) dmgAmount *= 1.5;
                        }
                    }

                    checkToDealDoT(damageOverTimes[R], damageOverTimeTurns[R],
                            dotPurePercents[R]);

                    checkToDealBoost(boostTypes[R], boostValues[R], boostSelves[R]);
                }

                curAnimation = skillAnim;
                anim.startAnimation(curAnimation, animSpeed);
            }
        }

        /**
         * When the fight begins, wait for some time to start the dialogue.
         */
        private void startDialogTimer() {
            if (state == State.START_ROOM) {
                if (showFirstDialogTimer > 0) showFirstDialogTimer--;
                else {
                    state = State.DIALOG_START;
                    dialog.createDialog(dialogStart, "dialog_enemy", normalSize, 0);
                }
            }
        }

        /**
         * Room calls this after hack is successful.
         */
        public void endDialogTimer() {
            anim.startAnimation(takeHitAnim, animSpeed);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    startIdle();
                    state = State.DIALOG_END;
                    dialog.createDialog(dialogEnd, "dialog_enemy", normalSize, 0);
                }
            }, 2);
        }
    }
}