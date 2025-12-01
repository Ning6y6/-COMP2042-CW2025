package com.comp2042.input;

import com.comp2042.GameController;


public interface GameCommand {
    void execute(GameController controller);
    String getName();
}