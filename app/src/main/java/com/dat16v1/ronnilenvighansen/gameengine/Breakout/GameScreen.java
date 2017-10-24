package com.dat16v1.ronnilenvighansen.gameengine.Breakout;

import android.graphics.Bitmap;

import com.dat16v1.ronnilenvighansen.gameengine.GameEngine;
import com.dat16v1.ronnilenvighansen.gameengine.Screen;

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

    Bitmap background = null;
    Bitmap resume = null;
    Bitmap gameOver = null;
    State state = State.Running;

    public GameScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        world = new World();
        renderer = new WorldRenderer(gameEngine, world);
        background = gameEngine.loadBitmap("breakoutassets/background.png");
        resume = gameEngine.loadBitmap("breakoutassets/resume.png");
        gameOver = gameEngine.loadBitmap("breakoutassets/gameover.png");
    }

    @Override
    public void update(float deltaTime)
    {
        if(state == State.Paused && gameEngine.getTouchEvents().size() > 0)
        {
            state = State.Running;
        }

        if(state == State.GameOver && gameEngine.getTouchEvents().size() > 0)
        {
            gameEngine.setScreen(new MainMenuScreen(gameEngine));
            return;
        }

        if(state == State.Running && gameEngine.getTouchY(0) < 38 && gameEngine.getTouchX(0) > 320-38)
        {
            state = State.Paused;
            return;
        }

        gameEngine.drawBitMap(background, 0, 0);

        if(state == State.Running)
        {
            world.update(deltaTime, gameEngine.getAccelerometer()[0]);
        }
        renderer.render();

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
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
