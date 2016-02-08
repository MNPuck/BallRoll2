package com.ballroll.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.ballroll.game.Assets;
import com.ballroll.game.Level;
import com.ballroll.game.WorldRenderer;
import com.ballroll.util.Constants;

public class Ball extends AbstractGameObject {
	
	private static final String TAG = Ball.class.getName();
	
	private TextureRegion Ball;
	
	private float moveXAxis;
	private float moveYAxis;
	
	private float slantXAxis;
	private float slantYAxis;
	
	private int ballHeight;
	
	private int saveTileSlant;
	private int saveTileHeight;
	private int saveBallDirection;
	private int saveLayer;
	
	private Vector2 screenPosition;
	private float savePosY;
	
	private boolean bounce;
	
	public Ball (Vector2 ballOrigin) {
		
		setPosition(ballOrigin);
		init();
	}
	
	private void setPosition(Vector2 ballOrigin) {
		
		position.x = ballOrigin.x;
		position.y = ballOrigin.y;
		
	}
	
	private void init() {
		
		dimension.set(.5f,.5f);
		
		bounds.set(0,0, dimension.x, dimension.y);
		
		Ball = Assets.instance.ball.ball;
		
		// init physics values
		
		terminalVelocity.x = 8.0f;
		terminalVelocity.y = 4.0f;
		friction.x = 3f;
		friction.y = 1.5f;
		accleration.x = .25f;
		accleration.y = .25f;
		velocity.x = 0;
		velocity.y = 0;
		
		// move values
		
		moveXAxis = 0;
		moveYAxis = 0;
		
		// slant values
		
		slantXAxis = 0;
		slantYAxis = 0;
		
		// height
		
		ballHeight = 2;
		
		// save tile slant
		
		saveTileSlant = 0;
		
		// save tile height
		
		saveTileHeight = 0;
		
		// save ball direction
		
		saveBallDirection = 0;
		
		// save layer
		saveLayer = 9;
		
		// screen position
		screenPosition = new Vector2();
		
		// bounce
		bounce = false;
		
	}
	
	public void update(float deltaTime, int moveDirection, int dropDistance, int tileSlant, 
					   int antiSlant, int tileHeight, int layer, boolean ballOut) {
		
		// create bounce if ball height is less then tile height or ball is out of bounds
		
		checkForBallOut(moveDirection, ballOut);
		checkForBounce(moveDirection, tileSlant, antiSlant, tileHeight, layer);
		
		if (tileSlant == Constants.NIL)
			applyMovement(deltaTime, moveDirection, dropDistance, tileHeight, layer);
		else
			applySlant(deltaTime, moveDirection, dropDistance, tileSlant, tileHeight, layer, antiSlant);
		
		screenPosition = updateScreenPosition(tileSlant, tileHeight, layer, moveDirection);
		
		// Gdx.app.debug(TAG, "Ball Direction " + returnCurrentDirection());
		// Gdx.app.debug(TAG, "Screen Position " + screenPosition);
		
		saveTileSlant = tileSlant;
		saveTileHeight = tileHeight;
		
		ballHeight = tileHeight * layer;
		
		bounce = false;
		
		saveLayer = layer;
		
	
	}
	
	private void checkForBallOut(int moveDirection, boolean ballOut) {
		
		if (ballOut) {
			
			velocity.x = 0;
			velocity.y = 0;
			
		}
		
		
	}
	
	private void checkForBounce(int moveDirection, int tileSlant, int antiSlant, int tileHeight, int layer) {
		
		if (ballHeight < tileHeight * layer) {
			
			if (tileSlant == Constants.NIL) {
				
				bounce = true;
				
			}
			
			if (returnCurrentDirection() != tileSlant &&
				returnCurrentDirection() != antiSlant) {
				
				bounce = true;
				
			}
			
		}
		
		if (bounce) {
			
			if (moveDirection == Constants.NE)
				moveDirection = Constants.SW;
			
			if (moveDirection == Constants.NW)
				moveDirection = Constants.SE;
			
			if (moveDirection == Constants.SW)
				moveDirection = Constants.NE;
				
			if (moveDirection == Constants.SE)
				moveDirection = Constants.NW;
				
				velocity.x = - velocity.x;
				velocity.y = - velocity.y;
			
		}
		
	}
	
