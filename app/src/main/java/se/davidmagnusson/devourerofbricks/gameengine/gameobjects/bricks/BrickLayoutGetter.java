package se.davidmagnusson.devourerofbricks.gameengine.gameobjects.bricks;

/**
 * This class helps with the brick layout. It has one method and it is to take the which level
 * it's as a param then return the brick layout for that level
 */
public class BrickLayoutGetter {

    /*
      ┃
      ┠───┬───┨
      ┃ 1 │ 2 ┃
      ┠───┼───┼┨
      ┃  │  ┃
      ┠──┼──┨
      ┃  │  ┃
      ┠──┼──┨
      ┃  │  ┃
      ┠──┼──┨
      ┃  │  ┃
      ┠──┴──┨

     */



    private byte[] stageOne = {
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK
    };

    private byte[] stageTwo = {
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK
    };

    private byte[] stageThree = {
            BrickFactory.BASIC_BRICK, BrickFactory.INDESTRCTIBLE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.INDESTRCTIBLE_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.INDESTRCTIBLE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.INDESTRCTIBLE_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK
    };

    private byte[] stageFour = {
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.INDESTRCTIBLE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.INDESTRCTIBLE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK
    };

    private byte[] stageFive = {
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.INDESTRCTIBLE_BRICK, BrickFactory.TWO_LIFE_BRICK, BrickFactory.INDESTRCTIBLE_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.INDESTRCTIBLE_BRICK, BrickFactory.INDESTRCTIBLE_BRICK, BrickFactory.INDESTRCTIBLE_BRICK, BrickFactory.BASIC_BRICK,
            BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK, BrickFactory.BASIC_BRICK
    };

    /**
     * Sends back a byte array to put in the BrickFactory.
     * @param level which level the player are going to play
     * @return returns the brick layout as a byte array of the chosen level
     */
    public byte[] getBrickLayout(byte level){
        switch (level){
            case 1:
                return stageOne;
            case 2:
                return stageTwo;
            case 3:
                return stageThree;
            case 4:
                return stageFour;
            case 5:
                return stageFive;
            default:
                return null;
        }
    }
}
