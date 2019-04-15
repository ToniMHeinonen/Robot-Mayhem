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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.I18NBundle;

public class RoomParent implements Screen {
    protected SpriteBatch batch;
    protected Texture imgBG, imgTopBar, imgBottomBar;
    protected I18NBundle localize;
    protected MainGame game;
    protected RoomParent thisRoom = this;
    protected Files files;
    protected Skills skills;
    protected Item items;
    protected Bosses bosses;
    protected OrthographicCamera camera;
    protected Stage stage;
    protected Skin skin;
    protected Skin testSkin;
    protected Skin finalSkin;
    protected ProgressBar progressBar;
    protected ProgressBar.ProgressBarStyle progressBarStyle;
    protected BitmapFont fontSteps;
    private int transitionCounter = 20;
    protected UtilDialog dialog;
    protected boolean clickedOpenSettings;

    protected Settings settings;

    RoomParent(MainGame game) {
        this.game = game;
        batch = game.getBatch();
        localize = game.getLocalize();
        skills = game.getSkills();
        items = game.getItems();
        bosses = game.getBosses();
        files = game.getFiles();
        camera = game.getCamera();
        stage = game.getStage();
        Gdx.input.setInputProcessor(stage);
        skin = game.getSkin();
        fontSteps = game.getFontSteps();
        progressBarStyle = game.getProgBarStyle();
        clickedOpenSettings = game.getClickedOpenSettings();
        testSkin = game.getTestSkin();
        finalSkin = game.getFinalSkin();
        stage.clear();

        dialog = game.getDialog();
        imgTopBar = files.imgTopBar;
        imgBottomBar = files.imgBottomBar;
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
        }
    }

    public void transitionColor() {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public void selectItem(String item) {
        // This is need for this method to work correctly in RoomGame and RoomFight
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

    public void createMenuButton(final String room) {
        ImageButton button = new ImageButton(testSkin.getDrawable("button_settings"),
                testSkin.getDrawable("button_settings_clicked"));
        button.setPosition(game.pixelWidth - button.getWidth()/2 - 190, game.pixelHeight - 120);
        button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                settings = new Settings(game, room, thisRoom);
            }
        });

        stage.addActor(button);
    }

    public void drawTopAndBottomBar() {
        batch.draw(imgTopBar, 0,game.pixelHeight - imgTopBar.getHeight(),
                imgTopBar.getWidth(), imgTopBar.getHeight());
        batch.draw(imgBottomBar, 0,0,
                imgTopBar.getWidth(), imgTopBar.getHeight());
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
        //System.out.println(clickedOpenSettings);
    }

    @Override
    public void dispose() {

    }
}
