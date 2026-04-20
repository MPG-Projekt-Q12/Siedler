import javax.swing.*;
import java.awt.*;

public class HexFeld extends JPanel {

    private Polygon[] hexFelder;
    private Color[] farben;
    private int radius = 80;

    private final int[][] directions = {
            {1, 0}, {1, -1}, {0, -1},
            {-1, 0}, {-1, 1}, {0, 1}
    };

    public HexFeld() {
        int total = 19;
        hexFelder = new Polygon[total];
        farben = new Color[total];

        setFarbenVerteilung();
        setBackground(new Color(200, 230, 255));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        int index = 0;

        // Mittelpunkt
        hexFelder[index++] = createHexagon(cx, cy, radius);

        // Ring 1 + Ring 2
        index = fillRing(cx, cy, 1, index);
        index = fillRing(cx, cy, 2, index);

        // Zeichnen
        for (int i = 0; i < hexFelder.length; i++) {
            Polygon hex = hexFelder[i];

            g.setColor(farben[i]);
            g.fillPolygon(hex);

            g.setColor(Color.BLACK);
            g.drawPolygon(hex);
        }
    }

    private int fillRing(int cx, int cy, int radiusHex, int index) {

        int q = directions[4][0] * radiusHex;
        int r = directions[4][1] * radiusHex;

        for (int side = 0; side < 6; side++) {
            for (int step = 0; step < radiusHex; step++) {

                int px = cx + (int)(Math.sqrt(3) * radius * (q + r / 2.0));
                int py = cy + (int)(1.5 * radius * r);

                hexFelder[index++] = createHexagon(px, py, radius);

                q += directions[side][0];
                r += directions[side][1];
            }
        }

        return index;
    }

    private Polygon createHexagon(int x, int y, int r) {
        Polygon hex = new Polygon();
        for (int i = 0; i < 6; i++) {
            double winkel = Math.toRadians(60 * i - 90);
            int px = (int) (x + r * Math.cos(winkel));
            int py = (int) (y + r * Math.sin(winkel));
            hex.addPoint(px, py);
        }
        return hex;
    }

    // 🎨 FARBVERTEILUNG

    private void setFarbenVerteilung() {
        java.util.List<Color> liste = new java.util.ArrayList<>();

        Color schaf = new Color(179, 238, 58);
        Color wald = new Color(0, 100, 0);
        Color weizen = new Color(255, 215, 0);
        Color lehm = new Color(238, 118, 33);
        Color stein = new Color(140, 140, 140);
        Color wueste = new Color(255, 211, 155);

        addColor(liste, schaf, 4);
        addColor(liste, wald, 4);
        addColor(liste, weizen, 4);
        addColor(liste, lehm, 3);
        addColor(liste, stein, 3);
        addColor(liste, wueste, 1);

        if (liste.size() != farben.length) {
            throw new IllegalStateException("Falsche Anzahl an Farben!");
        }

        java.util.Collections.shuffle(liste);

        for (int i = 0; i < farben.length; i++) {
            farben[i] = liste.get(i);
        }
    }

    private void addColor(java.util.List<Color> liste, Color color, int anzahl) {
        for (int i = 0; i < anzahl; i++) {
            liste.add(color);
        }
    }
}