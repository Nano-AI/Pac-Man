public class Rect {
    public Vector2 size;
    public Vector2 pos;

    public Rect(Vector2 pos, Vector2 size) {
        this.pos = pos;
        this.size = size;
    }

    public Rect(int x, int y, int width, int height) {
        this.pos = new Vector2(x, y);
        this.size = new Vector2(width, height);
    }

    public void pad(int width, int height) {
        this.pos.x = (width - this.size.x) / 2;
        this.pos.y = (height - this.size.y) / 2;
    }

    public boolean collides(Rect r) {
        // top intersections
        boolean xOverlap = Utils.inRange(getX(), r.getX(), r.getX() + r.getWidth()) ||
                Utils.inRange(r.getX(), getX(), getX() + getWidth());
        boolean yOverlap = Utils.inRange(getY(), r.getY(), r.getY() + r.getHeight()) ||
                Utils.inRange(r.getY(), getY(), getY() + getHeight());
        return xOverlap && yOverlap;
    }

    public boolean isIn(Rect r) {
        Vector2 topLeft = pos.copy();
        Vector2 topRight = pos.copy().add(new Vector2(getWidth(), 0));
        Vector2 bottomLeft = pos.copy().add(new Vector2(0, getHeight()));
        Vector2 bottomRight = pos.copy().add(getSize());

        Vector2
    }

    public Rect copy() {
        return new Rect(getX(), getY(), getWidth(), getHeight());
    }

    public Rect displace(Vector2 offset) {
        pos.add(offset);
        return this;
    }

    public void moveX(int x) {
        this.pos.x += x;
    }

    public void moveY(int y) {
        this.pos.y += y;
    }

    public int getX() {
        return (int) this.pos.x;
    }

    public int getY() {
        return (int) this.pos.y;
    }

    public int getWidth() {
        return (int) this.size.x;
    }

    public int getHeight() {
        return (int) this.size.y;
    }

    public Vector2 getSize() {
        return this.size;
    }
}
