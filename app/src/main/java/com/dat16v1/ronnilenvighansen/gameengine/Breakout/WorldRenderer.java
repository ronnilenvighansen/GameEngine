package com.dat16v1.ronnilenvighansen.gameengine.Breakout;

import android.graphics.Bitmap;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;

/**
 * Created by ronnilenvighansen on 10/10/2017.
 */

public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap ballImage;
    Bitmap paddleImage;
    Bitmap blocksImage;

    public WorldRenderer(GameEngine ge, World w)
    {
        gameEngine = ge;
        world = w;
        ballImage = gameEngine.loadBitmap("breakoutassets/ball.png");
        paddleImage = gameEngine.loadBitmap("breakoutassets/paddle.png");
        blocksImage = gameEngine.loadBitmap("breakoutassets/blocks.png");
    }

    public void render()
    {
        gameEngine.drawBitMap(ballImage, world.ball.x, world.ball.y);
        gameEngine.drawBitMap(paddleImage, (int)world.paddle.x, (int)world.paddle.y);
        int listSize = world.blocks.size();
        Block block = null;
        for(int i = 0; i < listSize; i++)
        {
            block = world.blocks.get(i);
            gameEngine.drawBitmap(blocksImage, (int)block.x,
                    (int)block.y, 0, (int)(block.type * Block.HEIGHT),
                    (int)Block.WIDTH, (int) Block.HEIGHT);
        }
    }
}
