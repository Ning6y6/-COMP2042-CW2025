package com.comp2042.logic.bricks;

import java.util.List;

/**
 * BrickGenerator is part of the bricks package and encapsulates behaviour related to brick generator.
 */
public interface BrickGenerator {

    /**
     * Returns the brick.
     *
     * @return the result of the operation
     */
    Brick getBrick();

    /**
     * Returns the next brick.
     *
     * @return the result of the operation
     */
    Brick getNextBrick();

    /**
     * Returns the next three bricks.
     *
     * @return the result of the operation
     */
    List<Brick> getNextThreeBricks();
}