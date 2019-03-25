package fi.tamk.fi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import java.awt.Font;
import java.awt.Rectangle;

public class PowerUps extends RoomParent {

    SpriteBatch batch;

    int pupA;
    int pupB;
    int pupC;

    // Pups as in PowerUPS.
    Array<Integer> pups;

    int counter = 0;

    PowerUps(MainGame game) {
        super(game);
        create();
        createButtonPowerUps();
    }

    public void createButtonPowerUps() {

        TextButton buttonPowerUps = new TextButton("PowerUp A", skin);
        buttonPowerUps.setWidth(400f);
        buttonPowerUps.setHeight(100f);
        buttonPowerUps.setPosition(500,
                200);

        stage.addActor(buttonPowerUps);

        TextButton buttonPowerUpsB = new TextButton("PowerUps B", skin);
        buttonPowerUpsB.setWidth(400f);
        buttonPowerUpsB.setHeight(100f);
        buttonPowerUpsB.setPosition(900,
                200);

        stage.addActor(buttonPowerUpsB);

        TextButton buttonPowerUpsC = new TextButton("PowerUps C", skin);
        buttonPowerUpsC.setWidth(400f);
        buttonPowerUpsC.setHeight(100f);
        buttonPowerUpsC.setPosition(1300,
                200);

        stage.addActor(buttonPowerUpsC);

        buttonPowerUps.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                TextButton buttonPowerUps = new TextButton(pupA + "", skin);
                buttonPowerUps.setWidth(400f);
                buttonPowerUps.setHeight(100f);
                buttonPowerUps.setPosition(500,
                        200);

                stage.addActor(buttonPowerUps);

                TextButton buttonPowerUpsB = new TextButton(pupB + "", skin);
                buttonPowerUpsB.setWidth(400f);
                buttonPowerUpsB.setHeight(100f);
                buttonPowerUpsB.setPosition(900,
                        200);

                stage.addActor(buttonPowerUpsB);

                TextButton buttonPowerUpsC = new TextButton(pupC + "", skin);
                buttonPowerUpsC.setWidth(400f);
                buttonPowerUpsC.setHeight(100f);
                buttonPowerUpsC.setPosition(1300,
                        200);

                stage.addActor(buttonPowerUpsC);

                counter++;
            }
        });

        buttonPowerUpsB.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                TextButton buttonPowerUps = new TextButton(pupA + "", skin);
                buttonPowerUps.setWidth(400f);
                buttonPowerUps.setHeight(100f);
                buttonPowerUps.setPosition(500,
                        200);

                stage.addActor(buttonPowerUps);

                TextButton buttonPowerUpsB = new TextButton(pupB + "", skin);
                buttonPowerUpsB.setWidth(400f);
                buttonPowerUpsB.setHeight(100f);
                buttonPowerUpsB.setPosition(900,
                        200);

                stage.addActor(buttonPowerUpsB);

                TextButton buttonPowerUpsC = new TextButton(pupC + "", skin);
                buttonPowerUpsC.setWidth(400f);
                buttonPowerUpsC.setHeight(100f);
                buttonPowerUpsC.setPosition(1300,
                        200);

                stage.addActor(buttonPowerUpsC);

                counter++;
            }
        });

        buttonPowerUpsC.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                final TextButton buttonPowerUps = new TextButton(pupA + "", skin);
                buttonPowerUps.setWidth(400f);
                buttonPowerUps.setHeight(100f);
                buttonPowerUps.setPosition(500,
                        200);

                stage.addActor(buttonPowerUps);

                TextButton buttonPowerUpsB = new TextButton(pupB + "", skin);
                buttonPowerUpsB.setWidth(400f);
                buttonPowerUpsB.setHeight(100f);
                buttonPowerUpsB.setPosition(900,
                        200);

                stage.addActor(buttonPowerUpsB);

                buttonPowerUpsB.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        createButtonPupB();
                    }
                });

                TextButton buttonPowerUpsC = new TextButton(pupC + "", skin);
                buttonPowerUpsC.setWidth(400f);
                buttonPowerUpsC.setHeight(100f);
                buttonPowerUpsC.setPosition(1300,
                        200);

                stage.addActor(buttonPowerUpsC);

                buttonPowerUpsC.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {

                        createButtonPupC();
                    }
                });

                counter++;

                System.out.println(counter);
            }
        });
    }

    public void createButtonPupB() {

        TextButton buttonPowerUpsB = new TextButton(pupB + "", skin);
        buttonPowerUpsB.setWidth(400f);
        buttonPowerUpsB.setHeight(100f);
        buttonPowerUpsB.setPosition(900,
                200);

        stage.addActor(buttonPowerUpsB);

        buttonPowerUpsB.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {

                TextButton buttonPowerUpsB = new TextButton("Selected!", skin);
                buttonPowerUpsB.setWidth(400f);
                buttonPowerUpsB.setHeight(100f);
                buttonPowerUpsB.setPosition(900,
                        200);

                stage.addActor(buttonPowerUpsB);
            }
        });
    }

    public void createButtonPupC() {

        TextButton buttonPowerUpsC = new TextButton(pupC + "", skin);
        buttonPowerUpsC.setWidth(400f);
        buttonPowerUpsC.setHeight(100f);
        buttonPowerUpsC.setPosition(1300,
                200);

        stage.addActor(buttonPowerUpsC);

            buttonPowerUpsC.addListener(new ClickListener() {

                public void clicked(InputEvent event, float x, float y) {

                    TextButton buttonPowerUpsC = new TextButton("Selected!", skin);
                    buttonPowerUpsC.setWidth(400f);
                    buttonPowerUpsC.setHeight(100f);
                    buttonPowerUpsC.setPosition(1300,
                            200);

                    stage.addActor(buttonPowerUpsC);
                }
        });
    }

    public void create() {

        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1920, 1080);

        pups = new Array<Integer>();

        for (int i = 0; i < 1; i++) {

            pupA = MathUtils.random(1, 3);
            pups.add(pupA);
        }

        for (int i = 0; i < 1; i++) {

            pupB = MathUtils.random(4, 6);
            pups.add(pupB);
        }

        for (int i = 0; i < 1; i++) {

            pupC = MathUtils.random(7, 9);
            pups.add(pupC);
            System.out.println(pups);
        }
    }

    public void render (float delta) {

        super.render(delta);

        if (!game.haveWeChangedTheRoom) {

            batch.setProjectionMatrix(camera.combined);

            batch.begin();


            /*if () {

                System.out.println("It works!");
            }*/


            batch.end();
        }
    }

    public void dispose () {
        batch.dispose();
    }
}
