package com.comp2042.input;

import com.comp2042.input.commands.MoveLeftCommand;
import com.comp2042.input.commands.MoveRightCommand;
import com.comp2042.input.commands.PauseCommand;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InputManagerTest {

    private InputManager inputManager;

    @BeforeEach
    void setUp() {
        inputManager = new InputManager();
    }

    @Test
    void testDefaultBindings() {
        assertTrue(inputManager.isKeyBound(KeyCode.A));
        assertTrue(inputManager.isKeyBound(KeyCode.D));
        assertTrue(inputManager.isKeyBound(KeyCode.W));
        assertTrue(inputManager.isKeyBound(KeyCode.S));
        assertTrue(inputManager.isKeyBound(KeyCode.P));
        assertTrue(inputManager.isKeyBound(KeyCode.N));
        assertTrue(inputManager.isKeyBound(KeyCode.B));
        assertTrue(inputManager.isKeyBound(KeyCode.SPACE));
    }

    @Test
    void testGetCommandNameForKey() {
        assertEquals("Move Left", inputManager.getCommandNameForKey(KeyCode.A));
        assertEquals("Move Right", inputManager.getCommandNameForKey(KeyCode.D));
        assertEquals("Pause / Resume ", inputManager.getCommandNameForKey(KeyCode.P));
    }

    @Test
    void testGetCommandNameForKeyUnbound() {
        assertNull(inputManager.getCommandNameForKey(KeyCode.F));
    }

    @Test
    void testGetKeyForCommand() {
        KeyCode key = inputManager.getKeyForCommand(MoveLeftCommand.class);
        assertEquals(KeyCode.A, key);
        
        key = inputManager.getKeyForCommand(PauseCommand.class);
        assertEquals(KeyCode.P, key);
    }

    @Test
    void testRebindKey() throws DuplicateKeyBindingException {
        MoveRightCommand command = new MoveRightCommand();
        inputManager.rebindKey(KeyCode.F, command);
        
        assertTrue(inputManager.isKeyBound(KeyCode.F));
        assertEquals("Move Right", inputManager.getCommandNameForKey(KeyCode.F));
        assertFalse(inputManager.isKeyBound(KeyCode.D));
    }

    @Test
    void testRebindKeyThrowsExceptionOnDuplicate() {
        MoveRightCommand command1 = new MoveRightCommand();
        
        assertThrows(DuplicateKeyBindingException.class, () -> {
            inputManager.rebindKey(KeyCode.A, command1);
        });
    }

    @Test
    void testRebindKeySameCommandNoException() throws DuplicateKeyBindingException {
        MoveLeftCommand command = new MoveLeftCommand();
        inputManager.rebindKey(KeyCode.A, command);
        
        assertTrue(inputManager.isKeyBound(KeyCode.A));
    }

    @Test
    void testRebindKeyRemovesOldBinding() throws DuplicateKeyBindingException {
        MoveLeftCommand command = new MoveLeftCommand();
        KeyCode oldKey = inputManager.getKeyForCommand(MoveLeftCommand.class);
        
        inputManager.rebindKey(KeyCode.Z, command);
        
        assertFalse(inputManager.isKeyBound(oldKey));
        assertTrue(inputManager.isKeyBound(KeyCode.Z));
    }

    @Test
    void testRebindKeyToAlreadyBoundKeyWithSameCommand() throws DuplicateKeyBindingException {
        MoveLeftCommand command = new MoveLeftCommand();
        KeyCode originalKey = inputManager.getKeyForCommand(MoveLeftCommand.class);
        
        inputManager.rebindKey(KeyCode.Z, command);
        inputManager.rebindKey(originalKey, command);
        
        assertFalse(inputManager.isKeyBound(KeyCode.Z));
        assertTrue(inputManager.isKeyBound(originalKey));
    }
}

