/**
 * The ghost class that is the enemy to the pacman
 *
 * @author Aditya B, Ekam S
 * @version 4 December, 2023
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Ghost extends Entity {
    private Vector2 nextPos;
    public Player player;
    public Map map;
    double detectionRadius;

    public Ghost(int x, int y, int gridX, int gridY, int width, int height) {
        super(x, y, gridX, gridY, width, height);
        System.out.println(getWidth() + " " + getHeight());
        setupImages("./img/ghost");
        mapChar = 'p';

        this.detectionRadius = 120f;
    }

    @Override
    public void update(double deltaT) {
//        if (getDistanceTo(player) < detectionRadius) {
//            addPos(player.getVectorDistance(this).multiply(0.05 * deltaT));
//        }
        updateGridSpot();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Vector2 p = getPos();
        Vector2 size = getSize();

        g.setColor(Color.green);
        g2.drawRect(getX(), getY(), getWidth(), getHeight());
        g2.fillRect((int) getGridPos().x, (int) getGridPos().y, getWidth(), getHeight());
        g2.drawImage(getImage(), (int) p.x, (int) p.y, (int) size.x, (int) size.y, null);

        ArrayList<Entity> path = getPathTo(player);
        for (int i = 0; i < path.size() - 1; i++) {
            g.drawLine(path.get(i).getX(), path.get(i).getY(), path.get(i + 1).getX(), path.get(i + 1).getY());
        }
    }

    private ArrayList<Entity> getPathTo(Entity e) {
        ArrayList<Entity> path = new ArrayList<>();
        char[][] m = this.map.baseGrid.clone();

        Queue<Entity> f = new LinkedList<>();
        f.add(getGridPos());
        m[getGridPos().getX()][getGridPos().getY()] = 'S';

        Entity current;

        while (f.size() > 0) {
            current = f.poll();
            // https://theory.stanford.edu/~amitp/GameProgramming/AStarComparison.html
            for (Entity n : )
        }

        return path;
    }

    private ArrayList<Vector2> getNeighbors(int x, int y, char[][] m) {
        ArrayList<Vector2> o = new ArrayList<>();
        if (x > 1) {
            o.add(new Vector2(x - 1, y));
        }

        if (x < m.length - 2) {
            o.add(new Vector2(x + 1, y));
        }

        if (y > 1) {
            o.add(new Vector2(x, y - 1));
        }

        if (y < m[0].length - 2) {
            o.add(new Vector2(x, y - 1));
        }

        return o;
    }
}
