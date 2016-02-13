package com.ballroll.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TmxMapLoader.Parameters;

import com.ballroll.game.objects.Ball;
import com.ballroll.game.objects.Tile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ballroll.util.AudioManager;
import com.ballroll.util.Constants;
import com.ballroll.game.Assets;
import com.ballroll.util.CameraHelper;
import java.math.*;

public class Level {

	public static final String TAG = Level.class.getName();
	
	// ball single
	public Ball ball;
	
	// map box pos
	public Vector2 mapBoxPos;
	
	// variable to store level number
	public int levelNumber;
	
	// variable to store score
	public int score;
	
	// variable to see if this is new game
	private boolean newGame; 

	// game over boolean
	public boolean isGameOver;
	
	// particle pool 1
	public ParticleEffectPool pool1;
	public Array<PooledEffect> activeEffects1;
	
	// particle pool 2
	public ParticleEffectPool pool2;
	public Array<PooledEffect> activeEffects2;
	
	// tiled map
	public TiledMap map;
	
	public MapObjects mapObjects;
	public MapLayer mapObjectLayer;
	
	// Center of map
	private float mapCenterX;
	private float mapCenterY;
	
	// Top of map
	private float mapTopX;
	private float mapTopY;
	
	//Tile map layers
	TiledMapTileLayer layer1;
	TiledMapTileLayer layer2;
	TiledMapTileLayer layer3;
	TiledMapTileLayer layer4;
	TiledMapTileLayer layer5;
	
	// Input mode: 0 = move unit, 1 = build
	private int inputMode;
	
	// click/touch start and end location
	private int touchStart;
	private int touchEnd;
	
	// move direction
	private int moveDirection;
	
	// save move direction
	private int currentMoveDirection;
	
	// drop direction
	private int dropDirection;
	
	// drop distance
	private int dropDistance;
	
	// ball map position
	private Vector2 ballMapPos;
	
	// ball screen position
	private Vector2 ballScreenPos;
	
	// movement type (1 = touch, 2 = swipe)
	private int movementType;
	
	// tile height
	private int tileHeight;
	
	// current layer
	private int currentLayer;
	
	// save layer
	private int saveLayer;
	
	// save ball height
	private int saveBallHeight;
	
	// current slant 
	private int tileSlant;
	
	// current anti slant
	private int antiSlant;
	
	// input slant 1 int
	private int inputSlant1;
	
	// input slnat 2 int
	private int inputSlant2;
	
	// first update 
	private boolean firstUpdate;
	
	// ball out of bounds
	private boolean ballOut;
	
	public Level (int levelNumberIn) {

		init(levelNumberIn);
	}
		
	private void init(int levelNumberIn) {
		
		// level number
		levelNumber = levelNumberIn;
		
		ballScreenPos = new Vector2(0f,-1f);
		// ballScreenPos.y += .50f;
		
		// Ball
		ball = new Ball(ballScreenPos);
		
		// set to new game
		newGame = true;
		
		// set score to 0
		score = 0;
		
		// level game over init
		isGameOver = false;
		
		loadLevel();

		// load tiled map
		map = new TmxMapLoader().load("maps/IsoTest.tmx");
		
		// fetch tiled map layers
		layer1 = (TiledMapTileLayer) map.getLayers().get(0);
		// layer2 = (TiledMapTileLayer) map.getLayers().get(1);
		// layer3 = (TiledMapTileLayer) map.getLayers().get(2);
		// layer4 = (TiledMapTileLayer) map.getLayers().get(3);
		// layer5 = (TiledMapTileLayer) map.getLayers().get(4);
		
		// click touch start and end
		
		touchStart = 0;
		touchEnd = 0;
		
		// set move diretions to nil
		moveDirection = Constants.NIL;
		currentMoveDirection = Constants.NIL;
		
		// set drop directions to nil
		dropDirection = Constants.NIL;
		 
		// set drop distance to 0
		dropDistance = 0;
		
		// ball map pos
		ballMapPos = new Vector2(0,0);
		
		// movement type (read from prefs eventuaully)
		movementType = 1;
		
		// tile heights
		tileHeight = 0;
		saveBallHeight = 0;
		
		// slant
		tileSlant = Constants.NIL;
		inputSlant1 = Constants.NIL;
		inputSlant2 = Constants.NIL;
		
		// anti slant
		antiSlant = Constants.NIL;
		
		// set first update 
		firstUpdate = true;
		
		// set ball out to false
		ballOut = false;
		
		// set current layer to 1
		currentLayer = 1;
		
		// set save layer to 9
		saveLayer = 9;
	
	}
	
