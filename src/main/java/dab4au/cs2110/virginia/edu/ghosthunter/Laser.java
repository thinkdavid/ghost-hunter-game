package dab4au.cs2110.virginia.edu.ghosthunter;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by thinkdavid on 4/13/15.
 */
public class Laser {

    private PacMan pacman;
    private Point position;
    private boolean up, down, left, right;
    private int speed;
    private Rect hitbox = new Rect(0, 0, 0, 0);

    public Laser(PacMan pm) {
        this.pacman = pm;
        Point pos = this.pacman.getPosition();
        position = new Point(pos.x + 50, pos.y + 80);
        speed = 20;
        hitbox = new Rect(position.x, position.y-16, position.x + 17, position.y + 16);

        if (pacman.isUp()) {
            up = true;
            down = false;
            left = false;
            right = false;
        }

        if(pacman.isDown()) {
            up = false;
            down = true;
            left = false;
            right = false;
        }

        if (pacman.isLeft()) {
            up = false;
            down = false;
            left = true;
            right = false;
        }

        if (pacman.isRight()) {
            up = false;
            down = false;
            left = false;
            right = true;
        }
    }

    public void shoot() {

        if (up) {
            position = new Point(position.x, position.y - speed);
            hitbox = new Rect(position.x, position.y-16, position.x + 17, position.y + 16);
        } else if (down) {
            position = new Point(position.x, position.y + speed);
            hitbox = new Rect(position.x, position.y-16, position.x + 17, position.y + 16);
        } else if (left) {
            position = new Point(position.x - speed, position.y);
            hitbox = new Rect(position.x, position.y-16, position.x + 17, position.y + 16);
        } else if (right) {
            position = new Point(position.x + speed, position.y);
            hitbox = new Rect(position.x, position.y-16, position.x + 17, position.y + 16);
        }

    }

    public boolean isColliding (Rect r) {
        return hitbox.intersect(r);
    }

    public int getSpeed() {
        return speed;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Position: " + position.toString();
    }


    public Rect getHitbox() {
        return hitbox;
    }
}
