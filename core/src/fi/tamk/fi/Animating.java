package fi.tamk.fi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animating {
    protected TextureRegion[][] tmp;
    protected TextureRegion[] frames;
    protected Animation<TextureRegion> animation;
    protected TextureRegion currentFrame;
    protected int frameCols;
    protected int frameRows;
    protected int frameSpeed;
    protected float stateTime = 0.0f;

    protected float width;
    protected float height;
    protected float X;
    protected float Y;

    //Create animations with this when at the start
    public Animation<TextureRegion> createAnimation(Texture image) {
        stateTime = 0.0f;

        tmp = TextureRegion.split(image, image.getWidth() / frameCols,
                image.getHeight() / frameRows);
        frames = toTextureArray(tmp);
        animation = new Animation(1 / 60f, frames);

        return animation;
    }

    //When you need to change to another animation, use this
    public void startAnimation(Animation<TextureRegion> animation) {
        stateTime = 0.0f;
        currentFrame = animation.getKeyFrame(stateTime, true);
        width = currentFrame.getRegionWidth();
        height = currentFrame.getRegionHeight();
    }

    //Don't modify this, it works perfectly
    public TextureRegion[] toTextureArray(TextureRegion[][] tr) {
        int fc = this.frameCols;
        int fr = this.frameRows;
        TextureRegion [] frames = new TextureRegion[fc * fr];

        int index = 0;
        for (int i = 0; i < fr; i++) {
            for (int j = 0; j < fc; j++) {
                frames[index++] = tr[i][j];
            }
        }

        return frames;
    }

    //Draw the animation
    public void draw(SpriteBatch batch) {
        batch.draw(currentFrame, X, Y, width, height);
    }
}
