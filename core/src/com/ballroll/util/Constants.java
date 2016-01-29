package com.ballroll.util;

public class Constants {

	// Visible game world is 16 meters wide
	public static final float VIEWPORT_WIDTH = 16.0f;

	// Visible game world is 9 meters tall
	public static final float VIEWPORT_HEIGHT = 9.0f;
	
	// GameWorld Border
	public static final float GAMEBOARD_WIDTH = 16.0f;
	public static final float GAMEBOARD_HEIGHT = 9.0f;
	
	// Level Size Input Size
	public static final float LEVEL_INPUT_WIDTH = 9.0f;
	public static final float LEVEL_INPUT_HEIGHT = 128.0f;

	// GUI Width
	public static final float VIEWPORT_GUI_WIDTH = 1920.0f;

	// GUI Height
	public static final float VIEWPORT_GUI_HEIGHT = 1080.0f;

	// Location of description file for texture atlas
	public static final String TEXTURE_ATLAS_OBJECTS = 
	"images/BallRoll.pack.atlas";
	
	// Number of Levels
	public static final int NUMBEROFLEVELS = 2;

	// Delay after game over
	public static final float TIME_DELAY_GAME_OVER = 3;
	
	// directions used for movement
	
	public static final int NUM_DIRS = 9;
	public static final int N = 0;  // north, etc going clockwise
	public static final int NE = 1;
	public static final int E = 2;
	public static final int SE = 3;
	public static final int S = 4;
	public static final int SW = 5;
	public static final int W = 6;
	public static final int NW = 7;
	public static final int NIL = 8;
	
	// Number of tiles
	public static final int NUMBEROFTILES = 10;
	
	// Size of highlight box
	public static final float HIGHLIGHTBOXXSIZE = 1f;
	public static final float HIGHLIGHTBOXYSIZE = 1f;
	
	// Size of tile box
	public static final float TILEXSIZE = 1f;
	public static final float TILEYSIZE = 1f;
	
	// Size of turnbox
	public static final float ROADBOXXSIZE = .10f;
	public static final float ROADBOXYSIZE = .10f;
	
	// Vehicle turn delay
	public static final float VEHICLETURNDELAY = .05f;
	
	// Game preferences file
	public static final String PREFERENCES = "AirCommander.prefs";
	
	// World to Box
	public static final float WORLD_TO_BOX = .0083f;
	public static final float BOX_TO_WORLD = 120f;
	
	// Highlight Box Size
	public static final int HB_SIZE_WIDTH = 64;
	public static final int HB_SIZE_HEIGHT = 64;
	
	// Map Dimensions
	public static final int MAP_WIDTH = 16;
	public static final int MAP_HEIGHT = 16;
	
	// Particle effect scale 1
	public static final float PARTICLESCALE1 = 0.0075f;
	
	// Particle effect scale 2
	public static final float PARTICLESCALE2 = 0.0150f;
	
	// Height 1 offset
	public static final float HEIGHT1_Y_OFFSET = 16f;
	
	// Height 2 offset
	public static final float HEIGHT2_Y_OFFSET = 32f;
	
	// Adjustment for X post calculation
	public static final float X_Adjustment = -128f;

	// Adjustment for Y post calculation
	public static final float Y_Adjustment = 64f;
	
	// Tile icon size
	public static final int TILE_ICON_SIZE = 50;
	
	// Drop distance
	public static final int DROP_DISTANCE = 16;
	
	// Move X Value
	public static final int MOVE_X_AXIS = 2;
	
	// Move Y Value
	public static final int MOVE_Y_AXIS = 1;
	
	// Slant X Value
	public static final float SLANT_X_AXIS = .05f;
	
	// Slant Y Value
	public static final float SLANT_Y_AXIS = .025f;
		
}
