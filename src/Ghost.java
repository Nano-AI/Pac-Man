import java.awt.*;

public class Ghost extends Entity {
    private Vector2 nextPos;

    public Ghost(int x, int y, int gridX, int gridY, int width, int height) {
        super(x, y, gridX, gridY, width, height);
    }

    @Override
    public void update(double deltaT) {

    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Vector2 p = getPos();
        Vector2 size = getSize();

        g2.drawImage(getImage(), (int) p.x, (int) p.y, (int) size.x, (int) size.y, null);
    }
}
