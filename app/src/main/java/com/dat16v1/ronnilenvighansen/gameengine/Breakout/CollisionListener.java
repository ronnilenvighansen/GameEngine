package com.dat16v1.ronnilenvighansen.gameengine.Breakout;

/**
 * Created by ronnilenvighansen on 24/10/2017.
 */

public interface CollisionListener
{
    public void collisionWall();
    public void collisionPaddle();
    public void collisionBlock();
    public void gameOver();
}