	public float returnMapCenterX() {
		
		return mapCenterX;
		
	}
	
	public float returnMapCenterY() {
		
		return mapCenterY;
		
	}

	public float returnMapTopX() {
		
		return mapTopX;
		
	}
	
	public float returnMapTopY() {
		
		return mapTopY;
		
	}
	
	public void update (float deltaTime, boolean isLeftClicked, boolean isRightClicked, Vector2 scrCoords) {
		
		if (isRightClicked) {
			
			ball.setBallPosition(scrCoords, tileSlant, tileHeight, currentLayer);
			
		}
		
		switch (movementType) {
		
			case 1: 
				updateBallMovementTouch(isLeftClicked, scrCoords);
				break;
		
			case 2:
				updateBallMovementSwipe(isLeftClicked, scrCoords);
				break;
		
			default:
				break;
				
		}
		
		ballUpdate(deltaTime);
		
		
		if (ball.returnCurrentDirection() != Constants.NIL) {
			
			currentMoveDirection = ball.returnCurrentDirection();
			
		}
		
		// dec drop distance
		
		if (dropDistance > 0) {
			
			dropDistance--;
			
		}
		
		// ballScreenPos.x = assignBallScreenPosX(currentMoveDirection, ballHeight);
		// ballScreenPos.y = assignBallScreenPosY(currentMoveDirection, ballHeight);
		
		ballScreenPos.x = ball.returnCenterBallPosX();
		ballScreenPos.y = ball.returnCenterBallPosY();
		
		ballMapPos = screenToMap(ballScreenPos, false);
	
		int mapX = (int) ballMapPos.x;
		int mapY =  Constants.MAP_HEIGHT - (int) ballMapPos.y - 1;
		
		float remX = ballMapPos.x - mapX;
		float remY = ((Constants.MAP_HEIGHT - ballMapPos.y - 1) - mapY) * -1;
		
		// get layer 2 cell
		
		/*
		
		Cell cell = layer2.getCell(mapX, mapY);
		
		if (cell != null) {
			
			currentLayer = 2;
			fetchCell(cell, currentLayer);
			ballOut = false;
			
		}
		
		else {
		
		*/

			// get layer 1 cell

			Cell cell = layer1.getCell(mapX, mapY);
			
			/*
			
			if (saveLayer == currentLayer + 1)
				cell = layer1.getCell(mapX + 1, mapY);
			
			if (saveLayer == currentLayer - 1)
				cell = layer1.getCell(mapX - 1, mapY);
				
			*/
		
			if (cell != null) {
			
				currentLayer = 1;
				fetchCell(cell, currentLayer, remX, remY);
				ballOut = false;

			}
		
			else {
			
				ballOut = true;
			
			}
		
		saveLayer = currentLayer;
		
		if (firstUpdate) {
			
			saveBallHeight = tileHeight;
			firstUpdate = false;
			
			
		}
		
	}	
	
	private void updateBallMovementTouch(boolean isLeftClicked, Vector2 scrCoords) {
		
		boolean right = false;
		boolean left = false;
		boolean up = false;
		boolean down = false;
		
		if (isLeftClicked &&
			scrCoords.x != 0 &&
			scrCoords.y != 0) {
			
			right = isTouchRight(scrCoords);		
			left = isTouchLeft(scrCoords);
			up = isTouchUp(scrCoords);
			down = isTouchDown(scrCoords);
			
			if (up && right)
				moveDirection = Constants.NE;
			
			if (up && left)
				moveDirection = Constants.NW;
				
			if (down && right)
				moveDirection = Constants.SE;
				
			if (down && left)
				moveDirection = Constants.SW;
				
		}
		
	}
	
