package com.comp2042;

import com.comp2042.ui.GameView;
import com.comp2042.event.*;
import com.comp2042.input.InputManager;
import com.comp2042.logic.Board;
import com.comp2042.logic.GameBoard;
import com.comp2042.state.*;
import com.comp2042.leaderboard.FileLeaderboardDAO;
import com.comp2042.leaderboard.LeaderboardService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameController implements GameInputHandler {
    private final Board board ;
    private final GameView gameView;
    private final InputManager inputManager;
    private final LeaderboardService leaderboardService;

    private Timeline gameLoop;
    private GameState currentState;
    private ClearRow lastClearRow;

    public GameController(GameView c) {
        this.gameView = c;
        this.board = new GameBoard();
        this.inputManager = new InputManager();
        this.leaderboardService = new LeaderboardService(new FileLeaderboardDAO());
        this.currentState = new PlayingState();
        initializeGame();
    }

    private void initializeGame() {
        board.createNewBrick();

        gameView.setEventListener(this);
        gameView.setInputManager(inputManager);
        gameView.initGameView(board.getBoardMatrix(), board.getViewData());
        gameView.bindScore(board.getScore().scoreProperty());

        gameView.updateHoldPiece(board.getViewData());
        gameView.updateNextPieces(board.getViewData());

        startGameLoop();
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

    public void handlePauseToggle() {
        currentState.handleTogglePause(this);
    }
    public void pauseGame() {
        setState(new PausedState());
        gameView.showPauseMenu();
        stopGameLoop();
    }
    public void resumeGame() {

        setState(new PlayingState());
        gameView.closePauseMenu();
        startGameLoop();
    }
    public void restartGame() {
        createNewGame();
        resumeGame();
    }

    public void moveLeft() {
        currentState.handleInput(this, () -> {
            board.moveBrickLeft();
            updateViewAfterMove();
        });
    }
    public void moveRight() {
        currentState.handleInput(this, () -> {
            board.moveBrickRight();
            updateViewAfterMove();
        });
    }
    public void rotate() {
        currentState.handleInput(this, () -> {
            board.rotateLeftBrick();
            updateViewAfterMove();
        });
    }
    public void softDrop() {
        currentState.handleInput(this, () -> {
            board.moveBrickDown();
            board.getScore().add(1);
            updateViewAfterMove();
        });
    }

    public void hardDrop() {
        currentState.handleInput(this, () -> {
            board.hardDrop();
            processBrickLanding();
            updateViewAfterMove();
        });
    }

    public void hold() {
        currentState.handleInput(this, () -> {
            board.holdBrick();
            updateViewAfterHold();
        });
    }

    private void updateViewAfterMove() {
        gameView.updateFallingBrick(board.getViewData());
        gameView.updateHoldPiece(board.getViewData());
        gameView.updateNextPieces(board.getViewData());
        gameView.requestFocus();
    }

    private void updateViewAfterHold() {
        gameView.updateFallingBrick(board.getViewData());
        gameView.updateHoldPiece(board.getViewData());
        gameView.updateNextPieces(board.getViewData());
    }

    private void updateViewAfterLanding() {
        gameView.refreshBoard(board.getBoardMatrix());
        gameView.updateFallingBrick(board.getViewData());
        gameView.updateNextPieces(board.getViewData());
    }

    private void processBrickLanding() {
        board.mergeBrickToBackground();
        ClearRow clearRow = board.clearRows();
        handleRowClearing(clearRow);
        boolean gameOver = board.createNewBrick();
        if (gameOver) {
            handleGameOver();
        } else {
            updateViewAfterLanding();
        }

    }

    private void handleRowClearing(ClearRow clearRow) {
        if (clearRow.getLinesRemoved() > 0) {
            board.getScore().add(clearRow.getScoreBonus());
            gameView.showLinesClearedNotification(clearRow.getScoreBonus());
            lastClearRow = clearRow;
        } else {
            lastClearRow = null;
        }
    }

    public void performGameLoopStep() {
        boolean canMove = board.moveBrickDown();
        if (!canMove) {
            processBrickLanding();
        }
        gameView.updateFallingBrick(board.getViewData());
    }

    private void handleGameOver() {
        setState(new GameOverState());
        stopGameLoop();
        gameView.showGameOver();
        leaderboardService.addScore("Player", board.getScore().scoreProperty().get());
    }

    @Override
    public DownData onDownEvent(MoveEvent event) {
        currentState.update(this);
        ClearRow clearRowForView = lastClearRow;
        lastClearRow = null;
        return new DownData(clearRowForView, board.getViewData());

    }

    @Override
    public ViewData onLeftEvent(MoveEvent event) {
        return board.getViewData();
    }

    @Override
    public ViewData onRightEvent(MoveEvent event) {
        return board.getViewData();
    }

    @Override
    public ViewData onRotateEvent(MoveEvent event) {
        return board.getViewData();
    }


    @Override
    public void createNewGame() {
        board.newGame();
        board.createNewBrick();
        gameView.refreshBoard(board.getBoardMatrix());
        gameView.updateFallingBrick(board.getViewData());
        gameView.updateHoldPiece(board.getViewData());
        gameView.updateNextPieces(board.getViewData());

        setState(new PlayingState());
        lastClearRow = null;
        startGameLoop();
    }

    private void startGameLoop() {
        if(gameLoop == null) {
            gameLoop = new Timeline(new KeyFrame(
                    Duration.millis(GameConstants.GAME_SPEED_MILLIS),
                    event -> {
                        if (!(currentState instanceof PausedState) && !(currentState instanceof GameOverState)){
                            performGameLoopStep();
                        }
                    }
            ));
            gameLoop.setCycleCount(Timeline.INDEFINITE);
        }
        gameLoop.play();
    }

    private void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
        }
    }
}
