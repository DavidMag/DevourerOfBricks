package se.davidmagnusson.devourerofbricks.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableArrayList;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

import se.davidmagnusson.devourerofbricks.R;
import se.davidmagnusson.devourerofbricks.adapters.LevelItemAdapter;
import se.davidmagnusson.devourerofbricks.database.SQLiteDB;
import se.davidmagnusson.devourerofbricks.helpers.FontReplacer;
import se.davidmagnusson.devourerofbricks.models.Model;

/**
 * The view model for the Level selection activity.
 */
public class LevelSelectionViewModel extends BaseObservable{

    private int totalHighscore;
    private ObservableArrayList<LevelItemViewModel> listItems;
    private Model model;

    /**
     * The constructor, takes a context as a parameter to initialise the DB handler.
     * @param c the activity context
     */
    public LevelSelectionViewModel(Context c){
        model = Model.getInstance(c);

        setTotalHighscore(model.getTotalHighscore());
        listItems = model.getLevels(c);
    }

    //<editor-fold desc="GETTERS AND SETTERS">

    /**
     * Returns the list of levels to put in the recycle view
     * @return returns the list of levels
     */
    public ObservableArrayList<LevelItemViewModel> getList(){
        return listItems;
    }

    /**
     * A simple getter for the total high score.
     * @return returns the total high score as a string so that the text view can display it
     */
    @Bindable
    public String getTotalHighscore() {
        return Integer.toString(totalHighscore);
    }

    /**
     * A simple setter for the total high score that also notifies the text view when changed
     * @param totalHighscore the new totalhighscore
     */
    private void setTotalHighscore(int totalHighscore) {
        this.totalHighscore = totalHighscore;
        notifyPropertyChanged(R.id.num_total_highscore);
    }

    //</editor-fold>

    /**
     * This method sets the adapter and the list for the recycle view
     * @param view the recycle view
     * @param list the list that will be used (the result from getList())
     */
    @BindingAdapter("app:items")
    public static void bindList(RecyclerView view, ObservableArrayList<LevelItemViewModel> list){
        view.setAdapter(new LevelItemAdapter(list));
    }

}
