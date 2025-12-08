package com.comp2042;

import com.comp2042.event.*;
import com.comp2042.input.InputManager;
import com.comp2042.logic.Board;
import com.comp2042.logic.ChallengeModeManager;
import com.comp2042.logic.GameMode;
import com.comp2042.state.*;
import com.comp2042.leaderboard.LeaderboardService;
import com.comp2042.viewmodel.GameViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * GameController acts as a controller, coordinating between the game model and the view.
 */
public class GameController  {
    private final Board board ;
    private final GameViewModel viewModel;
    private final InputManager inputManager;
    private final LeaderboardService leaderboardService;
    private final GameLoop gameLoop;
    private GameState currentState;
    private final GameMode gameMode;
    private ChallengeModeManager challengeModeManager;
    private Timeline challengeTimer;
    private Runnable onVictoryCallback;
    private Runnable onGameOverCallback;
    private Runnable onBackToMainMenuCallback;

    public GameController( Board board, GameViewModel viewModel, InputManager inputManager, LeaderboardService leaderboardService, GameLoop gameLoop, GameMode gameMode) {
        this.board = board;
        this.viewModel = viewModel;
        this.inputManager = inputManager;
        this.leaderboardService = leaderboardService;
        this.gameLoop = gameLoop;
        this.gameMode = gameMode;

        this.currentState = new PlayingState();
        this.gameLoop.setTickAction(this::onTimerTick);

        viewModel.setGameMode(gameMode);

        if (gameMode == GameMode.CHALLENGE) {
            this.challengeModeManager = new ChallengeModeManager();
            setupChallengeModeCallbacks();
        }
    }

    /**
     * Sets the challenge mode callbacks.
     */
    private void setupChallengeModeCallbacks() {
        challengeModeManager.setOnLevelCompleteCallback(() -> {

            int newLevel = challengeModeManager.getCurrentLevel().getLevelNumber();

            double baseSpeed = GameConstants.GAME_SPEED_MILLIS;
            double multiplier = challengeModeManager.getSpeedMultiplier();
            gameLoop.setSpeed(baseSpeed * multiplier);

            viewModel.setChallengeLevel(newLevel);
            viewModel.pushNotification("Level " + newLevel + "!");

            if (currentState instanceof PlayingState) {
                performGameLoopStep();
            }
        });

        challengeModeManager.setOnVictoryCallback(() -> {
            handleChallengeVictory();
        });

        challengeModeManager.setOnGameOverCallback(() -> {
            handleChallengeGameOver();
        });

        challengeModeManager.setOnTimerUpdateCallback(() -> {
            viewModel.setChallengeTimer(challengeModeManager.timerDisplayProperty().get());
        });
    }

    /**
     * Performs the start operation.
     */
    public void start() {
        board.newGame();
        board.createNewBrick();
        refreshViewModel();

        viewModel.setGameMode(gameMode);

        if (gameMode == GameMode.CHALLENGE) {
            challengeModeManager.startChallenge(board.getScore().getScore());
            viewModel.setChallengeLevel(1);
            startChallengeTimer();

            double baseSpeed = GameConstants.GAME_SPEED_MILLIS;
            double multiplier = challengeModeManager.getSpeedMultiplier();
            gameLoop.setSpeed(baseSpeed * multiplier);
        } else {

            gameLoop.setSpeed(GameConstants.GAME_SPEED_MILLIS);
        }

        startGameLoop();
    }

    /**
     * Performs the start challenge timer operation.
     */
    private void startChallengeTimer() {
        if (challengeTimer != null) {
            challengeTimer.stop();
        }

        challengeTimer = new Timeline(new KeyFrame(Duration.millis(100), e -> {
            if (challengeModeManager != null && challengeModeManager.isActive()) {
                challengeModeManager.update(System.currentTimeMillis(), board.getScore().getScore());
            }
        }));
        challengeTimer.setCycleCount(Timeline.INDEFINITE);
        challengeTimer.play();
    }

    /**
     * Returns the leaderboard service.
     *
     * @return the result of the operation
     */
    public LeaderboardService getLeaderboardService() {
        return leaderboardService;
    }

    /**
     * Returns the input manager.
     *
     * @return the result of the operation
     */
    public InputManager getInputManager() {
        return inputManager;
    }

    /**
     * Sets the state.
     *
     * @param state the state
     */
    public void setState(GameState state) {
        this.currentState = state;
    }

    /**
     * Handles the pause toggle.
     */
    public void handlePauseToggle() {
        currentState.handleTogglePause(this);
    }

