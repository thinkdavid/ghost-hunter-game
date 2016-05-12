package dab4au.cs2110.virginia.edu.ghosthunter;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by thinkdavid on 4/12/15.
 */
public class Ghost {
    private Point position;
    private Point futurePosition;
    int speed;
    private David pacman;
    private Rect hitbox;
    private boolean east;
    private boolean west;
    private boolean north;
    private boolean south;
    int health;

    public Ghost(David pm) {
        Random r = new Random();
        position = new Point(-1, -1);

        int x = r.nextInt(1200);
        int y = r.nextInt(600);

        while (position.equals(-1, -1)) {
            if (distance(pm.getPosition(), new Point(x, y)) > 200) {
                position = new Point(x, y);
            } else {
                x = 100 + r.nextInt(1200);
                y = 100 + r.nextInt(400);
            }
        }

        this.pacman = pm;
        futurePosition = pacman.getPosition();
        speed = 1;
        health = 1;

        north = west = east = south = false;

        hitbox = new Rect(position.x + 17, position.y + 10, position.x + 82, position.y + 100);
    }

    public Ghost(David pm, int level) {
        Random r = new Random();
        position = new Point(-1, -1);

        int x = r.nextInt(1200);
        int y = r.nextInt(600);

        while (position.equals(-1, -1)) {
            if (distance(pm.getPosition(), new Point(x, y)) > 100) {
                position = new Point(x, y);
            } else {
                x = 100 + r.nextInt(1200);
                y = 100 + r.nextInt(400);
            }
        }

        this.pacman = pm;
        futurePosition = pacman.getPosition();
        speed = level;
        health = level;

        north = west = east = south = false;

        hitbox = new Rect(position.x + 17, position.y + 10, position.x + 82, position.y + 100);
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getFuturePosition() {
        return futurePosition;
    }

    public void setFuturePosition(Point futurePosition) {
        this.futurePosition = futurePosition;
    }

    public void move() {
        int futureX = pacman.getPosition().x;
        int futureY = pacman.getPosition().y;
        int x = position.x;
        int y = position.y;

        if (futureX > x) {
            x = x + speed;
            east = true;
            west = false;
        } else if (futureX < x) {
            x = x - speed;
            east = false;
            west = true;
        }
        if (futureY > y) {
            y = y + speed;
            north = true;
            south = false;
        } else if (futureY < y) {
            y = y - speed;
            north = false;
            south = true;
        }
        if (futureX == x) {
            if (futureY > y) {
                y = y + speed;
                east = false;
                west = false;
                south = false;
                north = true;
            } else y = y - speed;
            east = false;
            west = false;
            south = true;
            north = false;
        }
        if (futureY == y) {
            if (futureX > x) {
                x = x + speed;
                east = true;
                west = false;
                south = false;
                north = false;
            } else x = x - speed;
            east = false;
            west = true;
            south = false;
            north = false;
        }
        position = new Point((int) x, (int) y);
        hitbox = new Rect(position.x + 17, position.y + 10, position.x + 82, position.y + 100);
    }

    public Rect getHitbox() {
        return hitbox;
    }

    public boolean isColliding(Rect rect) {
        return hitbox.intersect(rect);
    }

    public boolean isSouth() {
        return south;
    }

    public boolean isEast() {
        return east;
    }

    public boolean isWest() {
        return west;
    }

    public boolean isNorth() {
        return north;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    @Override
    public String toString() {
        return "Ghost Health " + health + "\nGhost Position: " + position.toString() + "\nGhost Speed: " + speed;
    }
}
