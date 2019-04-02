package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RoomSettings extends RoomParent {
    private BitmapFont fontTest;
    private TextButton.TextButtonStyle styleTest;
    private UtilItem item;
    private boolean startItem = false;

    RoomSettings(MainGame game) {
        super(game);
        createNewFont();
        createButtonFight();
        createButtonGame();
        createButtonMusic();
        createButtonPlus();
        createButtonMinus();
        createButtonTestailua();
        createButtonItem();

        createButtonPowerUps();

        backgroundMusic.setVolume(game.getMusicVol());
        backgroundMusic.play();
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (!game.haveWeChangedTheRoom) {
            stage.draw();
        }
        if (startItem) {
            item.update();
        }
    }

    public void createNewFont() {
        fontTest = new BitmapFont(Gdx.files.internal("font_test.fnt"),
                Gdx.files.internal("font_test.png"),
                false);
        styleTest = new TextButton.TextButtonStyle();
        styleTest.font = fontTest;
        styleTest.up = skin.getDrawable("button");
        styleTest.down = skin.getDrawable("button-down");
    }

    public void createButtonFight() {
        final TextButton buttonFight = new TextButton("RoomFight", skin);
        buttonFight.setWidth(300f);
        buttonFight.setHeight(100f);
        buttonFight.setPosition(game.pixelWidth /2 - buttonFight.getWidth() /2,
                (game.pixelHeight/3) *2 - buttonFight.getHeight() /2);
        stage.addActor(buttonFight);

        buttonFight.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.switchToRoomFight();
            }
        });
    }

    public void createButtonGame() {
        final TextButton buttonGame = new TextButton("RoomGame", skin);
        buttonGame.setWidth(300f);
        buttonGame.setHeight(100f);
        buttonGame.setPosition(game.pixelWidth /2 - buttonGame.getWidth() /2,
                game.pixelHeight/3 - buttonGame.getHeight() /2);
        stage.addActor(buttonGame);

        buttonGame.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.switchToRoomGame();
            }
        });
    }

    public void createButtonMusic() {
        final ImageTextButton buttonMusic = new ImageTextButton("Music Volume:", skin);
        buttonMusic.setWidth(300f);
        buttonMusic.setHeight(100f);
        buttonMusic.setPosition(game.pixelWidth / 2 - buttonMusic.getWidth() / 2,
                game.pixelHeight/3 * 1.5f - buttonMusic.getHeight() / 2);
        stage.addActor(buttonMusic);
    }

    public void createButtonPlus() {
        final TextButton buttonPlus = new TextButton("+", skin);
        buttonPlus.setWidth(100f);
        buttonPlus.setHeight(100f);
        buttonPlus.setPosition(game.pixelWidth / 2 + buttonPlus.getWidth() * 3,
                game.pixelHeight/3 * 1.5f - buttonPlus.getHeight() / 2);
        stage.addActor(buttonPlus);

        buttonPlus.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                    game.setMusicVol(game.getMusicVol() + 0.2f);
                    backgroundMusic.setVolume(game.getMusicVol());
                }
        });
    }

    public void createButtonMinus() {
        final TextButton buttonMinus = new TextButton("-", skin);
        buttonMinus.setWidth(100f);
        buttonMinus.setHeight(100f);
        buttonMinus.setPosition(game.pixelWidth / 2 + buttonMinus.getWidth() * 2,
                game.pixelHeight/3 * 1.5f - buttonMinus.getHeight() / 2);
        stage.addActor(buttonMinus);

        buttonMinus.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                    game.setMusicVol(game.getMusicVol() - 0.2f);
                    backgroundMusic.setVolume(game.getMusicVol());
            }
        });
    }

    public void createButtonTestailua() {
        final TextButton buttonTestailua = new TextButton("RoomTestailua", styleTest);
        buttonTestailua.setWidth(300f);
        buttonTestailua.setHeight(100f);
        buttonTestailua.setPosition(game.pixelWidth /2 - buttonTestailua.getWidth() /2,
                game.pixelHeight/3 * 2.5f - buttonTestailua.getHeight() /2);
        stage.addActor(buttonTestailua);

        buttonTestailua.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.switchToRoomTestailua();
            }
        });
    }

    public void createButtonPowerUps() {

        final TextButton buttonPowerUps = new TextButton("PowerUps", skin);
        buttonPowerUps.setWidth(300f);
        buttonPowerUps.setHeight(100f);
        buttonPowerUps.setPosition(game.pixelWidth /1.5f,
                (game.pixelHeight/3) *2 - buttonPowerUps.getHeight() /2);

        stage.addActor(buttonPowerUps);

        buttonPowerUps.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.switchToPowerUps();
            }
        });
    }

    public void createButtonItem() {
        final TextButton buttonFight = new TextButton("Item", skin);
        buttonFight.setWidth(300f);
        buttonFight.setHeight(100f);
        buttonFight.setPosition(game.pixelWidth /2 - buttonFight.getWidth() *2,
                (game.pixelHeight/3) *2 - buttonFight.getHeight() /2);
        stage.addActor(buttonFight);

        buttonFight.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                item = new UtilItem(game);
                startItem = true;
            }
        });
    }

    @Override
    public void hide() {
        super.hide();
        game.saveSettings();
    }
}
