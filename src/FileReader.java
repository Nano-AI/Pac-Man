import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileReader {
    public static Map readFile(String path) {
        int width = 0;
        int height = 0;
        Scanner s = null;

        try {
            s = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + path);
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
            }
        }

        return m;
    }
}
