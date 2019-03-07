package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class RoomGame extends RoomParent {

    private Texture imgBG;
    private int bgPos;
    private float bgSpd;

    RoomGame(MainGame game) {
        super(game);

        //Wrapping enables looping
        imgBG = new Texture("bgPlaceholder.jpg");
        imgBG.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        batch.begin();
        controlBackground();
        batch.end();
    }

    public void controlBackground() {
        //USE THIS TO TEST THE MOVEMENT
        if( Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) bgSpd += 0.5f;

        //Limit max speed
        if (bgSpd > 15f) bgSpd = 15f;

        //Add friction to speed
        if (bgSpd > 0f) {
            //4 different values for different speeds
            if (bgSpd > 7.5f) bgSpd -= 0.01f;
            else if (bgSpd > 5f) bgSpd -= 0.008f;
            else if (bgSpd > 2.5f) bgSpd -= 0.007f;
            else bgSpd -= 0.005f;
        } else {
            bgSpd = 0f;
        }

		/*If bgPos + next speed addition goes over image width,
		  then reset counter, because of the int limit (2147483647)*/
        if (bgPos + bgSpd > imgBG.getWidth()) {
            int spdToInt = (int) bgSpd;
            int var = bgPos + spdToInt - imgBG.getWidth();
            bgPos = var;
        } else {
            bgPos += Math.ceil(bgSpd); //Ceil since Int cuts decimals
        }

        //Draw background, srcX handles image looping
        batch.draw(imgBG, 0,0, bgPos, 0, imgBG.getWidth(), imgBG.getHeight());
    }
}
