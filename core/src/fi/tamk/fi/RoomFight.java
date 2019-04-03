package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class RoomFight extends RoomParent {

    // Enums give simple constants, which decreases the chance for coding mistakes
    enum State {
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

    private Texture hpBarLeft = game.getHpBarLeft();
    private Texture hpBarRight = game.getHpBarRight();
    private Texture imgBg, escapeBg;
    private Animation<TextureRegion> playerHealthBar, enemyHealthBar;
    private Animating animHealthPlayer = new Animating();
    private Animating animHealthEnemy = new Animating();
    private Player player;
    private Enemy enemy;
    private String[] btnTexts = new String[] {"Attack", "Defend", "Item",
                                game.getSkill1(), game.getSkill2()};
    private int btnCounter; // Used for button classes to get the correct value
    private int deathTimer = 240;
    private State state = State.START_ROOM;
    private boolean escapePopup, spawnHacking, actionButtonsOn, spawnPowerUp;
    private boolean firstHack = true;
    private ShaderProgram shFlashWhite;
    private Hacking hacking;
    private UtilPowerUp powerUp;

    //Dialog
    private float dialogX = 500f, dialogY = 500f;

    RoomFight(MainGame game) {
        super(game);
        imgBg = game.getImgBgBoss();
        escapeBg = game.getEscapeBg();

        createHealthBars();
        createShader(); // Used for flashing white

        player = new Player();
        enemy = new Enemy();
        backgroundMusic.pause();
        bossMusic.play();
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

    // Creates the upper left escape button
    private void createPowerUpButton() {
        final TextButton btn = new TextButton("PowerUpTest", skin);
        btn.setWidth(300);
        btn.setHeight(100);
        btn.setPosition(200f, game.pixelHeight - 100f);
        stage.addActor(btn);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.clear();
                state = State.POWER_UP;
            }
        });
    }

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
                deathTimer--;
                if (deathTimer <= 0) {
                    game.switchToRoomGame();
                }
                break;
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
        // These need to be optimized better, removed from here and used in MainGame
        backgroundMusic.play();
        bossMusic.stop();
        // hacking.dispose() Lisää myöhemmin
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
        createMenuButton();
        createEscapeButton();
        createActionButtons();
    }

    // When player has selected action, remove buttons
    private void removeButtons() {
        actionButtonsOn = false;
        stage.clear();
    }

    // This array has to be in same order than in Player's action array
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
    }

    // Controls the escape button's popup
    private void escaping() {
        if (escapePopup) {
            batch.draw(escapeBg, game.pixelWidth/2 - escapeBg.getWidth()/2f,
                    game.pixelHeight/2 - escapeBg.getHeight()/2f,
                    escapeBg.getWidth(), escapeBg.getHeight());
            // Temporary font and position, just for testing
            fontSteps.draw(batch, "Do you want to escape?",
                    game.pixelWidth/2 - 400, game.pixelHeight/2 + 150);
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
            }
            powerUp.update();

            if (powerUp.isPowerUpChosen()) {
                state = State.CHANGE_ROOM;
                stage.clear();
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
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

        animHealthPlayer.draw(batch, 400, game.pixelHeight - 115);
        animHealthEnemy.draw(batch, 1000, game.pixelHeight - 115);
    }

    // Creates the upper left escape button
    private void createEscapeButton() {
        final TextButton btn = new TextButton("Escape", skin);
        btn.setWidth(300);
        btn.setHeight(100);
        btn.setPosition(100f, game.pixelHeight - 100f);
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
        final TextButton btn = new TextButton("Yes", skin);
        btn.setWidth(300);
        btn.setHeight(100);
        btn.setPosition(game.pixelWidth/2 - btn.getWidth()/2, game.pixelHeight/2 - 50);
        stage.addActor(btn);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                state = State.ESCAPE;
                escapePopup = false;
                stage.clear();
            }
        });

        final TextButton btn2 = new TextButton("No", skin);
        btn2.setWidth(300);
        btn2.setHeight(100);
        btn2.setPosition(game.pixelWidth/2 - btn2.getWidth()/2, game.pixelHeight/2 - 175);
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
    class Fighters {

        protected float X, Y;
        protected double maxHp, hp, targetHp, hpDecreaseSpd, defaultDmg, dmgAmount;
        protected ArrayList<Double> dotDamage = new ArrayList<Double>();
        protected ArrayList<Integer> dotTurns = new ArrayList<Integer>();
        protected Animating anim = new Animating();
        protected Animating hitAnim = new Animating();

        protected int turnState, BEFORE = 0, TAKING_DOT = 1, START = 2, WAIT_FOR_ACTION = 3,
                                DOING_ACTION = 4, END_ACTION = 5;
        protected int actionState, TEMP_ANIM = 0, HIT_ANIM = 1, LONG_ANIM = 2;
        protected boolean flashWhite, hitAnimationRunning;
        protected float flashTime = 0.5f;
        protected State ifDead;
        protected int ID, PLAYER = 0, ENEMY = 1;

        protected float positionOffset;
        protected boolean positionIncorrect;
        protected int positionTime = 20; // How long to stay still after hit
        protected int positionTimer = positionTime;

        protected boolean pauseStates = false;
        protected boolean hpIncorrect = false;

        protected int idleSpd, skillSpd, hackSpd, takeHitSpd, itemSpd, deathSpd, escapeSpd,
                defendSpd, curHitAnimationSpd;
        protected Animation<TextureRegion> curAnimation, curHitAnimation, idleAnim, hackAnim,
                skillAnim, takeHitAnim, healthPlus, healthMinus;
        protected Integer[] hitSpeeds;
        protected HashMap<String,Integer> cooldowns;

        Fighters() {
            healthPlus = game.getAnimHealthPlusDoT();
            healthMinus = game.getAnimHealthMinusDoT();
        }

        // Do this at the start of update method
        protected void updateStart() {
            returnPosition();
            hpToTarget();
            checkToPause();
            anim.animate();
            if (hitAnim.getAnimation() != null) hitAnim.animate();
        }

        // Do this at the end of update method
        protected void updateEnd() {
            if (flashWhite) batch.setShader(shFlashWhite);
            anim.draw(batch, X + positionOffset, Y);
            drawHitAnimation();
            batch.setShader(null);
        }

        // Turn's agenda
        protected void controlTurnStates() {
            if (turnState == BEFORE) {
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
                    if (ID == PLAYER) {
                        enemy.startTurn();
                        enemy.checkIfAlive();
                    } else if (ID == ENEMY) {
                        player.startTurn();
                        player.checkIfAlive();
                    }
                    checkIfAlive();
                }
            }
        }

        protected void controlActionStates() {
            // This is needed for the parent's controlTurnStates to function correctly
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

        protected void startIdle() {
            anim.startAnimation(idleAnim, idleSpd);
        }

        protected void startHack() {
            anim.startAnimation(hackAnim, hackSpd);
        }

        // If taken hit, then pause so that new actions won't take place
        protected void checkToPause() {
            if (!hpIncorrect && !positionIncorrect) pauseStates = false;
            else pauseStates = true;
        }

        // If skill has DoT, it will be added here
        protected void addDoT(int turns, double damage) {
            dotTurns.add(turns);
            dotDamage.add(damage);
        }

        // Add all the DoTs on top of each other and decrease their turn timers
        protected void checkDoT() {
            double takeDoT = 0;
            for (int i = 0; i < dotTurns.size(); i++) {
                takeDoT += dotDamage.get(i);
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
                startHitAnimation(healthMinus, 15);
                turnState = TAKING_DOT;
            } else if (takeDoT < 0) {
                calcTargetHpSpd(takeDoT);
                startHitAnimation(healthPlus, 15);
                turnState = TAKING_DOT;
            } else {
                turnState = START;
            }
        }

        protected void changeToTakeHitAnimation() {
            anim.startAnimation(takeHitAnim, takeHitSpd);
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    startIdle();
                }
            }, 1f);
        }

        // When taken hit, flash white for a certain amount of time
        protected void flashAndMove() {
            flashWhite = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    flashWhite = false;
                }
            }, flashTime);

            // If target hp is over 0, then move the player
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

        // Calculates how fast to decrease hp
        protected void calcTargetHpSpd(double damage) {
            targetHp = hp - damage;
            if (targetHp < 0) targetHp = 0;
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
                        positionOffset--;
                    } else if (positionOffset < 0) {
                        positionOffset++;
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
                System.out.println(entry.getKey() + " = " + String.valueOf(entry.getValue()));
            }
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

        // Used to check if player and enemy has finished their turn on fightersTakingDamage
        public boolean isPauseStates() {
            return pauseStates;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    CREATE PLAYER
     */
    class Player extends Fighters {

        private Animation<TextureRegion> defendAnim, escapeAnim, itemAnim, deathAnim;
        private HashMap<String,Object> mapAttack, mapDefend;

        private String s_spd, s_dmg, s_cool, s_anim, s_hitAnim, s_name, s_DoTDmg, s_DoTTurns,
                curAction;
        private ArrayList<HashMap<String,Object>> mapSkills;
        private String[] skills;
        private double[] defaultDamages = new double[] {34, 25, 20, 25, 20, 15, 20, 15, 10};

        Player() {
            X = 100f;
            Y = 300f;
            maxHp = 100;
            hp = maxHp;
            targetHp = hp;
            defaultDmg = defaultDamages[game.getPoolBossNumber()];
            ifDead = State.DEAD;
            ID = PLAYER;

            idleSpd = 30;
            skillSpd = 30;
            defendSpd = 10;
            itemSpd = 20;
            takeHitSpd = 30;
            hackSpd = 30;
            deathSpd = 30;
            escapeSpd = 10;

            initSkillVariables();

            // Create maps for skills and cooldowns
            mapAttack = Skills.getSkill("Attack");
            mapDefend = Skills.getSkill("Defend");
            cooldowns = new HashMap<String, Integer>();
            cooldowns.put("Defend", 0);
            cooldowns.put("Skill0", 0);
            cooldowns.put("Skill1", 0);

            // Create animations (probably should be created in MainGame though)
            idleAnim = game.getAnimIdle();
            skillAnim = game.getAnimSkill();
            defendAnim = game.getAnimDefend();
            takeHitAnim = game.getAnimTakeHitAnim();
            escapeAnim = game.getAnimEscape();
            itemAnim = game.getAnimItem();
            hackAnim = game.getAnimHack();
            deathAnim = game.getAnimDeath();

            anim.startAnimation(idleAnim, idleSpd);
        }

        public void update() {
            updateStart();
            if (!pauseStates) {

                if (state == State.PLAYER_TURN) {
                    if (turnState == WAIT_FOR_ACTION) if (!actionButtonsOn) createButtons();
                    controlTurnStates();
                } else if (state == State.HACK) {
                    if (anim.getAnimation() != hackAnim) startHack();
                } else if (state == State.DEAD) {
                    if (anim.getAnimation() != deathAnim) anim.startAnimation(deathAnim, deathSpd);
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
            boolean actionSelected = false;
            int curAnimSpd = 0;

            if (action == "Attack")
            {
                actionSelected = true;
                curAnimation = skillAnim;
                curAnimSpd =  skillSpd;
                curHitAnimation = (Animation<TextureRegion>) mapAttack.get(s_hitAnim);
                curHitAnimationSpd = (Integer) mapAttack.get(s_spd + s_hitAnim);
                dmgAmount = defaultDmg;
                actionState = TEMP_ANIM;
            }
            else if (action == "Defend")
            {
                if (cooldowns.get("Defend") == 0) {
                    actionSelected = true;
                    curAnimation = defendAnim;
                    curAnimSpd = defendSpd;
                    addCooldown("Defend", (Integer) mapDefend.get(s_cool));
                    actionState = LONG_ANIM;
                }
            }
            else if (action == "Item")
            {
                actionSelected = true;
                curAnimation = itemAnim;
                curAnimSpd = itemSpd;
                actionState = TEMP_ANIM;
            }
            else { // It's skill
                for (int i = 0; i < 2; i++) {
                    /*
                    If selected action is this skill, if skill is not empty and if cooldown is 0
                     */
                    if (action == skills[i] && skills[i] != "" &&
                            cooldowns.get("Skill" + String.valueOf(i)) == 0) {
                        actionSelected = true;
                        HashMap<String, Object> skillMap = mapSkills.get(i);
                        addCooldown("Skill" + String.valueOf(i),
                                (Integer) skillMap.get(s_cool));
                        curAnimation = (Animation<TextureRegion>) skillMap.get(s_anim);
                        curAnimSpd = (Integer) skillMap.get(s_spd + s_anim);
                        curHitAnimation = (Animation<TextureRegion>) skillMap.get(s_hitAnim);
                        curHitAnimationSpd = (Integer) skillMap.get(s_spd + s_hitAnim);
                        dmgAmount = defaultDmg * (Double) skillMap.get(s_dmg);
                        // Damage over time
                        double dot = (Double) skillMap.get(s_DoTDmg);
                        int dotTurns = (Integer) skillMap.get(s_DoTTurns);
                        if (dot == 0); // Do nothing
                        else if (dot > 0) enemy.addDoT(dotTurns, dot); // Damage
                        else if (dot < 0) addDoT(dotTurns, dot); // Healing
                        actionState = TEMP_ANIM;
                    }
                }
            }

            // If player touched something that was on cooldown, actionSelected remains false
            if (actionSelected) {
                curAction = action;
                anim.startAnimation(curAnimation, curAnimSpd);
                turnState = DOING_ACTION;
                removeButtons();
                dialog.showSkillName(action);
            }
        }

        protected void controlActionStates() {
            if (actionState == TEMP_ANIM) {
                // If temporary animation is finished, and hitAnimation exists, draw hit animation
                // on enemy's draw method. If not causeDamage, start enemy's turn
                if (curAnimation.isAnimationFinished(anim.getStateTime())) {
                    if (curHitAnimation != null) {
                        enemy.startHitAnimation(curHitAnimation, curHitAnimationSpd);
                        actionState = HIT_ANIM;
                    } else {
                        turnState = END_ACTION;
                    }
                    startIdle();
                }
            } else if (actionState == HIT_ANIM) {
                // Hit animation is drawn on top of enemy
                if (!enemy.isHitAnimationRunning()) {
                    enemy.takeHit(dmgAmount);
                    turnState = END_ACTION;
                }
            } else if (actionState == LONG_ANIM) {
                // Animation lasts until next round
                turnState = END_ACTION;
            }
        }

        // Initialize skill variables for easy access
        private void initSkillVariables() {
            skills = new String[] {game.getSkill1(), game.getSkill2()};
            mapSkills = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < 2; i++) {
                if (skills[i] != "") mapSkills.add(i, Skills.getSkill(skills[i]));
            }
            s_spd = Skills.speed;
            s_dmg = Skills.damage;
            s_name = Skills.name;
            s_anim = Skills.animation;
            s_hitAnim = Skills.hitAnimation;
            s_cool = Skills.cooldown;
            s_DoTDmg = Skills.damageOverTime;
            s_DoTTurns = Skills.damageOverTimeTurns;
        }


        // Take hit, expect if defending, then return the damage back to the enemy.
        public void takeHit(double damage) {
            if (curAction == "Defend") {
                enemy.takeHit(damage);
            } else {
                flashAndMove();
                calcTargetHpSpd(damage);
                changeToTakeHitAnimation();
            }
        }


        // Run away if escape is chosen
        private void runAway() {
            if (anim.getAnimation() != escapeAnim) anim.startAnimation(escapeAnim, escapeSpd);

            if (X > - 400) {
                X -= Gdx.graphics.getDeltaTime() * 150f;
            } else {
                game.switchToRoomGame();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    CREATE ENEMY
     */
    class Enemy extends Fighters {

        private Animation<TextureRegion> skill1_hit, skill2_hit, skill3_hit;
        private String dialogStart, dialogEnd;
        private HashMap<String,Object> mapBoss;

        private int actionDelay = 30;
        private int actionTimer = actionDelay;
        private double[] damages, damageOverTimes;
        private ArrayList<Animation<TextureRegion>> hitAnimList;
        private String[] skillNames;
        private int[] cooldownAmount, damageOverTimeTurns;

        Enemy() {
            retrieveBoss();

            X = game.pixelWidth - 100f - idleAnim.getKeyFrame(0f).getRegionWidth();
            Y = 300f;
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

            startDialogTimer();

            //hitAnimList = new ArrayList<Animation<TextureRegion>>();
            //Collections.addAll(hitAnimList, skill1_hit, skill2_hit, skill3_hit);

            anim.startAnimation(idleAnim, idleSpd);
        }

        public void update() {
            updateStart();

            if (!pauseStates) {

                if (state == State.ENEMY_TURN) {
                    if (turnState == WAIT_FOR_ACTION) attack();
                    controlTurnStates();
                } else if (state == State.HACK) {
                    if (anim.getAnimation() != hackAnim) anim.startAnimation(hackAnim, hackSpd);
                } else if (state == State.HACK_FAILED) {
                    calcTargetHpSpd(-maxHp/3);
                    state = State.HACK_RESTORING;
                } else if (state == State.HACK_RESTORING) {
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
            mapBoss = Bosses.getBoss(Bosses.COPPER);

            // Use these arrays when selecting skills in attack()
            skillNames = new String[3];
            hitAnimList = new ArrayList<Animation<TextureRegion>>();
            hitSpeeds = new Integer[3];
            damages = new double[3];
            cooldownAmount = new int[3];
            damageOverTimes = new double[3];
            damageOverTimeTurns = new int[3];

            for (int i = 0; i < 3; i++) {
                // Retrieve skill's name from boss and add it to the array
                String skillName = (String) mapBoss.get(Bosses.skillName + String.valueOf(i));
                skillNames[i] = skillName;

                // Retrieve the skills map from Skills class which contains skill values
                HashMap<String, Object> mapSkill = Skills.getSkill(skillName);

                // Retrieve hit animation and it's speed from Skills class
                Animation<TextureRegion> skillHit =
                        (Animation<TextureRegion>) mapSkill.get(Skills.hitAnimation);
                hitAnimList.add(skillHit);
                int skillHitSpd = (Integer) mapSkill.get(Skills.speed + Skills.hitAnimation);
                hitSpeeds[i] = skillHitSpd;

                // Retrieve skills's damage
                double dmg = (Double) mapSkill.get(Skills.damage);
                damages[i] = dmg;

                // Retrieve skill's cooldown
                int cd = (Integer) mapSkill.get(Skills.cooldown);
                cooldownAmount[i] = cd;

                // Retrieve skills' damage over time
                double dot = (Double) mapSkill.get(Skills.damageOverTime);
                damageOverTimes[i] = dot;

                // Retrieve skill's damage over time turns
                int dotTurn = (Integer) mapSkill.get(Skills.damageOverTimeTurns);
                damageOverTimeTurns[i] = dotTurn;
            }

            // Retrieve enemy animations and speed
            idleAnim = (Animation<TextureRegion>) mapBoss.get(Bosses.idle);
            skillAnim = (Animation<TextureRegion>) mapBoss.get(Bosses.skill);
            hackAnim = (Animation<TextureRegion>) mapBoss.get(Bosses.hack);
            takeHitAnim = (Animation<TextureRegion>) mapBoss.get(Bosses.takeHit);
            idleSpd = (Integer) mapBoss.get(Bosses.speed + Bosses.idle);
            skillSpd = (Integer) mapBoss.get(Bosses.speed + Bosses.skill);
            hackSpd = (Integer) mapBoss.get(Bosses.speed + Bosses.hack);
            takeHitSpd = (Integer) mapBoss.get(Bosses.speed + Bosses.takeHit);

            // Retrieve dialog start and end from Boss
            dialogStart = (String) mapBoss.get(Bosses.dialogStart);
            dialogEnd = (String) mapBoss.get(Bosses.dialogEnd);
        }

        // Select skill
        private void attack() {
            // Wait for timer and skill name box to go down, then select action
            if (actionTimer > 0 || dialog.isSkillNameOn()) {
                actionTimer--;
            } else {
                actionTimer = actionDelay;
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
                dialog.showSkillName(skillNames[random]);
                curHitAnimation = hitAnimList.get(random);
                curHitAnimationSpd = hitSpeeds[random];
                dmgAmount = defaultDmg * damages[random];

                // Damage over time
                double dot = damageOverTimes[random];
                int dotTurns = damageOverTimeTurns[random];
                if (dot == 0); // Do nothing
                else if (dot > 0) player.addDoT(dotTurns, dot); // Damage
                else if (dot < 0) addDoT(dotTurns, dot); // Healing

                anim.startAnimation(skillAnim, skillSpd);
            }
        }

        // Control what happens once action has been selected
        protected void controlActionStates() {
            if (actionState == TEMP_ANIM) {
                if (skillAnim.isAnimationFinished(anim.getStateTime())) {

                    if (curHitAnimation != null) {
                        player.startHitAnimation(curHitAnimation, curHitAnimationSpd);
                        actionState = HIT_ANIM;
                    } else {
                        turnState = END_ACTION;
                    }
                    startIdle();
                }
            } else if (actionState == HIT_ANIM) {
                if (!player.isHitAnimationRunning()) {
                    player.takeHit(dmgAmount);
                    turnState = END_ACTION;
                }
            }
        }

        // When taking hit, lower targetHp, flash white and take knockback
        public void takeHit(double damage) {
            flashAndMove();
            calcTargetHpSpd(damage);
            changeToTakeHitAnimation();
        }

        // When the fight begins, wait for some time to start the dialogue
        private void startDialogTimer() {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    state = State.DIALOG_START;
                    dialog.createDialog(dialogStart, dialogX, dialogY);
                }
            }, 1);
        }

        // Room calls this after hack is successful
        public void endDialogTimer() {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    state = State.DIALOG_END;
                    dialog.createDialog(dialogEnd, dialogX, dialogY);
                }
            }, 2);
        }
    }
}