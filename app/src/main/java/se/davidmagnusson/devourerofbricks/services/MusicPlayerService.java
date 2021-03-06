package se.davidmagnusson.devourerofbricks.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.preference.PreferenceManager;
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
    private String currentSong = "";

    public IBinder onBind(Intent arg0) {
        return null;
    }

    /**
     * This method is the one that handles the intent.
     * Either it init a new media player, plays the music or pauses it
     * @param intent the intent that invoked the service
     * @param flags if there is any flags on the intent
     * @param startId startId, if there are multiple intents
     * @return returns if it should be sticky or not
     */
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences prefReader = PreferenceManager.getDefaultSharedPreferences(this);

        if (intent.getStringExtra("action") != null && prefReader.getBoolean("music", true)) {
            switch (intent.getStringExtra("action")) {
                case "create":
                    Log.i("DoB", "Create");
                    if (!ready) {
                        currentSong = intent.getStringExtra("song");
                        onMyCreate(currentSong);
                    } else if (currentSong != null && !currentSong.equals(intent.getStringExtra("song"))){
                        onDestroy();
                        currentSong = intent.getStringExtra("song");
                        onMyCreate(currentSong);
                    }
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
        if (intent.getBooleanExtra("changedPref", false)){

            if (prefReader.getBoolean("music", true)){
                if (!ready) {
                    onMyCreate(intent.getStringExtra("song"));
                }
            } else {
                if (ready) {
                    player.stop();
                    player.release();
                    player = null;
                    ready = false;
                }
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
        if (player != null) {
            player.start();
            ready = true;
        }

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
                if (!isInApp && player != null) {
                    onDestroy();
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
        if (!ready) {
            try {
                AssetFileDescriptor afd = getAssets().openFd(song);
                player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                player.setLooping(true);
                player.setVolume(100,100);
                player.setOnPreparedListener(this);
                player.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Resumes the music if the media player is ready and sets the isInApp boolean to true for the
     * "don't turn of between activity" function.
     */
    private void onResume(){
        isInApp = true;
        if (ready && player != null){
            player.start();
        }
    }

    /**
     * Releases the media player when the service is destroyed
     */
    @Override
    public void onDestroy() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
        ready = false;
    }
}