package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Random;

public class CloudFactory {
    public Texture texture;
    public TextureRegion[] regions;

    Array<Cloud> activeClouds = new Array<Cloud>();
    Pool<Cloud> cloudPool = new Pool<Cloud>() {
        @Override
        protected Cloud newObject() {
            return new Cloud();
        }
    };

    int tLen = 0;
    int tI = 0;
    Cloud tCloud;

    int tHealth = 0;
    int tType = 0;

    Random rng = new Random();

    public CloudFactory ()
    {
        texture = new Texture(Gdx.files.internal("images/clouds.png"), true);
        regions = new TextureRegion[3];
        regions[0] = new TextureRegion(texture, 0, 0, 128, 64);
        regions[1] = new TextureRegion(texture, 0, 64, 128, 64);
        regions[2] = new TextureRegion(texture, 0, 128, 128, 64);
    }

    public void spawn (int wave)
    {
        tType = rng.nextInt(wave);
        switch (tType)
        {
            case 0:
                tHealth = 10;
                break;
            case 1:
                tHealth = 30;
                break;
            case 2:
                tHealth = 60;
                break;
        }

        add (Settings.WIDTH, rng.nextFloat() * ((Settings.HEIGHT - 64) - 125f) + 125f, tType, tHealth);
    }

    public void add (float x, float y, int type, int health)
    {
        tCloud = cloudPool.obtain();
        tCloud.init(x, y, type, health);
        activeClouds.add(tCloud);
    }

    public Vector2 getRandomCloudPosition ()
    {
        return activeClouds.get(rng.nextInt(activeClouds.size)).position;
    }

    public void render (SpriteBatch batch)
    {
        tLen = activeClouds.size;
        for (tI = tLen; --tI >= 0;) {
            tCloud = activeClouds.get(tI);
            if ((tCloud.isAlive) || (tCloud.isDying)) {
                batch.setColor(1, 1, 1, tCloud.dyingAlpha);
                batch.draw(regions[tCloud.type], tCloud.position.x, tCloud.position.y);
                batch.setColor(1, 1, 1, 1);
            }
        }
    }

    public void update (float delta)
    {
        // RELEASE FIREWORKS
        tLen = activeClouds.size;
        for (tI = tLen; --tI >= 0;) {
            tCloud = activeClouds.get(tI);
            if (tCloud.isAlive == false) {
                activeClouds.removeIndex(tI);
                cloudPool.free(tCloud);
            } else {
                tCloud.update(delta);
            }
        }
    }

    public void dispose ()
    {
        texture.dispose();
    }
}
