package com.comp2042.input;

import com.comp2042.GameController;

/**
 * GameCommand encapsulates a single input command that can be executed by the game.
 */
public interface GameCommand {
    /**
     * Performs the execute operation.
     *
     * @param controller the controller
     */
    void execute(GameController controller);
    /**
     * Returns the name.
     *
     * @return the result of the operation
     */
    String getName();
}