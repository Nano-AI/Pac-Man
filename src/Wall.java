import java.awt.*;

public class Wall extends Entity {
    private Map m;

    public Wall(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.hitbox = new Rect(0, 0, width, height);
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.BLUE);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }
}
