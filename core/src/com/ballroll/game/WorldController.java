package com.ballroll.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.ballroll.game.objects.Tile;
import com.ballroll.game.objects.Ball;
import com.ballroll.screens.DirectedGame;
import com.ballroll.util.CameraHelper;
import com.ballroll.util.Constants;


public class WorldController extends InputAdapter {

	private static final String TAG = WorldController.class.getName();
	
	private DirectedGame game;
	public Level level;
	
	private boolean startPressed;
	private boolean selectPressed;
	public boolean escPressed;
	public boolean gameOver;
	
	private boolean isLeftClicked;
	private boolean isRightClicked;
	private boolean isSpacePressed;
	private int isScrolled;
	
	private Vector2 scrCoords;
	
	private float tsXAxis;
	private float tsYAxis;
	
	private int levelNumber;
	
	public CameraHelper cameraHelper;
	
	Vector2 cameraPosition;
	
	// Rectangles for collision detection
	private Rectangle r1 = new Rectangle();
	private Rectangle r2 = new Rectangle();
	
	// vertices to create polygon from for collision detection
	public float[] vehicleVertices = new float[8];
	public float[] roadBoxVertices = new float[8];
	
	// polygons used for collision detection
	private Polygon vehiclePoly = new Polygon();
	private Polygon roadBoxPoly = new Polygon();
	
	// Save collision rectangle for future comparisons
	private Rectangle saveRect = new Rectangle();
	
	// save Rectangle array
	public Array<Rectangle> saveRects;
	
	// first time flag to set camera start position
	boolean firstTime;

	public WorldController (DirectedGame game) {
		this.game = game;
		init();
	}

	private void init () {
		
		Gdx.input.setInputProcessor(this);
		Gdx.input.setCatchBackKey(true);
		
		cameraHelper = new CameraHelper();
		
		switch (Gdx.app.getType()) {
		
			case Desktop: 

				break;
				
			case Android:
				tsXAxis = 0;
				break;
				
			default:
				break;
		
		}
		
		startPressed = false;
		selectPressed = false;
		gameOver = false;
		escPressed = false;
		
		isLeftClicked = false;
		isRightClicked = false;
		isSpacePressed = false;
		isScrolled = 0;
		
		levelNumber = 1;
		
		firstTime = true;
		
		initLevel();
		
	}
	
	private void initLevel() {
		
		level = new Level(levelNumber);
		
		cameraHelper.setTarget(level.ball);
		
		saveRects = new Array<Rectangle>();
		
		scrCoords = new Vector2(0,0);
	
	}
	
	public boolean isGameOver () {
		return gameOver;
	}
	
	public boolean isEscPressed () {
		return escPressed;
	}
	
	public boolean isStartPressed() {
		return startPressed;
	}
	
	public boolean isSelectPressed() {
		return selectPressed;
	}
	
	public void update (float deltaTime, WorldRenderer worldRenderer) {
		
		handleDebugInput(deltaTime);
		
		if (!level.returnIsGameOver() &&
			!gameOver) {
		
			switch (Gdx.app.getType()) {
		
				case Desktop: 
					readUserInput(worldRenderer);
					break;
			
				case Android:
					readScreenInput(worldRenderer);
					break;
			
				default:
					break;
	
			}
			
			if (!gameOver) {
				
				cameraHelper.update(deltaTime);
				cameraPosition = cameraHelper.getPosition();
			
				level.update(deltaTime, isLeftClicked, isRightClicked, scrCoords);
				
				// reset input is booleans
				
				isSpacePressed = false;
				isLeftClicked = false;
				isRightClicked = false;
				isScrolled = 0;
		
			}
		
		}
		
		switch (Gdx.app.getType()) {
		
		case Desktop: 
			checkEscKey();
			break;
		
		case Android:
			break;
		
		default:
			break;
				
		}
		
		testCollisions();
	
	}
	
	private void handleDebugInput (float deltaTime) {
		if (Gdx.app.getType() != ApplicationType.Desktop) return;

		// Camera Controls (move)
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.LEFT))moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);

		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
	}
	
	private void moveCamera (float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}
	
	private void readUserInput(WorldRenderer worldRenderer) {
		
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			
			isLeftClicked = true;
			isRightClicked = false;
			
			scrCoords.x = Gdx.input.getX();
			scrCoords.y = Gdx.input.getY();
	
		}
		
		if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			
			isRightClicked = true;
			isLeftClicked = false;
			
			float coordX = Gdx.input.getX();
			float coordY = Gdx.input.getY();
			
			Vector3 convInput;
			convInput = new Vector3(coordX, coordY, 0f);
			
			Vector3 convOutput;
			convOutput = worldRenderer.cameraUnproject(convInput);
			
			scrCoords.x = convOutput.x;
			scrCoords.y = convOutput.y;
			
		}
		
		if (!Gdx.input.isButtonPressed(Buttons.LEFT) &&
			!Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			
			isLeftClicked = false;
			isRightClicked = false;
			
			scrCoords.x = 0;
			scrCoords.y = 0;
			
		}

	}
	
	private void readScreenInput(WorldRenderer worldRenderer) {
	
		// set up screen touch
		
		if (Gdx.input.isTouched()) {
			
			isLeftClicked = true;
			
			scrCoords.x = Gdx.input.getX();
			scrCoords.y = Gdx.input.getY();
				
		}
		
		if (!Gdx.input.isTouched()) {
				
			isLeftClicked = false;
			
			scrCoords.x = 0;
			scrCoords.y = 0;
			
		}
				
	}
	
	public Vector2 returnCameraCoords() {
		
		return cameraHelper.getPosition();
		
	}
	
	private void testCollisions() {

	}
	
	private void checkEscKey() {
		
		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE))
				escPressed = true;

	}
	
	@Override
	public boolean keyDown (int keycode) {
		// Reset game world
		if (keycode == Keys.BACK) {
			selectPressed = true;
		}
		return false;
		
	}

	@Override
	public boolean keyUp (int keycode) {
		// Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		
		// Toggle camera follow
		if (keycode == Keys.ENTER) {
		
		}
		
		// Space pressed
		
		if (keycode == Keys.SPACE) {
		
			isSpacePressed = true;
			
		}
	
		return false;
		
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		
		/*
		
		if (Gdx.app.getType() == ApplicationType.Desktop) {
			
			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				
				isLeftClicked = true;
				
				scrCoords.x = Gdx.input.getX();
				scrCoords.y = Gdx.input.getY();
		
			}
			
			else {
				
				isLeftClicked = false;
				isRightClicked = false;
				scrCoords.x = 0;
				scrCoords.y = 0;
				
			}
			
		}
		
		*/
		
		return false;
		
	}
	
	@Override
	public boolean scrolled(int amount) {
		
		isScrolled = amount;
		
		return false;
		
	}
	
}
