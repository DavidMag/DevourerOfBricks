package se.davidmagnusson.devourerofbricks.helpers;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

import se.davidmagnusson.devourerofbricks.database.SQLiteDB;
import se.davidmagnusson.devourerofbricks.models.Model;

/**
 * Helper class to play sound effects in the application, static access
 */
public class FX {

    public static SoundPool sounds = null;

    public static int menuButtonClicked;
    public static int countingPoints;

    public static int ballCrash;
    public static int brickCollision;
    public static int hardBrick;
    public static int collision;
    public int win;
    public int loose;
    public static int newHighscore;
    public static int pressStart;


    /**
     * private constructor that does nothing.
     */
    private FX(){
    }

    /**
     * This method initialises the sound pool
     * @param context the context uesd, should be application context not activity
     */
    public static void initFX(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sounds = new SoundPool.Builder().setMaxStreams(10).build();
        } else {
            sounds = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        }

        sounds.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                Log.i("DoB", "loaded:" + sampleId);
            }
        });

        try{
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("fx/button.wav");
            menuButtonClicked = sounds.load(descriptor, 0);

            descriptor = assetManager.openFd("fx/counting_points.wav");
            countingPoints = sounds.load(descriptor, 0);

            descriptor = assetManager.openFd("fx/lose_life.wav");
            ballCrash = sounds.load(descriptor, 0);

            descriptor = assetManager.openFd("fx/hard_brick.wav");
            hardBrick = sounds.load(descriptor, 0);

            descriptor = assetManager.openFd("fx/brick_collision.wav");
            brickCollision = sounds.load(descriptor, 0);

            descriptor = assetManager.openFd("fx/other_collision.wav");
            collision = sounds.load(descriptor, 0);

            descriptor = assetManager.openFd("fx/new_highscore.wav");
            newHighscore = sounds.load(descriptor, 0);

            descriptor = assetManager.openFd("fx/press_start.wav");
            pressStart = sounds.load(descriptor, 0);

        } catch (IOException ex){
            Log.i("DoB", ex.toString());
        }
    }
}
