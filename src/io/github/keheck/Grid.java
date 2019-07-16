package io.github.keheck;

import processing.core.PVector;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Random;

import static io.github.keheck.CellularAutomatonAPI.cam;
import static io.github.keheck.CellularAutomatonAPI.instance;

/**
 * The space the whole simulation is contained in. It updates
 * every tick (when {@link CellularAutomatonAPI#simMode} is 1, a tick is defined
 * in milliseconds by the {@link CellularAutomatonAPI#tickLength} field) or every
 * time you hit the enter key (see {@link CellularAutomatonAPI#keyPressed()}).
 */

final class Grid<C extends Cell>
{
    private int width;
    private int height;
    private int cellSize;
    private Cell[][] cells;
    private Class<C> clazz;

    /**
     * Constructs the Grid with the given width, height, cell size and the path to
     * a class inheriting {@link Cell}.
     * @param width the width of the grid (in cells)
     * @param height the height of the grid (in cells)
     * @param cellSize the size of avery cell
     */
    Grid(int width, int height, int cellSize, Class<C> clazz)
    {
        this.width = width;
        this.height = height;
        this.cells = new Cell[width][height];
        this.cellSize = cellSize;
        this.clazz = clazz;

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                try
                {
                    cells[x][y] = clazz.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
                {
                    handleException(e);
                }
            }
        }
    }

    private void handleException(Exception e)
    {
        if(e instanceof InstantiationException)
        {
            System.err.println("Couldn't instantiate object of type " + clazz.getName());
            e.printStackTrace();
            System.exit(1);
        }
        else if(e instanceof NoSuchMethodException)
        {
            System.err.println("Couldn't find method.");
            e.printStackTrace();
            System.exit(1);
        }
        else if(e instanceof IllegalAccessException)
        {
            System.err.println("Access level too low");
            e.printStackTrace();
            System.exit(1);
        }
        else
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * when {@link CellularAutomatonAPI#random} it true, it calls every cell's
     * {@link Cell#randomizeState(Random)} method.
     */
    void randomize()
    {
        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                cells[i][j].randomizeState(new Random());
            }
        }
    }

    Cell getCellAt(PVector coords)
    {
        if(coords.x < 0 || coords.y < 0 || coords.x >= width || coords.y >= height)
            return cells[0][0];

        return cells[(int)coords.x][(int)coords.y];
    }

    /**
     * Displays every cell with the color representing the state defined in
     * {@link Cell#getFillColor()} with the stroke color defined in
     * {@link Cell#getStrokeColor()} when {@link CellularAutomatonAPI#withStroke} is true.
     */
    void show()
    {
        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                instance.fill(cells[x][y].getFillColor().getRGB());
                if(instance.withStroke) instance.stroke(cells[x][y].getStrokeColor().getRGB());
                else instance.noStroke();
                instance.rect(x*cellSize, y*cellSize, cellSize, cellSize);
            }
        }
    }

    /**
     * converts the mouse position on the screen to grid coordinates
     * depending on:
     * <ul>
     *     <li>the current scale</li>
     *     <li>the current camera position</li>
     * </ul>
     * @param relMouseLoc the location of the mouse relative to the window location
     * @return a {@code PVector} representing two indices for the {@link #cells} array
     */
    PVector getBoardCoords(Point relMouseLoc)
    {
        if(cam.pos.x == 0 && cam.pos.y == 0 && cam.scale == 1)
        {
            double cellSize = this.cellSize;

            double scaledX = relMouseLoc.getX()/cellSize-0.5;
            double scaledY = relMouseLoc.getY()/cellSize-0.5;

            return new PVector((float)Math.round(scaledX), (float)Math.round(scaledY));
        }
        else
        {
            System.out.println("Please reset the board first! (I'm too stupid to get the formular right)");
            return new PVector(0, 0);
        }
    }

    private Cell copy(Cell cell) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException
    {
        return (Cell)clazz.getSuperclass().getDeclaredConstructor(int.class).newInstance(cell.getState());
    }

    /**
     * Applies the ruleset defined in the {@link Cell#changeState(Cell[])} method.
     * It creates a copy of every cell so to not interferre with the original array
     */
    void update()
    {
        Cell[][] prevState = new Cell[width][height];

        for(int i = 0; i < width; i++)
        {
            for(int j = 0; j < height; j++)
            {
                try
                {
                    Cell cell = cells[i][j];
                    prevState[i][j] = copy(cell);
                }
                catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e)
                {
                    handleException(e);
                }
            }
        }

        for(int x = 0; x < width; x++)
        {
            for(int y = 0; y < height; y++)
            {
                ArrayList<Cell> surrounding = new ArrayList<>();

                if(x > 0 && y > 0 && x < width-1 && y < height-1)
                {
                    for(int i = -1; i <= 1; i++)
                        for(int j = -1; j <= 1; j++)
                            if(!(i == 0 && j == 0))
                                surrounding.add(prevState[x+i][y+j]);
                }
                else if(x == 0 && y == 0)
                {
                    surrounding.add(prevState[x+1][y]  );
                    surrounding.add(prevState[x+1][y+1]);
                    surrounding.add(prevState[x]  [y+1]);
                }
                else if(x == width-1 && y == 0)
                {
                    surrounding.add(prevState[x-1][y]);
                    surrounding.add(prevState[x-1][y+1]);
                    surrounding.add(prevState[x][y+1]);
                }
                else if(x == 0 && y == height-1)
                {
                    surrounding.add(prevState[x]  [y-1]);
                    surrounding.add(prevState[x+1][y-1]);
                    surrounding.add(prevState[x+1][y]  );
                }
                else if(x == width-1 && y == height-1)
                {
                    surrounding.add(prevState[x-1][y]  );
                    surrounding.add(prevState[x-1][y-1]);
                    surrounding.add(prevState[x]  [y-1]);
                }
                else if(x == 0)
                {
                    surrounding.add(prevState[x]  [y-1]);
                    surrounding.add(prevState[x+1][y-1]);
                    surrounding.add(prevState[x+1][y]  );
                    surrounding.add(prevState[x+1][y+1]);
                    surrounding.add(prevState[x]  [y+1]);
                }
                else if(x == width-1)
                {
                    surrounding.add(prevState[x]  [y-1]);
                    surrounding.add(prevState[x-1][y-1]);
                    surrounding.add(prevState[x-1][y]  );
                    surrounding.add(prevState[x-1][y+1]);
                    surrounding.add(prevState[x]  [y+1]);
                }
                else if(y == 0)
                {
                    surrounding.add(prevState[x-1][y]  );
                    surrounding.add(prevState[x-1][y+1]);
                    surrounding.add(prevState[x]  [y+1]);
                    surrounding.add(prevState[x+1][y+1]);
                    surrounding.add(prevState[x+1][y]  );
                }
                else if(y == height-1)
                {
                    surrounding.add(prevState[x-1][y]  );
                    surrounding.add(prevState[x-1][y-1]);
                    surrounding.add(prevState[x]  [y-1]);
                    surrounding.add(prevState[x+1][y-1]);
                    surrounding.add(prevState[x+1][y]  );
                }
                else throw new IllegalStateException(String.format("x = %d, y = %d, height = %d, width = %d", x, y, width, height));

                cells[x][y].changeState(surrounding.toArray(new Cell[0]));
            }
        }
    }
}
