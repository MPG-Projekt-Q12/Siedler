import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;

public class HexFeld extends JPanel {

    private Polygon[] hexFelder;
    private Color[] farben;
    private int radius = 80;

    private int[] zahlen = {
            9,3,11,6,5,4,10,8,4,5,12,9,10,8,3,6,2,11
    };

    private int[] zahlenFinal;

    private final Color WUESTE = new Color(255, 211, 155);

    private final int[][] directions = {
            {1, 0}, {1, -1}, {0, -1},
            {-1, 0}, {-1, 1}, {0, 1}
    };

    private static HashMap<Punkt, Punkt> eckpunkte = new HashMap<>();

    public HexFeld() {
        int total = 19;
        hexFelder = new Polygon[total];
        farben = new Color[total];
        zahlenFinal = new int[total];

        setFarbenVerteilung();
        setZahlenAufFelder();

        setBackground(new Color(200, 230, 255));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int cx = getWidth() / 2;
        int cy = getHeight() / 2;

        int index = 0;

        hexFelder[index++] = createHexagon(cx, cy, radius);

        index = fillRing(cx, cy, 1, index);
        index = fillRing(cx, cy, 2, index);

        for (int i = 0; i < hexFelder.length; i++) {
            Polygon hex = hexFelder[i];

            g.setColor(farben[i]);
            g.fillPolygon(hex);

            g.setColor(Color.BLACK);
            g.drawPolygon(hex);

            Rectangle bounds = hex.getBounds();
            int textX = bounds.x + bounds.width / 2;
            int textY = bounds.y + bounds.height / 2;

            int zahl = zahlenFinal[i];

            if (zahl != 0) {
                int circleRadius = 18;

                g.setColor(Color.WHITE);
                g.fillOval(textX - circleRadius, textY - circleRadius, circleRadius * 2, circleRadius * 2);

                g.setColor(Color.BLACK);
                g.drawOval(textX - circleRadius, textY - circleRadius, circleRadius * 2, circleRadius * 2);

                if (zahl == 6 || zahl == 8) {
                    g.setFont(new Font("Arial", Font.BOLD, 34));
                    g.setColor(Color.RED);
                } else {
                    g.setFont(new Font("Arial", Font.BOLD, 24));
                    g.setColor(Color.BLACK);
                }

                String text = String.valueOf(zahl);
                FontMetrics fm = g.getFontMetrics();

                int tx = textX - fm.stringWidth(text) / 2;
                int ty = textY + fm.getAscent() / 2 - 2;

                g.drawString(text, tx, ty);
            }
        }

        // 🔍 DEBUG: alle Eckpunkte zeichnen
        g.setColor(Color.BLUE);
        for (Punkt p : eckpunkte.keySet()) {
            g.fillOval(p.x - 3, p.y - 3, 6, 6);
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

            Punkt p = new Punkt(px, py);

            if (!eckpunkte.containsKey(p)) {
                eckpunkte.put(p, p);
            } else {
                p = eckpunkte.get(p);
            }

            hex.addPoint(p.x, p.y);
        }

        return hex;
    }
    
    public static HashMap<Punkt, Punkt> getEckpunkte(){
        return eckpunkte;
    }
    
    private void setFarbenVerteilung() {
        List<Color> liste = new ArrayList<>();

        Color schaf = new Color(179, 238, 58);
        Color wald = new Color(0, 100, 0);
        Color weizen = new Color(255, 215, 0);
        Color lehm = new Color(238, 118, 33);
        Color stein = new Color(140, 140, 140);

        addColor(liste, schaf, 4);
        addColor(liste, wald, 4);
        addColor(liste, weizen, 4);
        addColor(liste, lehm, 3);
        addColor(liste, stein, 3);
        addColor(liste, WUESTE, 1);

        Collections.shuffle(liste);

        for (int i = 0; i < farben.length; i++) {
            farben[i] = liste.get(i);
        }
    }

    private void setZahlenAufFelder() {
        int zahlIndex = 0;

        for (int i = 0; i < farben.length; i++) {
            if (farben[i].equals(WUESTE)) {
                zahlenFinal[i] = 0;
            } else {
                zahlenFinal[i] = zahlen[zahlIndex++];
            }
        }
    }

    private void addColor(List<Color> liste, Color color, int anzahl) {
        for (int i = 0; i < anzahl; i++) {
            liste.add(color);
        }
    }
}