import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Window extends JFrame implements KeyListener {
    private Map map;
    private ArrayList<Entity> entities;
    private ArrayList<Entity> walls;
    private ArrayList<Entity> food;

    private int pixelPerHorizontalGrid, pixelPerVerticalGrid;
    Player player;
    private double deltaT;
    private Image bufferImage;
    private Graphics2D buffer;
    private int fps;
    private StatsCounter statsCounter;

    public Window(String title, Map m) {
        super(title);
        this.map = m;
        entities = new ArrayList<>();
        food = new ArrayList<>();

        setTitle(title);

        display();
        setupPoints();
        setupEntities();

        bufferImage = createImage(getWidth(), getHeight());
        buffer = (Graphics2D) bufferImage.getGraphics();

        addKeyListener(this);
    }


    private void setupEntities() {
        statsCounter = new StatsCounter(getInsets().left + 10, getInsets().top + 32, 0, 0);
        statsCounter.windowSize = new Vector2(getWidth(), getHeight());
        Vector2 p = map.getPoint(1, 1);

        ArrayList<Vector2> emptySpaces = new ArrayList<>();
        for (int i = 0; i < map.height; i++) {
            for (int j = 0; j < map.width; j++) {
                if (map.at(i, j) == '.') {
                    emptySpaces.add(new Vector2(i, j));
                }
            }
        }

        Vector2 spawnGrid = emptySpaces.get((int) (Math.random() * emptySpaces.size()));
        Vector2 spawn = map.getPoint((int) spawnGrid.x, (int) spawnGrid.y);

        player = new Player(
                (int) spawn.x,
                (int) spawn.y,
                pixelPerHorizontalGrid,
                pixelPerVerticalGrid
        );


        Rect playerRect = new Rect(0, 0, pixelPerHorizontalGrid, pixelPerVerticalGrid);
        playerRect.pad(player.getWidth(), player.getHeight());
        player.setHitbox(playerRect);
        player.setMap(map);
        player.setWalls(walls);
        player.setGridPos(spawn);
        player.setupImages("./img/pacman");

        entities.add(player);

        entities.add(statsCounter);

        for (Entity f : food) {
            ((Food) f).player = player;
            ((Food) f).counter = statsCounter;
        }


        for (Entity e : entities) {
            e.setGridSize(new Vector2(pixelPerHorizontalGrid, pixelPerVerticalGrid));
        }
    }

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void display() {
        ((JPanel) getContentPane()).setDoubleBuffered(true);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 800));
        pack();
        setVisible(true);
    }

    public void render(boolean hitbox) {
        // drawing to buffer to stop flickering
        Graphics2D g = (Graphics2D) getGraphics();
        paint(buffer, hitbox);
        g.drawImage(bufferImage, 0, 0, null);
    }

    public void update(double delta) {
        this.deltaT = delta;

        updateList(entities);
        updateList(walls);
        updateList(food);
    }

    private void updateList(ArrayList<Entity> l) {
        for (Entity e : l) {
            e.update(this.deltaT);
        }
    }

    public void paint(Graphics2D g, boolean hitbox) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        paintList(g, walls, hitbox);
        paintList(g, food, hitbox);
        paintList(g, entities, hitbox);
    }

    private void paintList(Graphics2D g, ArrayList<Entity> l, boolean hitbox) {
        for (Entity e : l) {
            e.draw(g);
            if (hitbox) {
                g.setColor(Color.GREEN);
                try {
                    g.drawRect(e.getX() + e.hitbox.getX(), e.getY() + e.hitbox.getY(), e.hitbox.getWidth(), e.hitbox.getHeight());
                } catch (NullPointerException err) {
                    continue;
                }
            }
        }
    }

    private void setupPoints() {
        walls = new ArrayList<Entity>();
        Container pane = getContentPane();
        pixelPerHorizontalGrid = pane.getWidth() / map.width;
        pixelPerVerticalGrid = pane.getHeight() / map.height;

        pixelPerVerticalGrid = Math.min(pixelPerHorizontalGrid, pixelPerVerticalGrid);
        pixelPerHorizontalGrid = Math.min(pixelPerHorizontalGrid, pixelPerVerticalGrid);

        int xOffset = (getWidth() - pixelPerHorizontalGrid * map.width) / 2;
        int yOffset = (getHeight() - pixelPerVerticalGrid * map.height) / 2;

        map.pixelPerHorizontalGrid = pixelPerHorizontalGrid;
        map.pixelPerVerticalGrid = pixelPerVerticalGrid;

        for (int i = 0; i < map.width; i++) {
            for (int j = 0; j < map.height; j++) {
                int xPos = xOffset + i * pixelPerHorizontalGrid;
                int yPos = yOffset + j * pixelPerVerticalGrid;
                if (map.at(j, i) == 'W') {
                    Wall e = new Wall(xPos, yPos, pixelPerHorizontalGrid, pixelPerVerticalGrid);
                    e.setHitbox(new Rect(0, 0, pixelPerHorizontalGrid, pixelPerVerticalGrid));
                    walls.add(e);
                } else {
                    int foodWidth = pixelPerHorizontalGrid / 4;
                    int foodHeight = pixelPerVerticalGrid / 4;
                    int foodXOffset = (pixelPerHorizontalGrid - foodWidth) / 2;
                    int foodYOffset = (pixelPerVerticalGrid - foodHeight) / 2;
                    Food f = new Food(foodXOffset + xPos, foodYOffset + yPos,
                            foodWidth, foodHeight);
                    f.setHitbox(new Rect(foodWidth / 4, foodHeight / 4, foodWidth / 2, foodHeight / 2));
                    food.add(f);
                }
                map.setPoint(new Vector2(xPos, yPos), j, i);
            }
        }
    }

    public void setFPS(int n) {
        statsCounter.setFPS(n);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> player.setWantedDirection('e');
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> player.setWantedDirection('w');
            case KeyEvent.VK_UP , KeyEvent.VK_W-> player.setWantedDirection('n');
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> player.setWantedDirection('s');
            default -> {
            }
        }
    }

//    public void setTitle(String t) {
//
//    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
