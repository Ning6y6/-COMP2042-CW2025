package com.comp2042.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Window manager for Victory view.
 * Handles only Stage creation, modal setup, and FXML loading.
 * All UI creation and styling is handled by FXML and CSS.
 */
public class VictoryView {

    private Stage stage;
    private VictoryFXController fxController;
    private Runnable onPlayAgainCallback;
    private Runnable onBackToMenuCallback;

    public VictoryView() {
    }

    /**
     * Sets callbacks for victory screen actions.
     */
    public void setCallbacks(Runnable onPlayAgain, Runnable onBackToMenu) {
        this.onPlayAgainCallback = onPlayAgain;
        this.onBackToMenuCallback = onBackToMenu;
    }

    /**
     * Shows the Victory window as a modal dialog.
     * Loads FXML, injects dependencies, and sets up callbacks.
     */
    public void show(Stage parentStage, int finalScore) {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/VictoryView.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("VictoryView.fxml not found at /fxml/VictoryView.fxml");
            }
            Parent root = loader.load();

            // Get controller from loader
            fxController = loader.getController();
            if (fxController == null) {
                throw new IllegalStateException("VictoryFXController not found in FXML");
            }

            // Create and configure Stage
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(parentStage);
            stage.setTitle("Victory!");

            // Create Scene and set it on Stage
            Scene scene = new Scene(root, 400, 300);
            stage.setScene(scene);

            // Set Stage reference in controller
            fxController.setStage(stage);

            // Set score
            fxController.setScore(finalScore);

            // Set callbacks
            fxController.setOnPlayAgainCallback(onPlayAgainCallback);
            fxController.setOnBackToMenuCallback(onBackToMenuCallback);

            // Show the Stage
            stage.show();
            stage.toFront(); // Ensure it's on top

        } catch (IOException e) {
            System.err.println("Failed to load VictoryView.fxml: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load VictoryView.fxml", e);
        } catch (Exception e) {
            System.err.println("Error showing Victory window: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to show Victory window", e);
        }
    }

    /**
     * Closes the Victory window.
     */
    public void close() {
        if (stage != null) {
            stage.close();
        }
    }
}

