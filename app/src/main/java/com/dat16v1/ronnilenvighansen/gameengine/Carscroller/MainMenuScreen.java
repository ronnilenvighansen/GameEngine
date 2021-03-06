package com.dat16v1.ronnilenvighansen.gameengine.Carscroller;

import android.graphics.Bitmap;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;
import com.dat16v1.ronnilenvighansen.gameengine.Screen;

/**
 * Created by ronnilenvighansen on 03/10/2017.
 */

public class MainMenuScreen extends Screen
{
    Bitmap background = null;
    Bitmap startgame = null;
    float passedTime = 0;
    long startTime = System.nanoTime();

    public MainMenuScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        background = gameEngine.loadBitmap("carscrollerassets/xcarbackground.png");
        startgame = gameEngine.loadBitmap("carscrollerassets/xstartgame.png");
    }

    @Override
    public void update(float deltaTime)
    {
        if(gameEngine.isTouchDown(0))
        {
            gameEngine.setScreen(new GameScreen(gameEngine));
            return;
        }
        gameEngine.drawBitMap(background, 0, 0);
        passedTime = passedTime + deltaTime;
        if((passedTime - (int)passedTime) > 0.5f)
        {
            gameEngine.drawBitMap(startgame, 240 - (startgame.getWidth()/2), 160);
        }
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
