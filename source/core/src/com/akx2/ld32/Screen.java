package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Screen {
    public Screen()
    {
    }

    public abstract void init();

    public abstract void render(SpriteBatch batch);

    public abstract void update(float delta);

    public abstract void dispose();
}

