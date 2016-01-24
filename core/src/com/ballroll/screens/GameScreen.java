package com.ballroll.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.ballroll.game.WorldController;
import com.ballroll.game.WorldRenderer;
import com.ballroll.util.GamePreferences;


public class GameScreen extends AbstractGameScreen {

	private static final String TAG = GameScreen.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	private boolean paused;
	
	private int fps;

	public GameScreen (DirectedGame game) {
		super(game);
		
	}

	@Override
	public void render (float deltaTime) {
		// Do not update game world when paused
		
		if (!paused) {
			// Update game world by the time that has passed
			// since last rendered frame.
			worldController.update(deltaTime, worldRenderer);
			
		}
		
		// Sets the clear screen color to: Black
		Gdx.gl.glClearColor(0,0,0,0);
		
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT | (Gdx.graphics.getBufferFormat().coverageSampling?GL20.GL_COVERAGE_BUFFER_BIT_NV:0));
	
		// Render game world to screen
		worldRenderer.render(deltaTime);
		
		
		// Change this back to old if statement that includes Game Over Check after testing is done
		
		if (worldController.isEscPressed() ||
			worldController.isSelectPressed()) hide();
		
	}

	@Override
	public void resize (int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show () {
		GamePreferences.instance.load();
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
		Gdx.input.setCatchBackKey(true);
	}

	@Override
	public void hide () {
		
		//worldRenderer.dispose();
		//game.dispose();
		
		Gdx.app.exit();
		
		Gdx.input.setCatchBackKey(false);
	
	}

	@Override
	public void pause () {
		paused = true;
	}

	@Override
	public void resume () {
		super.resume();
		// Only called on Android!
		paused = false;
	}

	@Override
	public InputProcessor getInputProcessor () {
		return worldController;
	}	

}
