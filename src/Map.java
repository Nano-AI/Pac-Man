/**
 * The Map class represents a grid-based structure used for mapping in the game.
 * It stores information about the layout of the game world, including characters at different positions.
 *
 * @author Aditya B, Ekam S
 * @version 26 November, 2023
 */

public class Map {
    private char[][] grid;
    public final int width, height;

    private Vector2[][] points;
    private Vector2[][] pixelPoints;

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
        this.points = new Vector2[height][width];
        this.pixelPoints = new Vector2[height][width];
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
        for (char[] r : grid) {
            for (char a : r) {
                o.append(a);
            }
            o.append("\n");
        }
        return o.toString();
    }
}
