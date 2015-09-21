package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.Set;

public class HUD {
    Texture blackDot;

    TextureRegion playerRegion;

    public BitmapFont font;

    public HUD(Texture player)
    {
        blackDot = new Texture(Gdx.files.internal("images/dot.black.png"), true);

        font = new BitmapFont(Gdx.files.internal("fonts/HUD-64.fnt"), false);

        playerRegion = new TextureRegion(player);
    }

    public void render (SpriteBatch batch, int lives, int tillNext, int wave, int score)
    {
        batch.draw(blackDot, 0, 0, Settings.WIDTH, 100);
        //font_HUD.draw(batch, )
        font.draw(batch, "x", 90, 90);
        font.draw(batch, "" + lives, 130, 80);

        batch.draw(playerRegion, 80, 7, 0, 0,  256, 64, 0.35f, 0.45f, 90);

        if (tillNext == -1) {
            font.draw(batch, "MAX Upgraded", 230, 80);
        } else {
            font.draw(batch, "Next Upgrade " + tillNext, 230, 80);
        }

        font.draw(batch, "Wave " + wave, 650, 80);

        font.draw(batch, "SCORE " + score, 900, 80);

    }

    public void update (float delta)
    {

    }

    public void dispose()
    {
        blackDot.dispose();
        font.dispose();
    }
}
