import java.awt.*;
import java.util.ArrayList;

public class Map {
    private char[][] grid;
    public final int width, height;

    private Vector2[][] points;

    public int pixelPerHorizontalGrid, pixelPerVerticalGrid;
    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        this.grid = new char[height][width];
        this.points = new Vector2[height][width];
    }

    public char at(int x, int y) {
        return grid[x][y];
    }

    public void set(char v, int x, int y) {
        grid[x][y] = v;
    }

    public void setPoint(Vector2 v, int x, int y) {
        points[x][y] = v;
    }

    public Vector2 getPoint(int x, int y) {
        return points[x][y];
    }

    private boolean collides(Vector2 p1, Vector2 p2, boolean iterate) {
        int width = pixelPerHorizontalGrid;
        int height = pixelPerVerticalGrid;
        // top intersections
        return p1.x > p2.x && p1.x < p2.x + width && p1.y > p2.y && p1.y < p2.y + height;
    }

    public boolean collides(Vector2 p1, Vector2 p2) {
        return collides(p1, p2, true) || collides(p2, p1, true);
    }

    public ArrayList<Vector2> collides(Vector2 pos, char type) {
        ArrayList<Vector2> entities = new ArrayList<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                char at = grid[i][j];
                Vector2 point = points[i][j];
                if (at == type && collides(point, pos)) {
                    entities.add(point);
                }
            }
        }
        return entities;
    }

    @Override
    public String toString() {
        StringBuilder o = new StringBuilder();
        for (char[] r : grid) {
            for (char a : r) {
                o.append(a);
            }
            o.append("\n");
        }
        return o.toString();
    }
}
