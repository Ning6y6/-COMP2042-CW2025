package com.comp2042.state;

import com.comp2042.GameController;

public interface GameState {
    void handleInput(GameController context, Runnable action);
    void update(GameController context);
    void handleTogglePause(GameController context);
}