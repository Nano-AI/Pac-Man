import java.awt.*;
import java.io.File;
import java.io.IOException;

public class StatsCounter extends Entity {
    private int fps;
    private double updateT;
    private boolean canDraw;
    private double deltaT;
    private int score;
    private Font font;
    public Vector2 windowSize;
    public StatsCounter(int x, int y, int width, int height) {
        super(x, y, width, height);
        fps = 0;
        updateT = 0;
        score = 0;
        canDraw = false;

        font = new Font("TimesRoman", Font.PLAIN , 32);
        try {
            font = Font.createFont(Font.TRUETYPE_FONT, new File("./img/RetroBoundUC.ttf")).deriveFont(Font.PLAIN, 32);
        } catch (FontFormatException e) {
            System.out.println("Error reading font file.");
        } catch (IOException e) {
            System.out.println("File not found!");
        }
    }
    @Override
    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(font);
        g2.setColor(Color.WHITE);
        g2.drawString("FPS: " + fps, getX(), getY());

        g2.drawString("Score: " + score, (int) windowSize.x / 2, getY());
    }

    public void setFPS(int fps) {
        if (updateT >= 20) {
            this.fps = fps;
            updateT = 0;
        }
    }

    public void addEaten() {
        score += 10;
    }

    @Override
    public void update(double deltaT) {
        updateT += deltaT;
    }
}
