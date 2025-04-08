package Vizual;

import java.awt.Color;
import java.awt.Graphics;

public final class Drawing {
    private Drawing() {

    }
    static void drawRectWithBorder(Graphics g, Color c, int cx, int cy, int r) {

        int left = cx - r;
        int top = cy - r;
        g.setColor(c);
        g.fillRect(left, top, r*2, r*2);
        g.setColor(Color.black);
        g.drawRect(left, top, r*2, r*2);
    }



}