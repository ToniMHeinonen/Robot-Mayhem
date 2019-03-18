package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.Collections;

// Enums give simple constants, which decreases the chance for coding mistakes
enum State {
    AWAITING,
    ACTION,
    ENEMY_WAITING,
    ENEMY_ACTION,
    HACK,
    DEAD,
    ESCAPE
}

public class RoomFight extends RoomParent {

    private Texture imgBg, escapeBg;
    private Player player;
    private Enemy enemy;
    private String[] btnTexts = new String[] {"Attack", "Defend", "Item"};
    private int btnCounter;
    private int deathTimer = 240;
    private State state = State.AWAITING;
    private boolean escapePopup;

    RoomFight(MainGame game) {
        super(game);
        imgBg = game.getImgBgBoss();
        escapeBg = game.getEscapeBg();
        createButtons();

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
            player.update();
            enemy.update();
            escaping();
            batch.end();

            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
            death();
        }
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

    public void createEscapeButton() {
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

        protected boolean tempAnimation = false;

        protected int idleSpd, hackSpd, deathSpd, escapeSpd;
        protected Animation<TextureRegion> curAnimation, idle, hack;
        protected ArrayList<Animation<TextureRegion>> animList;
        protected Integer[] speeds;

        public void updateStart() {
            anim.animate();
        }

        public void updateEnd() {
            anim.draw(batch, X, Y);
        }

        public void startIdle() {
            anim.startAnimation(idle, idleSpd);
            tempAnimation = false;
        }

        public void startHack() {
            anim.startAnimation(hack, hackSpd);
            tempAnimation = false;
        }

        public void takeHit(double damage) {
            hp -= damage;
        }
    }

    /*
    CREATE PLAYER
     */
    private class Player extends Fighters {

        private Animation<TextureRegion> attack, defend, escape, item, death;

        private boolean causeDamage;

        Player() {
            X = 100f;
            Y = 200f;
            hp = 10;
            damage = 2.5;
            idleSpd = 30;
            hackSpd = 30;
            deathSpd = 30;
            escapeSpd = 10;

            idle = anim.createAnimation(game.getPlayerIdle(), 3, 1);
            attack = anim.createAnimation(game.getPlayerAttack(), 3, 1);
            defend = anim.createAnimation(game.getPlayerDefend(), 3, 1);
            escape = anim.createAnimation(game.getPlayerEscape(), 3, 1);
            item = anim.createAnimation(game.getPlayerItem(), 3, 1);
            hack = anim.createAnimation(game.getPlayerHack(), 3, 1);
            death = anim.createAnimation(game.getPlayerDeath(), 3, 1);

            animList = new ArrayList<Animation<TextureRegion>>();
            Collections.addAll(animList, attack, defend, item);
            speeds = new Integer[] {30, 30, 30, 30, 30};

            anim.startAnimation(idle, idleSpd);
        }

        public void update() {
            updateStart();

            checkHp();

            if (state == State.ACTION) {
                // If temporary animation currently on, wait for it to finish,
                // else give turn to enemy
                if (tempAnimation) {
                    if (curAnimation.isAnimationFinished(anim.getStateTime())) {
                        if (causeDamage) {
                            enemy.takeHit(damage);
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

        public void doAction(int index) {
            curAnimation = animList.get(index);
            tempAnimation = true;

            //If defend, then it's not temporary
            if (curAnimation == attack){
                causeDamage = true;
                dmgAmount = damage;
            } else if (curAnimation == defend) {
                tempAnimation = false;
            }

            anim.startAnimation(curAnimation, speeds[index]);
        }

        private void checkHp() {
            if (hp <= 0) {
                state = State.DEAD;
            }
        }

        private void runAway() {
            if (anim.getAnimation() != escape) anim.startAnimation(escape, escapeSpd);

            if (X > - 400) {
                X -= Gdx.graphics.getDeltaTime() * 150f;
            } else {
                game.switchToRoomGame();
            }
        }
    }

    /*
    CREATE ENEMY
     */
    private class Enemy extends Fighters {

        private Animation<TextureRegion> attack1, attack2, attack3;

        private int actionDelay = 30;
        private int actionTimer = actionDelay;

        private double[] dmgMultiplier;

        Enemy() {

            X = game.pixelWidth - 100f - game.getEnemyIdle().getWidth()/3;
            Y = 200f;
            hp = 5;
            damage = 1;
            idleSpd = 30;
            hackSpd = 30;

            dmgMultiplier = new double[] {1, 1.5, 2};

            idle = anim.createAnimation(game.getEnemyIdle(), 3, 1);
            attack1 = anim.createAnimation(game.getEnemyAttack1(), 3, 1);
            attack2 = anim.createAnimation(game.getEnemyAttack2(), 3, 1);
            attack3 = anim.createAnimation(game.getEnemyAttack3(), 3, 1);
            hack = anim.createAnimation(game.getEnemyHack(), 3, 1);

            animList = new ArrayList<Animation<TextureRegion>>();
            Collections.addAll(animList, attack1, attack2, attack3);
            speeds = new Integer[] {30, 30, 30,};

            anim.startAnimation(idle, idleSpd);
        }

        public void update() {
            updateStart();

            attack();

            if (tempAnimation) {
                if (curAnimation.isAnimationFinished(anim.getStateTime())) {
                    startIdle();
                    actionTimer = actionDelay;
                    player.takeHit(dmgAmount);
                    state = State.AWAITING;
                }
            }

            updateEnd();
        }

        public void attack() {
            if (state == State.ENEMY_WAITING) {
                // Wait for timer to go down, then select action
                if (actionTimer > 0) {
                    actionTimer--;
                } else {
                    if (hp > 0) {
                        state = State.ENEMY_ACTION;
                        tempAnimation = true;
                        int random = MathUtils.random(0, animList.size() - 1);
                        curAnimation = animList.get(random);
                        dmgAmount = damage * dmgMultiplier[random];
                        anim.startAnimation(curAnimation, speeds[random]);
                    } else {
                        state = State.HACK;
                        anim.startAnimation(hack, hackSpd);
                    }
                }
            }
        }
    }
}