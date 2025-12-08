package com.comp2042.ui;

import com.comp2042.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsView {

    private final GameController controller;
    private Stage stage;
    private SettingsFXController fxController;

    public SettingsView(GameController controller) {
        this.controller = controller;
    }

    public void show() {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SettingsView.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("SettingsView.fxml not found at /fxml/SettingsView.fxml");
            }
            Parent root = loader.load();

            fxController = loader.getController();
            if (fxController == null) {
                throw new IllegalStateException("SettingsFXController not found in FXML");
            }

            fxController.setGameController(controller);

            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Settings");

            Scene scene = new Scene(root, 300, 500);
            stage.setScene(scene);

            fxController.setStage(stage);

            stage.setOnCloseRequest(e -> {
                controller.resumeGame();
            });

            stage.show();
            stage.toFront();

        } catch (IOException e) {
            System.err.println("Failed to load SettingsView.fxml: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load SettingsView.fxml", e);
        } catch (Exception e) {
            System.err.println("Error showing Settings window: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to show Settings window", e);
        }
    }

    public void close() {
        if (stage != null) {
            stage.close();
        }
    }
}