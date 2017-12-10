package com.dat16v1.ronnilenvighansen.gameengine.Breakout;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ronnilenvighansen on 10/10/2017.
 */

public class World
{
    public static final float MIN_X = 0;
    public static final float MAX_X = 319;
    public static final float MIN_Y = 36;
    public static final float MAX_Y = 479;
    Ball ball = new Ball();
    Paddle paddle = new Paddle();
    List<Block> blocks = new ArrayList<>();
    GameEngine gameEngine;
    CollisionListener listener;
    boolean gameOver = false;
    int points = 0;
    int lives = 3;
    int paddleHits = 0;
    int advance = 0;
    boolean levelDone = false;

    public World()
    {
        throw new RuntimeException("This should never happen");
    }

    public World(GameEngine ge, CollisionListener listener)
    {
        this.gameEngine = ge;
        this.listener = listener;
        generateBlocks();
    }

    private void generateBlocks()
    {
        blocks.clear();
        for (int y = 50, type = 0; y < 50 + 3 * Block.HEIGHT; y = y + (int)Block.HEIGHT+0, type++) //for each row
        {
            for(int x = 20; x < MAX_X - Block.WIDTH/2; x = x + (int)Block.WIDTH+0) //for each column
            {
                blocks.add(new Block(x,y, type));
            }
        }
    }

    public void update(float deltatime, float accelX)
    {
        ball.x = (int)(ball.x + ball.vx * deltatime);
        ball.y = (int)(ball.y + ball.vy * deltatime);
        if (ball.x < MIN_X)
        {
            ball.vx = -ball.vx;
            ball.x = (int)MIN_X;
            listener.collisionWall();
        }
        if(ball.x > MAX_X - ball.WIDTH)
        {
            ball.vx = -ball.vx;
            ball.x = (int)(MAX_X - ball.WIDTH);
            listener.collisionWall();
        }
        if(ball.y < MIN_Y)
        {
            ball.vy = -ball.vy;
            ball.y = (int)MIN_Y;
            listener.collisionWall();
        }
        /*if(ball.y > MAX_Y - ball.HEIGHT)
        {
            ball.vy = -ball.vy;
            ball.y = (int)(MAX_Y - ball.HEIGHT);
        }*/

        if(ball.y + Ball.HEIGHT > MAX_Y)
        {
            lives = lives - 1;
            if(lives == 0)
            {
                gameOver = true;
                listener.gameOver();
                return;
            }
            else
            {
                ball.y = (int)MAX_Y/2;
                if(ball.vy > 0)
                {
                    ball.vy = -ball.vy;
                }
                listener.collisionBlock();
            }
        }

        paddle.x = paddle.x + accelX * deltatime * 50;
        if (paddle.x < MIN_X)
        {
            paddle.x = MIN_X;
        }
        if (paddle.x + Paddle.WIDTH > MAX_X)
        {
            paddle.x = MAX_X - Paddle.WIDTH;
        }

        if(gameEngine.isTouchDown(0))
        {
            if(gameEngine.getTouchY(0) > 450)
            {
                paddle.x = gameEngine.getTouchX(0);
            }
        }

        collideBallPaddle();
        collideBallBlocks(deltatime);

        //if all blocks are removed, regenerate or start a new level

        if(blocks.size() == 0)
        {
            levelDone = true;
            //generateBlocks();
        }
    }

    private void collideBallBlocks(float deltatime)
    {
        Block block = null;
        for(int i = 0; i < blocks.size(); i++)
        {
            block = blocks.get(i);
            if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                    block.x, block.y, Block.WIDTH, Block.HEIGHT))
            {
                blocks.remove(i);
                listener.collisionBlock();
                i = i - 1;
                float oldvx = ball.vx;
                float oldvy = ball.vy;
                reflectBall(ball, block);
                ball.x = (int)(ball.x - oldvx * deltatime * 1.01f);
                ball.y = (int)(ball.y - oldvy * deltatime * 1.01f);
                points = points + (10 - block.type);
            }
        }
    }

    private void reflectBall(Ball ball, Block block)
    {
        //top left corner
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, 1, 1))
        {
            if(ball.vx > 0)
            {
                ball.vx = -ball.vx;
            }
            if(ball.vy > 0)
            {
                ball.vy = -ball.vy;
            }
        }

        //top right corner
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x + Block.WIDTH, block.y, 1, 1))
        {
            if(ball.vx < 0)
            {
                ball.vx = -ball.vx;
            }
            if(ball.vy > 0)
            {
                ball.vy = -ball.vy;
            }
            return;
        }

        //bottom left corner
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y + Block.HEIGHT, 1, 1))
        {
            if(ball.vx > 0)
            {
                ball.vx = -ball.vx;
            }
            if(ball.vy < 0)
            {
                ball.vy = -ball.vy;
            }
            return;
        }

        //bottom right corner
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x + Block.WIDTH, block.y + Block.HEIGHT, 1, 1))
        {
            if(ball.vx < 0)
            {
                ball.vx = -ball.vx;
            }
            if(ball.vy < 0)
            {
                ball.vy = -ball.vy;
            }
        }

        //top edge
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, Block.WIDTH, 1))
        {
            /*if(ball.vy > 0)
            {
                ball.vy = -ball.vy;
            }*/

            ball.vy = -ball.vy;
            return;
        }

        //bottom edge
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y + Block.HEIGHT, Block.WIDTH, 1))
        {
            ball.vy = -ball.vy;
            return;
        }

        //left edge
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT, block.x, block.y, 1, Block.HEIGHT))
        {
            ball.vx = -ball.vx;
            return;
        }

        //right edge
        if(collideRects(ball.x, ball.y, Ball.WIDTH, Ball.HEIGHT,
                block.x + Block.WIDTH, block.y, 1, Block.HEIGHT))
        {
            ball.vx = -ball.vx;
            return;
        }
    }

    private boolean collideRects(float x1, float y1, float width1, float height1,
                                 float x2, float y2, float width2, float height2)
    {
        if(x1 < x2 + width2 &&
                x1 + width1 > x2 &&
                y1 + height1 > y2 &&
                y1 < y2 + height2)
        {
            return true;
        }
        return false;
    }

    private void collideBallPaddle()
    {
        if (ball.y + Ball.HEIGHT >= paddle.y &&
                ball.x < paddle.x + Paddle.WIDTH &&
                ball.x + Ball.WIDTH > paddle.x)
        {
            ball.y = (int)(paddle.y - Ball.HEIGHT - 2);
            ball.vy = -ball.vy;
            listener.collisionPaddle();
            paddleHits++;
            if(paddleHits == 3)
            {
                paddleHits = 0;
                advance = 10;
                advanceBlocks();
            }
        }
    }

    private void advanceBlocks()
    {
        Block block;
        int size = blocks.size();
        for(int i = 0; i < size; i++)
        {
            block = blocks.get(i);
            block.y = block.y + advance;
        }
    }
}
