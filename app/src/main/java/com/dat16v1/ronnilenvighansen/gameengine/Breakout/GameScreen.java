package com.dat16v1.ronnilenvighansen.gameengine.Breakout;

import android.graphics.Bitmap;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;
import com.dat16v1.ronnilenvighansen.gameengine.Screen;

/**
 * Created by ronnilenvighansen on 03/10/2017.
 */

public class GameScreen extends Screen
{
    Bitmap background = null;

    public GameScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        background = gameEngine.loadBitmap("breakoutassets/background.png");

    }

    @Override
    public void update(float deltaTime)
    {
        gameEngine.drawBitMap(background, 0, 0);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
