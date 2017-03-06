package se.davidmagnusson.devourerofbricks.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

import se.davidmagnusson.devourerofbricks.R;

/**
 * A music player in form of a service
 */
public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    private MediaPlayer player;
    private boolean ready = false;
    //Needs to be volatile because it's accessed from different threads
    private volatile boolean isInApp;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * This method is the one that handles the intent.
     * Either it init a new media player, plays the music or pauses it
     * @param intent the intent that invoked the service
     * @param flags
     * @param startId
     * @return returns if it should be sticky or not
     */
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getStringExtra("action") != null) {
            switch (intent.getStringExtra("action")) {
                case "create":
                    Log.i("DoB", "Create");
                    onMyCreate(intent.getStringExtra("song"));
                    break;
                case "resume":
                    Log.i("DoB", "Resume");
                    onResume();
                    break;
                case "pause":
                    Log.i("DoB", "Pause");
                    onPause();
                    break;
            }
        }
        return START_NOT_STICKY;
    }

    /**
     * Starts the music when the async preparation of the media player is ready.
     * @param mp the used media player
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        player.start();
        ready = true;

    }

    /**
     * Pauses the music with "don't pause between activities" function.
     */
    private void onPause(){
        isInApp = false;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (!isInApp) {
                    player.pause();
                }
            }
        });
        t.start();
    }

    /**
     * Initialise the media player with the new song
     * @param song the song path, example "music/example_song.wav"
     */
    private void onMyCreate(String song){
        try {
            AssetFileDescriptor afd = getAssets().openFd(song);
            player = new MediaPlayer();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.setLooping(true); // Set looping
            player.setVolume(100,100);
            player.setOnPreparedListener(this);
            player.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resumes the music if the media player is ready and sets the isInApp boolean to true for the
     * "don't turn of between activity" function.
     */
    private void onResume(){
        isInApp = true;
        if (ready){
            player.start();
        }
    }

    /**
     * Releases the media player when the service is destroyed
     */
    @Override
    public void onDestroy() {
        if (player != null) {
            player.stop();
            player.release();
        }
    }
}