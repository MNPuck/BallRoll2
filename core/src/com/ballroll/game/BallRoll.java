package com.ballroll.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.ballroll.screens.DirectedGame;
import com.ballroll.screens.GameScreen;
import com.ballroll.game.Assets;

public class BallRoll extends DirectedGame {

	@Override
	public void create () {
		
		// Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		// Load assets
		Assets.instance.init(new AssetManager());
		
		// Initialize controller and renderer
		
		setScreen(new GameScreen(this));
		
	   // Assets.instance.dispose();
		
	}

}