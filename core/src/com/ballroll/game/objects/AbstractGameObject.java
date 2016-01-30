package com.ballroll.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.MathUtils;

public abstract class AbstractGameObject {

	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	
	public Vector2 velocity;
	public Vector2 terminalVelocity;
	public Vector2 friction;
	public Vector2 accleration;
	
	public Rectangle bounds;


	public AbstractGameObject () {
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
		velocity = new Vector2();
		terminalVelocity = new Vector2();
		friction = new Vector2();
		accleration = new Vector2();
		
		bounds = new Rectangle();
		
	}
	
	public void update (float deltaTime) {
		
	}

	public abstract void render (SpriteBatch batch);

}
