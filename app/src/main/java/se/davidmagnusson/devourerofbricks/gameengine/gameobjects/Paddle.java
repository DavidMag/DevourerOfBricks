package se.davidmagnusson.devourerofbricks.gameengine.gameobjects;

import android.graphics.RectF;

/**
 * The Paddle class is the paddle we will use and is has its own update method that check for
 * wall collision and moves the paddle.
 */
public class Paddle {

    //The coordinates of the Paddle
    private RectF paddleRect;

    //Height, length, speed and the movingDirection of the paddle
    private int height;
    private int length;
    private int movementSpeed;
    private byte movingDirection;
    //Some temp values
    private int x;
    private int y;

    //The screen resolution
    private float screenX;
    private float screenY;

    //Some bytes to clarify the movement setting
    public static final byte STOP = 0,
                            LEFT = 1,
                            RIGHT = 2;

    /**
     * The class constructor after this is done the paddle is ready to be drawn.
     * And the default moving state is STOP
     * @param screenX Should be the screens X resolution, measured in pixels
     * @param screenY Should be the screens Y resolution, measured in pixels
     */
    public Paddle(float screenX, float screenY){
        //Save the screens resolution for later use
        this.screenX = screenX;
        this.screenY = screenY;

        //Set standard speed
        movementSpeed = 450;

        //Set standard size
        length = (int) (screenX / 12);
        height = 10;

        //Calc the position and init the RectF with the new values
        x = (int) (screenX / 2 - length / 2);
        y = (int) (screenY * 0.8);

        paddleRect = new RectF(x, y, x + length, y + height);

        movingDirection = STOP;
    }

    /**
     * This method does the moving logic for the paddle and also the wall collision logic.
     * It takes the fps to make sure that the paddle always move at the same speed independent of
     * the current fps / mobile performance
     * @param fps The current fps, as a long
     */
    public void update(int fps){
        //Check which direction the paddle is going
        if (movingDirection == LEFT){
            //make sure that the paddle isn't going out of boundaries
            if (x - (movementSpeed / fps) > 0){
                //Set the new x coordinate *
                x -= movementSpeed / fps;
            } else {
                //If the paddle was going out the screen to the left set the x to 0
                x = 0;
            }
        } else if (movingDirection == RIGHT) {
            //This is basically a copy of the statements over but for the right moving and
            //the right wall.
            if (x + length + (movementSpeed / fps) < screenX){
                x += movementSpeed / fps;
            } else {
                x = (int) screenX - length;
            }
        }

        //Set the new values to the RectF object, and if the paddles moving state is STOP
        //the x variable is the same as previous
        paddleRect.left = x;
        paddleRect.right = x + length;

        //* Divided the movementSpeed by the fps to make sure that it'll move by the
        //  same speed regardless of the fps.
    }

    /**
     * Simple setting method for the paddles moving direction.
     * Use the Paddle.STOP, Paddle.LEFT and Paddle.RIGHT
     */
    public void setMovingDirection(byte movingDirection){
        this.movingDirection = movingDirection;
    }

    /**
     * This method returns the paddles coordinates to be drawn or checked for collisions
     * @return the paddles coordinates as a RectF object
     */
    public RectF getRect() {
        return paddleRect;
    }
}
