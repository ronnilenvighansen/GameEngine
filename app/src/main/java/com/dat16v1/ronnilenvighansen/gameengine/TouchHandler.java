package com.dat16v1.ronnilenvighansen.gameengine;

/**
 * Created by julieglasdam on 12/09/2017.
 */

public interface TouchHandler
{
    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
}