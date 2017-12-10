package com.dat16v1.ronnilenvighansen.gameengine.FoodKamikaze;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ronnilenvighansen on 24/11/2017.
 */

public class World
{
    public static final float MIN_X = 0;
    public static final float MAX_X = 319;
    public static final float MIN_Y = 32;
    public static final float MAX_Y = 479;
    Shirt shirt = new Shirt();
    List<Hotdog> hotdogList = new ArrayList<>();
    public int maxHotdogs = 3;
    GameEngine gameEngine;
    CollisionListener listener;
    boolean gameOver = false;
    int points = 0;
    int lives = 3;

    public World()
    {
        throw new RuntimeException("This should never happen");
    }

    public World(GameEngine ge, CollisionListener listener)
    {
        this.gameEngine = ge;
        this.listener = listener;
        initializeHotdogs();
    }



    public void update(float deltatime)
    {
        if(gameEngine.isTouchDown(0))
        {
            if(gameEngine.getTouchY(0) > 32 || gameEngine.getTouchX(0) < 282)
            {
                shirt.x = gameEngine.getTouchX(0) - Shirt.WIDTH;
            }
        }

        if (shirt.x < MIN_X)
        {
            shirt.x = (int)MIN_X;
        }
        if (shirt.x > MAX_X - Shirt.WIDTH)
        {
            shirt.x = (int)(MAX_X - Shirt.WIDTH);
        }

        Hotdog hotdog = null;
        for(int i = 0; i < maxHotdogs; i++)
        {
            hotdog = hotdogList.get(i);
            hotdog.y = (int)(hotdog.y - 100 * deltatime);
            if(hotdog.y < 0 - Hotdog.HEIGHT)
            {
                Random random = new Random();
                hotdog.y = 500 + random.nextInt(100);
                hotdog.x = random.nextInt(320);
            }
        }
        collideHotdog();
    }

    private void collideHotdog()
    {
        Hotdog hotdog = null;
        for(int i = 0; i < maxHotdogs; i++)
        {
            hotdog = hotdogList.get(i);
            if(collideRects(shirt.x, shirt.y, Shirt.WIDTH, Shirt.HEIGHT, hotdog.x, hotdog.y, Hotdog.WIDTH, Hotdog.HEIGHT))
            {
                gameOver = true;
            }
        }
    }



    private boolean collideRects(float x1, float y1, float width1, float height1,
                                 float x2, float y2, float width2, float height2)
    {
        if(y1 < y2 + height2 &&
                y1 + height1 > y2 &&
                x1 + width1 > x2 &&
                x1 < x2 + width2)
        {
            return true;
        }
        return false;
    }

    private void initializeHotdogs()
    {
        Random rand = new Random();
        int i = 0;
        while (i < maxHotdogs)
        {
            int randX = rand.nextInt(272);
            int randY = rand.nextInt(100);
            Hotdog hotdog = new Hotdog(randX, 480 + randY + i * 100);
            hotdogList.add(hotdog);
            i++;
        }
    }


}

