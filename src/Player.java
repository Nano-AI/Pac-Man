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
import java.util.LinkedList;
import java.util.Queue;

public class Player extends Entity {
    private double deltaAnimate = 0;
    private boolean blocked = false;

    public ArrayList<Vector2> visited;

    private BufferedImage[] rightFrames;
    private BufferedImage[] leftFrames;
    private BufferedImage[] upFrames;
    private BufferedImage[] downFrames;

    public boolean dead = false;
    private boolean angry = true;
    private double angryTimer = 0f;
    public final double TOTAL_ANGRY_TIME = 300f;
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
        mapChar = 'P';

        visited = new ArrayList<>();
        speed = 2.0;

        rightFrames = Utils.getImages("./img/pacman-right");
        leftFrames = Utils.getImages("./img/pacman-left");
        upFrames = Utils.getImages("./img/pacman-up");
        downFrames = Utils.getImages("./img/pacman-down");

        setDirection('e');
    }

    public boolean isAngry() {
        return this.angry;
    }

    public double getAngryTimer() {
        return this.angryTimer;
    }

    public void setAngry() {
        this.angryTimer = 0;
        this.angry = true;
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
        // draw the spots the pacman has been in
//        for (Vector2 v : visited) {
//            g.setColor(Color.GREEN);
//            Vector2 p = m.getPoint((int) v.x, (int) v.y);
//            g.fillRect(
//                    (int) p.x, (int) p.y, m.pixelPerHorizontalGrid, m.pixelPerVerticalGrid
//            );
//        }
        drawImage(g);
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

        BufferedImage[] frames = switch (Utils.getDirection(getDirection())) {
            case 'n' -> upFrames;
            case 's' -> downFrames;
            case 'w' -> leftFrames;
            default -> rightFrames;
        };

        setImages(frames);

        g2.drawImage(getImage(), (int) p.x, (int) p.y, (int) size.x, (int) size.y, null);
    }
    
    private void addPath() {
        int last = visited.size() - 1;
        if (visited.isEmpty() || visited.get(last) == null) {
            if (getGridPos() != null)
                visited.add(getGridPos().gridPos);
            return;
        }
        if (visited.size() > 20) {
            visited.remove(last);
        }
        if (!getGridPos().gridPos.equals(visited.get(0))) {
            visited.add(0, getGridPos().gridPos);
        }
    }

    public ArrayList<Vector2> getPath() {
        return visited;
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
        // add the history of path to pacman
        // unused for now
//        if (getGridPos() != null) {
//            addPath();
//        }
        // set the deltaT
//        System.out.println(angryTimer);
        if (angry) {
            angryTimer += deltaT;
            if (TOTAL_ANGRY_TIME <= angryTimer) {
                angry = false;
                angryTimer = 0;
                System.out.println("not angry!");
            }
        }
        deltaAnimate += deltaT;

        moveInDirection(deltaT);

        // animate every 5 (forgot units ms), and make sure it's not blocekd and moving
        if (deltaAnimate >= 5 && !blocked) {
            incrementFrameIndex();
            getAudioPlayer().playSound("gs_siren_soft");
            deltaAnimate = 0;
        }

        // update the grid spot at which the player is currently at
        updateGridSpot();
    }

    public void gameOver() {

    }

    public void playDead() {
    }
}
