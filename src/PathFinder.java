import java.util.*;

/**
 * The PathFinder class implements the A* pathfinding algorithm to locate a path from start to goal in a grid.
 *
 * This class utilizes a priority queue and various helper methods for pathfinding.
 *
 * @author Aditya Bankoti, Ekam Singh
 * @version January 25, 2024
 */
public class PathFinder {
    private class Node {
        Vector2 pos;
        int gCost;
        int hCost;
        Node parent;

        /**
         * Constructs a Node with the specified position.
         *
         * @param pos The position of the node.
         */
        public Node(Vector2 pos) {
            this.pos = pos;
            this.gCost = 0;
            this.hCost = 0;
            this.parent = null;
        }
    }

    final static char TRIED = 'T';
    final static char PATH = 'P';
    final static char OPEN = ' ';
    private char[][] grid;
    private int width;
    private int height;
    private char[][] map;

    private Vector2 start;
    private Vector2 end;

    Queue<Vector2> path;

    /**
     * Constructs a PathFinder with the specified grid.
     *
     * @param grid The grid representing the environment.
     */
    public PathFinder(char[][] grid) {
        this.grid = grid;
        this.width = grid[0].length;
        this.height = grid.length;

        this.map = new char[height][width];

        for (char[] c : this.grid) {
            System.out.println(Arrays.toString(c));
        }
        path = new LinkedList<Vector2>();
    }

    /**
     * Uses the A* pathfinding algorithm to locate a path from start to goal.
     *
     * @param start The start location.
     * @param goal  The eventual end goal to go towards.
     * @return A Queue telling directions in order.
     */
    public Queue<Vector2> findPath(Vector2 start, Vector2 goal) {
        if (start == null || goal == null) return new LinkedList<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(node -> node.gCost + node.hCost));
        Set<Vector2> closedSet = new HashSet<>();

        Node startNode = new Node(start);
        Node endNode = new Node(goal);

        openSet.add(startNode);

        int iterations = 0;

        while (!openSet.isEmpty()) {
            Node currentNode = openSet.poll();
            iterations++;
            if (iterations > Math.pow(map.length, 3)) {
                return new LinkedList<>();
            }

            if (currentNode.pos.equals(endNode.pos)) {
                return reconstructPath(currentNode);
            }

            closedSet.add(currentNode.pos);

            for (Vector2 n : getNeighbors(currentNode.pos)) {
                if (closedSet.contains(n)) {
                    continue;
                }

                int tentativeGCost = currentNode.gCost + 1;

                Node neighborNode = new Node(n);
                neighborNode.gCost = tentativeGCost;
                neighborNode.hCost = (int) heuristic(n, goal);
                neighborNode.parent = currentNode;

                if (!openSet.contains(neighborNode) || tentativeGCost < neighborNode.gCost) {
                    openSet.add(neighborNode);
                }
            }
        }

        return null;
    }

    /**
     * Reconstructs the path from the end node to the start node.
     *
     * @param node The end node.
     * @return A Queue representing the reconstructed path.
     */
    private Queue<Vector2> reconstructPath(Node node) {
        Queue<Vector2> path = new LinkedList<>();

        while (node != null) {
            path.add(node.pos);
            node = node.parent;
        }

        Collections.reverse((List<?>) path);
        return path;
    }

    /**
     * Retrieves the neighboring positions of a given position.
     *
     * @param pos The current position.
     * @return An ArrayList of neighboring positions.
     */
    private ArrayList<Vector2> getNeighbors(Vector2 pos) {
        ArrayList<Vector2> neighbors = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Vector2 newPos = pos.copy().add(i, j);
                if (Math.abs(i) != Math.abs(j) && isValid(newPos) && isOpen(newPos)) {
                    neighbors.add(newPos);
                }
            }
        }
        return neighbors;
    }

    /**
     * Calculates the heuristic distance between two positions.
     *
     * @param a The first position.
     * @param b The second position.
     * @return The heuristic distance between the two positions.
     */
    private double heuristic(Vector2 a, Vector2 b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private boolean isValid(Vector2 v) {
        return isValid((int) v.x, (int) v.y);
    }

    private boolean isOpen(Vector2 v) {
        return isOpen((int) v.x, (int) v.y);
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

    /**
     * Returns a string representation of the map.
     *
     * @return The string representation of the map.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (char[] row : map) {
            s.append(Arrays.toString(row)).append("\n");
        }
        return s.toString();
    }
}
