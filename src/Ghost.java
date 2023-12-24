/**
 * The ghost class that is the enemy to the pacman
 *
 * @author Aditya B, Ekam S
 * @version 4 December, 2023
 */

import java.awt.*;
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
    private double randomT = 0;
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
        // setup the images
        setupImages("./img/ghosts/" + name);
        // set the map char thing
        mapChar = 'p';
        // set detec ction radius
        this.detectionRadius = 120f;
        // setup the supposed path
        supposedPath = new LinkedList<>();
        // more vars
        blocked = true;
        baseSpeed = 1.75;
        speed = baseSpeed;
        chaseSpeed = 1.05 * speed;
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
        randomT += deltaT;
        updateGridSpot();
        move(deltaT);

        pathUpdate += deltaT;
    }

    public void move(double deltaT) {
        if (!canSee(player)) {
            moveScatter(deltaT);
            return;
        }
        switch (name) {
            case 'b':
                moveChase(deltaT);
                break;
            default:
                moveRandom(deltaT);
        }
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

    /**
     * Chasing the player
     * @param deltaT
     */
    private void moveChase(double deltaT) {
        if (canSee(player)) {
            speed = chaseSpeed;
            scatterMode = false;
        } else {
            speed = baseSpeed;
            scatterMode = true;
        }
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
        if (randomT > Math.random() * 1000 || blocked) {
            ArrayList<Vector2> spots = new ArrayList<>();
            Vector2[] neighbors = Utils.getDirections();
//            System.out.println(getGridPos().gridPos);
//            for (Vector2 c : neighbors) {
//                if (!c.multiply(-1).equals(getDirection().swap())) {
//                    spots.add(c);
//                }
//            }
//            if (spots.isEmpty()) {
//                spots.addAll(
//                        map.getNeighbors(getGridPos().gridPos)
//                );
//            }
//            int rand = (int) (Math.random() * spots.size());
            // swap because matrix coordinates are width, height BUT
            // graphical coordinates are x then y
            // this means that they MUST be swapped in order for it to work
            int rand = (int) (Math.random() * neighbors.length);
//            setWantedDirection(neighbors[rand]);
            randomT = 0;
        }
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

        Node<Vector2> head = sortedScatterPath.head;
        while (head.next != null) {
            Vector2 v1 = map.getPoint(head.val);
            Vector2 v2 = map.getPoint(head.next.val);
            g2.drawLine(
                    (int) v1.x, (int) v1.y, (int) v2.x, (int) v2.y
            );
            head = head.next;
        }

        for (Vector2 v : supposedPath) {
            Vector2 pFind = map.getPoint(v);
            g2.fillRect((int) pFind.x, (int) pFind.y, map.pixelPerHorizontalGrid, map.pixelPerHorizontalGrid);
        }

        g2.drawImage(getImage(), (int) p.x, (int) p.y, (int) size.x, (int) size.y, null);

        if (findPath != null)
            for (Vector2 v : findPath) {
                g2.setColor(Color.RED);
                g2.fillRect((int) v.x, (int) v.y, m.pixelPerHorizontalGrid, m.pixelPerVerticalGrid);
            }
    }
}
