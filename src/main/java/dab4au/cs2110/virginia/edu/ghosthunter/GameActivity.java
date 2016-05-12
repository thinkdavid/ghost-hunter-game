package dab4au.cs2110.virginia.edu.ghosthunter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;

/**
 * Created by thinkdavid on 4/1/15.
 */
public class GameActivity extends Activity implements View.OnTouchListener, View.OnClickListener {

    PacMan hero;
    ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
    ArrayList<Laser> lasers = new ArrayList<Laser>();

    Handler frame = new Handler();

    movePacMan move = null;
    shootLaser shoot = null;

    private static final int FRAME_RATE = 20;

    private boolean moving;
    private boolean laserShot;

    private int idButton;
    private int ticks, laserStart;

    MediaPlayer laserShoot = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        MediaPlayer mixTape = MediaPlayer.create(this, R.raw.soundtrack1);
        laserShoot = MediaPlayer.create(this, R.raw.laser_sound);
        mixTape.start();
        Handler h = new Handler();
        hero = new PacMan();
        moving = laserShot = false;
        for (int i = 0; i < 5; i++) {
            Ghost ghost1 = new Ghost(hero);
            ghosts.add(ghost1);
            Log.w("Ghost List", ghosts.get(i).getPosition().toString());
        }


        // Find out OnPressListener

        //** Set up Buttons **//
        ((ImageButton) findViewById(R.id.bdown)).setOnTouchListener(this);
        ((ImageButton) findViewById(R.id.bup)).setOnTouchListener(this);
        ((ImageButton) findViewById(R.id.bright)).setOnTouchListener(this);
        ((ImageButton) findViewById(R.id.bleft)).setOnTouchListener(this);

        ((ImageButton) findViewById(R.id.bshoot)).setOnClickListener(this);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                initGfx();
            }
        }, 1000);

    }

    public void initGfx() {
        Log.w("initializeGfx", "Initialized Graphics");
        ((GameView) findViewById(R.id.the_canvas)).setPacManLocation(hero.getPosition());
        for (int i = 0; i < ghosts.size(); i++) {
            ((GameView) findViewById(R.id.the_canvas)).addGhosts(ghosts.get(i).getPosition());
        }
        frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    // ******           BUTTON CLICKS           ****** //
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moving = true;
                idButton = v.getId();
                break;
            case MotionEvent.ACTION_UP:
                moving = false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        laserShot = true;
        laserStart = ticks;
        laserShoot.start();
        Laser l = new Laser(hero);
        lasers.add(l);
        ((GameView) findViewById(R.id.the_canvas)).addLasers(l.getPosition());

        Log.w("Clicked Shoot", "" + laserShot + laserStart);
    }


    // ******* THREADS ********* //

    private class shootLaser extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            for (int i=0; i< lasers.size(); i++) {
                lasers.get(i).shoot();
            }
            return null;
        }
    }

    private class movePacMan extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... params) {
            switch (params[0]) {
                case R.id.bup:
                    hero.moveUp();
                    Log.w("Hero Position:", hero.getPosition().toString());
                    break;
                case R.id.bleft:
                    hero.moveLeft();
                    Log.w("Hero Position:", hero.getPosition().toString());
                    break;
                case R.id.bdown:
                    hero.moveDown();
                    Log.w("Hero Position:", hero.getPosition().toString());
                    break;
                case R.id.bright:
                    hero.moveRight();
                    Log.w("Hero Position:", hero.getPosition().toString());
                    break;
            }
            return null;
        }

    }

    private class setPosition extends AsyncTask<GameView, Void, Void> {

        @Override
        protected Void doInBackground(GameView... params) {
            params[0].setDirection(hero.getDirection());
            params[0].setPacManLocation(hero.getPosition());

            for (int i=0; i< ghosts.size(); i++) {
                params[0].setGhostPosition(i, ghosts.get(i).getPosition());
            }
            for (int i=0; i< lasers.size(); i++) {
                params[0].setLaserPosition(i, lasers.get(i).getPosition());
            }
            return null;
        }
    }

    private class moveGhosts extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            for (int i=0; i<ghosts.size(); i++) {
                ghosts.get(i).move();
            }
            return null;
        }
    }



    // **********           FRAME UPDATE            ********** //
    private Runnable frameUpdate = new Runnable() {
        @Override
        synchronized public void run() {
            ticks++;

            frame.removeCallbacks(frameUpdate);

            new moveGhosts().execute();

            if (laserShot) {
                if (laserStart + 200 > ticks) {
                    shoot = new shootLaser();
                    shoot.execute();
                } else {
                    shoot = null;
                    laserShot = false;
                    lasers.remove(0);
                }
            }

            if (moving) {
                move = new movePacMan();
                move.execute(idButton);
            }

            new setPosition().execute((GameView) findViewById(R.id.the_canvas));

            ((GameView) findViewById(R.id.the_canvas)).invalidate();
            frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };


}
