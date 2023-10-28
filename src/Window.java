import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Window extends JFrame implements KeyListener {
    private Map map;
    private ArrayList<Entity> entities;

    private int pixelPerHorizontalGrid, pixelPerVerticalGrid;
    Player player;
    private double deltaT;
    private Image bufferImage;
    private Graphics2D buffer;
    public Window(String title, Map m) {
        super(title);
        this.map = m;
        display();
        setupPoints();
        setupEntities();

        bufferImage = createImage(getWidth(), getHeight());
        buffer = (Graphics2D) bufferImage.getGraphics();

        addKeyListener(this);
    }

    private void setupEntities() {
        entities = new ArrayList<>();
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
        player.setMap(map);
        entities.add(player);
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

    public void render() {
        // drawing to buffer to stop flickering
        Graphics2D g = (Graphics2D) getGraphics();
        paint(buffer);
        g.drawImage(bufferImage, 0, 0, null);
    }

    public void update(double delta) {
        this.deltaT = delta;

        for (Entity e : entities) {
            e.update(deltaT);
        }
    }

    private void setupPoints() {
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
                map.setPoint(new Vector2(xPos, yPos), j, i);
            }
        }
    }

    private void drawGrid(Graphics g) {
        for (int i = 0; i < map.width; i++) {
            for (int j = 0; j < map.height; j++) {
                g.setColor(Color.WHITE);
                int xPos = (int) map.getPoint(j, i).x;
                int yPos = (int) map.getPoint(j, i).y;
                int xSize = pixelPerHorizontalGrid;
                int ySize = pixelPerVerticalGrid;

                Color c;

                switch (map.at(j, i)) {
                    case 'W':
                        c = Color.BLUE;
                        break;
                    case '.':
                        c = Color.YELLOW;
                        xPos += xSize / 3;
                        yPos += ySize / 3;
                        xSize /= 3;
                        ySize /= 3;
                        break;
                    default:
                        c = Color.BLACK;
                        break;
                }

                g.setColor(c);

                g.fillRect(xPos,
                        yPos,
                        xSize,
                        ySize
                );
            }
        }
    }

    public void paint(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        drawGrid(g);

        for (Entity e : entities) {
            e.draw(g);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        switch (key) {
            case (KeyEvent.VK_RIGHT) -> player.setDirection('e');
            case (KeyEvent.VK_LEFT) -> player.setDirection('w');
            case (KeyEvent.VK_UP) -> player.setDirection('n');
            case (KeyEvent.VK_DOWN) -> player.setDirection('s');
            default -> {
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
