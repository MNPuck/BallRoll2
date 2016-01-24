package com.ballroll.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.ballroll.game.Assets;
import com.ballroll.game.WorldRenderer;
import com.ballroll.util.Constants;

public class Tile extends AbstractGameObject {

	private static final String TAG = WorldRenderer.class.getName();
	
	private TextureRegion Tile;
	
	// box size
	private float boxXSize;
	private float boxYSize;
	
	// location in map coordinates
	private int mapX;
	private int mapY;
	
	// layer of tile
	private int layer;
	
	// height of tile
	private int height;
	
	// slant direction of tile
	private int slant;
	
	public Tile (int mapXIn, int mapYIn, int heightIn, int slantIn, int layerIn) {
		
		setMapPosition(mapXIn, mapYIn);
		init(heightIn, slantIn, layerIn);
		
	}
	
	private void setMapPosition(int mapXIn, int mapYIn) {
		
		mapX = mapXIn;
		mapY = Constants.MAP_HEIGHT - mapYIn - 1;
		
	}
	
	private void init(int heightIn, int slantIn, int layerIn) {
			
		dimension.set(Constants.TILEXSIZE, Constants.TILEYSIZE);
		
		bounds.set(0,0, dimension.x, dimension.y);
		
		origin.set((float) (position.x * .5), (float) (position.y * .5));
		
		rotation = 0;
		
		height = heightIn;
		
		slant = slantIn;
		
		layer = layerIn;
		
	}
	
	public void update(float deltaTime) {
		
	
	}
	
	public int getHeight() {
		
		return height;
		
	}
	
	public int getMapPositionX() {
		
		return mapX;
		
	}
	
	public int getMapPositionY() {
		
		return mapY;
		
	}
	
	public void putPosition(Vector2 newPos) {
		
		position.x = newPos.x * Constants.WORLD_TO_BOX;
		position.y = newPos.y * Constants.WORLD_TO_BOX;
		
	}
	
	public void putMapPosition(int mapXIn, int mapYIn) {
		
		mapX = mapXIn;
		mapY = Constants.MAP_HEIGHT - mapYIn - 1;
		
	}
	
	
	public void render (SpriteBatch batch) {
		
		position.x = position.x + boxXSize;
		position.y = position.y + boxYSize;
			
		batch.draw(Tile, position.x, position.y, 0, 0, dimension.x, dimension.y, 1, 1, rotation);		
		
	}
		
}
