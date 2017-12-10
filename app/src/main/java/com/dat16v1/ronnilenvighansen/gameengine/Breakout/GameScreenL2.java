package com.dat16v1.ronnilenvighansen.gameengine.Breakout;

import com.dat16v1.ronnilenvighansen.gameengine.Screen;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;
import com.dat16v1.ronnilenvighansen.gameengine.Sound;
import com.dat16v1.ronnilenvighansen.gameengine.TouchEvent;

import java.util.List;


/**
 * Created by ronnilenvighansen on 24/10/2017.
 */

public class GameScreenL2 extends Screen
{
    enum State
    {
        Paused,
        Running,
        GameOver
    }

    WorldL2 world2;
    WorldRendererL2 renderer;
    GameScreen.State state = GameScreen.State.Running;

    Bitmap background = null;
    Bitmap resume = null;
    Bitmap gameOver = null;
    Typeface font = null;
    Sound bounceSound = null;
    Sound blockSound = null;
    public Sound gameOverSound = null;

    public GameScreenL2(GameEngine gameEngine)
    {
        super(gameEngine);
        world2 = new WorldL2(gameEngine, new CollisionListener()
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
        renderer = new WorldRendererL2(gameEngine, world2);
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
        if(world2.gameOver)
        {
            state = GameScreen.State.GameOver;
        }
        if(state == GameScreen.State.Paused && gameEngine.getTouchEvents().size() > 0)
        {
            state = GameScreen.State.Running;
            resume();
        }

        if(state == GameScreen.State.GameOver)
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

        if(state == GameScreen.State.Running && gameEngine.getTouchY(0) < 38 && gameEngine.getTouchX(0) > 320-38)
        {
            state = GameScreen.State.Paused;
            pause();
            return;
        }

        gameEngine.drawBitMap(background, 0, 0);

        if(state == GameScreen.State.Running)
        {
            world2.update(deltaTime, gameEngine.getAccelerometer()[0]);
        }
        renderer.render();

        gameEngine.drawText(font, ("Points: " + world2.points + " Lives: " + world2.lives), 24, 24, Color.GREEN, 13);

        if(state == GameScreen.State.Paused)
        {
            gameEngine.drawBitMap(resume, 160 - resume.getWidth()/2, 240 - resume.getHeight()/2);
        }

        if(state == GameScreen.State.GameOver)
        {
            gameEngine.drawBitMap(gameOver, 160 - gameOver.getWidth()/2, 240 - gameOver.getHeight()/2);
        }
    }


    @Override
    public void pause()
    {
        if(state == GameScreen.State.Running)
        {
            state = GameScreen.State.Paused;
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
