package com.dat16v1.ronnilenvighansen.gameengine.Carscroller;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;
import com.dat16v1.ronnilenvighansen.gameengine.Screen;

/**
 * Created by ronnilenvighansen on 31/10/2017.
 */

public class Carscroller extends GameEngine
{
    @Override
    public Screen createScreen()
    {
        music = loadMusic("breakoutassets/music.ogg");
        return new MainMenuScreen(this);
    }

    public void onPause()
    {
        super.onPause();
        music.pause();
    }

    public void onResume()
    {
        super.onResume();
        music.play();
    }
}
