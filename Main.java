import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

                    // Spieleranzahl abfragen
                    int playerCount = 0;

                    while (playerCount < 1 || playerCount > 4){

                        String input = JOptionPane.showInputDialog(
                                null,
                                "Wie viele Spieler? (1-4)",
                                "Catan",
                                JOptionPane.QUESTION_MESSAGE
                            );

                        // Fenster geschlossen
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

                    // Namen abfragen
                    String[] playerNames = new String[playerCount];

                    for (int i = 0; i < playerCount; i++){

                        String name = "";

                        while (name.trim().isEmpty()){

                            name = JOptionPane.showInputDialog(
                                null,
                                "Name von Spieler " + (i + 1),
                                "Spielername",
                                JOptionPane.QUESTION_MESSAGE
                            );

                            // Abbrechen -> Programm beenden
                            if (name == null){
                                System.exit(0);
                            }
                        }

                        playerNames[i] = name;
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

                    game.newGame(
                        frame.getWidth(),
                        frame.getHeight(),
                        playerNames
                    );
            });
    }
}