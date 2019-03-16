package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.Collections;

public class RoomFight extends RoomParent {

    private Texture imgBg;
    private Player player;
    private Enemy enemy;
    private String[] btnTexts = new String[] {"Attack", "Defend", "Escape", "Item", "Hack"};
    private int btnCounter;

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
                    if (player.readyToAct) player.doAction(i);
                }
            });
        }
    }

    /*
    CREATE PLAYER
     */
    private class Player {
        private float X;
        private float Y;
        private Animating anim;

        private Animation<TextureRegion> curAnimation, idle, attack, defend, escape, item, hack;

        boolean tempAnimation = false;
        boolean readyToAct = true;

        private ArrayList<Animation<TextureRegion>> animList;
        private Integer[] speeds;

        Player() {
            X = 100;
            Y = 200f;
            anim = new Animating();

            idle = anim.createAnimation(game.getPlayerIdle(), 3, 1);
            attack = anim.createAnimation(game.getPlayerAttack(), 3, 1);
            defend = anim.createAnimation(game.getPlayerDefend(), 3, 1);
            escape = anim.createAnimation(game.getPlayerEscape(), 3, 1);
            item = anim.createAnimation(game.getPlayerItem(), 3, 1);
            hack = anim.createAnimation(game.getPlayerHack(), 3, 1);

            animList = new ArrayList<Animation<TextureRegion>>();
            Collections.addAll(animList, attack, defend, escape, item, hack);
            speeds = new Integer[] {30, 30, 30, 30, 30};

            anim.startAnimation(idle, 30);
        }

        public void update() {
            anim.animate();

            if (tempAnimation) {
                if (curAnimation.isAnimationFinished(anim.getStateTime())) {
                    anim.startAnimation(idle, 30);
                    tempAnimation = false;
                    enemy.counterAttack();
                }
            }

            anim.draw(batch, X, Y);
        }

        public void doAction(int index) {
            tempAnimation = true;
            readyToAct = false;
            curAnimation = animList.get(index);
            anim.startAnimation(curAnimation, speeds[index]);
        }
    }

    /*
    CREATE ENEMY
     */
    private class Enemy {
        private float X;
        private float Y;
        private Animating anim;

        private Animation<TextureRegion> yellowmove;
        private Animation<TextureRegion> redmove;
        boolean tempAnimation = false;

        private Texture yellow;
        private Texture red;

        Enemy() {

            X = game.pixelWidth - 300;
            Y = 200f;
            anim = new Animating();

            red = game.getRedTexture();
            yellow = game.getYellowTexture();

            redmove = anim.createAnimation(red, 2, 1);
            yellowmove = anim.createAnimation(yellow, 2, 1);
            anim.startAnimation(yellowmove, 50);
        }

        public void update() {
            anim.animate();

            if (tempAnimation) {
                if (redmove.isAnimationFinished(anim.getStateTime())) {
                    anim.startAnimation(yellowmove, 50);
                    tempAnimation = false;
                    player.readyToAct = true;
                }
            }

            anim.draw(batch, X, Y);
        }

        public void counterAttack() {
            tempAnimation = true;
            anim.startAnimation(redmove, 50);
        }
    }
}