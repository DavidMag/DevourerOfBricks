package se.davidmagnusson.devourerofbricks.gameengine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import se.davidmagnusson.devourerofbricks.R;
import se.davidmagnusson.devourerofbricks.activities.ScoreScreenActivity;
import se.davidmagnusson.devourerofbricks.gameengine.gameobjects.Ball;
import se.davidmagnusson.devourerofbricks.gameengine.gameobjects.Paddle;
import se.davidmagnusson.devourerofbricks.gameengine.gameobjects.PowerUp;
import se.davidmagnusson.devourerofbricks.gameengine.gameobjects.bricks.Brick;
import se.davidmagnusson.devourerofbricks.gameengine.gameobjects.bricks.BrickFactory;
import se.davidmagnusson.devourerofbricks.gameengine.gameobjects.bricks.BrickLayoutGetter;
import se.davidmagnusson.devourerofbricks.helpers.BasicUtils;
import se.davidmagnusson.devourerofbricks.helpers.FX;

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
    private LinkedList<PowerUp> powerUps = new LinkedList<>();
    private byte[] brickLayout;

    private Random random = new Random();

    //Game variables
    private byte life;
    private int points;
    private boolean ballDirectionSwitched;
    private byte levelId;
    private short brickValue = 10;
    private short bricksCrushed = 0;
    private short powerUpsTaken = 0;

    //Time variables
    private long gameTimeStart;
    private long gameTimeMillis;
    private long gameTimeSedonds;
    private long startPauseTime;
    private long stopPauseTime;
    private long pauseTime;
    //hud
    private String timeStr;
    private String pointsStr;
    private String lifeStr;
    private String hud;

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
    public GameView(Context context, byte level, float screenX, float screenY){
        //Let the original SurfaceView do some magic in the constructor
        super(context);

        //Save the screens resolution
        this.screenX = screenX;
        this.screenY = screenY;

        //Get the holder and create a new Paint
        ourHolder = getHolder();
        painter = new Paint();
        painter.setTypeface(Typeface.createFromAsset(this.getContext().getAssets(), "fonts/EarlyGameBoy.ttf"));

        //Init the hud strings
        timeStr = getResources().getString(R.string.in_game_hud_time);
        pointsStr = getResources().getString(R.string.in_game_hud_points);
        lifeStr = getResources().getString(R.string.in_game_hud_lifes);

        //Init the game objects
        paddle = new Paddle(screenX, screenY);
        ball = new Ball(screenX, screenY, context);
        bricks = new LinkedList<>();

        //create the game scene
        this.levelId = level;
        this.brickLayout = new BrickLayoutGetter().getBrickLayout(level);
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

        //Init some game variables
        life = 3;
        points = 0;

        //time variables
        gameTimeStart = System.currentTimeMillis();
        gameTimeSedonds = 0;
        gameTimeMillis = 0;
        pauseTime = 0;
        startPauseTime = 0;

        ball.reset(this.getContext());

        //Brick building
        //Get screen independent block size
        short width = (short) (screenX / 5);
        short height = (short) (screenY / 20);

        BrickFactory brickFactory = new BrickFactory();

        //Start at row 3 (0 indexed) to make space for the HUD
        byte idx = 0;
        for (byte row = 2; row < 7; row++){
            for (byte column  = 0; column < 5; column++){
                bricks.add(brickFactory.getBrick(brickLayout[idx++] ,row, column, width, height));
            }
        }
    }

    /**
     * The update method is where we check for collisions, update new positions and all the
     * other logic.
     */
    private void update() {

        //Update the game objects
        if (ball.update(fps)){
            FX.play(FX.hardBrick, 1, 1, 0, 0, 1);
        }
        paddle.update(fps);

        //Check if ball and paddle collides
        if (RectF.intersects(ball.getRect(), paddle.getRect())){
            ball.paddleHit(paddle.getRect());
            FX.play(FX.hardBrick, 1, 1, 0, 0, 1);
        }

        ballDirectionSwitched = false;
        for (Iterator<Brick> iterator = bricks.iterator(); iterator.hasNext();){
            Brick brick = iterator.next();
            if (brick != null) {
                if (RectF.intersects(brick.getRect(), ball.getRect()) && !ballDirectionSwitched) {
                    ball.brickCollision(brick.getRect());
                    ballDirectionSwitched = true;

                    if (brick.gotHit()) {
                        FX.play(FX.brickCollision, 1, 1, 0, 0, 1);
                        points += brickValue;
                        bricksCrushed++;
                        if (random.nextInt(5) == 1){
                            powerUps.add(new PowerUp(brick.getRect(), this.getContext(), screenX, screenY));
                        }
                        iterator.remove();
                    } else {
                        FX.play(FX.hardBrick, 1, 1, 0, 0, 1);
                    }
                }
            }
        }

        //if ball hits floor
        if (ball.getRect().bottom > screenY){
            FX.play(FX.ballCrash, 1, 1, 0, 0, 1);
            if (--life == 0){
                gameEnded(false);
            } else {
                ball.reset(this.getContext());
            }
        }

        //Updates the power ups
        if (powerUps.size() > 0){
            for (Iterator<PowerUp> iterator = powerUps.iterator(); iterator.hasNext();){
                PowerUp p = iterator.next();
                if (p.update(fps)){
                    iterator.remove();
                }
            }
        }

        //if won
        if (bricksSize() == 0){
            gameEnded(true);
        }

        //Update the HUD time
        gameTimeMillis = System.currentTimeMillis() - gameTimeStart - pauseTime;
        gameTimeSedonds = gameTimeMillis / 1000;
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

            //POWER UPS
            if (powerUps.size() > 0) {
                for (PowerUp p : powerUps){
                    canvas.drawBitmap(p.getSprite(), p.getRect().left, p.getRect().top, painter);
                }
            }

            //hud
            painter.setColor(Color.argb(100, 80, 80, 80));
            canvas.drawRect(0, 0, screenX, (screenY / 10), painter);
            painter.setColor(Color.argb(255, 0, 255, 0)); //GREEN
            painter.setTextAlign(Paint.Align.CENTER);
            painter.setTextSize(BasicUtils.convertDpToPixel(10, this.getContext()));
            hud = lifeStr +": "+ life +" - "+ pointsStr+": "+ points+ " - "+ timeStr +": "+ gameTimeSedonds;
            canvas.drawText(hud , (screenX / 2), (screenY / 20) + 25, painter);


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
     * @return boolean if valid or not
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                isPaused = false;

                //Movement
                if (event.getX() < (screenX / 2)){
                    paddle.setMovingDirection(Paddle.LEFT);
                } else {
                    paddle.setMovingDirection(Paddle.RIGHT);
                }

                //Power ups
                if (powerUps.size() > 0){
                    for (Iterator<PowerUp> iterator = powerUps.iterator(); iterator.hasNext();){
                        PowerUp p = iterator.next();
                        //Check if the player pressed inside the power up rect
                        if (event.getX() > p.getRect().left - 10 && event.getX() < p.getRect().right + 10 &&
                                event.getY() > p.getRect().top - 10 && event.getY() < p.getRect().bottom + 10)
                        {
                            FX.play(FX.countingPoints, 1, 1, 0, 0, 1);
                            powerUpsTaken++;
                            activatePowerUp(p.activate());
                            iterator.remove();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //Stop movement
                paddle.setMovingDirection(Paddle.STOP);
                break;
        }
        return true;
    }

    /**
     * When a player presses a power up this method will be called from the "onTouchEvent" method.
     * And handles the power up.
     * @param activate which power up action it is
     */
    private void activatePowerUp(String activate) {
        switch (activate){
            case "Bigger paddle, 20 sec":
                paddle.paddlePowerUp(true);
                break;
            case "Smaller paddle, 20 sec":
                paddle.paddlePowerUp(false);
                break;
            case "Double points 20 sec":
                brickValue *= 2;
                new CountDownTimer(20000, 20000){
                    @Override
                    public void onTick(long millisUntilFinished) {}
                    @Override
                    public void onFinish() {
                        brickValue /= 2;
                    }
                }.start();
                break;
            case "<":
                life++;
                break;
        }
    }

    /**
     * Here we set isPlaying boolean to true
     * and we initialize our gameThread and then starts it.
     */
    public void onResume(){
        if (startPauseTime != 0) {
            stopPauseTime = System.currentTimeMillis();
            pauseTime += stopPauseTime - startPauseTime;
        }

        isPlaying = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * In this method we turn of the "Game loop" and then waits for the thread to die
     */
    public void onPause(){
        startPauseTime = System.currentTimeMillis();
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
        painter.setTextSize(100);
        painter.getTextBounds(paused, 0, paused.length(), r);

        //Calc the x, y and draw the text
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(paused, x, y, painter);
        //Just change the size, prepare the bound, recalc the x and draw second text
        painter.setTextSize(25);
        painter.getTextBounds(press, 0, press.length(), r);
        x = cWidth / 2f - r.width() / 2f - r.left;
        canvas.drawText(press, x, y + 150, painter);
    }

    /**
     * Counts the size of the remaining bricks except the indestructible ones.
     * @return the size - the indestructible, as a int
     */
    private int bricksSize() {
        int numOfBricks = 0;
        for (Brick brick: bricks){
            if (!brick.getClass().getSimpleName().equals("IndestructibleBrick")){
                numOfBricks++;
            }
        }
        return numOfBricks;
    }

    /**
     * Called when either the player wins the game or loses it. Starts a new score screen activity,
     * with info about the game in the intent.
     * @param won if the game won or lost
     */
    private void gameEnded(boolean won) {
        Intent intent = new Intent(this.getContext(), ScoreScreenActivity.class);
        intent.putExtra("won", won);
        intent.putExtra("time", gameTimeSedonds);
        intent.putExtra("points", points);
        intent.putExtra("life", life);
        intent.putExtra("finalScore", points * (life + levelId / 2) - gameTimeSedonds);
        intent.putExtra("level", levelId);
        intent.putExtra("bricksCrushed", bricksCrushed);
        intent.putExtra("powerUps", powerUpsTaken);

        getContext().startActivity(intent);
    }
}
