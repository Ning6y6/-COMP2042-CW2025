package com.comp2042.state;

import com.comp2042.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class GameOverStateTest {

    private GameOverState gameOverState;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        gameOverState = new GameOverState();
        gameController = Mockito.mock(GameController.class);
    }

    @Test
    void handleInput() {
        Runnable action = Mockito.mock(Runnable.class);
        gameOverState.handleInput(gameController, action);
        verify(action, never()).run();
    }

    @Test
    void update() {
        gameOverState.update(gameController);
        verify(gameController, never()).performGameLoopStep();
    }

    @Test
    void handleTogglePause() {
        gameOverState.handleTogglePause(gameController);
        verify(gameController, never()).pauseGame();
        verify(gameController, never()).resumeGame();
    }
}