    /**
     * Callback invoked when  timer tick occurs.
     */
    private void onTimerTick() {
        if (currentState instanceof PlayingState) {
            performGameLoopStep();

            if (gameMode == GameMode.CHALLENGE && challengeModeManager != null && challengeModeManager.isActive()) {
                challengeModeManager.update(System.currentTimeMillis(), board.getScore().getScore());
            }
        }
    }

    /**
     * Performs the pause game operation.
     */
    public void pauseGame() {
        setState(new PausedState());
        stopGameLoop();
        if (challengeTimer != null) {
            challengeTimer.pause();
        }

        com.comp2042.ui.AudioManager.getInstance().pauseBGM();
        viewModel.setPaused(true);
    }
    /**
     * Performs the resume game operation.
     */
    public void resumeGame() {
        setState(new PlayingState());
        gameLoop.start();
        if (challengeTimer != null) {
            challengeTimer.play();
        }

        com.comp2042.ui.AudioManager.getInstance().resumeBGM();
        viewModel.setPaused(false);
    }
    /**
     * Performs the restart game operation.
     */
    public void restartGame() {
        board.newGame();
        board.createNewBrick();
        setState(new PlayingState());
        viewModel.setGameOver(false);
        viewModel.setPaused(false);
        viewModel.setVictory(false);

        viewModel.setGameMode(gameMode);

        if (gameMode == GameMode.CHALLENGE && challengeModeManager != null) {
            challengeModeManager.reset();
            challengeModeManager.startChallenge(board.getScore().getScore());
            viewModel.setChallengeLevel(1);
            startChallengeTimer();
            double baseSpeed = GameConstants.GAME_SPEED_MILLIS;
            double multiplier = challengeModeManager.getSpeedMultiplier();
            gameLoop.setSpeed(baseSpeed * multiplier);
        } else {

            gameLoop.setSpeed(GameConstants.GAME_SPEED_MILLIS);
        }

        refreshViewModel();
        startGameLoop();
    }

    /**
     * Performs the move left operation.
     */
    public void moveLeft() {
        currentState.handleInput(this, () -> {
            boolean shouldMoveLeft = true;
            if (gameMode == GameMode.CHALLENGE && challengeModeManager != null && challengeModeManager.isKeysReversed()) {
                shouldMoveLeft = false;
            }

            if (shouldMoveLeft) {
                if(board.moveBrickLeft()) refreshViewModel();
            } else {
                if(board.moveBrickRight()) refreshViewModel();
            }
        });
    }
    /**
     * Performs the move right operation.
     */
    public void moveRight() {
        currentState.handleInput(this, () -> {
            boolean shouldMoveRight = true;
            if (gameMode == GameMode.CHALLENGE && challengeModeManager != null && challengeModeManager.isKeysReversed()) {
                shouldMoveRight = false;
            }

            if (shouldMoveRight) {
                if(board.moveBrickRight()) refreshViewModel();
            } else {
                if(board.moveBrickLeft()) refreshViewModel();
            }
        });
    }
    /**
     * Performs the rotate operation.
     */
    public void rotate() {
        currentState.handleInput(this, () -> {
            if(board.rotateLeftBrick()) refreshViewModel();
        });
    }
    /**
     * Performs the soft drop operation.
     */
    public void softDrop() {
        currentState.handleInput(this, () -> {
            if(board.moveBrickDown()) {
                board.getScore().add(1);
                refreshViewModel();
            }
        });
    }

    /**
     * Performs the hard drop operation.
     */
    public void hardDrop() {
        currentState.handleInput(this, () -> {
            board.hardDrop();
            processBrickLanding();
            refreshViewModel();
        });
    }

    /**
     * Performs the hold operation.
     */
    public void hold() {
        currentState.handleInput(this, () -> {
            board.holdBrick();
            refreshViewModel();
        });
    }

    /**
     * Performs the process brick landing operation.
     */
    private void processBrickLanding() {
        board.mergeBrickToBackground();
        ClearRow result = board.clearRows();

        if(result.getLinesRemoved() > 0){
            int pointsAdded = board.getScore().addLinesCleared(result.getLinesRemoved());
            viewModel.pushNotification("+" + pointsAdded);
        }
        if(board.createNewBrick()){
            handleGameOver();
        }

    }

    /**
     * Performs the perform game loop step operation.
     */
    public void performGameLoopStep() {
        boolean canMove = board.moveBrickDown();
        if (!canMove) {
            processBrickLanding();
        }
        refreshViewModel();
    }

