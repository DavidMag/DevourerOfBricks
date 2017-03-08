package se.davidmagnusson.devourerofbricks.gameengine.gameobjects.bricks;

import android.graphics.Color;
import android.graphics.RectF;

/**
 * This is the basic brick and it will be removed on the first hit
 * FOR MORE INFORMATION CHECK THE BRICK INTERFACE
 */
class BasicBrick implements Brick {

    private RectF brickRect;
    private int color;

    /**
     * This constructor is just for all the bricks the only difference is the color that
     * will be initialed.
     * @param row the row that the brick will be placed on, 0 indexed
     * @param column the column that the brick will be placed on, 0 indexed
     * @param width the width of the brick, measured in pixels
     * @param height the height of the brick, measured in pixels
     */
    BasicBrick(byte row, byte column, short width, short height){
        byte padding = 4;

        color = Color.argb(255, 0, 255, 0);

        int left = column * width + padding;
        int right = left + width - (padding * 2);
        int top = row * height + padding;
        int bottom = top + height - (padding * 2);

        brickRect = new RectF(left, top, right, bottom);
    }

    /**
     * SEE BRICK INTERFACE JAVADOC FOR MORE INFORMATION
     * Since it's a basic brick this always returns true
     * @return true
     */
    @Override
    public boolean gotHit(){
        return true;
    }

    /**
     * SEE BRICK INTERFACE JAVADOC FOR INFORMATION
     * @return brick coordinates as RectF object
     */
    @Override
    public RectF getRect(){
        return brickRect;
    }

    /**
     * SEE BRICK INTERFACE JAVADOC FOR MORE INFORMATION
     * @return green colors int
     */
    @Override
    public int getColor() {
        return color;
    }
}
