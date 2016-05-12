package dab4au.cs2110.virginia.edu.ghosthunter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

/**
 * Created by thinkdavid on 4/1/15.
 */
public class TestGame extends Activity implements View.OnTouchListener, View.OnClickListener {

    David hero;
    ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
    ArrayList<Coin> coins = new ArrayList<Coin>();
    ArrayList<Laser> lasers = new ArrayList<Laser>();
    ArrayList<FreezeRay> freezeRays = new ArrayList<FreezeRay>();

    Handler frame = new Handler();

    movePacMan move = null;
    shootLaser shoot = null;

    private static final int FRAME_RATE = 20;

    private boolean moving;
    private boolean laserShot;
    private boolean canShoot;
    private boolean isFrozen;

    private int idButton;
    private int ticks, laserStart, freezeStart;
    private int ghostsKilled;

    MediaPlayer laserShoot = null;
    private int score;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        score = 0;

        MediaPlayer mixTape = MediaPlayer.create(this, R.raw.soundtrack1);
        MediaPlayer mixTape2 = MediaPlayer.create(this, R.raw.soundtrack2);
        laserShoot = MediaPlayer.create(this, R.raw.laser_sound);
        mixTape.start();
        if (!mixTape.isPlaying()) {
            mixTape2.start();
        }

        Handler h = new Handler();
        hero = new David();
        moving = laserShot = isFrozen = false;
        canShoot = true;
        for (int i = 0; i < 5; i++) {
            Ghost ghost1 = new Ghost(hero);
            ghosts.add(ghost1);
        }
        ghostsKilled = 0;

        //** Set up Buttons **//
        ((ImageButton) findViewById(R.id.bdown)).setOnTouchListener(this);
        ((ImageButton) findViewById(R.id.bup)).setOnTouchListener(this);
        ((ImageButton) findViewById(R.id.bright)).setOnTouchListener(this);
        ((ImageButton) findViewById(R.id.bleft)).setOnTouchListener(this);

        ((ImageButton) findViewById(R.id.bshoot)).setOnClickListener(this);

