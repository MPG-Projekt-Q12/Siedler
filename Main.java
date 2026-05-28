import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

                    JFrame frame = new JFrame("Catan");

                    Draw draw = new Draw();

                    frame.add(draw);

                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                    // Vollbild
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

                    frame.setVisible(true);

                    // Board erstellen nachdem Fenster sichtbar ist
                    BoardFactory factory =
                        new BoardFactory(draw);

                    factory.createBoard(
                        frame.getWidth(),
                        frame.getHeight()
                    );

                    // Daten an Draw übergeben
                    draw.tiles = factory.tiles;
                    draw.streets = factory.streets;
                    draw.settlements = factory.settlements;

                    draw.repaint();
            });
    }
}