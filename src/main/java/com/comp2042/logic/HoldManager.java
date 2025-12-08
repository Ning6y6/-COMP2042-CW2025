package com.comp2042.logic;

import com.comp2042.logic.bricks.Brick;
import java.util.Optional;

/**
 * HoldManager manages and coordinates hold behaviour.
 */
public class HoldManager {

    private Brick heldBrick;
    private boolean canSwap;

    public HoldManager() {
        this.heldBrick = null;
        this.canSwap = true;
    }

    /**
     * Performs the swap operation.
     *
     * @param currentBrick the currentBrick
     * @return the result of the operation
     */
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

    /**
     * Performs the reset swap flag operation.
     */
    public void resetSwapFlag() {
        this.canSwap = true;
    }

    /**
     * Performs the reset operation.
     */
    public void reset() {
        this.heldBrick = null;
        this.canSwap = true;
    }

    /**
     * Returns the held brick.
     *
     * @return the result of the operation
     */
    public Brick getHeldBrick() {
        return heldBrick;
    }

    /**
     * Returns whether the can swap is true.
     *
     * @return the result of the operation
     */
    public boolean isCanSwap() {
        return canSwap;
    }
}