import java.awt.*;

public class Vector2 {
    public double x, y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 multiply(Vector2 m) {
        this.x *= m.x;
        this.y *= m.y;
        return this;
    }

    public Vector2 multiply(double d) {
        this.x *= d;
        this.y *= d;
        return this;
    }

    public Vector2 add(Vector2 a) {
        this.x += a.x;
        this.y += a.y;
        return this;
    }

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

    public Vector2 copy() {
        return new Vector2(x, y);
    }

    public boolean equals(Vector2 v) {
        return x == v.x && y == v.y;
    }

    @Override
    public String toString() {
        return String.format("{x=%.2f, y=%.2f}", x, y);
    }
}
