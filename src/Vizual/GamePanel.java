package Vizual;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Mainthings.Hero;
import Mainthings.Engine;
import Mainthings.Platform;
import Mainthings.Score;

import static java.lang.String.valueOf;

public class GamePanel extends JPanel {

    private Engine e;
    private int height;
    private int width;
    private ExampleDrawer emoteDrawer;
    private BufferedImage background;

    GamePanel(Engine engine, int width, int height) {
        this.width = width;
        this.e = engine;
        this.height = height;
        this.setPreferredSize(new Dimension(width, height));
        try {
            background = ImageIO.read(Keys.backgroundURL);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void setExampleDrawer(ExampleDrawer d) {
        synchronized (e) {
            emoteDrawer = d;
            d.setDimensions(width, height);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        synchronized (e) {
            super.paintComponent(g);
            g.drawImage(background, 0, 0, width, height, 0, 0,
                    Math.min(background.getWidth(), width),
                    Math.min(background.getHeight(), height), null);
            int top, left;
            for (Platform p : e.getPlatForms()) {
                left = p.getX();
                top = height - p.getY() - Platform.HEIGHT / 2;
                g.setColor(Color.GREEN);
                g.fillRect(left, top, Platform.WIDTH, Platform.HEIGHT);
            }
            Hero b = e.getHero();
            emoteDrawer.drawHeroWithTail(g, b);
            g.setColor(Color.BLACK);
            ((Graphics2D) g).setStroke(new BasicStroke(Platform.WIDTH / 2));
            g.drawRect(0, 0, width, height);
            Score[] scores = Engine.getHighScores();
            int numScores = scores.length;
            Score s;
            String text;
            g.setColor(Color.WHITE);
            g.setFont(new Font("Calibri", Font.PLAIN, 20));
            g.drawString("Score:", 570, 50);
            g.drawString(String.valueOf(e.getScore()), 640, 50);
            g.drawString("Records:", 50, 40);
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            for (int i = 0; i < numScores; i++) {
                s = scores[i];
                if (s != null) {
                    text = "" + s.getScore() + " : " + s.getName();
                    g.drawString(text, width / 10, (i + 1) * height / (numScores + 7));
                }
            }
        }
    }
}