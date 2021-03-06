package se.davidmagnusson.devourerofbricks.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.databinding.ObservableArrayList;

import se.davidmagnusson.devourerofbricks.viewmodels.LevelItemViewModel;

/**
 * The SQLite Databse that the application uses
 */
public class SQLiteDB extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DB = "DoBDb";
    private static final String TABLE_ONE = "Levels";
    private static final String TABLE_TWO = "User";
    private static final String CREATE_QUERY_ONE =
            "CREATE TABLE "+TABLE_ONE+
                    "(id INTEGER PRIMARY KEY," +
                    "levelId INTEGER," +
                    "mipmapId INTEGER," +
                    "minHighscore INTEGER," +
                    "highscore INTEGER," +
                    "pending INTEGER);";
    private static final String INIT_INSERT =
            "INSERT INTO " + TABLE_ONE+
            "(levelId, mipmapId, minHighscore, highscore, pending)"+
            "VALUES (1, 2131165184, 0, 0, 0),"+
            "(2, 2131165185, 600, 0, 0),"+
            "(3, 2131165186, 1200, 0, 0),"+
            "(4, 2131165187, 2000, 0, 0),"+
            "(5, 2131165188, 3000, 0, 0)," +
            "(6, 2131165189, 4000, 0, 0),"+
            "(7, 2131165190, 5000, 0, 0),"+
            "(8, 2131165191, 7000, 0, 0);";

    private static final String CREATE_QUERY_TWO =
            "CREATE TABLE "+TABLE_TWO+
                "(id INTEGET PRIMARY KEY,"+
                "games INTEGER,"+
                "bricks INTEGER," +
                "powerUps INTEGER);";
    private static final String INIT_INSERT_TWO =
            "INSERT INTO "+ TABLE_TWO+
            "(games, bricks, powerUps)" +
            "VALUES (0, 0, 0);";

    /**
     * simple constructor for the class
     * @param c Context, the appliation context
     */
    public SQLiteDB(Context c){
        super(c, DB, null, VERSION);
    }

    /**
     * Auto called the first time the database is used.
     * Creates the databse and inserts the levels
     * @param db auto generated by super class, the db that we'll be using
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_QUERY_ONE);
        db.execSQL(INIT_INSERT);
        db.execSQL(CREATE_QUERY_TWO);
    }

    /**
     * When a new version of the database is released this method upgrades the old one to the new
     * version
     * @param db auto generated, the db that we'll use
     * @param oldVersion The version that's current active
     * @param newVersion The new version of the db
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Fetches all the levels from the database and parses them to LevelItemViewModel objects
     * then returns a list of them
     * @param c Context, used when initialising the LevelItems
     * @return a complete list of the levels
     */
    public ObservableArrayList<LevelItemViewModel> getLevelItems(Context c){
        int totalHighscore = getTotalHighscore();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Levels", null);
        ObservableArrayList<LevelItemViewModel> list = new ObservableArrayList<>();

        while (res.moveToNext()){
            LevelItemViewModel item = new LevelItemViewModel(c,
                    (byte)res.getInt(res.getColumnIndex("levelId")),
                    res.getInt(res.getColumnIndex("highscore")),
                    res.getInt(res.getColumnIndex("mipmapId")),
                    res.getInt(res.getColumnIndex("minHighscore")),
                    totalHighscore);
            item.pending = res.getInt(res.getColumnIndex("pending"));
            list.add(item);
        }

        res.close();
        db.close();

        return list;
    }

    /**
     * Accumulates all the high scores in the database and returns
     * @return the accumulated high score
     */
    public int getTotalHighscore() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        int totalHighscore = 0;

        res = db.rawQuery("SELECT highscore FROM Levels", null);
        while (res.moveToNext()){
            totalHighscore += res.getInt(res.getColumnIndex("highscore"));
        }

        res.close();
        db.close();

        return totalHighscore;
    }

    /**
     * Checks the old high score of the specific level and if the new high score is better it's
     * put into the database.
     * @param level which level it is
     * @param highscore the new high score
     * @param pending if it's waiting to be sent to the game service
     */
    public void insertHighscore(byte level, int highscore, int pending){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT highscore FROM "+TABLE_ONE+" WHERE levelId="+level, null);
        int oldHighscore = 0;
        try {
            if (res.moveToFirst()){
                oldHighscore = res.getInt(res.getColumnIndex("highscore"));
            }
        } catch (CursorIndexOutOfBoundsException ex){
            ex.printStackTrace();
        }

        if (oldHighscore < highscore) {
            ContentValues values = new ContentValues();
            values.put("highscore", highscore);
            values.put("pending", pending);

            db.update(TABLE_ONE, values, "levelId=" + level, null);
        }

        res.close();
        db.close();
    }

    /**
     * Gets the current high score for the specific level
     * @param levelId the level id
     * @return the highscore as a int
     */
    public int getLevelHighscore(byte levelId){
        int highscore = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT highscore FROM "+TABLE_ONE+" WHERE levelId="+levelId, null);
        if (res.moveToFirst()) {
            highscore = res.getInt(res.getColumnIndex("highscore"));
        }
        res.close();
        db.close();
        return highscore;
    }

    /**
     * sets the pending to false (0) of the specific level
     * @param levelId the level id
     */
     public void updatedPendingHighscore(byte levelId){
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues values = new ContentValues();
         values.put("pending", 0);
         db.update(TABLE_ONE, values, "levelId=" + levelId, null);
     }

     public int[] increaseUserInfo(short values[]){
         int result[] = new int[3];
         SQLiteDatabase db = this.getWritableDatabase();
         if (values.length == 2) {
             db.execSQL("UPDATE " + TABLE_TWO + " SET bricks = bricks + " + values[0] + " WHERE id = 1");
             db.execSQL("UPDATE " + TABLE_TWO + " SET games = games + 1 WHERE id = 1");
             db.execSQL("UPDATE "+TABLE_TWO+ " SET powerUps = powerUps + "+values[1]+" WHERE id = 1");
         }
         Cursor res = db.rawQuery("SELECT * FROM "+TABLE_TWO+" WHERE id = 1", null);
         if (res.moveToFirst()){
             result[0] = res.getInt(res.getColumnIndex("bricks"));
             result[1] = res.getInt(res.getColumnIndex("games"));
             result[2] = res.getInt(res.getColumnIndex("powerUps"));
         }

         res.close();
         db.close();

         return result;
    }
}
