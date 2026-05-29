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
            if (tile.number != diceNumber) {
                continue;
            }

            // Wüste überspringen
            if (tile.resource == Variables.Resource.DESERT) {
                continue;
            }

            // Alle Settlements prüfen
            for (Settlement settlement : settlements) {

                // Muss gebaut sein
                if (!settlement.build) {
                    continue;
                }

                // Distanz Settlement <-> Tile
                double dist = distance(
                        tile.centerx,
                        tile.centery,
                        settlement.centerx,
                        settlement.centery
                    );

                // Settlement gehört zu diesem Tile
                if (dist < 120) {

                    Player player = getPlayerByNumber(
                            settlement.owner,
                            players
                        );

                    if (player == null) {
                        continue;
                    }

                    // City = 2 Ressourcen
                    if (settlement.city) {
                        player.addResource(tile.resource, 2);

                        System.out.println(
                            player.name + " bekommt 2 "
                            + tile.resource
                        );
                    }

                    // Normales Settlement = 1 Ressource
                    else {
                        player.addResource(tile.resource, 1);

                        System.out.println(
                            player.name + " bekommt 1 "
                            + tile.resource
                        );
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

            if (p.playerNumber == number) {
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