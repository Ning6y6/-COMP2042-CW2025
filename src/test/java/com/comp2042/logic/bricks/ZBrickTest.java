package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ZBrickTest {

    @Test
    void getShapeMatrix() {
        ZBrick zBrick = new ZBrick();
        List<int[][]> shapeMatrix = zBrick.getShapeMatrix();

        assertEquals(2, shapeMatrix.size());

        int[][] expectedShape1 = {
                {0, 0, 0, 0},
                {7, 7, 0, 0},
                {0, 7, 7, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(expectedShape1, shapeMatrix.get(0));

        int[][] expectedShape2 = {
                {0, 7, 0, 0},
                {7, 7, 0, 0},
                {7, 0, 0, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(expectedShape2, shapeMatrix.get(1));
    }
}