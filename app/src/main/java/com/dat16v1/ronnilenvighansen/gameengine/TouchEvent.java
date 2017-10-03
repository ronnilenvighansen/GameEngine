package com.dat16v1.ronnilenvighansen.gameengine;

public class TouchEvent
{
    public enum TouchEventType
    {
        Down,
        Up,
        Dragged
    }

    public TouchEventType type;

    public int x;       // Coordinate x
    public int y;       // Coordinate y
    public int pointer; // Pointer id from Android system
}