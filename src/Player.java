import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
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
    private double deltaAnimate = 0;

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
//        g2.drawImage(getImage(), getX(), getY(), getWidth(), getHeight(), null);
        drawImage(g);
    }

    public void drawImage(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Vector2 p = getPos();
        Vector2 size = getSize();
        Vector2 center = getCenter();
        BufferedImage img = getImage();
//        tr.scale(size.x, size.y);
        int rotation = switch (Utils.getDirection(getDirection())) {
            case 'n' -> 90;
            case 'e' -> 180;
            case 's' -> 270;
            case 'w' -> 0;
            default -> 0;
        };

        AffineTransform backup = g2.getTransform();
        AffineTransform a = AffineTransform.getRotateInstance(Math.toRadians(rotation), center.x, center.y);

        g2.setTransform(a);
        g2.drawImage(img, (int) p.x, (int) p.y, (int) size.x, (int) size.y, null);
        g2.setTransform(backup);
    }

    @Override
    public void update(double deltaT) {
        deltaAnimate += deltaT;
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

        if (deltaAnimate >= 5) {
            incrementFrameIndex();
            deltaAnimate = 0;
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
