package dab4au.cs2110.virginia.edu.ghosthunter;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by thinkdavid on 3/30/15.
 */

public class MenuActivity extends ActionBarActivity {

    Button playButton, settingButton, testButton;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final MediaPlayer buttonSound = MediaPlayer.create(MenuActivity.this, R.raw.button_click);

        playButton = (Button) findViewById(R.id.playButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSound.start();
                startActivity(new Intent("dab4au.cs2110.virginia.edu.ghosthunter.TESTGAME"));
            }
        });


    }


    protected void onPause() {
        super.onPause();
    }
}

