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
        PLAYER_TURN,
        ENEMY_TURN,
        HACK,
        HACK_SUCCESS,
        HACK_FAILED,
        HACK_RESTORING,
        DEAD,
        ESCAPE
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
    private boolean escapePopup, spawnHacking, actionButtonsOn;
    private boolean firstHack = true;
    private ShaderProgram shFlashWhite;
    private Hacking hacking;

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

            hackingPhase();
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    private void universalStateChecks() {
        switch (state) {
            // If dialog box has been closed, start the turn
            case DIALOG_START: {
                if (!dialog.isDialogOn()) player.startTurn();
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

    private void createButtons() {
        actionButtonsOn = true;
        createMenuButton();
        createEscapeButton();
        createActionButtons();
    }

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

    private void hackingPhase() {
        if (state == State.HACK) {
            if (!spawnHacking) {
                spawnHacking = true;
                hacking = new Hacking(game, firstHack);
                firstHack = false;
            }
            hacking.update();

            if (hacking.isBulletHitEnemy()) {
                state = State.HACK_SUCCESS;
            } else if (hacking.isBulletMissedEnemy()) {
                state = State.HACK_FAILED;
                spawnHacking = false;
            }
        }
    }

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

        protected int idleSpd, hackSpd, itemSpd, deathSpd, escapeSpd, curHitAnimationSpd;
        protected Animation<TextureRegion> curAnimation, curHitAnimation, idle, hack, healthPlus,
                                            healthMinus;
        protected ArrayList<Animation<TextureRegion>> animList, hitAnimList;
        protected Integer[] speeds, hitSpeeds;
        protected HashMap<String,Integer> cooldowns;

        Fighters() {
            healthPlus = hitAnim.createAnimation(game.getHealthPlus(), 3, 1);
            healthMinus = hitAnim.createAnimation(game.getHealthMinus(), 3, 1);
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
                if (anim.getAnimation() != idle) startIdle();

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
                    System.out.println("turnEnd");
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
            anim.startAnimation(idle, idleSpd);
        }

        protected void startHack() {
            anim.startAnimation(hack, hackSpd);
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
            /*
            If DoT is over 0, then do damage, if it's under 0, then heal, else start the turn
             */
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
                if (hp < targetHp) {
                    hp = targetHp;
                    hpIncorrect = false;
                }
            } else if (hp < targetHp){
                hpIncorrect = true;
                hp += hpDecreaseSpd;
                if (hp > targetHp) {
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

        /*
        Check Hp at the start of every round.
         */
        protected void checkIfAlive() {
            if (hp <= 0) {
                state = ifDead;
            }
        }

        /*
        At the start of each round, decrease cooldown timers.
         */
        protected void decreaseCooldowns() {
            for (Map.Entry<String, Integer> entry : cooldowns.entrySet()) {
                //System.out.println(entry.getKey() + " = " + String.valueOf(entry.getValue()));
                int value = entry.getValue();
                if (value > 0) {
                    cooldowns.put(entry.getKey(), entry.getValue() - 1);
                }
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

        private Animation<TextureRegion> escape, item, death;
        private HashMap<String,Object> mapAttack, mapDefend;

        private String s_spd, s_dmg, s_cool, s_anim, s_hitAnim, s_name, s_DoTDmg, s_DoTTurns,
                curAction;
        private ArrayList<HashMap<String,Object>> mapSkills;
        private String[] skills;

        Player() {
            X = 100f;
            Y = 300f;
            maxHp = 100;
            hp = maxHp;
            targetHp = hp;
            defaultDmg = 30; // Replace this with the correct value later
            ifDead = State.DEAD;
            ID = PLAYER;

            idleSpd = 30;
            itemSpd = 20;
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

            // Discovered: Learned how to use methods from other classes.
            /*float Testy = PowerUps.getHeight();
            System.out.println(Testy);*/

            // Create animations (probably should be created in MainGame though)
            idle = anim.createAnimation(game.getPlayerIdle(), 3, 1);
            escape = anim.createAnimation(game.getPlayerEscape(), 3, 1);
            item = anim.createAnimation(game.getPlayerItem(), 3, 1);
            hack = anim.createAnimation(game.getPlayerHack(), 3, 1);
            death = anim.createAnimation(game.getPlayerDeath(), 3, 1);

            anim.startAnimation(idle, idleSpd);
        }

        public void update() {
            updateStart();
            if (!pauseStates) {


                if (state == State.PLAYER_TURN) {
                    if (turnState == WAIT_FOR_ACTION) if (!actionButtonsOn) createButtons();
                    controlTurnStates();
                } else if (state == State.HACK) {
                    if (anim.getAnimation() != hack) startHack();
                } else if (state == State.DEAD) {
                    if (anim.getAnimation() != death) anim.startAnimation(death, deathSpd);
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

            if (action == "Attack"){
                actionSelected = true;
                curAnimation = (Animation<TextureRegion>) mapAttack.get(s_anim);
                curAnimSpd =  (Integer) mapAttack.get(s_spd + s_anim);
                curHitAnimation = (Animation<TextureRegion>) mapAttack.get(s_hitAnim);
                curHitAnimationSpd = (Integer) mapAttack.get(s_spd + s_hitAnim);
                dmgAmount = defaultDmg;
                actionState = TEMP_ANIM;
            } else if (action == "Defend") {
                if (cooldowns.get("Defend") == 0) {
                    actionSelected = true;
                    curAnimation = (Animation<TextureRegion>) mapDefend.get(s_anim);
                    curAnimSpd = (Integer) mapDefend.get(s_spd + s_anim);
                    cooldowns.put("Defend", (Integer) mapDefend.get(s_cool));
                    actionState = LONG_ANIM;
                }
            } else if (action == "Item") {
                actionSelected = true;
                curAnimation = item;
                curAnimSpd = itemSpd;
                actionState = TEMP_ANIM;
            } else { // It's skill
                for (int i = 0; i < 2; i++) {
                    /*
                    If selected action is this skill, if skill is not empty and if cooldown is 0
                     */
                    if (action == skills[i] && skills[i] != "" &&
                            cooldowns.get("Skill" + String.valueOf(i)) == 0) {
                        actionSelected = true;
                        HashMap<String, Object> skillMap = mapSkills.get(i);
                        cooldowns.put("Skill" + String.valueOf(i),
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

        private void initSkillVariables() {
            skills = new String[] {game.getSkill1(), game.getSkill2()};
            mapSkills = new ArrayList<HashMap<String, Object>>();
            for (int i = 0; i < 2; i++) {
                if (skills[i] != "") mapSkills.add(i, Skills.getSkill(skills[i]));
            }
            s_spd = Skills.getSpeed();
            s_dmg = Skills.getDamage();
            s_name = Skills.getName();
            s_anim = Skills.getAnimation();
            s_hitAnim = Skills.getHitAnimation();
            s_cool = Skills.getCooldown();
            s_DoTDmg = Skills.getDamageOverTime();
            s_DoTTurns = Skills.getDamageOverTimeTurns();
        }

        /*
        Take hit, expect if defending, then return the damage back to the enemy.
         */
        public void takeHit(double damage) {
            if (curAction == "Defend") {
                enemy.takeHit(damage);
            } else {
                flashAndMove();
                calcTargetHpSpd(damage);
            }
        }

        /*
        Run away if escape is chosen
         */
        private void runAway() {
            if (anim.getAnimation() != escape) anim.startAnimation(escape, escapeSpd);

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

        private Animation<TextureRegion> skill1, skill2, skill3, skill1_hit, skill2_hit, skill3_hit;
        private String curSkillName, dialogStart, dialogEnd;
        private HashMap<String,Object> mapBoss;

        private int actionDelay = 30;
        private int actionTimer = actionDelay;
        private double[] damages;
        private String[] skillNames;
        private int[] cooldownAmount;

        Enemy() {
            retrieveBoss();

            X = game.pixelWidth - 100f - idle.getKeyFrame(0f).getRegionWidth();
            Y = 300f;
            maxHp = 100;
            hp = maxHp;
            targetHp = hp;
            defaultDmg = 15; // Replace this with the correct value later
            ifDead = State.HACK;
            ID = ENEMY;

            cooldowns = new HashMap<String, Integer>();
            cooldowns.put("Skill0", 0);
            cooldowns.put("Skill1", 0);
            cooldowns.put("Skill2", 0);

            startDialogTimer();

            animList = new ArrayList<Animation<TextureRegion>>();
            Collections.addAll(animList, skill1, skill2, skill3);

            hitAnimList = new ArrayList<Animation<TextureRegion>>();
            Collections.addAll(hitAnimList, skill1_hit, skill2_hit, skill3_hit);

            anim.startAnimation(idle, idleSpd);
        }

        public void update() {
            updateStart();

            if (!pauseStates) {

                // Pretty much same stuff happens as in player's action states
                if (state == State.ENEMY_TURN) {
                    if (turnState == WAIT_FOR_ACTION) attack();
                    controlTurnStates();
                } else if (state == State.HACK) {
                    if (anim.getAnimation() != hack) anim.startAnimation(hack, hackSpd);
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

        private void retrieveBoss() {
            mapBoss = Bosses.getBoss("Roombot");
            String skill = Bosses.getSkill();
            String skillHit = Bosses.getSkillHit();
            String skillName = Bosses.getSkillName();
            String spd = Bosses.getSpeed();
            String dmg = Bosses.getDamage();
            String skillCd = Bosses.getSkillCooldown();

            // Retrieve Strings
            String skill1_name = (String) mapBoss.get(skillName + "1");
            String skill2_name = (String) mapBoss.get(skillName + "2");
            String skill3_name = (String) mapBoss.get(skillName + "3");
            dialogStart = (String) mapBoss.get(Bosses.getDialogStart());
            dialogEnd = (String) mapBoss.get(Bosses.getDialogEnd());

            skillNames = new String[] {skill1_name, skill2_name, skill3_name};

            // Retrieve animations
            idle = (Animation<TextureRegion>) mapBoss.get(Bosses.getIdle());
            skill1 = (Animation<TextureRegion>) mapBoss.get(skill + "1");
            skill2 = (Animation<TextureRegion>) mapBoss.get(skill + "2");
            skill3 = (Animation<TextureRegion>) mapBoss.get(skill + "3");
            skill1_hit = (Animation<TextureRegion>) mapBoss.get(skillHit + "1");
            skill2_hit = (Animation<TextureRegion>) mapBoss.get(skillHit + "2");
            skill3_hit = (Animation<TextureRegion>) mapBoss.get(skillHit + "3");
            hack = (Animation<TextureRegion>) mapBoss.get(Bosses.getHack());

            // Retrieve damages
            double dmg1 = Double.valueOf(mapBoss.get(dmg + "1").toString());
            double dmg2 = Double.valueOf(mapBoss.get(dmg + "2").toString());
            double dmg3 = Double.valueOf(mapBoss.get(dmg + "3").toString());

            damages = new double[] {dmg1, dmg2, dmg3};

            // Retrieve cooldowns
            int cd1 = (Integer) mapBoss.get(skillCd + "1");
            int cd2 = (Integer) mapBoss.get(skillCd + "2");
            int cd3 = (Integer) mapBoss.get(skillCd + "3");

            cooldownAmount = new int[] {cd1, cd2, cd3};

            // Retrieve animation speeds
            idleSpd = (Integer) mapBoss.get(spd + Bosses.getIdle());
            int skill1Spd = (Integer) mapBoss.get(spd + skill + "1");
            int skill2Spd = (Integer) mapBoss.get(spd + skill + "2");
            int skill3Spd = (Integer) mapBoss.get(spd + skill + "3");
            int skill1HitSpd = (Integer) mapBoss.get(spd + skillHit + "1");
            int skill2HitSpd = (Integer) mapBoss.get(spd + skillHit + "2");
            int skill3HitSpd = (Integer) mapBoss.get(spd + skillHit + "3");
            hackSpd = (Integer) mapBoss.get(spd + Bosses.getHack());

            speeds = new Integer[] {skill1Spd, skill2Spd, skill3Spd};
            hitSpeeds = new Integer[] {skill1HitSpd, skill2HitSpd, skill3HitSpd};
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
                    random = MathUtils.random(0, animList.size() - 1);
                    String selSkill = "Skill" + String.valueOf(random);
                    if (cooldowns.get(selSkill) == 0) {
                        cooldowns.put(selSkill, cooldownAmount[random]);
                        break;
                    }
                }
                dialog.showSkillName(skillNames[random]);
                curAnimation = animList.get(random);
                curHitAnimation = hitAnimList.get(random);
                curHitAnimationSpd = hitSpeeds[random];
                dmgAmount = defaultDmg * damages[random];
                anim.startAnimation(curAnimation, speeds[random]);


                // Discovered: Added this for testing purposes.
                /*int i = 2;

                dialog.showSkillName(skillNames[i]);
                curAnimation = animList.get(i);
                curHitAnimation = hitAnimList.get(i);
                curHitAnimationSpd = hitSpeeds[i];
                dmgAmount = defaultDmg * damages[i];
                anim.startAnimation(curAnimation, speeds[i]);

                System.out.println(skillNames[i]);*/
            }
        }

        // Control what happens once action has been selected
        protected void controlActionStates() {
            if (actionState == TEMP_ANIM) {
                if (curAnimation.isAnimationFinished(anim.getStateTime())) {
                    player.startHitAnimation(curHitAnimation, curHitAnimationSpd);
                    startIdle();
                    actionState = HIT_ANIM;
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
    }
}