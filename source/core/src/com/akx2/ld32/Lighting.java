package com.akx2.ld32;

import box2dLight.RayHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;

public class Lighting {
    public RayHandler rayHandler;

    public Lighting(World world) {
        rayHandler = new RayHandler(world, Settings.WIDTH, Settings.HEIGHT);
        rayHandler.setAmbientLight(0f, 0f, 0f, 0.3f);
        rayHandler.setBlurNum(12);
        rayHandler.setBlur(true);
        rayHandler.setCulling(true);

        rayHandler.useDefaultViewport();
    }
}
