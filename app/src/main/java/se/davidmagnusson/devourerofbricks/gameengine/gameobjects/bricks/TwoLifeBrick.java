package se.davidmagnusson.devourerofbricks.gameengine.gameobjects.bricks;

import android.graphics.Color;
import android.graphics.RectF;

/**
 * Brick class, this brick has to be hit two times to be removed.
 * Also changes color when hit.
 * FOR MORE INFORMATION CHECK THE BRICK INTERFACE
 */
class TwoLifeBrick implements Brick {

    private RectF brickRect;
    private int color;
    private byte lifes;

    /**
     * SEE BasicBrick FOR INFORMATION ABOUT BRICK CONSTRUCTOR
     * @param row
     * @param column
     * @param width
     * @param height
     */
    TwoLifeBrick(byte row, byte column, short width, short height){
        byte padding = 4;

        color = Color.argb(255, 0, 150, 0);
        lifes = 2;

        int left = column * width + padding;
        int right = left + width - (padding * 2);
        int top = row * height + padding;
        int bottom = top + height - (padding * 2);

        brickRect = new RectF(left, top, right, bottom);
    }

    /**
     * SEE BRICK INTERFACE JAVADOC FOR MORE INFORMATION
     * First time hit it changes color second time it returns true to be removed
     * @return first time false, second time true
     */
    @Override
    public boolean gotHit() {
        if (--lifes == 0){
            return true;
        } else {
            color = Color.argb(255, 0, 255, 0);
            return false;
        }
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
