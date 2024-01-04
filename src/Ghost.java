/**
 * The ghost class that is the enemy to the pacman
 *
 * @author Aditya B, Ekam S
 * @version 4 December, 2023
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Ghost extends Entity {
    // next position
    private Vector2 nextPos;
    // ref of player + map
    public Player player;
    public Map map;
    // detection radius; prollt not gonna be used
    double detectionRadius;
    // name of the thing on the map tmeplate
    public char name = 'r';
    // idk what this is
    private double pathUpdate = 0;
    // a lit of vectors telling it how to get to the path of scattering
    private ArrayList<Vector2> scatterPath;
    // the queue telling it where to go at what point of time
    Queue<Vector2> supposedPath;

    // render update time or smth idk
    private double totalScatterTime = 0;
    // path to player
    ArrayList<Vector2> findPath;
    // chase speed of ghost
    private double chaseSpeed;
    // base speed of ghost
    private double baseSpeed;
    // is ghost in scatter mode? aka are they running circles
    private boolean scatterMode = false;
    // the sorted path the ghost is supposed to be taking when going around in circles

    private LList<Vector2> sortedScatterPath;

    // path find to player or some other stuff
    private PathFinder finder;
    private double randomT = 0;

    private BufferedImage[] blueGhost;
    private BufferedImage[] normalGhost;
    private BufferedImage[] eatenGhost;
    private boolean scared = false;
    private double flickerTimer = 0f;
    private double MAX_SCATTER_TIME;

    private boolean resetting = false;

    public void setMode(Mode mode) {
        if (this.mode != mode) {
            supposedPath.clear();
            this.mode = mode;
        }

        if (mode == Mode.EATEN) {
            setImages(eatenGhost);
            scared = false;
        } else if (mode == Mode.SCARED) {
            setImages(blueGhost);
            scared = true;
        } else {
            setImages(normalGhost);
            scared = false;
        }
    }

    public enum Mode {
        BASE,
        EATEN,
        SCARED
    }

    private Mode mode = Mode.BASE;
    private Vector2 spawn;

    /**
     * Default constructor for the ghost
     * @param name Name of the ghost.
     * @param x X position of the ghost.
     * @param y Y position of the ghost.
     * @param gridX The grid-x position of the ghost.
     * @param gridY The grid-y position of the ghost;
     * @param width The width of the ghost.
     * @param height The height of the ghost.
     */
    public Ghost(String name, int x, int y, int gridX, int gridY, int width, int height) {
        // super method
        super(x, y, gridX, gridY, width, height);
        MAX_SCATTER_TIME = 750 + Math.random() * 200;
        // setup the images
        normalGhost = Utils.getImages("./img/ghosts/" + name);
        // set the map char thing
        mapChar = 'p';
        // set detec ction radius
        this.detectionRadius = 120f;
        // setup the supposed path
        supposedPath = new LinkedList<>();
        // more vars
        blocked = true;
        baseSpeed = 1.33;
        speed = baseSpeed;
//        chaseSpeed = 1.1 * speed;
        chaseSpeed = 1.5;
        System.out.println(gridX + " " + gridY);
        spawn = new Vector2(gridX, gridY);

        setImages(normalGhost);
    }

    public void setBlueGhost(BufferedImage[] images) {
        this.blueGhost = images;
    }
    public void setEatenGhost(BufferedImage[] images) {
        this.eatenGhost = images;
    }

    /**
     * Sets up the scatter path that the ghost is supposed to take on the map. AKA what is it gonna go circles around.
     */
    public void setupScatterPath() {
        // iterate and find the vectors of each square
        scatterPath = new ArrayList<>();
        char c = Character.toLowerCase(mapChar);
        for (int i = 0; i < map.height; i++) {
            for (int j = 0; j < map.width; j++) {
                if (map.at(i, j) == c) {
                    scatterPath.add(new Vector2(i, j));
                }
            }
        }

        // create a linked list for this
        sortedScatterPath = new LList<Vector2>();
        sortedScatterPath.head = new Node<>(scatterPath.get(0).copy());
        // remove the null head node
        scatterPath.remove(0);
        // the head
        Node<Vector2> head = sortedScatterPath.head;

        // iterate through the whole scatter path
        int i = 0;
        while (i < scatterPath.size()) {
            // if the current grid is one unit away from the head
            if (scatterPath.get(i).distanceTo(head.val).getMagnitude() == 1f) {
                // add it to the array list
                head.next = new Node<>(scatterPath.get(i));
                head = head.next;
                // remove it
                scatterPath.remove(i);
                // re-uterate through the array
                i = 0;
            } else {
                i++;
            }
        }
    }

    @Override
    public void update(double deltaT) {
        if (player.isAngry()) {
            flickerTimer += deltaT;
        }

        if (player.getAngryTimer() >= player.TOTAL_ANGRY_TIME) {
//            mode = Mode.BASE;
            setMode(Mode.BASE);
            scared = false;
        }

        if (Utils.withinRange(player.getAngryTimer(), player.TOTAL_ANGRY_TIME, 0.7) && !resetting && mode == Mode.SCARED) {
            if (flickerTimer >= 10) {
                setImages(
                        scared ? blueGhost : normalGhost
                );
                scared = !scared;
                flickerTimer = 0;
            }
        } else {
            if (resetting) {
                setMode(Mode.EATEN);
            } else if (mode == Mode.SCARED) {
                setMode(Mode.SCARED);
            } else {
                setMode(Mode.BASE);
            }
        }

        if (getGridPos() != null && isTouching(player) && getGridPos().gridPos.equals(player.getGridPos().gridPos)) {
            if (player.isAngry() && mode == Mode.SCARED) {
                if (!resetting) {
                    scared = false;
                    resetting = true;
                    supposedPath.clear();
                    mode = Mode.EATEN;
                    getAudioPlayer().playSound("gs_eatghost");
                }
            } else if (mode == Mode.BASE) {
                player.dead = true;
            } else if (mode == Mode.SCARED) {
                getAudioPlayer().playSound("gs_returnghost");
            }
        }

        randomT += deltaT;
        updateGridSpot();
        move(deltaT);
        pathUpdate += deltaT;
    }

    private void moveReset(double deltaT) {
        if (getGridPos().gridPos.equals(spawn)) {
            resetting = false;
            supposedPath.clear();
            return;
        }
        if (supposedPath == null || supposedPath.isEmpty()) {
            supposedPath = finder.findPath(getGridPos().gridPos, spawn);
        }

        if (supposedPath.isEmpty()) {
            moveRandom(deltaT);
            return;
        }

        Vector2 v = supposedPath.peek();
        if (v == null) return;
        if (getGridPos().gridPos.equals(v)) {
            // make sure to loop throuhg it and make sure that it goes back to the head
            // after it reaches the end
            v = supposedPath.remove();
        }

        // go towards the grid pos by finding the distance between the current and next,
        // normalizing it, then setting the direction
        if (v == null) return;
        Vector2 goTo = getGridPos().gridPos.distanceTo(v).normalize();
        setWantedDirection(goTo.swap().multiply(-1));
        moveInDirection(deltaT);
    }

    // TODO: fix the scatter movement; it doesn't work
    public void move(double deltaT) {
        if (resetting) {
            moveReset(deltaT);
            return;
        }
        if (!canSee(player) && totalScatterTime < MAX_SCATTER_TIME) {
            moveScatter(deltaT);
            return;
        }

        if (mode == Mode.SCARED) {
            moveScared(deltaT);
            return;
        }

        switch (name) {
            case 'b':
                moveChase(deltaT);
                break;
            case 'i':
                moveInfront(deltaT);
                break;
            case 'c':
                moveProximity(deltaT);
                break;
            default:
                break;
        }
//        switch (name) {
//            case 'b' -> moveChase(deltaT);
//            case 'i' -> moveInfront(deltaT);
//            case 'c' -> moveProximity(deltaT);
//            default -> moveRandom(deltaT);
//        }
    }

    // TODO: write better code for when the ghost is scared
    private void moveScared(double deltaT) {
        moveRandom(deltaT);
//        ArrayList<Vector2> pos = map.getOpenNeighbors(getGridPos().gridPos);
//        Vector2 currentDir = getDirection();
//        Vector2 dir = pos.get(0).swap();
//        Vector2 toPlayer = player.getGridPos().gridPos.distanceTo(getGridPos().gridPos)
//                .normalize()
//                .swap();
//        int i;
//        for (i = 0; i < pos.size(); i++) {
//            Vector2 sp = pos.get(i).swap();
//            if (sp.multiply(-1).equals(currentDir)) {
//                pos.remove(i);
//                i--;
//            }
//            else if (sp.x == toPlayer.x || sp.y == toPlayer.y) {
//                pos.remove(i);
//                i--;
//            }
//        }
//        if (!pos.isEmpty()) dir = pos.get((int) (Math.random() * pos.size())).swap();
////        else dir = Utils.getDirections()[((int) (Math.random() * Utils.getDirections().length))];
//
//        setWantedDirection(dir);
//        moveInDirection(deltaT);
    }

    /**
     * Move to the scatter location
     * @param deltaT The deltat
     */
    private void moveScatter(double deltaT) {
        // if there is no intialized path to go down
        if (supposedPath.isEmpty()) {
            // if we are not going in circles
            if (!scatterMode) {
                // find a path to the cirlce area
                supposedPath = finder.findPath(this.getGridPos().gridPos, sortedScatterPath.head.val);
                // setup the iteration
                sortedScatterPath.iterate = sortedScatterPath.head;
                // set to scatter mode
                scatterMode = true;
            } else {
                totalScatterTime += deltaT;
                // check if we are on the same grid pos so we can update it
                if (getGridPos().gridPos.equals(sortedScatterPath.iterate.val)) {
                    // make sure to loop throuhg it and make sure that it goes back to the head
                    // after it reaches the end
                    if (sortedScatterPath.iterate.next != null) {
                        sortedScatterPath.iterate = sortedScatterPath.iterate.next;
                    } else {
                        sortedScatterPath.iterate = sortedScatterPath.head;
                    }
                }
                // go towards the grid pos by finding the distance between the current and next,
                // normalizing it, then setting the direction
                Vector2 goTo = getGridPos().gridPos.distanceTo(sortedScatterPath.iterate.val).normalize();
                setWantedDirection(goTo.swap().multiply(-1));
                moveInDirection(deltaT);
            }
        } else {
            totalScatterTime += deltaT;
            // move through the scatter path
            Vector2 nextPos = supposedPath.peek();
            // same thing, check if we're same grid pos
            // so we can update
            if (nextPos.equals(getGridPos().gridPos)) {
                nextPos = supposedPath.remove();
            }
            // just remove if we're already on it
            if (Utils.inRange(getPos().distanceTo(nextPos).getMagnitude(), 0, map.pixelPerHorizontalGrid / 2)) {
                supposedPath.remove();
            } else {
                // go towards the grid pos by finding the distance between the current and next,
                // normalizing it, then setting the direction
                Vector2 goTo = getGridPos().gridPos.distanceTo(nextPos).normalize();
                // pixel coordinates: (x, y) and go left to right, then top to bottom increments
                // grid coordinates: (y, x) and go up to down, then left to right
                // so in order to go in the correct direction we have to swap and mulitply by -1
                setWantedDirection(
                        goTo.swap().multiply(-1)
                );
                moveInDirection(deltaT);
            }
        }
    }

    private void moveProximity(double deltaT) {
        if (getDistanceTo(player) < 100f) {
            moveChase(deltaT);
        } else if (getDistanceTo(player) < 200f) {
            moveInfront(deltaT);
        } else {
            moveRandom(deltaT);
        }
    }

    /**
     * Chasing the player
     * @param deltaT
     */
    private void moveChase(double deltaT) {
        boolean see = canSee(player);
        if (see) {
            speed = chaseSpeed;
            scatterMode = false;
            supposedPath.clear();
        } else {
            speed = baseSpeed;
            scatterMode = true;
        }

        if (supposedPath.size() <= 1) {
            supposedPath = finder.findPath(this.getGridPos().gridPos, player.getGridPos().gridPos);
            if (supposedPath == null) return;
            if (supposedPath.size() <= 1) {
                moveRandom(deltaT);
                return;
            }
            if (getGridPos().gridPos.equals(supposedPath.peek())) {
                supposedPath.poll();
            }
            if (supposedPath.isEmpty()) {
                return;
            }
        }
        if (getGridPos().gridPos.equals(supposedPath.peek())) {
            supposedPath.poll();
        }
        // go towards the grid pos by finding the distance between the current and next,
        // normalizing it, then setting the direction
        Vector2 goTo = getGridPos().gridPos.distanceTo(supposedPath.peek()).normalize();
        if (Math.abs(goTo.x) == Math.abs(goTo.y)) {
            supposedPath.clear();
            return;
        }
        // pixel coordinates: (x, y) and go left to right, then top to bottom increments
        // grid coordinates: (y, x) and go up to down, then left to right
        // so in order to go in the correct direction we have to swap and mulitply by -1
        setWantedDirection(
                goTo.swap().multiply(-1)
        );
        moveInDirection(deltaT);
    }

    /**
     * Can the ghost see the entity?
     * @param e The entity the ghost is seeing or not.
     * @return True if ghost can see entity, false if it cannot.
     */
    private boolean canSee(Entity e) {
        // iterate through sightline and whether or not it's seeing it
        for (Vector2 v : getSightLine()) {
            if (e.getGridPos().gridPos.equals(v)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the sightline of the ghost
     * @return The sight line of it as a list of vector2s
     */
    private ArrayList<Vector2> getSightLine() {
        ArrayList<Vector2> sight = new ArrayList<>();

        // get x values to the right
        // iterate through up, down, left, and right and add them
        for (int x = -1; x <= 1; x+= 1) {
            for (int y = -1; y <= 1; y += 1) {
                // no center pos
                if (x == 0 && y == 0) continue;
                Vector2 g = getGridPos().gridPos.copy();
                while (map.moveable(g)) {
                    sight.add(g.copy());
                    g.add(x, y);
                }
            }
        }

        return sight;
    }

    private void moveRandom(double deltaT) {
        // TODO: dude idk fix it later
        ArrayList<Vector2> pos = map.getOpenNeighbors(getGridPos().gridPos);
        Vector2 currentDir = getDirection();
        Vector2 dir = pos.get(0).swap();
        for (int i = 0; i < pos.size(); i++) {
            if (pos.get(i).swap().multiply(-1).equals(currentDir)) {
                pos.remove(i);
                break;
            }
        }
        if (!pos.isEmpty()) dir = pos.get((int) (Math.random() * pos.size())).swap();

        setWantedDirection(dir);
        moveInDirection(deltaT);
    }

    private void moveInfront(double deltaT) {
        if (supposedPath.size() <= 1) {
//            Vector2 infront = getSig
            supposedPath = finder.findPath(this.getGridPos().gridPos, player.getMovingTowards());
            if (supposedPath == null) return;
            if (supposedPath.size() <= 1) {
                moveRandom(deltaT);
                return;
            }
            if (getGridPos().gridPos.equals(supposedPath.peek())) {
                supposedPath.poll();
            }
            if (supposedPath.isEmpty()) {
                return;
            }
        }
        if (getGridPos().gridPos.equals(supposedPath.peek())) {
            supposedPath.poll();
        }
        // go towards the grid pos by finding the distance between the current and next,
        // normalizing it, then setting the direction
        Vector2 goTo = getGridPos().gridPos.distanceTo(supposedPath.peek()).normalize();
        if (Math.abs(goTo.x) == Math.abs(goTo.y)) {
            supposedPath.clear();
            return;
        }
        // pixel coordinates: (x, y) and go left to right, then top to bottom increments
        // grid coordinates: (y, x) and go up to down, then left to right
        // so in order to go in the correct direction we have to swap and mulitply by -1
        setWantedDirection(
                goTo.swap().multiply(-1)
        );
        moveInDirection(deltaT);
    }

    public void updateMap(Map m) {
        this.map = m;
        finder = new PathFinder(this.map.baseGrid);
//        finder = new PathFinder(this.map.baseGrid);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Vector2 p = getPos();
        Vector2 size = getSize();

        g2.drawImage(getImage(), (int) p.x, (int) p.y, (int) size.x, (int) size.y, null);
    }
}
