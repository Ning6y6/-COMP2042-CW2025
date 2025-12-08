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
            // Get the new level number BEFORE updating speed
            int newLevel = challengeModeManager.getCurrentLevel().getLevelNumber();
            
            // Update game speed for new level
            double baseSpeed = GameConstants.GAME_SPEED_MILLIS;
            double multiplier = challengeModeManager.getSpeedMultiplier();
            gameLoop.setSpeed(baseSpeed * multiplier);
            
            // Update view model with the new level
            // setChallengeLevel already handles thread safety
            viewModel.setChallengeLevel(newLevel);
            viewModel.pushNotification("Level " + newLevel + "!");
            
            // Force a game loop step immediately to ensure brick starts moving
            // This prevents bricks from getting stuck at spawn after level transition
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

        // Ensure game mode is set in view model (important for UI visibility bindings)
        viewModel.setGameMode(gameMode);

        if (gameMode == GameMode.CHALLENGE) {
            challengeModeManager.startChallenge(board.getScore().getScore());
            viewModel.setChallengeLevel(1);
            startChallengeTimer();
            // Set initial speed
            double baseSpeed = GameConstants.GAME_SPEED_MILLIS;
            double multiplier = challengeModeManager.getSpeedMultiplier();
            gameLoop.setSpeed(baseSpeed * multiplier);
        } else {
            // Endless mode uses default speed
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
            
            // Update challenge mode timer (also handled by separate timeline, but this ensures sync)
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
        // Pause BGM
        com.comp2042.ui.AudioManager.getInstance().pauseBGM();
        viewModel.setPaused(true);
    }
    public void resumeGame() {
        setState(new PlayingState());
        gameLoop.start();
        if (challengeTimer != null) {
            challengeTimer.play();
        }
        // Resume BGM
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
        
        // Ensure game mode is set in view model (important for UI visibility bindings)
        // This ensures the level display visibility updates correctly
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
            // Endless mode - no challenge level needed
            // The visibility binding will hide the level display automatically
            gameLoop.setSpeed(GameConstants.GAME_SPEED_MILLIS);
        }
        
        refreshViewModel();
        startGameLoop();
    }

    public void moveLeft() {
        currentState.handleInput(this, () -> {
            boolean shouldMoveLeft = true;
            if (gameMode == GameMode.CHALLENGE && challengeModeManager != null && challengeModeManager.isKeysReversed()) {
                shouldMoveLeft = false; // Reversed: left command becomes right
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
                shouldMoveRight = false; // Reversed: right command becomes left
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
        // In challenge mode, check if death occurred before time expired
        if (gameMode == GameMode.CHALLENGE && challengeModeManager != null && challengeModeManager.isActive()) {
            challengeModeManager.handlePlayerDeath();
            return; // ChallengeModeManager will trigger appropriate callback
        }
        
        // Standard game over for endless mode or challenge mode after time expired
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
    
    /**
     * Navigates back to the main menu.
     * Stops the game and calls the callback if set.
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
    private void refreshViewModel() {
        int[][] staticBoard = board.getBoardMatrix();
        ViewData viewData = board.getViewData(); // Contains falling piece info

        // 2. Merge falling piece into a display grid for the View
        int[][] displayGrid = mergeFallingPieceToGrid(staticBoard, viewData);

        // 3. Update ViewModel properties
        viewModel.updateBoard(displayGrid);
        viewModel.updateScore(board.getScore().getScore());
        // Level logic could be added here
        viewModel.updateNextPieces(viewData.getNextThreeBricksData());
        viewModel.updateHoldPiece(viewData.getHeldBrickData(), viewData.isHoldAllowed());
    }

    private int[][] mergeFallingPieceToGrid(int[][] board, ViewData brick) {
        int[][] combined = new int[board.length][board[0].length];
        // Copy static board
        for(int i=0; i<board.length; i++) System.arraycopy(board[i], 0, combined[i], 0, board[i].length);

        if (brick.getBrickData() != null) {
            // Draw Active Piece FIRST (so it's on top)
            drawPieceOnGrid(combined, brick.getBrickData(), brick.getXPosition(), brick.getYPosition(), false);
            
            // Then draw Ghost Piece (only in empty cells, not overlapping with active piece)
            drawGhostPieceOnGrid(combined, brick.getBrickData(), brick.getXPosition(), brick.getYPosition(), brick.getGhostYPosition());
        }
        return combined;
    }

    private void drawPieceOnGrid(int[][] grid, int[][] piece, int x, int y, boolean isGhost) {
        for (int r = 0; r < piece.length; r++) {
            for (int c = 0; c < piece[r].length; c++) {
                if (piece[r][c] != 0) {
                    int targetY = y + r;
                    int targetX = x + c;
                    if (targetY >= 0 && targetY < grid.length && targetX >= 0 && targetX < grid[0].length) {
                        // Active piece always overwrites (it's drawn on top)
                        grid[targetY][targetX] = piece[r][c];
                    }
                }
            }
        }
    }
    
    /**
     * Draws the ghost piece, ensuring it doesn't overlap with the active piece or landed pieces.
     * The active piece is already drawn, so we only draw ghost in cells that are still empty (0).
     */
    private void drawGhostPieceOnGrid(int[][] grid, int[][] piece, int activeX, int activeY, int ghostY) {
        // Only draw ghost if it's in a different position than the active piece
        if (ghostY == activeY) {
            return; // Ghost and active piece are at the same position, don't draw ghost
        }
        
        for (int r = 0; r < piece.length; r++) {
            for (int c = 0; c < piece[r].length; c++) {
                if (piece[r][c] != 0) {
                    int ghostTargetY = ghostY + r;
                    int ghostTargetX = activeX + c;
                    
                    // Check if this ghost cell overlaps with any active piece cell
                    boolean overlapsWithActive = false;
                    for (int ar = 0; ar < piece.length; ar++) {
                        for (int ac = 0; ac < piece[ar].length; ac++) {
                            if (piece[ar][ac] != 0) {
                                int activeTargetY = activeY + ar;
                                int activeTargetX = activeX + ac;
                                if (ghostTargetY == activeTargetY && ghostTargetX == activeTargetX) {
                                    overlapsWithActive = true;
                                    break;
                                }
                            }
                        }
                        if (overlapsWithActive) break;
                    }
                    
                    if (!overlapsWithActive && ghostTargetY >= 0 && ghostTargetY < grid.length && 
                        ghostTargetX >= 0 && ghostTargetX < grid[0].length) {
                        // Only draw ghost piece if:
                        // 1. The cell doesn't overlap with active piece
                        // 2. The cell is empty (0) - not a landed piece
                        if (grid[ghostTargetY][ghostTargetX] == 0) {
                            // Use negative ID for ghost pieces so the view can style them differently
                            grid[ghostTargetY][ghostTargetX] = -piece[r][c];
                        }
                    }
                }
            }
        }
    }
}
