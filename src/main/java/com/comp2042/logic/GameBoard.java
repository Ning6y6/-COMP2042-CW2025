package com.comp2042.logic;

import com.comp2042.GameConstants;
import com.comp2042.event.ClearRow;
import com.comp2042.event.NextShapeInfo;
import com.comp2042.event.ViewData;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.util.ArrayList;
import java.util.List;

public class GameBoard implements Board {

    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private int[][] currentGameMatrix;

    private int currentX;
    private int currentY;

    private final Score score;

    public GameBoard() {

        this.currentGameMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        this.brickGenerator = new RandomBrickGenerator();
        this.brickRotator = new BrickRotator();
        this.score = new Score();

        this.currentX = 0;
        this.currentY = 0;
    }

    @Override
    public boolean moveBrickDown() {
        return tryMove(this.currentX, this.currentY + 1);
    }


    @Override
    public boolean moveBrickLeft() {
        return tryMove(currentX - 1, currentY);
    }

    @Override
    public boolean moveBrickRight() {
        return tryMove(currentX + 1, currentY);
    }

    private boolean tryMove(int nextX, int nextY) {
        int[][] currentMatrix = MatrixOperations.copy(currentGameMatrix);
        boolean conflict = MatrixOperations.intersect(currentMatrix, brickRotator.getCurrentShape(), nextX, nextY);
        if (conflict) {
            return false;
        } else {
            currentX = nextX;
            currentY = nextY;
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
        currentX = GameConstants.SPAWN_X;
        currentY = GameConstants.SPAWN_Y;
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
            nextThreeBricks.add(brick.getShapeMatrix().getFirst());
        }
        return new ViewData(brickRotator.getCurrentShape(), currentX, currentY,
                brickGenerator.getNextBrick().getShapeMatrix().getFirst(), nextThreeBricks);
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
        currentGameMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        score.reset();
        createNewBrick();
    }
}