    /**
     * Handles the game over.
     */
    private void handleGameOver() {

        if (gameMode == GameMode.CHALLENGE && challengeModeManager != null && challengeModeManager.isActive()) {
            challengeModeManager.handlePlayerDeath();
            return;
        }

        setState(new GameOverState());
        stopGameLoop();
        if (challengeTimer != null) {
            challengeTimer.stop();
        }
        viewModel.setGameOver(true);
        leaderboardService.addScore("Player", board.getScore().getScore());

        if (onGameOverCallback != null) {
            onGameOverCallback.run();
        }
    }

    /**
     * Handles the challenge victory.
     */
    private void handleChallengeVictory() {
        setState(new GameOverState());
        stopGameLoop();
        if (challengeTimer != null) {
            challengeTimer.stop();
        }
        viewModel.setVictory(true);
        leaderboardService.addScore("Player", board.getScore().getScore());

        if (onVictoryCallback != null) {
            onVictoryCallback.run();
        }
    }

    /**
     * Handles the challenge game over.
     */
    private void handleChallengeGameOver() {
        setState(new GameOverState());
        stopGameLoop();
        if (challengeTimer != null) {
            challengeTimer.stop();
        }
        viewModel.setGameOver(true);
        leaderboardService.addScore("Player", board.getScore().getScore());

        if (onGameOverCallback != null) {
            onGameOverCallback.run();
        }
    }

    /**
     * Performs the start game loop operation.
     */
    private void startGameLoop() {
        gameLoop.start();
    }

    /**
     * Performs the stop game loop operation.
     */
    private void stopGameLoop() {
        gameLoop.stop();
        if (challengeTimer != null) {
            challengeTimer.pause();
        }
    }

    /**
     * Returns the game mode.
     *
     * @return the result of the operation
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Sets the on victory callback.
     *
     * @param callback the callback
     */
    public void setOnVictoryCallback(Runnable callback) {
        this.onVictoryCallback = callback;
    }

    /**
     * Sets the on game over callback.
     *
     * @param callback the callback
     */
    public void setOnGameOverCallback(Runnable callback) {
        this.onGameOverCallback = callback;
    }

    /**
     * Sets the on back to main menu callback.
     *
     * @param callback the callback
     */
    public void setOnBackToMainMenuCallback(Runnable callback) {
        this.onBackToMainMenuCallback = callback;
    }

    /**
     * Performs the navigate to main menu operation.
     */
    public void navigateToMainMenu() {
        stopGameLoop();
        if (challengeTimer != null) {
            challengeTimer.stop();
        }
        if (onBackToMainMenuCallback != null) {
            onBackToMainMenuCallback.run();
        }
    }
    /**
     * Performs the refresh view model operation.
     */
    private void refreshViewModel() {
        int[][] staticBoard = board.getBoardMatrix();
        ViewData viewData = board.getViewData();

        int[][] displayGrid = mergeFallingPieceToGrid(staticBoard, viewData);

        viewModel.updateBoard(displayGrid);
        viewModel.updateViewData(viewData);
        viewModel.updateScore(board.getScore().getScore());

        viewModel.updateNextPieces(viewData.getNextThreeBricksData());
        viewModel.updateHoldPiece(viewData.getHeldBrickData(), viewData.isHoldAllowed());
    }

    /**
     * Performs the merge falling piece to grid operation.
     *
     * @param board the board
     * @param brick the brick
     * @return the result of the operation
     */
    private int[][] mergeFallingPieceToGrid(int[][] board, ViewData brick) {

        int[][] combined = com.comp2042.logic.MatrixOperations.copy(board);

        if (brick.getBrickData() != null) {

            drawPieceOnGrid(combined, brick.getBrickData(), brick.getXPosition(), brick.getYPosition());
        }
        return combined;
    }

    /**
     * Renders the piece on grid.
     *
     * @param grid the grid
     * @param piece the piece
     * @param x the x
     * @param y the y
     */
    private void drawPieceOnGrid(int[][] grid, int[][] piece, int x, int y) {
        for (int r = 0; r < piece.length; r++) {
            for (int c = 0; c < piece[r].length; c++) {
                if (piece[r][c] != 0) {
                    int targetY = y + r;
                    int targetX = x + c;
                    if (targetY >= 0 && targetY < grid.length && targetX >= 0 && targetX < grid[0].length) {
                        grid[targetY][targetX] = piece[r][c];
                    }
                }
            }
        }
    }
}