public class Main {
    public static void main(String[] args) throws Exception {
        Window window = new Window("Pac-Man", FileReader.readFile("./template.map"));
        window.display();

        boolean running = true;
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;

        while (running) {
            long now = System.nanoTime();
            long updateTime = now - lastLoopTime;
            int FPS = (int) (1.0 / (updateTime / 1_000_000_000.0));
            lastLoopTime = now;

            double delta = updateTime / (double) OPTIMAL_TIME;

            window.setFPS(FPS);
            window.update(delta);
            window.render(false);
        }
    }
}