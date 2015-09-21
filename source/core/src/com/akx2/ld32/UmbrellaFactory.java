package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Random;

public class UmbrellaFactory {
    public Texture texture;
    public TextureRegion[] regions;

    Array<Umbrella> activeUmbrellas = new Array<Umbrella>();
    Pool<Umbrella> umbrellasPool = new Pool<Umbrella>() {
        @Override
        protected Umbrella newObject() {
            return new Umbrella();
        }
    };

    int tLen = 0;
    int tI = 0;
    Umbrella tUmbrella;

    Random rng = new Random();

    public UmbrellaFactory ()
    {
        texture = new Texture(Gdx.files.internal("images/umbrellas.png"), true);
        regions = new TextureRegion[7];

        for (int i=0; i<regions.length; i++) {
            regions[i] = new TextureRegion(texture, i * 32, 0, 32, 32);
        }

    }

    public void spawn (float x, float y)
    {
        add (x, y, rng.nextInt(7));
    }

    public void add (float x, float y, int type)
    {
        tUmbrella = umbrellasPool.obtain();
        tUmbrella.init(x, y, type);
        activeUmbrellas.add(tUmbrella);
    }

    public void render (SpriteBatch batch)
    {
        tLen = activeUmbrellas.size;
        for (tI = tLen; --tI >= 0;) {
            tUmbrella = activeUmbrellas.get(tI);
            if (tUmbrella.isAlive) {
                batch.draw(regions[tUmbrella.type], tUmbrella.position.x, tUmbrella.position.y, 16, 16, 32, 32, 1.25f, 1.25f, 0);
            }
        }
    }

    public void update (float delta)
    {
        // RELEASE FIREWORKS
        tLen = activeUmbrellas.size;
        for (tI = tLen; --tI >= 0;) {
            tUmbrella = activeUmbrellas.get(tI);
            if (tUmbrella.isAlive == false) {
                activeUmbrellas.removeIndex(tI);
                umbrellasPool.free(tUmbrella);
            } else {
                tUmbrella.update(delta);
            }
        }
    }

    public void dispose ()
    {
        texture.dispose();
    }

}
