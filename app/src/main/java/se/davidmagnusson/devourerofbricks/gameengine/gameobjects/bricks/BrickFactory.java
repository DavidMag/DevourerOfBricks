package se.davidmagnusson.devourerofbricks.gameengine.gameobjects.bricks;

/**
 * Simple BrickFactory used by the game view to get all the different Bricks in
 * a smooth way
 */
public class BrickFactory {

    public static final byte EMPTY_SPACE = 0,
                            BASIC_BRICK = 1,
                            INDESTRCTIBLE_BRICK = 2,
                            TWO_LIFE_BRICK = 3;

    /**
     * Creates and returns a new brick of specified type
     * @param brickType the type of brick that will be created use BrickFactorys "enums"
     * @param row which row the brick will be placed on
     * @param column which column the brick will be in
     * @param width the width of the brick, measured in pixels
     * @param height the height if the brick, measured in pixels
     * @return the new and correct type of brick
     */
    public Brick getBrick(byte brickType, byte row, byte column, short width, short height){
        switch (brickType){
            case EMPTY_SPACE:
                return null;
            case BASIC_BRICK:
                return new BasicBrick(row, column, width, height);
            case INDESTRCTIBLE_BRICK:
                return new IndestructibleBrick(row, column, width, height);
            case TWO_LIFE_BRICK:
                return new TwoLifeBrick(row, column, width, height);
            default:
                return null;
        }
    }
}
