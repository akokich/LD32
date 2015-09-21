package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Stack;

public class ScreenManager {
    private Stack<Screen> screens;

    public ScreenManager ()
    {
        screens = new Stack<Screen>();
    }

    public void loadScreen (Screen screen)
    {
        if (!screens.empty()) {
            screens.peek().dispose();
        }

        screen.init();
        screens.push(screen);
    }

    public void update(float delta)
    {
        screens.peek().update(delta);
    }

    public void render(SpriteBatch batch)
    {
        screens.peek().render(batch);
    }

    public void dispose ()
    {
        for (int i=0; i<screens.size(); i++) {
            screens.peek().dispose();
            screens.pop();
        }

        screens = null;
    }
}
