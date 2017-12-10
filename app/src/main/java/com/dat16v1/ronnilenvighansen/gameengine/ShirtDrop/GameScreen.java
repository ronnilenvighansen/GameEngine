package com.dat16v1.ronnilenvighansen.gameengine.ShirtDrop;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;
import com.dat16v1.ronnilenvighansen.gameengine.Screen;
import com.dat16v1.ronnilenvighansen.gameengine.Sound;
import com.dat16v1.ronnilenvighansen.gameengine.TouchEvent;

import java.util.List;

/**
 * Created by ronnilenvighansen on 24/11/2017.
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
    float backgroundY;
    Bitmap pause = null;
    Bitmap resume = null;
    Bitmap gameOver = null;
    Bitmap guardian = null;
    Sound sound = null;
    Typeface font = null;

    public GameScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        world = new World(gameEngine);
        renderer = new WorldRenderer(gameEngine, world);
        background = gameEngine.loadBitmap("shirtdropassets/background.jpg");
        pause = gameEngine.loadBitmap("shirtdropassets/pause.png");
        resume = gameEngine.loadBitmap("shirtdropassets/resume.png");
        gameOver = gameEngine.loadBitmap("shirtdropassets/gameover.png");
        guardian = gameEngine.loadBitmap("shirtdropassets/guardian.png");
        sound = gameEngine.loadSound("shirtdropassets/explosion.wav");
        font = gameEngine.loadFont("shirtdropassets/font.ttf");
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

        if(state == State.Running && gameEngine.getTouchY(0) < 28 && gameEngine.getTouchX(0) > 288)
        {
            state = State.Paused;
            pause();
            return;
        }

        if(state == GameScreen.State.Running)
        {
            backgroundY = backgroundY + 100 * deltaTime;
            if(backgroundY > 960 - 480)
            {
                backgroundY = 0;
            }

            world.update(deltaTime);
        }
        gameEngine.drawBitmap(background, 0, 0, 0, (int)backgroundY, 320, 480);
        gameEngine.drawBitMap(pause, 288, 0);
        renderer.render();
        renderer.shoot();

        gameEngine.drawText(font, "Points: " + world.points, 24, 24, Color.GREEN, 13);
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
