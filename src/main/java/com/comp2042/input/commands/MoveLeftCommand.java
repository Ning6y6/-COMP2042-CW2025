package com.comp2042.input.commands;

import com.comp2042.GameController;
import com.comp2042.input.GameCommand;
public class MoveLeftCommand  implements GameCommand{
    @Override
    public void execute(GameController controller){
        controller.moveLeft();
    }
    @Override
    public String getName() {
        return "Move Left";
    }
}