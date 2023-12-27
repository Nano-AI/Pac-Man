/**
 * Main class which includes game loops and runs the game.
 * @author Aditya B, Ekam S
 * @version 4 December, 2023
 */
public class Main {
    public static void main(String[] args) {
        // create a window to display the game, and provide the map of the game
        Window window = new Window("Pac-Man", FileReader.readFile("./template.map"));
        // display the window
        window.display();

        // game loop logic
        boolean running = true;
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 60;
        final long OPTIMAL_TIME = 1_000_000_000 / TARGET_FPS;

        // game loop
        while (window.running) {
            // calculate delta-T (time since last frame)
            long now = System.nanoTime();
            long updateTime = now - lastLoopTime;
            int FPS = (int) (1.0 / (updateTime / 1_000_000_000.0));

            double delta = updateTime / (double) OPTIMAL_TIME;

            lastLoopTime = now;

            // set the display FPS
            window.setFPS(FPS);
            // update the game based on delta
            window.update(delta);
            // render the game and disable hitbox outlines
            window.render(false);
        }
    }
}