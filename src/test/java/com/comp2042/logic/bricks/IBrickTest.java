package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IBrickTest {

    @Test
    void getShapeMatrix() {
        IBrick iBrick = new IBrick();
        List<int[][]> shapeMatrix = iBrick.getShapeMatrix();

        assertEquals(2, shapeMatrix.size());

        int[][] expectedShape1 = {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(expectedShape1, shapeMatrix.get(0));

        int[][] expectedShape2 = {
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0},
                {0, 1, 0, 0}
        };
        assertArrayEquals(expectedShape2, shapeMatrix.get(1));
    }
}