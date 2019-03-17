package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.Collections;

// Enums gives simple constants, which decrease the change for coding mistakes
enum State {
    AWAITING,
    ACTION,
    ENEMY,
    HACK
}

public class RoomFight extends RoomParent {

    private Texture imgBg;
    private Player player;
    private Enemy enemy;
    private String[] btnTexts = new String[] {"Attack", "Defend", "Escape", "Item", "Hack"};
    private int btnCounter;
    private State state = State.AWAITING;

    RoomFight(MainGame game) {
        super(game);
        imgBg = game.getImgBgBoss();
        createButtons();

        player = new Player();
        enemy = new Enemy();
        createMenuButton();
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
            batch.end();

            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    private void createButtons() {
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

    /*
    CREATE PARENT FIGHTER
     */
    private class Fighters {
        protected float X;
        protected float Y;
        protected int hp;
        protected Animating anim = new Animating();

        protected boolean tempAnimation = false;

        protected int idleSpd;
        protected Animation<TextureRegion> curAnimation, idle;
        protected ArrayList<Animation<TextureRegion>> animList;
        protected Integer[] speeds;

        public void updateStart() {
            anim.animate();
        }

        public void updateEnd() {
            anim.draw(batch, X, Y);
        }

        public void startIdle() {
            anim.startAnimation(idle, 30);
            tempAnimation = false;
        }
    }

    /*
    CREATE PLAYER
     */
    private class Player extends Fighters {

        private Animation<TextureRegion> attack, defend, escape, item, hack;

        Player() {
            X = 100f;
            Y = 200f;
            hp = 10;
            idleSpd = 30;

            idle = anim.createAnimation(game.getPlayerIdle(), 3, 1);
            attack = anim.createAnimation(game.getPlayerAttack(), 3, 1);
            defend = anim.createAnimation(game.getPlayerDefend(), 3, 1);
            escape = anim.createAnimation(game.getPlayerEscape(), 3, 1);
            item = anim.createAnimation(game.getPlayerItem(), 3, 1);
            hack = anim.createAnimation(game.getPlayerHack(), 3, 1);

            animList = new ArrayList<Animation<TextureRegion>>();
            Collections.addAll(animList, attack, defend, escape, item, hack);
            speeds = new Integer[] {30, 30, 30, 30, 30};

            anim.startAnimation(idle, idleSpd);
        }

        public void update() {
            updateStart();

            if (state == State.ACTION) {
                // If temporary animation currently on, wait for it to finish,
                // else give turn to enemy
                if (tempAnimation) {
                    if (curAnimation.isAnimationFinished(anim.getStateTime())) {
                        startIdle();
                        state = State.ENEMY;
                    }
                } else {
                    state = State.ENEMY;
                }
            } else if (state == State.AWAITING) {
                if (anim.getAnimation() != idle) startIdle();
            }

            updateEnd();
        }

        public void doAction(int index) {
            curAnimation = animList.get(index);
            //If defend, then it's not temporary
            if (curAnimation != defend) {
                tempAnimation = true;
            }
            anim.startAnimation(curAnimation, speeds[index]);
        }
    }

    /*
    CREATE ENEMY
     */
    private class Enemy extends Fighters {

        private Animation<TextureRegion> attack1, attack2, attack3;

        private int actionDelay = 30;
        private int actionTimer = actionDelay;

        Enemy() {

            X = game.pixelWidth - 100f - game.getEnemyIdle().getWidth()/3;
            Y = 200f;
            hp = 5;
            idleSpd = 30;

            idle = anim.createAnimation(game.getEnemyIdle(), 3, 1);
            attack1 = anim.createAnimation(game.getEnemyAttack1(), 3, 1);
            attack2 = anim.createAnimation(game.getEnemyAttack2(), 3, 1);
            attack3 = anim.createAnimation(game.getEnemyAttack3(), 3, 1);

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
                    state = State.AWAITING;
                }
            }

            updateEnd();
        }

        public void attack() {
            if (state == State.ENEMY) {
                // Wait for timer to go down, then select action
                if (actionTimer > 0) {
                    actionTimer--;
                } else if (!tempAnimation) {
                    tempAnimation = true;
                    int randomAction = MathUtils.random(0, animList.size()-1);
                    curAnimation = animList.get(randomAction);
                    anim.startAnimation(curAnimation, speeds[randomAction]);
                }
            }
        }
    }
}