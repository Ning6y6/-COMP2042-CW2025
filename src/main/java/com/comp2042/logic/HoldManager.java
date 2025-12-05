package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;
import java.util.Optional;

public class HoldManager {

    private Brick heldBrick;
    private boolean canSwap;

    public HoldManager() {
        this.heldBrick = null;
        this.canSwap = true;
    }

    public Optional<Brick> swap(Brick currentBrick) {
        if (!canSwap) {
            return Optional.of(currentBrick);
        }

        Brick brickToPlay;
        if (heldBrick == null) {
            heldBrick = currentBrick;
            brickToPlay = null;
        } else {
            Brick temp = heldBrick;
            heldBrick = currentBrick;
            brickToPlay = temp;
        }

        canSwap = false;
        return Optional.ofNullable(brickToPlay);
    }

    public void resetSwapFlag() {
        this.canSwap = true;
    }

    public void reset() {
        this.heldBrick = null;
        this.canSwap = true;
    }

    public Brick getHeldBrick() {
        return heldBrick;
    }

    public boolean isCanSwap() {
        return canSwap;
    }
}