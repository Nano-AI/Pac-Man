import java.awt.*;

public class Utils {
    public static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
    public static boolean inRange(double value, double min, double max) {
        return (value >= min) && (value <= max);
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
