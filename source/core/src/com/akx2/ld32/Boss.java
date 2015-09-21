package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;
import java.util.Set;

public class Boss {
    final int MAX_EXPLOSIONS = 10;
    Texture texture;

    Sound boom;

    Texture explosionTexture;
    TextureRegion[] explosionRegions;
    Explosion[] explosions;

    Texture white;

    int explosionCount = 0;
    float explosionTriggerTimer = 0;
    final float explosionTriggerSpeed = 2.5f;


    Vector2 position;
    Rectangle bounds;

    public Rectangle collisionBox;
    public boolean isSpawned;
    public boolean isAlive;
    public boolean isDying;
    public boolean isEntering;

    public int health = 200;

    Vector2 tVector2 = new Vector2();

    Random rng = new Random();
    int tIdx= 0 ;

    public Boss ()
    {
        boom = Gdx.audio.newSound(Gdx.files.internal("audio/bossboom.mp3"));
        texture = new Texture (Gdx.files.internal("images/boss.png"), true);

        white = new Texture(Gdx.files.internal("images/dot.white.png"), true);
        explosionTexture = new Texture (Gdx.files.internal("images/explosion.png"), true);
        explosionRegions = new TextureRegion[5];
        for (int i=0;i <5;i++)
        {
            explosionRegions[i] = new TextureRegion(explosionTexture, i * 64, 0, 64, 64);
        }

        explosions = new Explosion[MAX_EXPLOSIONS];
        for (int i=0;i <MAX_EXPLOSIONS;i++)
        {
            explosions[i] = new Explosion();
        }

        position = new Vector2(0,0);
        bounds = new Rectangle(0,0, texture.getWidth(), texture.getHeight());

        collisionBox = new Rectangle(0,0, texture.getWidth(), texture.getHeight());
        isSpawned = false;
        isAlive = false;
        isDying = false;
        isEntering = false;
    }

    public void damage(int damage){
        if (isEntering)
        {
            return;
        }

        health -= damage;
        if (health <= 0)
        {
            health=  0;
            isAlive = false;
            isDying = true;
        }
    }

    public void spawn ()
    {
        position.set(1400, 300);
        isAlive = true;
        isDying = false;
        isEntering = true;
        isSpawned = true;
    }

    public void render (SpriteBatch batch)
    {
        if ((isAlive) || (isDying)) {
            batch.draw(texture, position.x, position.y);

            batch.setColor(1, 0, 0, 1);
            batch.draw(white, (Settings.WIDTH / 2) - (health * 3 / 2), Settings.HEIGHT - 100, health * 3, 50);
            batch.setColor(1, 1, 1, 1);
        }

        for (tIdx=0; tIdx<MAX_EXPLOSIONS; tIdx++)
        {
            if (explosions[tIdx].isActive) {
                batch.draw(explosionRegions[explosions[tIdx].animationIndex], explosions[tIdx].position.x, explosions[tIdx].position.y);
            }
        }
    }

    public void update (float delta)
    {
        if (position.x > Settings.WIDTH - (bounds.getWidth() + (bounds.getWidth() * 0.5f))) {
            position.add(-100 * delta, 0);
            collisionBox.x = position.x;
            collisionBox.y = position.y;
        } else {
            isEntering = false;
        }

        if ((isDying) && (explosionCount < MAX_EXPLOSIONS))
        {
            explosionTriggerTimer += explosionTriggerSpeed * delta;
            if (explosionTriggerTimer >= 1)
            {
                tVector2 = getRandomExplosionPoint();
                explosionTriggerTimer -= 1;
                explosions[explosionCount].isActive = true;
                explosions[explosionCount].position.set(tVector2.x, tVector2.y);
                explosionCount ++;
                boom.play(1.0f);
            }
        }

        if (explosionCount == MAX_EXPLOSIONS)
        {
            isDying = false;
        }

        for (tIdx=0; tIdx<MAX_EXPLOSIONS; tIdx++)
        {
            explosions[tIdx].update(delta);
        }
    }

    public Vector2 getRandomExplosionPoint ()
    {
        tVector2.x = position.x + rng.nextInt(texture.getWidth());
        tVector2.y = position.y + rng.nextInt(texture.getHeight());

        return tVector2;
    }

    public void dispose ()
    {
        texture.dispose();
        explosionTexture.dispose();
        white.dispose();
    }
}
