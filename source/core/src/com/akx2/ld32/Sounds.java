package com.akx2.ld32;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class Sounds {
    Sound shoot;
    Sound explode;
    Sound powerup;
    Sound enemyDie;

    public Sounds ()
    {
        shoot = Gdx.audio.newSound(Gdx.files.internal("audio/fire.mp3"));
        explode = Gdx.audio.newSound(Gdx.files.internal("audio/explode.mp3"));
        powerup = Gdx.audio.newSound(Gdx.files.internal("audio/powerup.mp3"));
        enemyDie = Gdx.audio.newSound(Gdx.files.internal("audio/enemydie.mp3"));
    }

    public void playShoot ()
    {
        if (Settings.SOUNDS) {
            shoot.play(0.5f);
        }
    }

    public void playExplode()
    {
        if (Settings.SOUNDS) {
            explode.play(0.5f);
        }
    }

    public void playPowerup()
    {
        if (Settings.SOUNDS) {
            powerup.play(0.5f);
        }
    }

    public void playEnemyDie ()
    {
        if (Settings.SOUNDS) {
            enemyDie.play(0.5f);
        }
    }

    public void dispose()
    {
        shoot.dispose();
        explode.dispose();
        powerup.dispose();
        enemyDie.dispose();
    }
}
