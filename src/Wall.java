/**
 * The Wall class represents a wall entity in the game.
 * It extends the Entity class and includes a method to draw the wall.
 *
 * @author Aditya B, Ekam S
 * @version 26 November, 2023
 */
import java.awt.*;

public class Wall extends Entity {
    private Map m;

    /**
     * Constructor for Wall with specified position, width, and height.
     *
     * @param x      The x-coordinate of the wall's position.
     * @param y      The y-coordinate of the wall's position.
     * @param width  The width of the wall.
     * @param height The height of the wall.
     */
    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height);
        mapChar = 'W';
        this.hitbox = new Rect(0, 0, width, height);
    }

    /**
     * Draw the wall on the specified Graphics object.
     *
     * @param g The Graphics object on which to draw the wall.
     */
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}
