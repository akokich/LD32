package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import javafx.scene.input.KeyCode;

import java.util.Random;

public class PlayScreen extends Screen {
    final float BOSS_FIRE_TIMER_RECHARGE = 1f;

    final float FIRE_TIMER_RECHARGE = 2.25f;

    final float SPAWN_TIMER_RECHARGE = 0.75f;

    final float CLOUD_FIRE_RECHARGE = 0.75f;

    final LD32 game;

    Player player;

    Background background;

    FireworkFactory fireworks;

    CloudFactory clouds;

    UmbrellaFactory umbrellas;

    RaindropFactory raindrops;

    World world;

    Boss boss;
    float bossFireTimer = 0;

    Sounds sound;

    HUD hud;

    ScreenMessage message;

    float fireTimer = 0;
    boolean canFire = true;

    float spawnTimer = 0;
    boolean doSpawn = true;

    float cloudFireTimer = 0;

    int wave = 1;

    boolean goBackToMenu = false;
    boolean hasSunset = false;

    int tLen = 0;
    int tLen2 = 0;
    int tI = 0;
    int tI2 = 0;
    Vector2 tVector2 = new Vector2(0,0);
    Cloud tCloud;
    Firework tFirework;
    Umbrella tUmbrella;
    Raindrop tRaindrop;
    Random rng = new Random();

    Music gameMusic;

    public PlayScreen(LD32 game)
    {
        this.game = game;
    }

    @Override
    public void init() {
        sound = new Sounds ();
        player = new Player ();
        fireworks =  new FireworkFactory();
        clouds = new CloudFactory();
        umbrellas = new UmbrellaFactory();
        raindrops = new RaindropFactory();
        background = new Background();
        world = new World(new Vector2(0, 0), true);
        message = new ScreenMessage();
        hud = new HUD(player.playerTexture);
        boss = new Boss();

        spawnTimer = 1;

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("audio/game.mp3"));
        gameMusic.setLooping(true);
        gameMusic.setVolume(1.0f);
        if (Settings.MUSIC)
        {
            gameMusic.play();
        }

