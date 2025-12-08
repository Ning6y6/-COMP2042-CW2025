package com.comp2042.logic;

import com.comp2042.event.ClearRow;
import com.comp2042.event.ViewData;

/**
 * Board is part of the logic package and encapsulates behaviour related to board.
 */
public interface Board {

    /**
     * Performs the move brick down operation.
     *
     * @return the result of the operation
     */
    boolean moveBrickDown();

    /**
     * Performs the move brick left operation.
     *
     * @return the result of the operation
     */
    boolean moveBrickLeft();

    /**
     * Performs the move brick right operation.
     *
     * @return the result of the operation
     */
    boolean moveBrickRight();

    /**
     * Performs the rotate left brick operation.
     *
     * @return the result of the operation
     */
    boolean rotateLeftBrick();

    /**
     * Performs the create new brick operation.
     *
     * @return the result of the operation
     */
    boolean createNewBrick();

    /**
     * Returns the board matrix.
     *
     * @return the result of the operation
     */
    int[][] getBoardMatrix();

    /**
     * Returns the view data.
     *
     * @return the result of the operation
     */
    ViewData getViewData();

    /**
     * Performs the merge brick to background operation.
     */
    void mergeBrickToBackground();

    /**
     * Performs the clear rows operation.
     *
     * @return the result of the operation
     */
    ClearRow clearRows();

    /**
     * Returns the score.
     *
     * @return the result of the operation
     */
    Score getScore();

    /**
     * Performs the new game operation.
     */
    void newGame();

    /**
     * Performs the hard drop operation.
     */
    void hardDrop();

    /**
     * Performs the hold brick operation.
     */
    void holdBrick();
}