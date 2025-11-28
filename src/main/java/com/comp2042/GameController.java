package com.comp2042;

import com.comp2042.ui.GameView;
import com.comp2042.event.*;
import com.comp2042.logic.Board;
import com.comp2042.logic.SimpleBoard;

public class GameController implements InputEventListener {

    private Board board = new SimpleBoard(10, 25);

    private final GameView gameView;

    public GameController(GameView c) {
        gameView = c;
        board.createNewBrick();
        gameView.setEventListener(this);
        gameView.initGameView(board.getBoardMatrix(), board.getViewData());
        gameView.bindScore(board.getScore().scoreProperty());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        boolean canMove = board.moveBrickDown();
        ClearRow clearRow = null;
        if (!canMove) {
            board.mergeBrickToBackground();
            clearRow = board.clearRows();
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
            }
            if (board.createNewBrick()) {
                gameView.gameOver();
            }

            gameView.refreshGameBackground(board.getBoardMatrix());

        } else {
            if (event.getEventSource() == EventSource.USER) {
                board.getScore().add(1);
            }
        }
        return new DownData(clearRow, board.getViewData());
    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        board.moveBrickLeft();
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        board.moveBrickRight();
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        board.rotateLeftBrick();
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        gameView.refreshGameBackground(board.getBoardMatrix());
        gameView.refreshBrick(board.getViewData());
    }
}
