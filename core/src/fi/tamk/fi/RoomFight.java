package fi.tamk.fi;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import javax.xml.soap.Text;

import static com.badlogic.gdx.Input.Keys.X;
import static com.badlogic.gdx.Input.Keys.Y;
import static fi.tamk.fi.MainGame.pixelHeight;
import static fi.tamk.fi.MainGame.pixelWidth;

import static javax.swing.text.html.HTML.Attribute.COLS;
import static javax.swing.text.html.HTML.Attribute.ROWS;

public class RoomFight extends RoomParent {

    private Texture imgBg;
    private Player player;
    private Enemy enemy;

    RoomFight(MainGame game) {
        super(game);
        imgBg = game.getImgBgBoss();
        createActionButton();

        player = new Player();
        enemy = new Enemy();

        createMenuButton();
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        batch.draw(imgBg, 0,0, imgBg.getWidth(), imgBg.getHeight());
        drawTopBar();
        player.update();
        enemy.update();
        batch.end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void createActionButton() {
        final TextButton buttonSettings = new TextButton("Action!", skin);
        buttonSettings.setWidth(300f);
        buttonSettings.setHeight(100f);
        buttonSettings.setPosition(game.pixelWidth / 2 - buttonSettings.getWidth() / 2,
                (game.pixelHeight / 3) * 2 - buttonSettings.getHeight() / 2);
        stage.addActor(buttonSettings);

        buttonSettings.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                player.attack();
            }
        });
    }

    /*
    CREATE PLAYER
     */
    private class Player {
        private float X;
        private float Y;
        private Animating anim;

        private Texture orange;
        private Texture green;

        private Animation<TextureRegion> orangemove;
        private Animation<TextureRegion> greenmove;
        boolean tempAnimation = false;

        Player() {
            X = 100;
            Y = 200f;
            anim = new Animating();

            orange = game.getOrangeTexture();
            green = game.getGreenTexture();

            orangemove = anim.createAnimation(orange, 2, 1);
            greenmove = anim.createAnimation(green, 2, 1);
            anim.startAnimation(greenmove, 50);
        }

        public void update() {
            anim.animate();

            if (tempAnimation) {
                if (orangemove.isAnimationFinished(anim.getStateTime())) {
                    anim.startAnimation(greenmove, 50);
                    tempAnimation = false;
                    enemy.counterAttack();
                }
            }

            anim.draw(batch, X, Y);
        }

        public void attack() {
            tempAnimation = true;
            anim.startAnimation(orangemove, 50);
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