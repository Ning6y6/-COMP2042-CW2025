package com.comp2042.ui;

import com.comp2042.GameController;
import com.comp2042.input.commands.PauseCommand;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;


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

    /**
     * Called by FXMLLoader after FXML is loaded.
     * Sets up key event handling for pause key.
     */
    @FXML
    private void initialize() {
        // Key event handling will be set up after Scene is created
    }

    /**
     * Sets the GameController dependency.
     * Called by SettingsView after FXML is loaded.
     */
    public void setGameController(GameController controller) {
        this.gameController = controller;
        setupPauseKeyHandler();
        updateKeybindsButtonVisibility();
    }
    
    /**
     * Updates the visibility of the keybinds button based on game mode.
     * In challenge mode, keybinds button should be hidden.
     */
    private void updateKeybindsButtonVisibility() {
        if (keybindsButton != null && gameController != null) {
            boolean isChallengeMode = gameController.getGameMode() == com.comp2042.logic.GameMode.CHALLENGE;
            keybindsButton.setVisible(!isChallengeMode);
            keybindsButton.setManaged(!isChallengeMode);
        }
    }

    /**
     * Sets the Stage reference for window close handling.
     * Called by SettingsView after Stage is created.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
        setupPauseKeyHandler();
    }

    /**
     * Sets up pause key press handler on the Scene.
     * Delegates to GameController to handle pause toggle.
     * Only sets up once to avoid duplicate handlers.
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
    private void handleResume() {
        if (gameController != null) {
            gameController.resumeGame();
            // Resume BGM
            AudioManager.getInstance().resumeBGM();
            if (stage != null) {
                stage.close();
            }
        }
    }

    @FXML
    private void handleRestart() {
        if (gameController != null) {
            gameController.restartGame();
            // Resume BGM after restart (game was paused, so BGM was paused)
            AudioManager.getInstance().resumeBGM();
            if (stage != null) {
                stage.close();
            }
        }
    }

    @FXML
    private void handleKeybinds() {
        if (gameController != null) {
            new KeybindsView(gameController.getInputManager()).show();
        }
    }

    @FXML
    private void handleLeaderboard() {
        if (gameController != null) {
            new LeaderboardView(gameController.getLeaderboardService()).show();
        }
    }
    
    @FXML
    private void handleBackToMenu() {
        if (stage != null) {
            stage.close();
        }
        if (gameController != null) {
            // Call the back to main menu callback if set
            gameController.navigateToMainMenu();
        }
    }
}

