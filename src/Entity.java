/**
 * The entity class which keeps track of each renderable thing in the game.
 *
 * @author Aditya B, Ekam S
 * @version 4 December, 2023
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.sql.Array;
import java.util.ArrayList;

public class Entity {

    // The position of the entity
    private Vector2 pos; // Position of the entity
    private int width, height; // Width and height of the entity
    private Vector2 grid, gridSize; // Grid position and grid size of the entity
    private Vector2 direction; // Direction of the entity
    private Vector2 gridPos; // Grid position of the entity

    private BufferedImage[] images; // Array of images for the entity

    public Rect hitbox; // Rectangle representing the hitbox of the entity

    public Map m;

    private int frameIndex = 0; // Index of the current frame for animation

    // arraylist to keep track of grids
    public ArrayList<Grid> grids;

    /**
     * Get the array of images associated with this entity.
     *
     * @return The array of images.
     */
    public BufferedImage[] getImages() {
        return this.images;
    }

    /**
     * Get a specific image from the array of images.
     *
     * @param index The index of the image to retrieve.
     * @return The BufferedImage at the specified index.
     */
    public BufferedImage getImage(int index) {
        return this.images[index];
    }

    /**
     * Get the current image based on the frame index.
     *
     * @return The current BufferedImage.
     */
    public BufferedImage getImage() {
        return this.images[frameIndex];
    }

    /**
     * Increment the frame index for animation and return the previous image.
     *
     * @return The BufferedImage of the previous frame.
     */
    public BufferedImage incrementFrameIndex() {
        BufferedImage prev = getImage(frameIndex);
        frameIndex++;
        if (frameIndex >= images.length) {
            frameIndex = 0;
        }
        return prev;
    }

    /**
     * Set up images for the entity based on the given path.
     *
     * @param path The path to load images from.
     */
    public void setupImages(String path) {
        this.images = Utils.getImages(path);
    }

    /**
     * Constructor for the Entity class with specified parameters.
     *
     * @param x      The x-coordinate of the entity's position.
     * @param y      The y-coordinate of the entity's position.
     * @param gridX  The x-coordinate of the entity's grid position.
     * @param gridY  The y-coordinate of the entity's grid position.
     * @param width  The width of the entity.
     * @param height The height of the entity.
     */
    public Entity(int x, int y, int gridX, int gridY, int width, int height) {
        this.pos = new Vector2(x, y);
        this.gridPos = new Vector2(gridX, gridY);
        this.hitbox = new Rect(0, 0, width, height);
        this.width = width;
        this.height = height;
        this.hitbox.size = new Vector2(width, height);
    }

    /**
     * Set the hitbox of the entity.
     *
     * @param r The new hitbox.
     */
    public void setHitbox(Rect r) {
        this.hitbox = r;
    }

    /**
     * Check if this entity is touching another entity based on hitbox collision.
     *
     * @param e The other entity to check for collision.
     * @return True if the entities are touching, false otherwise.
     */
    public boolean isTouching(Entity e) {
        Rect d1 = hitbox.copy().displace(this.pos);
        Rect d2 = e.hitbox.copy().displace(e.pos);
        return d1.collides(d2);
    }

    /**
     * Set the grid position of the entity.
     *
     * @param pos The new grid position.
     */
    public void setGridPos(Vector2 pos) {
        this.gridPos = pos;
    }

    /**
     * Get a copy of the grid position of the entity.
     *
     * @return A copy of the grid position.
     */

    /**
     * Check if this entity is completely inside another entity based on hitbox inclusion.
     *
     * @param e The other entity to check for inclusion.
     * @return True if this entity is completely inside the other, false otherwise.
     */
    public boolean isIn(Entity e) {
        Rect d1 = hitbox.copy().displace(this.pos);
        Rect d2 = e.hitbox.copy().displace(e.pos);
        return d1.isIn(d2);
    }

    /**
     * Get the center of the hitbox.
     *
     * @return The center of the hitbox.
     */
    public Vector2 getHitboxCenter() {
        return getCenter(hitbox.pos, hitbox.size);
    }

    /**
     * Get the center of the entity.
     *
     * @return The center of the entity.
     */
    public Vector2 getCenter() {
        return getCenter(getPos(), getSize());
    }

    /**
     * Get the center of a rectangle given its position and size.
     *
     * @param pos  The position of the rectangle.
     * @param size The size of the rectangle.
     * @return The center of the rectangle.
     */
    private Vector2 getCenter(Vector2 pos, Vector2 size) {
        return getPos().copy().add(getSize().copy().multiply(0.5));
    }

    /**
     * Alternate constructor for the Entity class with specified parameters.
     *
     * @param x      The x-coordinate of the entity's position.
     * @param y      The y-coordinate of the entity's position.
     * @param width  The width of the entity.
     * @param height The height of the entity.
     */
    public Entity(int x, int y, int width, int height) {
        this.pos = new Vector2(x, y);
        this.width = width;
        this.height = height;
    }

    /**
     * Get the x-coordinate of the entity's position.
     *
     * @return The x-coordinate of the entity's position.
     */
    public int getX() {
        return (int) this.pos.x;
    }

    /**
     * Get the y-coordinate of the entity's position.
     *
     * @return The y-coordinate of the entity's position.
     */
    public int getY() {
        return (int) this.pos.y;
    }

    /**
     * Get a copy of the position vector of the entity.
     *
     * @return A copy of the position vector.
     */
    public Vector2 getPos() {
        return this.pos.copy();
    }

    /**
     * Set the position vector of the entity.
     *
     * @param pos The new position vector.
     */
    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    /**
     * Set the grid size vector.
     *
     * @param size The new grid size vector.
     */
    public void setGridSize(Vector2 size) {
        gridSize = size;
    }

    /**
     * Get a copy of the direction vector of the entity.
     *
     * @return A copy of the direction vector.
     */
    public Vector2 getDirection() {
        return direction.copy();
    }

    /**
     * Set the direction of the entity based on a character input.
     *
     * @param d The character representing the new direction.
     */
    public void setDirection(char d) {
        direction = Utils.getDirection(d);
    }

    /**
     * Set the direction of the entity based on a vector input.
     *
     * @param p The new direction vector.
     */
    public void setDirection(Vector2 p) {
        direction = p.normalize();
    }

    /**
     * Get the width of the entity.
     *
     * @return The width of the entity.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Get the height of the entity.
     *
     * @return The height of the entity.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Get a copy of the size vector of the entity.
     *
     * @return A copy of the size vector.
     */
    public Vector2 getSize() {
        return new Vector2(this.width, this.height);
    }

    /**
     * Update method to be implemented for specific entity behavior.
     *
     * @param deltaT The time elapsed since the last update.
     */
    public void update(double deltaT) {
        // To be implemented
    }

    /**
     * Draw method to be implemented for specific entity rendering.
     *
     * @param g The Graphics object used for drawing.
     */
    public void draw(Graphics g) {
        // To be implemented
    }

    public double distanceTo(Entity e) {
        return Utils.pythag(getX() - e.getX(), getY() - e.getY());
    }

    /**
     * Updates the grid (array spot) of the player.
     */
    public void updateGridSpot() {
        // store the min dist of the closest grid
        // iterate through all grids
//        double minDist = Double.MAX_VALUE;
        for (Entity w : grids) {
            // get the distance to the wall
            double d = distanceTo(w);
            // check if dist is less than min dist
            if (Utils.inside(d, 0, Utils.pythag(getWidth(), getHeight()) / 2.0)) {
//            if (d < minDist) {
                // updatge
                gridPos = w.getPos();
                return;
//                System.out.println();
//                minDist = d;
            }
        }
    }

    public Vector2 getGridPos() {
        return this.gridPos.copy();
    }
}
