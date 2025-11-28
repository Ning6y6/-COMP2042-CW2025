package com.comp2042.logic;

import com.comp2042.event.ClearRow;
import com.comp2042.event.NextShapeInfo;
import com.comp2042.event.ViewData;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.util.ArrayList;
import java.util.List;

public class GameBoard implements Board {

    private final int width;
    private final int height;
    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;

    private int currentX;
    private int currentY;

    private final Score score;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        currentGameMatrix = new int[height][width];
        brickGenerator = new RandomBrickGenerator();
        brickRotator = new BrickRotator();
        score = new Score();

        this.currentX = 0;
        this.currentY = 0;
    }

    @Override
    public boolean moveBrickDown() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        int nextY = currentY + 1;
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), currentX, nextY);
        if (conflict) {
            return false;
        } else {
            currentY = nextY;
            return true;
        }
    }


    @Override
    public boolean moveBrickLeft() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);

        int nextX = currentX - 1;
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), nextX, currentY);
        if (conflict) {
            return false;
        } else {
            currentX = nextX;
            return true;
        }
    }

    @Override
    public boolean moveBrickRight() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        int nextX = currentX + 1;
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), nextX, currentY);
        if (conflict) {
            return false;
        } else {
            currentX = nextX;
            return true;
        }
    }

    @Override
    public boolean rotateLeftBrick() {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        NextShapeInfo nextShape = brickRotator.getNextShape();
        boolean conflict = MatrixOperations.intersect(currentMatrix, nextShape.getShape(), currentX, currentY);
        if (conflict) {
            return false;
        } else {
            brickRotator.setCurrentShape(nextShape.getPosition());
            return true;
        }
    }

    @Override
    public boolean createNewBrick() {
        Brick currentBrick = brickGenerator.getBrick();
        brickRotator.setBrick(currentBrick);
        currentX = 4;
        currentY = 0;
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), currentX, currentY);
    }

    @Override
    public int[][] getBoardMatrix() {

        return MatrixOperations.copy(currentGameMatrix);
    }

    @Override
    public ViewData getViewData() {
        List<int[][]> nextThreeBricks = new ArrayList<>();
        for (Brick brick : brickGenerator.getNextThreeBricks()) {
            nextThreeBricks.add(brick.getShapeMatrix().get(0));
        }
        return new ViewData(brickRotator.getCurrentShape(), currentX, currentY,
                brickGenerator.getNextBrick().getShapeMatrix().get(0), nextThreeBricks);
    }

    @Override
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), currentX, currentY);
    }

    @Override
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    public Score getScore() {

        return score;
    }


    @Override
    public void newGame() {
        currentGameMatrix = new int[height][width];
        score.reset();
        createNewBrick();
    }
}
