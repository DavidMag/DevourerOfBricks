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
}