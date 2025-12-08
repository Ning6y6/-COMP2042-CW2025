package com.comp2042;

/**
 * Utility class that defines constant configuration values used throughout
 * the Tetris game such as board dimensions, spawn positions and window size.
 * <p>
 * This class is not meant to be instantiated.
 */
public final class GameConstants {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private GameConstants() {}

    /** Width of the game board in blocks. */
    public static final int BOARD_WIDTH = 10;

    /** Height of the game board in blocks (including hidden rows). */
    public static final int BOARD_HEIGHT = 25;

    /** Number of rows at the top of the board that are hidden from the player. */
    public static final int HIDDEN_ROWS = 2;

    /** Number of rows that are visible to the player. */
    public static final int VISIBLE_ROWS = BOARD_HEIGHT - HIDDEN_ROWS;

    /** Size of a single block (brick) in pixels. */
    public static final int BRICK_SIZE = 20;

    /** Default horizontal spawn position (column) for new pieces. */
    public static final int SPAWN_X = 4;

    /** Default vertical spawn position (row) for new pieces. */
    public static final int SPAWN_Y = 0;

    /** Default game speed in milliseconds between automatic piece drops. */
    public static final double GAME_SPEED_MILLIS = 400.0;

    /** Number of upcoming pieces to show in the preview area. */
    public static final int PREVIEW_COUNT = 3;

    /** Width of the main game window in pixels. */
    public static final int WINDOW_WIDTH = 690;

    /** Height of the main game window in pixels. */
    public static final int WINDPW_HEIGHT = 550;
}