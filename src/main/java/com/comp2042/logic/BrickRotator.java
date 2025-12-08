package com.comp2042.logic;

import com.comp2042.event.NextShapeInfo;
import com.comp2042.logic.bricks.Brick;

public class BrickRotator {

    private Brick brick;
    private int currentShape = 0;

    public NextShapeInfo getNextShape() {
        validateBrickInitialized();
        validateShapeMatrixNotEmpty();

        int nextShape = currentShape;
        nextShape = (++nextShape) % brick.getShapeMatrix().size();
        return new NextShapeInfo(brick.getShapeMatrix().get(nextShape), nextShape);
    }

    public int[][] getCurrentShape() {
        validateBrickInitialized();
        validateShapeMatrixNotEmpty();
        validateCurrentShapeIndex();

        return brick.getShapeMatrix().get(currentShape);
    }

    public void setCurrentShape(int currentShape) {
        validateBrickInitialized();
        if (currentShape < 0 || (brick != null && currentShape >= brick.getShapeMatrix().size())) {
            throw new IllegalArgumentException("Invalid shape index: " + currentShape);
        }
        this.currentShape = currentShape;
    }

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

    public Brick getBrick() {
        return brick;
    }

    private void validateBrickInitialized() {
        if (brick == null) {
            throw new IllegalStateException("Brick must be initialized before rotation operations");
        }
    }

    private void validateShapeMatrixNotEmpty() {
        if (brick.getShapeMatrix().isEmpty()) {
            throw new IllegalStateException("Brick shape matrix cannot be empty");
        }
    }

    private void validateCurrentShapeIndex() {
        if (currentShape < 0 || currentShape >= brick.getShapeMatrix().size()) {
            throw new IllegalStateException("Current shape index out of bounds: " + currentShape);
        }
    }
}