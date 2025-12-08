package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller for VictoryView.
 * Acts as a mediator between the FXML view and navigation logic.
 */
public class VictoryFXController {

    @FXML
    private Button playAgainButton;

    @FXML
    private Button backToMenuButton;

    @FXML
    private Label scoreLabel;

    private Stage stage;
    private Runnable onPlayAgainCallback;
    private Runnable onBackToMenuCallback;

    @FXML
    private void initialize() {
        // Initialization if needed
    }

    @FXML
    private void handlePlayAgain() {
        if (onPlayAgainCallback != null) {
            onPlayAgainCallback.run();
        }
        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    private void handleBackToMenu() {
        if (onBackToMenuCallback != null) {
            onBackToMenuCallback.run();
        }
        if (stage != null) {
            stage.close();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setScore(int score) {
        if (scoreLabel != null) {
            scoreLabel.setText("Score: " + score);
        }
    }

    public void setOnPlayAgainCallback(Runnable callback) {
        this.onPlayAgainCallback = callback;
    }

    public void setOnBackToMenuCallback(Runnable callback) {
        this.onBackToMenuCallback = callback;
    }
}

