package se.davidmagnusson.devourerofbricks.gameengine.gameobjects.bricks;

import android.graphics.Color;
import android.graphics.RectF;

/**
 * This brick is indestructible and won't be removed when hit
 * FOR MORE INFORMATION CHECK THE BRICK INTERFACE
 */
class IndestructibleBrick implements Brick {

    private RectF brickRect;
    private int color;

    /**
     * SEE BasicBrick FOR INFORMATION ABOUT BRICK CONSTRUCTOR
     * @param row
     * @param column
     * @param width
     * @param height
     */
    IndestructibleBrick(byte row, byte column, short width, short height){
        byte padding = 2;

        color = Color.argb(255, 128, 128, 128);

        int left = column * width + padding;
        int right = left + width - (padding * 2);
        int top = row * height + padding;
        int bottom = top + height - (padding * 2);

        brickRect = new RectF(left, top, right, bottom);
    }

    /**
     * SEE BRICK INTERFACE JAVADOC FOR MORE INFORMATION
     * Since it's a indestructible brick this always returns false
     * @return false
     */
    @Override
    public boolean gotHit() {
        return false;
    }

    /**
     * SEE BRICK INTERFACE JAVADOC FOR INFORMATION
     * @return brick coordinates as RectF object
     */
    @Override
    public RectF getRect() {
        return brickRect;
    }

    /**
     * SEE BRICK INTERFACE JAVADOC FOR MORE INFORMATION
     * @return white colors int
     */
    @Override
    public int getColor() {
        return color;
    }
}
