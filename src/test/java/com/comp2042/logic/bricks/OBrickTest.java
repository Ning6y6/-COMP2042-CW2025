package com.comp2042.logic.bricks;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OBrickTest {

    @Test
    void getShapeMatrix() {
        OBrick oBrick = new OBrick();
        List<int[][]> shapeMatrix = oBrick.getShapeMatrix();

        assertEquals(1, shapeMatrix.size());

        int[][] expectedShape1 = {
                {0, 0, 0, 0},
                {0, 4, 4, 0},
                {0, 4, 4, 0},
                {0, 0, 0, 0}
        };
        assertArrayEquals(expectedShape1, shapeMatrix.get(0));
    }
}