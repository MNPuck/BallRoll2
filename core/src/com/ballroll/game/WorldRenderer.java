package com.ballroll.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.utils.Disposable;
import com.ballroll.util.Constants;
import com.ballroll.util.GamePreferences;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;


public class WorldRenderer implements Disposable {

	private static final String TAG = WorldRenderer.class.getName();

	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private IsometricTiledMapRenderer renderer;
	private WorldController worldController;
	private ShapeRenderer shapeRenderer;

	public WorldRenderer (WorldController worldController) {
		this.worldController = worldController;
		init();
	}

	private void init () {
		
		batch = new SpriteBatch();
		
		camera = new OrthographicCamera(Constants.GAMEBOARD_WIDTH, Constants.GAMEBOARD_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true); // flip y-axis
		cameraGUI.update();
		
		renderer = new IsometricTiledMapRenderer(worldController.level.map, 1f / 32f);
		
		shapeRenderer = new ShapeRenderer();
	
	}

	public void render (float deltaTime) {
		
		renderMap(renderer);
		renderWorld(batch, shapeRenderer, deltaTime);
		
		renderGui(batch);
		
	}
	
	private void renderMap(TiledMapRenderer renderer) {
		
		float width = (float) (Constants.GAMEBOARD_WIDTH - .25f);
		float height = (float) (Constants.GAMEBOARD_HEIGHT - .75f);
		
		camera.translate(width, height);
		camera.update();
		renderer.setView(camera);
		renderer.render();
		
	}

	private void renderWorld (SpriteBatch batch, ShapeRenderer shapeRenderer, float deltaTime) {
		
		// render sprite batch
		
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		worldController.level.render(batch, deltaTime);
		batch.end();
		
		// render shapes
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		worldController.level.renderShapes(shapeRenderer, deltaTime);
		shapeRenderer.end();
		
				
	}
	
	private void renderGui (SpriteBatch batch) {
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();

		// draw score in upper left
		// renderGuiScore(batch);
		
		// draw high Score next to score
		//renderGuiHighScore(batch);
		
		// draw multiplier in upper middle
		//renderGuiMultiplier(batch);
		
		// draw lives in upper right
		// renderGuiLives(batch);
		
		// draw bombs to the left of lives
		// renderGuiBombs(batch);
		
		// draw FPS text bottom right
		renderGuiFpsCounter(batch);
		
		// draw ball graphic used for movement
		renderGuiBallImage(batch);
		
		// draw screen coords
		// renderGuiBallMapPos(batch);
		
		// draw game over text
		// renderGuiGameOverMessage(batch);

		batch.end();
		
	}
	
	private void renderGuiScore (SpriteBatch batch) {
		float x = 20;
		float y = 15;
		
		BitmapFont fpsFont = Assets.instance.fonts.defaultBig;
		
		fpsFont.setColor(0, 1, 0, 1); // green
		fpsFont.draw(batch, "Score " + worldController.level.returnScore() , x, y);
		fpsFont.setColor(1, 1, 1, 1); // white
	
	}
	
	private void renderGuiHighScore (SpriteBatch batch) {
		
	}
	
	private void renderGuiMultiplier (SpriteBatch batch) {

	}
	
	private void renderGuiLives (SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 155;
		float y = 15;
		
		BitmapFont fpsFont = Assets.instance.fonts.defaultBig;
		
		fpsFont.setColor(0, 1, 0, 1);
		// fpsFont.draw(batch, "Lives: " + worldController.level.returnLives(), x , y);		
		fpsFont.setColor(1, 1, 1, 1); // white
		
	}
	
	private void renderGuiFpsCounter (SpriteBatch batch) {
		float x = cameraGUI.viewportWidth - 155;
		float y = cameraGUI.viewportHeight - 55;
		
		int fps = Gdx.graphics.getFramesPerSecond();
		
		BitmapFont fpsFont = Assets.instance.fonts.defaultBig;
		
		if (fps >= 45) {
			
			fpsFont.setColor(0, 1, 0, 1);
			
		} else if (fps >= 30) {
			
			fpsFont.setColor(1, 1, 0, 1);
			
		} else {
			
			fpsFont.setColor(1, 0, 0, 1);
			
		}
		
		fpsFont.draw(batch, "FPS: " + fps , x, y);
		fpsFont.setColor(1, 1, 1, 1); // white

	}
	
	private void renderGuiGameOverMessage (SpriteBatch batch) {
		float x = cameraGUI.viewportWidth * .5f;
		float y = cameraGUI.viewportHeight * .5f;
		if (worldController.gameOver) {
			BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
			fontGameOver.setColor(0, 1, 0, 1);
			fontGameOver.setColor(1, 1, 1, 1);
		}

	}
	
	private void renderGuiBallImage(SpriteBatch batch) {
		
		float x = cameraGUI.viewportWidth * .5f - 100;
		float y = cameraGUI.viewportHeight - 425;
		
		batch.draw(Assets.instance.ball.ball, x, y, 200, 200, 200, 200, 1, -1, 0);
		
	}
	
	private void renderGuiBallMapPos(SpriteBatch batch) {
		
		float x = 155;
		float y = cameraGUI.viewportHeight - 55;
		
		BitmapFont fpsFont = Assets.instance.fonts.defaultBig;
		fpsFont.draw(batch, "Ball Map Pos: " + worldController.returnCameraCoords() , x, y);
		
	}

	public void resize (int width, int height) {
	
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		
	}
	
	public Vector3 cameraUnproject(Vector3 tsInputs) {
		
		return camera.unproject(tsInputs);
		
	}

	@Override
	public void dispose () {
		batch.dispose();
		
	}

}
