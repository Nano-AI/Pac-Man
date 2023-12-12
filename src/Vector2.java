/**
 * The Vector2 class represents a 2D vector with x and y coordinates.
 * It includes methods for vector operations such as multiplication, addition, normalization, and equality checks.
 *
 * @author Aditya B, Ekam S
 * @version 26 November, 2023
 */
import java.awt.*;

public class Vector2 {
    public double x, y;

    /**
     * Constructor for Vector2 with specified x and y coordinates as doubles.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor for Vector2 with specified x and y coordinates as integers.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Multiply the vector by another vector component-wise.
     *
     * @param m The vector to multiply with.
     * @return The current vector after multiplication.
     */
    public Vector2 multiply(Vector2 m) {
        this.x *= m.x;
        this.y *= m.y;
        return this;
    }

    /**
     * Multiply the vector by a scalar value.
     *
     * @param d The scalar value to multiply with.
     * @return The current vector after multiplication.
     */
    public Vector2 multiply(double d) {
        this.x *= d;
        this.y *= d;
        return this;
    }

    /**
     * Add another vector to the current vector.
     *
     * @param a The vector to add.
     * @return The current vector after addition.
     */
    public Vector2 add(Vector2 a) {
        this.x += a.x;
        this.y += a.y;
        return this;
    }

    /**
     * Normalize the vector, i.e., convert it to a unit vector.
     *
     * @return A new normalized vector.
     */
    public Vector2 normalize() {
        int x = 0;
        int y = 0;

        if (this.x > 0) {
            x = 1;
        } else if (this.x < 0) {
            x = -1;
        }

        if (this.y > 0) {
            y = 1;
        } else if (this.y < 0) {
            y = -1;
        }

        return new Vector2(x, y);
    }

    /**
     * Create a copy of the current vector.
     *
     * @return A new vector with the same coordinates.
     */
    public Vector2 copy() {
        return new Vector2(x, y);
    }

    /**
     * Check if the current vector is equal to another vector.
     *
     * @param v The vector to compare with.
     * @return True if the vectors are equal, false otherwise.
     */
    public boolean equals(Vector2 v) {
        return x == v.x && y == v.y;
    }

    /**
     * Check if the vector is within the boundaries of an entity.
     *
     * @param e The entity for boundary checking.
     * @return True if the vector is within the entity boundaries, false otherwise.
     */
    public boolean isIn(Entity e) {
        Rect r = new Rect(e.getPos().copy().add(e.hitbox.pos), e.hitbox.size);
        return Utils.inRange(x, r.getX(), r.getX() + r.getWidth()) && Utils.inRange(y, r.getY(), r.getY() + r.getHeight());
    }

    /**
     * Get a string representation of the vector.
     *
     * @return A string representation of the vector.
     */
    @Override
    public String toString() {
        return String.format("{x=%.2f, y=%.2f}", x, y);
    }

    public Vector2 distanceTo(Vector2 pos) {
        return new Vector2(
                this.x - pos.x,
                this.y - pos.y
        );
    }
}
