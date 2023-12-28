/**
 * The StatsCounter class represents an entity responsible for displaying game statistics.
 * Extends the Entity class.
 *
 * @author Aditya B, Ekam S
 * @version 26 November, 2023
 */

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class StatsCounter extends Entity {

    private int fps; // Frames per second
    private double updateT; // Time since the last update
    private boolean canDraw; // Flag to determine if drawing is allowed
    private double deltaT; // Time interval between updates
    private int score; // Player score
    private Font font; // Font for text rendering
    public Vector2 windowSize; // Size of the game window

    /**
     * Constructor for StatsCounter class with specified parameters.
     *
     * @param x      The x-coordinate of the entity's position.
     * @param y      The y-coordinate of the entity's position.
     * @param width  The width of the entity.
     * @param height The height of the entity.
     */
    public StatsCounter(int x, int y, int width, int height) {
        super(x, y, width, height);
        fps = 0;
        updateT = 0;
        score = 0;
        canDraw = false;

        font = new Font("TimesRoman", Font.PLAIN, 32);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("./img/RetroBoundUC.ttf")).deriveFont(Font.PLAIN, 32);
        } catch (FontFormatException e) {
            System.out.println("Error reading font file.");
        } catch (IOException e) {
            System.out.println("File not found!");
        }
    }

    /**
     * Override of the draw method to render statistics on the screen.
     *
     * @param g The Graphics object used for drawing.
     */
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(font);
        g2.setColor(Color.WHITE);

        // Draw FPS
        g2.drawString("FPS: " + fps, getX(), getY());

        // Draw Score at the center of the window
        g2.drawString("Score: " + score, (int) windowSize.x / 2, getY());
    }

    /**
     * Set the frames per second value for the StatsCounter.
     *
     * @param fps The new frames per second value.
     */
    public void setFPS(int fps) {
        if (updateT >= 20) {
            this.fps = fps;
            updateT = 0;
        }
    }

    /**
     * Increase the score when an entity is eaten.
     */
    public void addEaten() {
        score += 10;
    }

    public void addGhost() {
        score += 100;
    }

    /**
     * Override of the update method to track the time since the last update.
     *
     * @param deltaT The time elapsed since the last update.
     */
    @Override
    public void update(double deltaT) {
        updateT += deltaT;
    }
}