        ((TextView) findViewById(R.id.score)).setText("Score: " + score);
        // copy this and put it under the loop of the program, with an Asynctask to Update Score

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                initGfx();
            }
        }, 1000);

    }

    public void initGfx() {
        Log.w("initializeGfx", "Initialized Graphics");
        ((TestView) findViewById(R.id.the_canvas)).setPacManLocation(hero.getPosition());
        for (int i = 0; i < ghosts.size(); i++) {
            ((TestView) findViewById(R.id.the_canvas)).addGhosts(ghosts.get(i).getPosition());
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
        if (canShoot) {
            laserShot = true;
            laserStart = ticks;
            canShoot = false;

            laserShoot.start();

            Laser l = new Laser(hero);
            lasers.add(l);
            ((TestView) findViewById(R.id.the_canvas)).addLasers(l.getPosition());
        }
    }


    // ******* THREADS ********* //

    private class shootLaser extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            for (int i = 0; i < lasers.size(); i++) {
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
//                    Log.w("Hero Position:", hero.getPosition().toString());
                    break;
                case R.id.bleft:
                    hero.moveLeft();
//                    Log.w("Hero Position:", hero.getPosition().toString());
                    break;
                case R.id.bdown:
                    hero.moveDown();
//                    Log.w("Hero Position:", hero.getPosition().toString());
                    break;
                case R.id.bright:
                    hero.moveRight();
//                    Log.w("Hero Position:", hero.getPosition().toString());
                    break;
            }
            return null;
        }

    }

    private class setPosition extends AsyncTask<TestView, Void, Void> {

        @Override
        protected Void doInBackground(TestView... params) {
            params[0].setDirection(hero.getDirection());
            params[0].setPacManLocation(hero.getPosition());

            if (!ghosts.isEmpty()) {
                for (int i = 0; i < ghosts.size(); i++) {
                    params[0].setGhostPosition(i, ghosts.get(i).getPosition());
                }
            }

            if (!lasers.isEmpty()) {
                for (int i = 0; i < lasers.size(); i++) {
                    params[0].setLaserPosition(i, lasers.get(i).getPosition());
                }
            }
            return null;
        }
    }

    private class moveGhosts extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            for (int i = 0; i < ghosts.size(); i++) {
                ghosts.get(i).move();
            }
            return null;
        }
    }

    private class checkForCollisions extends AsyncTask<TestView, Void, Boolean> {

        @Override
        protected Boolean doInBackground(TestView... params) {
            boolean gameover1 = false;
            // ***** CHECK TO SEE IF GHOSTS COLLIDE WITH HERO ***** //
            if (ghosts.size() > 0) {
                for (int i = 0; i < ghosts.size(); i++) {
                    if (hero.isColliding(ghosts.get(i).getHitbox())) {
                        gameover1 = true;
                    }

                    // ****** CHECK TO SEE IF GHOSTS COLLIDE WITH LASER ***** //
                    if (lasers.size() > 0) {
                        if (lasers.get(0).isColliding(ghosts.get(i).getHitbox())) {
                            ((TestView) params[0]).deleteLasers();
                            lasers.remove(0);
                            ((TestView) params[0]).setShooting(false);
                            canShoot = true;
                            laserShot = false;
                            ghosts.get(i).setHealth(ghosts.get(i).getHealth() - 1);

                            if (ghosts.get(i).getHealth() == 0) {
                                ((TestView) params[0]).deleteGhosts(i);
                                Coin coin1 = new Coin(ghosts.get(i));
                                coins.add(coin1);
                                ((TestView) params[0]).addCoins(coin1.getPosition());
                                ghosts.remove(i);
                                ghostsKilled++;
                                score += 10;
                            }
                        }
                    }

                    // ****** CHECK TO SEE IF GHOSTS COLLIDE WITH EACH OTHER ****** //
                    for (int j = 0; j < ghosts.size() - 1; j++) {
                        if (ghosts.get(j).isColliding(ghosts.get(j + 1).getHitbox())) {
                            Rect intersection = ghosts.get(j).getHitbox();
                            intersection.intersect(ghosts.get(j + 1).getHitbox());
//                            Log.w("Collision", "Ghost " + j + " and Ghost " + j + 1);
                            if (intersection.width() >= intersection.height()) {
                                if (ghosts.get(j).isNorth()) {
                                    ghosts.get(j).setPosition(new Point(ghosts.get(j).getPosition().x, ghosts.get(j).getPosition().y - intersection.height()));
                                    ghosts.get(j + 1).setPosition(new Point(ghosts.get(j + 1).getPosition().x, ghosts.get(j + 1).getPosition().y + intersection.height()));
                                } else if (ghosts.get(j).isSouth()) {
                                    ghosts.get(j).setPosition(new Point(ghosts.get(j).getPosition().x, ghosts.get(j).getPosition().y + intersection.height()));
                                    ghosts.get(j + 1).setPosition(new Point(ghosts.get(j + 1).getPosition().x, ghosts.get(j + 1).getPosition().y - intersection.height()));
                                }
                            } else if (intersection.width() < intersection.height()) {
                                if (ghosts.get(j).isWest()) {
                                    ghosts.get(j).setPosition(new Point(ghosts.get(j).getPosition().x + intersection.width(), ghosts.get(j).getPosition().y));
                                    ghosts.get(j + 1).setPosition(new Point(ghosts.get(j + 1).getPosition().x - intersection.width(), ghosts.get(j + 1).getPosition().y));
                                } else if (ghosts.get(j).isEast()) {
                                    ghosts.get(j).setPosition(new Point(ghosts.get(j).getPosition().x - intersection.width(), ghosts.get(j).getPosition().y));
                                    ghosts.get(j + 1).setPosition(new Point(ghosts.get(j + 1).getPosition().x + intersection.width(), ghosts.get(j + 1).getPosition().y));
                                }
                            }
                        }
                    }
                }

            }

            // ****** CHECK TO SEE IF PACMAN COLLIDES WITH COIN ****** //
            if (coins.size() > 0) {
                for (int i = 0; i < coins.size(); i++) {
                    if (hero.isColliding(coins.get(i).getHitbox())) {
                        score += coins.get(i).getPoints();
                        ((TestView) params[0]).deleteCoins(i);
                        coins.remove(i);
                    }
                }
            }

            // ****** CHECK TO SEE IF PACMAN COLLIDES WITH FREEZERAY ****** //
            if (freezeRays.size() > 0) {
                for (int i = 0; i < freezeRays.size(); i++) {
                    if (hero.isColliding(freezeRays.get(i).getHitbox())) {
                        score += 30;
                        ((TestView) params[0]).deleteFreezeRay(i);
                        freezeRays.remove(i);
                        isFrozen = true;
                        freezeStart = ticks;
                    }
                }
            }
            return gameover1;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                Toast toast = Toast.makeText(TestGame.this, "You died! \nYour Score Was: " + score, Toast.LENGTH_LONG);
                toast.show();
                finish();
            }
        }


    }

    private class createGhosts extends AsyncTask<TestView, Void, Void> {
        @Override
        protected Void doInBackground(TestView... params) {
            if (ghostsKilled < 5) {
                ghosts.add(new Ghost(hero));
                ((TestView) params[0]).addGhosts(ghosts.get(ghosts.size() - 1).getPosition());
            } else if (ghostsKilled >= 5 && ghostsKilled <= 7) {
                ((TestView) params[0]).setLevel(2);
                ghosts.add(new GhostLevel2(hero));
                Log.w("Ghost added: ", ghosts.get(ghosts.size() - 1).toString());
                ((TestView) params[0]).addGhosts(ghosts.get(ghosts.size() - 1).getPosition());
            } else {
                ((TestView) params[0]).setLevel(3);
                ghosts.add(new GhostLevel3(hero));
                Log.w("Ghost added: ", ghosts.get(ghosts.size() - 1).toString());
                ((TestView) params[0]).addGhosts(ghosts.get(ghosts.size() - 1).getPosition());
            }
            return null;
        }
    }

    private class createFreezeRay extends AsyncTask<TestView, Void, Void> {
        @Override
        protected Void doInBackground(TestView... params) {
            FreezeRay f = new FreezeRay();
            freezeRays.add(f);
            ((TestView) params[0]).addFreezeRay(f.getPosition());
            return null;
        }
    }

    private class updateScore extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
