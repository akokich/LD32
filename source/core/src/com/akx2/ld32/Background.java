package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.Set;

public class Background {
    public final int MAX_SCROLL = -1250;

    public boolean isMaxScroll = false;

    Texture backstrip;
    Texture[] backgrounds;

    Vector2[] positions;

    float[] parallaxSpeed;

    int tI = 0;

    public Background ()
    {
        backstrip = new Texture (Gdx.files.internal("images/back-strip.png"), true);
        backgrounds = new Texture[5];
        backgrounds[0] = new Texture(Gdx.files.internal("images/back-mountain.png"), true);
        backgrounds[1] = new Texture(Gdx.files.internal("images/back-fog3.png"), true);
        backgrounds[2] = new Texture(Gdx.files.internal("images/back-fog2.png"), true);
        backgrounds[3] = new Texture(Gdx.files.internal("images/back-fog1.png"), true);
        backgrounds[4] = new Texture(Gdx.files.internal("images/back-ground.png"), true);

        parallaxSpeed = new float[5];
        parallaxSpeed[0] = 15;
        parallaxSpeed[1] = 20;
        parallaxSpeed[2] = 22;
        parallaxSpeed[3] = 24;
        parallaxSpeed[4] = 30;

        positions = new Vector2[5];
        positions[0] = new Vector2(0, 100);
        positions[1] = new Vector2(0, 100);
        positions[2] = new Vector2(0, 100);
        positions[3] = new Vector2(0, 100);
        positions[4] = new Vector2(0, 100);
    }

    public void reset ()
    {
        positions[0].set(0, 100);
        positions[1].set(0, 100);
        positions[2].set(0, 100);
        positions[3].set(0, 100);
        positions[4].set(0, 100);
        isMaxScroll = false;
    }

    public void update(float delta)
    {
        if (positions[4].x > MAX_SCROLL) {
            for (tI = 0; tI < backgrounds.length; tI++) {
                positions[tI].add(-parallaxSpeed[tI] * delta, 0);
            }
        } else {
            isMaxScroll = true;
        }
    }

    public void render (SpriteBatch batch)
    {
        batch.draw(backstrip, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        for (tI = 0; tI<backgrounds.length; tI++) {
            batch.draw(backgrounds[tI], positions[tI].x, positions[tI].y);
        }
    }

    public void dispose()
    {
        backstrip.dispose();
        for (int i=0; i<backgrounds.length;i++)
        {
            backgrounds[i].dispose();
        }
    }
}
