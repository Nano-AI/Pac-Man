import java.awt.*;

public class Utils {
    public static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }
}
