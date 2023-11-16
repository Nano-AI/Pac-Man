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
    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        walls = new ArrayList<>();
        collisions = new ArrayList<>();
        setDirection('0');
        wantedDirection = getDirection();
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
        // TODO: update it so we keep track of possible moves through the grid instead of using hitboxes and collisions
        boolean newPath = nextIsBlocked(wantedDirection, deltaT);
        boolean samePath = nextIsBlocked(getDirection(), deltaT);
        if (!newPath) {
            System.out.println("Wanted direction is NOT blocked!" + wantedDirection);
            goTowards(wantedDirection, deltaT);
        } else if (!samePath) {
            System.out.println("Current direction is NOT blocked!");
            goTowards(getDirection(), deltaT);
        }
    }

    private void goTowards(Vector2 direction, double deltaT) {
        prevDirection = getDirection().copy();
        setDirection(direction);
        System.out.println(getNextPos(direction, deltaT));
        setPos(getNextPos(direction, deltaT));
    }

    private boolean nextIsBlocked(Vector2 direction, double deltaT) {
        this.collisions.clear();
        Entity c = getNextPosEntity(direction, deltaT);
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

    private Entity getNextPosEntity(Vector2 direction, double deltaT) {
        Vector2 check = getNextPos(direction, deltaT);
        Entity c = new Entity((int) check.x, (int) check.y, getWidth(), getHeight());
        c.hitbox = hitbox.copy();
        return c;
    }

    private Vector2 getNextPos(Vector2 direction, double deltaT) {
        return getPos().copy().add(direction.copy().multiply(1.5 * deltaT));
    }

    public void setWantedDirection(char direction) {
        wantedDirection = Utils.getDirection(direction);
    }
}
