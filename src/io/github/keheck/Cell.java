package io.github.keheck;

import java.awt.*;
import java.util.Random;

/**
 * The interface used by the {@link Grid} class
 * to display every single cell and perform
 * the given ruleset. It has a copy function
 * used by the {@link Grid#update()} function
 * to perform the ruleset without manipulating the new layout
 *
 * @see Cell#changeState(Cell[])
 */

public class Cell
{
    private int state = 0;

    Cell(int state) { this.state = state; }

    public Cell() { this(0); }

    /**
     * Thr ruleset applied every step (either every tick
     * or every manual step)
     *
     * The default ruleset is that the state of all
     * cells are summed up and then the state
     * is equal to the sum mod(%) 8
     *
     * @param surrCells the cells surrounding <b>this</b> cell
     */
    public void changeState(Cell[] surrCells)
    {
        int defInt = 0;

        for(Cell cell : surrCells)
            defInt += cell.getState();

        defInt %= 8;

        setState(defInt);
    }

    /**
     * @return the number of possible states (only used for cycling the cell state on click)
     */
    public int getStates() { return 1; }

    /**
     * @return the customized stroke color, if {@link CellularAutomatonAPI#withStroke} is true
     */
    public Color getStrokeColor() { return new Color(0); }

    /**
     * Sets the new state determined by the ruleset
     * @param state the new state
     */
    public final void setState(int state) { this.state = state; }

    /**
     * @return the current state
     */
    public final int getState() { return state; }

    /**
     * @return the color of the current state (fully customizeable)
     */
    public Color getFillColor() { return new Color(255, 255, 255); }

    public final void cycleState()
    {
        if(getState() == getStates()-1)
            setState(0);
        else
            setState(getState()+1);
    }

    /**
     * Called when {@link Grid#randomize()} is called. randomization
     * in customizeable
     * @param rand a object of the {@link Random} class used in the
     *             method
     */
    public void randomizeState(Random rand) {}
}
