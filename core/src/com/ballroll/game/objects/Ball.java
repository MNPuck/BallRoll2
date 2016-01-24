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
		
	}
	
	public void update(float deltaTime, int moveDirection, int dropDistance, int tileSlant) {
		
		if (tileSlant == Constants.NIL)
			applyMovement(deltaTime, moveDirection, dropDistance, tileSlant);
		else
			applySlant(deltaTime, moveDirection, dropDistance, tileSlant);
		
	
	}
	
	private void applyMovement(float deltaTime, int moveDirection, int dropDistance, int tileSlant) {
		
		
		// if ball just moved down ramp even out the velocity
		
		if (saveTileSlant == Constants.NE) {
			
			velocity.x = velocity.y * 2;
			
		}
		
		if (saveTileSlant == Constants.NW) {
			
			velocity.x = velocity.y * - 2;
			
		}
		
		if (saveTileSlant == Constants.SE) {
			
			velocity.x = velocity.y * - 2;
			
		}
		
		if (saveTileSlant == Constants.SW) {
			
			velocity.x = velocity.y * 2;
			
		}
		
		// reset save tile slant
		
		saveTileSlant = Constants.NIL;
		
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

		if (velocity.x != 0) {
			
			// Apply friction
			if (velocity.x > 0) {
				velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
			} else {
				velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
			}
		}
			
		velocity.x += accleration.x * deltaTime * moveXAxis;
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
		
		velocity.y += accleration.y * deltaTime * moveYAxis;
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
			
		position.y += velocity.y * deltaTime;	
		
		// apply down force for straight down drop to lower tile
		
		if (dropDistance > 0 &&
			tileSlant == Constants.NIL) {
			
			position.y += -Constants.WORLD_TO_BOX;
			
		}
		
	}
	
	private void applySlant(float deltaTime, int moveDirection, int dropDistance, int tileSlant) {
		
		// if first time applying slant: reset slant axis and even out velocities
		
		if (tileSlant != saveTileSlant) {
			
			slantXAxis = 0;
			slantYAxis = 0;
			
			if (tileSlant == Constants.NE) {
				
				if (velocity.x > velocity.y)
					velocity.y = velocity.x;
				
				if (velocity.y > velocity.x)
					velocity.x = velocity.y;
				
			}
			
			if (tileSlant == Constants.NW) {
				
				if (velocity.x * -1 > velocity.y)
					velocity.y = velocity.x * -1;
				
				if (velocity.y * -1 < velocity.x)
					velocity.x = velocity.y * - 1;
				
			}
			
			if (tileSlant == Constants.SE) {
		
				if (velocity.x * -1 < velocity.y)
					velocity.y = velocity.x * -1;
				
				if (velocity.y * -1 < velocity.x)
					velocity.x = velocity.y * - 1; 
		
			}
			
			if (tileSlant == Constants.SW) {
				
				if (velocity.x < velocity.y)
					velocity.y = velocity.x;
				
				if (velocity.y < velocity.x)
					velocity.x = velocity.y;
				
			}	
			
		}
	
		
		// apply slant movement based on what tile the ball is touching
		
		if (tileSlant == Constants.NE) {
			
			slantXAxis += Constants.SLANT_X_AXIS;
			slantYAxis += - Constants.SLANT_Y_AXIS;
			
		}
		
		if (tileSlant == Constants.NW) {
			
			slantXAxis += - Constants.SLANT_X_AXIS;
			slantYAxis += - Constants.SLANT_Y_AXIS;
			
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
		
		if (velocity.x != 0) {
			
			// Apply friction
			if (velocity.x > 0) {
				velocity.x = Math.max(velocity.x - frictionSlant.x * deltaTime, 0);
			} else {
				velocity.x = Math.min(velocity.x + frictionSlant.x * deltaTime, 0);
			}
			
		}
		
		velocity.x += acclerationSlant.x * deltaTime * slantXAxis;
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocitySlant.x, terminalVelocitySlant.x);
		
		
		if (velocity.y != 0) {
			
			// Apply friction
			if (velocity.y > 0) {
				velocity.y = Math.max(velocity.y - frictionSlant.y * deltaTime, 0);
			} else {
				velocity.y = Math.min(velocity.y + frictionSlant.y * deltaTime, 0);
			}
			
		}
		
		velocity.y += acclerationSlant.y * deltaTime * slantYAxis;
	  	velocity.y = MathUtils.clamp(velocity.y, -terminalVelocitySlant.y, terminalVelocitySlant.y);
	  	
	  	/*
		
		if (tileSlant == Constants.NE &&
			returnCurrentDirection() == Constants.NE) {
			
			velocity.y = velocity.x;
			
		}
		
		if (tileSlant == Constants.NW) {
			
			velocity.y =  - velocity.x;
			
		}
		
		if (tileSlant == Constants.SE) {
			
			velocity.y = - velocity.x;
			
		}
		
		if (tileSlant == Constants.SW) {
			
			velocity.y = velocity.x;
			
		}
		
	    */	
		  	
	  	position.x += velocity.x * deltaTime;
	  	position.y += velocity.y * deltaTime;
	
		
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
	
	public void setBallPosition(Vector2 screenCoords){
		
		velocity.x = 0;
		velocity.y = 0;
		
		position.x = screenCoords.x;
		position.y = screenCoords.y;
		
	}
	
	public void render (SpriteBatch batch) {
		
		batch.draw(Ball, position.x, position.y, 0, 0, dimension.x, dimension.y, 1, 1, rotation);		
		
	}	
	
}