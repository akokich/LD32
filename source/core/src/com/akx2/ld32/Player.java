package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Player {
    final int START_HEIGHT = 420;
    final float MOVEMENT_SPEED = 420f;
    final Rectangle MOVEMENT_BOUNDS = new Rectangle(0, 100, Settings.WIDTH / 1.5f, Settings.HEIGHT);
    public final int UMBRELLAS_REQ = 5;
    public final int POWER_MAX = 3;

    final Vector2 MOUNT_POINT = new Vector2(230,25);

    Texture playerTexture;
    Texture playerDieTexture;
    TextureRegion[] playerDieRegion;

    Texture trailTexture;
    TextureRegion[] trailTextureRegions;
    Trail[] trails;
    float trailSpawnTimer = 0;
    float trailSpawnSpeed = 30;


    public Vector2 position;
    public Vector2 bounds;
    public Rectangle collisionBox;

    public int powerLevel = 0;
    public int umbrellasCollected = 0;

    public boolean isAlive = true;
    public boolean isDying = false;
    public boolean isRespawning = false;
    public int dyingAnimationIndex = 0;
    public float dyingAnimationTimer = 0;
    public float dyingAnimationSpeed = 10;

    public float respawnSafeTimer = 0;

    public int lives = 3;

    public int score = 0;

    int tI = 0;

    public Player ()
    {
        playerTexture = new Texture (Gdx.files.internal("images/player.png"), true);
        playerDieTexture = new Texture(Gdx.files.internal("images/player-die.png"), true);
        playerDieRegion = new TextureRegion[5];

        for (int i=0;i <5; i++)
        {
            playerDieRegion[i] = new TextureRegion(playerDieTexture, 0, i * 64, 256, 64);
        }

        position = new Vector2 (0, START_HEIGHT);
        bounds = new Vector2(playerTexture.getWidth(), playerTexture.getHeight());
        collisionBox = new Rectangle(0, 0, 256, 4);

        trailTexture = new Texture(Gdx.files.internal("images/needle-trail.png"), true);
        trailTextureRegions = new TextureRegion[7];
        for (tI=0;tI <7; tI++)
        {
            trailTextureRegions[tI] = new TextureRegion(trailTexture, tI * 36, 0, 36, 36);
        }
        trails = new Trail[20];
        for (tI=0; tI<trails.length; tI++)
        {
            trails[tI]=  new Trail();
        }
    }

    public void die()
    {
        if ((isAlive) && (!Settings.isHacking)) {
            isAlive = false;
            isDying = true;
            lives--;
        }
    }

    public void respawn (boolean keepWeapon)
    {
        position.set(-256, START_HEIGHT);
        isRespawning = true;
        isAlive = false;
        dyingAnimationIndex = 0;

        if (!keepWeapon)
        {
            powerLevel = 0;
            umbrellasCollected = 0;
        }
    }

    public void collectedUmbrella ()
    {
        if (isAlive) {
            umbrellasCollected++;

            if (umbrellasCollected == UMBRELLAS_REQ) {
                umbrellasCollected = 0;

                if (powerLevel < POWER_MAX) {
                    powerLevel++;
                }
            }
        }
    }

    public void render (SpriteBatch batch)
    {
        if ((isAlive) || (isRespawning))
        {
            for (tI=0; tI<trails.length; tI++)
            {
                if (trails[tI].isActive) {
                    batch.draw(trailTextureRegions[trails[tI].trailAnimationIndex], trails[tI].position.x, trails[tI].position.y + 14);
                }
            }

            if (respawnSafeTimer < 2)
            {
                batch.setColor(1, 1, 1, respawnSafeTimer % 0.5f);
                batch.draw(playerTexture, position.x, position.y);
                batch.setColor(1, 1, 1, 1);
            } else {
                batch.draw(playerTexture, position.x, position.y);
            }
        }

        if (isDying)
        {
            batch.draw(playerDieRegion[dyingAnimationIndex], position.x, position.y);
        }


    }


    public void moveLeft (float delta)
    {
        if (isAlive) {
            position.x -= MOVEMENT_SPEED * delta;
        }
    }

    public void moveRight (float delta)
    {
        if (isAlive) {
            position.x += MOVEMENT_SPEED * delta * 1.3f;
        }
    }

    public void moveUp (float delta)
    {
        if (isAlive) {
            position.y += MOVEMENT_SPEED * delta;
        }
    }

    public void moveDown (float delta)
    {
        if (isAlive) {
            position.y -= MOVEMENT_SPEED * delta;
        }
    }

    public void update (float delta)
    {
        if (isAlive) {
            // CLAMP POSITION
            if (position.x < MOVEMENT_BOUNDS.x) {
                position.x = MOVEMENT_BOUNDS.x;
            } else if (position.x + bounds.x > MOVEMENT_BOUNDS.getWidth()) {
                position.x = MOVEMENT_BOUNDS.getWidth() - bounds.x;
            }

            if (position.y < MOVEMENT_BOUNDS.y) {
                position.y = MOVEMENT_BOUNDS.y;
            } else if (position.y + bounds.y > MOVEMENT_BOUNDS.getHeight()) {
                position.y = MOVEMENT_BOUNDS.getHeight() - bounds.y;
            }

            collisionBox.x = position.x;
            collisionBox.y = position.y + 32;

            respawnSafeTimer += delta;
        }

        if (isDying)
        {
            dyingAnimationTimer += dyingAnimationSpeed * delta;
            if (dyingAnimationTimer > 1)
            {
                dyingAnimationTimer -= 1;
                dyingAnimationIndex ++;
                if (dyingAnimationIndex > 4)
                {
                    dyingAnimationIndex = 4;
                    isDying = false;
                }
            }
        }

        if (isRespawning)
        {
            position.add(500 * delta, 0);

            if (position.x > 100)
            {
                position.x = 100;
                isRespawning = false;
                isAlive = true;
                respawnSafeTimer = 0;
            }
        }

        trailSpawnTimer += trailSpawnSpeed * delta;
        if (trailSpawnTimer > 1)
        {
            trailSpawnTimer -= 1;
            spawnTrail ();
        }

        for (tI=0; tI<trails.length; tI++)
        {
            if (trails[tI].isActive) {
                trails[tI].update(delta);
            }
        }
    }

    public void spawnTrail ()
    {
        for (tI=0; tI < trails.length; tI++)
        {
            if (!trails[tI].isActive)
            {
                trails[tI].isActive = true;
                trails[tI].position.x = position.x;
                trails[tI].position.y = position.y;
                return;
            }
        }
    }

    public void dispose ()
    {
        playerTexture.dispose();
        playerDieTexture.dispose();
        trailTexture.dispose();
    }
}

