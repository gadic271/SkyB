package Mainthings;

import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Vizual.ViewListener;
public class Engine {
    static int highScoresKept = 5;
    private static Score[] highScores = new Score[highScoresKept];

    private int score = 0;
    private int WINDOW_WIDTH;
    private int WINDOW_HEIGHT;
    private int scrollUpLimit;
    private int distanceBetweenPlatforms;
    private final long timerDelay = 30;
    LinkedList<Platform> visiblePlatforms;
    private int variance;
    private ExecutorService pool = Executors.newFixedThreadPool(1);
    private Hero hero;
    private ViewListener gui;
    private boolean hasStarted = false;
    private boolean gameOver = false;
    private ActionListener gameOverListener;

    public Engine(int width, int height) {

        WINDOW_HEIGHT = height;
        scrollUpLimit = WINDOW_HEIGHT * 4 / 5;
        WINDOW_WIDTH = width;
        variance = WINDOW_WIDTH / 7;
        distanceBetweenPlatforms = (int) (Hero.maxDistance / 1.3);
        init();
        pool.execute(new Timer(this, timerDelay));
    }
    public void init() {
        initPlatforms();
        initHero();
        hasStarted = false;
        gameOver = false;
        this.score = 0;
    }

    public static Score[] getHighScores() {
        synchronized (highScores) {
            return highScores;
        }
    }

    public static boolean shouldAddScore(int score) {
        synchronized (highScores) {
            for (Score s : highScores) {
                if (s == null)
                    return true;
                else if (s.getScore() > score)
                    return true;
            }
            return false;
        }
    }

    public int getScore() {
        return this.score;
    }

    private void initHero() {
        Platform first = visiblePlatforms.get(0);
        hero = new Hero(first.getX(), first.getY());
    }

    public Hero getHero() {
        return hero;
    }

    public LinkedList<Platform> getPlatForms() {
        return visiblePlatforms;
    }

    private boolean hasCollided(Platform p) {
        int x = p.getX();
        int y = p.getY();
        int cx = hero.getX();
        int cy = hero.getY();
        int r = Hero.r;
        if (x - Platform.WIDTH / 2 < cx && x + Platform.WIDTH / 2 > cx
                && y - Platform.HEIGHT / 2 < cy - r
                && y + Platform.HEIGHT / 2 > cy - r) {
            return true;
        } else
            return false;
    }

    private void initPlatforms() {
        visiblePlatforms = new LinkedList<Platform>();
        Random gen = new Random();
        int y = gen.nextInt(distanceBetweenPlatforms / 2);
        y += distanceBetweenPlatforms / 2;
        int x = WINDOW_WIDTH / 2;
        visiblePlatforms.add(new Platform(x, y));
        int ydiff;
        int xdiff;
        while (y < WINDOW_HEIGHT) {
            ydiff = gen.nextInt(distanceBetweenPlatforms / 2);
            ydiff += (distanceBetweenPlatforms / 2);

            y += ydiff;
            xdiff = gen.nextInt(variance * 2);
            x = (x + xdiff - variance) % WINDOW_WIDTH;
            if (x < 0)
                x += WINDOW_WIDTH;
            visiblePlatforms.add(new Platform(x, y));
        }
    }

    private void refillPlatforms() {
        Platform last;

        last = visiblePlatforms.get(visiblePlatforms.size() - 1);
        int ydiff;
        int xdiff;
        Random gen = new Random();
        int x;
        while (last.getY() < WINDOW_HEIGHT) {
            ydiff = gen.nextInt(distanceBetweenPlatforms / 2);
            ydiff += (distanceBetweenPlatforms / 2);
            xdiff = gen.nextInt(variance * 2);
            x = (last.getX() + xdiff - variance) % WINDOW_WIDTH;
            if (x < 0)
                x += WINDOW_WIDTH;
            last = new Platform(x, last.getY() + ydiff);
            visiblePlatforms.add(last);
            score += 1;
        }
    }

    private void updateHeroPos() {
        hero.updateX(WINDOW_WIDTH);
        if (hero.getY() < 0) {
            gameOverListener.actionPerformed(null);
            this.gameOver = true;
        } else if (hero.isMovingDown()) {
            while (hero.isMovingDown() && hero.shouldMoveY()) {
                for (Platform p : visiblePlatforms) {
                    if (hasCollided(p)) {
                        p.doCollisionAction();
                        hero.bounce();
                        score= hero.getY()/distanceBetweenPlatforms;
                        break;
                    } else if (p.getY() - Platform.HEIGHT / 2 > hero.getY()
                            + Hero.r) {
                        break;
                    }
                }
                hero.updateSmoothY();
            }
        } else
            hero.updateRoughY();
        hero.resetYStepCounter();
        hero.updateDy();
    }

    private void updatePlatforms() {
        int y = hero.getY();
        if (y > scrollUpLimit) {
            int diff = y - scrollUpLimit;
            hero.setY(scrollUpLimit);
            Iterator<Platform> iter = visiblePlatforms.iterator();
            Platform p;
            while (iter.hasNext()) {
                p = iter.next();
                if (p.getY() > diff) {
                    p.scroll(diff);
                } else {
                    iter.remove();
                }
            }
        }
        refillPlatforms();
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public boolean gameOver() {
        return gameOver;
    }

    public void registerGameOverListener(ActionListener a) {
        this.gameOverListener = a;
    }

    void updateAll() {
        synchronized (this) {
            if (hasStarted) {
                if (!gameOver) {
                    updateHeroPos();
                    updatePlatforms();
                }
                gui.actionPerformed();

            }
        }
    }

    public void moveLeft() {
        synchronized (this) {
            if (hasStarted) {
                hero.moveLeft();
            }
        }
    }

    public void moveRight() {
        synchronized (this) {
            if (hasStarted) {
                hero.moveRight();
            }
        }
    }

    public void stopMoving() {
        synchronized (this) {
            if (hasStarted) {
                hero.stopMoving();
            }
        }
    }

    public void registerView(ViewListener object) {
        gui = object;
    }

    public void start() {
        synchronized (this) {
            if (hasStarted == false)
                hasStarted = true;
            else
                init();
        }
    }

    public static void addScore(Score s) {
        synchronized (highScores) {
            for (int i = 0; i < highScoresKept; i++) {
                if (highScores[i] == null) {
                    highScores[i] = s;
                    return;
                } else if (s.compareTo(highScores[i]) > 0) {
                    for (int j = highScoresKept - 1; j > i; j--) {
                        highScores[j] = highScores[j - 1];
                    }
                    highScores[i] = s;
                    return;
                }
            }
        }
    }
}

