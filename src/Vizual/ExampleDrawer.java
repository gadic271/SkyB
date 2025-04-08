package Vizual;

import java.awt.Color;
import java.awt.Graphics;

import Mainthings.Hero;
import Mainthings.Platform;

public abstract class ExampleDrawer {
    protected int width;
    protected int height;

    protected void drawHero(Graphics g, Color c, int cx, int cy, int r) {
        g.setColor(c);

    }

    protected void drawHeroWithBorder(Graphics g, Color c, int cx, int cy, int r) {

        int newCx = cx + Platform.WIDTH / 2;
        int newCy = height - cy;
        Drawing.drawRectWithBorder(g, c, newCx, newCy, r);
    }

    void setDimensions(int width, int height) {
        this.height = height;
        this.width = width;
    }
    abstract void drawHeroWithTail(Graphics g, Hero b);


}