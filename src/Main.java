public class Main {
    public static void main(String[] args) {
        Window window = new Window("Pac-Man", FileReader.readFile("./template.map"));
        window.display();

        boolean running = true;

        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;

        while (running) {
            long now = System.nanoTime();
            long updateTime = now - lastLoopTime;
            lastLoopTime = now;

            double delta = updateTime / (double) OPTIMAL_TIME;

            window.update(delta);
            window.render(true);
        }
    }
}