package com.dat16v1.ronnilenvighansen.gameengine.ShirtDrop;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;
import com.dat16v1.ronnilenvighansen.gameengine.Screen;

/**
 * Created by ronnilenvighansen on 24/11/2017.
 */

public class ShirtDrop extends GameEngine
    {
        @Override
        public Screen createScreen()
        {
            music = loadMusic("shirtdropassets/music.ogg");
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
