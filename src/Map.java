import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The Map class represents a grid-based structure used for mapping in the game.
 * It stores information about the layout of the game world, including characters at different positions.
 *
 * @author Aditya B, Ekam S
 * @version 4 December, 2023
 */

public class Map {
    private char[][] grid;
    public char[][] baseGrid;
    public final int width, height;

    private Vector2[][] points;
    private Vector2[][] pixelPoints;
    private Vector2 playerPos;
    private double minDist = Double.MAX_VALUE;

    public int pixelPerHorizontalGrid, pixelPerVerticalGrid;

    public HashMap<Character, ArrayList<Vector2>> scatterPaths;

    private ArrayList<Vector2> emptySpaces;

    /**
     * Constructor for the Map class with specified width and height.
     *
     * @param width  The width of the map.
     * @param height The height of the map.
     */
    public Map(int width, int height) {
        this.width = width;
        this.height = height;

        this.grid = new char[height][width];
        this.baseGrid = new char[height][width];
        this.points = new Vector2[height][width];
        this.pixelPoints = new Vector2[height][width];

        for (char[] a : this.baseGrid) {
            Arrays.fill(a, ' ');
        }
    }

    /**
     * Get the character at a specific position on the map.
     *
     * @param v The position vector.
     * @return The character at the specified position.
     */
    public char at(Vector2 v) {
        if (!valid(v)) return 'W';
        return at((int) v.x, (int) v.y);
    }

    /**
     * Get the character at specific coordinates on the map.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The character at the specified position.
     */
    public char at(int x, int y) {
        if (x > height || x < 0 || y > width || y < 0) {
            return 'W';
        }
        return grid[x][y];
    }

    public void set(char v, Vector2 pos) {
        grid[(int) pos.x][(int) pos.y] = v;
    }

    /**
     * Set the character at specific coordinates on the map.
     *
     * @param v The character to be set.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public void set(char v, int x, int y) {
        grid[x][y] = v;
    }

    public void setBase(char v, int x, int y) {
        baseGrid[x][y] = v;
    }

    /**
     * Set a point at specific coordinates on the map.
     *
     * @param v The point vector to be set.
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public void setPoint(Vector2 v, int x, int y) {
        points[x][y] = v;
    }

    /**
     * Get a point at specific coordinates on the map.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @return The point vector at the specified position.
     */
    public Vector2 getPoint(int x, int y) {
        return points[x][y];
    }

    public Vector2 getPoint(Vector2 v) {
        return getPoint((int) v.x, (int) v.y);
    }

    /**
     * Override of the toString method to represent the map as a string.
     *
     * @return A string representation of the map.
     */
    @Override
    public String toString() {
        StringBuilder o = new StringBuilder();
        for (char[] r : baseGrid) {
            for (char a : r) {
                o.append("'");
                o.append(a);
                o.append("', ");
            }
            o.append("\n");
        }
        return o.toString();
    }

    public void setupEmptySpots() {
        emptySpaces = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (at(i, j) == '.') {
                    emptySpaces.add(new Vector2(i, j));
                }
            }
        }
    }

    public Vector2 getRandomGridSpot() {
        // get a random empty position on the map
        // this is in terms of array coordintes
        return emptySpaces.get((int) (Math.random() * emptySpaces.size()));
    }

    public void updatePlayerPos(Vector2 pos, double minDist) {
        if (minDist < this.minDist) {
            this.playerPos = pos;
            this.minDist = minDist;
        }
    }

    public Vector2 getPlayerPos() {
        return playerPos.copy();
    }

    public void resetCount() {
        minDist = Double.MAX_VALUE;
    }

    public void clearGrid() {
        for (char[] chars : grid) {
            Arrays.fill(chars, ' ');
        }
    }

    public boolean valid(Vector2 v) {
        return v.x >= 0 && v.x < height && v.y >= 0 && v.y < width;
    }

    public boolean moveable(Vector2 v) {
        return at(v) != 'W' && valid(v);
    }

    public ArrayList<Vector2> getNeighbors(Vector2 pos) {
        ArrayList<Vector2> o = new ArrayList<>();
        for (Vector2 c : Utils.getDirections()) {
            Vector2 cpy = pos.copy().add(c);
            if (baseGrid[(int) cpy.x][(int) cpy.y] == ' ') {
                o.add(c);
            }
        }
        return o;
    }

    public void printNeighbors(Vector2 pos) {
        for (int i = -1; i <= 1; i++) {
            if (pos.x + i < 0 || pos.x + i > height) {
                continue;
            }
            for (int j = -1; j <= 1; j++) {
                if (pos.y + j < 0 || pos.y + j > width) {
                    continue;
                }
                System.out.print(at((int) (pos.x + j), (int) (pos.y + i)));
            }
            System.out.println();
        }
    }
}
