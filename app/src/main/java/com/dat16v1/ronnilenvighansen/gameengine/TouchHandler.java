package com.dat16v1.ronnilenvighansen.gameengine;

public interface TouchHandler
{
    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
}