package com.akx2.ld32;

import com.badlogic.gdx.math.Vector2;

public class Explosion {
    public Vector2 position = new Vector2(0,0);
    public int animationIndex = 0;
    public boolean isActive = false;

    float animationTimer = 0;
    float animationSpeed = 5f;

    public void update (float delta)
    {
        if (isActive) {
            animationTimer += animationSpeed * delta;
            if (animationTimer >= 1) {
                animationTimer -= 1;
                if (animationIndex == 4) {
                    isActive = false;
                } else {
                    animationIndex++;
                }
            }
        }
    }
}
