package fi.phstudios.robotmayhem;

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

    /**
     * Create animations with this to be used later.
     * @param image sprite sheet
     * @param cols how many columns
     * @param rows how many rows
     * @return animation
     */
    public Animation<TextureRegion> createAnimation(Texture image, int cols, int rows) {
        this.frameCols = cols;
        this.frameRows = rows;

        tmp = TextureRegion.split(image, image.getWidth() / frameCols,
                image.getHeight() / frameRows);
        frames = toTextureArray(tmp);
        animation = new Animation(1 / 60f, frames);

        return animation;
    }

    /**
     * Use this if you need to reverse the animation.
     * @param image sprite sheet
     * @param cols how many columns
     * @param rows how many rows
     * @return animation
     */
    public Animation<TextureRegion> createAnimationReverse(Texture image, int cols, int rows) {
        this.frameCols = cols;
        this.frameRows = rows;

        tmp = TextureRegion.split(image, image.getWidth() / frameCols,
                image.getHeight() / frameRows);
        frames = toTextureArrayReverse(tmp);
        animation = new Animation(1 / 60f, frames);

        return animation;
    }

    /**
     * When you need to change to another animation, use this.
     * @param animation selected animation
     * @param speed frame speed
     */
    public void startAnimation(Animation<TextureRegion> animation, int speed) {
        stateTime = 0.0f;
        this.animation = animation;
        this.frameSpeed = speed;
        currentFrame = animation.getKeyFrame(stateTime, true);
        width = currentFrame.getRegionWidth();
        height = currentFrame.getRegionHeight();
    }

    /**
     * Animates animation.
     */
    public void animate() {
        stateTime += Gdx.graphics.getDeltaTime() / frameSpeed;
        currentFrame = animation.getKeyFrame(stateTime, true);
    }

    /**
     * Changes 2D sprite sheet to 1D. Don't modify this, it works perfectly.
     * @param tr sprite sheet 2D array
     * @return 1D texture region array
     */
    private TextureRegion[] toTextureArray(TextureRegion[][] tr) {
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

    /**
     * Changes 2D sprite sheet to 1D reversed. Don't modify this, it works perfectly.
     * @param tr sprite sheet 2D array
     * @return 1D texture region array
     */
    private TextureRegion[] toTextureArrayReverse(TextureRegion[][] tr) {
        int fc = this.frameCols;
        int fr = this.frameRows;
        TextureRegion [] frames = new TextureRegion[fc * fr];

        int index = 0;
        for (int i = fr-1; i >= 0; i--) {
            for (int j = fc-1; j >= 0; j--) {
                frames[index++] = tr[i][j];
            }
        }

        return frames;
    }

    /**
     * Draws the animation.
     * @param batch selected batch
     * @param x position x value
     * @param y position y value
     */
    public void draw(SpriteBatch batch, float x, float y) {
        batch.draw(currentFrame, x, y, width, height);
    }

    /**
     * Sets the peed of animation.
     * @param frameSpeed selected speed
     */
    public void setFrameSpeed(int frameSpeed) {
        this.frameSpeed = frameSpeed;
    }

    /**
     * Gets current state time.
     * @return current state time
     */
    public float getStateTime() {
        return stateTime;
    }

    /**
     * Sets state time.
     * @param stateTime wanted state time
     */
    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
        currentFrame = animation.getKeyFrame(stateTime, false);
    }

    /**
     * Gets current animation which is running.
     * @return current animation
     */
    public Animation<TextureRegion> getAnimation() {
        return animation;
    }
}
