package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameOverFXController {

    @FXML
    private Button restartButton;

    @FXML
    private Button backToMenuButton;

    @FXML
    private Label scoreLabel;

    private Stage stage;
    private Runnable onRestartCallback;
    private Runnable onBackToMenuCallback;

    @FXML
    private void initialize() {

    }

    @FXML
    private void handleRestart() {
        if (onRestartCallback != null) {
            onRestartCallback.run();
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

    public void setOnRestartCallback(Runnable callback) {
        this.onRestartCallback = callback;
    }

    public void setOnBackToMenuCallback(Runnable callback) {
        this.onBackToMenuCallback = callback;
    }
}
