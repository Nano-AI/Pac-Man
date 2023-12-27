/**
 * The FileReader class provides a static method to read a file and create a Map object.
 * It reads the contents of the file, determines the dimensions, and populates a Map accordingly.
 *
 * @author Aditya B, Ekam S
 * @version 26 November, 2023
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class FileReader {

    /**
     * Read a file and create a Map object based on its contents.
     *
     * @param path The path to the file to be read.
     * @return A Map object representing the contents of the file.
     */
    public static Map readFile(String path) {
        int width = 0;
        int height = 0;
        Scanner s = null;

        try {
            s = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + path);
            return null;
        }

        ArrayList<String> file = new ArrayList<>();
        while (s.hasNextLine()) {
            String f = s.nextLine();
            width = f.length();
            file.add(f);
        }

        height = file.size();

        Map m = new Map(width, height);

        for (int i = 0; i < file.size(); i++) {
            String line = file.get(i);
            for (int j = 0; j < line.length(); j++) {
                m.set(line.charAt(j), i, j);
                if (line.charAt(j) == 'W') {
                    m.setBase(line.charAt(j), i, j);
                }
            }
        }

        for (char[] l : m.baseGrid) {
            System.out.println(Arrays.toString(l));
        }

        m.setupEmptySpots();

        return m;
    }
}
