package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JBrickTest {

    @Test
    void getShapeMatrix() {
        JBrick jBrick = new JBrick();
        List<int[][]> shapeMatrix = jBrick.getShapeMatrix();

        assertEquals(4, shapeMatrix.size());

        int[][] expectedShape1 = {
                {0, 0, 0, 0},
                {0, 2, 0, 0},
                {0, 2, 2, 2},
                {0, 0, 0, 0}
        };
        assertArrayEquals(expectedShape1, shapeMatrix.get(0));

        int[][] expectedShape2 = {
                {0, 0, 0, 0},
                {0, 2, 2, 0},
                {0, 2, 0, 0},
                {0, 2, 0, 0}
        };
        assertArrayEquals(expectedShape2, shapeMatrix.get(1));

        int[][] expectedShape3 = {
                {0, 0, 0, 0},
                {2, 2, 2, 0},
                {0, 0, 2, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(expectedShape3, shapeMatrix.get(2));

        int[][] expectedShape4 = {
                {0, 0, 2, 0},
                {0, 0, 2, 0},
                {0, 2, 2, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(expectedShape4, shapeMatrix.get(3));
    }
}