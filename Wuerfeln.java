import java.awt.*;

public class Wuerfeln {

    // beide Würfelzahlen
    private int wuerfel1 = 1;
    private int wuerfel2 = 1;
    private Color farbe = Color.WHITE;
    
    private int cx;
    private int cy;

    // Punktpositionen
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

    // Welche Punkte benutzt werden
    private final int[][] wuerfel = {

            {},

            {4},

            {0, 8},

            {0, 4, 8},

            {0, 2, 6, 8},

            {0, 2, 4, 6, 8},

            {0, 2, 3, 5, 6, 8}
        };

    // Würfeln
    public int wuerfeln() {

        wuerfel1 =
        (int)(Math.random() * 6) + 1;

        wuerfel2 =
        (int)(Math.random() * 6) + 1;

        return wuerfel1 + wuerfel2;
    }

    // Beide Würfel zeichnen
    public void zeichnen(Graphics g) {
        
        zeichneWuerfel(g, 900, 850, wuerfel1);

        zeichneWuerfel(g, 975, 850, wuerfel2);

        // Summe anzeigen
        g.setColor(Color.BLACK);

        g.setFont(new Font("Arial", Font.BOLD, 30));

        g.drawString(
            "Summe: " + (wuerfel1 + wuerfel2),
            887,
            950
        );
    }

    // EINEN Würfel zeichnen
    private void zeichneWuerfel(
    Graphics g,
    int startX,
    int startY,
    int zahl
    ) {

        // Würfelkasten
        g.setColor(farbe);

        g.fillRoundRect(
            startX,
            startY,
            50,
            50,
            10,
            10
        );

        // Rand
        g.setColor(Color.BLACK);

        g.drawRoundRect(
            startX,
            startY,
            50,
            50,
            10,
            10
        );

        // Punkte zeichnen
        for (int index : wuerfel[zahl]) {

            int x =
                startX + punkte[index][0];

            int y =
                startY + punkte[index][1];

            g.fillOval(
                x - 5,
                y - 5,
                10,
                10
            );
        }
    }

    public void animation(HexFeld feld) {

        new Thread(() -> 
                {

                    for (int i = 0; i < 10; i++) {

                        wuerfel1 =
                        (int)(Math.random() * 6) + 1;

                        wuerfel2 =
                        (int)(Math.random() * 6) + 1;

                        farbe = new Color(
                            (int)(Math.random() * 255),
                            (int)(Math.random() * 255),
                            (int)(Math.random() * 255)
                        );
                        feld.repaint();

                        try {

                            Thread.sleep(100 + i * 15);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    farbe = Color.WHITE;
                    feld.repaint();
            }).start();
    }
}