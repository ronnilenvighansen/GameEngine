package com.dat16v1.ronnilenvighansen.gameengine;

import android.view.MotionEvent;
import android.view.View;
import java.util.List;

/**
 * Created by ronnilenvighansen on 19/09/2017.
 */

public class MultiTouchHandler implements TouchHandler, View.OnTouchListener
{

    private boolean[] isTouched = new boolean[20];
    private int[] touchX = new int[20];
    private int[] touchY = new int[20];
    private List<TouchEvent> touchEventBuffer;
    private TouchEventPool touchEventPool;

    public MultiTouchHandler(View v, List<TouchEvent> touchEventBuffer, TouchEventPool touchEventPool)
    {
        v.setOnTouchListener(this);
        this.touchEventBuffer = touchEventBuffer;
        this.touchEventPool = touchEventPool;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent)
    {
        TouchEvent touchEvent = null;
        int action = motionEvent.getAction() & MotionEvent.ACTION_MASK;
        // Extract bits, and shift to right
        int pointerIndex = (motionEvent.getActionIndex() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
        int pointerId = motionEvent.getPointerId(pointerIndex);

        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                // get object to put information in
                touchEvent = touchEventPool.obtains();
                touchEvent.type = TouchEvent.TouchEventType.Down;
                touchEvent.pointer = pointerId;
                touchEvent.x = (int)motionEvent.getX(pointerIndex);
                touchX[pointerId] = touchEvent.x;
                touchEvent.y = (int)motionEvent.getY(pointerIndex);
                touchY[pointerId] = touchEvent.y;
                isTouched[pointerId] = true;
                synchronized (touchEventBuffer)
                {
                    touchEventBuffer.add(touchEvent);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                // get object to put information in
                touchEvent = touchEventPool.obtains();
                touchEvent.type = TouchEvent.TouchEventType.Up;
                touchEvent.pointer = pointerId;
                touchEvent.x = (int)motionEvent.getX(pointerIndex);
                touchX[pointerId] = touchEvent.x;
                touchEvent.y = (int)motionEvent.getY(pointerIndex);
                touchY[pointerId] = touchEvent.y;
                isTouched[pointerId] = false;
                synchronized (touchEventBuffer)
                {
                    touchEventBuffer.add(touchEvent);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                int pointerCount = motionEvent.getPointerCount();
                synchronized (touchEventBuffer)
                {
                    for (int i = 0; i < pointerCount; i++) {
                        touchEvent = touchEventPool.obtains();
                        touchEvent.type = TouchEvent.TouchEventType.Dragged;
                        pointerIndex = i;
                        pointerId = motionEvent.getPointerId(pointerIndex);
                        touchEvent.pointer = pointerId;
                        touchEvent.x = (int)motionEvent.getX(pointerIndex);
                        touchEvent.y = (int)motionEvent.getY(pointerIndex);
                        touchX[pointerId] = touchEvent.x;
                        touchY[pointerId] = touchEvent.y;
                        isTouched[pointerId] = true;
                        touchEventBuffer.add(touchEvent);
                    }
                }
                break;
        }

        return true;
    }

    @Override
    public boolean isTouchDown(int pointer)
    {
        return isTouched[pointer];
    }

    @Override
    public int getTouchX(int pointer)
    {
        return touchX[pointer];
    }

    @Override
    public int getTouchY(int pointer)
    {
        return touchY[pointer];
    }


}