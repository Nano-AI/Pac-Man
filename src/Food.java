import java.awt.*;

public class Food extends Entity {
    public Food(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void update(double deltaT) {

    }
}
