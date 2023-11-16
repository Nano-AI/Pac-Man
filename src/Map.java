import java.awt.*;
import java.util.ArrayList;

public class Map {
    private char[][] grid;
    public final int width, height;

    private Vector2[][] points;
    private Vector2[][] pixelPoints;

    public int pixelPerHorizontalGrid, pixelPerVerticalGrid;
    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        this.grid = new char[height][width];
        this.points = new Vector2[height][width];
        this.pixelPoints = new Vector2[height][width];
    }

    public char at(Vector2 v) {
        return at((int) v.x, (int) v.y);
    }

    public char at(int x, int y) {
        if (x > height || x < 0 || y > width || y < 0) {
            return ' ';
        }
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
    //    public ArrayList<Vector2> collides(Vector2 pos, Vector2 type) {
//        ArrayList<Vector2> entities = new ArrayList<>();
//        for (int i = 0; i < grid.length; i++) {
//            for (int j = 0; j < grid[i].length; j++) {
//                char at = grid[i][j];
//                Vector2 point = points[i][j];
//                if (at == type && collides(point, pos)) {
//                    entities.add(point);
//                }
//            }
//        }
//        return entities;
//    }

//    public ArrayList<Vector2> getPossibleDirections(Vector2 pos) {
//        ArrayList<Vector2> values = new ArrayList<>();
//        char[] possibleDirections = new char[]{'u', 'd', 'l', 'r'};
//
//        Vector2 size = new Vector2(pixelPerHorizontalGrid, pixelPerVerticalGrid);
//
//        for (char d : possibleDirections) {
//            Vector2 dV = Utils.getDirection(d);
//            Vector2 newPos = dV.multiply(size).add(pos);
//            if (!collides(newPos, pos)) {
//                values.add(newPos);
//            }
//        }
//
//        System.out.println(values);
//
//        return values;
//    }

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
