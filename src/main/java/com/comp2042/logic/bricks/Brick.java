package com.comp2042.logic.bricks;

import java.util.List;

/**
 * Brick is part of the bricks package and encapsulates behaviour related to brick.
 */
public interface Brick {

    /**
     * Returns the shape matrix.
     *
     * @return the result of the operation
     */
    List<int[][]> getShapeMatrix();
}