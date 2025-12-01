package com.comp2042.input;

import javafx.scene.input.KeyCode;

public class DuplicateKeyBindingException extends Exception {
    
    private final KeyCode keyCode;
    private final String existingCommand;
    
    public DuplicateKeyBindingException(KeyCode keyCode, String existingCommand) {
        super(String.format("Key '%s' is already bound to '%s'", keyCode, existingCommand));
        this.keyCode = keyCode;
        this.existingCommand = existingCommand;
    }
    
    public KeyCode getKeyCode() {
        return keyCode;
    }
    
    public String getExistingCommand() {
        return existingCommand;
    }
}

