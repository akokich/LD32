package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import javafx.application.Application;

public class MenuScreen extends Screen {
    final int maxMenuIndex = 3;
    final LD32 game;

    ScreenMessage message;

    Texture blackDot;
    Texture backGrad;
    Texture backGradSunset;
    Texture backNeedle;
    Texture backNeedleSunset;
    Texture backSkyline;
    Texture backSkylineSunset;

    BitmapFont font;
    BitmapFont fontLarge;
    BitmapFont fontHL;
    BitmapFont.TextBounds tBounds;

    Music menuMusic;

    boolean isSunset = false;
    int menuIndex = 0;
    boolean isPressingButton = false;
    boolean isViewingInstructions = false;

    public MenuScreen (LD32 game, boolean isSunset)
    {
        this.isSunset = isSunset;
        this.game = game;
        font = new BitmapFont(Gdx.files.internal("fonts/HUD-64.fnt"), false);
        fontLarge = new BitmapFont(Gdx.files.internal("fonts/HUD-92.fnt"), false);
        fontHL = new BitmapFont(Gdx.files.internal("fonts/HUD-64-HL.fnt"), false);
        blackDot = new Texture (Gdx.files.internal("images/dot.black.png"), true);
        message = new ScreenMessage();

        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/menu.mp3"));
    }
    @Override
    public void init() {
        backGrad = new Texture(Gdx.files.internal("images/menu-back-grad.png"), true);
        backGradSunset = new Texture(Gdx.files.internal("images/menu-back-grad-sunset.png"), true);

        backNeedle = new Texture(Gdx.files.internal("images/menu-needle.png"), true);
        backNeedleSunset = new Texture(Gdx.files.internal("images/menu-needle-sunset.png"), true);

        backSkyline = new Texture(Gdx.files.internal("images/menu-skyline-dark.png"), true);
        backSkylineSunset = new Texture(Gdx.files.internal("images/menu-skyline-sunset.png"), true);

        menuMusic.setVolume(0.5f);
        menuMusic.setLooping(true);

        if (Settings.MUSIC) {
            menuMusic.play();
        }

    }

    @Override
    public void render(SpriteBatch batch) {
        if (isSunset)
        {
            batch.draw(backGradSunset, 0, 0, Settings.WIDTH, Settings.HEIGHT);
            batch.draw(backSkylineSunset, 0, 0);
            batch.draw(backNeedleSunset, 300, 107);
        } else {
            batch.draw(backGrad, 0, 0, Settings.WIDTH, Settings.HEIGHT);
            batch.draw(backSkyline, 0, 0);
            batch.draw(backNeedle, 300, 107);
        }

        tBounds = fontLarge.getBounds("Rain, Rain, GO AWAY!");
        fontLarge.draw(batch, "Rain, Rain, GO AWAY!", (Settings.WIDTH / 2) - (tBounds.width / 2), Settings.HEIGHT - 20);

        if (!isViewingInstructions) {
            renderMenu(batch);
        }

        if (isSunset) {
            batch.setColor(1, 1, 1, 0.5f);
            batch.draw(blackDot, 0, 0, Settings.WIDTH, 140);
            batch.setColor(1, 1, 1, 1f);

            tBounds = font.getBounds("Created by Aaron & Athena Kokich");
            font.drawMultiLine(batch, "Created by Aaron & Athena Kokich", (Settings.WIDTH / 2) - (tBounds.width / 2), 140);
            tBounds = font.getBounds("Ludum Dare 32 Game Jam");
            font.drawMultiLine(batch, "Ludum Dare 32 Game Jam", (Settings.WIDTH / 2) - (tBounds.width / 2), 75);
        } else {
            batch.setColor(1, 1, 1, 0.5f);
            batch.draw(blackDot, 0, 0, Settings.WIDTH, 140);
            batch.setColor(1, 1, 1, 1f);

            tBounds = font.getBounds("Up / Down or W / S to navigate menu");
            font.drawMultiLine(batch, "Up / Down or W / S to navigate menu", (Settings.WIDTH / 2) - (tBounds.width / 2), 140);
            tBounds = font.getBounds("space to select");
            font.drawMultiLine(batch, "space to select", (Settings.WIDTH / 2) - (tBounds.width / 2), 75);
        }

        message.render(batch);
    }

