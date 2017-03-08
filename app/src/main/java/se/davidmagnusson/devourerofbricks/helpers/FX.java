package se.davidmagnusson.devourerofbricks.helpers;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
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

    private static SoundPool sounds = null;

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

    private static boolean ready = false;


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
        if (!ready) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AudioAttributes aa = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build();
                sounds = new SoundPool.Builder().setAudioAttributes(aa).setMaxStreams(10).build();
            } else {
                sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
            }

            sounds.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    Log.i("DoB", "loaded:" + sampleId);
                    ready = true;
                }
            });

            try {
                AssetManager assetManager = context.getAssets();
                AssetFileDescriptor descriptor;

                ready = false;
                descriptor = assetManager.openFd("fx/button.wav");
                menuButtonClicked = sounds.load(descriptor, 0);

                ready = false;
                descriptor = assetManager.openFd("fx/counting_points.wav");
                countingPoints = sounds.load(descriptor, 0);

                ready = false;
                descriptor = assetManager.openFd("fx/lose_life.wav");
                ballCrash = sounds.load(descriptor, 0);

                ready = false;
                descriptor = assetManager.openFd("fx/hard_brick.wav");
                hardBrick = sounds.load(descriptor, 0);

                ready = false;
                descriptor = assetManager.openFd("fx/brick_collision.wav");
                brickCollision = sounds.load(descriptor, 0);

                ready = false;
                descriptor = assetManager.openFd("fx/other_collision.wav");
                collision = sounds.load(descriptor, 0);

                ready = false;
                descriptor = assetManager.openFd("fx/new_highscore.wav");
                newHighscore = sounds.load(descriptor, 0);

                ready = false;
                descriptor = assetManager.openFd("fx/press_start.wav");
                pressStart = sounds.load(descriptor, 0);

            } catch (IOException ex) {
                Log.i("DoB", ex.toString());
            }
        }
    }

    /**
     * The method used to play sounds from the soundpool.
     * @param soundId The sound id as a int, use FX.*theSoundYouWant*
     * @param leftVolume the left volume
     * @param rightVolume the right volume
     * @param priority how high the priority are
     * @param loop how many times it's going to be looped , -1 is infinity
     * @param rate how fast it should be played, between 0.5 and 2.5
     */
    public static void play(int soundId, float leftVolume, float rightVolume, int priority, int loop, float rate){
        if (ready && soundId != 0){
            sounds.play(soundId, leftVolume, rightVolume, priority, loop, rate);
        }
    }
}
