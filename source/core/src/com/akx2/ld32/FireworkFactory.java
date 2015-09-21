package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class FireworkFactory {

    Texture fireworkTexture;
    TextureRegion[] fireworkTextureRegion;

    Array<Firework> activeFireworks = new Array<Firework>();
    Pool<Firework> fireworkPool = new Pool<Firework>() {
        @Override
        protected Firework newObject() {
            return new Firework();
        }
    };

    Firework tFirework;

    int tLen = 0;
    int tI = 0;

    Color batchColor;

    public FireworkFactory()
    {
        fireworkTexture = new Texture(Gdx.files.internal("images/firework1.png"), true);
        fireworkTextureRegion = new TextureRegion[2];
        fireworkTextureRegion[0] = new TextureRegion(fireworkTexture, 0, 0, 16, 16);
        fireworkTextureRegion[1] = new TextureRegion(fireworkTexture, 16, 0, 16, 16);
    }

    public void add (float x, float y, float velX, float velY)
    {
        tFirework = fireworkPool.obtain();
        tFirework.init(x, y, velX, velY);
        activeFireworks.add(tFirework);
    }

    public void render (SpriteBatch batch)
    {
        tLen = activeFireworks.size;
        for (tI = tLen; --tI >= 0;) {
            tFirework = activeFireworks.get(tI);
            if (tFirework.isAlive) {

                batchColor = batch.getColor();
                batch.setColor(tFirework.color);

                switch (tFirework.modeIndex)
                {
                    case 0:
                        batch.draw(fireworkTextureRegion[0], tFirework.position.x, tFirework.position.y, 8, 8, 16, 16, 2f, 2f, 0f);
                        break;
                    case 1:
                        batch.draw(fireworkTextureRegion[0], tFirework.position.x, tFirework.position.y, 8, 8, 16, 16, 2f, 2f, 0f);
                        batch.draw(fireworkTextureRegion[1], tFirework.position.x, tFirework.position.y, 8, 8, 16, 16, 2f, 2f, 0f);
                        break;
                    case 2:
                        batch.draw(fireworkTextureRegion[1], tFirework.position.x, tFirework.position.y, 8, 8, 16, 16, 2f, 2f, 0f);
                        break;

                }
                batch.setColor(1,1,1,1);
            }
        }
    }

    public void update (float delta)
    {
        // RELEASE FIREWORKS
        tLen = activeFireworks.size;
        for (tI = tLen; --tI >= 0;) {
            tFirework = activeFireworks.get(tI);
            if (tFirework.isAlive == false) {
                activeFireworks.removeIndex(tI);
                fireworkPool.free(tFirework);
            } else {
                tFirework.update(delta);
            }
        }
    }


    public void clearAll ()
    {
        tLen = activeFireworks.size;
        for (tI = tLen; --tI >= 0;) {
            tFirework = activeFireworks.get(tI);
            activeFireworks.removeIndex(tI);
            fireworkPool.free(tFirework);
        }
    }


    public void dispose ()
    {
        fireworkTexture.dispose();
    }
}
