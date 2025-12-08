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

    public int[][] getBrickData() {
        return MatrixOperations.copy(brickData);
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public int getGhostYPosition(){
        return ghostYPosition;
    }

    public int[][] getNextBrickData() {
        return MatrixOperations.copy(nextBrickData);
    }

    public List<int[][]> getNextThreeBricksData(){
        return  MatrixOperations.deepCopyList(nextThreeBricksData);
    }
    public int[][] getHeldBrickData() {
        return heldBrickData != null ? MatrixOperations.copy(heldBrickData) : null;
    }
    public boolean isHoldAllowed() {
        return holdAllowed;
    }
}
