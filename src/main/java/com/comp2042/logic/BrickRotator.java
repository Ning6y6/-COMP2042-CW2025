package com.comp2042.logic;

import com.comp2042.event.NextShapeInfo;
import com.comp2042.logic.bricks.Brick;

/**
 * BrickRotator is part of the logic package and encapsulates behaviour related to brick rotator.
 */
public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    /**
     * Returns the next shape.
     *
     * @return the result of the operation
     */
    public NextShapeInfo getNextShape() {
        validateBrickInitialized();
        validateShapeMatrixNotEmpty();

        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    /**
     * Returns the current shape.
     *
     * @return the result of the operation
     */
    public int[][] getCurrentShape() {
        validateBrickInitialized();
        validateShapeMatrixNotEmpty();
        validateCurrentShapeIndex();

        return brick.getShapeMatrix().get(currentShape);
    }

    /**
     * Sets the current shape.
     *
     * @param currentShape the currentShape
     */
    public void setCurrentShape(int currentShape) {
        validateBrickInitialized();
        if (currentShape < 0 || (brick != null && currentShape >= brick.getShapeMatrix().size())) {
            throw new IllegalArgumentException("Invalid shape index: " + currentShape);
        }
        this.currentShape = currentShape;
    }

    /**
     * Sets the brick.
     *
     * @param brick the brick
     */
    public void setBrick(Brick brick) {
        if (brick == null) {
            throw new IllegalArgumentException("Brick cannot be null");
        }
        if (brick.getShapeMatrix() == null || brick.getShapeMatrix().isEmpty()) {
            throw new IllegalArgumentException("Brick shape matrix cannot be null or empty");
        }
        this.brick = brick;
        currentShape = 0;
    }

    /**
     * Returns the brick.
     *
     * @return the result of the operation
     */
    public Brick getBrick() {
        return brick;
    }

    /**
     * Performs the validate brick initialized operation.
     */
    private void validateBrickInitialized() {
        if (brick == null) {
            throw new IllegalStateException("Brick must be initialized before rotation operations");
        }
    }

    /**
     * Performs the validate shape matrix not empty operation.
     */
    private void validateShapeMatrixNotEmpty() {
        if (brick.getShapeMatrix().isEmpty()) {
            throw new IllegalStateException("Brick shape matrix cannot be empty");
        }
    }

    /**
     * Performs the validate current shape index operation.
     */
    private void validateCurrentShapeIndex() {
        if (currentShape < 0 || currentShape >= brick.getShapeMatrix().size()) {
            throw new IllegalStateException("Current shape index out of bounds: " + currentShape);
        }
    }
}