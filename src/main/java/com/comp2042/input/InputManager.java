package com.comp2042.input;

import com.comp2042.GameController;
import com.comp2042.input.commands.*;
import javafx.scene.input.KeyCode;
import java.util.HashMap;
import java.util.Map;

/**
 * InputManager manages and coordinates input behaviour.
 */
public class InputManager {

    private final Map<KeyCode, GameCommand> keyBindingMap;

    public InputManager() {
        this.keyBindingMap = new HashMap<>();
        loadDefaultBindings();
    }

    /**
     * Performs the load default bindings operation.
     */
    private void loadDefaultBindings() {
        keyBindingMap.put(KeyCode.A, new MoveLeftCommand());
        keyBindingMap.put(KeyCode.D, new MoveRightCommand());
        keyBindingMap.put(KeyCode.W, new RotateCommand());
        keyBindingMap.put(KeyCode.S, new SoftDropCommand());
        keyBindingMap.put(KeyCode.P, new PauseCommand());
        keyBindingMap.put(KeyCode.N, new RestartGameCommand());
        keyBindingMap.put(KeyCode.B, new HoldCommand());
        keyBindingMap.put(KeyCode.SPACE, new HardDropCommand());
    }

    /**
     * Performs the rebind key operation.
     *
     * @param newKey the newKey
     * @param command the command
     */
    public void rebindKey(KeyCode newKey, GameCommand command) throws DuplicateKeyBindingException {
        GameCommand existingCommand = keyBindingMap.get(newKey);
        if (existingCommand != null && !existingCommand.getClass().equals(command.getClass())) {
            String existingCommandName = existingCommand.getName();
            throw new DuplicateKeyBindingException(newKey, existingCommandName);
        }

        KeyCode oldKey = getKeyForCommand(command.getClass());
        if (oldKey != KeyCode.UNDEFINED) {
            keyBindingMap.remove(oldKey);
        }

        keyBindingMap.put(newKey, command);
    }

    /**
     * Returns whether the key bound is true.
     *
     * @param keyCode the keyCode
     * @return the result of the operation
     */
    public boolean isKeyBound(KeyCode keyCode) {
        return keyBindingMap.containsKey(keyCode);
    }

    /**
     * Returns the command name for key.
     *
     * @param keyCode the keyCode
     * @return the result of the operation
     */
    public String getCommandNameForKey(KeyCode keyCode) {
        GameCommand command = keyBindingMap.get(keyCode);
        return command != null ? command.getName() : null;
    }

    /**
     * Handles the input.
     *
     * @param key the key
     * @param controller the controller
     */
    public void handleInput(KeyCode key, GameController controller) {
        if (keyBindingMap.containsKey(key)) {
            keyBindingMap.get(key).execute(controller);
        }
    }

    /**
     * Returns the key for command.
     *
     * @param commandClass the commandClass
     * @return the result of the operation
     */
    public KeyCode getKeyForCommand(Class<? extends GameCommand> commandClass) {
        for (Map.Entry<KeyCode, GameCommand> entry : keyBindingMap.entrySet()) {
            if (commandClass.isInstance(entry.getValue())) {
                return entry.getKey();
            }
        }
        return KeyCode.UNDEFINED;
    }
}