package dab4au.cs2110.virginia.edu.ghosthunter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by thinkdavid on 4/6/15.
 */
public class GameView extends View {

    Bitmap background = null;
    Bitmap pacManLeft = null;
    Bitmap pacManRight = null;
    Bitmap ghost1 = null;
    Bitmap laser = null;


    Rect pacmanHitBox = new Rect(0, 0, 0, 0);
    Rect laserHitBox = new Rect(0, 0, 0, 0);
    ArrayList<Rect> ghostHitBox;

    char direction;

    Point pacmanPosition;
    ArrayList<Point> laserPosition;
    ArrayList<Point> ghostPosition;


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pacmanPosition = new Point(50, 50);
        ghostPosition = new ArrayList<Point>();
        laserPosition = new ArrayList<Point>();

        background = BitmapFactory.decodeResource(getResources(), R.drawable.ghosthuntermazepsdbackground);
        pacManLeft = BitmapFactory.decodeResource(getResources(), R.drawable.leftlookguy);
        pacManRight = BitmapFactory.decodeResource(getResources(), R.drawable.rightlookguy);
        ghost1 = BitmapFactory.decodeResource(getResources(), R.drawable.mainghostlevel1);
        laser = BitmapFactory.decodeResource(getResources(), R.drawable.gunlaser);

        pacmanHitBox = new Rect(pacmanPosition.x, pacmanPosition.y, pacManLeft.getWidth(), pacManLeft.getHeight());

    }

    @Override
    synchronized public void onDraw(Canvas canvas) {
        //create a blank canvas
        canvas.drawBitmap(background, null, new Rect(0, 0, 1280, 720), null);

        switch(direction) {
            case 'u':
                canvas.drawBitmap(pacManRight, pacmanPosition.x, pacmanPosition.y, null);
                break;
            case 'd':
                canvas.drawBitmap(pacManRight, pacmanPosition.x, pacmanPosition.y, null);
                break;
            case 'l':
                canvas.drawBitmap(pacManLeft, pacmanPosition.x, pacmanPosition.y, null);
                break;
            case 'r':
                canvas.drawBitmap(pacManRight, pacmanPosition.x, pacmanPosition.y, null);
                break;

        }

        for (int i = 0; i < ghostPosition.size(); i++) {
            canvas.drawBitmap(ghost1, ghostPosition.get(i).x, ghostPosition.get(i).y, null);
        }
        for (int i = 0; i < laserPosition.size(); i++) {
            canvas.drawBitmap(laser, laserPosition.get(i).x, laserPosition.get(i).y, null);
        }
    }

                         // ****** PACMAN  ****** //

    // Allows the controller to set PacMan's location

    synchronized public void setPacManLocation(Point p) {
        pacmanPosition = p;
    }

    synchronized public Point getPacManLocation() {
        return pacmanPosition;
    }

    // Allows the controller to see how big PacMan is
    synchronized public int getPacManWidth() {
        return pacmanHitBox.width();
    }

    synchronized public int getPacManHeight() {
        return pacmanHitBox.height();
    }



                            // ****** GHOSTS ****** //

    synchronized public void addGhosts(Point p) {
        ghostPosition.add(p);
    }

    // Allows the controller to set the Ghost's location
    synchronized public void setGhostPosition(int i, Point p) {
        ghostPosition.set(i, p);
    }

    synchronized public Point getGhostPosition(int i) {
        return ghostPosition.get(i);
    }



                            // ****** LASER ****** //

    synchronized public void setLaserPosition(int i, Point p) {
        laserPosition.set(i, p);
    }

    synchronized public void addLasers(Point p) {
        laserPosition.add(p);
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

}