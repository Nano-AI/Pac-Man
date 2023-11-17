import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;

public class Player extends Entity {
    private Vector2 prevDirection;
    private Vector2 wantedDirection;
    private Map m;
    private Vector2 nextPos;

    private ArrayList<Entity> collisions;
    private ArrayList<Entity> walls;
    private Vector2 nextGridPos;

    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        walls = new ArrayList<>();
        collisions = new ArrayList<>();
        setDirection('0');
        wantedDirection = getDirection();
        prevDirection = getDirection();
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
//         TODO: update it so we keep track of possible moves through the grid instead of using hitboxes and collisions
        boolean newPath = nextIsBlocked(wantedDirection, deltaT);
        boolean samePath = nextIsBlocked(getDirection(), deltaT);
        if (!newPath) {
            Vector2 temp = getDirection();
            goTowards(wantedDirection, deltaT);
            if (!temp.equals(getDirection())) {
                prevDirection = temp;
            }
        } else if (!samePath) {
            Vector2 temp = getDirection();
            goTowards(getDirection(), deltaT);
            if (!temp.equals(getDirection())) {
                prevDirection = temp;
            }
        }
    }

    private void goTowards(Vector2 direction, double deltaT) {
        prevDirection = getDirection().copy();
        setDirection(direction);
        setPos(getNextPos(1.8, direction, deltaT));
    }

    private boolean nextIsBlocked(Vector2 direction, double deltaT) {
        this.collisions.clear();
        // I HAVE NO CLUE WHY GETTING SIZE AND DIVIDING BY 16 WORKS, BUT HEY, IT DOES!
        Entity c = getNextPosEntity((int) (getSize().x / 16), direction, 1);
        boolean blocked = false;

        for (Entity wall : walls) {
            if (c.isIn(wall)) {
                this.collisions.add(wall);
                blocked = true;
                break;
            }
        }

        return blocked;
    }

    private Entity getNextPosEntity(double distance, Vector2 direction, double deltaT) {
        Vector2 check = getNextPos(distance, direction, deltaT);
        Entity c = new Entity((int) check.x, (int) check.y, getWidth(), getHeight());
        c.hitbox = hitbox.copy();
        return c;
    }

    private Vector2 getNextPos(double distance, Vector2 direction, double deltaT) {
        return getPos().copy().add(direction.copy().multiply(distance * deltaT));
    }

    public void setWantedDirection(char direction) {
        wantedDirection = Utils.getDirection(direction);
    }
}
