package com.dat16v1.ronnilenvighansen.gameengine.Breakout;

/**
 * Created by ronnilenvighansen on 17/10/2017.
 */

public class Block
{
    public static final float WIDTH = 40;
    public static final float HEIGHT = 18;
    public float x;
    public float y;
    public int type;

    public Block(float x, float y, int type)
    {
        this.x = x;
        this.y = y;
        this.type = type;
    }
}
