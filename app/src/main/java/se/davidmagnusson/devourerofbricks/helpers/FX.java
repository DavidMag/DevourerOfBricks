package se.davidmagnusson.devourerofbricks.helpers;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

/**
 * Helper class to play sound effects in the application
 */
public class FX {

    public SoundPool sounds;

    public int menuButtonClicked;

    public int ballCrash;
    public int brickCollision;
    public int paddleCollision;
    public int wallCollision;
    public int win;
    public int loose;
    public int pause;
    public int resume;

    /**
     * Initialises the soundpool and all the IDs for the sounds.
     * @param context the application context, used to get access to the assets
     */
    public FX(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sounds = new SoundPool.Builder().setMaxStreams(10).build();
        } else {
            sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        }

        try{
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("fx/button.wav");
            menuButtonClicked = sounds.load(descriptor, 0);

        } catch (IOException ex){
            Log.i("DoB", ex.toString());
        }
    }

}
