package com.dat16v1.ronnilenvighansen.gameengine.Breakout;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;
import com.dat16v1.ronnilenvighansen.gameengine.Screen;

/**
 * Created by ronnilenvighansen on 03/10/2017.
 */

public class Breakout extends GameEngine
{


    @Override
    public Screen createScreen()
    {
        return new MainMenuScreen(this);
    }
}
