package se.davidmagnusson.devourerofbricks.gameengine.gameobjects.bricks;

import android.graphics.RectF;

/**
 * The Brick interface.
 * The Brick classes is the bricks that we'll be using in the game.
 * To see the difference between them check their javadoc
 */
public interface Brick {

    /**
     * This method is called when the ball collides with a brick, if this method returns
     * true the block will be removed and if it returns false it wont be.
     * @return boolean
     */
    boolean gotHit();

    /**
     * This is a simple getter method of the coordinates of the brick
     * @return The bricks coordinates as a RectF object
     */
    RectF getRect();

    /**
     * Returns the color that the brick will be drawn in
     * @return the colors int
     */
    int getColor();
}
