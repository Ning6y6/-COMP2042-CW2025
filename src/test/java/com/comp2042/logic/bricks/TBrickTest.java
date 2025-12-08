package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TBrickTest {

    @Test
    void getShapeMatrix() {
        TBrick tBrick = new TBrick();
        List<int[][]> shapeMatrix = tBrick.getShapeMatrix();

        assertEquals(4, shapeMatrix.size());

        int[][] expectedShape1 = {
                {0, 6, 0, 0},
                {6, 6, 6, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(expectedShape1, shapeMatrix.get(0));

        int[][] expectedShape2 = {
                {0, 6, 0, 0},
                {0, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(expectedShape2, shapeMatrix.get(1));

        int[][] expectedShape3 = {
                {0, 0, 0, 0},
                {6, 6, 6, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(expectedShape3, shapeMatrix.get(2));

        int[][] expectedShape4 = {
                {0, 6, 0, 0},
                {6, 6, 0, 0},
                {0, 6, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(expectedShape4, shapeMatrix.get(3));
    }
}