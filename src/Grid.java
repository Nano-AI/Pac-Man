/**
 * Class meant to keep track of each grid space in the game.
 * @author Aditya Bankoti, Ekam Singh
 * @version 4 December, 2023
 */

import java.awt.*;
public class Grid extends Entity {
    Map map;
    Player player;
    public Vector2 gridPos;
    public Grid(int x, int y, int gridX, int gridY, int width, int height) {
        super(x, y, gridX, gridY, width, height);
        this.gridPos = new Vector2(gridX, gridY);
    }

    @Override
    public void draw(Graphics g) {
    }

    @Override
    public void update(double deltaT) {
    }
}
