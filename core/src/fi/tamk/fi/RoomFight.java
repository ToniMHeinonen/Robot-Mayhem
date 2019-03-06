package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import static fi.tamk.fi.MainGame.pixelHeight;
import static fi.tamk.fi.MainGame.pixelWidth;

import static javax.swing.text.html.HTML.Attribute.COLS;
import static javax.swing.text.html.HTML.Attribute.ROWS;

// I comment at least on things that are unclear to me.
// I comment out lines of code that aren't functional yet.
public class RoomFight extends RoomParent {

    // Not sure what I should put in this constructor.
    RoomFight(MainGame game) {

        super(game);

        examplesheet = new Texture("exampleanimation.png");

        TextureRegion[][] tmp = TextureRegion.split(

                examplesheet,
                examplesheet.getWidth() / COLS,
                examplesheet.getHeight() / ROWS);

        TextureRegion [] frames = transfromTo1D(tmp);
        exampleAnimation = new Animation<TextureRegion>(1/10f, frames);
    }

    private TextureRegion [] transfromTo1D(TextureRegion[][] tmp) {

        TextureRegion [] exampleFrames = new TextureRegion[COLS * ROWS];
        int index = 0;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

                exampleFrames[index++] = tmp[i][j];
            }
        }

        return exampleFrames;
    }

    int COLS = 2;
    int ROWS = 1;

    Animation<TextureRegion> exampleAnimation;
    private Sprite example;
    private Texture examplesheet;

    TextureRegion currentFrame;
    float stateTime = 0.0f;

    @Override
    public void render(float delta) {

        super.render(delta);
        stateTime += Gdx.graphics.getDeltaTime();

        currentFrame = exampleAnimation.getKeyFrame(stateTime, true);

        batch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        Texture badlogic = new Texture("badlogic.jpg");
        batch.draw(badlogic, 0, 0, pixelWidth, pixelHeight);
        // x: example.getX() & example.getY() caused a nullpoint interaction exception
        // x: 0 & y: 0 is a bad fix, I know : D
        batch.draw(currentFrame, 0, 0);

        //update();

        batch.end();
    }
}
