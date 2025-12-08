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

/**
 * KeybindsFXController acts as a controller, coordinating between the game model and the view.
 */
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

    /**
     * Sets the input manager.
     *
     * @param inputManager the inputManager
     */
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
        populateBindings();
    }

    /**
     * Sets the stage.
     *
     * @param stage the stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Performs the populate bindings operation.
     */
    private void populateBindings() {
        bindingsContainer.getChildren().clear();
        for (Map.Entry<String, Class<? extends GameCommand>> entry : configurableCommands.entrySet()) {
            bindingsContainer.getChildren().add(createBindRow(entry.getKey(), entry.getValue()));
        }
    }

    /**
     * Performs the create bind row operation.
     *
     * @param labelText the labelText
     * @param commandClass the commandClass
     * @return the result of the operation
     */
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

    /**
     * Performs the start key capture operation.
     *
     * @param keyBtn the keyBtn
     * @param commandClass the commandClass
     */
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

    /**
     * Performs the format key code operation.
     *
     * @param keyCode the keyCode
     * @return the result of the operation
     */
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

    /**
     * Returns whether the valid binding key is true.
     *
     * @param keyCode the keyCode
     * @return the result of the operation
     */
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

    /**
     * Performs the show duplicate key error operation.
     *
     * @param ex the ex
     */
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

    /**
     * Performs the show generic error operation.
     *
     * @param message the message
     */
    private void showGenericError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Key Binding Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    /**
     * Handles the close.
     */
    private void handleClose() {
        if (stage != null) {
            stage.close();
        }
    }
}