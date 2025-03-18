package Vizual;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Key;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Mainthings.Engine;

public class Start extends JPanel {
    private BufferedImage background;
    private BufferedImage startButton;
    private int width;
    private int height;
    private int startButtonWidth;
    private int startButtonHeight;

    public Start(final Engine engine, int width, int height) {
        this.width = width;
        this.height = height;
        this.setPreferredSize(new Dimension(width, height));
        try {
            background = ImageIO.read(Keys.backgroundURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            startButton = ImageIO.read(Keys.startButtonURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.startButtonWidth = startButton.getWidth();
        this.startButtonHeight = startButton.getHeight();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(background, 0, 0, width, height, 0, 0,
                Math.min(background.getWidth(), width),
                Math.min(background.getHeight(), height), null);
        g.drawRoundRect(width / 8, height * 3 / 8, width * 3 / 4, height / 4, width / 8, height / 8);
        g.drawImage(startButton, width / 8 + 5, height * 3 / 8, width / 8 + startButtonWidth, height * 3 / 8 + startButtonHeight, 0, 0,
                startButtonWidth,
                startButtonHeight, null);
    }
}
