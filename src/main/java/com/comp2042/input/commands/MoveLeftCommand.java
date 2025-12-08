package com.comp2042.input.commands;

import com.comp2042.GameController;
import com.comp2042.input.GameCommand;
/**
 * MoveLeftCommand encapsulates a single input command that can be executed by the game.
 */
public class MoveLeftCommand  implements GameCommand{
    @Override
    /**
     * Performs the execute operation.
     *
     * @param controller the controller
     */
    public void execute(GameController controller){
        controller.moveLeft();
    }
    @Override
    /**
     * Returns the name.
     *
     * @return the result of the operation
     */
    public String getName() {
        return "Move Left";
    }
}