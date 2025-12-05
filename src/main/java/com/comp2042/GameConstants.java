package com.comp2042;

public final class GameConstants {
    private GameConstants(){}

    public static final int BOARD_WIDTH = 10 ;
    public static final int BOARD_HEIGHT = 25;

    public static final int HIDDEN_ROWS = 2;
    public static final int VISIBLE_ROWS = BOARD_HEIGHT - HIDDEN_ROWS;

    public static final int BRICK_SIZE = 20;

    public static final int SPAWN_X = 4;
    public static final int SPAWN_Y = 0;

    public static final double GAME_SPEED_MILLIS = 400.0;
    public static final int PREVIEW_COUNT = 3;
}
