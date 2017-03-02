package se.davidmagnusson.devourerofbricks.gameengine;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Iterator;
import java.util.LinkedList;

import se.davidmagnusson.devourerofbricks.R;
import se.davidmagnusson.devourerofbricks.gameengine.gameobjects.Ball;
import se.davidmagnusson.devourerofbricks.gameengine.gameobjects.Paddle;
import se.davidmagnusson.devourerofbricks.gameengine.gameobjects.bricks.BasicBrick;
import se.davidmagnusson.devourerofbricks.gameengine.gameobjects.bricks.Brick;

/**
 * This class is where the game runs, it extends the SurfaceView class and
 * implements the Runnable interface so that a thread can run it.
 * All you'll have to worry about is to init it correctly and pausing and resuming it.
 */
public class GameView extends SurfaceView implements Runnable {

    //The thread the game will be running on
    private Thread gameThread = null;

    //The Game Objects
    private Paddle paddle;
    private Ball ball;
    private LinkedList<Brick> bricks;


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

    /**
     * Class constructor
     *
     * @param context the application context
     * @param screenX the screens X resolution, measured in pixels
     * @param screenY the screens Y resolution, measured in pixels
     */
    public GameView(Context context, float screenX, float screenY){
        //Let the original SurfaceView do some magic in the constructor
        super(context);

        //Save the screens resolution
        this.screenX = screenX;
        this.screenY = screenY;

        //Get the holder and create a new Paint
        ourHolder = getHolder();
        painter = new Paint();

        //Init the game objects
        paddle = new Paddle(screenX, screenY);
        ball = new Ball(screenX, screenY);
        bricks = new LinkedList<>();

        //create the game scene
        createGameScene();
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
     * It's here the scene is built, the ball is reset to start position, the bricks are generated
     */
    private void createGameScene(){
        //TODO
        ball.reset();

        //TODO temporary brick init
        short width = (short) (screenX / 6);
        short height = 50;

        for (byte row = 0; row < 4; row++){
            for (byte column  = 0; column < 6; column++){
                bricks.add(new BasicBrick(row, column, width, height));
            }
        }
    }

    /**
     * The update method is where we check for collisions, update new positions and all the
     * other logic.
     */
    private void update() {

        //Update the game objects
        paddle.update(fps);
        ball.update(fps);

        //Check if ball and paddle collides
        if (RectF.intersects(ball.getRect(), paddle.getRect())){
            ball.paddleHit(paddle.getRect());
        }

        for (Iterator<Brick> iterator = bricks.iterator(); iterator.hasNext();){
            Brick brick = iterator.next();
            if (RectF.intersects(brick.getRect(), ball.getRect())){
                ball.brickCollision(brick.getRect());
                if (brick.gotHit()){
                   iterator.remove();
                }
            }
        }

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
            painter.setColor(Color.argb(255, 0, 255, 0));
            canvas.drawRect(paddle.getRect(), painter);

            //BALL
            canvas.drawCircle(ball.getRect().left + ball.getRadius(),
                    ball.getRect().top + ball.getRadius(),
                    ball.getRadius(),
                    painter);

            //BRICKS
            for (Brick brick: bricks){
                if (brick != null){
                    painter.setColor(brick.getColor());
                    canvas.drawRect(brick.getRect(), painter);
                }
            }

            //HUD
            painter.setTextSize(40);
            painter.setColor(Color.argb(255, 0, 255, 0)); //GREEN
            canvas.drawText("FPS: " + fps, (screenX / 2), (screenY / 2), painter);

            //PAUSE
            if (isPaused) {
                drawPaused();

            }

            //WIN

            //LOSE

            //Set the canvas to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    /**
     * This method handles all the user input (through the touch screen), mostly setting the paddles
     * moving direction or stopping it.
     * @param event auto generated by android, contains info about what happened (touched, released and so on)
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isPaused = false;

                if (event.getX() < (screenX / 2)){
                    paddle.setMovingDirection(Paddle.LEFT);
                } else {
                    paddle.setMovingDirection(Paddle.RIGHT);
                }
                break;
            case MotionEvent.ACTION_UP:
                paddle.setMovingDirection(Paddle.STOP);
                break;
        }
        return true;
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
        isPaused = true;
        try{
            gameThread.join();
        } catch (Exception ex){
            Log.e("DoB", ex.toString());
        }
    }

    /**
     * This is some extracted code from the draw() method to make is easier to follow
     * this method draws a is paused layer over the screen.
     */
    private void drawPaused() {
        //Draw a half transparent black background over everything
        canvas.drawColor(Color.argb(120, 0, 0, 0));
        //A new rect to work with
        Rect r = new Rect();
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();

        painter.setColor(Color.argb(255, 120,120,120));
        painter.setTextAlign(Paint.Align.LEFT);

        //Get the Strings
        String paused = getResources().getString(R.string.in_game_paused);
        String press = getResources().getString(R.string.in_game_press_to_start);

        //Prepare the paint
        painter.setTextSize(200);
        painter.getTextBounds(paused, 0, paused.length(), r);

        //Calc the x, y and draw the text
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(paused, x, y, painter);
        //Just change the size, prepare the bound, recalc the x and draw second text
        painter.setTextSize(50);
        painter.getTextBounds(press, 0, press.length(), r);
        x = cWidth / 2f - r.width() / 2f - r.left;
        canvas.drawText(press, x, y + 150, painter);
    }
}
