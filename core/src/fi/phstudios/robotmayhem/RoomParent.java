package fi.phstudios.robotmayhem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.I18NBundle;

public class RoomParent implements Screen {

    protected SpriteBatch batch;
    protected Texture imgBG, imgTopBar, imgBottomBar, dialogArrow;
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
    protected Skin finalSkin;
    protected ProgressBar progressBar;
    protected ProgressBar.ProgressBarStyle progressBarStyle;
    protected BitmapFont fontSteps;
    private int transitionCounter = 20;
    private int dialogArrowTime = 60;
    private int dialogArrowCounter = dialogArrowTime;
    private boolean showDialogArrow = true;
    protected UtilDialog dialog;
    protected boolean clickedOpenSettings;

    protected Settings settings;

    /**
     * Retrieves correct values from main game instance.
     * @param game main game instance
     */
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
        finalSkin = game.getFinalSkin();
        stage.clear();

        dialog = game.getDialog();
        imgTopBar = files.imgTopBar;
        imgBottomBar = files.imgBottomBar;
        dialogArrow = files.dialogArrow;
    }

    /**
     * Renders all the frames of the room. Used for drawing transition colors and stage acting.
     * @param delta time
     */
    @Override
    public void render(float delta) {

        if (game.haveWeChangedTheRoom) {

            transitionColor();
            transitionUpdate();
        }

        if (!game.haveWeChangedTheRoom) {

            defaultColor();

            stage.act(Gdx.graphics.getDeltaTime());
        }
    }

    /**
     * Do this at the end of the render.
     */
    public void endOfRender() {
        drawDialogArrow();
    }

    /**
     * What color is used when transitioning from room to room.
     */
    public void transitionColor() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Handles select item. RoomParent needs to guide the method to correct room.
     * @param item selected item
     */
    public void selectItem(String item) {
        // This is need for this method to work correctly in RoomGame and RoomFight
    }

    /**
     * Handles what is the default color when not transitioning.
     */
    public void defaultColor() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    /**
     * Handles transitioning from room to room.
     */
    public void transitionUpdate() {
        transitionCounter--;

        if (transitionCounter <= 0) {
            game.haveWeChangedTheRoom = false;
        }
    }

    /**
     * Used for creating top right corner meny button.
     * @param room what room the method is called
     */
    public void createMenuButton(final String room) {
        ImageButton button = new ImageButton(finalSkin.getDrawable("button_settings"),
                finalSkin.getDrawable("button_settings_clicked"));
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

    /**
     * Draws the top and bottom bar textures.
     */
    public void drawTopAndBottomBar() {
        batch.draw(imgTopBar, 0,game.pixelHeight - imgTopBar.getHeight(),
                imgTopBar.getWidth(), imgTopBar.getHeight());
        batch.draw(imgBottomBar, 0,0,
                imgBottomBar.getWidth(), imgBottomBar.getHeight());
    }

    /**
     * Draws dialog arrow when needed.
     */
    public void drawDialogArrow() {
        int type = game.getDialogType();
        if (type != game.DIAL_STOP) {
            if (dialogArrowCounter > 0) dialogArrowCounter--;
            else {
                showDialogArrow = !showDialogArrow;
                dialogArrowCounter = dialogArrowTime;
            }

            if (showDialogArrow) {
                float x = game.pixelWidth / 2 - dialogArrow.getWidth() / 2;
                float y = game.pixelHeight / 2 - dialogArrow.getHeight() / 2;
                if (type == game.DIAL_TALL) {
                    x += 375f;
                    y += 110f;
                } else if (type == game.DIAL_SMALL) {
                    x += 375f;
                    y += 10f;
                } else if (type == game.DIAL_BOX) {
                    x += 350f;
                } else if (type == game.DIAL_PLAYER) {
                    x += 375f;
                    y += 110f;
                } else if (type == game.DIAL_SKILL) {
                    x += 420f;
                    y += 30f;
                }
                batch.begin();
                batch.draw(dialogArrow, x, y);
                batch.end();
            }
        } else {
            showDialogArrow = true;
            dialogArrowCounter = dialogArrowTime;
        }
    }

    /**
     * Handles what happens when room shows up.
     */
    @Override
    public void show() {

    }

    /**
     * Handles what happens when window is resized.
     * @param width current width
     * @param height current height
     */
    @Override
    public void resize(int width, int height) {

    }

    /**
     * Handles what happens when game is paused in background.
     */
    @Override
    public void pause() {

    }

    /**
     * Handles what happens when game returns from background.
     */
    @Override
    public void resume() {

    }

    /**
     * Handles what happens when room is changed. Disposes this current room.
     */
    @Override
    public void hide() {
        this.dispose();
    }

    /**
     * Handles what will be disposed.
     */
    @Override
    public void dispose() {

    }
}
