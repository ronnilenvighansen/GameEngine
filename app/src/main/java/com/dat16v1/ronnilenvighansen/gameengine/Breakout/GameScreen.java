package com.dat16v1.ronnilenvighansen.gameengine.Breakout;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;
import com.dat16v1.ronnilenvighansen.gameengine.Music;
import com.dat16v1.ronnilenvighansen.gameengine.Screen;
import com.dat16v1.ronnilenvighansen.gameengine.Sound;
import com.dat16v1.ronnilenvighansen.gameengine.TouchEvent;

import java.util.List;

/**
 * Created by ronnilenvighansen on 03/10/2017.
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

    Bitmap background = null;
    Bitmap resume = null;
    Bitmap gameOver = null;
    Typeface font = null;
    Sound bounceSound = null;
    Sound blockSound = null;
    public Sound gameOverSound = null;

    public GameScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        world = new World(gameEngine, new CollisionListener()
        {
            @Override
            public void collisionWall()
            {
                bounceSound.play(1);
            }

            @Override
            public void collisionPaddle()
            {
                bounceSound.play(1);
            }

            @Override
            public void collisionBlock()
            {
                blockSound.play(1);
            }

            @Override
            public void gameOver()
            {
                gameOverSound.play(1);
            }
        }
        );
        renderer = new WorldRenderer(gameEngine, world);
        background = gameEngine.loadBitmap("breakoutassets/background.png");
        resume = gameEngine.loadBitmap("breakoutassets/resume.png");
        gameOver = gameEngine.loadBitmap("breakoutassets/gameover.png");
        font = gameEngine.loadFont("breakoutassets/font.ttf");
        bounceSound = gameEngine.loadSound("breakoutassets/bounce.wav");
        blockSound = gameEngine.loadSound("breakoutassets/blocksplosion.wav");
        gameOverSound = gameEngine.loadSound("breakoutassets/gameover.wav");
    }

    @Override
    public void update(float deltaTime)
    {
        if(world.gameOver)
        {
            state = State.GameOver;
        }

        if(world.levelDone)
        {
            gameEngine.setScreen((new GameScreenL2(gameEngine)));
            return;
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

        gameEngine.drawBitMap(background, 0, 0);

        if(state == State.Running)
        {
            world.update(deltaTime, gameEngine.getAccelerometer()[0]);
        }
        renderer.render();

        gameEngine.drawText(font, ("Points: " + world.points + " Lives: " + world.lives), 24, 24, Color.GREEN, 13);

        if(state == State.Paused)
        {
            gameEngine.drawBitMap(resume, 160 - resume.getWidth()/2, 240 - resume.getHeight()/2);
        }

        if(state == State.GameOver)
        {
            gameEngine.drawBitMap(gameOver, 160 - gameOver.getWidth()/2, 240 - gameOver.getHeight()/2);
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
