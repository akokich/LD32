package com.akx2.ld32;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Cloud implements Pool.Poolable {
    final float MOVEMENT_SPEED = -250f;

    public Vector2 position;
    public Rectangle bounds;
    public Rectangle collisionBox;
    public boolean isAlive;
    public boolean isDying;
    public int type;
    public int health;

    public float dyingAlpha = 1;
    public float dyingSpeed = 0;

    public Cloud ()
    {
        position = new Vector2(0, 0);
        bounds = new Rectangle(0, 0, 128, 64);
        collisionBox = new Rectangle(0, 0, 128, 64);
        isAlive = false;
        isDying = false;
        type = 0;
        health = 10;
    }

    public void init (float x, float y, int type, int health)
    {
        position.set(x, y);
        collisionBox.x = position.x;
        collisionBox.y = position.y;

        this.type = type;
        this.health = health;

        isAlive = true;
        isDying = false;
    }

    public void die ()
    {
        isAlive = false;
        isDying = true;
        dyingAlpha = 1;
    }

    public void damage(int damage)
    {
        if (isAlive) {
            health -= damage;
            if (health <= 0) {
                die();
            }
        }
    }

    @Override
    public void reset() {
        position.set(0, 0);
        isAlive = false;
        health = 0;
    }

    public void update (float delta)
    {
        position.add(MOVEMENT_SPEED * delta, 0);
        collisionBox.x = position.x;
        collisionBox.y = position.y;

        if (!isOnScreen())
        {
            isAlive = false;
        }

        if (isDying)
        {
            dyingAlpha -= dyingSpeed * delta;
            if (dyingAlpha < 0)
            {
                isDying = false;
            }
        }
    }

    public boolean isOnScreen ()
    {
        if (position.x - bounds.x + bounds.getWidth() < 0)
        {
            return false;
        }

        return true;
    }
}
