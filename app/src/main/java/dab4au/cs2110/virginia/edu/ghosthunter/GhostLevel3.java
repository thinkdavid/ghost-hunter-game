package dab4au.cs2110.virginia.edu.ghosthunter;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by thinkdavid on 4/15/15.
 */
public class GhostLevel3 extends Ghost {

    public GhostLevel3(David pm) {
        super(pm);
        health = 3;
        speed = 3;

    }

    @Override
    public Point getPosition() {
        return super.getPosition();
    }

    @Override
    public void setPosition(Point position) {
        super.setPosition(position);
    }

    @Override
    public Point getFuturePosition() {
        return super.getFuturePosition();
    }

    @Override
    public void setFuturePosition(Point futurePosition) {
        super.setFuturePosition(futurePosition);
    }

    @Override
    public void move() {
        super.move();
    }

    @Override
    public Rect getHitbox() {
        return super.getHitbox();
    }

    @Override
    public boolean isColliding(Rect rect) {
        return super.isColliding(rect);
    }

    @Override
    public boolean isSouth() {
        return super.isSouth();
    }

    @Override
    public boolean isEast() {
        return super.isEast();
    }

    @Override
    public boolean isWest() {
        return super.isWest();
    }

    @Override
    public boolean isNorth() {
        return super.isNorth();
    }

    @Override
    public int getHealth() {
        return super.getHealth();
    }

    @Override
    public void setHealth(int health) {
        super.setHealth(health);
    }
}
