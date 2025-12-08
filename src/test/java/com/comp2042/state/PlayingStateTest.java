package com.comp2042.state;

import com.comp2042.GameController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.verify;

class PlayingStateTest {

    private PlayingState playingState;
    private GameController gameController;

    @BeforeEach
    void setUp() {
        playingState = new PlayingState();
        gameController = Mockito.mock(GameController.class);
    }

    @Test
    void handleInput() {
        Runnable action = Mockito.mock(Runnable.class);
        playingState.handleInput(gameController, action);
        verify(action).run();
    }

    @Test
    void update() {
        playingState.update(gameController);
        verify(gameController).performGameLoopStep();
    }

    @Test
    void handleTogglePause() {
        playingState.handleTogglePause(gameController);
        verify(gameController).pauseGame();
    }
}