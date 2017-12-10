package com.dat16v1.ronnilenvighansen.gameengine.FoodKamikaze;

import android.graphics.Bitmap;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;

/**
 * Created by ronnilenvighansen on 24/11/2017.
 */

public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap shirtImage;
    Bitmap hotdogImage;

    public WorldRenderer(GameEngine ge, World w)
    {
        gameEngine = ge;
        world = w;
        shirtImage = gameEngine.loadBitmap("foodkamikazeassets/shirt.png");
        hotdogImage = gameEngine.loadBitmap("foodkamikazeassets/hotdog.png");
    }

    public void render()
    {
        gameEngine.drawBitMap(shirtImage, world.shirt.x, world.shirt.y);
        for(int i = 0; i < world.maxHotdogs; i++)
        {
            gameEngine.drawBitMap(hotdogImage, world.hotdogList.get(i).x,
                    world.hotdogList.get(i).y);
        }

    }
}