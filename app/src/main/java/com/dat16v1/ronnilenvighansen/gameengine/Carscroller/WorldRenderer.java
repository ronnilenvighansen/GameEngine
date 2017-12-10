package com.dat16v1.ronnilenvighansen.gameengine.Carscroller;

import android.graphics.Bitmap;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;

/**
 * Created by ronnilenvighansen on 31/10/2017.
 */

public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap carImage;
    Bitmap monsterImage;

    public WorldRenderer(GameEngine ge, World w)
    {
        gameEngine = ge;
        world = w;
        carImage = gameEngine.loadBitmap("carscrollerassets/xbluecar2.png");
        monsterImage = gameEngine.loadBitmap("carscrollerassets/xyellowmonster2.png");
    }

    public void render()
    {
        gameEngine.drawBitMap(carImage, world.car.x, world.car.y);
        for(int i = 0; i < world.maxMonsters; i++)
        {
            gameEngine.drawBitMap(monsterImage, world.monsterList.get(i).x,
                    world.monsterList.get(i).y);
        }

    }
}
