package fi.tamk.fi;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RoomTestailua extends RoomParent {

    RoomTestailua(MainGame game) {
        super(game);
    }

    public void playMusic() {
        backgroundMusic.setVolume(game.getMusicVol());
        backgroundMusic.play();
    }

    public void createButtonSettings() {
        final TextButton buttonSettings = new TextButton("RoomSettings", skin);
        buttonSettings.setWidth(300f);
        buttonSettings.setHeight(100f);
        buttonSettings.setPosition(game.pixelWidth /2 - buttonSettings.getWidth() /2,
                (game.pixelHeight/3) *2 - buttonSettings.getHeight() /2);
        stage.addActor(buttonSettings);

        buttonSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                RoomSettings roomSettings = new RoomSettings(game);
                game.setScreen(roomSettings);
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        createButtonSettings();
        playMusic();
    }
}
