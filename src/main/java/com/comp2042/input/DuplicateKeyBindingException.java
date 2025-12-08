package com.comp2042.input;

import javafx.scene.input.KeyCode;

/**
 * DuplicateKeyBindingException represents an application-specific exception type used in the game.
 */
public class DuplicateKeyBindingException extends Exception {

    private final KeyCode keyCode;
    private final String existingCommand;

    public DuplicateKeyBindingException(KeyCode keyCode, String existingCommand) {
        super(String.format("Key '%s' is already bound to '%s'", keyCode, existingCommand));
        this.keyCode = keyCode;
        this.existingCommand = existingCommand;
    }

    /**
     * Returns the key code.
     *
     * @return the result of the operation
     */
    public KeyCode getKeyCode() {
        return keyCode;
    }

    /**
     * Returns the existing command.
     *
     * @return the result of the operation
     */
    public String getExistingCommand() {
        return existingCommand;
    }
}