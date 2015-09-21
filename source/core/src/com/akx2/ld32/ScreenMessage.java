package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenMessage {

    final float MIN_DISPLAY = 1f;

    Texture black;

    boolean isShowing = false;
    boolean isButtonDown = false;
    String message = "";

    BitmapFont.TextBounds tBounds = new BitmapFont.TextBounds();

    public BitmapFont font;

    public float displayTimer = 0;

    public ScreenMessage ()
    {
        black = new Texture(Gdx.files.internal("images/dot.black.png"), true);
        font = new BitmapFont(Gdx.files.internal("fonts/HUD-64.fnt"), false);
        displayTimer = 0;
    }

    public void show(String message)
    {
        this.message = message;
        isShowing = true;
    }

    public void render (SpriteBatch batch)
    {
        if (isShowing)
        {
            batch.setColor(1, 1, 1, 0.35f);
            batch.draw(black, 0, 0, Settings.WIDTH, Settings.HEIGHT);

            batch.setColor(1, 1, 1, 0.75f);
            batch.draw(black, 0, (Settings.HEIGHT / 5) * 2, Settings.WIDTH, 200);

            tBounds = font.getBounds(message);
            font.draw(batch, message, (Settings.WIDTH / 2) - (tBounds.width / 2), (Settings.HEIGHT / 2) - (tBounds.height / 2) + 120);

            tBounds = font.getBounds("press space");
            font.draw(batch, "press space", (Settings.WIDTH / 2) - (tBounds.width / 2), (Settings.HEIGHT / 2) - (tBounds.height / 2) + 50);
        }
    }

    public void update (float delta)
    {
        displayTimer += delta;
        if (displayTimer> MIN_DISPLAY) {

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                isButtonDown = true;
            } else {
                if (isButtonDown) {
                    isShowing = false;
                    isButtonDown = false;
                }
            }
        }
    }

    public void dispose()
    {
        black.dispose();
        font.dispose();
    }
}
