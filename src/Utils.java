/**
 * The Utils class contains utility methods used throughout the game.
 * It includes functions for handling directions, clamping values, checking ranges, and reading images.
 *
 * @author Aditya B, Ekam S
 * @version 4 December, 2023
 */

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utils {
    public static char[] cardinalDirections = new char[]{'n', 'e', 's', 'w'};

    public static Vector2[] getDirections() {
        return new Vector2[]{
                new Vector2(0, -1),
                new Vector2(1, 0),
                new Vector2(0, 1),
                new Vector2(-1, 0)
        };
    }

    /**
     * Clamp a value within a specified range.
     *
     * @param v   The value to clamp.
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return The clamped value.
     */
    public static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    /**
     * Check if a double value is within a specified range.
     *
     * @param value The value to check.
     * @param min   The minimum value of the range.
     * @param max   The maximum value of the range.
     * @return True if the value is within the range, false otherwise.
     */
    public static boolean inRange(double value, double min, double max) {
        return (value >= min) && (value <= max);
    }

    /**
     * Check if a double value is inside a specified range (excluding the bounds).
     *
     * @param value The value to check.
     * @param min   The minimum value of the range.
     * @param max   The maximum value of the range.
     * @return True if the value is inside the range, false otherwise.
     */
    public static boolean inside(double value, double min, double max) {
        return (value > min) && (value < max);
    }

    /**
     * Check if a value is within a specified range with a given percentage tolerance.
     *
     * @param value   The value to check.
     * @param equal   The target value.
     * @param percent The percentage tolerance.
     * @return True if the value is within the range, false otherwise.
     */
    public static boolean withinRange(double value, double equal, double percent) {
        return value * (1 - percent) <= equal && value * (1 + percent) >= equal;
    }

    /**
     * Get the cardinal direction character based on a direction vector.
     *
     * @param d The direction vector.
     * @return The cardinal direction character ('n', 'e', 's', 'w').
     */
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

    /**
     * Get the direction vector based on a cardinal direction character.
     *
     * @param d The cardinal direction character.
     * @return The direction vector.
     */
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

    /**
     * Read a sequence of images from a specified folder.
     *
     * @param folder The path to the folder containing images.
     * @return An array of BufferedImage objects representing the images.
     */
    public static BufferedImage[] getImages(String folder) {
        File dir = new File(folder);
        File[] listing = dir.listFiles();

        System.out.println(folder);
        assert listing != null;
        BufferedImage[] o = new BufferedImage[listing.length];

        for (File child : listing) {
            String name = child.getName();
            int fileIndex;
            try {
                fileIndex = Integer.parseInt(name.substring(0, name.lastIndexOf(".")));
            } catch (NumberFormatException e) {
                System.out.println("The given directory has an invalid file name: " + name);
                continue;
            }

            try {
                o[fileIndex] = ImageIO.read(child);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return o;
    }

    public static double pythag(double a, double b) {
        return Math.sqrt(
                a * a + b * b
        );
    }
}
