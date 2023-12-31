/**
 * The Food class represents an entity in the game that can be consumed by the player.
 * Extends the Entity class.
 *
 * @author Aditya B, Ekam S
 * @version 26 November, 2023
 */

import java.awt.*;

public class Food extends Entity {

    public Player player; // Reference to the player entity
    private boolean eaten = false; // Flag to track whether the food has been eaten
    private boolean fruit = false;

    public void setFruit() {
        this.fruit = true;
    }

    public boolean isFruit() {
        return this.fruit;
    }

    /**
     * Constructor for Food class with specified parameters.
     *
     * @param x      The x-coordinate of the entity's position.
     * @param y      The y-coordinate of the entity's position.
     * @param width  The width of the entity.
     * @param height The height of the entity.
     */
    public Food(int x, int y, int width, int height) {
        super(x, y, width, height);
        mapChar = '.';
    }

    /**
     * Override of the draw method to render the food on the screen if it hasn't been eaten.
     *
     * @param g The Graphics object used for drawing.
     */
    @Override
    public void draw(Graphics g) {
        if (!eaten) {
            g.setColor(Color.YELLOW);
            if (isFruit()) {
                g.fillOval(getX() - getWidth() / 2, getY() - getHeight() / 2, getWidth() * 2, getHeight() * 2);
            } else {
                g.fillRect(getX(), getY(), getWidth(), getHeight());
            }
        }
    }

    /**
     * Override of the update method to check if the food is touched by the player and update the stats counter accordingly.
     *
     * @param deltaT The time elapsed since the last update.
     */
    @Override
    public void update(double deltaT) {
        if (isTouching(player) && !eaten) {
            if (isFruit()) {
                getAudioPlayer().playSound("gs_eatfruit");
                player.setAngry();
                for (Ghost g : getGhosts()) {
                    g.setMode(Ghost.Mode.SCARED);
                }
                getStatsCounter().addFruitEaten();
            } else {
                getStatsCounter().addEaten();
                getAudioPlayer().playSound("gs_chomp");
            }
            eaten = true;
        }
    }

    /**
     * Check if the food has been eaten.
     *
     * @return True if the food has been eaten, false otherwise.
     */
    public boolean isEaten() {
        return this.eaten;
    }
}
