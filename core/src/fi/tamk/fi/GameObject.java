package fi.tamk.fi;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// Uhhh I don't know how the f to do this yet.
public class GameObject extends Animating {

    protected MainGame game;
    protected Texture action;
    protected Animation<TextureRegion> actionMove;

    protected Texture idle;
    protected Animation<TextureRegion> idleMove;

    private SpriteBatch batch;

    public Animating anim;
    boolean tempAnimation = true;

    public int X;
    public int Y;

    public GameObject(MainGame game, Animating anim, Texture action, Animation<TextureRegion> actionMove, Texture idle, Animation<TextureRegion> idleMove) {

        super();
        batch = game.getBatch();
        action = game.getOrangeTexture();
        actionMove = anim.createAnimation(action, 2, 1);
        anim.startAnimation(actionMove, 50);
    }

    /*public void counterAttack() {
        tempAnimation = true;
        anim.startAnimation(redmove, 50);
    }*/

    /*public void update() {
        //anim.animate();

        if (tempAnimation) {
            if (orangemove.isAnimationFinished(anim.getStateTime())) {
                anim.startAnimation(greenmove, 50);
                tempAnimation = false;
            }
        }

        anim.draw(batch, X, Y);
    }*/
}

    /* public void update() {
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
        }*/

    /* public void update() {
        anim.animate();

        if (tempAnimation) {
            if (redmove.isAnimationFinished(anim.getStateTime())) {
                anim.startAnimation(yellowmove, 50);
                tempAnimation = false;
            }
        }

        anim.draw(batch, X, Y);
    } */
