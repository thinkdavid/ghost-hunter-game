package dab4au.cs2110.virginia.edu.ghosthunter;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Created by thinkdavid on 4/20/15.
 */
public class Coin {

    private Point position;
    private Rect hitbox;
    private int points;

    public Coin(Ghost g) {
        int x = g.getPosition().x + 50;
        int y = g.getPosition().y + 55;
        position = new Point (x,y);
        hitbox = new Rect(position.x + 7, position.y + 3, position.x + 33, position.y + 26);
        points = 10;
    }

    public boolean isColliding(Rect rect) {
        return hitbox.intersect(rect);
    }

    public Rect getHitbox() {
        return hitbox;
    }

    public Point getPosition() {
        return position;
    }

    public int getPoints() {
        return points;
    }
}
