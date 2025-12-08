package com.comp2042.logic;

/**
 * Provides wall kick offset data used when attempting to rotate a piece
 * near walls or other obstacles.
 * <p>
 * The offsets are tried in order until a valid position for the rotated
 * piece is found.
 */
public final class WallKickTable {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private WallKickTable() {}

    /**
     * Wall kick offset table.
     * <p>
     * Each entry represents an (x, y) offset that may be applied after a
     * failed rotation attempt in order to find a valid position for the piece.
     */
    public static final int[][] OFFSETS = {
            {0, 0},
            {1, 0},
            {-1, 0},
            {2, 0},
            {-2, 0}
    };
}
