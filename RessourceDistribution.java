import java.util.ArrayList;

public class RessourceDistribution {

    public static void distributeResources(
    int diceNumber,
    ArrayList<Tile> tiles,
    ArrayList<Settlement> settlements,
    ArrayList<Player> players) {

        // Alle Tiles prüfen
        for (Tile tile : tiles) {

            // Nur passende Zahl
            if (tile.getNumber() != diceNumber) {
                continue;
            }

            // Wüste überspringen
            if (tile.getResource() == Variables.Resource.DESERT) {
                continue;
            }

            // Alle Settlements prüfen
            for (Settlement settlement : settlements) {

                // Muss gebaut sein
                if (!settlement.getBuild()) {
                    continue;
                }

                // Distanz Settlement <-> Tile
                double dist = distance(
                        tile.getCenterX(),
                        tile.getCenterY(),
                        settlement.getCenterX(),
                        settlement.getCenterY()
                    );

                // Settlement gehört zu diesem Tile
                if (dist < 120) {

                    Player player = getPlayerByNumber(
                            settlement.getOwner(),
                            players
                        );

                    if (player == null) {
                        
                        continue;
                    }
                    
                    if (settlement.getCity()) {
                        
                        player.addResource(tile.getResource(), 2);
                        System.out.println(player.getPlayerName() + " bekommt 2 " + tile.getResource());
                    }
                    
                    else {
                        
                        player.addResource(tile.getResource(), 1);
                        System.out.println(player.getPlayerName() + " bekommt 1 " + tile.getResource());
                    }
                }
            }
        }
    }

    // ---------------- HELPER ----------------

    public static Player getPlayerByNumber(
    int number,
    ArrayList<Player> players) {

        for (Player p : players) {

            if (p.getPlayerNumber() == number) {
                return p;
            }
        }

        return null;
    }

    public static double distance(
    int x1,
    int y1,
    int x2,
    int y2) {

        return Math.hypot(x1 - x2, y1 - y2);
    }
}