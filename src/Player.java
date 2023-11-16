import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;

public class Player extends Entity {
    private Map m;
    private Vector2 nextPos;

    private ArrayList<Entity> collisions;
    private ArrayList<Entity> walls;
    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        walls = new ArrayList<>();
        collisions = new ArrayList<>();
        setDirection('0');
    }

    public void setMap(Map m) {
        this.m = m;
    }

    public void setWalls(ArrayList<Entity> walls) {
        this.walls = walls;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.YELLOW);
        g.fillOval(getX(), getY(), getWidth(), getHeight());

        for (Entity c : collisions) {
            g.setColor(Color.RED);
            g.drawLine(getX() + (getWidth() / 2), getY() + (getHeight() / 2), c.getX() + (c.getWidth() / 2), c.getY() + (c.getHeight() / 2));
        }

        g.drawRect(getX(), getY(), 10, 10);
    }

    @Override
    public void update(double deltaT) {
        Vector2 check = getPos().copy().add(getDirection().copy().multiply(0.8 * deltaT));
        Entity c = new Entity((int) check.x, (int) check.y, getWidth(), getHeight());
        c.hitbox = hitbox.copy();

        this.collisions.clear();
        boolean blocked = false;

        for (Entity wall : walls) {
            if (c.isIn(wall) && c.isTouching(wall)) {
                blocked = true;
                break;
            }
        }

        if (!blocked) {
            setPos(check);
        } else {
            nextPos = getPos();
        }
    }

    public char[] getPossibleDirections() {
        char[] pos = Utils.cardinalDirections;

        int i = 0;
        for (Entity wall : collisions) {

        }
        return pos;
    }
}
