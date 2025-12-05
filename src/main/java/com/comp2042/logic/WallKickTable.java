package com.comp2042.logic;


public final class WallKickTable {

    private WallKickTable() {}

    public static final int[][] OFFSETS = {
            {0, 0},
            {1, 0},
            {-1, 0},
            {2, 0},
            {-2, 0}
    };
}