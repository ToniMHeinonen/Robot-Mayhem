package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.I18NBundle;

public class RoomParent implements Screen {
    protected SpriteBatch batch;
    protected Texture imgBG;
    protected Texture imgTopBar;
    protected I18NBundle myBundle;
    protected MainGame game;
    protected OrthographicCamera camera;
    protected Stage stage;
    protected Skin skin;
    protected Music backgroundMusic;
    protected ProgressBar progressBar;
    protected Texture progBarEnemy;
    private boolean haveWeChangedTheRoom;

    protected TextButton button; //Temporary solution

    RoomParent(MainGame game) {
        this.batch = game.getBatch();
        this.myBundle = game.getMyBundle();
        this.game = game;
        this.camera = game.getCamera();
        this.stage = game.getStage();
        this.skin = game.getSkin();
        Gdx.input.setInputProcessor(this.stage);
        this.backgroundMusic = game.getBackgroundMusic();
        progBarEnemy = game.getProgBarEnemy();
        stage.clear();

        imgTopBar = game.getImgTopBar();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        batch.setProjectionMatrix(camera.combined);

        // Will not change color yet.
        if (game.haveWeChangedTheRoom) {

            Gdx.gl.glClearColor(1, 1, 1, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            Thread thread = new Thread();
            thread.start();

            System.out.println("Thread");

            try {
                Thread.sleep(1);
            } catch(Exception e) { }

            haveWeChangedTheRoom = false;

            /*if (!haveWeChangedTheRoom) {

                Gdx.gl.glClearColor(0, 0, 1, 1);
                Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            }*/

        } else {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void drawTopBar() {
        batch.draw(imgTopBar, 0,game.pixelHeight - imgTopBar.getHeight(),
                imgTopBar.getWidth(), imgTopBar.getHeight());
    }

    public void createMenuButton() {
        button = new TextButton("", game.getStyle());
        button.setPosition(game.pixelWidth - button.getWidth()/2 - 100, game.pixelHeight - 120);
        button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("my app", "Pressed"); //** Usually used to start Game, etc. **//

                return true;

            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.log("my app", "Rggggggeleased");
                game.switchToRoomSettings();
            }
        });

        stage.addActor(button);
    }

    public void createProgressBar() {
        progressBar = new ProgressBar(0, 20, 1, false, game.getProgBarStyle());
        progressBar.setWidth(400);
        progressBar.setHeight(140);
        progressBar.setBounds(game.pixelWidth / 2 - progressBar.getWidth() / 2,
                game.pixelHeight - progressBar.getHeight(),
                progressBar.getWidth(),
                progressBar.getHeight());
        stage.addActor(progressBar);
    }

    public void drawProgBarEnemy() {
        batch.draw(progBarEnemy, progressBar.getX() + progressBar.getWidth() - 10,
                progressBar.getY() + progressBar.getHeight() / 3.5f,
                progBarEnemy.getWidth(),
                progBarEnemy.getHeight());
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {

    }
}
