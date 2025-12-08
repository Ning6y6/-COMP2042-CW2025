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

/**
 * GameBoard is part of the logic package and encapsulates behaviour related to game board.
 */
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
    /**
     * Performs the move brick down operation.
     *
     * @return the result of the operation
     */
    public boolean moveBrickDown() {
        return tryMove(this.currentX, this.currentY + 1);
    }

    @Override
    /**
     * Performs the move brick left operation.
     *
     * @return the result of the operation
     */
    public boolean moveBrickLeft() {
        return tryMove(currentX - 1, currentY);
    }

    @Override
    /**
     * Performs the move brick right operation.
     *
     * @return the result of the operation
     */
    public boolean moveBrickRight() {
        return tryMove(currentX + 1, currentY);
    }

    /**
     * Performs the try move operation.
     *
     * @param nextX the nextX
     * @param nextY the nextY
     * @return the result of the operation
     */
    private boolean tryMove(int nextX, int nextY) {
        boolean conflict = MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), nextX, nextY);
        if (conflict) {
            return false;
        } else {
            currentX = nextX;
            currentY = nextY;
            return true;
        }
    }
    @Override
    /**
     * Performs the hard drop operation.
     */
    public void hardDrop() {
        while (moveBrickDown()) {
            score.add(1);
        }
    }

    @Override
    /**
     * Performs the hold brick operation.
     */
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
    /**
     * Performs the rotate left brick operation.
     *
     * @return the result of the operation
     */
    public boolean rotateLeftBrick() {
        NextShapeInfo nextShapeInfo = brickRotator.getNextShape();
        int[][] nextMatrix = nextShapeInfo.getShape();
        int nextIndex = nextShapeInfo.getPosition();

        for (int[] offset : WallKickTable.OFFSETS) {
            int kickX = offset[0];
            int kickY = offset[1];

            int targetX = currentX + kickX;
            int targetY = currentY + kickY;

            boolean conflict = MatrixOperations.intersect(currentGameMatrix, nextMatrix, targetX, targetY);
            if (!conflict) {
                brickRotator.setCurrentShape(nextIndex);
                currentX = targetX;
                currentY = targetY;
                return true;
            }
        }
        return false;
    }

    @Override
    /**
     * Performs the create new brick operation.
     *
     * @return the result of the operation
     */
    public boolean createNewBrick() {
        Brick nextBrick = brickGenerator.getBrick();
        return spawnBrick(nextBrick);
    }

    /**
     * Performs the spawn brick operation.
     *
     * @param brick the brick
     * @return the result of the operation
     */
    private boolean spawnBrick(Brick brick) {
        brickRotator.setBrick(brick);
        currentX = GameConstants.SPAWN_X;
        currentY = GameConstants.SPAWN_Y;
        holdManager.resetSwapFlag();
        return MatrixOperations.intersect(currentGameMatrix, brickRotator.getCurrentShape(), currentX, currentY);
    }

    @Override
    /**
     * Returns the board matrix.
     *
     * @return the result of the operation
     */
    public int[][] getBoardMatrix() {
        return MatrixOperations.copy(currentGameMatrix);
    }

    /**
     * Performs the calculate ghost y operation.
     *
     * @return the result of the operation
     */
    private int calculateGhostY() {
        int ghostY = currentY;
        int[][] shape = brickRotator.getCurrentShape();

        while (!MatrixOperations.intersect(currentGameMatrix, shape, currentX, ghostY + 1)) {
            ghostY++;
        }
        return ghostY;
    }

    @Override
    /**
     * Returns the view data.
     *
     * @return the result of the operation
     */
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
                calculateGhostY(),
                nextBrickShape,
                nextThreeBricks,
                heldShape,
                holdManager.isCanSwap()
        );
    }

    /**
     * Performs the extract next three brick shapes operation.
     *
     * @return the result of the operation
     */
    private List<int[][]> extractNextThreeBrickShapes() {
        List<int[][]> nextThreeBricks = new ArrayList<>();
        for (Brick brick : brickGenerator.getNextThreeBricks()) {
            validateBrickShapeMatrix(brick);
            nextThreeBricks.add(brick.getShapeMatrix().getFirst());
        }
        return nextThreeBricks;
    }

    /**
     * Performs the extract next brick shape operation.
     *
     * @return the result of the operation
     */
    private int[][] extractNextBrickShape() {
        Brick nextBrick = brickGenerator.getNextBrick();
        validateBrickShapeMatrix(nextBrick);
        return nextBrick.getShapeMatrix().getFirst();
    }

    /**
     * Performs the validate brick shape matrix operation.
     *
     * @param brick the brick
     */
    private void validateBrickShapeMatrix(Brick brick) {
        if (brick == null) {
            throw new IllegalStateException("Brick cannot be null");
        }
        if (brick.getShapeMatrix() == null || brick.getShapeMatrix().isEmpty()) {
            throw new IllegalStateException("Brick shape matrix cannot be null or empty");
        }
    }

    @Override
    /**
     * Performs the merge brick to background operation.
     */
    public void mergeBrickToBackground() {
        currentGameMatrix = MatrixOperations.merge(currentGameMatrix, brickRotator.getCurrentShape(), currentX, currentY);
    }

    @Override
    /**
     * Performs the clear rows operation.
     *
     * @return the result of the operation
     */
    public ClearRow clearRows() {
        ClearRow clearRow = MatrixOperations.checkRemoving(currentGameMatrix);
        currentGameMatrix = clearRow.getNewMatrix();
        return clearRow;

    }

    @Override
    /**
     * Returns the score.
     *
     * @return the result of the operation
     */
    public Score getScore() {

        return score;
    }

    @Override
    /**
     * Performs the new game operation.
     */
    public void newGame() {
        currentGameMatrix = new int[GameConstants.BOARD_HEIGHT][GameConstants.BOARD_WIDTH];
        score.reset();
        holdManager.reset();
        createNewBrick();
    }
}