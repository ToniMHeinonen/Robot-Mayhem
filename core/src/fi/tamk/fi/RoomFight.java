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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class RoomFight extends RoomParent {

    // Enums give simple constants, which decreases the chance for coding mistakes
    enum State {
        START_TURN,
        AWAITING,
        ACTION,
        ENEMY_HIT,
        ENEMY_WAITING,
        ENEMY_ACTION,
        PLAYER_HIT,
        HACK,
        DEAD,
        ESCAPE
    }

    private Texture imgBg, escapeBg;
    private Player player;
    private Enemy enemy;
    private String[] btnTexts = new String[] {"Attack", "Defend", "Item"};
    private int btnCounter; // Used for button classes to get the correct value
    private int deathTimer = 240;
    private State state = State.START_TURN;
    private boolean escapePopup;
    private ShaderProgram shFlashWhite;

    RoomFight(MainGame game) {
        super(game);
        imgBg = game.getImgBgBoss();
        escapeBg = game.getEscapeBg();
        createButtons();
        createShader();

        player = new Player();
        enemy = new Enemy();
        backgroundMusic.pause();
        bossMusic.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (!game.haveWeChangedTheRoom) {

            batch.begin();
            batch.draw(imgBg, 0,0, imgBg.getWidth(), imgBg.getHeight());
            drawTopBar();
            drawHP();
            player.update();
            enemy.update();
            escaping();
            batch.end();

            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
            death();
        }
    }

    public void createShader() {
        String vertexShader =
                "attribute vec4 a_position; \n" +
                        "attribute vec4 a_color;\n" +
                        "attribute vec2 a_texCoord0; \n" +

                        "uniform mat4 u_projTrans; \n" +

                        "varying vec4 v_color; \n" +
                        "varying vec2 v_texCoords; \n" +

                        "void main() { \n" +
                        "v_color = a_color; \n" +
                        "v_texCoords = a_texCoord0; \n" +
                        "gl_Position = u_projTrans * a_position; \n" +
                        "};";

        String fragmentShader = "#ifdef GL_ES\n" +
                "precision mediump float;\n" +
                "#endif\n" + "varying vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "uniform sampler2D u_texture;\n" +
                "uniform float grayscale;\n" +
                "void main()\n" +
                "{\n" +
                "vec4 texColor = texture2D(u_texture, v_texCoords);\n" +
                "float gray = dot(texColor.rgb, vec3(5, 5, 5));\n" +
                "texColor.rgb = mix(vec3(gray), texColor.rgb, grayscale);\n" +
                " gl_FragColor = v_color * texColor;\n" +
                "}";

        shFlashWhite = new ShaderProgram(vertexShader, fragmentShader);
    }

    @Override
    public void hide() {
        backgroundMusic.play();
        bossMusic.stop();
    }

    private void createButtons() {
        createMenuButton();
        createEscapeButton();
        createActionButtons();
    }

    private void createActionButtons() {
        float space = 150f;
        for (int i = 0; i < btnTexts.length; i++) {
            btnCounter = i;
            final TextButton btn = new TextButton(btnTexts[i], skin);
            btn.setWidth(300);
            btn.setHeight(100);
            btn.setPosition(game.pixelWidth / 2 - btn.getWidth() / 2,
                    game.pixelHeight - 300f - space*i);
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

    private void death() {
        if (state == State.DEAD) {
            deathTimer--;

            if (deathTimer <= 0) {
                game.switchToRoomGame();
            }
        }
    }

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
        fontSteps.draw(batch, "Player " + String.valueOf(player.getHp()),
                400, game.pixelHeight - 50);
        fontSteps.draw(batch, "Enemy " + String.valueOf(enemy.getHp()),
                1000, game.pixelHeight - 50);
    }

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

    /*
    CREATE PARENT FIGHTER
     */
    private class Fighters {
        protected float X;
        protected float Y;
        protected double hp;
        protected double damage;
        protected double dmgAmount;
        protected Animating anim = new Animating();

        protected boolean flashWhite;
        protected int flashTime = 15;
        protected int whiteTimer = flashTime;

        protected boolean tempAnimation = false;

        protected int idleSpd, hackSpd, deathSpd, escapeSpd;
        protected Animation<TextureRegion> curAnimation, idle, hack;
        protected ArrayList<Animation<TextureRegion>> animList;
        protected Integer[] speeds;

        // Do this at the start of update method
        public void updateStart() {
            controlFlashing();
            anim.animate();
        }

        // Do this at the end of update method
        public void updateEnd() {
            if (flashWhite) batch.setShader(shFlashWhite);
            anim.draw(batch, X, Y);
            batch.setShader(null);
        }

        public void startIdle() {
            anim.startAnimation(idle, idleSpd);
            tempAnimation = false;
        }

        public void startHack() {
            anim.startAnimation(hack, hackSpd);
            tempAnimation = false;
        }

        public void controlFlashing() {
            if (flashWhite) {
                if (whiteTimer > 0) {
                    whiteTimer--;
                } else {
                    flashWhite = false;
                    whiteTimer = flashTime;
                }
            }
        }

        public double getHp() {
            return hp;
        }
    }

    /*
    CREATE PLAYER
     */
    private class Player extends Fighters {

        private Animation<TextureRegion> attack, defend, escape, item, death;
        private HashMap<String,Object> mapAttack, mapDefend;

        private boolean causeDamage;
        private HashMap<String,Integer> cooldowns;

        Player() {
            X = 100f;
            Y = 200f;
            hp = 10;
            damage = 2.5;
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
            defend = (Animation<TextureRegion>) mapDefend.get(Skills.getAnimation());
            //defend = anim.createAnimation((Texture) mapDefend.get(Skills.getSheet()), 3, 1);
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
            checkHp();

            if (state == State.START_TURN) {
                decreaseCooldowns();
                state = State.AWAITING;
            } else if (state == State.ACTION) {
                // If temporary animation currently on, wait for it to finish,
                // else give turn to enemy
                if (tempAnimation) {
                    if (curAnimation.isAnimationFinished(anim.getStateTime())) {
                        if (causeDamage) {
                            enemy.takeHit(dmgAmount);
                            causeDamage = false;
                        }
                        startIdle();
                        state = State.ENEMY_WAITING;
                    }
                } else {
                    state = State.ENEMY_WAITING;
                }
            } else if (state == State.AWAITING) {
                if (anim.getAnimation() != idle) startIdle();
            } else if (state == State.HACK) {
                if (anim.getAnimation() != hack) startHack();
            } else if (state == State.DEAD) {
                if (anim.getAnimation() != death) anim.startAnimation(death, deathSpd);
            } else if (state == State.ESCAPE) {
                runAway();
            }

            updateEnd();
        }

        /*
        Iterate through the actions to find the selected action.
         */
        public void doAction(int index) {
            curAnimation = animList.get(index);

            //If defend, then it's not temporary
            if (curAnimation == attack){
                causeDamage = true;
                tempAnimation = true;
                /*
                Explanation: Object can't be cast to Double, so the object has to be first cast to
                String and then get the value of String. This is so stupid...
                 */
                dmgAmount = Double.valueOf(mapAttack.get(Skills.getDamage()).toString());
                anim.startAnimation(curAnimation, speeds[index]);
            } else if (curAnimation == defend) {
                if (cooldowns.get("defend") > 0) {
                    state = State.AWAITING;
                } else {
                    anim.startAnimation(curAnimation, speeds[index]);
                    cooldowns.put("defend", (Integer) mapDefend.get(Skills.getCooldown()));
                }
            } else if (curAnimation == item) {
                tempAnimation = true;
                anim.startAnimation(curAnimation, speeds[index]);
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
            else hp -= damage; flashWhite = true;

            if (hp < 0) hp = 0;
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

    /*
    CREATE ENEMY
     */
    private class Enemy extends Fighters {

        private Animation<TextureRegion> skill1, skill2, skill3;
        private HashMap<String,Object> mapBoss;

        private int actionDelay = 30;
        private int actionTimer = actionDelay;
        private double[] damages;

        Enemy() {
            retrieveBoss();

            X = game.pixelWidth - 100f - idle.getKeyFrame(0f).getRegionWidth();
            Y = 200f;
            hp = 5;

            animList = new ArrayList<Animation<TextureRegion>>();
            Collections.addAll(animList, skill1, skill2, skill3);

            anim.startAnimation(idle, idleSpd);
        }

        public void update() {
            updateStart();

            checkHp();
            attack();

            if (tempAnimation) {
                if (curAnimation.isAnimationFinished(anim.getStateTime())) {
                    startIdle();
                    actionTimer = actionDelay;
                    player.takeHit(dmgAmount);
                    state = State.START_TURN;
                }
            }

            updateEnd();
        }

        private void retrieveBoss() {
            mapBoss = Bosses.getBoss("roombot");

            // Retrieve animations
            idle = (Animation<TextureRegion>) mapBoss.get(Bosses.getIdle());
            skill1 = (Animation<TextureRegion>) mapBoss.get(Bosses.getSkill() + "1");
            skill2 = (Animation<TextureRegion>) mapBoss.get(Bosses.getSkill() + "2");
            skill3 = (Animation<TextureRegion>) mapBoss.get(Bosses.getSkill() + "3");
            hack = (Animation<TextureRegion>) mapBoss.get(Bosses.getHack());

            // Retrieve damages
            double dmg1 = Double.valueOf(mapBoss.get(Bosses.getDamage() + "1").toString());
            double dmg2 = Double.valueOf(mapBoss.get(Bosses.getDamage() + "2").toString());
            double dmg3 = Double.valueOf(mapBoss.get(Bosses.getDamage() + "3").toString());

            damages = new double[] {dmg1, dmg2, dmg3};

            // Retrieve animation speeds
            String spd = Bosses.getSpeed();
            idleSpd = (Integer) mapBoss.get(spd + Bosses.getIdle());
            int skill1Spd = (Integer) mapBoss.get(spd + Bosses.getSkill() + "1");
            int skill2Spd = (Integer) mapBoss.get(spd + Bosses.getSkill() + "2");
            int skill3Spd = (Integer) mapBoss.get(spd + Bosses.getSkill() + "3");
            hackSpd = (Integer) mapBoss.get(spd + Bosses.getHack());

            speeds = new Integer[] {skill1Spd, skill2Spd, skill3Spd};
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
                    tempAnimation = true;
                    int random = MathUtils.random(0, animList.size() - 1);
                    curAnimation = animList.get(random);
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

        public void takeHit(double damage) {
            hp -= damage;
            flashWhite = true;
            if (hp < 0) hp = 0;
        }
    }
}