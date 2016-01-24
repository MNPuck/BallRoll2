package com.ballroll.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import com.ballroll.game.BallRoll;

public class DesktopLauncher {	
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;


	public static void main (String[] args) {
		if (rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets-raw/images", "../android/assets/images", "BallRoll.pack");
		}
		
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Operator";
		cfg.width = 1920;
		cfg.height = 1080;
		cfg.fullscreen = true;
		cfg.vSyncEnabled = true;
	
		
		new LwjglApplication(new BallRoll(), cfg);
		
	}
	
}