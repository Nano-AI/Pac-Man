import java.awt.*;

public class Utils {
    public static char[] cardinalDirections = new char[]{'n', 'e', 's', 'w'};
    public static Vector2[] directions = new Vector2[]{
            new Vector2(0, -1),
            new Vector2(1, 0),
            new Vector2(0, 1),
            new Vector2(-1, 0)
    };
    public static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
    public static boolean inRange(double value, double min, double max) {
        return (value >= min) && (value <= max);
    }

    public static boolean inside(double value, double min, double max) {
        return value > min && value < max;
    }

    public static char getDirection(Vector2 d) {
        if (d.equals(getDirection('n'))) {
            return 'n';
        } else if (d.equals(getDirection('s'))) {
            return 's';
        } else if (d.equals(getDirection('e'))) {
            return 'e';
        } else if (d.equals(getDirection('w'))) {
            return 'w';
        } else {
            return ' ';
        }
    }

    public static Vector2 getDirection(char d) {
        int x = 0;
        int y = 0;
        switch (Character.toLowerCase(d)) {
            case 'n', 'u' -> y = -1;
            case 'e', 'r' -> x = 1;
            case 's', 'd' -> y = 1;
            case 'w', 'l' -> x = -1;
            case '0' -> x = 0;
            default -> {
                return new Vector2(0, 0);
            }
        }
        return new Vector2(x, y);
    }

}
