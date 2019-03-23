package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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

    //Dialog
    protected TextureAtlas testButtonAtlas;
    protected Skin testSkin;
    protected Label.LabelStyle labelStyle;
    protected Window.WindowStyle windowStyle;
    protected com.badlogic.gdx.graphics.Color fontColor;

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
        stage.clear();

        imgTopBar = game.getImgTopBar();
        imgBottomBar = game.getImgBottomBar();

        //Dialog
        testButtonAtlas = game.getTestButtonAtlas();
        testSkin = game.getTestSkin();
        labelStyle = game.getLabelStyle();
        windowStyle = game.getWindowStyle();
        fontColor = game.getFontColor();
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

    public void createDialog(String text, float x, float y) {
        Label label = new Label(text, labelStyle);
        label.setWrap(true);
        final Dialog dialog = new Dialog("", windowStyle);
        dialog.getContentTable().add(label).prefWidth(400);
        dialog.setPosition(x, y);
        // dialog.getBackground().getMinHeight()
        // dialog.getBackground().getMinWidth()
        dialog.setSize(label.getWidth() + 50,label.getHeight()*4f);
        stage.addActor(dialog);
        dialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                dialog.remove();
            }
        });
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
