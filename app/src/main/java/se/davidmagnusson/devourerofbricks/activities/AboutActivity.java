package se.davidmagnusson.devourerofbricks.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import se.davidmagnusson.devourerofbricks.R;
import se.davidmagnusson.devourerofbricks.helpers.FontReplacer;
import se.davidmagnusson.devourerofbricks.services.MusicPlayerService;

/**
 * Created by davidmagnusson on 2017-03-13.
 */
public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_layout);

        TextView body = (TextView) findViewById(R.id.about_body);
        body.setText(Html.fromHtml(getString(R.string.about_body)));
        body.setMovementMethod(LinkMovementMethod.getInstance());
        body.setMovementMethod(ScrollingMovementMethod.getInstance());

        FontReplacer.setAppFont((ViewGroup) findViewById(android.R.id.content).getRootView(),
                Typeface.createFromAsset(getAssets(), "fonts/EarlyGameBoy.ttf"),
                false);

        AdView ad = (AdView) findViewById(R.id.about_ad);
        AdRequest adReq = new AdRequest.Builder().addKeyword("games").build();
        ad.loadAd(adReq);

        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.putExtra("action", "create");
        intent.putExtra("song", "music/first.mp3");
        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.putExtra("action", "resume");
        startService(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.putExtra("action", "pause");
        startService(intent);
    }
}
