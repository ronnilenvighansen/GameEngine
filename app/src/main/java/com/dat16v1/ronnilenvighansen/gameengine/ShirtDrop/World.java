package com.dat16v1.ronnilenvighansen.gameengine.ShirtDrop;

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
    List<Guardian> guardianList = new ArrayList<>();
    List<Hotdog> hotdogList = new ArrayList<>();
    public int maxHotdogs = 3;
    public int maxGuardians = 1;
    GameEngine gameEngine;
    boolean gameOver = false;
    int points = 0;
    int hotdogVelocity = 100;
    int velocityThreshold = 100;

    public World()
    {
        throw new RuntimeException("This should never happen");
    }

    public World(GameEngine ge)
    {
        this.gameEngine = ge;
        initializeHotdogs();
        initializeGuardian();
    }



    public void update(float deltaTime)
    {
        if(gameEngine.isTouchDown(0))
        {
            if(gameEngine.getTouchY(0) > MIN_Y || gameEngine.getTouchX(0) < 144 ||
                    (gameEngine.getTouchX(0) > 176 && gameEngine.getTouchX(0) < 282))
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

        Hotdog hotdog;
        Guardian guardian;
        for(int i = 0; i < maxHotdogs; i++)
        {
            hotdog = hotdogList.get(i);
            hotdog.y = (int)(hotdog.y - hotdogVelocity * deltaTime);
            if(hotdog.y < 0 - Hotdog.HEIGHT)
            {
                Random random = new Random();
                hotdog.y = (int)MAX_Y + random.nextInt(100);
                hotdog.x = random.nextInt((int)MAX_X);
                points-=5;
            }
        }

        for(int i = 0; i < maxGuardians; i++)
        {
            guardian = guardianList.get(i);
            guardian.y = (int)(guardian.y + 300 * deltaTime);
            if(guardian.y > (int)MAX_Y + Guardian.HEIGHT)
            {
                guardian.y = shirt.y + Shirt.HEIGHT;
                guardian.x = shirt.x;
            }
        }

        collideHotdog();
        collideGuardian();
        if(points >= velocityThreshold)
        {
            hotdogVelocity += 20;
            velocityThreshold+=100;
        }
    }

    private void collideHotdog()
    {
        Hotdog hotdog;
        for(int i = 0; i < maxHotdogs; i++)
        {
            hotdog = hotdogList.get(i);
            if(collideRects(shirt.x, shirt.y, Shirt.WIDTH, Shirt.HEIGHT, hotdog.x, hotdog.y, Hotdog.WIDTH, Hotdog.HEIGHT))
            {
                gameOver = true;
            }
        }
    }

    private void collideGuardian()
    {
        Hotdog hotdog;
        Guardian guardian = null;
        for(int i = 0; i < maxHotdogs; i++)
        {
            hotdog = hotdogList.get(i);
            for(int j = 0; j < maxGuardians; j++)
            guardian = guardianList.get(j);
            if(collideRects(guardian.x, guardian.y, Guardian.WIDTH, Guardian.HEIGHT,
                    hotdog.x, hotdog.y, hotdog.WIDTH, hotdog.HEIGHT))
            {
                Random random = new Random();
                hotdog.y = (int)MAX_Y + random.nextInt((int)MAX_Y-Shirt.HEIGHT);
                hotdog.x = random.nextInt((int)MAX_X-Shirt.WIDTH);
                guardian.y = shirt.y + Shirt.HEIGHT;
                guardian.x = shirt.x;
                points += 10;
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
            int randX = rand.nextInt((int)MAX_X-Shirt.WIDTH);
            int randY = rand.nextInt((int)MAX_Y-Shirt.HEIGHT);
            Hotdog hotdog = new Hotdog(randX, (int)MAX_Y + randY);
            hotdogList.add(hotdog);
            i++;
        }
    }

    private void initializeGuardian()
    {
        Guardian guardian = new Guardian(shirt.x, (shirt.y + Shirt.HEIGHT));
        guardianList.add(guardian);
    }

}

