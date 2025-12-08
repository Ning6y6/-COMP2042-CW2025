package com.comp2042;

import com.comp2042.event.*;
import com.comp2042.input.InputManager;
import com.comp2042.logic.Board;
import com.comp2042.state.*;
import com.comp2042.leaderboard.LeaderboardService;
import com.comp2042.viewmodel.GameViewModel;

public class GameController  {
    private final Board board ;
    private final GameViewModel viewModel;
    private final InputManager inputManager;
    private final LeaderboardService leaderboardService;
    private final GameLoop gameLoop;
    private GameState currentState;

    public GameController( Board board, GameViewModel viewModel, InputManager inputManager, LeaderboardService leaderboardService, GameLoop gameLoop) {
        this.board = board;
        this.viewModel = viewModel;
        this.inputManager = inputManager;
        this.leaderboardService = leaderboardService;
        this.gameLoop = gameLoop;

        this.currentState = new PlayingState();
        this.gameLoop.setTickAction(this::onTimerTick);
    }

    public void start() {
        board.newGame();
        board.createNewBrick();
        refreshViewModel();

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

    private void onTimerTick() {
        if (currentState instanceof PlayingState) {
            performGameLoopStep();
        }
    }

    public void pauseGame() {
        setState(new PausedState());
        stopGameLoop();
        viewModel.setPaused(true);
    }
    public void resumeGame() {

        setState(new PlayingState());
        gameLoop.start();
        viewModel.setPaused(false);
    }
    public void restartGame() {
        board.newGame();
        board.createNewBrick();
        setState(new PlayingState());
        viewModel.setGameOver(false);
        viewModel.setPaused(false);
        refreshViewModel();
        startGameLoop();
    }

    public void moveLeft() {
        currentState.handleInput(this, () -> {
            if(board.moveBrickLeft()) refreshViewModel();
        });
    }
    public void moveRight() {
        currentState.handleInput(this, () -> {
            if(board.moveBrickRight()) refreshViewModel();
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
        setState(new GameOverState());
        stopGameLoop();
        viewModel.setGameOver(true);
        leaderboardService.addScore("Player", board.getScore().getScore());
    }

    private void startGameLoop() {
        gameLoop.start();
    }

    private void stopGameLoop() {
        gameLoop.stop();
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

        // Draw Ghost Piece (Negative IDs for ghost styling)
        if (brick.getBrickData() != null) {
            drawPieceOnGrid(combined, brick.getBrickData(), brick.getXPosition(), brick.getGhostYPosition(), true);
            // Draw Active Piece
            drawPieceOnGrid(combined, brick.getBrickData(), brick.getXPosition(), brick.getYPosition(), false);
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
                        // If ghost, we might use a special ID or handle in View.
                        // For simplicity, we assume View handles IDs.
                        // Let's use negative ID for ghost if logic allows, or just standard ID.
                        // Here we just write the ID.
                        grid[targetY][targetX] = piece[r][c];
                    }
                }
            }
        }


    }
}
