package Vizual;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Mainthings.Engine;

public class Keys {
    public static String backgroundName = "background.jpg";
    public static String startButtonName = "Start.png";
    public static URL backgroundURL = Keys.class.getResource("background.jpg");
    public static URL startButtonURL = Keys.class.getResource("Start.png");

    private GamePanel game;
    private JPanel startScreen;
    private JFrame main;
    private GameOver gameOverScreen;
    private Engine e;

    public Keys (final Engine e, int width, int height) {
        e.registerView(new ViewListener(this));
        this.e = e;
        e.registerGameOverListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                gameOverScreen.setup();
                switchTo(gameOverScreen);
            }
        });
        main = new JFrame();
        setupStartupContainer(e, width, height);
        setupGameContainer(e, width, height);
        setupGameOverContainer(e, width, height);
        main.setSize(width, height);
        main.setResizable(false);
        switchTo(startScreen);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setVisible(true);
    }

    private void setupGameOverContainer(final Engine e, int width, int height) {
        gameOverScreen = new GameOver(e,width, height);
        gameOverScreen.addMouseListener(new MouseListener() {

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent event) {
                e.init();
                switchTo(startScreen);
            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }
        });
    }

    private void switchTo(JPanel p) {
        main.remove(startScreen);
        main.remove(game);
        main.remove(gameOverScreen);
        main.add(p);
        main.setContentPane(p);
        main.pack();
    }

    private void setupStartupContainer(final Engine e, int width, int height) {
        startScreen = new Start(e, width, height);
        startScreen.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent arg0) {

            }

            @Override
            public void mouseEntered(MouseEvent arg0) {

            }

            @Override
            public void mouseExited(MouseEvent arg0) {

            }

            @Override
            public void mousePressed(MouseEvent arg0) {
                switchTo(game);
                e.start();
                game.requestFocusInWindow();

            }

            @Override
            public void mouseReleased(MouseEvent arg0) {

            }

        });
    }

    private void setupGameContainer(final Engine e, int width, int height) {

        game = new GamePanel(e, width, height);
        game.setFocusable(true);
        game.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent arg0) {
            }

            @Override
            public void keyReleased(KeyEvent arg0) {
                int code = arg0.getKeyCode();
                if (code == 39 || code == 37)
                    e.stopMoving();
                else
                    System.out.println("key error :" + code);
            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                int code = arg0.getKeyCode();
                if (code == 39)
                    e.moveRight();
                else if (code == 37)
                    e.moveLeft();
                else if (code == 32)
                    e.start();
                else
                    System.out.println("key error :" + code);
            }
        });
    }

    public void setCircleTailDrawer(ExampleDrawer d) {
        game.setExampleDrawer(d);
    }

    void updateAll() {
        if (e.gameOver()) {
            gameOverScreen.repaint();
        } else if (e.hasStarted())
            game.repaint();
    }
}