	private void updateBallMovementSwipe(boolean isLeftClicked, Vector2 scrCoords) {
		
		boolean right = false;
		boolean left = false;
		boolean up = false;
		boolean down = false;
		
		if (isLeftClicked &&
			scrCoords.x != 0 &&
			scrCoords.y != 0 &&
			touchStart == 0) {
			
			// assign new touch start area
			 
			right = isTouchRight(scrCoords);		
			left = isTouchLeft(scrCoords);
			up = isTouchUp(scrCoords);
			down = isTouchDown(scrCoords);
			
			if (up && right)
				touchStart = Constants.NE;
			
			if (up && left)
				touchStart = Constants.NW;
				
			if (down && right)
				touchStart = Constants.SE;
				
			if (down && left)
				touchStart = Constants.SW;
			
		}
		
		// assign end touch area
		
		if (touchStart != 0) {
			
			right = isTouchRight(scrCoords);		
			left = isTouchLeft(scrCoords);
			up = isTouchUp(scrCoords);
			down = isTouchDown(scrCoords);
			
			if (up && right)
				touchEnd = Constants.NE;
			
			if (up && left)
				touchEnd = Constants.NW;
			
			if (down && right)
				touchEnd = Constants.SE;
			
			if (down && left)
				touchEnd = Constants.SW;
			
		}
		
		// if touch has ended and touch start and end have values, see the direction
		
		if (!isLeftClicked &&
			touchStart != 0 &&
			touchEnd != 0) {
			
			moveDirection = Constants.NIL;
			
			if (touchStart == Constants.NE &&
				touchEnd == Constants.SW)
				moveDirection = Constants.SW;
			
			if (touchStart == Constants.NW &&
				touchEnd == Constants.SE)
				moveDirection = Constants.SE;
			
			if (touchStart == Constants.SE &&
				touchEnd == Constants.NW)
				moveDirection = Constants.NW;
			
			if (touchStart == Constants.SW &&
				touchEnd == Constants.NE)
				moveDirection = Constants.NE;
			
			touchStart = 0;
			touchEnd = 0;
			
		}
		
	}
	
	private boolean isTouchLeft(Vector2 scrCoords) {
		
		if (scrCoords.x < Constants.VIEWPORT_GUI_WIDTH * .5f)
			
			return true;
		
		return false;
			
	}
	
	private boolean isTouchRight(Vector2 scrCoords) {
		
		if (scrCoords.x > Constants.VIEWPORT_GUI_WIDTH * .5f)
			
			return true;
		
		return false;
			
	}
	
	private boolean isTouchUp(Vector2 scrCoords) {
		
		if (scrCoords.y < Constants.VIEWPORT_GUI_HEIGHT * .5f)
			
			return true;
		
		return false;
			
	}
	
	private boolean isTouchDown(Vector2 scrCoords) {
		
		if (scrCoords.y > Constants.VIEWPORT_GUI_HEIGHT * .5f)
			
			return true;
		
		return false;
			
	}
	
	/*
	
	private boolean isTouchLeft(Vector2 scrCoords) {
		
		if (scrCoords.x > Constants.VIEWPORT_GUI_WIDTH * .5f - 100 &&
			scrCoords.x < Constants.VIEWPORT_GUI_WIDTH * .5f &&
			scrCoords.y > Constants.VIEWPORT_GUI_HEIGHT - 250 &&
			scrCoords.y < Constants.VIEWPORT_GUI_HEIGHT - 0)
			
			return true;
		
		return false;
			
	}
	
	private boolean isTouchRight(Vector2 scrCoords) {
			
		if (scrCoords.x > Constants.VIEWPORT_GUI_WIDTH * .5f &&
			scrCoords.x < Constants.VIEWPORT_GUI_WIDTH * .5f + 100 &&
			scrCoords.y > Constants.VIEWPORT_GUI_HEIGHT - 250 &&
			scrCoords.y < Constants.VIEWPORT_GUI_HEIGHT - 0)
			
			return true;
		
		return false;
			
	}
	
	private boolean isTouchUp(Vector2 scrCoords) {
		
		if (scrCoords.x > Constants.VIEWPORT_GUI_WIDTH * .5f - 100 &&
			scrCoords.x < Constants.VIEWPORT_GUI_WIDTH * .5f + 100 &&
			scrCoords.y > Constants.VIEWPORT_GUI_HEIGHT - 250 &&
			scrCoords.y < Constants.VIEWPORT_GUI_HEIGHT - 125)
			
			return true;
		
		return false;
			
	}
	
	private boolean isTouchDown(Vector2 scrCoords) {
		
		if (scrCoords.x > Constants.VIEWPORT_GUI_WIDTH * .5f - 100 &&
			scrCoords.x < Constants.VIEWPORT_GUI_WIDTH * .5f + 100 &&
			scrCoords.y > Constants.VIEWPORT_GUI_HEIGHT - 125 &&
			scrCoords.y < Constants.VIEWPORT_GUI_HEIGHT - 0)
			
			return true;
		
		return false;
			
	}
	
	*/
	
	private void ballUpdate(float deltaTime) {
		
		// ball update
		
		ball.update(deltaTime, moveDirection, dropDistance, tileSlant, antiSlant, tileHeight, currentLayer, ballOut);
		moveDirection = Constants.NIL;
	
	}
	
