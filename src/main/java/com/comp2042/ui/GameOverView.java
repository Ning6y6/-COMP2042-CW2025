package com.comp2042.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Window manager for Game Over view.
 * Handles only Stage creation, modal setup, and FXML loading.
 * All UI creation and styling is handled by FXML and CSS.
 */
public class GameOverView {

    private Stage stage;
    private GameOverFXController fxController;
    private Runnable onRestartCallback;
    private Runnable onBackToMenuCallback;

    public GameOverView() {
    }

    /**
     * Sets callbacks for game over screen actions.
     */
    public void setCallbacks(Runnable onRestart, Runnable onBackToMenu) {
        this.onRestartCallback = onRestart;
        this.onBackToMenuCallback = onBackToMenu;
    }

    /**
     * Shows the Game Over window as a modal dialog.
     * Loads FXML, injects dependencies, and sets up callbacks.
     */
    public void show(Stage parentStage, int finalScore) {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/GameOverView.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("GameOverView.fxml not found at /fxml/GameOverView.fxml");
            }
            Parent root = loader.load();

            // Get controller from loader
            fxController = loader.getController();
            if (fxController == null) {
                throw new IllegalStateException("GameOverFXController not found in FXML");
            }

            // Create and configure Stage
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(parentStage);
            stage.setTitle("Game Over");

            // Create Scene and set it on Stage
            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);

            // Set Stage reference in controller
            fxController.setStage(stage);

            // Set score
            fxController.setScore(finalScore);

            // Set callbacks
            fxController.setOnRestartCallback(onRestartCallback);
            fxController.setOnBackToMenuCallback(onBackToMenuCallback);

            // Show the Stage
            stage.show();
            stage.toFront(); // Ensure it's on top

        } catch (IOException e) {
            System.err.println("Failed to load GameOverView.fxml: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load GameOverView.fxml", e);
        } catch (Exception e) {
            System.err.println("Error showing Game Over window: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to show Game Over window", e);
        }
    }

    /**
     * Closes the Game Over window.
     */
    public void close() {
        if (stage != null) {
            stage.close();
        }
    }
}