        message.show("Wave " + wave);
    }

    @Override
    public void render(SpriteBatch batch) {
        background.render(batch);
        umbrellas.render(batch);
        player.render(batch);
        clouds.render(batch);
        boss.render(batch);
        fireworks.render(batch);

        raindrops.render(batch);

        hud.render(batch, (player.lives-1 > 0) ? player.lives-1 : 0, (player.powerLevel == 3) ? -1 :  (player.UMBRELLAS_REQ - player.umbrellasCollected), wave, player.score);

        message.render(batch);
    }

    @Override
    public void update(float delta) {
        if (!message.isShowing) {
            if (goBackToMenu)
            {
                game.screens.loadScreen(new MenuScreen(game, hasSunset));
            }

            handleInput(delta);

            spawnTimer += SPAWN_TIMER_RECHARGE * delta;
            if (spawnTimer >= 1) {
                spawnTimer -= 1;
                if (doSpawn) {
                    clouds.spawn(wave);
                }
            }

            cloudFireTimer += CLOUD_FIRE_RECHARGE * delta;
            if (cloudFireTimer >= 1) {
                cloudFireTimer -= 1;

                if ((clouds.activeClouds.size > 0) && (player.isAlive)) {
                    tVector2 = clouds.getRandomCloudPosition();
                    raindrops.add(tVector2.x, tVector2.y, player.position.x, player.position.y);
                }
            }

            background.update(delta);
            player.update(delta);

            // CLOUD - FIREWORK COLLISION
            tLen = clouds.activeClouds.size;
            for (tI = tLen; --tI >= 0; ) {
                tCloud = clouds.activeClouds.get(tI);

                tLen2 = fireworks.activeFireworks.size;
                for (tI2 = tLen2; --tI2 >= 0; ) {
                    tFirework = fireworks.activeFireworks.get(tI2);

                    if ((tCloud.isAlive) && (tCloud.collisionBox.overlaps(tFirework.collisionBox))) {
                        tCloud.damage(10);
                        tFirework.isAlive = false;
                        if (!tCloud.isAlive) {
                            umbrellas.spawn(tFirework.position.x, tFirework.position.y);
                            player.score += 10;
                            sound.playEnemyDie();
                        }
                    }
                }
            }

            // firework boss
            if ((boss.isAlive) && (!boss.isEntering)) {
                tLen2 = fireworks.activeFireworks.size;
                for (tI2 = tLen2; --tI2 >= 0; ) {
                    tFirework = fireworks.activeFireworks.get(tI2);

                    if ((boss.collisionBox.overlaps(tFirework.collisionBox))) {
                        boss.damage(10);
                        tFirework.isAlive = false;
                    }
                }

                bossFireTimer += BOSS_FIRE_TIMER_RECHARGE * delta;

                if (bossFireTimer > 1)
                {
                    bossFireTimer -= 1;
                    raindrops.add(boss.position.x + (boss.bounds.getWidth() / 2), boss.position.y + (boss.bounds.getHeight() / 2), 0, player.position.y + (boss.bounds.getHeight() / 2));
                    raindrops.add(boss.position.x + (boss.bounds.getWidth() / 2), boss.position.y + (boss.bounds.getHeight() / 2), 0, player.position.y + (boss.bounds.getHeight() / 2) + 200);
                    raindrops.add(boss.position.x + (boss.bounds.getWidth() / 2), boss.position.y + (boss.bounds.getHeight() / 2), 0, player.position.y + (boss.bounds.getHeight() / 2) - 200);
                    raindrops.add(boss.position.x + (boss.bounds.getWidth() / 2), boss.position.y + (boss.bounds.getHeight() / 2), 0, player.position.y + (boss.bounds.getHeight() / 2) + 400);
                    raindrops.add(boss.position.x + (boss.bounds.getWidth() / 2), boss.position.y + (boss.bounds.getHeight() / 2), 0, player.position.y + (boss.bounds.getHeight() / 2) - 400);
                }
            }

            // PLAYER - UMBRELLA COLLISION
            tLen = umbrellas.activeUmbrellas.size;
            for (tI = tLen; --tI >= 0; ) {
                tUmbrella = umbrellas.activeUmbrellas.get(tI);

                if ((tUmbrella.isAlive) && tUmbrella.collisionBox.overlaps(player.collisionBox)) {
                    tUmbrella.isAlive = false;
                    player.collectedUmbrella();
                    sound.playPowerup();
                    player.score += 100;
                }
            }

            // PLAYER - RAINDROP COLLISION
            tLen = raindrops.activeRaindrops.size;
            for (tI = tLen; --tI >= 0; ) {
                tRaindrop = raindrops.activeRaindrops.get(tI);

                if ((tRaindrop.isAlive) && tRaindrop.collisionBox.overlaps(player.collisionBox) && (player.isAlive) && (player.respawnSafeTimer > 2)) {
                    tRaindrop.isAlive = false;
                    doSpawn = false;
                    sound.playExplode();
                    player.die();
                }
            }

            if ((!player.isAlive) && (!player.isRespawning) && (!player.isDying)) {
                if (player.lives > 0) {
                    player.respawn(false);
                } else {
                    message.show("GAME OVER");
                    goBackToMenu = true;
                }
            }

            if ((player.isAlive) && (!background.isMaxScroll))
            {
                doSpawn = true;
            }


            umbrellas.update(delta);
            fireworks.update(delta);
            raindrops.update(delta);
            clouds.update(delta);
            boss.update(delta);

            if ((background.isMaxScroll) && (clouds.activeClouds.size == 0))
            {
                if (wave == 3) {
                    if (!boss.isSpawned) {
                        // spawn boss
                        doSpawn = false;
                        boss.spawn();
                    }

                    if ((!boss.isAlive) && (!boss.isDying) && (boss.isSpawned))
                    {
                        player.score += 1000;
                        message.show("You Saved New Year's in Seattle!");
                        goBackToMenu = true;
                        hasSunset = true;
                    }
                } else {
                    message.show("Wave " + wave + " Complete");
                    wave++;
                    restartWave(true);
                }
            }
        } else {
            message.update(delta);
        }
    }

    public void restartWave(boolean keepWeapon)
    {
        background.reset();
        raindrops.clearAll();
        fireworks.clearAll();
    }

    private void handleInput (float delta)
    {
        if (Gdx.input.isKeyPressed(Input.Keys.A) || (Gdx.input.isKeyPressed(Input.Keys.LEFT)))
        {
            player.moveLeft(delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) || (Gdx.input.isKeyPressed(Input.Keys.RIGHT)))
        {
            player.moveRight(delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) || (Gdx.input.isKeyPressed(Input.Keys.UP)))
        {
            player.moveUp(delta);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S) || (Gdx.input.isKeyPressed(Input.Keys.DOWN)))
        {
            player.moveDown(delta);
        }

        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) || Gdx.input.isKeyPressed(Input.Keys.SPACE))
        {
            if ((canFire) && (player.isAlive)) {
                sound.playShoot();
                switch (player.powerLevel)
                {
                    case 0:
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, 0);
                        break;
                    case 1:
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, 250);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, 0);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, -250);
                        break;
                    case 2:
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, 550);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, 250);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, 0);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, -250);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, -550);
                        break;
                    case 3:
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, 550);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, 250);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, 0);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, -250);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x, player.position.y + player.MOUNT_POINT.y, 550, -550);

                        fireworks.add(player.position.x + player.MOUNT_POINT.x - 100, player.position.y + player.MOUNT_POINT.y, 0, 550);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x - 100, player.position.y + player.MOUNT_POINT.y, 0, -550);

                        fireworks.add(player.position.x + player.MOUNT_POINT.x - 50, player.position.y + player.MOUNT_POINT.y, 0, 550);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x - 50, player.position.y + player.MOUNT_POINT.y, 0, -550);

                        fireworks.add(player.position.x + player.MOUNT_POINT.x - 150, player.position.y + player.MOUNT_POINT.y, 0, 550);
                        fireworks.add(player.position.x + player.MOUNT_POINT.x - 150, player.position.y + player.MOUNT_POINT.y, 0, -550);
                        break;
                }

                canFire = false;
                fireTimer = 0;
            }
        }

        fireTimer += FIRE_TIMER_RECHARGE * delta;
        if (fireTimer >= 1)
        {
            fireTimer -= 1;
            canFire = true;
        }
    }

    @Override
    public void dispose() {
        player.dispose();
        clouds.dispose();
        umbrellas.dispose();
        fireworks.dispose();
        message.dispose();
        sound.dispose();
        background.dispose();
        boss.dispose();
        hud.dispose();

        if (gameMusic.isPlaying())
        {
            gameMusic.stop();
        }
        gameMusic.dispose();
    }
}

