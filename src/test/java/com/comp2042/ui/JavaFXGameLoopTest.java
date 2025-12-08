package com.comp2042.ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
@ExtendWith(MockitoExtension.class)
class JavaFXGameLoopTest {

    @Mock
    private Runnable tickAction;

    @Test
    void testSetTickAction() {
        assertDoesNotThrow(() -> {
            JavaFXGameLoop gameLoop = new JavaFXGameLoop();
            gameLoop.setTickAction(tickAction);
        });
    }

    @Test
    void testSetSpeed() {
        assertDoesNotThrow(() -> {
            JavaFXGameLoop gameLoop = new JavaFXGameLoop();
            gameLoop.setSpeed(500);
        });
    }
}