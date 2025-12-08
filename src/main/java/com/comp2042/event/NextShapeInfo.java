package com.comp2042.event;

import com.comp2042.logic.MatrixOperations;

public final class NextShapeInfo {

    private final int[][] shape;
    private final int position;

    public NextShapeInfo(final int[][] shape, final int position) {
        this.shape = shape;
        this.position = position;
    }

    /**
     * Returns the shape.
     *
     * @return the result of the operation
     */
    public int[][] getShape() {
        return MatrixOperations.copy(shape);
    }

    /**
     * Returns the position.
     *
     * @return the result of the operation
     */
    public int getPosition() {
        return position;
    }
}