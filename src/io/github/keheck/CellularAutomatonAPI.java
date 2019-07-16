package io.github.keheck;

import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

import java.awt.*;

/**
 * This is the class that should be called from the outside
 *
 * To get the API running:
 * - call any combionation of these methods:
 *   - {@link Initializer#setClass(Class)}
 *   - {@link Initializer#setRandom(boolean)}
 *   - {@link Initializer#setTickLength(int)}
 *   - {@link Initializer#setGridHeight(int)}
 *   - {@link Initializer#setGridWidth(int)}
 *   - {@link Initializer#setCellSize(int)}
 *   - {@link Initializer#setBgColor(Color)}
 * * NOTE 1: since the set-methods return {@link Initializer#INSTANCE}, you can chain them.
 * * NOTE 2: you can omit calling some of the methods; the according fields have a default value
 * - if there is an error coming from {@link Grid#Grid(int, int, int, Class)}, look at the
 *   message: what does it say? adjust your code accordingly
 *
 * How to setup the class inheriting cell:
 *   - extend the class {@link Cell}
 *   - override any methods you want to override
 *     - NOTE: the methods
 *       <ul>
 *           <li>{@link Cell#cycleState()}</li>
 *           <li>{@link Cell#getState()}</li>
 *           <li>{@link Cell#setState(int)}</li>
 *       </ul>
 *       are declared final, so they cannot be overridden
 *
 * What you can do during the simulation:
 * - control the way the aoutomaton is simulated:
 *   - 0: stop the simulation
 *   - 1: take a step every tick
 *   - 2: take a step everytime you hit enter
 * - cycle the state of every cell:
 *   - right-click on any cell to cycle through it's states
 */

public final class CellularAutomatonAPI extends PApplet
{
    double height;
    double width;
    static Camera cam = new Camera();
    static CellularAutomatonAPI instance;
    private static Grid grid;
    static Class clazz = ExampleCell.class;
    static int tickLength = 100;
    static int gridHeight = 50;
    static int gridWidth = 50;
    static int cellSize = 15;
    static Color bgColor = new Color(100, 100, 100);
    private int lastMillis;
    //0 = stopped
    //1 = tick wise
    //2 = step wise (press enter to move forward)
    private int simMode = 1;
    boolean withStroke = true;
    static boolean isRunning = false;
    static boolean random = true;

    @Override
    public void settings()
    {
        instance = this;
        size(gridWidth*cellSize+20, gridHeight*cellSize+20);
    }

    public static void main(String[] args)
    {
        Initializer.getInstance().finish();
    }

    @Override
    public void setup()
    {
        height = super.height;
        width = super.width;
        grid = new Grid<ExampleCell>(gridWidth, gridHeight, cellSize, clazz);

        if(random)
            grid.randomize();

        lastMillis = millis();
        surface.setResizable(true);
    }

    @Override
    public void draw()
    {
        if(height != super.height)
            height = super.height;
        if(width != super.width)
            width = super.width;

        instance.translate((float)(width/2), (float)(height/2));
        instance.scale((float)cam.scale);
        instance.translate(-(float)(width/2), -(float)(height/2));
        instance.translate(cam.pos.x, cam.pos.y);

        background(bgColor.getRGB());
        grid.show();
        cam.update();
        translate(-cam.pos.x, -cam.pos.y);
        //fill(new Color(103, 96, 0).getRGB());
        //rect(0, 0, cellSize, cellSize);

        if(lastMillis < millis() - tickLength && simMode == 1)
        {
            grid.update();
            lastMillis = millis();
        }

        //used to test the camera feature
        //fill(255, 0, 0);
        //rect(0, 0, 25, 25);
    }

    /**
     * is used to zoom up and down
     */
    @Override
    public void mouseWheel(MouseEvent event)
    {
        if(event.getCount() == -1)
            cam.up();
        else if(event.getCount() == 1)
            cam.down();
    }

    @Override
    public void mousePressed(MouseEvent event)
    {
        if(event.getButton() == LEFT)
            cam.enable();
    }

    @Override
    public void mouseReleased(MouseEvent event)
    {
        if(event.getButton() == LEFT)
            cam.disable();
    }

    /**
     * Used to create new cells
     */
    @Override
    public void mouseClicked(MouseEvent event)
    {
        if(event.getButton() == RIGHT)
        {
            Point eventPoint = new Point(event.getX(), event.getY());
            grid.getCellAt(grid.getBoardCoords(eventPoint)).cycleState();
        }
    }

    /**
     * when r pressed: resets the scale and position of the camera (needed to set cells)
     * when 0 pressed: sets the simMode to 0
     * when 1 pressed: sets the simMode to 1
     * when 2 pressed: sets the simMode to 2
     * when enter pressed: simulates one step, when simMode == 2
     * when s pressed: switches strokes on/off
     */
    @Override
    public void keyPressed()
    {
        if(key == 'r')
        {
            cam.scale = 1;
            cam.pos = new PVector(0, 0);
        }
        if(key == '0')
            simMode = 0;
        if(key == '1')
            simMode = 1;
        if(key == '2')
            simMode = 2;
        if(keyCode == 10 && simMode == 2)
            grid.update();
        if(key == 's')
            withStroke = !withStroke;

        System.out.println(simMode);
    }
}
