package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class RoomFight extends RoomParent {

    // Enums give simple constants, which decreases the chance for coding mistakes
    enum State {
        TUTORIAL,
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
    private Texture imgBg, escapeBg;
    private Animation<TextureRegion> playerHealthBar, enemyHealthBar;
    private Animating animHealthPlayer = new Animating();
    private Animating animHealthEnemy = new Animating();
    private Player player;
    private Enemy enemy;
    private int btnCounter; // Used for button classes to get the correct value
    private State state;
    private boolean escapePopup, spawnHacking, actionButtonsOn, spawnPowerUp;
    private boolean firstHack = true;
    private int deathTimer = 240;
    private boolean startDeathTimer;
    private ShaderProgram shFlashWhite;
    private Hacking hacking;
    private UtilPowerUp powerUp;
    private boolean firstPlayTimeFight;

    RoomFight(final MainGame game) {
        super(game);
        imgBg = files.imgBgBoss;
        escapeBg = files.escapeBg;
        firstPlayTimeFight = game.isFirstPlayTimeFight();


        if (firstPlayTimeFight) {
            state = State.TUTORIAL;
            FirstPlay firstPlay = new FirstPlay(game, "fight", thisRoom);
        } else {
            state = State.START_ROOM;
        }

        createHealthBars();
        createShader(); // Used for flashing white

        player = new Player();
        enemy = new Enemy();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!game.haveWeChangedTheRoom) {

            universalStateChecks();

            batch.begin();
            batch.draw(imgBg, 0,0, imgBg.getWidth(), imgBg.getHeight());
            drawTopAndBottomBar();
            drawHP();
            player.update();
            enemy.update();
            escaping();
            batch.end();
            stage.draw(); // Keep before hacking and powerup since they use the same stage

            hackingPhase();
            powerUpPhase();
        }
    }

    @Override
    public void selectItem(String item) {
        player.selectItem(item);
    }

    @Override
    public void tutorialFinished() { state = State.START_ROOM; }

    private void universalStateChecks() {
        switch (state) {
            // If dialog box has been closed, start the turn
            case DIALOG_START: {
                if (!dialog.isDialogOn()) player.startTurn();
                break;
            }
            // If dialog box has been closed, start the
            case DIALOG_END: {
                if (!dialog.isDialogOn()) {
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
            // If dead, wait some time and then exit back to corridor
            case DEAD: {
                if (!startDeathTimer) {
                    startDeathTimer = true;
                    game.playSound(files.sndLoseToBoss);
                }

                if (deathTimer > 0) deathTimer--;
                else game.switchToRoomGame();
            }
        }
    }

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

    @Override
    public void hide() {
        if (hacking != null) hacking.dispose();
    }

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

    // When player's turn starts, spawn buttons
    private void createButtons() {
        actionButtonsOn = true;
        createMenuButton("fight");
        createEscapeButton();
        player.createActionButtons();
    }

    // When player has selected action, remove buttons
    private void removeButtons() {
        actionButtonsOn = false;
        stage.clear();
    }

    /*// This array has to be in same order than in Player's action array
    private void createActionButtons() {
        float space = (game.pixelWidth - 100) / 5; // Temporary solution
        for (int i = 0; i < btnTexts.length; i++) {
            btnCounter = i;
            final TextButton btn = new TextButton(btnTexts[i], skin);
            btn.setWidth(300);
            btn.setHeight(100);
            btn.setPosition(50f + i*space, 100f);
            stage.addActor(btn);

            btn.addListener(new ClickListener() {
                int i = btnCounter;
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    player.doAction(btnTexts[i]);
                }
            });
        }
    }*/

    // Controls the escape button's popup
    private void escaping() {
        if (escapePopup) {
            batch.draw(escapeBg, game.pixelWidth/2 - escapeBg.getWidth()/2f,
                    game.pixelHeight/2 - escapeBg.getHeight()/2f,
                    escapeBg.getWidth(), escapeBg.getHeight());

            Label escapeLabel = new Label(localize.get("escape"), finalSkin);
            escapeLabel.setWrap(true);
            escapeLabel.setWidth(530);
            escapeLabel.setAlignment(1);
            escapeLabel.setPosition(game.pixelWidth/2 - 260, game.pixelHeight/2 + 120);
            escapeLabel.draw(batch, 1);
        }
    }

    // Controls the hacking phase
    private void hackingPhase() {
        if (state == State.HACK) {
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
                enemy.endDialogTimer();
            } else if (hacking.isBulletMissedEnemy()) {
                state = State.HACK_FAILED;
                spawnHacking = false; // Reset spawnHacking
            }
        }
    }

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
                        game.switchToRoomGame();
                    }
                }, 2);
            }
        }
    }

    // Draws player's and enemy's health bars on top
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
        animHealthPlayer.draw(batch, x1, game.pixelHeight - 115);
        animHealthEnemy.draw(batch, x2, game.pixelHeight - 115);
    }

    // Creates the upper left escape button
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
                }
            }
        });
    }

    // Creates yes and no buttons for escaping
    private void createYesNo() {
        final TextButton btn = new TextButton(localize.get("yes"), finalSkin);
        btn.setPosition(game.pixelWidth/2 - btn.getWidth(), game.pixelHeight/2 - 175);
        stage.addActor(btn);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setStepCount(game.getProgressBarMilestone()*0.75f);
                state = State.ESCAPE;
                escapePopup = false;
                stage.clear();
            }
        });

        final TextButton btn2 = new TextButton(localize.get("no"), finalSkin);
        btn2.setPosition(game.pixelWidth/2, game.pixelHeight/2 - 175);
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

    // Used for checking that enemy and player has completed their action, before moving on
    public boolean fightersTakingDamage() {
        boolean takingDamage = false;

        if (player.isPauseStates() || enemy.isPauseStates()) {
            takingDamage = true;
        }

        return takingDamage;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    CREATE PARENT FIGHTER
     */
    public class Fighters {

        protected float X, Y;
        protected double maxHp, hp, targetHp, hpDecreaseSpd, defaultDmg, dmgAmount, dotAmount;
        protected ArrayList<Double> dotDamage = new ArrayList<Double>();
        protected ArrayList<Integer> dotTurns = new ArrayList<Integer>();
        protected Animating anim = new Animating();
        protected Animating hitAnim = new Animating();
        protected Animating critMissAnim = new Animating();

        protected int turnState, BEFORE = 0, TAKING_DOT = 1, START = 2, WAIT_FOR_ACTION = 3,
                                DOING_ACTION = 4, END_ACTION = 5;
        protected int actionState, TEMP_ANIM = 0, HIT_ANIM = 1, LONG_ANIM = 2, MISS_ANIM = 3,
                        HEAL_ANIM = 4, DOT_ANIM = 5, DELAY_ANIM = 6, ITEM_ANIM = 7;
        protected int skillState, SKILL_RESET = 0, SKILL_DAMAGE = 1, SKILL_HEAL = 2, SKILL_MISS = 3,
                        SKILL_DEFEND = 4, SKILL_ITEM = 5;
        protected boolean flashWhite, hitAnimationRunning, missCritAnimationRunning, dealDoT,
                            dealCriticalHit;
        protected float flashTime = 0.25f;
        protected State ifDead;
        protected int ID, PLAYER = 0, ENEMY = 1;
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

        Fighters() {
            healthPlus = files.animHealthPlusDoT;
            healthMinus = files.animHealthMinusDoT;
            dotMinus = files.animDoTMinus;
            dotPlus = files.animDoTPlus;
            criticalHitAnim = files.animCriticalHit;
            missAnim = files.animMiss;
        }

        // Do this at the start of update method
        protected void updateStart() {
            retrieveOpponent();
            returnPosition();
            hpToTarget();
            checkToPause();
            anim.animate();
            if (hitAnim.getAnimation() != null) hitAnim.animate();
            if (critMissAnim.getAnimation() != null) critMissAnim.animate();
        }

        // Do this at the end of update method
        protected void updateEnd() {
            if (flashWhite) batch.setShader(shFlashWhite);
            anim.draw(batch, X + positionOffset, Y);
            drawHitAnimation();
            drawCritOrMissAnimation();
            batch.setShader(null);
        }

        protected void retrieveOpponent() {
            /*
            This needs to be declared in update event, since it can not be declared at the start.
            When player gets created, enemy is still null, since player is created before enemy.
             */
            if (ID == PLAYER) {
                if (opponent == null) opponent = enemy;
            } else {
                if (opponent == null) opponent = player;
            }
        }

        // Turn's agenda
        protected void controlTurnStates() {
            if (turnState == BEFORE) {
                actionTimer = actionDelay; // reset actionTimer
                if (!dialog.isSkillNameOn()) {
                    checkDoT();
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
            } else if (turnState == END_ACTION) {
                if (!fightersTakingDamage()) {
                    opponent.startTurn();
                    opponent.checkIfAlive();
                    checkIfAlive();
                }
            }
        }

        // Control what happens after action has been selected
        protected void controlActionStates() {
            if (actionState == TEMP_ANIM) {
                // If temp animation is finished, check skillState for type
                if (curAnimation.isAnimationFinished(anim.getStateTime())) {
                    if (skillState == SKILL_DAMAGE) {
                        opponent.startHitAnimation(curHitAnimation, animSpeed);
                        actionState = HIT_ANIM;
                    } else if (skillState == SKILL_HEAL){
                        takeHeal(dmgAmount);
                        startHitAnimation(healthPlus, animSpeed);
                        actionState = HEAL_ANIM;
                    } else if (skillState == SKILL_MISS) {
                        opponent.startMissAnimation();
                        actionState = MISS_ANIM;
                    } else if (skillState == SKILL_ITEM) {
                        useItem();
                    } else {
                        // Else it's only DoT move
                        turnState = END_ACTION;
                    }
                    startIdle();
                }
            } else if (actionState == HIT_ANIM) {
                // Hit animation is drawn on top of enemy
                if (!opponent.isHitAnimationRunning()) {
                    opponent.takeHit(dmgAmount);
                    turnState = END_ACTION;
                    game.playSound(files.sndMetallicHit);
                    if (dealCriticalHit) {
                        dealCriticalHit = false;
                        opponent.startCriticalHitAnimation();
                    }
                }
            } else if (actionState == LONG_ANIM) {
                // Animation lasts until next round
                turnState = END_ACTION;
            } else if (actionState == HEAL_ANIM) {
                if (!isHitAnimationRunning()) {
                    turnState = END_ACTION;
                }
            } else if (actionState == MISS_ANIM) {
                // Miss animation is drawn on top of enemy
                if (!opponent.isMissCritAnimationRunning()) {
                    turnState = END_ACTION;
                }
            } else if (actionState == DOT_ANIM) {
                if (!isHitAnimationRunning() && !opponent.isHitAnimationRunning()) {
                    actionState = DELAY_ANIM;
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            turnState = END_ACTION;
                        }
                    }, 0.5f);
                }
            }

            // If doing action has ended, but DoT was inflicted during turn, then do DoT animation
            if (turnState == END_ACTION) {
                if (dealDoT) {
                    dealDoT = false;
                    turnState = DOING_ACTION;
                    actionState = DOT_ANIM;
                    if (dotAmount > 0) opponent.startHitAnimation(dotMinus, animSpeed);
                    else startHitAnimation(dotPlus, animSpeed);
                } else if (usedItem != null) {
                    usedItem = null;
                    turnState = WAIT_FOR_ACTION;
                }
            }
        }

        protected void useItem() {
            HashMap<String, Object> item = items.getItem(usedItem);
            int boost = (Integer) item.get(items.boostType);
            if (boost == items.CRIT_BOOST) {
                int amount = (Integer) item.get(items.value);
                critBoost += amount;
                game.addCritBoost(amount);
                turnState = END_ACTION;
            } else if (boost == items.MISS_BOOST) {
                int amount = (Integer) item.get(items.value);
                missBoost += amount;
                game.addMissBoost(amount);
                turnState = END_ACTION;
            } else if (boost == items.DMG_BOOST) {
                double amount = (Double) item.get(items.value);
                dmgBoost += amount;
                game.addDmgBoost(amount);
                turnState = END_ACTION;
            } else if (boost == items.ARMOR_BOOST) {
                double amount = (Double) item.get(items.value);
                armorBoost += amount;
                game.addArmorBoost(amount);
                turnState = END_ACTION;
            } else if (boost == items.HEAL_BOOST) {
                double amount = (Double) item.get(items.value);
                healBoost += amount;
                game.addHealBoost(amount);
                turnState = END_ACTION;
            } else if (usedItem == items.POTION) {
                double amount = (Double) item.get(items.value);
                amount += amount * healBoost;
                takeHeal(amount);
                startHitAnimation(healthPlus, animSpeed);
                actionState = HEAL_ANIM;
            }

        }

        // Before starting turn, reset turnState
        protected void startTurn() {
            turnState = BEFORE;
            if (ID == PLAYER) state = State.PLAYER_TURN;
            else if (ID == ENEMY) state = State.ENEMY_TURN;
        }

        // Opponent starts this when hitting you
        protected void startHitAnimation(Animation<TextureRegion> animation, int spd) {
            hitAnimationRunning = true;
            hitAnim.startAnimation(animation, spd);
        }

        // Draw the animation for as long as it lasts
        protected void drawHitAnimation() {
            if (hitAnimationRunning) {
                if (hitAnim.getAnimation().isAnimationFinished(hitAnim.getStateTime())) {
                    hitAnimationRunning = false;
                } else {
                    hitAnim.draw(batch, X, Y);
                }
            }
        }

        // Opponent starts this when hitting you
        protected void startMissAnimation() {
            missCritAnimationRunning = true;
            if (ID == PLAYER) game.playSound(files.sndPlayerMiss);
            else game.playSound(files.sndEnemyMiss);
            critMissAnim.startAnimation(missAnim, animSpeed);
        }

        // Opponent starts this when hitting you
        protected void startCriticalHitAnimation() {
            missCritAnimationRunning = true;
            if (ID == PLAYER) game.playSound(files.sndPlayerCriticalHit);
            else game.playSound(files.sndEnemyCriticalHit);
            critMissAnim.startAnimation(criticalHitAnim, animSpeed);
        }

        // Draw miss and crit animations
        protected void drawCritOrMissAnimation() {
            if (missCritAnimationRunning) {
                if (critMissAnim.getAnimation().isAnimationFinished(critMissAnim.getStateTime())) {
                    missCritAnimationRunning = false;
                } else {
                    critMissAnim.draw(batch, X, Y);
                }
            }
        }

        protected void startIdle() {
            anim.startAnimation(idleAnim, animSpeed);
        }

        protected void startHack() {
            anim.startAnimation(hackAnim, animSpeed);
        }

        // If taken hit, then pause so that new actions won't take place
        protected void checkToPause() {
            if (!hpIncorrect && !positionIncorrect) pauseStates = false;
            else pauseStates = true;
        }

        // Add all the DoTs on top of each other and decrease their turn timers
        protected void checkDoT() {
            double takeDoT = 0;
            for (int i = 0; i < dotTurns.size(); i++) {
                double damage = dotDamage.get(i);
                if (damage > 0) {
                    // Add opponent's damage boost value
                    damage += damage * opponent.getDmgBoost();
                    // Decrease damage by using your armorBoost
                    damage -= damage * armorBoost;
                } else damage += damage * healBoost;
                takeDoT += damage;
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
                startHitAnimation(healthMinus, animSpeed);
                game.playSound(files.sndDamageOverTime);
                turnState = TAKING_DOT;
            } else if (takeDoT < 0) {
                calcTargetHpSpd(takeDoT);
                startHitAnimation(healthPlus, animSpeed);
                game.playSound(files.sndHealOverTime);
                turnState = TAKING_DOT;
            } else {
                turnState = START;
            }
        }

        protected void changeToTakeHitAnimation() {
            anim.startAnimation(takeHitAnim, animSpeed);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    startIdle();
                }
            }, 1f);
        }

        // When taken hit, flash white for a certain amount of time
        protected void whiteFlash() {
            flashWhite = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    flashWhite = false;
                }
            }, flashTime);
        }

        // When taken hit, take hard knockback
        protected void knockbackHard() {
            // If target hp is over 0, then move the fighter
            if (targetHp > 0) {
                positionIncorrect = true;
                if (X < game.pixelWidth/2) {
                    // Player
                    positionOffset = -100f;
                } else {
                    // Enemy
                    positionOffset = 100f;
                }
            }
        }

        // When taken hit, take soft knockback when defending
        protected void knockbackSoft() {
            // If target hp is over 0, then move the fighter
            if (targetHp > 0) {
                positionIncorrect = true;
                if (X < game.pixelWidth/2) {
                    // Player
                    positionOffset = -50f;
                } else {
                    // Enemy
                    positionOffset = 50f;
                }
            }
        }

        // Calculates how fast to decrease hp
        protected void calcTargetHpSpd(double damage) {
            targetHp = hp - damage;
            if (targetHp < 0) targetHp = 0;
            else if (targetHp > 100) targetHp = 100;
            hpDecreaseSpd = (targetHp - hp) / 100;
        }

        // When taken hit, target hp is lower than hp. This makes the hp bar smoothly lower down
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

        // When taken hit and position is off, return back to original position
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

        // Checks hp after DoT and at the end of the player's and enemy's turn
        protected void checkIfAlive() {
            if (hp <= 0) {
                state = ifDead;
            }
        }

        protected boolean randomCritChance(int value) {
            value += critBoost;
            boolean critical = false;
            int random = MathUtils.random(1, 100);

            if (random <= value) critical = true;

            return critical;
        }

        protected boolean randomMissChance(int value) {
            value -= missBoost;
            boolean miss = false;
            int random = MathUtils.random(1, 100);

            if (random <= value) miss = true;

            return miss;
        }

        protected void addCooldown(String skill, int amount) {
            /*
             +1 since cooldowns get decreased at the beginning of round and we want cooldown "3"
             to last for 3 whole rounds
              */
            cooldowns.put(skill, amount + 1);
        }

        // At the start of each round, decrease cooldown timers
        protected void decreaseCooldowns() {
            for (Map.Entry<String, Integer> entry : cooldowns.entrySet()) {
                int value = entry.getValue();
                if (value > 0) {
                    cooldowns.put(entry.getKey(), entry.getValue() - 1);
                }
                //System.out.println(entry.getKey() + " = " + String.valueOf(entry.getValue()));
            }
        }

        // If skill has DoT, it will be added here
        protected void addDoT(int turns, double damage) {
            if (skillState == SKILL_DEFEND) {
                if (reflectingShield) {
                    opponent.dotTurns.add(turns);
                    opponent.dotDamage.add(damage);
                } // else do nothing
            } else {
                dotTurns.add(turns);
                dotDamage.add(damage);
            }
        }

        /*
        Take hit, expect if defending. If reflecting shield is unlocked,
        then return the damage back to the opponent.
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

        protected void takeHeal(double damage) {
            damage += damage * healBoost;
            game.playSound(files.sndFastHeal);
            calcTargetHpSpd(damage);
        }

        // Health bar uses this
        public double getHp() {
            return hp;
        }

        // Health bar uses this
        public double getMaxHp() {
            return maxHp;
        }

        // Opponent uses this to check if it is time to inflict damage and pass the turn
        public boolean isHitAnimationRunning() {
            return hitAnimationRunning;
        }

        // Opponent uses this to check if it is time to pass the turn
        public boolean isMissCritAnimationRunning() {
            return missCritAnimationRunning;
        }

        // Used to check if player and enemy has finished their turn on fightersTakingDamage
        public boolean isPauseStates() {
            return pauseStates;
        }

        public double getDmgBoost() {
            return dmgBoost;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    CREATE PLAYER
     */
    public class Player extends Fighters {

        private Animation<TextureRegion> defendAnim, escapeAnim, itemAnim, deathAnim;
        private HashMap<String,Object> mapAttack, mapDefend;

        private ArrayList<String> descriptions = new ArrayList<String>();
        private ArrayList<HashMap<String,Object>> mapSkills;
        private String[] skillNames;
        private double[] defaultDamages = new double[] {34, 25, 20, 25, 20, 15, 20, 15, 10};
        private String[] btnTexts = new String[] {skills.ATTACK, skills.DEFEND, skills.ITEM,
                game.getSkill1(), game.getSkill2()};

        Player() {
            X = game.gridSize;
            Y = game.gridSize*2;
            maxHp = 100;
            hp = maxHp;
            targetHp = hp;
            defaultDmg = defaultDamages[game.getPoolMult()];
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
            cooldowns.put(skills.DEFEND, 0);
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

        public void update() {
            updateStart();
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

        /*
        Iterate through the actions to find the selected action.
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
            else if (action.equals(skills.DEFEND))
            {
                if (cooldowns.get(skills.DEFEND) == 0) {
                    actionSelected = true;
                    curAnimation = defendAnim;
                    game.playSound(files.sndDefend);
                    addCooldown(skills.DEFEND, (Integer) mapDefend.get(skills.cooldown));
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
                        if (snd != null) snd.play();

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

                            // Damage over time can happen with healing and damage
                            double value = (Double) skillMap.get(skills.damageOverTime);
                            double dot;
                            // If purePercent is true, then dot value 2.0 deals 2 percent of MaxHp
                            // otherwise it deals 2.0*defaultDmg
                            if ((Boolean) skillMap.get(skills.dotPurePercent)) dot = value;
                            else dot = value * defaultDmg;
                            dotAmount = dot;
                            int dotTurns = (Integer) skillMap.get(skills.damageOverTimeTurns);
                            if (dot != 0) {
                                dealDoT = true;
                                if (dot > 0) enemy.addDoT(dotTurns, dot); // Damage
                                else if (dot < 0) addDoT(dotTurns, dot); // Healing
                            }
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

        // Add current skills to map
        private void addSkillsToMap() {
            skillNames = new String[] {game.getSkill1(), game.getSkill2()};
            mapSkills = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < 2; i++) {
                if (skillNames[i] != "") mapSkills.add(i, skills.getSkill(skillNames[i]));
            }
        }

        // Run away if escape is chosen
        private void runAway() {
            if (anim.getAnimation() != escapeAnim) anim.startAnimation(escapeAnim, animSpeed);

            if (X > - 400) {
                X -= Gdx.graphics.getDeltaTime() * 300f;
            } else {
                game.switchToRoomGame();
            }
        }

        public void selectItem(String item) {
            skillState = SKILL_ITEM;
            state = State.PLAYER_TURN;
            usedItem = item;
            game.playSound(files.sndUseItem);
            curAnimation = itemAnim;
            actionState = TEMP_ANIM;
            anim.startAnimation(curAnimation, animSpeed);
            turnState = DOING_ACTION;
            removeButtons();
            dialog.showSkillName(localize.get(item), "skillname");
        }

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
                            dialog.createDialog(descriptions.get(i), "skilldescription");
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

    /*
    CREATE ENEMY
     */
    public class Enemy extends Fighters {

        private String dialogStart, dialogEnd;
        private HashMap<String,Object> mapBoss;

        private double[] damages, damageOverTimes;
        private ArrayList<Animation<TextureRegion>> hitAnimList;
        private String[] skillNames;
        private int[] critChances, missChances, cooldownAmount, damageOverTimeTurns;
        private boolean[] dmgPurePercents, dotPurePercents;
        private Sound[] sounds;
        private int showFirstDialogTimer = 60;

        Enemy() {
            retrieveBoss();

            X = game.pixelWidth - game.gridSize -
                    idleAnim.getKeyFrame(0f).getRegionWidth();
            Y = game.gridSize*2;
            maxHp = 100;
            hp = maxHp;
            targetHp = hp;
            defaultDmg = 15;
            ifDead = State.HACK;
            ID = ENEMY;

            cooldowns = new HashMap<String, Integer>();
            cooldowns.put("Skill0", 0);
            cooldowns.put("Skill1", 0);
            cooldowns.put("Skill2", 0);

            //hitAnimList = new ArrayList<Animation<TextureRegion>>();
            //Collections.addAll(hitAnimList, skill1_hit, skill2_hit, skill3_hit);

            anim.startAnimation(idleAnim, animSpeed);
        }

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
                }
}
            updateEnd();
        }

        // Retrieve boss's information
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

            for (int i = 0; i < 3; i++) {
                // Retrieve skill's name from boss and add it to the array
                String skillName = (String) mapBoss.get(bosses.skillName + String.valueOf(i));
                skillNames[i] = skillName;

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

        // Select skill
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
                int random;
                // While skill chosen which is on cooldown, select new one
                while (true) {
                    random = MathUtils.random(0, cooldowns.size() - 1);
                    String selSkill = "Skill" + String.valueOf(random);
                    if (cooldowns.get(selSkill) == 0) {
                        addCooldown(selSkill, cooldownAmount[random]);
                        break;
                    }
                }

                // Localize name and show it, if it's attack, then retrieve boss's attackName
                String localizedName;
                if (skillNames[random] == skills.ATTACK) localizedName = localize.get(attackName);
                else localizedName = localize.get(skillNames[random]);
                dialog.showSkillName(localizedName, "skillname_enemy");

                // Play sound if not null
                if (sounds[random] != null) sounds[random].play();

                boolean miss = randomMissChance(missChances[random]);
                if (miss) skillState = SKILL_MISS;
                else {
                    double damage = damages[random];
                    // Check if skill heals, else do damage
                    if (damage < 0) {
                        skillState = SKILL_HEAL;
                        if (dmgPurePercents[random]) dmgAmount = damage;
                        else dmgAmount = damage * defaultDmg;
                    } else {
                        // If skill does not heal, get hitAnimation
                        curHitAnimation = hitAnimList.get(random);
                        // If hitAnimation is null, then it does no damage
                        if (curHitAnimation != null) {
                            skillState = SKILL_DAMAGE;

                            // If pureDmg, value 20 deals 20% of maxHp, else it deal wholeDmg*20
                            if (dmgPurePercents[random]) dmgAmount = damage;
                            else dmgAmount = damage * defaultDmg;
                            // If critical hit, deal 1.5x damage
                            dealCriticalHit = randomCritChance(critChances[random]);
                            if (dealCriticalHit) dmgAmount *= 1.5;
                        }
                    }

                    // Damage over time (can happen with healing and damage)
                    double dot;
                    double dmg = damageOverTimes[random];
                    if (dotPurePercents[random]) dot = dmg;
                    else dot = dmg * defaultDmg;
                    int dotTurns = damageOverTimeTurns[random];
                    dotAmount = dot;
                    if (dot != 0) {
                        dealDoT = true;
                        if (dot > 0) player.addDoT(dotTurns, dot); // Damage
                        else if (dot < 0) addDoT(dotTurns, dot); // Healing
                    }
                }

                curAnimation = skillAnim;
                anim.startAnimation(curAnimation, animSpeed);
            }
        }

        // When the fight begins, wait for some time to start the dialogue
        private void startDialogTimer() {
            if (state == State.START_ROOM) {
                if (showFirstDialogTimer > 0) showFirstDialogTimer--;
                else {
                    state = State.DIALOG_START;
                    dialog.createDialog(dialogStart, "dialog_enemy");
                }
            }
        }

        // Room calls this after hack is successful
        public void endDialogTimer() {
            anim.startAnimation(takeHitAnim, animSpeed);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    startIdle();
                    state = State.DIALOG_END;
                    dialog.createDialog(dialogEnd, "dialog_enemy");
                }
            }, 2);
        }
    }
}