/**
 * The Wall class represents a wall entity in the game.
 * It extends the Entity class and includes a method to draw the wall.
 *
 * @author Aditya B, Ekam S
 * @version 26 November, 2023
 */
import java.awt.*;
import java.awt.image.BufferedImage;

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
    private BufferedImage wallImage;
    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height);
        mapChar = 'W';
        this.hitbox = new Rect(0, 0, width, height);
    }

    public void setWallImage(BufferedImage img) {
        this.wallImage = img;
    }

    /**
     * Draw the wall on the specified Graphics object.
     *
     * @param g The Graphics object on which to draw the wall.
     */
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.BLUE);
        if (wallImage != null)
            g2.drawImage(wallImage, getX(), getY(), getWidth(), getHeight(), null);
        else {
            g.setColor(Color.PINK);
            g.fillRect(getX(), getY(), getWidth(), getHeight());
        }
    }
}
