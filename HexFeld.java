import javax.swing.*;
import java.awt.*;

public class HexFeld extends JPanel {

    private Color farbe = Color.LIGHT_GRAY; // z.B. Wald
    private int zahl = 8;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    Polygon hex = new Polygon();

        g.setColor(farbe);
        g.fillPolygon(hex);

        g.setColor(Color.BLACK);
        g.drawPolygon(hex);

    
    }

    private Polygon createHexagon(int x, int y, int r) {
        Polygon hex = new Polygon();
        for (int i = 0; i < 6; i++) {
            double winkel = Math.toRadians(60 * i);
            int px = (int) (x + r * Math.cos(winkel));
            int py = (int) (y + r * Math.sin(winkel));
            hex.addPoint(px, py);
        }
        return hex;
    }
}