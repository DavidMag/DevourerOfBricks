package se.davidmagnusson.devourerofbricks.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import se.davidmagnusson.devourerofbricks.R;
import se.davidmagnusson.devourerofbricks.activities.GameActivity;
import se.davidmagnusson.devourerofbricks.activities.LoadingScreenActivity;
import se.davidmagnusson.devourerofbricks.helpers.FX;

/**
 * The view model for the items in the recycle view in the level selection menu.
 */
public class LevelItemViewModel {

    private byte levelId;
    private int highscore;
    private String highscoreString;
    private int imgId;
    public int minHighscore;
    private boolean unlocked;
    public int pending;

    /**
     * Simple constructor that sets all the values of the object
     * @param levelId the level id which will be shown and send to the game activity
     * @param highscore the high score this level
     * @param imgId the resId for the img of the brick layout
     */
    public LevelItemViewModel(Context c, byte levelId, int highscore, int imgId, int minHighscore, int totalHighscore){
        if (totalHighscore < minHighscore){
            highscoreString = c.getResources().getString(R.string.level_item_not_unlocked, minHighscore);
            unlocked = false;
            this.imgId = R.mipmap.bricklayout_locked;
        } else {
            highscoreString = c.getResources().getString(R.string.level_item_high_score) + highscore;
            unlocked = true;
            this.imgId = imgId;
        }

        this.highscore = highscore;
        this.levelId = levelId;
        this.minHighscore = minHighscore;

        if (FX.sounds == null){
            FX.initFX(c.getApplicationContext());
        }
    }

    //<editor-fold desc="GETTERS AND SETTERS">

    /**
     * Simple getter for the levelId that converts it to String for the text view
     * @return the levl id as a String.
     */
    public String getLevelId() {
        return Byte.toString(levelId);
    }

    /**
     * This method returns the highscore + the string before it if the level is unlocked
     * else it returns info about how much you need to unlock it
     * @return the highscore as a string
     */
    public String getHighscore() {
        return highscoreString;
    }

    /**
     * Simple getter for the imgId, if not unlocked it'll always be image of a padlock
     * @return the resId for the image
     */
    public int getImgId() {
        return imgId;
    }

    /**
     * Used by the view to see if it's clickable or not
     * @return boolean if it is unlocked or not
     */
    public boolean isUnlocked(){
        return unlocked;
    }

    //</editor-fold>

    /**
     * My own implementation of setting the image source for the image view.
     * @param imageView the image view
     * @param resource the imgId (getImgId())
     */
    @BindingAdapter({"app:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }



    /**
     * The on click method sends a intent to the loading screen activity class with info about which level
     * @param view the view that got pressed
     */
    public void onClick(View view){
        FX.sounds.play(FX.menuButtonClicked, 1, 1, 0, 0, 1);
        Intent intent = new Intent(view.getContext(), LoadingScreenActivity.class);
        intent.putExtra("level", levelId);
        view.getContext().startActivity(intent);
    }
}
