package com.comp2042.ui;

import com.comp2042.GameController;
import com.comp2042.input.commands.PauseCommand;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * SettingsFXController acts as a controller, coordinating between the game model and the view.
 */
public class SettingsFXController {

    @FXML
    private Button resumeButton;

    @FXML
    private Button restartButton;

    @FXML
    private Button keybindsButton;

    @FXML
    private Button leaderboardButton;

    @FXML
    private Button backToMenuButton;

    private GameController gameController;
    private Stage stage;
    private boolean pauseKeyHandlerSetup = false;

    @FXML
    /**
     * Performs the initialize operation.
     */
    private void initialize() {

    }

    /**
     * Sets the game controller.
     *
     * @param controller the controller
     */
    public void setGameController(GameController controller) {
        this.gameController = controller;
        setupPauseKeyHandler();
        updateKeybindsButtonVisibility();
    }

    /**
     * Updates the keybinds button visibility.
     */
    private void updateKeybindsButtonVisibility() {
        if (keybindsButton != null && gameController != null) {
            boolean isChallengeMode = gameController.getGameMode() == com.comp2042.logic.GameMode.CHALLENGE;
            keybindsButton.setVisible(!isChallengeMode);
            keybindsButton.setManaged(!isChallengeMode);
        }
    }

    /**
     * Sets the stage.
     *
     * @param stage the stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        setupPauseKeyHandler();
    }

    /**
     * Sets the pause key handler.
     */
    private void setupPauseKeyHandler() {
        if (pauseKeyHandlerSetup || stage == null || stage.getScene() == null || gameController == null) {
            return;
        }

        Scene scene = stage.getScene();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            KeyCode pauseKey = gameController.getInputManager().getKeyForCommand(PauseCommand.class);
            if (event.getCode() == pauseKey) {
                gameController.handlePauseToggle();
                stage.close();
                event.consume();
            }
        });
        pauseKeyHandlerSetup = true;
    }

    @FXML
    /**
     * Handles the resume.
     */
    private void handleResume() {
        if (gameController != null) {
            gameController.resumeGame();

            AudioManager.getInstance().resumeBGM();
            if (stage != null) {
                stage.close();
            }
        }
    }

    @FXML
    /**
     * Handles the restart.
     */
    private void handleRestart() {
        if (gameController != null) {
            gameController.restartGame();

            AudioManager.getInstance().resumeBGM();
            if (stage != null) {
                stage.close();
            }
        }
    }

    @FXML
    /**
     * Handles the keybinds.
     */
    private void handleKeybinds() {
        if (gameController != null) {
            new KeybindsView(gameController.getInputManager()).show();
        }
    }

    @FXML
    /**
     * Handles the leaderboard.
     */
    private void handleLeaderboard() {
        if (gameController != null) {
            new LeaderboardView(gameController.getLeaderboardService()).show();
        }
    }

    @FXML
    /**
     * Handles the back to menu.
     */
    private void handleBackToMenu() {
        if (stage != null) {
            stage.close();
        }
        if (gameController != null) {

            gameController.navigateToMainMenu();
        }
    }
}