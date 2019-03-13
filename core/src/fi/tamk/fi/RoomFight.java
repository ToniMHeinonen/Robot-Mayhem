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

// I comment at least on things that are unclear to me.
// I comment out lines of code that aren't functional yet.
public class RoomFight extends RoomParent {

    private Player player;
    private Enemy enemy;

    private Texture action;
    private Texture idle;

    private Animation<TextureRegion> actionMove;
    private Animation<TextureRegion> idleMove;

    private Animating anim;

    RoomFight(MainGame game) {
        super(game);

        player = new Player(game, anim, action, actionMove, idle, idleMove);
        //enemy = new Enemy(game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        createActionButton();
        batch.begin();
        //player.update();
        //enemy.update();
        batch.end();
    }

    public void createActionButton() {
        final TextButton buttonSettings = new TextButton("Action!", skin);
        buttonSettings.setWidth(300f);
        buttonSettings.setHeight(100f);
        buttonSettings.setPosition(game.pixelWidth /2 - buttonSettings.getWidth() /2,
                (game.pixelHeight/3) *2 - buttonSettings.getHeight() /2);
        stage.addActor(buttonSettings);

        buttonSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                //player.attack();
            }
        });
    }

    /*
    Now you could try to make a new class for Player and Enemy to extend. That class could have the
    variables and methods that are same for these classes, this would decrease duplication.
     */

    /*
    CREATE PLAYER
     */
    private class Player extends GameObject{
        private float X;
        private float Y;

        private Texture orange;
        private Texture green;

        private Animation<TextureRegion> orangemove;
        private Animation<TextureRegion> greenmove;
        boolean tempAnimation = false;

        private Animating anim;

        public Player(MainGame game, Animating anim, Texture action, Animation<TextureRegion> actionMove, Texture idle, Animation<TextureRegion> idleMove) {

            super(game, anim, action, actionMove, idle, idleMove);

            X = 100;
            Y = game.pixelHeight / 2;
            anim = new Animating();

            orange = game.getOrangeTexture();
            green = game.getGreenTexture();

            orangemove = anim.createAnimation(orange, 2, 1);
            greenmove = anim.createAnimation(green, 2, 1);
            anim.startAnimation(orangemove, 50);

            //update();
        }
    }

    /*
    CREATE ENEMY
     */
    private class Enemy extends GameObject{
        private float X;
        private float Y;
        private Animating anim;

        private Animation<TextureRegion> idleMove;
        private Animation<TextureRegion> actionMove;
        boolean tempAnimation = false;

        private Texture action;
        private Texture idle;

        Enemy(MainGame game) {
            super(game, enemy.anim, enemy.action, enemy.actionMove, enemy.idle, enemy.idleMove);

            X = game.pixelWidth - 200f;
            Y = game.pixelHeight/2;
            anim = new Animating();

            action = game.getRedTexture();
            idle = game.getYellowTexture();

            actionMove = anim.createAnimation(action, 2, 1);
            idleMove = anim.createAnimation(idle, 2,1);
            anim.startAnimation(idleMove, 50);
        }

        public void render(float delta) {
            game.render();
        }
    }
}
