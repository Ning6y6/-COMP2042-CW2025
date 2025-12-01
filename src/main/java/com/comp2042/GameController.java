package com.comp2042;

import com.comp2042.ui.GameView;
import com.comp2042.ui.SettingsView;
import com.comp2042.event.*;
import com.comp2042.input.InputManager;
import com.comp2042.logic.Board;
import com.comp2042.logic.GameBoard;
import com.comp2042.state.*;
import com.comp2042.leaderboard.FileLeaderboardDAO;
import com.comp2042.leaderboard.LeaderboardService;

public class GameController implements GameInputHandler {
    private final Board board ;
    private final GameView gameView;
    private final InputManager inputManager;
    private final LeaderboardService leaderboardService;

    private GameState currentState;

    /**
     * Stores information about the last row clear operation during a game loop step.
     * Used to notify the UI (GameView) so it can display the +score popup.
     * Applies SRP: GameController tracks game events, UI handles rendering.
     */
    private ClearRow lastClearRow;

    public GameController(GameView c) {
        this.gameView = c;
        this.board = new GameBoard();
        this.inputManager = new InputManager();
        this.leaderboardService = new LeaderboardService(new FileLeaderboardDAO());
        this.currentState = new PlayingState();
        this.board.createNewBrick();

        this.gameView.setEventListener(this);
        this.gameView.setInputManager(this.inputManager);
        this.gameView.initGameView(board.getBoardMatrix(), board.getViewData());
        this.gameView.bindScore(board.getScore().scoreProperty());
    }
    public LeaderboardService getLeaderboardService() {
        return leaderboardService;
    }

    public InputManager getInputManager() {
        return inputManager;
    }

    public void setState(GameState state) {
        this.currentState = state;
    }

    public void pauseGame() {
        setState(new PausedState());
        new SettingsView(this).show(); // Open menu on pause
    }
    public void resumeGame() {
        setState(new PlayingState());
    }
    public void restartGame() {
        createNewGame();
        resumeGame();
    }

    public void moveLeft() {
        currentState.handleInput(this, () -> {
            board.moveBrickLeft();
            gameView.refreshBrick(board.getViewData());
        });
    }
    public void moveRight() {
        currentState.handleInput(this, () -> {
            board.moveBrickRight();
            gameView.refreshBrick(board.getViewData());
        });
    }
    public void rotate() {
        currentState.handleInput(this, () -> {
            board.rotateLeftBrick();
            gameView.refreshBrick(board.getViewData());
        });
    }
    public void softDrop() {
        currentState.handleInput(this, () -> {
            board.moveBrickDown();
            board.getScore().add(1);
            gameView.refreshBrick(board.getViewData());
        });
    }

    public void performGameLoopStep() {
        boolean canMove = board.moveBrickDown();
        if (!canMove) {
            board.mergeBrickToBackground();
            var clearRow = board.clearRows();
            // Track row clear event for UI notification (bonus popup)
            if (clearRow.getLinesRemoved() > 0) {
                board.getScore().add(clearRow.getScoreBonus());
                lastClearRow = clearRow;
            } else {
                lastClearRow = null;
            }
            if (board.createNewBrick()) {
                handleGameOver();
            }
            gameView.refreshGameBackground(board.getBoardMatrix());
        }
        gameView.refreshBrick(board.getViewData());
    }

    private void handleGameOver() {
        setState(new GameOverState());
        gameView.gameOver();
        leaderboardService.addScore("Player", board.getScore().scoreProperty().get());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        currentState.update(this);
        // Pass ClearRow information to the view so it can show the +score popup.
        ClearRow clearRowForView = lastClearRow;
        // Reset after consumption to avoid duplicate notifications
        lastClearRow = null;
        return new DownData(clearRowForView, board.getViewData());

    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        return null;
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        return null;
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        return null;
    }


    @Override
    public void createNewGame() {
        board.newGame();
        gameView.refreshGameBackground(board.getBoardMatrix());
        gameView.refreshBrick(board.getViewData());
    }
}
