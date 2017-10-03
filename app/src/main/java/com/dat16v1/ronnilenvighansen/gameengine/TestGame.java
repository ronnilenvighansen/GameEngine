package com.dat16v1.ronnilenvighansen.gameengine;
import java.util.Random;
/**
 * Created by ronnilenvighansen on 05/09/2017.
 */

public class TestGame extends GameEngine
{
    @Override
    public Screen createScreen()
    {
        return new TestScreen(this);
    }
}