//           Log.w("doInBackground", params[0].toString());
            return params[0];
        }

        @Override
        protected void onPostExecute(Integer integer) {
//            Log.w("doInBackground", integer.toString());
            super.onPostExecute(integer);
            ((TextView) findViewById(integer)).setText("Score: " + score);
        }


    }


    // **********           FRAME UPDATE            ********** //
    private Runnable frameUpdate = new Runnable() {
        @Override
        synchronized public void run() {
            ticks++;

            frame.removeCallbacks(frameUpdate);

            if (!ghosts.isEmpty() && !isFrozen) {
                new moveGhosts().execute();
            }

            if (isFrozen) {
                if (freezeStart + 100 < ticks) {
                    isFrozen = false;
                }
            }

            new checkForCollisions().execute((TestView) findViewById(R.id.the_canvas));

            if (laserShot) {
                if (laserStart + 20 > ticks) {
                    shoot = new shootLaser();
                    shoot.execute();
                    ((TestView) findViewById(R.id.the_canvas)).setShooting(true);
                } else {
                    shoot = null;
                    laserShot = false;
                    canShoot = true;
                    lasers.remove(0);
                    ((TestView) findViewById(R.id.the_canvas)).deleteLasers();
                    ((TestView) findViewById(R.id.the_canvas)).setShooting(false);
                }
            }

            if (score < 100) {
                if (ticks % 500 == 0) {
                    new createGhosts().execute((TestView) findViewById(R.id.the_canvas));
                    score += 10;
                }
            } else if (score >= 100 && score < 200) {
                if (ticks % 350 == 0) {
                    new createGhosts().execute((TestView) findViewById(R.id.the_canvas));
                    score += 20;
                }
            } else {
                if (ticks % 200 == 0) {
                    new createGhosts().execute((TestView) findViewById(R.id.the_canvas));
                    score += 30;
                }
            }

            if (ticks % 1000 == 0) {
                new createFreezeRay().execute((TestView) findViewById(R.id.the_canvas));
            }

            if (moving) {
                move = new movePacMan();
                move.execute(idButton);
            }

            new setPosition().execute((TestView) findViewById(R.id.the_canvas));
            new updateScore().execute(R.id.score);

            ((TestView) findViewById(R.id.the_canvas)).invalidate();
            frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };


}

