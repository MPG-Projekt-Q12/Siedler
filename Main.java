import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

                    // Splieler initiieren
                    int playerCount = 0;

                    while (playerCount < 1 || playerCount > 4){

                        String input = JOptionPane.showInputDialog(null, "Wie viele Spieler? (1-4)", "Catan", JOptionPane.QUESTION_MESSAGE);

                        if (input == null){
                            System.exit(0);
                        }

                        try {
                            playerCount = Integer.parseInt(input);
                        }
                        catch (NumberFormatException e){
                            playerCount = 0;
                        }
                    }

                    String[] playerNames = new String[playerCount];

                    for (int i = 0; i < playerCount; i++){

                        String name = "";

                        while (name.trim().isEmpty()){

                            name = JOptionPane.showInputDialog(null, "Name von Spieler " + (i + 1), "Spielername", JOptionPane.QUESTION_MESSAGE);

                            if (name == null){
                                System.exit(0);
                            }
                        }

                        playerNames[i] = name;
                    }

                    int maxWinningPoints = 0;

                    while (true) {
                        String input = JOptionPane.showInputDialog(null, "Gewinnpunkte zum Gewinnen:", "Spiel", JOptionPane.QUESTION_MESSAGE);

                        if (input == null) {
                            System.exit(0);
                        }

                        input = input.trim();

                        if (input.isEmpty()) {
                            continue;
                        }

                        try {
                            maxWinningPoints = Integer.parseInt(input);
                            break;
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Bitte gib eine gültige Zahl ein!");
                        }
                    }

                    // Fenster erstellen
                    JFrame frame = new JFrame("Catan");

                    Draw draw = new Draw();

                    frame.add(draw);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    frame.setVisible(true);

                    // Spiel starten
                    Game game = new Game(draw);

                    game.newGame(1920, 1080, playerNames, maxWinningPoints);
                    game.addConsole("Spiel gestartet");
            });
    }
}