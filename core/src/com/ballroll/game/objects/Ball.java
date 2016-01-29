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
	
	private int height;
	
	private int saveTileSlant;
	
	private Vector2 screenPosition;
	private float savePosY;
	
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
		
		terminalVelocity.x = 4.0f;
		terminalVelocity.y = 2.0f;
		terminalVelocitySlant.x = 10.0f;
		terminalVelocitySlant.y = 10.0f;
		friction.x = 1f;
		friction.y = .5f;
		frictionSlant.x = 1f;
		frictionSlant.y = 1f;
		accleration.x = 20;
		accleration.y = 20;
		acclerationSlant.x = 5;
		acclerationSlant.y = 5;
		velocity.x = 0;
		velocity.y = 0;
		
		// move values
		
		moveXAxis = 0;
		moveYAxis = 0;
		
		// slant values
		
		slantXAxis = 0;
		slantYAxis = 0;
		
		// height
		
		height = 0;
		
		// save tile slant
		
		saveTileSlant = Constants.NIL;
		
		// screen position
		screenPosition = new Vector2();
		
	}
	
	public void update(float deltaTime, int moveDirection, int dropDistance, int tileSlant, int tileHeight) {
		
		if (tileSlant == Constants.NIL)
			applyMovement(deltaTime, moveDirection, dropDistance, tileSlant);
		else
			applySlant(deltaTime, moveDirection, dropDistance, tileSlant);
		
		screenPosition = updateScreenPosition(tileSlant, tileHeight, moveDirection);
		
	
	}
	
	private void applyMovement(float deltaTime, int moveDirection, int dropDistance, int tileSlant) {
		
		// reset save tile slant
		
		saveTileSlant = Constants.NIL;
		
		// reset slant axis
		
		slantXAxis = 0;
		slantYAxis = 0;
		
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
		
		/*
		
		// apply down force for straight down drop to lower tile
		
		if (dropDistance > 0 &&
			tileSlant == Constants.NIL) {
			
			position.y += -Constants.WORLD_TO_BOX;
			
		}
		
		*/
		
	}
	
	private void applySlant(float deltaTime, int moveDirection, int dropDistance, int tileSlant) {
		
		// reset movement axis
		moveXAxis = 0;
		moveYAxis = 0;
		
		// save position y if first applying slant
		
		if (saveTileSlant != tileSlant) {
			
			savePosY = position.y;
			
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
		
		saveTileSlant = tileSlant;
		
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
	
	private Vector2 updateScreenPosition(int tileSlant, int tileHeight, int moveDirection) {
		
		Vector2 returnPosition = new Vector2();
		float posYDiff = 0;
		
		if (savePosY > position.y)
			posYDiff = savePosY - position.y;
		else
			posYDiff = position.y - savePosY;
		
		returnPosition.x = position.x;
		
		if (tileSlant == Constants.NIL)
			returnPosition.y = position.y + (tileHeight * .5f);
		
		if (tileSlant == Constants.NW)
			returnPosition.y = position.y + (tileHeight * .5f - posYDiff);
		
		if (tileSlant == Constants.NE)
			returnPosition.y = position.y + (tileHeight * .5f - posYDiff);
		
		if (tileSlant == Constants.SW)
			returnPosition.y = position.y + (tileHeight * .5f - posYDiff);
		
		if (tileSlant == Constants.SE)
			returnPosition.y = position.y + (tileHeight * .5f - posYDiff);
		
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
	
	public void setBallPosition(Vector2 screenCoords, int tileSlant, int tileHeight){
		
		velocity.x = 0;
		velocity.y = 0;
		
		position.x = screenCoords.x;
		position.y = screenCoords.y;
		
		screenPosition = updateScreenPosition(tileSlant, tileHeight, Constants.NIL);
		
		
	}
	
	public void render (SpriteBatch batch) {
		
		batch.draw(Ball, screenPosition.x, screenPosition.y, 0, 0, dimension.x, dimension.y, 1, 1, rotation);		
		
	}	
	
}