/**
 * Window class which combines all components of the game into one class to manage everything.
 * @author Aditya B, Ekam S
 * @version 4 December, 2023
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class Window extends JFrame implements KeyListener {
    // keep track of the map as its own class
    private Map map;
    // arraylist/variables of things to render and objects in the game
    private ArrayList<Entity> entities;
    private ArrayList<Entity> walls;
    private ArrayList<Entity> food;
    private ArrayList<Ghost> ghosts;
    private ArrayList<Grid> grids;
    private StatsCounter statsCounter;
    Player player;
    // variables to keep track of window config things
    private int pixelPerHorizontalGrid, pixelPerVerticalGrid;
    private double deltaT;
    private Image bufferImage;
    private Graphics2D buffer;

    /**
     * Constructor of the Window class.
     * @param title - The title of the window
     * @param m - The map of the game provided as a Map object
     */
    public Window(String title, Map m) {
        // super jframe class
        super(title);
        // intialize class vars
        this.map = m;
        entities = new ArrayList<>();
        food = new ArrayList<>();
        ghosts = new ArrayList<>();
        grids = new ArrayList<>();

        // set title
        setTitle(title);

        // window setup
        display();
        setupPoints();
        setupEntities();

        // setup a bufferimage to remove screen flickering
        bufferImage = createImage(getWidth(), getHeight());
        // set the buffer
        buffer = (Graphics2D) bufferImage.getGraphics();
        // disable antialiasing to remove blurry photos
        buffer.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

//        setupGridPos();

        // add key listeners for key press events
        addKeyListener(this);
    }

    /**
     * Sets up all the different entities in teh game
     * @see Entity
     */
    private void setupEntities() {
        // setup each thing
        setupPlayer();
        setupStats();
        setupFood();
        setupGhosts();
        setupGrid();

        // iterate through all the entities and set the grid size;
        // TODO: might have to remove this
        for (Entity e : entities) {
            e.setGridSize(new Vector2(pixelPerHorizontalGrid, pixelPerVerticalGrid));
        }
    }

    private void setupGrid() {
        for (int i = 0; i < map.height; i++) {
            for (int j = 0; j < map.width; j++) {
                Vector2 p = map.getPoint(i, j);
                Grid g = new Grid((int) p.x, (int) p.y, j, i, pixelPerHorizontalGrid, pixelPerVerticalGrid);
                g.map = map;
                g.player = player;
                g.gridPos = new Vector2(i, j);
                grids.add(g);
                addEntity(g);
            }
        }

        // set grids for all entities
        for (Entity e : entities) {
            e.grids = grids;
        }
    }

    /**
     * Sets up the ghosts in the game.
     * @see Ghost
     */
    private void setupGhosts() {
        // create an array list of spawn points
        ArrayList<Vector2> spawns = new ArrayList<>();
        // iterate through map and set spawns
        for (int i = 0; i < map.height; i++) {
            for (int j = 0; j < map.width; j++) {
                if (map.at(i, j) == 'B') {
                    spawns.add(new Vector2(i, j));
//                    emptySpaces.add(new Vector2(i, j));
                }
            }
        }

        // get the pixel spot of spawn location
        Vector2 pixelSpot = map.getPoint((int) spawns.get(0).x, (int) spawns.get(0).y);
        // create a ghost at that position
        Ghost g = new Ghost((int) pixelSpot.x, (int) pixelSpot.y, (int) spawns.get(0).x, (int) spawns.get(0).x, pixelPerHorizontalGrid, pixelPerVerticalGrid);
        g.hitbox = new Rect(0, 0, pixelPerHorizontalGrid, pixelPerVerticalGrid);
        ghosts.add(g);

        for (Ghost gh : ghosts) {
            gh.player = this.player;
            gh.map = this.map;
            addEntity(gh);
        }
    }

    /**
     * Sets up a stat counter for FPS and Score
     * @see StatsCounter
     */
    private void setupStats() {
        // set it up
        statsCounter = new StatsCounter(getInsets().left + 10, getInsets().top + 32, 0, 0);
        statsCounter.windowSize = new Vector2(getWidth(), getHeight());

        // add as an entity to render
        addEntity(statsCounter);
    }

    /**
     * Sets up food by setting players and counters
     * @see Food
     */
    private void setupFood() {
        // iterate through all food and set the player and counter of each one as reference
        for (Entity f : food) {
            ((Food) f).player = player;
            ((Food) f).counter = statsCounter;
        }
    }

    /**
     * Sets up the player
     * @see Player
     */
    private void setupPlayer() {
        // get an array list of empty spaces
        ArrayList<Vector2> emptySpaces = new ArrayList<>();
        for (int i = 0; i < map.height; i++) {
            for (int j = 0; j < map.width; j++) {
                if (map.at(i, j) == '.') {
                    emptySpaces.add(new Vector2(i, j));
                }
            }
        }

        // get a random empty position on the map
        // this is in terms of array coordintes
        Vector2 spawnGrid = emptySpaces.get((int) (Math.random() * emptySpaces.size()));
        // get the vector2 point as pixel coordinates
        Vector2 spawn = map.getPoint((int) spawnGrid.x, (int) spawnGrid.y);
        // setup the player
        player = new Player(
                (int) spawn.x,
                (int) spawn.y,
                pixelPerHorizontalGrid,
                pixelPerVerticalGrid
        );

        // set the player's pos through the map
//        player.setGridPos();
        // setup the player hitbox
        Rect playerRect = new Rect(0, 0, pixelPerHorizontalGrid, pixelPerVerticalGrid);
        // add some padding
        playerRect.pad(player.getWidth(), player.getHeight());
        // setup class variables
        player.setHitbox(playerRect);
        player.setMap(map);
        player.setWalls(walls);
//        player.setGridPos(spawn);
        player.setupImages("./img/pacman");

        // add as an entity to render
        addEntity(player);
    }

    /**
     * Adds an entity to the render list
     * @param e Add an entity to the render list
     * @see Entity
     */
    public void addEntity(Entity e) {
        entities.add(e);
    }

    /**
     * Displays the window
     * @see JPanel
     */
    public void display() {
        // set it double buffered
        ((JPanel) getContentPane()).setDoubleBuffered(true);
        // unresizeable
        setResizable(false);
        // exit on close
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // set size
        setPreferredSize(new Dimension(800, 800));
        // add everything
        pack();
        // make it visible
        setVisible(true);
    }

    /**
     * Renders everything onto the scene. Done with Buffer to prevent screen flickering
     * @param hitbox Display the hitbox
     */
    public void render(boolean hitbox) {
        // drawing to buffer to stop flickering
        Graphics2D g = (Graphics2D) getGraphics();
        // paint to the buffer
        paint(buffer, hitbox);
        // and draw the content of the buffer onto the screen
        g.drawImage(bufferImage, 0, 0, null);
    }

    /**
     * Update function that updates all game logic.
     * @param delta Delta time between previous and current render
     */
    public void update(double delta) {
        // set delta t
        this.deltaT = delta;

        // update all entities
        updateFood(food);

        updateList(entities);
        updateList(walls);
        updateList(food);

        map.clearGrid();
        map.update(entities);
    }

    /**
     * Removes food if it's been eaten.
     * @param e The ArrayList of food.
     * @see Food
     */
    private void updateFood(ArrayList<Entity> e) {
        int i = 0;
        while (i < e.size()) {
            if (e.get(i) instanceof Food f) {
                if (f.isEaten()) {
                    e.remove(i);
                    i--;
                }
            }
            i++;
        }
    }

    /**
     * Updates the Delta-T for each Entity inside the ArrayList
     * @param l The ArrayList to update Delta-T
     * @see Entity
     */
    private void updateList(ArrayList<Entity> l) {
        for (Entity e : l) {
            e.update(this.deltaT);
        }
    }

    /**
     * Paint all entities onto the buffer.
     * @param g The graphics of the Buffer.
     * @param hitbox - Display hitbox
     */
    public void paint(Graphics2D g, boolean hitbox) {
        // reset screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        // paint everything in each arraylist
        paintList(g, walls, hitbox);
        paintList(g, food, hitbox);
        paintList(g, entities, hitbox);
    }

    /**
     * Paint each entity in the arraylist
     * @param g Graphics2D of the Buffer
     * @param l The ArrayList to draw
     * @param hitbox - Draw hitbox
     * @see Entity
     */
    private void paintList(Graphics2D g, ArrayList<Entity> l, boolean hitbox) {
        // iterate through each entity
        for (Entity e : l) {
            // draw the entity
            e.draw(g);
            // draw hitbox if provided as yes
            if (hitbox) {
                g.setColor(Color.GREEN);
                // try to draw; if hitbox is null just continue
                try {
                    g.drawRect(e.getX() + e.hitbox.getX(), e.getY() + e.hitbox.getY(), e.hitbox.getWidth(), e.hitbox.getHeight());
                } catch (NullPointerException err) {
                    continue;
                }
            }
        }
    }

    /**
     * Setup points from a grid (x, y) to a screen-pixel (x, y).
     * @see Map
     */
    private void setupPoints() {
        // setup the walls
        walls = new ArrayList<Entity>();
        // get the contianer of the screen
        Container pane = getContentPane();
        // get the number of pixels per grid size
        pixelPerHorizontalGrid = pane.getWidth() / map.width;
        pixelPerVerticalGrid = pane.getHeight() / map.height;

        // set each to the minimum of both to allow for square grids
        pixelPerVerticalGrid = Math.min(pixelPerHorizontalGrid, pixelPerVerticalGrid);
        pixelPerHorizontalGrid = Math.min(pixelPerHorizontalGrid, pixelPerVerticalGrid);

        // setup the x and y offset to center the board
        int xOffset = (getWidth() - pixelPerHorizontalGrid * map.width) / 2;
        int yOffset = (getHeight() - pixelPerVerticalGrid * map.height) / 2;

        // set the map's sizing accordingly
        map.pixelPerHorizontalGrid = pixelPerHorizontalGrid;
        map.pixelPerVerticalGrid = pixelPerVerticalGrid;

        // iterate through everything in the grid
        for (int i = 0; i < map.width; i++) {
            for (int j = 0; j < map.height; j++) {
                // calculate the x and y location of each grid spot
                int xPos = xOffset + i * pixelPerHorizontalGrid;
                int yPos = yOffset + j * pixelPerVerticalGrid;
                // check if the current position is a wall
                if (map.at(j, i) == 'W') {
                    // add the wall to the list of walls if so
                    Wall e = new Wall(xPos, yPos, pixelPerHorizontalGrid, pixelPerVerticalGrid);
                    e.setHitbox(new Rect(0, 0, pixelPerHorizontalGrid, pixelPerVerticalGrid));
                    walls.add(e);
                    // set the base grid
                    map.baseGrid[j][i] = 'W';
                } else {
                    // setup food size
                    int foodWidth = pixelPerHorizontalGrid / 4;
                    int foodHeight = pixelPerVerticalGrid / 4;
                    // setup food's hitbox offset
                    int foodXOffset = (pixelPerHorizontalGrid - foodWidth) / 2;
                    int foodYOffset = (pixelPerVerticalGrid - foodHeight) / 2;
                    // setup the food
                    Food f = new Food(foodXOffset + xPos, foodYOffset + yPos,
                            foodWidth, foodHeight);
                    // setup the food's hitbox
                    f.setHitbox(new Rect(foodWidth / 4, foodHeight / 4, foodWidth / 2, foodHeight / 2));
                    // add the food
                    food.add(f);
                }
                // set the point of the grid to the vector2 position
                map.setPoint(new Vector2(xPos, yPos), j, i);
            }
        }
    }

    /**
     * Set the FPS of the game
     * @param n FPS
     */
    public void setFPS(int n) {
        statsCounter.setFPS(n);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * Sets the direction to the wanted direction when player presses the respective button down
     * @param e The event to be processed
     * @see Player
     * @see KeyListener
     */
    @Override
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> player.setWantedDirection('e');
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> player.setWantedDirection('w');
            case KeyEvent.VK_UP , KeyEvent.VK_W-> player.setWantedDirection('n');
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> player.setWantedDirection('s');
            case KeyEvent.VK_P -> System.out.println(map.toString());
            default -> {
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