    public void renderMenu (SpriteBatch batch)
    {
        batch.setColor(1, 1, 1, 0.5f);
        batch.draw(blackDot, (Settings.WIDTH / 2) - 150, (Settings.HEIGHT / 2) - 200, 300, 400);

        batch.setColor(1, 1, 1, 1f);

        tBounds = font.getBounds("Play!");
        if (menuIndex == 0) {
            fontHL.draw(batch, "Play!", (Settings.WIDTH / 2) - (tBounds.width / 2), 550);
        } else {
            font.draw(batch, "Play!", (Settings.WIDTH / 2) - (tBounds.width / 2), 550);
        }

        tBounds = font.getBounds("Sound " + (Settings.SOUNDS ? "ON" : "OFF"));
        if (menuIndex == 1) {
            fontHL.draw(batch, "Sound " + (Settings.SOUNDS ? "ON" : "OFF"), (Settings.WIDTH / 2) - (tBounds.width / 2), 450);
        } else {
            font.draw(batch, "Sound " + (Settings.SOUNDS ? "ON" : "OFF"), (Settings.WIDTH / 2) - (tBounds.width / 2), 450);
        }

        tBounds = font.getBounds("Music " + (Settings.MUSIC ? "ON" : "OFF"));
        if (menuIndex == 2) {
            fontHL.draw(batch, "Music " + (Settings.MUSIC ? "ON" : "OFF"), (Settings.WIDTH / 2) - (tBounds.width / 2), 350);
        } else {
            font.draw(batch, "Music " + (Settings.MUSIC ? "ON" : "OFF"), (Settings.WIDTH / 2) - (tBounds.width / 2), 350);
        }

        tBounds = font.getBounds("Exit");
        if (menuIndex == 3) {
            fontHL.draw(batch, "Exit", (Settings.WIDTH / 2) - (tBounds.width / 2), 250);
        } else {
            font.draw(batch, "Exit", (Settings.WIDTH / 2) - (tBounds.width / 2), 250);
        }
    }

    @Override
    public void update(float delta) {
        if (!message.isShowing) {
            if (isViewingInstructions) {
                game.screens.loadScreen(new PlayScreen(game));
            }

            if ((Gdx.input.isKeyPressed(Input.Keys.W) || (Gdx.input.isKeyPressed(Input.Keys.UP)))) {
                if (!isPressingButton) {
                    isPressingButton = true;
                    if (menuIndex == 0) {
                        menuIndex = maxMenuIndex;
                    } else {
                        menuIndex--;
                    }
                }
            } else if ((Gdx.input.isKeyPressed(Input.Keys.S) || (Gdx.input.isKeyPressed(Input.Keys.DOWN)))) {
                if (!isPressingButton) {
                    isPressingButton = true;
                    if (menuIndex == maxMenuIndex) {
                        menuIndex = 0;
                    } else {
                        menuIndex++;
                    }
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                if (!isPressingButton) {
                    isPressingButton = true;
                    switch (menuIndex) {
                        case 0:
                            message.show("Use Arrows or W,A,S,D to fly, LMB or Space to shoot");

                            isViewingInstructions = true;
                            break;
                        case 1:
                            Settings.SOUNDS = (Settings.SOUNDS) ? false : true;
                            break;
                        case 2:
                            Settings.MUSIC = (Settings.MUSIC) ? false : true;
                            if (!Settings.MUSIC)
                            {
                                if (menuMusic.isPlaying()) {
                                    menuMusic.stop();
                                }
                            } else {
                                if (!menuMusic.isPlaying()) {
                                    menuMusic.play();
                                }
                            }
                            break;
                        case 3:
                            Gdx.app.exit();
                            break;
                    }
                }

            } else {
                isPressingButton = false;
            }
        } else {
            message.update(delta);
        }
    }

    @Override
    public void dispose() {
        blackDot.dispose();
        backGrad.dispose();
        backGradSunset.dispose();

        backNeedle.dispose();
        backNeedleSunset.dispose();

        backSkyline.dispose();
        backSkylineSunset.dispose();
        font.dispose();
        fontLarge.dispose();
        fontHL.dispose();

        message.dispose();

        if (menuMusic.isPlaying())
        {
            menuMusic.stop();
        }
        menuMusic.dispose();
    }

}
