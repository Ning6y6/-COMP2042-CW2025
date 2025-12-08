package com.comp2042.state;

import com.comp2042.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class PausedStateTest {

    private PausedState pausedState;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        pausedState = new PausedState();
        gameController = Mockito.mock(GameController.class);
    }

    @Test
    void handleInput() {
        Runnable action = Mockito.mock(Runnable.class);
        pausedState.handleInput(gameController, action);
        verify(action, never()).run();
    }

    @Test
    void update() {
        pausedState.update(gameController);
        verify(gameController, never()).performGameLoopStep();
    }

    @Test
    void handleTogglePause() {
        pausedState.handleTogglePause(gameController);
        verify(gameController).resumeGame();
    }
}