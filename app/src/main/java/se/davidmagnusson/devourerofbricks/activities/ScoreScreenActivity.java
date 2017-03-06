package se.davidmagnusson.devourerofbricks.activities;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.ViewGroup;

import se.davidmagnusson.devourerofbricks.R;
import se.davidmagnusson.devourerofbricks.databinding.ScoreScreenLayoutBinding;
import se.davidmagnusson.devourerofbricks.helpers.FontReplacer;
import se.davidmagnusson.devourerofbricks.services.MusicPlayerService;
import se.davidmagnusson.devourerofbricks.viewmodels.ScoreScreenViewModel;

/**
 * The score screen activity class does the things around, like starting music, changing typeface
 * and so on, the rest is managed by the view model
 */
public class ScoreScreenActivity extends Activity {

    private ScoreScreenViewModel viewModel;

    /**
     * The on create method is auto called by android when the activity is created.
     * Initialises and sets the view model, changes the typeface and starts the menu music again.
     * @param savedInstanceState auto generated by android, not used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();

        viewModel = new ScoreScreenViewModel(
                this,
                (int) i.getLongExtra("time", 0),
                i.getIntExtra("points", 0),
                i.getByteExtra("life",(byte) 0),
                (int) i.getLongExtra("finalScore", 0),
                i.getBooleanExtra("won", false),
                i.getByteExtra("level", (byte)1));
        ScoreScreenLayoutBinding binding = DataBindingUtil.setContentView(this, R.layout.score_screen_layout);
        binding.setViewmodel(viewModel);

        FontReplacer.setAppFont((ViewGroup) findViewById(android.R.id.content).getRootView(),
                Typeface.createFromAsset(getAssets(), "fonts/EarlyGameBoy.ttf"),
                false);

        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.putExtra("action", "create");
        intent.putExtra("song", "music/first.mp3");
        startService(intent);
    }

    /**
     * Auto called by android when the activity gets focus again, starts the music again
     */
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.putExtra("action", "resume");
        startService(intent);
    }

    /**
     * Auto called by android when activity loses focus, pauses the music
     */
    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.putExtra("action", "pause");
        startService(intent);
    }
}
