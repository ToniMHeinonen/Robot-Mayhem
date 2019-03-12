package fi.tamk.fi;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// Uhhh I don't know how the f to do this yet.
public class GameObject extends Animating {

    protected MainGame game;
    private Texture orange;
    private Animation<TextureRegion> orangemove;

    private Texture green;
    private Animation<TextureRegion> greenmove;

    private SpriteBatch batch;

    public Animating anim;
    boolean tempAnimation = true;

    public int X;
    public int Y;

    GameObject(MainGame game) {

        super();
        batch = game.getBatch();
        orange = game.getOrangeTexture();
        //orangemove = anim.createAnimation(orange, 2, 1);
        //anim.startAnimation(orangemove, 50);
    }

    public void render(float delta) {
        game.render();
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