	private void applyMovement(float deltaTime, int moveDirection, int dropDistance, int tileHeight, int layer) {
		
		// reset slant axis
		
		slantXAxis = 0;
		slantYAxis = 0;
		
		// if going down ramp and changing layers, modify y position
		
		if (layer + 1 == saveLayer) {

			if (returnCurrentDirection() == Constants.SE ||
				returnCurrentDirection() == Constants.SW)
				position.y = position.y - 1f;
			
			if (returnCurrentDirection() == Constants.NE ||
				returnCurrentDirection() == Constants.NW)
				position.y = position.y + 1f;

		
		}
		
		// apply player movement
		
		if (moveDirection == Constants.NE) {
			
			moveXAxis += Constants.MOVE_X_AXIS;
			moveYAxis += Constants.MOVE_Y_AXIS;
			
		}
		
		if (moveDirection == Constants.NW) {
			
			moveXAxis += - Constants.MOVE_X_AXIS;
			moveYAxis += Constants.MOVE_Y_AXIS;
			
		}
		
		if (moveDirection == Constants.SE) {
			
			moveXAxis += Constants.MOVE_X_AXIS;
			moveYAxis += - Constants.MOVE_Y_AXIS;
			
		}
		
		if (moveDirection == Constants.SW) {
			
			moveXAxis += - Constants.MOVE_X_AXIS;
			moveYAxis += - Constants.MOVE_Y_AXIS;
			
		}
		
		if (moveDirection == Constants.NIL) {
			
			moveXAxis = 0;
			moveYAxis = 0;
			
		}
		
		applyForce(deltaTime, moveXAxis, moveYAxis);
		
	}
	
	private void applySlant(float deltaTime, int moveDirection, int dropDistance, 
							int tileSlant, int tileHeight, int layer, int antiSlant) {
		
		// reset movement axis
		moveXAxis = 0;
		moveYAxis = 0;	
		
		// save position y if first applying slant
		
		if (saveTileSlant != tileSlant ||
			saveTileHeight != tileHeight) {
			
			if (tileSlant == returnCurrentDirection())
				savePosY = position.y;
			
			if (antiSlant == returnCurrentDirection()) {
				
				savePosY = position.y;
					
			}
			
			saveBallDirection = returnCurrentDirection();
			
		}
		
		// apply slant movement
		
		if (tileSlant == Constants.NE) {
			
			slantXAxis += Constants.SLANT_X_AXIS;
			slantYAxis += Constants.SLANT_Y_AXIS;
			
		}
		
		if (tileSlant == Constants.NW) {
			
			slantXAxis += - Constants.SLANT_X_AXIS;
			slantYAxis += Constants.SLANT_Y_AXIS;
			
		}
		
		if (tileSlant == Constants.SE) {
			
			slantXAxis += Constants.SLANT_X_AXIS;
			slantYAxis += - Constants.SLANT_Y_AXIS;
			
		}
		
		if (tileSlant == Constants.SW) {
			
			slantXAxis += - Constants.SLANT_X_AXIS;
			slantYAxis += - Constants.SLANT_Y_AXIS;
			
		}
		
		applyForce(deltaTime, slantXAxis, slantYAxis);
		
	}
	
	private void applyForce(float deltaTime, float applyXAxis, float applyYAxis) {
		
		if (velocity.x != 0) {
			
			// Apply friction
			if (velocity.x > 0) {
				velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
			} else {
				velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
			}
		}
			
		velocity.x += accleration.x * deltaTime * applyXAxis;
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
		
		position.x += velocity.x * deltaTime;
		
		if (velocity.y != 0) {
				
			// Apply friction
			if (velocity.y > 0) {
				velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
			} else {
				velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
			}
		}
		
		velocity.y += accleration.y * deltaTime * applyYAxis;
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
			
		position.y += velocity.y * deltaTime;	
	
	}
	
