package com.comp2042.logic;

import  com.comp2042.GameConstants;
import com.comp2042.event.ClearRow;
import com.comp2042.event.NextShapeInfo;
import com.comp2042.event.ViewData;
import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickGenerator;
import com.comp2042.logic.bricks.RandomBrickGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameBoard implements Board {

    private final BrickGenerator brickGenerator;
    private final BrickRotator brickRotator;
    private final HoldManager holdManager;
    private int[][] currentGameMatrix;

    private int currentX;
    private int currentY;

    private final Score score;

    public GameBoard() {

        this.currentGameMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        this.brickGenerator = new RandomBrickGenerator();
        this.brickRotator = new BrickRotator();
        this.holdManager = new HoldManager();
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
    public void hardDrop() {
        while (moveBrickDown()) {
            score.add(1);
        }
    }

    @Override
    public void holdBrick() {
        Brick currentBrick = brickRotator.getBrick();

        if (!holdManager.isCanSwap()) return;

        Optional<Brick> swapResult = holdManager.swap(currentBrick);

        if (swapResult.isEmpty()) {
            createNewBrick();
        } else {
            Brick brickFromHold = swapResult.get();

            brickRotator.setBrick(brickFromHold);

            currentX = GameConstants.SPAWN_X;
            currentY = GameConstants.SPAWN_Y;


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
        Brick nextBrick = brickGenerator.getBrick();
        return spawnBrick(nextBrick);
    }

    private boolean spawnBrick(Brick brick) {
        brickRotator.setBrick(brick);
        currentX = GameConstants.SPAWN_X;
        currentY = GameConstants.SPAWN_Y;
        holdManager.resetSwapFlag();
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), currentX, currentY);
    }

    @Override
    public int[][] getBoardMatrix() {

        return MatrixOperations.copy(currentGameMatrix);
    }

    @Override
    public ViewData getViewData() {
        List<int[][]> nextThreeBricks = extractNextThreeBrickShapes();
        int[][] nextBrickShape = extractNextBrickShape();

        int[][] heldShape = null;
        if (holdManager.getHeldBrick() != null) {
            heldShape = holdManager.getHeldBrick().getShapeMatrix().get(0);
        }
        
        return new ViewData(
                brickRotator.getCurrentShape(),
                currentX,
                currentY,
                nextBrickShape,
                nextThreeBricks,
                heldShape,
                holdManager.isCanSwap()
        );
    }

    private List<int[][]> extractNextThreeBrickShapes() {
        List<int[][]> nextThreeBricks = new ArrayList<>();
        for (Brick brick : brickGenerator.getNextThreeBricks()) {
            validateBrickShapeMatrix(brick);
            nextThreeBricks.add(brick.getShapeMatrix().getFirst());
        }
        return nextThreeBricks;
    }


    private int[][] extractNextBrickShape() {
        Brick nextBrick = brickGenerator.getNextBrick();
        validateBrickShapeMatrix(nextBrick);
        return nextBrick.getShapeMatrix().getFirst();
    }

    private void validateBrickShapeMatrix(Brick brick) {
        if (brick == null) {
            throw new IllegalStateException("Brick cannot be null");
        }
        if (brick.getShapeMatrix() == null || brick.getShapeMatrix().isEmpty()) {
            throw new IllegalStateException("Brick shape matrix cannot be null or empty");
        }
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
        holdManager.reset();
        createNewBrick();
    }
}
