package com.akx2.ld32.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.akx2.ld32.LD32;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.foregroundFPS = 0;
        config.backgroundFPS = 0;
        config.vSyncEnabled = false;
        config.width = 1280;
        config.height = 720;
		new LwjglApplication(new LD32(), config);
	}
}
