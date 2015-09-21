package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import java.util.Random;
import java.util.Set;

public class Raindrop implements Pool.Poolable
{
    public final int SPEED = 350;

    public Vector2 position;
    public Vector2 origin;

    public Vector2 targetPosition;

    public Rectangle bounds;
    public Rectangle collisionBox;

    Vector2 moveToDirectionVector;
    Vector2 moveToVelocityVector;
    Vector2 moveToMovementVector;

    boolean isAlive = false;

    public Raindrop ()
    {
        position = new Vector2(0,0);
        targetPosition = new Vector2(0,0);
        bounds = new Rectangle(0, 0, 32, 32);
        origin = new Vector2(16, 16);

        moveToDirectionVector = new Vector2 (0,0);
        moveToVelocityVector = new Vector2 (0,0);
        moveToMovementVector = new Vector2 (0,0);
        collisionBox = new Rectangle(0, 0, 32, 32);
    }

    public void init (float x, float y, float tarX, float tarY)
    {
        targetPosition.set (tarX, tarY);
        position.set(x, y);
        isAlive = true;
        collisionBox.x = position.x;
        collisionBox.y = position.y;

        moveToDirectionVector.set(targetPosition).sub(position).nor();
        moveToVelocityVector = new Vector2(moveToDirectionVector).scl(SPEED);
    }

    @Override
    public void reset() {

        position.set(0,0);
        targetPosition.set(0,0);
        collisionBox.x = 0;
        collisionBox.y = 0;

        moveToDirectionVector.set(0,0);
        moveToVelocityVector.set(0,0);
        moveToMovementVector.set(0,0);

        isAlive = false;
    }

    public void update (float delta)
    {
        moveToMovementVector.set(moveToVelocityVector).scl(delta);
        position.add(moveToMovementVector);

        collisionBox.x = position.x;
        collisionBox.y = position.y;

        if (!isOnScreen())
        {
            isAlive = false;
        }
    }

    public boolean isOnScreen ()
    {
        if (position.x + bounds.getWidth() < 0)
        {
            return false;
        } else if (position.x > Settings.WIDTH)
        {
            return false;
        }

        if (position.y + bounds.y < 0 )
        {
            return false;
        } else if (position.y > Settings.HEIGHT)
        {
            return false;
        }

        return true;
    }
}

