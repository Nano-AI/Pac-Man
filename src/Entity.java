import java.awt.*;

public class Entity {
    private Vector2 pos;
    private Vector2 velocity;
    private Vector2 grid;
    private int width, height;
    private Vector2 direction;
    private Vector2 gridSize;

    public Rect hitbox;

    public Entity(int x, int y, int gridX, int gridY, int width, int height) {
        this.pos = new Vector2(x, y);
        this.grid = new Vector2(gridX, gridY);
        this.hitbox.size = new Vector2(width, height);
    }

    public void setHitbox(Rect r) {
        this.hitbox = r;
    }

    public boolean isTouching(Entity e) {
        return hitbox.collides(e.hitbox);
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
        this.hitbox.pos = pos;
    }

    public void setGridSize(Vector2 size) {
        gridSize = size;
    }

    public Vector2 getDirection() {
        return direction;
    }
    public void setDirection(char d) {
        direction = Utils.getDirection(d);
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
