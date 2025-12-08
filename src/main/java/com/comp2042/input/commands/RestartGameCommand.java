package com.comp2042.input.commands;

import com.comp2042.GameController;
import com.comp2042.input.GameCommand;

/**
 * RestartGameCommand encapsulates a single input command that can be executed by the game.
 */
public class RestartGameCommand implements GameCommand {

    @Override
    /**
     * Performs the execute operation.
     *
     * @param controller the controller
     */
    public void execute(GameController controller) {
        controller.restartGame();
    }

    @Override
    /**
     * Returns the name.
     *
     * @return the result of the operation
     */
    public String getName() {
        return "Restart Game";
    }
}