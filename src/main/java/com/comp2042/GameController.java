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

    private void onTimerTick() {
        if (currentState instanceof PlayingState) {
            performGameLoopStep();

            if (gameMode == GameMode.CHALLENGE && challengeModeManager != null && challengeModeManager.isActive()) {
                challengeModeManager.update(System.currentTimeMillis(), board.getScore().getScore());
            }
        }
    }

    public void pauseGame() {
        setState(new PausedState());
        stopGameLoop();
        if (challengeTimer != null) {
            challengeTimer.pause();
        }

        com.comp2042.ui.AudioManager.getInstance().pauseBGM();
        viewModel.setPaused(true);
    }
    public void resumeGame() {
        setState(new PlayingState());
        gameLoop.start();
        if (challengeTimer != null) {
            challengeTimer.play();
        }

        com.comp2042.ui.AudioManager.getInstance().resumeBGM();
        viewModel.setPaused(false);
    }
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
    public void rotate() {
        currentState.handleInput(this, () -> {
            if(board.rotateLeftBrick()) refreshViewModel();
        });
    }
    public void softDrop() {
        currentState.handleInput(this, () -> {
            if(board.moveBrickDown()) {
                board.getScore().add(1);
                refreshViewModel();
            }
        });
    }

    public void hardDrop() {
        currentState.handleInput(this, () -> {
            board.hardDrop();
            processBrickLanding();
            refreshViewModel();
        });
    }

    public void hold() {
        currentState.handleInput(this, () -> {
            board.holdBrick();
            refreshViewModel();
        });
    }

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

    public void performGameLoopStep() {
        boolean canMove = board.moveBrickDown();
        if (!canMove) {
            processBrickLanding();
        }
        refreshViewModel();
    }

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

    private void startGameLoop() {
        gameLoop.start();
    }

    private void stopGameLoop() {
        gameLoop.stop();
        if (challengeTimer != null) {
            challengeTimer.pause();
        }
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setOnVictoryCallback(Runnable callback) {
        this.onVictoryCallback = callback;
    }

    public void setOnGameOverCallback(Runnable callback) {
        this.onGameOverCallback = callback;
    }

    public void setOnBackToMainMenuCallback(Runnable callback) {
        this.onBackToMainMenuCallback = callback;
    }

    public void navigateToMainMenu() {
        stopGameLoop();
        if (challengeTimer != null) {
            challengeTimer.stop();
        }
        if (onBackToMainMenuCallback != null) {
            onBackToMainMenuCallback.run();
        }
    }
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

    private int[][] mergeFallingPieceToGrid(int[][] board, ViewData brick) {

        int[][] combined = com.comp2042.logic.MatrixOperations.copy(board);

        if (brick.getBrickData() != null) {

            drawPieceOnGrid(combined, brick.getBrickData(), brick.getXPosition(), brick.getYPosition());
        }
        return combined;
    }

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