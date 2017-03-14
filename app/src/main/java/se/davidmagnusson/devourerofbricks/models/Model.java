package se.davidmagnusson.devourerofbricks.models;

import android.content.Context;
import android.databinding.ObservableArrayList;

import se.davidmagnusson.devourerofbricks.database.SQLiteDB;
import se.davidmagnusson.devourerofbricks.viewmodels.LevelItemViewModel;

/**
 * Model for this application and uses the singleton pattern, handles all the DB and game service
 * related stuff.
 */
public class Model {

    private static Model ourInstance = new Model();
    private static SQLiteDB db;

    /**
     * empty constructor but private so that there cannot be any more models
     */
    private Model() {
    }

    /**
     * This method returns the model that we use and also initialises the Sqlite database
     * @return the model
     */
    public static Model getInstance(Context c) {
        db = new SQLiteDB(c);
        return ourInstance;
    }

    /**
     * Fetches all the levels from the sqlite database and returns them.
     * Also checks if there is any new and pending highscores if thats the case it'll be
     * sent to the game service
     * @param c Context, used by the database
     * @return the list of levels
     */
    public ObservableArrayList<LevelItemViewModel> getLevels(Context c){
        ObservableArrayList<LevelItemViewModel> list = db.getLevelItems(c);
        for (LevelItemViewModel item : list){
            if (item.pending == 1 /*TODO network check */){
                //TODO upload to game service
                /*
                if (succeded){
                    db.updatedPendingHighscore(item.levelId);
                }
                 */
            }
        }
        return list;
    }

    /**
     * Fetches the total high score from the sqlite database and returns
     * @return the total high score
     */
    public int getTotalHighscore(){
        return db.getTotalHighscore();
    }

    /**
     * Fetches a high score from one level
     * @param levelId the level id for the level we want the high score from
     * @return the highscore as a int
     */
    public int getLevelHighscore(byte levelId){
        return db.getLevelHighscore(levelId);
    }

    /**
     * Sends a new highscore to the game service and the sqlite database
     * @param levelId which level it is as a byte
     * @param newHighscore the new high score as a int
     */
    public void newHighscore(byte levelId, int newHighscore){
        int pending = 1;
        /*
        TODO SEND TO GAME SERVICE
         */

        db.insertHighscore(levelId, newHighscore, pending);
    }

    /**
     * Updates the database and game services about the users progress such as how many bricks
     * the player have smahsed, the number of power ups the player has taken and how many games the
     * player has played.
     * @param values contains new info [0]=Number of bricks player destroyed this game,
     *               [1]=number of power ups the player took
     */
    public void updateBricksCrushed(short[] values){
        int[] res;

        res = db.increaseUserInfo(values);
        //res[0] = bricks
        //res[1] = games
        //res[2] = powerUps

        /*
        TODO SEND TO GAME SERVICE
         */
    }
}
