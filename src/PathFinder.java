import java.util.Arrays;

public class PathFinder implements Runnable {
    final static char TRIED = 'T';
    final static char PATH = 'P';
    final static char OPEN = ' ';
    private char[][] grid;
    private int width;
    private int height;
    private char[][] map;

    private Vector2 start;
    private Vector2 end;
    public PathFinder(char[][] grid) {
        this.grid = grid;
        this.width = grid[0].length;
        this.height = grid.length;

        this.map = new char[height][width];

        for (char[] c : this.grid) {
            System.out.println(Arrays.toString(c));
        }
    }

    public void solve(Vector2 start, Vector2 end) {
        this.start = start;
        this.end = end;

        traverse((int) start.x, (int) start.y);
    }

    private boolean traverse(int i, int j) {
        if (!isValid(i, j)) {
            return false;
        }

        if (isEnd(i, j)) {
            map[i][j] = PATH;
            return true;
        } else {
            map[i][j] = TRIED;
        }

        if (traverse(i - 1, j)) {
            map[i - 1][j] = PATH;
            return true;
        }

        if (traverse(i, j + 1)) {
            map[i][j + 1] = PATH;
            return true;
        }

        if (traverse(i + 1, j)) {
            map[i + 1][j] = PATH;
            return true;
        }

        if (traverse(i, j - 1)) {
            map[i][j - 1] = PATH;
            return true;
        }

        return false;
    }

    private boolean isEnd(int i, int j) {
        return new Vector2(i, j).equals(end);
    }

    private boolean isValid(int i, int j) {
        return inRange(i, j) && isOpen(i, j) && !isTried(i, j);
    }

    private boolean isOpen(int i, int j) {
        return grid[i][j] == OPEN;
    }

    private boolean isTried(int i, int j) {
        return grid[i][j] == TRIED;
    }

    private boolean inRange(int i, int j) {
        return inHeight(i) && inWidth(j);
    }

    private boolean inHeight(int i) {
        return i >= 0 && i < height;
    }

    private boolean inWidth(int j) {
        return j >= 0 && j < width;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        for (char[] row : map) {
            s.append(Arrays.toString(row)).append("\n");
        }
        return s.toString();
    }

    @Override
    public void run() {
    }
}