	private Vector2 updateScreenPosition(int tileSlant, int tileHeight, int layer, int moveDirection) {
		
		Vector2 returnPosition = new Vector2();
		
		if (bounce) {
			
			returnPosition.x = position.x;
			returnPosition.y = position.y;
			
			return returnPosition;
			
		}
			
		float posYDiff = 0;
		
		if (savePosY > position.y)
			posYDiff = savePosY - position.y;
		else
			posYDiff = position.y - savePosY;
		
		returnPosition.x = position.x;
		
		if (tileSlant == Constants.NIL) {
		
			returnPosition.y = position.y + (tileHeight * .5f);
			return returnPosition;
			
		}
		
		if (tileSlant == saveBallDirection) {
			
			returnPosition.y = position.y + (tileHeight * .5f - posYDiff);	
		
		}
		
		else {
			
			returnPosition.y = position.y + ((tileHeight - 1) * .5f + posYDiff);
			
		}
		
		return returnPosition;
		
	}
	
	public int returnCurrentDirection() {
		
		if (velocity.x > 0 &&
			velocity.y > 0) {
		
			return Constants.NE;
			
		}
		
		if (velocity.x < 0 &&
			velocity.y > 0) {
		
			return Constants.NW;
			
		}
		
		if (velocity.x > 0 &&
			velocity.y < 0) {
			
			return Constants.SE;
			
		}
		
		if (velocity.x < 0 &&
			velocity.y < 0) {
			
			return Constants.SW;
			
		}
		
		return Constants.NIL;
		
	}
	
	public Vector2 returnBallPos() {
		
		return position;
		
	}
	
	public float returnCenterBallPosX() {
	
		float centerX = 0;
		centerX = position.x + dimension.x * .5f;
		return centerX;
		
	}
	
	public float returnCenterBallPosY() {
		
		float centerY = 0;
		centerY = position.y - dimension.y * .5f;
		return centerY;
		
	}
	
	public float returnNEBallPosX() {
		
		float ballX = 0;
		ballX = (position.x + dimension.x * .75f);
		return ballX;
		
	}
	
	public float returnNEBallPosY() {
		
		float ballY = 0;
		ballY = (position.y - dimension.y * .75f);
		return ballY;
		
	}
	
	public float returnNWBallPosX() {
		
		float ballX = 0;
		ballX = (position.x + dimension.x * .25f);
		return ballX;
		
	}
	
	public float returnNWBallPosY() {
		
		float ballY = 0;
		ballY = (position.y - dimension.y * .75f);
		return ballY;
		
	}
	
	public float returnSEBallPosX() {
		
		float ballX = 0;
		ballX = (position.x + dimension.x * .75f);
		return ballX;
		
	}
	
	public float returnSEBallPosY() {
		
		float ballY = 0;
		ballY = (position.y - dimension.y * .25f);
		return ballY;
		
	}
	
	public float returnSWBallPosX() {	
		
		float ballX = 0;
		ballX = (position.x - dimension.x * .25f);
		return ballX;
		
	}
	
	public float returnSWBallPosY() {
		
		float ballY = 0;
		ballY = (position.y - dimension.y * .25f);
		return ballY;
	
	}
	
	public Vector2 returnDimension() {
		
		return dimension;
		
	}
	
	public void setBallPosition(Vector2 screenCoords, int tileSlant, int tileHeight, int layer){
		
		velocity.x = 0;
		velocity.y = 0;
		
		position.x = screenCoords.x;
		position.y = screenCoords.y;
		
		screenPosition = updateScreenPosition(tileSlant, tileHeight, layer, Constants.NIL);	
		
	}
	
	public void render (SpriteBatch batch) {
		
		batch.draw(Ball, screenPosition.x, screenPosition.y, 0, 0, dimension.x, dimension.y, 1, 1, rotation);		
		
	}	
	
}