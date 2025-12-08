package com.comp2042.ui;

import com.comp2042.input.DuplicateKeyBindingException;
import com.comp2042.input.GameCommand;
import com.comp2042.input.InputManager;
import com.comp2042.input.commands.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class KeybindsFXController {

    @FXML
    private VBox rootContainer;

    @FXML
    private VBox bindingsContainer;

    private InputManager inputManager;
    private Stage stage;
    private final Map<String, Class<? extends GameCommand>> configurableCommands;

    public KeybindsFXController() {
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

    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
        populateBindings();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void populateBindings() {
        bindingsContainer.getChildren().clear();
        for (Map.Entry<String, Class<? extends GameCommand>> entry : configurableCommands.entrySet()) {
            bindingsContainer.getChildren().add(createBindRow(entry.getKey(), entry.getValue()));
        }
    }

    private HBox createBindRow(String labelText, Class<? extends GameCommand> commandClass) {
        HBox row = new HBox();
        row.getStyleClass().add("keybind-row");
        row.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(labelText);
        label.getStyleClass().add("keybind-label");

        KeyCode currentKey = inputManager.getKeyForCommand(commandClass);
        Button keyBtn = new Button(formatKeyCode(currentKey));
        keyBtn.getStyleClass().add("keybind-button");

        keyBtn.setOnAction(e -> startKeyCapture(keyBtn, commandClass));

        row.getChildren().addAll(label, keyBtn);
        return row;
    }

    private void startKeyCapture(Button keyBtn, Class<? extends GameCommand> commandClass) {
        String originalText = keyBtn.getText();
        keyBtn.setText("Press any key...");

        Scene scene = keyBtn.getScene();
        if (scene == null) return;

        Node root = scene.getRoot();
        root.setFocusTraversable(true);
        root.requestFocus();

        AtomicReference<EventHandler<KeyEvent>> handlerRef = new AtomicReference<>();

        EventHandler<KeyEvent> keyHandler = event -> {
            KeyCode newCode = event.getCode();

            if (isValidBindingKey(newCode)) {
                EventHandler<KeyEvent> handler = handlerRef.get();
                if (handler != null) {
                    scene.removeEventFilter(KeyEvent.KEY_PRESSED, handler);
                    handlerRef.set(null);
                }
                performKeyRebind(newCode, commandClass, keyBtn, originalText);
            }
        };

        handlerRef.set(keyHandler);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, keyHandler);
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
            case LEFT -> "\u2190 Left";
            case RIGHT -> "\u2192 Right";
            case UP -> "\u2191 Up";
            case DOWN -> "\u2193 Down";
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

    @FXML
    private void handleClose() {
        if (stage != null) {
            stage.close();
        }
    }
}