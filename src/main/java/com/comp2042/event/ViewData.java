package com.comp2042.event;

import com.comp2042.logic.MatrixOperations;

import java.util.List;

public final class ViewData {

    private final int[][] brickData;
    private final int xPosition;
    private final int yPosition;
    private final int ghostYPosition;
    private final int[][] nextBrickData;
    private final List<int[][]> nextThreeBricksData;

    private final int[][] heldBrickData;
    private final boolean holdAllowed;

    public ViewData(int[][] brickData, int xPosition, int yPosition,int ghostYPosition, int[][] nextBrickData, List<int[][]>nextThreeBricksData, int[][] heldBrickData, boolean holdAllowed) {
        this.brickData = brickData;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.ghostYPosition = ghostYPosition;
        this.nextBrickData = nextBrickData;
        this.nextThreeBricksData = nextThreeBricksData;
        this.heldBrickData = heldBrickData;
        this.holdAllowed = holdAllowed;
    }

    /**
     * Returns the brick data.
     *
     * @return the result of the operation
     */
    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    /**
     * Returns the x position.
     *
     * @return the result of the operation
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Returns the y position.
     *
     * @return the result of the operation
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * Returns the ghost y position.
     *
     * @return the result of the operation
     */
    public int getGhostYPosition(){
        return ghostYPosition;
    }

    /**
     * Returns the next brick data.
     *
     * @return the result of the operation
     */
    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    /**
     * Returns the next three bricks data.
     *
     * @return the result of the operation
     */
    public List<int[][]> getNextThreeBricksData(){
        return  MatrixOperations.deepCopyList(nextThreeBricksData);
    }
    /**
     * Returns the held brick data.
     *
     * @return the result of the operation
     */
    public int[][] getHeldBrickData() {
        return heldBrickData != null ? MatrixOperations.copy(heldBrickData) : null;
    }
    /**
     * Returns whether the hold allowed is true.
     *
     * @return the result of the operation
     */
    public boolean isHoldAllowed() {
        return holdAllowed;
    }
}