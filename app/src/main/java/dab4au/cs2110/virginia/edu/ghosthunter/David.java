package dab4au.cs2110.virginia.edu.ghosthunter;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;

/**
 * Created by thinkdavid on 4/5/15.
 */

public class David {

    private Point position;
    private int speed;
    private boolean up, down, left, right;

    public Rect getHitbox() {
        return hitbox;
    }

    private Rect hitbox;

    public David() {
        position = new Point(50, 50);
        speed = 6;
        up = down = left = false;
        right = true;
        hitbox = new Rect(position.x + 15, position.y, position.x + 88, position.y + 100); // Replace 76 x 103 with size of sprite
    }

    // Movement Methods
    public void moveUp() {
        position = new Point(position.x, position.y - speed);
        hitbox = new Rect(position.x + 15, position.y, position.x + 88, position.y + 100);
        up = true;
        down = false;
        left = false;
        right = false;
    }

    public void moveDown() {
        position = new Point(position.x, position.y + speed);
        hitbox = new Rect(position.x + 15, position.y, position.x + 88, position.y + 100);
        up = false;
        down = true;
        left = false;
        right = false;
    }

    public void moveLeft() {
        position = new Point(position.x - speed, position.y);
        hitbox = new Rect(position.x + 15, position.y, position.x + 88, position.y + 100);
        up = false;
        down = false;
        left = true;
        right = false;
    }

    public void moveRight() {
        position = new Point(position.x + speed, position.y);
        hitbox = new Rect(position.x + 15, position.y, position.x + 88, position.y + 100);
        up = false;
        down = false;
        left = false;
        right = true;
    }

    public boolean isColliding(Rect r) {
        return hitbox.intersect(r);
    }

    public int getX() {
        return position.x;
    }

    public int getY() {
        return position.y;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(int x, int y) {
        position = new Point(x, y);
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public char getDirection() {

        if (up) {
            return 'u';
        } else if (down) {
            return 'd';
        } else if (left) {
            return 'l';
        } else
            return 'r';


    }

}