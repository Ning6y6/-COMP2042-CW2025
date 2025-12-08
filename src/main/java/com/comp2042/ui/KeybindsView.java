package com.comp2042.ui;

import com.comp2042.input.InputManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Window manager for KeybindsView.
 * Handles Stage creation, modal setup, and FXML loading.
 */
public class KeybindsView {

    private final InputManager inputManager;
    private Stage stage;

    public KeybindsView(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public void show() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/KeybindsView.fxml"));
            Parent root = loader.load();

            KeybindsFXController controller = loader.getController();

            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Key Bindings");

            Scene scene = new Scene(root, 350, 500);
            stage.setScene(scene);

            controller.setInputManager(inputManager);
            controller.setStage(stage);

            stage.show();
            root.setFocusTraversable(true);
            root.requestFocus();

        } catch (IOException e) {
            System.err.println("Failed to load KeybindsView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void close() {
        if (stage != null) {
            stage.close();
        }
    }
}
