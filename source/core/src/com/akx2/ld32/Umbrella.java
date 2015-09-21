package com.akx2.ld32;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Umbrella implements Pool.Poolable {
    final float MOVEMENT_SPEED = -250f;

    public Vector2 position;
    public Rectangle bounds;
    public Rectangle collisionBox;
    public boolean isAlive;
    public int type;

    public Umbrella() {

        position = new Vector2(0, 0);
        bounds = new Rectangle(0, 0, 128, 64);
        collisionBox = new Rectangle(0, 0, 128, 64);
        isAlive = false;
    }


    public void init(float x, float y, int type) {
        position.set(x, y);
        collisionBox.x = position.x;
        collisionBox.y = position.y;
        isAlive = true;
        this.type = type;
    }


    @Override
    public void reset() {
        position.set(0, 0);
        isAlive = false;
    }

    public void update(float delta) {
        position.add(MOVEMENT_SPEED * delta, 0);
        collisionBox.x = position.x;
        collisionBox.y = position.y;

        if (!isOnScreen()) {
            isAlive = false;
        }
    }

    public boolean isOnScreen() {
        if (position.x - bounds.x + bounds.getWidth() < 0) {
            return false;
        }

        return true;
    }
}