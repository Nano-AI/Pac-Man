/**
 * The ghost class that is the enemy to the pacman
 *
 * @author Aditya B, Ekam S
 * @version 4 December, 2023
 */

import javax.management.QueryEval;
import java.awt.*;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

public class Ghost extends Entity {
    private Vector2 nextPos;
    public Player player;
    public Map map;
    double detectionRadius;
    public char name = 'r';
    private double pathUpdate = 0;

    private ArrayList<Vector2> scatterPath;

    PathFinder finder;

    Queue<Vector2> sortedPath;

    private double randomT = 0;

    ArrayList<Vector2> findPath;
    private double chaseSpeed;
    private double baseSpeed;

    private LList<Vector2> sortedScatterPath;

    public Ghost(String name, int x, int y, int gridX, int gridY, int width, int height) {
        super(x, y, gridX, gridY, width, height);
        System.out.println(getWidth() + " " + getHeight());
        setupImages("./img/ghosts/" + name);
        mapChar = 'p';

        this.detectionRadius = 120f;

        sortedPath = new LinkedList<>();

        blocked = true;
        baseSpeed = 1.75;
        speed = baseSpeed;
        chaseSpeed = 1.05 * speed;
    }

    public void setupScatterPath() {
        scatterPath = new ArrayList<>();
        char c = Character.toLowerCase(mapChar);
        for (int i = 0; i < map.height; i++) {
            for (int j = 0; j < map.width; j++) {
                if (map.at(i, j) == c) {
                    scatterPath.add(new Vector2(i, j));
                }
            }
        }

        sortedScatterPath = new LList<Vector2>();
        sortedScatterPath.head = new Node<>(scatterPath.get(0).copy());
        scatterPath.remove(0);
        Node<Vector2> head = sortedScatterPath.head;

        int i = 0;
        while (i < scatterPath.size()) {
            System.out.println(scatterPath.get(i).distanceTo(head.val).getMagnitude());
            if (scatterPath.get(i).distanceTo(head.val).getMagnitude() == 1f) {
                head.next = new Node<>(scatterPath.get(i));
                head = head.next;
                scatterPath.remove(i);
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
        switch (name) {
            case 'b':
                moveChase(deltaT);
                break;
            default:
                moveRandom(deltaT);
        }
    }

    private void moveChase(double deltaT) {
        if (canSee(player)) {
            speed = chaseSpeed;
        } else {
            speed = baseSpeed;
        }
        if (getDistanceTo(player) < detectionRadius && pathUpdate > 50) {
        } else {
            moveRandom(deltaT);
        }
    }

    private boolean canSee(Entity e) {
        for (Vector2 v : getSightLine()) {
            if (e.getGridPos().gridPos.equals(v)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Vector2> getSightLine() {
        ArrayList<Vector2> sight = new ArrayList<>();

        // get x values to the right
        for (int x = -1; x <= 1; x+= 1) {
            for (int y = -1; y <= 1; y += 1) {
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
            setWantedDirection(neighbors[rand]);
            randomT = 0;
        }
        moveInDirection(deltaT);
    }

    public void updateMap(Map m) {
        this.map = m;
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
        g2.drawImage(getImage(), (int) p.x, (int) p.y, (int) size.x, (int) size.y, null);

        if (findPath != null)
            for (Vector2 v : findPath) {
                g2.setColor(Color.RED);
                g2.fillRect((int) v.x, (int) v.y, m.pixelPerHorizontalGrid, m.pixelPerVerticalGrid);
            }
    }
}
