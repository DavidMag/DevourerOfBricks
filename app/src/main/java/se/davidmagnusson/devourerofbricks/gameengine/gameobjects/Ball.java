package se.davidmagnusson.devourerofbricks.gameengine.gameobjects;

import android.graphics.RectF;
import android.util.Log;

import java.util.Random;

/**
 * This class is the ball that the game will be using. It has it's own wall collision handling
 */
public class Ball {

    private RectF ballRect;

    //Because it's a "square" the height and length will be the same
    private byte sideSize;
    //Some more info about the ball
    private short xSpeed;
    private short ySpeed;
    private int x;
    private int y;

    //Screen resolution measured in pixels
    private float screenX;
    private float screenY;

    /**
     * Class constructor
     * @param screenX the screens X resolution (pixels)
     * @param screenY the screens Y resolution (pixels)
     */
    public Ball(float screenX, float screenY){
        this.screenX = screenX;
        this.screenY = screenY;

        sideSize = 20;
    }

    /**
     * This method resets the ball to it's starting position (x = middle, y = 30% up).
     * Also sets a random xSpeed to start with.
     * Should be called when the game scene is building (new game) and when the ball hits the "floor"
     */
    public void reset(){
        //Place the ball in the middle of the X axis and 30 % up in the Y axis
        //Or 10 % above the Paddle
        x = (int) (screenX / 2 - sideSize / 2);
        y = (int) (screenY * 0.7);

        //Init the rect with the new gotten values
        ballRect = new RectF(x, y, x + sideSize, y + sideSize);

        //Set a new random moving direction
        xSpeed = (short) (new Random().nextInt(600) - 300);
        //set standard "up" speed
        ySpeed = -500;
    }

    /**
     * Checks if the ball is colliding with the wall and handles the wall collision if
     * that's the case, otherwise it just sets new coordinates to the balls rect.
     * The fps is necessary to make sure that the ball moves at the same speed no matter what
     * fps you have.
     * @param fps the current fps
     */
    public void update(int fps){
        //Check for wall collision, but not bottom cause you shouldn't bounce there

        if(fps != 0) {
            if (x + (xSpeed / fps) < 0) { //LEFT WALL
                invertX();
                resetX(0);
            } else if (x + sideSize + (xSpeed / fps) > screenX) { //RIGHT WALL
                invertX();
                resetX((int) screenX - sideSize);
            } else if (y + (ySpeed / fps) < 0) { //ROOF
                invertY();
                resetY(0);
            } else { //NO HIT, REGULAR MOVEMENT
                x += (xSpeed / fps);
                y += (ySpeed / fps);

                ballRect.left = x;
                ballRect.right = x + sideSize;
                ballRect.top = y;
                ballRect.bottom = y + sideSize;
            }
        }
    }

    /**
     * Use this when the paddle and ball intersects with one another.
     * It calcs some fake physics and make the ball go different ways depending
     * on where the ball hits the paddel.
     * @param paddle The coordinates of the paddle as a RectF object
     */
    public void paddleHit(RectF paddle){
        int middleOfBall = (int) ((ballRect.left + ballRect.right) / 2);
        middleOfBall -= paddle.left;
        float hitPoint = middleOfBall / (paddle.right - paddle.left);

        xSpeed = (short) (600 * hitPoint - 300);
        y = (int) paddle.top - sideSize - 1;
        invertY();
    }

    /**
     * This method is used when a brick got hit and to check which of the sides
     * that the ball collided with.
     * @param brick the RectF from the brick that was hitten
     */
    public void brickCollision(RectF brick) {
        /*
          1 │  2  │ 3
          ──╆━━━━━╅──
          4 ┃  5  ┃ 6
          ──╄━━━━━╃──
          7 │  8  │ 9
        */
        if (y + sideSize >= brick.bottom){      // 8 (7 9)
            invertY();
            y = (int) brick.bottom + 1;
            if (x < brick.left){                // 7
                invertX();
                x = (int) brick.left - sideSize - 1;
            } else {                            // 9
                invertX();
                x = (int) brick.right + 1;
            }
        } else if (y + sideSize <= brick.top){  // 2 (1 3)
            invertY();
            y = (int) brick.top - sideSize - 1;
            if (x <= brick.left){               // 1
                invertX();
                x = (int) brick.left - sideSize - 1;
            } else {                            // 3
                invertX();
                x = (int) brick.right + 1;
            }
        } else {                                // 4 5 6
            if (x + sideSize <= brick.left){    // 4
                invertX();
                x = (int) brick.left - sideSize - 1;
            } else if (x >= brick.right){       // 6
                invertX();
                x = (int) brick.right + 1;
            } else {                            // 5
                invertY();
                x = (int) brick.centerX();
                y = (int) brick.bottom + 1;
            }
        }
    }

    /**
     * Inverts the Y speed, used when hits a brick, roof or paddle.
     */
    public void invertY(){
        ySpeed = (short) -ySpeed;
    }

    /**
     * Inverts the X speed, used when the ball hits the wall or side of a brick
     */
    public void invertX(){
        xSpeed = (short) -xSpeed;
    }

    /**
     * To make sure that the ball doesn't get stuck inside something.
     * Use this when it collides with something on the Y axis.
     * @param left The new lower left corner coordinate, should be right next to the collided object
     *             or right next to + the balls size.
     */
    public void resetX(int left){
        ballRect.left = left;
        ballRect.right = left + sideSize;
    }

    /**
     * To make sure that the ball doesn't get stuck inside something.
     * Use this when it collides with something on the Y axis.
     * @param top The new upper left corner coordinate, should be right under the collided object
     *            if it's hitting downwards it should be right over + balls size.
     */
    public void resetY(int top){
        ballRect.top = top;
        ballRect.bottom = top + sideSize;
    }

    /**
     * This method returns the balls coordinates to be drawn or checked for collisions
     * @return the balls coordinates as a RectF object
     */
    public RectF getRect(){
        return ballRect;
    }

    /**
     * This method returns the radius of the ball, it's used when drawing the ball as a circle.
     * @return the radius of the ball as a byte
     */
    public byte getRadius(){
        return (byte) (sideSize / 2);
    }
}
