package Vizual ;

public class ViewListener {
    private Keys g;

    ViewListener(Keys g) {
        this.g = g;
    }

    public void actionPerformed() {
        g.updateAll();

    }
}