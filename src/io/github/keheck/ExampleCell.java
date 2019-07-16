package io.github.keheck;

import java.awt.*;
import java.util.Random;

/**
 * This is an example class to demonstrate the API
 * it has an 8 states, with 9 colors representing
 * them (if the state ends up being >8)
 */

public final class ExampleCell extends Cell
{
    /**
     * @inheritDoc
     */
    @Override
    public Color getFillColor()
    {
        switch(getState())
        {
            case 0:  return new Color(0);
            case 1:  return new Color(255, 0, 0);
            case 2:  return new Color(255, 126, 0);
            case 3:  return new Color(255, 255, 0);
            case 4:  return new Color(0, 255, 0);
            case 5:  return new Color(0, 126, 255);
            case 6:  return new Color(255, 0, 255);
            case 7:  return new Color(0, 255, 255);
            default: return new Color(255, 255, 255);
        }
    }

    @Override
    public int getStates() { return 8; }

    /**
     * @inheritDoc
     */
    @Override
    public Color getStrokeColor() { return new Color(127, 127, 127); }

    /**
     * @inheritDoc
     */
    @Override
    public void randomizeState(Random rand) { setState(rand.nextInt(getStates())); }
}
