import java.awt.*;
import java.util.ArrayList;

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

    private ArrayList<Vector2> collisions;

    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g.setColor(Color.YELLOW);
        g.drawRect(getX(), getY(), getWidth(), getHeight());

        Stroke old = g2.getStroke();
//        for (Vector2 e : collisions) {
//            g2.setStroke(new BasicStroke(2));
//            g2.setColor(Color.GREEN);
//            g2.fillRect((int) e.x, (int) e.y, m.pixelPerHorizontalGrid, m.pixelPerVerticalGrid);
//        }
//        g2.setStroke(old);
    }

    @Override
    public void update(double deltaT) {

//        Vector2 newPos = getPos().copy().add(getDirection().copy().multiply(0.8 * deltaT));
//        m.getPossibleDirections(getPos());
//        collisions = m.collides(newPos, 'W');
//        if (collisions.isEmpty()) {
//            setPos(newPos);
//        } else {
//        }
    }
}
