package com.dat16v1.ronnilenvighansen.gameengine;

public class TouchEventPool extends Pool<TouchEvent>
{
    @Override
    protected TouchEvent newItem() {
        return new TouchEvent();
    }
}