package se.davidmagnusson.devourerofbricks.activities;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import se.davidmagnusson.devourerofbricks.gameengine.GameView;

/**
 * Created by davidmagnusson on 2017-02-28.
 */

public class GameActivity extends Activity {

    GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the screens resolution
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        gameView = new GameView(this, size.x, size.y);
        setContentView(gameView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.onPause();
    }
}
