package com.dat16v1.ronnilenvighansen.gameengine.Carscroller;

/**
 * Created by ronnilenvighansen on 31/10/2017.
 */

public class Car
{
    public static final int WIDTH = 64;
    public static final int HEIGHT = 34;
    public int x = 0;
    public int y = 0;

    public Car()
    {
        x = 20;
        y = 160 - HEIGHT/2;
    }
}
