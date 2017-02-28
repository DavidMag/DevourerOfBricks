package se.davidmagnusson.devourerofbricks.gameengine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import se.davidmagnusson.devourerofbricks.gameengine.gameobjects.Paddle;

/**
 * Created by davidmagnusson on 2017-02-28.
 */
public class GameView extends SurfaceView implements Runnable {

    //The thread the game will be running on
    Thread gameThread = null;

    //The Game Objects
    Paddle paddle;

    //For the drawing
    private Paint painter;
    private Canvas canvas;

    //Our Surface holder
    private SurfaceHolder ourHolder;

    //Screen resolution
    private float screenX;
    private float screenY;

    //FPS and a variable to help calc the FPS
    private int fps;
    private long millisThisFrame;

    //Two booleans to keep track if the game is running and or is paused
    //The volatile tells java that that boolean can/will be changed from different threads
    volatile boolean isPlaying;
    boolean isPaused;

    //Some shorts to use to simplify the choises later on
    public static byte STOP = 0,
                        LEFT = 1,
                        RIGHT = 2;

    /**
     * Class constructor
     *
     * @param context the application context
     * @param screenX the screens X resolution
     * @param screenY the screens Y resolution
     */
    public GameView(Context context, float screenX, float screenY){
        //Let the original SurfaceView do some magic in the constructor
        super(context);

        Log.i("DoB", "Constructor");
        //Save the screens resolution
        this.screenX = screenX;
        this.screenY = screenY;

        //Get the holder and create a new Paint
        ourHolder = getHolder();
        painter = new Paint();
    }

    /**
     * The "Game loop", it's here all the updating and drawing is done in a loop
     */
    @Override
    public void run() {
        //Make sure that we are playing
        while (isPlaying) {
            //Save the current millis to calc FPS in the end of the loop
            long startMillis = System.currentTimeMillis();

            //Run update method where all logic and movement happens
            //If the game is paused we don't want to do any movement so skip if paused
            if (!isPaused) {
                this.update();
            }

            //Now run the draw method so that the new data comes to the screen
            this.draw();

            //Now lets calc the FPS
            millisThisFrame = System.currentTimeMillis() - startMillis;
            //We don't want a divide by zero exception
            if (millisThisFrame > 0){
                fps = (int) (1000 / millisThisFrame);
            }
        }
    }

    /**
     * The update method is where we check for collisions, update new positions and all the
     * other logic.
     */
    private void update() {

    }

    /**
     * This is typically called right after or shortly after the update to draw the updated data
     * to the screen.
     */
    private void draw(){
        //Lets check that our SurfaceHolder is valid
        if (ourHolder.getSurface().isValid()) {
            //Lock the canvas
            canvas = ourHolder.lockCanvas();

            //BACKGROUND
            //Draw the Background black
            canvas.drawColor(Color.argb(255, 0,0,0));

            //PADDLE

            //BALL

            //BRICKS

            //HUD
            painter.setTextSize(40);
            painter.setColor(Color.argb(255, 0, 255, 0)); //GREEN
            canvas.drawText("FPS: " + fps, (screenX / 2), (screenY / 2), painter);

            //PAUSE

            //WIN

            //LOSE

            //Set the canvas to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * Here we set isPlaying boolean to true
     * and we initialize our gameThread and then starts it.
     */
    public void onResume(){
        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * In this method we turn of the "Game loop" and then waits for the thread to die
     */
    public void onPause(){
        isPlaying = false;
        try{
            gameThread.join();
        } catch (Exception ex){
            Log.e("DoB", ex.toString());
        }
    }
}
