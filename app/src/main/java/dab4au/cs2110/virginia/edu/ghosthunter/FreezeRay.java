package dab4au.cs2110.virginia.edu.ghosthunter;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by thinkdavid on 4/20/15.
 */
public class FreezeRay {

    private Point position;
    private Rect hitbox;

    public FreezeRay() {
        Random r = new Random();

        // change 700 to the size of the screen once I find that out
        int x = 100 + r.nextInt(1000);
        int y = 100 + r.nextInt(400);
        position = new Point(x, y);

        hitbox = new Rect(position.x, position.y - 16, position.x + 17, position.y + 16);
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


}
