package com.akx2.ld32;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import javafx.scene.Camera;

public class LD32 extends ApplicationAdapter {
	private SpriteBatch batch;
    public ScreenManager screens;
    private BitmapFont font;
    private OrthographicCamera camera;
	private Viewport viewport;

	@Override
	public void create () {
		batch = new SpriteBatch();
        camera = new OrthographicCamera();

        viewport = new ScalingViewport(Scaling.fit, Settings.WIDTH, Settings.HEIGHT, camera);
        viewport.apply();

        screens = new ScreenManager();
        screens.loadScreen(new MenuScreen(this, false));

        font = new BitmapFont();
    }

	@Override
	public void render () {
        // UPDATE
        screens.update(Gdx.graphics.getDeltaTime());

        // RENDER
		Gdx.gl20.glClearColor(0.15f, 0.15f, 0.15f, 1);
		Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		screens.render(batch);

       // font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Settings.HEIGHT- 20);
		batch.end();


	}
}
