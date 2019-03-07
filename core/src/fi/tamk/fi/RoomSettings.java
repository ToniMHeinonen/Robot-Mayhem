package fi.tamk.fi;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RoomSettings extends RoomParent {
    private boolean moveToFightRoom;

    RoomSettings(MainGame game) {
        super(game);
        createButtonFight();
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
                moveToFightRoom = true;
            }
        });
    }

    public void checkPresses() {
        if (moveToFightRoom) {
            RoomFight roomFight = new RoomFight(game);
            game.setScreen(roomFight);
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        checkPresses();
    }
}
