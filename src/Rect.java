/**
 * The Rect class represents a rectangular shape defined by a position and size.
 * It includes methods for collision detection and manipulation of the rectangle.
 *
 * @author Aditya B, Ekam S
 * @version 26 November, 2023
 */
public class Rect {
    public Vector2 size;
    public Vector2 pos;

    /**
     * Constructor for the Rect class with specified position and size vectors.
     *
     * @param pos  The position vector.
     * @param size The size vector.
     */
    public Rect(Vector2 pos, Vector2 size) {
        this.pos = pos;
        this.size = size;
    }

    /**
     * Constructor for the Rect class with specified position and size parameters.
     *
     * @param x      The x-coordinate of the position.
     * @param y      The y-coordinate of the position.
     * @param width  The width of the rectangle.
     * @param height The height of the rectangle.
     */
    public Rect(int x, int y, int width, int height) {
        this.pos = new Vector2(x, y);
        this.size = new Vector2(width, height);
    }

    /**
     * Move the rectangle to the center of a specified width and height.
     *
     * @param width  The width of the container.
     * @param height The height of the container.
     */
    public void pad(int width, int height) {
        this.pos.x = (width - this.size.x) / 2;
        this.pos.y = (height - this.size.y) / 2;
    }

    /**
     * Check if the current rectangle collides with another rectangle.
     *
     * @param r The other rectangle for collision checking.
     * @return True if the rectangles collide, false otherwise.
     */
    public boolean collides(Rect r) {
        // top intersections
        boolean xOverlap = Utils.inRange(getX(), r.getX(), r.getX() + r.getWidth()) ||
                Utils.inRange(r.getX(), getX(), getX() + getWidth());
        boolean yOverlap = Utils.inRange(getY(), r.getY(), r.getY() + r.getHeight()) ||
                Utils.inRange(r.getY(), getY(), getY() + getHeight());
        return xOverlap && yOverlap;
    }

    /**
     * Check if the current rectangle is completely inside another rectangle.
     *
     * @param r The other rectangle for containment checking.
     * @return True if the current rectangle is inside the other rectangle, false otherwise.
     */
    public boolean isIn(Rect r) {
        Vector2 topLeft = pos.copy();
        Vector2 topRight = pos.copy().add(new Vector2(getWidth(), 0));
        Vector2 bottomLeft = pos.copy().add(new Vector2(0, getHeight()));
        Vector2 bottomRight = pos.copy().add(getSize());

        Vector2 rtopLeft = r.pos.copy();
        Vector2 rtopRight = r.pos.copy().add(new Vector2(getWidth(), 0));
        Vector2 rbottomLeft = r.pos.copy().add(new Vector2(0, getHeight()));
        Vector2 rbottomRight = r.pos.copy().add(getSize());

        return topRight.x > rtopLeft.x && topLeft.x < rtopRight.x && bottomRight.y > rtopLeft.y && topLeft.y < rbottomRight.y;
    }

    /**
     * Create a copy of the current rectangle.
     *
     * @return A new rectangle with the same position and size.
     */
    public Rect copy() {
        return new Rect(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * Displace the current rectangle by a specified offset vector.
     *
     * @param offset The displacement vector.
     * @return The current rectangle after displacement.
     */
    public Rect displace(Vector2 offset) {
        pos.add(offset);
        return this;
    }

    /**
     * Move the rectangle horizontally by a specified amount.
     *
     * @param x The amount to move the rectangle horizontally.
     */
    public void moveX(int x) {
        this.pos.x += x;
    }

    /**
     * Move the rectangle vertically by a specified amount.
     *
     * @param y The amount to move the rectangle vertically.
     */
    public void moveY(int y) {
        this.pos.y += y;
    }

    /**
     * Get the x-coordinate of the top-left corner of the rectangle.
     *
     * @return The x-coordinate of the top-left corner.
     */
    public int getX() {
        return (int) this.pos.x;
    }

    /**
     * Get the y-coordinate of the top-left corner of the rectangle.
     *
     * @return The y-coordinate of the top-left corner.
     */
    public int getY() {
        return (int) this.pos.y;
    }

    /**
     * Get the width of the rectangle.
     *
     * @return The width of the rectangle.
     */
    public int getWidth() {
        return (int) this.size.x;
    }

    /**
     * Get the height of the rectangle.
     *
     * @return The height of the rectangle.
     */
    public int getHeight() {
        return (int) this.size.y;
    }

    /**
     * Get the size vector of the rectangle.
     *
     * @return The size vector of the rectangle.
     */
    public Vector2 getSize() {
        return this.size;
    }
}
