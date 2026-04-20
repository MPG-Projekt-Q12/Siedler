import javax.swing.*;
import java.awt.*;

public class HexFeld extends JPanel {

    private Polygon[][] hexFelder;
    private Color[][] farben;
    private int[][] zahlen;

    public HexFeld() {
    int rows = 3;
    int cols = 3;

    hexFelder = new Polygon[rows][cols];
    farben = new Color[rows][cols];
    zahlen = new int[rows][cols];

    int radius = 60;
    int offsetX = 100;
    int offsetY = 100;

    for (int r = 0; r < rows; r++) {
        for (int c = 0; c < cols; c++) {

            int x = offsetX + c * (int)(radius * 1.5);
            int y = offsetY + r * (int)(radius * Math.sqrt(3));

            // Hex erzeugen
            hexFelder[r][c] = createHexagon(x, y, radius);

            // Beispiel: Mitte = Wüste
            if (r == 1 && c == 1) {
                farben[r][c] = new Color(210, 180, 140);
                zahlen[r][c] = 0;
            } else {
                farben[r][c] = Color.GREEN;
                zahlen[r][c] = 8;
            }
        }
    }
}
@Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    for (int r = 0; r < hexFelder.length; r++) {
        for (int c = 0; c < hexFelder[r].length; c++) {

            Polygon hex = hexFelder[r][c];

            g.setColor(farben[r][c]);
            g.fillPolygon(hex);

            g.setColor(Color.BLACK);
            g.drawPolygon(hex);

            if (zahlen[r][c] > 0) {
                Rectangle bounds = hex.getBounds();
                g.drawString(
                    String.valueOf(zahlen[r][c]),
                    bounds.x + bounds.width / 2,
                    bounds.y + bounds.height / 2
                );
            }
        }
    }
}
}