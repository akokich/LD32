package com.akx2.ld32;

import com.badlogic.gdx.math.Vector2;

public class Trail {
    public Vector2 position = new Vector2(0,0);
    public boolean isActive = false;

    float trailAnimationTimer = 0;
    float trailAnimationSpeed = 40;
    int trailAnimationIndex = 0;

    public Trail()
    {

    }

    public void update(float delta)
    {
        trailAnimationTimer += trailAnimationSpeed * delta;

        if ((trailAnimationTimer > 1) && (trailAnimationIndex == 6))
        {
            trailAnimationTimer -= 1;
            trailAnimationIndex = 0;
            isActive = false;
            return;
        }


        if ((trailAnimationTimer > 1) && (trailAnimationIndex < 6))
        {
            trailAnimationTimer -= 1;
            trailAnimationIndex++;
        }

        position.add(-500 * delta, 0);

    }
}
