package io.github.keheck;

import processing.core.PApplet;

import java.awt.*;

import static io.github.keheck.CellularAutomatonAPI.isRunning;

public final class Initializer
{
    private static final Initializer INSTANCE = new Initializer();

    private Initializer() {}

    public static Initializer getInstance() { return INSTANCE; }

    /**
     * set the class the API will be using
     * @param clazz the class inheriting {@code io.github.keheck.Cell}
     * @see Cell
     * @return {@code INITIALIZER} to chain methods
     */
    public Initializer setClass(Class clazz)
    {
        if(!isRunning)
            if(clazz.getSuperclass().equals(Cell.class))
                CellularAutomatonAPI.clazz = clazz;
            else
                throw new IllegalArgumentException("Class has to extend io.github.keheck.Cell");
        else
            System.out.println("The API is already running!");
        return INSTANCE;
    }

    /**
     * set if the cells should be randomized
     * @param random should the cells be randomized?
     * @return {@code INITIALIZER} class to chain methods
     */
    public Initializer setRandom(boolean random)
    {
        if(!isRunning)
            CellularAutomatonAPI.random = random;
        else
            System.out.println("The API is already running!");
        return INSTANCE;
    }

    /**
     * set the intervall in milliseconds of every step
     * @param tickLength the time in milliseconds
     * @return {@code INITIALIZER} class to chain methods
     */
    public Initializer setTickLength(int tickLength)
    {
        if(!isRunning)
            CellularAutomatonAPI.tickLength = tickLength;
        else
            System.out.println("The API is already running!");
        return INSTANCE;
    }

    /**
     * @param gridHeight how many cells are in a colloumn
     * @return {@code INITIALIZER} class to chain methods
     */
    public Initializer setGridHeight(int gridHeight)
    {
        if(!isRunning)
            CellularAutomatonAPI.gridHeight = gridHeight;
        else
            System.out.println("The API is already running!");
        return INSTANCE;
    }

    /**
     * @param gridWidth how many cells are in a row
     * @return {@code INITIALIZER} to chain methods
     */
    public Initializer setGridWidth(int gridWidth)
    {
        if(!isRunning)
            CellularAutomatonAPI.gridWidth = gridWidth;
        else
            System.out.println("The API is already running!");
        return INSTANCE;
    }

    /**
     * @param cellSize how big is each cell (in pixels with scale 1)
     * @return {@code INITIALIZER} to chain methods
     */
    public Initializer setCellSize(int cellSize)
    {
        if(!isRunning)
            CellularAutomatonAPI.cellSize = cellSize;
        else
            System.out.println("The API is already running!");
        return INSTANCE;
    }

    /**
     * @param bgColor the background color when nothing's there
     * @return {@code INITIALIZER} to chain methods
     */
    public Initializer setBgColor(Color bgColor)
    {
        if(!isRunning)
            CellularAutomatonAPI.bgColor = bgColor;
        else
            System.out.println("The API is already running!");
        return INSTANCE;
    }

    /**
     * Call this when you done every stting needed
     */
    public void finish()
    {
        if(!isRunning)
        {
            isRunning = true;
            PApplet.main(CellularAutomatonAPI.class.getName());
        }
        else
            System.out.println("The API is already running!");
    }
}
