package com.comp2042.ui;

import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Panel displayed when game is over.
 * Uses CSS class for styling.
 */
public class GameOverPanel extends BorderPane {

    public GameOverPanel() {
        getStyleClass().add("game-over-panel");

        Label gameOverLabel = new Label("GAME OVER");
        gameOverLabel.getStyleClass().add("game-over-label");
        setCenter(gameOverLabel);
    }
}
