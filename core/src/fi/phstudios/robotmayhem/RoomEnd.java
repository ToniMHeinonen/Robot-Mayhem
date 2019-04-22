package fi.phstudios.robotmayhem;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RoomEnd extends RoomParent {
    private final SpriteBatch batch;
    private final Files files;
    private final Texture imgBg;

    private final Animating animPlayer = new Animating();
    private final Animating animRoombot = new Animating();
    private final Animating animRobber = new Animating();
    private final Animating animCopper = new Animating();
    private final Animating animCopier = new Animating();
    private final Animating animPC = new Animating();
    private final Animating animBaller = new Animating();
    private final Animating animFabio = new Animating();

    private final Animating[] animatings = new Animating[] {animPlayer, animRoombot, animRobber,
    animCopper, animCopier, animPC, animBaller, animFabio};

    private final Animation<TextureRegion> playerIdle, roombotIdle, robberIdle, copperIdle,
            copierIdle, pcIdle, ballerIdle, fabioIdle;

    private final Animation<TextureRegion>[] animations;


    private final int animSpeed = 8;

    RoomEnd(MainGame game) {
        super(game);
        batch = game.getBatch();
        files = game.getFiles();
        imgBg = files.imgBgBoss;

        playerIdle = files.animIdle;
        roombotIdle = files.a_roombotIdle;
        robberIdle = files.a_robberIdle;
        copperIdle = files.a_copperIdle;
        copierIdle = files.a_copierIdle;
        pcIdle = files.a_pcIdle;
        ballerIdle = files.a_ballerIdle;
        fabioIdle = files.a_fabioIdle;

        animations = new Animation[] {playerIdle, roombotIdle, robberIdle, copperIdle, copierIdle,
        pcIdle, ballerIdle, fabioIdle};

        for (int i = 0; i < animations.length; i++) {
            animatings[i].startAnimation(animations[i], animSpeed);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        batch.begin();
        drawTopAndBottomBar();
        batch.draw(imgBg, 0,0);
        animateAndDrawEveryone();
        batch.end();
    }

    private void animateAndDrawEveryone() {
        float space = 200f;
        float x;
        for (int i = 0; i < animations.length; i++) {
            if (i == 0) x = -100f;
            else x = 0f;
            animatings[i].animate();
            animatings[i].draw(batch, x + space*i, game.gridSize*2);
        }
    }
}
