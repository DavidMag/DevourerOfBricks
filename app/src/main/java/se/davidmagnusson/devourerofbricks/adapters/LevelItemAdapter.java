package se.davidmagnusson.devourerofbricks.adapters;

import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import se.davidmagnusson.devourerofbricks.databinding.LevelItemLayoutBinding;
import se.davidmagnusson.devourerofbricks.helpers.FontReplacer;
import se.davidmagnusson.devourerofbricks.viewmodels.LevelItemViewModel;

/**
 * The adapter the recycle view that holds level uses
 */
public class LevelItemAdapter extends RecyclerView.Adapter<LevelItemAdapter.ViewHolder> {

    /**
     * The View holder that the adapter uses
     */
    class ViewHolder extends RecyclerView.ViewHolder{

        LevelItemLayoutBinding binder;

        ViewHolder(View itemView) {
            super(itemView);
            binder = DataBindingUtil.bind(itemView);
        }
    }

    private ArrayList<LevelItemViewModel> listItems;

    /**
     * Simple constructor that takes a list as a param to put in the recycle view
     * @param list the list of levels
     */
    public LevelItemAdapter(ArrayList<LevelItemViewModel> list){
        this.listItems = list;
    }

    /**
     * Auto called and creates the viewholder that binds the level items to their layout
     * And also changes the font of the view holder
     * @param parent the view group that the layout will be in
     * @param viewType the view type as a int
     * @return a new view holder that we'll be using
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LevelItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false).getRoot();
        FontReplacer.setAppFont((ViewGroup) view, Typeface.createFromAsset(view.getContext().getAssets(), "fonts/EarlyGameBoy.ttf"), false);
        return new ViewHolder(view);
    }

    /**
     * Auto called when a new level item is going to be displayed. It takes a new level and binds it to
     * a level item layout and publish
     * @param holder our view holder
     * @param position The index the level item has in the list
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final LevelItemViewModel item = listItems.get(position);
        holder.binder.setViewmodel(item);
        holder.binder.executePendingBindings();
    }

    /**
     * Getter for the quantity of levels
     * @return the size of the level list
     */
    @Override
    public int getItemCount() {
        return listItems.size();
    }


}
