package com.dat16v1.ronnilenvighansen.gameengine;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/*
* We use events because, we want to save battery, so the program only runs when something happens
* we use threads so we can change the code that runs, so everything doesn't run at once*/

public abstract class GameEngine extends Activity implements Runnable, SensorEventListener
{

    private Thread mainLoopThread;
    private State state = State.Paused;
    private List<State> stateChanges = new ArrayList<>(); // Use Arraylist because we only delete from end

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private Screen screen;
    private Canvas canvas;
    private Bitmap virtualScreen;

    Rect src = new Rect();
    Rect dst = new Rect();
    Paint paint = new Paint();

    private SoundPool soundPool;
    public Music music = null;

    private TouchHandler touchHandler;
    private TouchEventPool touchEventPool = new TouchEventPool();
    private List<TouchEvent> touchEventBuffer = new ArrayList<>();
    private List<TouchEvent> touchEventCopied = new ArrayList<>();
    private int framesPerSecond = 0;

    private float[] accelerometer = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Has to call super because it's overriding method in super class
        super.onCreate(savedInstanceState);
        // Add flags (true or false)
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        surfaceHolder = surfaceView.getHolder();

        touchHandler = new MultiTouchHandler(surfaceView, touchEventBuffer, touchEventPool);

        SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)  // check size of list
        {
            Sensor accelerometer = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        SoundPool.Builder sBuilder = new SoundPool.Builder();
        sBuilder.setMaxStreams(20);
        AudioAttributes.Builder audioAttributesBuilder = new AudioAttributes.Builder();
        audioAttributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
        AudioAttributes audioAttributes = audioAttributesBuilder.build();
        sBuilder.setAudioAttributes(audioAttributes);
        this.soundPool = sBuilder.build();

        //this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0); Deprecated.
        screen = createScreen();
        // Check if display is horizontal or vertical
        if (surfaceView.getWidth() > surfaceView.getHeight())
        {
            setVirtualScreen(480, 320);
        }
        else
        {
            setVirtualScreen(320, 480);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void onSensorChanged(SensorEvent sensorEvent)
    {
        System.arraycopy(sensorEvent.values, 0, accelerometer, 0, 3);
        accelerometer[0] = -1.0f * accelerometer[0];
    }

    public float[] getAccelerometer()
    {
        return accelerometer;
    }

    private void fillEvents()
    {
        synchronized (touchEventBuffer)
        {
            int stop = touchEventBuffer.size();
            for(int i = 0; i < stop; i++)
            {
                touchEventCopied.add(touchEventBuffer.get(i));
            }
            touchEventBuffer.clear();
        }
    }

    private void freeEvents()
    {
        synchronized (touchEventBuffer)
        {
            int stop = touchEventCopied.size();
            for(int i = 0; i < stop; i++)
            {
                touchEventPool.free(touchEventCopied.get(i));
            }
            touchEventCopied.clear();
        }
    }

    public List<TouchEvent> getTouchEvents()
    {
        return touchEventCopied;
    }

    public void setVirtualScreen(int width, int height)
    {
        if (virtualScreen != null)
        {
            virtualScreen.recycle(); // reuse if there's already a screen
        }
        virtualScreen = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        canvas = new Canvas(virtualScreen);
    }

    public int getFrameBufferWidth()
    {
        return virtualScreen.getWidth();
    }

    public int getFrameBufferHeight()
    {
        return virtualScreen.getHeight();
    }

    public abstract Screen createScreen();

    public void setScreen(Screen screen)
    {
        if (this.screen != null) {
            this.screen.dispose();
            this.screen = screen;
        }
    }

    // Load some texture. Parameter is the filename of the image we want to load
    public Bitmap loadBitmap(String filename)
    {
        InputStream  in = null;
        Bitmap bitmap = null;

        try {
            // Get the image from the resource folder and return it
            in = getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null) {
                throw new RuntimeException(" Could not find image " + filename);
            }
            return bitmap;
        } catch (IOException e) {
            throw new RuntimeException("Could not open image " + filename);
        }
        // Close input stream
        finally {
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Music loadMusic(String filename)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(filename);
            return new Music(assetFileDescriptor);
        }
        catch(IOException e)
        {
            throw new RuntimeException("Could not load music file" + filename + "!!");
        }
    }

    public Sound loadSound(String filename)
    {
        try
        {
            AssetFileDescriptor assDescriptor = getAssets().openFd(filename);
            int soundId = soundPool.load(assDescriptor, 0);
            Sound sound = new Sound(soundPool, soundId);
            return sound;
        }
        catch(IOException e)
        {
            throw new RuntimeException("Could not load sound from file: " + filename);
        }
    }

    public void clearFrameBuffer(int color) {
        canvas.drawColor(color);
    }


    // Draw whole bitmap
    public void drawBitMap(Bitmap bitmap, int x, int y)
    {
        if (canvas != null) {
            canvas.drawBitmap(bitmap, x, y, null);
        }
    }


    // Draw part of the bitmap (for spritesheets)
    public void drawBitmap(Bitmap bitmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight)
    {
        Rect src = new Rect();
        Rect dst = new Rect();
        if (canvas == null) return;
        src.left = srcX;
        src.top = srcY;
        src.right = srcX + srcWidth;
        src.bottom = srcY + srcHeight;
        dst.left = x;
        dst.top = y;
        dst.right = x + srcWidth;
        dst.bottom = y + srcHeight;
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    // Methods for when the screen is touched
    public boolean isTouchDown(int pointer)
    {
        return touchHandler.isTouchDown(pointer);
    }
    public int getTouchX(int pointer)
    {
        int virtualX = 0;
        virtualX = (int)((float)touchHandler.getTouchX(pointer)/(float)surfaceView.getWidth()*virtualScreen.getWidth()); // Get some other x than the real x
        return virtualX;
    }
    public int getTouchY(int pointer)
    {
        int virtualY = 0;
        virtualY = (int)((float)touchHandler.getTouchY(pointer)/(float)surfaceView.getHeight()*virtualScreen.getHeight()); // Get some other x than the real x
        return virtualY;
    }

    public Typeface loadFont(String fileName)
    {
        Typeface font = Typeface.createFromAsset(getAssets(), fileName);
        if(font == null)
        {
            throw new RuntimeException("Could not load font from assets: " + fileName);
        }
        return font;
    }

    public void drawText(Typeface font, String text, int x, int y, int color, int size)
    {
        paint.setTypeface(font);
        paint.setTextSize(size);
        paint.setColor(color);
        canvas.drawText(text, x, y, paint);
    }

    public void onPause()
    {
        super.onPause();
        synchronized (stateChanges) {
            // Check is the app is shutting down or just paused
            if (isFinishing()) // Method inherited from Activity
            {
                soundPool.release();
                stateChanges.add(State.Disposed);
            }
            else
            {
                stateChanges.add(State.Paused);
            }
        }
        try
        {
            mainLoopThread.join();
        }
        catch (Exception e)
        {
            Log.d("GameEngine", "error when waiting for Mainloop thread to die");
        }
        if (isFinishing()) {
            ((SensorManager)getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
        }
    }


    public void onResume()
    {
        super.onResume();
        // Syncronize before creating thread
        synchronized (stateChanges){
            stateChanges.add(State.Resumed);
        }

        mainLoopThread = new Thread(this); // Create new thread
        mainLoopThread.start();
    }

    @Override
    /* stateChanges is like buffer. Check if something needs to be handled, and iterate
    through array to figure out what needs to be done */
    public void run()
    {
        //int frames = 0;
        long startTime = System.nanoTime();
        long currentTime = startTime;
        float deltaTime = 0;
        while(true)
        {
            synchronized (stateChanges) // Lock stateChanges, so it can't be accesed by multiple threads at once
            {
                int stopValue = stateChanges.size(); // Do this so loop doesn't need to check array size for every iteration
                for(int i = 0; i < stopValue; i++)
                {
                    state = stateChanges.get(i);
                    if (state == State.Disposed)
                    {
                        if (screen != null) screen.dispose();
                        Log.d("GameEngine", "disposed");
                        stateChanges.clear();
                        return; // Break out of the method
                    }
                    if (state == State.Paused)
                    {
                        if (screen != null) screen.pause();
                        Log.d("GameEngine", "paused");
                        stateChanges.clear();
                        return;
                    }
                    if (state == State.Resumed)
                    {
                        if (screen != null) screen.resume();
                        Log.d("GameEngine", "resumed");
                        state = State.Running;
                    }
                }
                stateChanges.clear();
            }
            // After syncronizing, do the actual work of the thread
            if (state == State.Running)
            {
                // Check if there's connection to the actual screen
                if (!surfaceHolder.getSurface().isValid())
                {
                    continue; // Start from the beginning of the loop
                }
                Canvas canvas = surfaceHolder.lockCanvas(); // gets a canvas and locks it
                fillEvents();
                currentTime = System.nanoTime();
                deltaTime = (currentTime - startTime)/1000000000.0f;
                // canvas.drawColor(Color.RED);
                if (screen != null)
                {
                    screen.update(deltaTime); // parameter doesn't matter now
                }
                startTime = System.nanoTime();
                freeEvents();
                // copy objects from virtual screen to actual screen
                src.left = 0;
                src.top = 0;
                src.right = virtualScreen.getWidth() - 1;
                src.bottom = virtualScreen.getHeight() - 1;
                dst.left = 0;
                dst.top = 0;
                dst.right = surfaceView.getWidth();
                dst.bottom = surfaceView.getHeight();
                canvas.drawBitmap(virtualScreen, src, dst, null);

                surfaceHolder.unlockCanvasAndPost(canvas); // post updates
            }
            /*frames++;
            if(System.nanoTime() - startTime > 1000000000)
            {
                framesPerSecond = frames;
                frames = 0;
                startTime = System.nanoTime();
                Log.d("MainLoop", "FramesPerSecond = " + framesPerSecond + " *");
            }*/
        }
    }
    /*public int getFramerate()
    {
        return framesPerSecond;
    }*/
}