import java.awt.*;

public class Entity {
    private Vector2 pos;
    private Vector2 velocity;
    private Vector2 grid;
    private int width, height;
    private Vector2 direction;

    public Entity(int x, int y, int gridX, int gridY, int width, int height) {
        this.pos = new Vector2(x, y);
        this.grid = new Vector2(gridX, gridY);
        this.width = width;
        this.height = height;
    }

    public Entity(int x, int y, int width, int height) {
        this.pos = new Vector2(x, y);
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return (int) this.pos.x;
    }

    public int getY() {
        return (int) this.pos.y;
    }

    public Vector2 getPos() {
        return this.pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public void setX(int x) {
        this.pos.x = x;
    }

    public void setY(int y) {
        this.pos.y = y;
    }

    public void addX(int x) {
        this.pos.x += x;
    }

    public void addY(int y) {
        this.pos.y += y;
    }

    public void setGridX(int x) {
        this.grid.x = x;
    }

    public void setGridY(int y) {
        this.grid.y = y;
    }

    public Vector2 getGrid() {
        return this.grid;
    }

    public void addPos(Vector2 pos) {
        this.pos.add(pos);
    }

    public Vector2 getDirection() {
        return direction;
    }

    public void setVelocity(double x, double y) {
        this.velocity = new Vector2(x, y);
    }

    public void setVelocityX(double v) {
        this.velocity.x = v;
    }

    public void setVelocityY(double v) {
        this.velocity.y = v;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setDirection(char d) {
        int x = 0;
        int y = 0;
        switch (Character.toLowerCase(d)) {
            case 'n', 'u' -> y = -1;
            case 'e', 'r' -> x = 1;
            case 's', 'd' -> y = 1;
            case 'w', 'l' -> x = -1;
            case '0' -> x = 0;
            default -> {
                return;
            }
        }
        direction = new Vector2(x, y);
        System.out.println(direction);
    }

    public void setDirection(Vector2 p) {
        direction = p.normalize();
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void update(double deltaT) {

    }

    public void draw(Graphics g) {

    }
}