	private float assignBallScreenPosX(int direction, int ballHeight) {
		
		float returnX = 0;
		
		switch (direction) {
		
			case Constants.NIL:
				returnX = ball.returnCenterBallPosX();
				break;
			
			case Constants.NW:
				returnX = ball.returnNWBallPosX();
				break;
				
			case Constants.NE:
				returnX = ball.returnNEBallPosX();
				break;
				
			case Constants.SW:
				returnX = ball.returnSWBallPosX();
				break;
				
			case Constants.SE:
				returnX = ball.returnSEBallPosX();
				break;
			
			default:
				returnX = ball.returnCenterBallPosX();
				break;
		
		}
		
		return returnX;
		
	}
	
	private float assignBallScreenPosY(int direction, int ballHeight) {
		
		float returnY = 0;
			
		switch (direction) {
		
			case Constants.NIL:
				returnY = ball.returnCenterBallPosY();
				break;
				
			case Constants.NW:
				returnY = ball.returnNWBallPosY();
				returnY = returnY - (ballHeight * .5f);
				break;
				
			case Constants.NE:
				returnY = ball.returnNEBallPosY();
				returnY = returnY - (ballHeight * .5f);
				break;
				
			case Constants.SW:
				returnY = ball.returnSWBallPosY();
				returnY = returnY - (ballHeight * .5f);
				break;
				
			case Constants.SE:
				returnY = ball.returnSEBallPosY();
				returnY = returnY - (ballHeight * .5f);
				break;
				
			default:
				returnY = ball.returnCenterBallPosY();
				break;
	
		}
		
		return returnY;
		
	}
	
	private Vector2 screenToMap(Vector2 position, boolean clamp) {
		
		Vector2 mapPos = new Vector2(0,0);
		
		float Width = 2;
		float Height = 1;
		
		float screenPosX = position.x;
		float screenPosY = - position.y;
		
		// calculate map position
		
		mapPos.x = (screenPosX / Width + screenPosY / Height);
		mapPos.y = (screenPosY / Height - screenPosX / Width);
		
		// limit map positions to map dimensions
		
		if (clamp) {
		
			mapPos.x = MathUtils.clamp(mapPos.x, 0, Constants.MAP_WIDTH - 1);
			mapPos.y = MathUtils.clamp(mapPos.y, 0, Constants.MAP_HEIGHT - 1);
		}

		return mapPos;
		
	}
	
	private int getAntiSlant(int slant) {

		if (slant == Constants.NIL)
			return Constants.NIL;
		
		if (slant == Constants.NW)
			return Constants.SE;
		
		if (slant == Constants.NE)
			return Constants.SW;
		
		if (slant == Constants.SW)
			return Constants.NE;
		
		if (slant == Constants.SE)
			return Constants.NW;
		
		return Constants.NIL;
				
	}
	
	private void fetchCell(Cell cell, int layer, float remX, float remY) {
		
		int cellId = cell.getTile().getId();
		
		Gdx.app.debug(TAG, "Cell Id " + cellId);
		
		String height = (String) cell.getTile().getProperties().get("Height");
		tileHeight = Integer.parseInt(height.trim());
			
		String slant1 = (String) cell.getTile().getProperties().get("Slant1");
		inputSlant1 = Integer.parseInt(slant1.trim());
		
		String slant2 = (String) cell.getTile().getProperties().get("Slant2");
		inputSlant2 = Integer.parseInt(slant2.trim());
		
		if (inputSlant2 == Constants.NIL) {
			
			tileSlant = inputSlant1;
			
		}
		
		if (cellId == 13 ||
			cellId == 16) {
			
			if (remX <= .5f)
				tileSlant = inputSlant1;
			else
				tileSlant = inputSlant2;
				
		}
		
		if (cellId == 14 ||
			cellId == 15) {
			
			if (remY <= .5f)
				tileSlant = inputSlant1;
			else
				tileSlant = inputSlant2;
			
		}
		
		antiSlant = getAntiSlant(tileSlant);
		
	}
	
	private void loadLevel() {
		
		if (newGame) {
			
			newGame = false;
			
		}
			
	}
	
	public int returnLevelNumber() {
		
		return levelNumber;
		
	}
	
	public int returnScore() {
		
		return score;
		
	}
	
	public boolean returnIsGameOver() {
		
		return isGameOver;
	}
	
	public int returnInputMode() {
		
		return inputMode;
				
	}
	
	public void render (SpriteBatch batch, float deltaTime) {
		
		ball.render(batch);
		
	}

	// render for just the collision boxes
	
	public void renderShapes (ShapeRenderer shapeRenderer, float deltaTime) {
		
		// draw clickbox
		
		/*
		
		shapeRenderer.setColor(Color.ORANGE);
		
		for (ClickBox roadBox : roadBoxes)
			shapeRenderer.polygon(roadBox.rectangleToVertices());
			
		*/
		
	}

}
