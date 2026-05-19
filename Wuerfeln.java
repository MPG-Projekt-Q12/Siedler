import java.awt.*;

public class Wuerfeln {

    private int wuerfel1 = 1;
    private int wuerfel2 = 1;

    private int finaleSumme = 0;

    private Color farbe = Color.WHITE;

    private boolean fertig = true;

    // Punktmatrix
    private final int[][] punkte = {

            {12, 12},
            {25, 12},
            {38, 12},

            {12, 25},
            {25, 25},
            {38, 25},

            {12, 38},
            {25, 38},
            {38, 38}
    };

    // Würfelanzeige
    private final int[][] wuerfel = {

            {},
            {4},
            {0, 8},
            {0, 4, 8},
            {0, 2, 6, 8},
            {0, 2, 4, 6, 8},
            {0, 2, 3, 5, 6, 8}
    };

    //  Würfeln 
    public int wuerfeln() {

        wuerfel1 = (int)(Math.random() * 6) + 1;
        wuerfel2 = (int)(Math.random() * 6) + 1;

        finaleSumme = wuerfel1 + wuerfel2;

        return finaleSumme;
    }

    //Zeichnen
    public void zeichnen(Graphics g, int breite, int hoehe) {

        int size = 50;
        int abstand = 25;

        int gesamtBreite = size * 2 + abstand;

        int startX = breite / 2 - gesamtBreite / 2;
        int y = hoehe - 180;

        zeichneWuerfel(g, startX, y, wuerfel1);

        zeichneWuerfel(g, startX + size + abstand, y, wuerfel2);

        // 🧾 Summe nur anzeigen wenn fertig
        if (fertig) {

            g.setColor(Color.BLACK);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));

            String text = "Summe: " + finaleSumme;

            FontMetrics fm = g.getFontMetrics();

            int tx = breite / 2 - fm.stringWidth(text) / 2;
            int ty = y + 100;

            g.drawString(text, tx, ty);
        }
    }

    // 🎲 einzelner Würfel
    private void zeichneWuerfel(Graphics g, int x, int y, int zahl) {

        g.setColor(farbe);

        g.fillRoundRect(x, y, 50, 50, 10, 10);

        g.setColor(Color.BLACK);

        g.drawRoundRect(x, y, 50, 50, 10, 10);

        for (int index : wuerfel[zahl]) {

            int px = x + punkte[index][0];
            int py = y + punkte[index][1];

            g.fillOval(px - 5, py - 5, 10, 10);
        }
    }

    //Animation
    public void animation(HexFeld feld) {

        new Thread(() -> {

            fertig = false;

            for (int i = 0; i < 15; i++) {

                wuerfel1 = (int)(Math.random() * 6) + 1;
                wuerfel2 = (int)(Math.random() * 6) + 1;

                farbe = new Color(
                        (int)(Math.random() * 255),
                        (int)(Math.random() * 255),
                        (int)(Math.random() * 255)
                );

                feld.repaint();

                try {
                    Thread.sleep(80 + i * 15);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            farbe = Color.WHITE;

            wuerfeln();

            fertig = true;

            feld.repaint();

        }).start();
    }
}