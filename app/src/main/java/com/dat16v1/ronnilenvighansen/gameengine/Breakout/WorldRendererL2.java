package com.dat16v1.ronnilenvighansen.gameengine.Breakout;

import android.graphics.Bitmap;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;

/**
 * Created by ronnilenvighansen on 31/10/2017.
 */

public class WorldRendererL2
{
    GameEngine gameEngine;
    WorldL2 world;
    Bitmap ballImage;
    Bitmap paddleImage;
    Bitmap blocksImage;

    public WorldRendererL2(GameEngine ge, WorldL2 w)
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
