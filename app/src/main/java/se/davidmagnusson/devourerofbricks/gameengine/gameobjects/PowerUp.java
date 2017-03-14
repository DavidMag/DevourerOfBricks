package se.davidmagnusson.devourerofbricks.gameengine.gameobjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

import se.davidmagnusson.devourerofbricks.R;
import se.davidmagnusson.devourerofbricks.helpers.BasicUtils;

/**
 * The class for the power ups
 */
public class PowerUp {

    private RectF powerRect;
    private Bitmap sprite;
    private float radius;

    private int xSpeed;
    private int ySpeed;

    private String power;

    private float screenX;
    private float screenY;

    /**
     * The class constructor, initialises the power ups RectF object with coordinates
     * and sets the moving direction (and speed) to a random value
     * @param brick the hitten bricks coordinates as a RectF object
     * @param c the context to access the resources
     * @param screenX the screens x resolution as a float
     * @param screenY the screens y resolution as a float
     */
    public PowerUp(RectF brick, Context c, float screenX, float screenY){
        Random random = new Random();

        radius = BasicUtils.convertDpToPixel(12, c);

        powerRect = new RectF(
                brick.centerX() - radius,
                brick.centerY() - radius,
                brick.centerX() + radius,
                brick.centerY() + radius
        );

        this.screenX = screenX;
        this.screenY = screenY;

        int tempSpeed =(int) (screenY / 4);


        xSpeed = random.nextInt(tempSpeed) - tempSpeed / 2;
        ySpeed = random.nextInt(tempSpeed) - tempSpeed / 2;

        setPower(random.nextInt(4), c);
    }

    /**
     * The update method that moves the power up and checks if it is out of screen
     * @param fps the current fps to make sure it moves at the same speed no mather the fps
     * @return returns true if it is outside the screen and should be removed
     */
    public boolean update(long fps){
        boolean outSideScreen = false;
        if (fps != 0) {

            powerRect.left += xSpeed / fps;
            powerRect.right += xSpeed / fps;
            powerRect.top += ySpeed / fps;
            powerRect.bottom += ySpeed / fps;

            if (
                    powerRect.right < 0 ||
                            powerRect.left > screenX ||
                            powerRect.bottom < screenY * 0.1 ||
                            powerRect.top > screenY) {
                outSideScreen = true;
            }
        }

        return outSideScreen;
    }

    /**
     * Returns the power ups power string
     * @return the String that contains info about what it does
     */
    public String activate(){
        return power;
    }

    /**
     * Sets the power to the object
     * @param i a random generated int
     * @param c context to get resources
     */
    private void setPower(int i, Context c){
        int id = 0;
        switch (i){
            case 0:
                power = c.getString(R.string.in_game_power_life);
                id = R.raw.power_up_life;
                break;
            case 1:
                power = c.getString(R.string.in_game_power_bigger_paddle);
                id = R.raw.power_up_bigger_paddle;
                break;
            case 2:
                power = c.getString(R.string.in_game_power_double_points);
                id = R.raw.power_up_double_points;
                break;
            case 3:
                power = c.getString(R.string.in_game_power_smaller_paddle);
                id = R.raw.power_up_smaller_paddle;
        }
        sprite = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(c.getResources(), id),
                (int) radius * 2,
                (int) radius * 2,
                false);

    }

    /**
     * Simple getter for the bitmap of the power up
     * @return the power ups Bitmap
     */
    public Bitmap getSprite() {
        return sprite;
    }

    /**
     * Simple getter for the power ups coordinates as a RectF object
     * @return the power ups RectF object
     */
    public RectF getRect() {
        return powerRect;
    }
}
