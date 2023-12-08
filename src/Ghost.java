/**
 * The ghost class that is the enemy to the pacman
 *
 * @author Aditya B, Ekam S
 * @version 4 December, 2023
 */

import java.awt.*;

public class Ghost extends Entity {
    private Vector2 nextPos;
    public Player player;
    double detectionRadius;

    public Ghost(int x, int y, int gridX, int gridY, int width, int height) {
        super(x, y, gridX, gridY, width, height);
        System.out.println(getWidth() + " " + getHeight());
        setupImages("./img/ghost");

        this.detectionRadius = 120f;
    }

    @Override
    public void update(double deltaT) {
//        if (getDistanceTo(player) < detectionRadius) {
//            addPos(player.getVectorDistance(this).multiply(0.05 * deltaT));
//        }
        updateGridSpot();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Vector2 p = getPos();
        Vector2 size = getSize();

        g.setColor(Color.green);
        g2.drawRect(getX(), getY(), getWidth(), getHeight());
        g2.fillRect((int) getGridPos().x, (int) getGridPos().y, getWidth(), getHeight());
        g2.drawImage(getImage(), (int) p.x, (int) p.y, (int) size.x, (int) size.y, null);
    }
}
