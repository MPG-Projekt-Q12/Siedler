import javax.swing.*;
import java.awt.*;

public class HexFeld extends JPanel {

    private int radius = 60;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Zentrum
        drawHex(g, centerX, centerY, radius, Color.GRAY);

        // 6 Hexfelder drum herum
        for (int i = 0; i < 6; i++) {
            double winkel = Math.toRadians(60 * i);

            int offsetX = (int) (centerX + radius * 2 * Math.cos(winkel));
            int offsetY = (int) (centerY + radius * 2 * Math.sin(winkel));

            drawHex(g, offsetX, offsetY, radius, Color.GREEN);
        }
    }

    private void drawHex(Graphics g, int x, int y, int r, Color color) {
        Polygon hex = createHexagon(x, y, r);

        g.setColor(color);
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