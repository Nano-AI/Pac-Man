/**
 * The ghost class that is the enemy to the pacman
 *
 * @author Aditya B, Ekam S
 * @version 4 December, 2023
 */

import javax.management.QueryEval;
import java.awt.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Ghost extends Entity {
    private Vector2 nextPos;
    public Player player;
    public Map map;
    double detectionRadius;
    public char name = 'r';

    PathFinder finder;

    Queue<Vector2> path;

    private double randomT = 0;

    public Ghost(int x, int y, int gridX, int gridY, int width, int height) {
        super(x, y, gridX, gridY, width, height);
        System.out.println(getWidth() + " " + getHeight());
        setupImages("./img/ghost");
        mapChar = 'p';

        this.detectionRadius = 120f;

        path = new LinkedList<>();
    }

    @Override
    public void update(double deltaT) {
//        if (getDistanceTo(player) < detectionRadius) {
//            addPos(player.getVectorDistance(this).multiply(0.05 * deltaT));
//        }
        randomT += deltaT;
        updateGridSpot();
        move(deltaT);
        speed = 1.75;
//        finder.solve(this.getGridPos().gridPos, player.getGridPos().gridPos);
//        System.out.println(finder.toString());
    }

    public void move(double deltaT) {
        switch (name) {
            case 'r':
                moveChase();
                break;
            default:
                moveRandom(deltaT);
        }
    }

    private void moveChase() {

    }

    private void moveRandom(double deltaT) {
        if (randomT > Math.random() * 10 || blocked) {
            ArrayList<Vector2> spots = map.getNeighbors(getGridPos().gridPos);
            System.out.println(spots);
            Vector2 dir;
            int rand = (int) (Math.random() * spots.size());
            dir = spots.get(rand);
            spots.forEach(i -> {System.out.print(Utils.getDirection(i));});
            System.out.println();
            setWantedDirection(Utils.getDirection(dir));
            randomT = 0;
        }
        moveInDirection(deltaT);
    }

    public void updateMap(Map m) {
        this.map = m;
        finder = new PathFinder(this.map.baseGrid);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Vector2 p = getPos();
        Vector2 size = getSize();

        g.setColor(Color.green);
        g2.fillRect((int) getGridPos().getX(), (int) getGridPos().getY(), getWidth(), getHeight());
        g2.drawRect(getX(), getY(), getWidth(), getHeight());
        g2.drawImage(getImage(), (int) p.x, (int) p.y, (int) size.x, (int) size.y, null);

//        ArrayList<Entity> path = getPathTo(player);
//        for (int i = 0; i < path.size() - 1; i++) {
//            g.drawLine(path.get(i).getX(), path.get(i).getY(), path.get(i + 1).getX(), path.get(i + 1).getY());
//        }
    }

//    private ArrayList<Vector2> getPathTo(Entity e) {
//        ArrayList<Vector2> path = new ArrayList<>();
//        char[][] reached = this.map.baseGrid.clone();
//
//        Queue<Vector2> frontier = new LinkedList<>();
//        frontier.add(getGridPos().gridPos);
//        reached[getGridPos().getX()][getGridPos().getY()] = 'S';
//
//        Vector2 current;
//
//        while (frontier.size() > 0) {
//            current = frontier.poll();
//            // https://theory.stanford.edu/~amitp/GameProgramming/AStarComparison.html
//            for (Vector2 next : getNeighbors(current, reached)) {
//                for (Vector2 item : frontier) {
//                    if (isIn(item, frontier)) {
//                        frontier.add(next);
//                        m[next.x][next.y] = 'R';
//                    }
//                }
//            }
//        }
//
//        return path;
//    }
//
//    private boolean isIn(Vector2 v, Queue q) {
//        for (Object i : q) {
//            if (((Vector2) i).equals(v)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private ArrayList<Vector2> getNeighbors(Vector2 pos, char[][] m) {
//        int x = (int) pos.x;
//        int y = (int) pos.y;
//        ArrayList<Vector2> o = new ArrayList<>();
//        if (x > 1) {
//            o.add(new Vector2(x - 1, y));
//        }
//
//        if (x < m.length - 2) {
//            o.add(new Vector2(x + 1, y));
//        }
//
//        if (y > 1) {
//            o.add(new Vector2(x, y - 1));
//        }
//
//        if (y < m[0].length - 2) {
//            o.add(new Vector2(x, y - 1));
//        }
//
//        return o;
//    }
}
