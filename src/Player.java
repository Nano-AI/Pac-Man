import java.awt.*;

public class Player extends Entity {
    private Map m;
    private Vector2 prevDirection;
    public Player(int x, int y, int width, int height) {
        super(x, y, width, height);
        setDirection('0');
    }

    public void setMap(Map m) {
        this.m = m;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.drawOval(getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void update(double deltaT) {
        Vector2 newPos = getPos().copy().add(getDirection().copy().multiply(0.8 * deltaT));
        if (m.collides(newPos, 'W').isEmpty()) {
            setPos(newPos);
        }
    }
}
