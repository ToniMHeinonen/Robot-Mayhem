package fi.phstudios.robotmayhem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class PowerUps extends RoomParent {

    SpriteBatch batch;

    int pupA;
    int pupB;
    int pupC;

    // Pups as in PowerUPS.
    Array<Integer> pups;

    boolean canItBeClickedA = true;
    boolean canItBeClickedB = true;
    boolean canItBeClickedC = true;

    static Texture testBox = new Texture(Gdx.files.internal("box.png"));

    public static Rectangle testRectangle;

    PowerUps(MainGame game) {
        super(game);
        create();
        createButtonPowerUps();
    }

    // A terribly long method, just horrible to go trough. Haven't made a better way yet tho.
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

                if (canItBeClickedA == true) {
                    buttonPowerUps.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            canItBeClickedB = false;
                            canItBeClickedC = false;
                            createButtonPup();
                        }
                    });
                }

                TextButton buttonPowerUpsB = new TextButton(pupB + "", skin);
                buttonPowerUpsB.setWidth(400f);
                buttonPowerUpsB.setHeight(100f);
                buttonPowerUpsB.setPosition(900,
                        200);

                stage.addActor(buttonPowerUpsB);

                if (canItBeClickedB == true) {
                    buttonPowerUpsB.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            canItBeClickedA = false;
                            canItBeClickedC = false;
                            createButtonPupB();
                        }
                    });
                }

                TextButton buttonPowerUpsC = new TextButton(pupC + "", skin);
                buttonPowerUpsC.setWidth(400f);
                buttonPowerUpsC.setHeight(100f);
                buttonPowerUpsC.setPosition(1300,
                        200);

                stage.addActor(buttonPowerUpsC);

                if (canItBeClickedC == true) {
                    buttonPowerUpsC.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            canItBeClickedB = false;
                            canItBeClickedA = false;
                            createButtonPupC();
                        }
                    });
                }
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

                if (canItBeClickedA == true) {
                    buttonPowerUps.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            canItBeClickedB = false;
                            canItBeClickedC = false;
                            createButtonPup();
                        }
                    });
                }

                TextButton buttonPowerUpsB = new TextButton(pupB + "", skin);
                buttonPowerUpsB.setWidth(400f);
                buttonPowerUpsB.setHeight(100f);
                buttonPowerUpsB.setPosition(900,
                        200);

                stage.addActor(buttonPowerUpsB);

                if (canItBeClickedB == true) {
                    buttonPowerUpsB.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            canItBeClickedA = false;
                            canItBeClickedC = false;
                            createButtonPupB();
                        }
                    });
                }

                TextButton buttonPowerUpsC = new TextButton(pupC + "", skin);
                buttonPowerUpsC.setWidth(400f);
                buttonPowerUpsC.setHeight(100f);
                buttonPowerUpsC.setPosition(1300,
                        200);

                stage.addActor(buttonPowerUpsC);

                if (canItBeClickedC == true) {
                    buttonPowerUpsC.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            canItBeClickedB = false;
                            canItBeClickedA = false;
                            createButtonPupC();
                        }
                    });
                }
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

                if (canItBeClickedA == true) {
                    buttonPowerUps.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            canItBeClickedB = false;
                            canItBeClickedC = false;
                            createButtonPup();
                        }
                    });
                }

                TextButton buttonPowerUpsB = new TextButton(pupB + "", skin);
                buttonPowerUpsB.setWidth(400f);
                buttonPowerUpsB.setHeight(100f);
                buttonPowerUpsB.setPosition(900,
                        200);

                stage.addActor(buttonPowerUpsB);

                if (canItBeClickedB == true) {
                    buttonPowerUpsB.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            canItBeClickedA = false;
                            canItBeClickedC = false;
                            createButtonPupB();
                        }
                    });
                }

                TextButton buttonPowerUpsC = new TextButton(pupC + "", skin);
                buttonPowerUpsC.setWidth(400f);
                buttonPowerUpsC.setHeight(100f);
                buttonPowerUpsC.setPosition(1300,
                        200);

                stage.addActor(buttonPowerUpsC);

                if (canItBeClickedC == true) {
                    buttonPowerUpsC.addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {

                            canItBeClickedB = false;
                            canItBeClickedA = false;
                            createButtonPupC();
                        }
                    });
                }
            }
        });
    }

    public void createButtonPup() {

        TextButton buttonPowerUps = new TextButton(pupA + "", skin);
        buttonPowerUps.setWidth(400f);
        buttonPowerUps.setHeight(100f);
        buttonPowerUps.setPosition(500,
                200);

        stage.addActor(buttonPowerUps);

        buttonPowerUps.addListener(new ClickListener() {

            public void clicked(InputEvent event, float x, float y) {

                TextButton buttonPowerUps = new TextButton("Selected!", skin);
                buttonPowerUps.setWidth(400f);
                buttonPowerUps.setHeight(100f);
                buttonPowerUps.setPosition(500,
                        200);

                stage.addActor(buttonPowerUps);
                stage.clear();
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

        float bx = 500;
        float by = 200;

        testRectangle = new Rectangle(bx, by,
                testBox.getHeight(), testBox.getWidth());

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

    public float getWidth() {

        return testBox.getWidth() + 1200;
    }

    public static float getHeight() {

        return testBox.getHeight() + 1200;
    }

    public static float getX() {

        return testRectangle.x;
    }

    public float getY() {

        return testRectangle.y;
    }

    public void render (float delta) {

        super.render(delta);

        if (!game.haveWeChangedTheRoom) {

            batch.setProjectionMatrix(camera.combined);

            batch.begin();

            batch.draw(testBox, testRectangle.x - 50, testRectangle.y - 50,
                    testRectangle.getWidth() + 1100, testRectangle.getHeight() + 200);

            batch.end();

            stage.draw();
        }
    }

    //public static

    public void dispose () {
        batch.dispose();
    }
}
