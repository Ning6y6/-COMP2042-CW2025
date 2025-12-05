package com.comp2042.ui;

import com.comp2042.input.DuplicateKeyBindingException;
import com.comp2042.input.GameCommand;
import com.comp2042.input.InputManager;
import com.comp2042.input.commands.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import java.util.LinkedHashMap;
import java.util.Map;


public class KeybindsView {

    private final InputManager inputManager;
    private final Map<String, Class<? extends GameCommand>> configurableCommands;

    public KeybindsView(InputManager inputManager) {
        this.inputManager = inputManager;
        this.configurableCommands = new LinkedHashMap<>();
        configurableCommands.put("Move Left", MoveLeftCommand.class);
        configurableCommands.put("Move Right", MoveRightCommand.class);
        configurableCommands.put("Rotate", RotateCommand.class);
        configurableCommands.put("Soft Drop", SoftDropCommand.class);
        configurableCommands.put("Pause/Resume", PauseCommand.class);
        configurableCommands.put("Restart Game", RestartGameCommand.class);
        configurableCommands.put("Hold", HoldCommand.class);
        configurableCommands.put("Hard Drop", HardDropCommand.class);
    }

    public void show() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Key Bindings");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: #2b2b2b; -fx-padding: 20;");

        Label header = new Label("Click button to rebind");
        header.setStyle("-fx-text-fill: white; -fx-font-size: 18px;");
        layout.getChildren().add(header);

        for (Map.Entry<String, Class<? extends GameCommand>> entry : configurableCommands.entrySet()) {
            layout.getChildren().add(createBindRow(entry.getKey(), entry.getValue()));
        }

        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> stage.close());
        layout.getChildren().add(closeBtn);

        Scene scene = new Scene(layout, 350, 500);
        stage.setScene(scene);
        stage.show();
        layout.setFocusTraversable(true);
        layout.requestFocus();
    }

    private HBox createBindRow(String labelText, Class<? extends GameCommand> commandClass) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: white; -fx-min-width: 100px;");

        KeyCode currentKey = inputManager.getKeyForCommand(commandClass);
        Button keyBtn = new Button(formatKeyCode(currentKey));
        keyBtn.setMinWidth(100);

        keyBtn.setOnAction(e -> {
            String originalText = keyBtn.getText();
            keyBtn.setText("Press any key...");

            Scene scene = keyBtn.getScene();
            if (scene != null) {
                javafx.scene.Node root = scene.getRoot();
                root.setFocusTraversable(true);
                root.requestFocus();

                final java.util.concurrent.atomic.AtomicReference<javafx.event.EventHandler<javafx.scene.input.KeyEvent>> handlerRef = 
                    new java.util.concurrent.atomic.AtomicReference<>();

                javafx.event.EventHandler<javafx.scene.input.KeyEvent> keyHandler = event -> {
                    KeyCode newCode = event.getCode();

                    if (isValidBindingKey(newCode)) {
                        javafx.event.EventHandler<javafx.scene.input.KeyEvent> handler = handlerRef.get();
                        if (handler != null) {
                            scene.removeEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, handler);
                            handlerRef.set(null);
                        }

                        performKeyRebind(newCode, commandClass, keyBtn, originalText);
                    }
                };

                handlerRef.set(keyHandler);

                scene.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, keyHandler);
            }
        });

        row.getChildren().addAll(label, keyBtn);
        return row;
    }

    private void performKeyRebind(KeyCode newCode, Class<? extends GameCommand> commandClass, 
                                   Button keyBtn, String originalText) {
        try {
            inputManager.rebindKey(newCode, commandClass.getDeclaredConstructor().newInstance());
            keyBtn.setText(formatKeyCode(newCode));
        } catch (DuplicateKeyBindingException ex) {
            showDuplicateKeyError(ex);
            keyBtn.setText(originalText);
        } catch (Exception ex) {
            showGenericError("Failed to rebind key: " + ex.getMessage());
            keyBtn.setText(originalText);
        }
    }

    private String formatKeyCode(KeyCode keyCode) {
        if (keyCode == KeyCode.UNDEFINED) {
            return "Not Set";
        }
        return switch (keyCode) {
            case LEFT -> "← Left";
            case RIGHT -> "→ Right";
            case UP -> "↑ Up";
            case DOWN -> "↓ Down";
            default -> keyCode.toString();
        };
    }

    private boolean isValidBindingKey(KeyCode keyCode) {
        if (keyCode == KeyCode.UNDEFINED) {
            return false;
        }

        return keyCode != KeyCode.SHIFT && 
               keyCode != KeyCode.CONTROL && 
               keyCode != KeyCode.ALT &&
               keyCode != KeyCode.META &&
               keyCode != KeyCode.WINDOWS;
    }

    private void showDuplicateKeyError(DuplicateKeyBindingException ex) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Duplicate Key Binding");
        alert.setHeaderText("Key Already Bound");
        alert.setContentText(String.format(
            "The key '%s' is already bound to '%s'.\n\n" +
            "Please choose a different key or unbind the existing command first.",
            ex.getKeyCode(), ex.getExistingCommand()
        ));
        alert.showAndWait();
    }

    private void showGenericError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Key Binding Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}