import java.awt.*;

public class Entity {
    private Vector2 pos;
    private Vector2 velocity;
    private Vector2 grid;
    private int width, height;
    private Vector2 direction;
    private Vector2 gridPos;
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
        Rect d1 = hitbox.copy().displace(this.pos);
        Rect d2 = e.hitbox.copy().displace(e.pos);
        return d1.collides(d2);
    }

    public void setGridPos(Vector2 pos) {
        this.gridPos = pos;
    }

    public Vector2 getGridPos() {
        return this.gridPos.copy();
    }

    public boolean isIn(Entity e) {
        Rect d1 = hitbox.copy().displace(this.pos);
        Rect d2 = e.hitbox.copy().displace(e.pos);
        return d1.isIn(d2);
    }

    public Vector2 getHitboxCenter() {
        return getCenter(hitbox.pos, hitbox.size);
    }

    public Vector2 getCenter() {
        return getCenter(getPos(), getSize());
    }

    private Vector2 getCenter(Vector2 pos, Vector2 size) {
        return getPos().copy().add(getSize().copy().multiply(0.5));
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

    public void setGridSize(Vector2 size) {
        gridSize = size;
    }

    public Vector2 getDirection() {
        return direction.copy();
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

    public Vector2 getSize() {
        return new Vector2(this.width, this.height);
    }

    public void update(double deltaT) {

    }

    public void draw(Graphics g) {

    }
}
