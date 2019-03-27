package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.I18NBundle;

public class RoomParent implements Screen {
    protected SpriteBatch batch;
    protected Texture imgBG, imgTopBar, imgBottomBar;
    protected I18NBundle myBundle;
    protected MainGame game;
    protected OrthographicCamera camera;
    protected Stage stage;
    protected Skin skin;
    protected Music backgroundMusic;
    protected Music bossMusic;
    protected ProgressBar progressBar;
    protected ProgressBar.ProgressBarStyle progressBarStyle;
    protected BitmapFont fontSteps;
    private int transitionCounter = 20;
    protected UtilDialog dialog;
    protected FloatArray hackPosX;
    protected FloatArray hackPosY;
    protected int hackShieldAmount;
    protected boolean hackFirstTry;
    protected int tier1HackShieldAmount;
    protected int tier2HackShieldAmount;
    protected int tier3HackShieldAmount;

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
        this.bossMusic = game.getBossMusic();
        this.fontSteps = game.getFontSteps();
        this.progressBarStyle = game.getProgBarStyle();
        this.hackPosX = game.getHackPosX();
        this.hackPosY = game.getHackPosY();
        this.hackShieldAmount = game.getHackShieldAmount();
        this.hackFirstTry = game.getHackFirstTry();
        this.tier1HackShieldAmount = game.getTier1HackShieldAmount();
        this.tier2HackShieldAmount = game.getTier2HackShieldAmount();
        this.tier3HackShieldAmount = game.getTier3HackShieldAmount();
        stage.clear();

        dialog = game.getDialog();
        imgTopBar = game.getImgTopBar();
        imgBottomBar = game.getImgBottomBar();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        batch.setProjectionMatrix(camera.combined);

        if (game.haveWeChangedTheRoom) {

            transitionColor();
            transitionUpdate();
        }

        if (!game.haveWeChangedTheRoom) {

            defaultColor();

            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
    }

    public void transitionColor() {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void defaultColor() {

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void transitionUpdate() {

        transitionCounter--;

        if (transitionCounter <= 0) {

            game.haveWeChangedTheRoom = false;
        }
    }



    public void drawTopAndBottomBar() {
        batch.draw(imgTopBar, 0,game.pixelHeight - imgTopBar.getHeight(),
                imgTopBar.getWidth(), imgTopBar.getHeight());
        batch.draw(imgBottomBar, 0,0,
                imgTopBar.getWidth(), imgTopBar.getHeight());
    }

    public void createMenuButton() {
        button = new TextButton("", game.getStyle());
        button.setPosition(game.pixelWidth - button.getWidth()/2 - 100, game.pixelHeight - 120);
        button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                game.switchToRoomSettings();
            }
        });

        stage.addActor(button);
    }

    public void createProgressBar() {
        progressBar = new ProgressBar(0, 20, 1, false, game.getProgBarStyle());
        progressBar.setWidth(progressBarStyle.background.getMinWidth());
        progressBar.setHeight(progressBarStyle.background.getMinHeight());
        progressBarStyle.background.setLeftWidth(22f);
        progressBarStyle.background.setRightWidth(25f);
        progressBar.setPosition(game.pixelWidth / 2 - progressBar.getWidth() / 2,
                game.pixelHeight - progressBar.getHeight());
        stage.addActor(progressBar);
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
