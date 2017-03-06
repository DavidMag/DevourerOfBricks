package se.davidmagnusson.devourerofbricks.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import se.davidmagnusson.devourerofbricks.R;
import se.davidmagnusson.devourerofbricks.activities.LevelSelectionActivity;
import se.davidmagnusson.devourerofbricks.activities.MainMenuActivity;
import se.davidmagnusson.devourerofbricks.helpers.FX;
import se.davidmagnusson.devourerofbricks.models.Model;
import se.davidmagnusson.devourerofbricks.BR;

/**
 * The view model for the score screen, most of the logic regarding the view is located here
 */
public class ScoreScreenViewModel extends BaseObservable {

    private volatile int gameTime;
    private volatile int gamePoints;
    private volatile int gameLife;
    private volatile int gameFinalScore;
    private volatile String header;
    private volatile String newHighscoreString;

    //Used for the "press to skip" method
    private volatile int tempGameTime;
    private volatile int tempGamePoints;
    private volatile int tempGameLife;
    private volatile int tempGameFinalScore;
    private volatile int lasHighscore;

    private volatile byte levelId;

    private volatile FX fx;

    private Model model;

    //The thread we will use at the counting animation
    Thread animationThread;

    /**
     * The constructor initialises the model and the FX, and then sets the new values with the
     * setValues() method
     * @param c the application context, used to get resources, initialise the model.
     * @param gameTime the number of seconds the game was
     * @param gamePoints the amount of points the player got
     * @param gameLife the number of life the player had wen the game ended
     * @param gameFinalScore the final score (gamePoints * (gameLife + levelId / 2) - gameTime)
     * @param gameWon if the player won or lost as a boolean
     * @param levelId which level it was
     */
    public ScoreScreenViewModel(Context c, int gameTime, int gamePoints, int gameLife, int gameFinalScore, boolean gameWon, byte levelId){
        model = Model.getInstance(c);
        fx = new FX(c);


        this.tempGameFinalScore = gameFinalScore;
        this.tempGamePoints = gamePoints;
        this.tempGameTime = gameTime;
        this.tempGameLife = gameLife;
        this.lasHighscore = model.getLevelHighscore(levelId);
        this.levelId = levelId;


        if (gameWon){
            setHeader(c.getResources().getString(R.string.score_screen_game_won));
        } else {
            setHeader(c.getResources().getString(R.string.score_screen_game_lost));
        }

        setValues(c, gameTime, gamePoints, gameLife, gameFinalScore, lasHighscore);
    }


    //<editor-fold desc="GETTERS">

    /**
     * Getter for the gameTime, used by the view
     * @return the gameTime as a String
     */
    @Bindable
    public String getGameTime() {
        return Integer.toString(gameTime);
    }

    /**
     * Getter for the gamePoints, used by the view
     * @return the gamePoints as a String
     */
    @Bindable
    public String getGamePoints() {
        return Integer.toString(gamePoints);
    }

    /**
     * Getter for the gameLife, used by the view
     * @return the gameLife as a String
     */
    @Bindable
    public String getGameLife() {
        return Integer.toString(gameLife);
    }

    /**
     * Getter for the final scorem used by the viwe
     * @return the final score as a String
     */
    @Bindable
    public String getGameFinalScore() {
        return Integer.toString(gameFinalScore);
    }

    /**
     * Getter for the header text, used by the view
     * @return the headers String
     */
    @Bindable
    public String getHeader() {
        return header;
    }

    /**
     * Getter for the "NEW HIGH SCORE" string
     * @return returns the new high score string
     */
    @Bindable
    public String getNewHighscoreString() {
        return newHighscoreString;
    }

    //</editor-fold>

    //<editor-fold desc="SETTERS">

