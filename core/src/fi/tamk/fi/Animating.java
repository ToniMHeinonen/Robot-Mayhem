package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animating {
    private TextureRegion[][] tmp;
    private TextureRegion[] frames;
    private Animation<TextureRegion> animation;
    private TextureRegion currentFrame;
    private int frameCols, frameRows, frameSpeed;
    private float stateTime = 0.0f;

    private float width, height;

    //Create animations with this when at the start
    public Animation<TextureRegion> createAnimation(Texture image, int cols, int rows) {
        this.frameCols = cols;
        this.frameRows = rows;

        tmp = TextureRegion.split(image, image.getWidth() / frameCols,
                image.getHeight() / frameRows);
        frames = toTextureArray(tmp);
        animation = new Animation(1 / 60f, frames);

        return animation;
    }

    //When you need to change to another animation, use this
    public void startAnimation(Animation<TextureRegion> animation, int speed) {
        stateTime = 0.0f;
        this.animation = animation;
        this.frameSpeed = speed;
        currentFrame = animation.getKeyFrame(stateTime, true);
        width = currentFrame.getRegionWidth();
        height = currentFrame.getRegionHeight();
    }

    public void animate() {
        stateTime += Gdx.graphics.getDeltaTime() / frameSpeed;
        currentFrame = animation.getKeyFrame(stateTime, true);
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
    public void draw(SpriteBatch batch, float x, float y) {
        batch.draw(currentFrame, x, y, width, height);
    }

    public int getFrameSpeed() {
        return frameSpeed;
    }

    public void setFrameSpeed(int frameSpeed) {
        this.frameSpeed = frameSpeed;
    }

    public float getStateTime() {
        return stateTime;
    }

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }
}
