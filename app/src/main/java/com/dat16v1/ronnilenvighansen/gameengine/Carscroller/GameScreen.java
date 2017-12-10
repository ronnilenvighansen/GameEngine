package com.dat16v1.ronnilenvighansen.gameengine.Carscroller;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;
import com.dat16v1.ronnilenvighansen.gameengine.Screen;
import com.dat16v1.ronnilenvighansen.gameengine.Sound;
import com.dat16v1.ronnilenvighansen.gameengine.TouchEvent;

import java.util.List;

/**
 * Created by ronnilenvighansen on 31/10/2017.
 */

public class GameScreen extends Screen
{
    enum State
    {
        Paused,
        Running,
        GameOver
    }

    World world;
    WorldRenderer renderer;
    State state = State.Running;

    Bitmap background;
    float backgroundX;
    Bitmap resume = null;
    Bitmap gameOver = null;
    //Typeface font = null;
    Sound bounceSound = null;
    Sound blockSound = null;
    public Sound gameOverSound = null;

    public GameScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        world = new World(gameEngine, new CollisionListener() {
            @Override
            public void collisionWall() {

            }

            @Override
            public void CollisionMonster() {

            }

            @Override
            public void gameOver() {

            }
        });
        renderer = new WorldRenderer(gameEngine, world);
        background = gameEngine.loadBitmap("carscrollerassets/xcarbackground.png");
        resume = gameEngine.loadBitmap("carscrollerassets/resume.png");
        gameOver = gameEngine.loadBitmap("carscrollerassets/gameover.png");
        bounceSound = gameEngine.loadSound("carscrollerassets/bounce.wav");
        blockSound = gameEngine.loadSound("carscrollerassets/blocksplosion.wav");
        gameOverSound = gameEngine.loadSound("carscrollerassets/gameover.wav");
    }

    @Override
    public void update(float deltaTime)
    {
        if(world.gameOver)
        {
            state = State.GameOver;
        }

        if(state == State.Paused && gameEngine.getTouchEvents().size() > 0)
        {
            state = State.Running;
            resume();
        }

        if(state == State.GameOver)
        {
            List<TouchEvent> events = gameEngine.getTouchEvents();
            for(int i = 0; i < events.size(); i++)
            {
                if (events.get(i).type == TouchEvent.TouchEventType.Up)
                {
                    gameEngine.setScreen(new MainMenuScreen(gameEngine));
                    return;
                }
            }
        }

        if(state == State.Running && gameEngine.getTouchY(0) < 38 && gameEngine.getTouchX(0) > 320-38)
        {
            state = State.Paused;
            pause();
            return;
        }

        if(state == State.Running)
        {
            backgroundX = backgroundX + 100 * deltaTime;
            if(backgroundX > 2700 - 480)
            {
                backgroundX = 0;
            }

            world.update(deltaTime, gameEngine.getAccelerometer()[0]);
        }
        gameEngine.drawBitmap(background, 0, 0, (int)backgroundX, 0, 480, 320);
        renderer.render();

        if(state == State.Paused)
        {
            gameEngine.drawBitMap(resume, 240 - resume.getWidth()/2, 160 - resume.getHeight()/2);
        }

        if(state == State.GameOver)
        {
            gameEngine.drawBitMap(gameOver, 240 - gameOver.getWidth()/2, 160 - gameOver.getHeight()/2);
        }
    }

    @Override
    public void pause()
    {
        if(state == State.Running)
        {
            state = State.Paused;
        }
        gameEngine.music.pause();
    }

    @Override
    public void resume()
    {
        gameEngine.music.play();
    }

    @Override
    public void dispose()
    {

    }
}
