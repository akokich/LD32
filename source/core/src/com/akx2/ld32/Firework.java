package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import java.util.Random;

public class Firework implements Pool.Poolable{
    final float MODE_TIMER_DELAY = 1f;
    final float MODE_TIMER_SPEED = 10f;

    public Vector2 velocity;

    public Vector2 position;
    public Rectangle collisionBox;
    public boolean isAlive;

    public int modeIndex = 0;
    public float modeIndexTimer = 0;

    Color color;
    Random rng = new Random();

    public Firework ()
    {
        velocity = new Vector2(0,0);
        position = new Vector2(0,0);
        collisionBox = new Rectangle(0, 0, 32, 32);
        isAlive = false;
        color = new Color();
    }

    public void init (float x, float y, float velX, float velY)
    {
        velocity.set (velX, velY);
        position.set(x, y);
        isAlive = true;
        collisionBox.x = position.x;
        collisionBox.y = position.y;

        color.set(getRandomColorValue (), getRandomColorValue (), getRandomColorValue (), 1);
    }

    private float getRandomColorValue ()
    {
        return rng.nextFloat() * (1 - 0.5f) + 0.5f;
    }

    @Override
    public void reset() {
        position.set(0, 0);
        isAlive = false;
        modeIndexTimer = 0;
        modeIndex = 0;
    }

    public void update (float delta)
    {
        position.add(velocity.x * delta, velocity.y * delta);
        collisionBox.x = position.x;
        collisionBox.y = position.y;

        if (!isOnScreen())
        {
            isAlive = false;
        }

        modeIndexTimer += MODE_TIMER_SPEED * delta;
        if (modeIndexTimer >= MODE_TIMER_DELAY) {
            modeIndexTimer -= MODE_TIMER_DELAY;

            modeIndex++;
            if (modeIndex == 3) {
                modeIndex = 0;
            }
        }
    }

    public boolean isOnScreen ()
    {
        if (position.x > Settings.WIDTH)
        {
            return false;
        }
        return true;
    }
}
