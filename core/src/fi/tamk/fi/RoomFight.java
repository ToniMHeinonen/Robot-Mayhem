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
        START_TURN,
        AWAITING,
        ACTION,
        ENEMY_WAITING,
        ENEMY_ACTION,
        HACK,
        DEAD,
        ESCAPE
    }

    private Texture imgBg, escapeBg, hpBarLeft, hpBarRight;
    private Animation<TextureRegion> playerHealthBar, enemyHealthBar;
    private Animating animHealthPlayer = new Animating();
    private Animating animHealthEnemy = new Animating();
    private Player player;
    private Enemy enemy;
    private String[] btnTexts = new String[] {"Attack", "Defend", "Item"};
    private int btnCounter; // Used for button classes to get the correct value
    private int deathTimer = 240;
    private State state = State.START_ROOM;
    private boolean escapePopup;
    private ShaderProgram shFlashWhite;

    //Dialog
    private float dialogX = 500f, dialogY = 500f;

    RoomFight(MainGame game) {
        super(game);
        imgBg = game.getImgBgBoss();
        escapeBg = game.getEscapeBg();

        createHealthBars();
        createButtons();
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

            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    private void universalStateChecks() {
        switch (state) {
            // If dialog box has been closed, start the turn
            case DIALOG_START: {
                if (!dialog.isDialogOn()) state = State.START_TURN;
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
    }

    private void createHealthBars() {
        // Retrieve Texture
        hpBarLeft = game.getHpBarLeft();
        hpBarRight = game.getHpBarRight();
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
        createMenuButton();
        createEscapeButton();
        createActionButtons();
    }

    // This array has to be in same order than in Player's action array
    private void createActionButtons() {
        float space = 400f;
        for (int i = 0; i < btnTexts.length; i++) {
            btnCounter = i;
            final TextButton btn = new TextButton(btnTexts[i], skin);
            btn.setWidth(300);
            btn.setHeight(100);
            btn.setPosition(100f + i*space, 100f);
            stage.addActor(btn);

            btn.addListener(new ClickListener() {
                int i = btnCounter;
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (state == State.AWAITING) {
                        state = State.ACTION;
                        player.doAction(i);
                    }
                }
            });
        }
    }

    public void scheduleState(final State s, float time) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                state = s;
            }
        }, time);
    }

    // Controls the escape button's popup
    private void escaping() {
        if (escapePopup) {
            batch.draw(escapeBg, game.pixelWidth/2 - escapeBg.getWidth()/2,
                    game.pixelHeight/2 - escapeBg.getHeight()/2,
                    escapeBg.getWidth(), escapeBg.getHeight());
            // Temporary font and position, just for testing
            fontSteps.draw(batch, "Do you want to escape?",
                    game.pixelWidth/2 - 400, game.pixelHeight/2 + 150);
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
                if (state == State.AWAITING) {
                    if (!escapePopup) {
                        escapePopup = true;
                        stage.clear();
                        createYesNo();
                    }
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

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    CREATE PARENT FIGHTER
     */
    private class Fighters {

        protected float X, Y;
        protected double maxHp, hp, targetHp, hpDecreaseSpd, dmgAmount;
        protected Animating anim = new Animating();
        protected Animating hitAnim = new Animating();

        protected int actionState, NONE = -1, TEMP_ANIM = 0, HIT_ANIM = 1, LONG_ANIM = 2;
        protected boolean flashWhite, hitAnimationRunning;
        protected float flashTime = 0.5f;

        protected float positionOffset;
        protected boolean positionIncorrect;
        protected int positionTime = 20; // How long to stay still after hit
        protected int positionTimer = positionTime;

        protected boolean pauseStates = false;
        protected boolean hpIncorrect = false;

        protected int idleSpd, hackSpd, deathSpd, escapeSpd, curHitAnimationSpd;
        protected Animation<TextureRegion> curAnimation, curHitAnimation, idle, hack;
        protected ArrayList<Animation<TextureRegion>> animList, hitAnimList;
        protected Integer[] speeds, hitSpeeds;

        // Do this at the start of update method
        public void updateStart() {
            returnPosition();
            hpToTarget();
            checkToPause();
            anim.animate();
            if (hitAnim.getAnimation() != null) hitAnim.animate();
        }

        // Do this at the end of update method
        public void updateEnd() {
            if (flashWhite) batch.setShader(shFlashWhite);
            anim.draw(batch, X + positionOffset, Y);
            drawHitAnimation();
            batch.setShader(null);
        }

        // Opponent starts this when hitting you
        public void startHitAnimation(Animation<TextureRegion> animation, int spd) {
            hitAnimationRunning = true;
            hitAnim.startAnimation(animation, spd);
        }

        // Draw the animation for as long as it lasts
        public void drawHitAnimation() {
            if (hitAnimationRunning) {
                if (hitAnim.getAnimation().isAnimationFinished(hitAnim.getStateTime())) {
                    hitAnimationRunning = false;
                } else {
                    hitAnim.draw(batch, X, Y);
                }
            }
        }

        public void startIdle() {
            anim.startAnimation(idle, idleSpd);
        }

        public void startHack() {
            anim.startAnimation(hack, hackSpd);
        }

        // If taken hit, then pause so that new actions won't take place
        public void checkToPause() {
            if (!hpIncorrect && !positionIncorrect) pauseStates = false;
            else pauseStates = true;
        }

        // When taken hit, flash white for a certain amount of time
        public void flashAndMove() {
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

        // When taken hit, target hp is lower than hp. This makes the hp bar smoothly lower down
        public void hpToTarget() {
            if (hp > targetHp) {
                hpIncorrect = true;
                hp -= hpDecreaseSpd;
            } else {
                hpIncorrect = false;
                hp = targetHp;
            }
        }

        // When taken hit and position is off, return back to original position
        public void returnPosition() {
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
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    CREATE PLAYER
     */
    private class Player extends Fighters {

        private Animation<TextureRegion> attack, attackHit, defend, escape, item, death;
        private HashMap<String,Object> mapAttack, mapDefend;

        private boolean causeDamage;
        private HashMap<String,Integer> cooldowns;

        Player() {
            X = 100f;
            Y = 300f;
            maxHp = game.getPlayerMaxHp();
            hp = maxHp;
            targetHp = hp;

            idleSpd = 30;
            hackSpd = 30;
            deathSpd = 30;
            escapeSpd = 10;

            // Create maps for skills and cooldowns
            mapAttack = Skills.getSkill("attack");
            mapDefend = Skills.getSkill("defend");
            cooldowns = new HashMap<String, Integer>();
            cooldowns.put("attack", 0);
            cooldowns.put("defend", 0);

            // Create animations (probably should be created in MainGame though)
            idle = anim.createAnimation(game.getPlayerIdle(), 3, 1);
            attack = (Animation<TextureRegion>) mapAttack.get(Skills.getAnimation());
            attackHit = (Animation<TextureRegion>) mapAttack.get(Skills.getHitAnimation());
            defend = (Animation<TextureRegion>) mapDefend.get(Skills.getAnimation());
            escape = anim.createAnimation(game.getPlayerEscape(), 3, 1);
            item = anim.createAnimation(game.getPlayerItem(), 3, 1);
            hack = anim.createAnimation(game.getPlayerHack(), 3, 1);
            death = anim.createAnimation(game.getPlayerDeath(), 3, 1);

            // Fill needed arrays for doAction() method
            animList = new ArrayList<Animation<TextureRegion>>();
            Collections.addAll(animList, attack, defend, item);
            speeds = new Integer[] {30, 5, 30};

            anim.startAnimation(idle, idleSpd);
        }

        public void update() {
            updateStart();
            if (!pauseStates) {
                checkHp();

                if (state == State.START_TURN) {
                    decreaseCooldowns();
                    state = State.AWAITING;
                } else if (state == State.ACTION) {
                    controlActionStates();
                } else if (state == State.AWAITING) {
                    if (anim.getAnimation() != idle) startIdle();
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
        public void doAction(int index) {
            curAnimation = animList.get(index);
            // Reset necessary values
            curHitAnimation = null;
            causeDamage = false;
            actionState = NONE;

            if (curAnimation == attack){
                curHitAnimation = attackHit;
                causeDamage = true;
                /*
                Explanation: Object can't be cast to Double, so the object has to be first cast to
                String and then get the value of String. This is so stupid...
                 */
                dmgAmount = Double.valueOf(mapAttack.get(Skills.getDamage()).toString());
                anim.startAnimation(curAnimation, speeds[index]);
                actionState = TEMP_ANIM;
                dialog.showSkillName("Attack");
            } else if (curAnimation == defend) {
                if (cooldowns.get("defend") > 0) {
                    state = State.AWAITING;
                } else {
                    anim.startAnimation(curAnimation, speeds[index]);
                    cooldowns.put("defend", (Integer) mapDefend.get(Skills.getCooldown()));
                    actionState = LONG_ANIM;
                    scheduleState(State.ENEMY_WAITING, 2.5f);
                    dialog.showSkillName("Defend");
                }
            } else if (curAnimation == item) {
                anim.startAnimation(curAnimation, speeds[index]);
                actionState = TEMP_ANIM;
                dialog.showSkillName("Item");
            }
        }

        private void controlActionStates() {
            if (actionState == TEMP_ANIM) {
                // If temporary animation is finished, and causeDamage is true, draw hit animation
                // on enemy's draw method. If not causeDamage, start enemy's turn
                if (curAnimation.isAnimationFinished(anim.getStateTime())) {
                    if (causeDamage) {
                        enemy.startHitAnimation(curHitAnimation, 15);
                        actionState = HIT_ANIM;
                    } else {
                        state = State.ENEMY_WAITING;
                    }
                    startIdle();
                }
            } else if (actionState == HIT_ANIM) {
                // Hit animation is drawn on top of enemy
                if (!enemy.isHitAnimationRunning()) {
                    enemy.takeHit(dmgAmount);
                    state = State.ENEMY_WAITING;
                }
            } else if (actionState == LONG_ANIM) {
                // Animation lasts until next round
            }
        }

        /*
        Check Hp at the start of every round.
         */
        private void checkHp() {
            if (hp <= 0) {
                state = State.DEAD;
            }
        }

        /*
        Take hit, expect if defending, then return the damage back to the enemy.
         */
        public void takeHit(double damage) {
            if (curAnimation == defend) enemy.takeHit(damage);
            else {
                targetHp = hp - damage;
                flashAndMove();
            }

            if (targetHp < 0) targetHp = 0;
            hpDecreaseSpd = (hp - targetHp) / 100;
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

        /*
        At the start of each round, decrease cooldown timers.
         */
        private void decreaseCooldowns() {
            for (Map.Entry<String, Integer> entry : cooldowns.entrySet()) {
                //System.out.println(entry.getKey() + " = " + String.valueOf(entry.getValue()));
                int value = entry.getValue();
                if (value > 0) {
                    cooldowns.put(entry.getKey(), entry.getValue() - 1);
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /*
    CREATE ENEMY
     */
    private class Enemy extends Fighters {

        private Animation<TextureRegion> skill1, skill2, skill3, skill1_hit, skill2_hit, skill3_hit;
        private String curSkillName, dialogStart, dialogEnd;
        private HashMap<String,Object> mapBoss;

        private int actionDelay = 30;
        private int actionTimer = actionDelay;
        private double[] damages;
        private String[] skillNames;

        Enemy() {
            retrieveBoss();

            X = game.pixelWidth - 100f - idle.getKeyFrame(0f).getRegionWidth();
            Y = 300f;
            maxHp = 5;
            hp = maxHp;
            targetHp = hp;

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
                checkHp();
                attack();

                // Pretty much same stuff happens as in player's action states
                if (state == State.ENEMY_ACTION) {
                    if (actionState == TEMP_ANIM) {
                        if (curAnimation.isAnimationFinished(anim.getStateTime())) {
                            player.startHitAnimation(curHitAnimation, curHitAnimationSpd);
                            startIdle();
                            actionTimer = actionDelay;
                            actionState = HIT_ANIM;
                        }
                    } else if (actionState == HIT_ANIM) {
                        if (!player.isHitAnimationRunning()) {
                            player.takeHit(dmgAmount);
                            state = State.START_TURN;
                        }
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
            double dmg1 = Double.valueOf(mapBoss.get(Bosses.getDamage() + "1").toString());
            double dmg2 = Double.valueOf(mapBoss.get(Bosses.getDamage() + "2").toString());
            double dmg3 = Double.valueOf(mapBoss.get(Bosses.getDamage() + "3").toString());

            damages = new double[] {dmg1, dmg2, dmg3};

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

        /*
        Attack if state is ENEMY_WAITING.
         */
        private void attack() {
            if (state == State.ENEMY_WAITING) {
                // Wait for timer to go down, then select action
                if (actionTimer > 0) {
                    actionTimer--;
                } else {
                    state = State.ENEMY_ACTION;
                    actionState = TEMP_ANIM;
                    int random = MathUtils.random(0, animList.size() - 1);
                    dialog.showSkillName(skillNames[random]);
                    curAnimation = animList.get(random);
                    curHitAnimation = hitAnimList.get(random);
                    curHitAnimationSpd = hitSpeeds[random];
                    dmgAmount = damages[random];
                    anim.startAnimation(curAnimation, speeds[random]);
                }
            }
        }

        /*
        Check hp before anything else in render.
         */
        private void checkHp() {
            if (hp <= 0) {
                state = State.HACK;
                if (anim.getAnimation() != hack) anim.startAnimation(hack, hackSpd);
            }
        }

        // When taking hit, lower targetHp, flash white and take knockback
        public void takeHit(double damage) {
            targetHp = hp - damage;
            flashAndMove();
            if (targetHp < 0) targetHp = 0;
            hpDecreaseSpd = (hp - targetHp) / 100;
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