package fi.tamk.fi;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RoomSettings extends RoomParent {

    RoomSettings(MainGame game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        createButtonFight();
        createButtonGame();
        createButtonMusic();
        createButtonPlus();
        createButtonMinus();
        createButtonTestailua();
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
                RoomFight roomFight = new RoomFight(game);
                game.setScreen(roomFight);
            }
        });
    }

    public void createButtonGame() {
        final TextButton buttonFight = new TextButton("RoomGame", skin);
        buttonFight.setWidth(300f);
        buttonFight.setHeight(100f);
        buttonFight.setPosition(game.pixelWidth /2 - buttonFight.getWidth() /2,
                game.pixelHeight/3 - buttonFight.getHeight() /2);
        stage.addActor(buttonFight);

        buttonFight.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                RoomGame room = new RoomGame(game);
                game.setScreen(room);
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
                if (backgroundMusic.getVolume() < 1.0f) {
                    backgroundMusic.setVolume(backgroundMusic.getVolume() + 0.2f);
                    prefs.flush();
                }
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
                if (backgroundMusic.getVolume() > 0) {
                    backgroundMusic.setVolume(backgroundMusic.getVolume() - 0.2f);
                    prefs.putFloat("musicVolume", backgroundMusic.getVolume());
                    prefs.flush();
                }
            }
        });
    }

    public void createButtonTestailua() {
        final TextButton buttonGlobal = new TextButton("RoomTestailua", skin);
        buttonGlobal.setWidth(300f);
        buttonGlobal.setHeight(100f);
        buttonGlobal.setPosition(game.pixelWidth /2 - buttonGlobal.getWidth() /2,
                game.pixelHeight/3 * 2.5f - buttonGlobal.getHeight() /2);
        stage.addActor(buttonGlobal);

        buttonGlobal.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                RoomTestailua room = new RoomTestailua(game);
                game.setScreen(room);
            }
        });
    }
}
