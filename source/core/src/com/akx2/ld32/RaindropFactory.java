package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class RaindropFactory {
    public Texture texture;
    public TextureRegion region;

    int tLen = 0;
    int tI = 0;
    Raindrop tRaindrop;

    Array<Raindrop> activeRaindrops = new Array<Raindrop>();
    Pool<Raindrop> raindropsPool = new Pool<Raindrop>() {
        @Override
        protected Raindrop newObject() {
            return new Raindrop();
        }
    };

    public RaindropFactory ()
    {
        texture = new Texture(Gdx.files.internal("images/raindrop.png"), true);
        region = new TextureRegion(texture);
    }

    public void add (float x, float y, float tarX, float tarY)
    {
        tRaindrop = raindropsPool.obtain();
        tRaindrop.init(x, y, tarX, tarY);
        activeRaindrops.add(tRaindrop);
    }

    public void render (SpriteBatch batch)
    {
        tLen = activeRaindrops.size;
        for (tI = tLen; --tI >= 0;) {
            tRaindrop = activeRaindrops.get(tI);
            if (tRaindrop.isAlive) {
                batch.draw(region, tRaindrop.position.x, tRaindrop.position.y, 16, 16, 32, 32, 1, 1, 0);
            }
        }
    }

    public void update (float delta)
    {
        // RELEASE FIREWORKS
        tLen = activeRaindrops.size;
        for (tI = tLen; --tI >= 0;) {
            tRaindrop = activeRaindrops.get(tI);
            if (tRaindrop.isAlive == false) {
                activeRaindrops.removeIndex(tI);
                raindropsPool.free(tRaindrop);
            } else {
                tRaindrop.update(delta);
            }
        }
    }


    public void clearAll ()
    {
        tLen = activeRaindrops.size;
        for (tI = tLen; --tI >= 0;) {
            tRaindrop = activeRaindrops.get(tI);
            activeRaindrops.removeIndex(tI);
            raindropsPool.free(tRaindrop);
        }
    }

    public void dispose ()
    {
        texture.dispose();
    }

}
