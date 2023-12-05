/**
 * The Player class represents a player entity in the game.
 * It extends the Entity class and includes methods for player movement and collision detection.
 *
 * @author Aditya B, Ekam S
 * @version 4 December, 2023
 */
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.sql.Array;
import java.util.ArrayList;

public class Player extends Entity {
    private Vector2 prevDirection;
    private Vector2 wantedDirection;
    private Vector2 nextPos;

    private ArrayList<Entity> collisions;
    private ArrayList<Entity> walls;
    private Vector2 nextGridPos;
    private double deltaAnimate = 0;
    private boolean blocked = false;

    /**
     * Constructor for the Player class with specified parameters.
     *
     * @param x      The x-coordinate of the player's initial position.
     * @param y      The y-coordinate of the player's initial position.
     * @param width  The width of the player entity.
     * @param height The height of the player entity.
     */
    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        walls = new ArrayList<>();
        collisions = new ArrayList<>();
        setDirection('0');
        wantedDirection = getDirection();
        prevDirection = getDirection();
    }

    /**
     * Set the map for the player to interact with.
     *
     * @param m The map to be set.
     */
    public void setMap(Map m) {
        this.m = m;
    }

    /**
     * Set the walls that the player should consider for collision detection.
     *
     * @param walls The ArrayList of walls to be set.
     */
    public void setWalls(ArrayList<Entity> walls) {
        this.walls = walls;
    }

    /**
     * Override of the draw method to render the player on the screen.
     *
     * @param g The Graphics object used for drawing.
     */
    @Override
    public void draw(Graphics g) {
        drawImage(g);
        g.fillRect((int) getGridPos().x, (int) getGridPos().y, m.pixelPerHorizontalGrid, m.pixelPerHorizontalGrid);
    }

    /**
     * Draw the player image on the screen, considering rotation based on direction.
     *
     * @param g The Graphics object used for drawing.
     */
    public void drawImage(Graphics g) {
        // get all info needed to draw
        Graphics2D g2 = (Graphics2D) g;
        Vector2 p = getPos();
        Vector2 size = getSize();
        Vector2 center = getCenter();
        BufferedImage img = getImage();
        // get the direction of the entity
        int rotation = switch (Utils.getDirection(getDirection())) {
            case 'n' -> 90;
            case 'e' -> 180;
            case 's' -> 270;
            case 'w' -> 0;
            default -> 0;
        };

        // store the previous rotation
        AffineTransform backup = g2.getTransform();
        // set the rotation to those set degrees around the origin of center
        AffineTransform a = AffineTransform.getRotateInstance(Math.toRadians(rotation), center.x, center.y);

        // set the transform and draw image
        g2.setTransform(a);
        g2.drawImage(img, (int) p.x, (int) p.y, (int) size.x, (int) size.y, null);
        // reset transform
        g2.setTransform(backup);
    }

    /**
     * Override of the update method to handle player movement and animation.
     * This also makes sure that the player doesn't stop and continues on the path of movement
     * until the desired direction is travel-able.
     *
     * @param deltaT The time elapsed since the last update.
     */
    @Override
    public void update(double deltaT) {
        // set the deltaT
        deltaAnimate += deltaT;

        // check if the wanted direction's path is blocked
        boolean newPath = nextIsBlocked(wantedDirection, deltaT);
        // check if the current direction's path is blocked
        boolean samePath = nextIsBlocked(getDirection(), deltaT);
        blocked = true;

        // if the new path isn't blocked, go the direction
        if (!newPath) {
            Vector2 temp = getDirection();
            // go towards the wanted direction
            goTowards(wantedDirection, deltaT);
            // set the previous direction if it isn't the same as the one we're pressing down
            if (!temp.equals(getDirection())) {
                prevDirection = temp;
            }
            blocked = false;
        } else if (!samePath) {
            // otherwise if the same path isn't blocked but the new one is
            Vector2 temp = getDirection();
            // go towards the current direction
            goTowards(getDirection(), deltaT);
            // and set prev direction if it isn't the same as pressing down
            if (!temp.equals(getDirection())) {
                prevDirection = temp;
            }
            blocked = false;
        }

        // animate every 5 (forgot units), and make sure it's not blocekd and moving
        if (deltaAnimate >= 5 && !blocked) {
            incrementFrameIndex();
            deltaAnimate = 0;
        }

        // update the grid spot at which the player is currently at
        updateGridSpot();
    }

    /**
     * Move the player towards a specified direction.
     *
     * @param direction The direction vector to move towards.
     * @param deltaT    The time elapsed since the last update.
     */
    private void goTowards(Vector2 direction, double deltaT) {
        prevDirection = getDirection().copy();
        setDirection(direction);
        setPos(getNextPos(1.8, direction, deltaT));
    }

    /**
     * Check if the next position is blocked by collisions.
     *
     * @param direction The direction vector to check.
     * @param deltaT    The time elapsed since the last update.
     * @return True if the next position is blocked, false otherwise.
     */
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

    /**
     * Get the entity at the next position based on distance and direction.
     *
     * @param distance The distance to the next position.
     * @param direction The direction vector.
     * @param deltaT    The time elapsed since the last update.
     * @return The entity at the next position.
     */
    private Entity getNextPosEntity(double distance, Vector2 direction, double deltaT) {
        Vector2 check = getNextPos(distance, direction, deltaT);
        Entity c = new Entity((int) check.x, (int) check.y, getWidth(), getHeight());
        c.hitbox = hitbox.copy();
        return c;
    }

    /**
     * Get the next position based on distance and direction.
     *
     * @param distance The distance to the next position.
     * @param direction The direction vector.
     * @param deltaT    The time elapsed since the last update.
     * @return The next position vector.
     */
    private Vector2 getNextPos(double distance, Vector2 direction, double deltaT) {
        return getPos().copy().add(direction.copy().multiply(distance * deltaT));
    }

    /**
     * Set the wanted direction based on a character input.
     *
     * @param direction The character representing the new direction.
     */
    public void setWantedDirection(char direction) {
        wantedDirection = Utils.getDirection(direction);
    }
}
