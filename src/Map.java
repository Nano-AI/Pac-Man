import java.util.ArrayList;
import java.util.Arrays;

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
            return ' ';
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

    public Vector2 getRandomGridSpot() {
        ArrayList<Vector2> emptySpaces = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (at(i, j) == '.') {
                    emptySpaces.add(new Vector2(i, j));
                }
            }
        }

        // get a random empty position on the map
        // this is in terms of array coordintes
        Vector2 spawnGrid = emptySpaces.get((int) (Math.random() * emptySpaces.size()));
        return spawnGrid;
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

    public void update(ArrayList<Entity> es) {
    }
}