    /**
     * Setter for the gameTime, notifies that the property has changed
     * @param gameTime the new gameTime that will be displayed
     */
    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
        notifyPropertyChanged(BR.gameTime);
    }

    /**
     * Setter for the gamePoints, notifies that the property has changed
     * @param gamePoints the new gamePoints that will be displayed
     */
    public void setGamePoints(int gamePoints) {
        this.gamePoints = gamePoints;
        notifyPropertyChanged(BR.gamePoints);
    }

    /**
     * Setter for the gameLife, notifies that the property has changed
     * @param gameLife the new gameLife that will be displayed
     */
    public void setGameLife(int gameLife) {
        this.gameLife = gameLife;
        notifyPropertyChanged(BR.gameLife);
    }

    /**
     * Setter for the final score, notifies that the property has changed
     * @param gameFinalScore the new final score will be displayed
     */
    public void setGameFinalScore(int gameFinalScore) {
        this.gameFinalScore = gameFinalScore;
        notifyPropertyChanged(BR.gameFinalScore);
    }

    /**
     * Setter for the header, notifies that the property has changed
     * @param header the header string, will be displayed
     */
    public void setHeader(String header) {
        this.header = header;
        notifyPropertyChanged(BR.header);
    }

    /**
     * Setter for the "NEW HIGH SCORE" string, notifies that the property has changed
     * @param newHighscoreString the new String, will be displayed as headline
     */
    public void setNewHighscoreString(String newHighscoreString) {
        this.newHighscoreString = newHighscoreString;
        notifyPropertyChanged(BR.newHighscoreString);
    }

    //</editor-fold>

    /**
     * Sets the values with a smooth enumeration animation
     * @param c the application context, used to get resources
     * @param gameTime the number of seconds the game was
     * @param gamePoints the points the player got
     * @param gameLife the number of life the player had at the end of the game
     * @param gameFinalScore the fianl score of the game
     * @param lastHighscore the high score for this level
     */
    private void setValues(final Context c, final int gameTime, final int gamePoints, final int gameLife, final int gameFinalScore, final int lastHighscore) {
        //Prepare the thread with the runnable method
        animationThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(600);
                    int tempValue = 0;

                    while (tempValue < gamePoints){
                        setGamePoints(tempValue++);
                        Thread.sleep(3);
                    }
                    Thread.sleep(300);

                    tempValue = 0;
                    while (tempValue < gameLife){
                        setGameLife(tempValue++);
                        Thread.sleep(5);
                    }
                    Thread.sleep(300);

                    tempValue = 0;
                    while (tempValue < gameTime){
                        setGameTime(tempValue++);
                        Thread.sleep(3);
                    }
                    Thread.sleep(300);

                    tempValue = 0;
                    while (tempValue < gameFinalScore){
                        setGameFinalScore(tempValue++);
                        Thread.sleep(2);
                    }
                } catch (InterruptedException e) {
                    //If it crashes set the values direct.
                    setGamePoints(gamePoints);
                    setGameFinalScore(gameFinalScore);
                    setGameLife(gameLife);
                    setGameTime(gameTime);
                } finally {
                    if (gameFinalScore > lastHighscore){
                        setNewHighscoreString(c.getString(R.string.score_screen_new_highscore));
                        model.newHighscore(levelId, gameFinalScore);
                    }
                }
            }
        });
        //Start thread
        animationThread.start();
    }

    /**
     * Plays button press sound and starts main menu
     * @param v the button that got pressed as a view
     */
    public void onMainMenuButtonClick(View v){
        fx.sounds.play(fx.menuButtonClicked, 1, 1,0, 0, 1);
        Intent intent = new Intent(v.getContext(), MainMenuActivity.class);
        v.getContext().startActivity(intent);
    }

    /**
     * Plays button press sound and starts the level selection menu
     * @param v the button that got pressed on as a view
     */
    public void onPlayAgainButtonClicked(View v){
        fx.sounds.play(fx.menuButtonClicked, 1, 1,0, 0, 1);
        Intent intent = new Intent(v.getContext(), LevelSelectionActivity.class);
        v.getContext().startActivity(intent);
    }

    /**
     * The "Skip animation" method, triggered by pressing the root view.
     * Stops the animation and sets the values direct.
     * @param v the view that got pressed, in this case the root view
     */
    public void onLayoutClick(View v){
        //Make sure that we dont get null pointer exception
        if (animationThread != null && animationThread.isAlive()){
            try {
                //Kill the thread
                animationThread.interrupt();
                animationThread = null;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            //Set the values
            setGameLife(tempGameLife);
            setGameFinalScore(tempGameFinalScore);
            setGamePoints(tempGamePoints);
            setGameTime(tempGameTime);

            //If this final score was better then the last high score update the high score
            //and show "new high score" text.
            if (tempGameFinalScore > this.lasHighscore){
                setNewHighscoreString(v.getContext().getString(R.string.score_screen_new_highscore));
                model.newHighscore(this.levelId, this.gameFinalScore);
            }
        }
    }
}