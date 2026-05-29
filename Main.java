import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

                    JFrame frame = new JFrame("Catan");

                    Draw draw = new Draw();

                    frame.add(draw);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    frame.setVisible(true);

                    String[] playerNames = {
                            "adam",
                            "ben",
                            "lfrnjesus",
                            "TinaTurner2005"
                        };

                    // Game-Objekt erstellen
                    Game game = new Game(draw);

                    game.newGame(frame.getWidth(), frame.getHeight(), playerNames);
            });
    }
}