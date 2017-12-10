package com.dat16v1.ronnilenvighansen.gameengine.FoodKamikaze;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;
import com.dat16v1.ronnilenvighansen.gameengine.Screen;

/**
 * Created by ronnilenvighansen on 24/11/2017.
 */

public class FoodKamikaze extends GameEngine
    {
        @Override
        public Screen createScreen()
        {
            music = loadMusic("foodkamikazeassets/music.ogg");
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
