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
public class TestView extends View {

    Bitmap background = null;
    Bitmap faceClosed = null;
    Bitmap faceShot = null;
    Bitmap ghost1 = null;
    Bitmap ghost2 = null;
    Bitmap ghost3 = null;
    Bitmap laser = null;


    Rect pacmanHitBox = new Rect(0, 0, 0, 0);
    Rect laserHitBox = new Rect(0, 0, 0, 0);
    ArrayList<Rect> ghostHitBox;

    char direction;

    Point pacmanPosition;
    ArrayList<Point> laserPosition;
    ArrayList<Point> ghostPosition;
    private boolean shooting;
    private int level;


    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pacmanPosition = new Point(50, 50);
        ghostPosition = new ArrayList<Point>();
        laserPosition = new ArrayList<Point>();
        level = 1;

        background = BitmapFactory.decodeResource(getResources(), R.drawable.ghosthuntermazepsdbackground);
        faceClosed = BitmapFactory.decodeResource(getResources(), R.drawable.faceclose);
        faceShot = BitmapFactory.decodeResource(getResources(), R.drawable.faceopen);
        ghost1 = BitmapFactory.decodeResource(getResources(), R.drawable.mainghostlevel1);
        laser = BitmapFactory.decodeResource(getResources(), R.drawable.gunlaser);

        ghost2 = BitmapFactory.decodeResource(getResources(), R.drawable.shaqface);
        ghost3 = BitmapFactory.decodeResource(getResources(), R.drawable.jamesface);

        pacmanHitBox = new Rect(pacmanPosition.x, pacmanPosition.y, 76, 103); // Replace 76 x 103 with size of sprite
        ghostHitBox = new ArrayList<Rect>();
    }

    @Override
    synchronized public void onDraw(Canvas canvas) {
        //create a blank canvas
        canvas.drawBitmap(background, null, new Rect(0, 0, 1280, 720), null);

        if (shooting) {
            canvas.drawBitmap(faceShot, pacmanPosition.x, pacmanPosition.y, null);
        } else {
            canvas.drawBitmap(faceClosed, pacmanPosition.x, pacmanPosition.y, null);
        }

        if (level == 1) {
            for (int i = 0; i < ghostPosition.size(); i++) {
                canvas.drawBitmap(ghost1, ghostPosition.get(i).x, ghostPosition.get(i).y, null);
            }
        }
        if (level == 2) {
            for (int i = 0; i < ghostPosition.size(); i++) {
                canvas.drawBitmap(ghost2, ghostPosition.get(i).x, ghostPosition.get(i).y, null);
            }
        }
        if (level == 3) {
            for (int i = 0; i < ghostPosition.size(); i++) {
                canvas.drawBitmap(ghost3, ghostPosition.get(i).x, ghostPosition.get(i).y, null);
            }
        }

        for (int i = 0; i < laserPosition.size(); i++) {
            canvas.drawBitmap(laser, laserPosition.get(i).x, laserPosition.get(i).y, null);
        }
    }

    // ****** PACMAN  ****** //

    // Allows the controller to set PacMan's location

    synchronized public void setPacManLocation(Point p) {
        pacmanPosition = p;
        pacmanHitBox = new Rect(p.x, p.y, 76, 103); // Replace 76 x 103 with size of sprite
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
        ghostHitBox.add(new Rect(p.x + 17, p.y + 13, 65, 65));
    }

    synchronized public void deleteGhosts(int i) {
        ghostPosition.remove(i);
        ghostHitBox.remove(i);
    }

    // Allows the controller to set the Ghost's location
    synchronized public void setGhostPosition(int i, Point p) {
        ghostPosition.set(i, p);
        ghostHitBox.set(i, new Rect(p.x + 17, p.y + 13, 65, 65));
    }

    synchronized public Point getGhostPosition(int i) {
        return ghostPosition.get(i);
    }


    // ****** LASER ****** //

    synchronized public void setLaserPosition(int i, Point p) {
        laserPosition.set(i, p);
        laserHitBox = new Rect(p.x, p.y, 7, 7);
    }

    synchronized public void addLasers(Point p) {
        laserPosition.add(p);
        laserHitBox = new Rect(p.x, p.y, 7, 7);
    }

    synchronized public void deleteLasers() {
        laserPosition.remove(0);
        laserHitBox = null;
    }

    synchronized public boolean checkForCollisionsWithHero() {
        if (ghostHitBox.size() > 0) {
            for (int i = 0; i < ghostHitBox.size(); i++) {
                if (pacmanHitBox.intersect(ghostHitBox.get(i))) {
                    Log.w("Dead", "You died!");
                    return true;
                    // END GAME??
                }
            }
        }
        return false;
    }

    synchronized public int checkForCollisionsWithGhost() {
        if (ghostHitBox.size() > 0) {
            for (int i = 0; i < ghostHitBox.size(); i++) {
                if (laserHitBox.intersect(ghostHitBox.get(i))) {
                    deleteGhosts(i);
                    deleteLasers();
                    return i;
                }
            }
        }
        return -1;
    }


    public void setLevel(int i) {
        level = i;
    }

    public void setDirection(char direction) {
        this.direction = direction;
    }

    public void setShooting(boolean b) {
        shooting = b;
    }

    public void collisionWithGhost() {
        // end Game
        Log.w("Collision", "END GAME");
    }
}