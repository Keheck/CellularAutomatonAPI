package io.github.keheck;

import processing.core.PVector;

final class Camera
{
    PVector pos;
    private boolean dragging;

    private double lastX;
    private double lastY;
    private double lastMouseX;
    private double lastMouseY;
    double scale;

    Camera()
    {
        pos = new PVector(0, 0);
        scale = 1;
    }

    void up()
    {
        scale += .1;

        if(scale > 2)
            scale = 2;
    }

    void down()
    {
        scale -= .1;

        if(scale < .5)
            scale = .5;
    }

    void update()
    {
        if(dragging)
        {
            pos.x = (float)(lastX - (lastMouseX - CellularAutomatonAPI.instance.mouseX) / scale);
            pos.y = (float)(lastY - (lastMouseY - CellularAutomatonAPI.instance.mouseY) / scale);
        }
    }

    void enable()
    {
        dragging = true;
        lastMouseX = CellularAutomatonAPI.instance.mouseX;
        lastMouseY = CellularAutomatonAPI.instance.mouseY;
        lastX = pos.x;
        lastY = pos.y;
    }

    void disable()
    {
        dragging = false;
        System.out.println(pos.x);
    }
}
