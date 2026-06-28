import java.util.ArrayList;
import java.util.List;

public class Robber {

    public void stealRandomResource(Tile tile, Player currentPlayer, Game game) {

        List<Player> victims = new ArrayList<>();

        int radius = 80;

        for (Settlement s : game.getSettlements()) {

            if (!s.getBuild()) continue;
            if (s.getOwner() == currentPlayer.getPlayerNumber()) continue;

            double dist = Math.hypot(tile.getCenterX() - s.getCenterX(), tile.getCenterY() - s.getCenterY());

            if (dist > radius + 5) continue;

            Player p = game.getPlayerByNumber(s.getOwner());

            if (p != null && !victims.contains(p)) {
                victims.add(p);
            }
        }

        if (victims.isEmpty()) return;

        Player victim = victims.get((int)(Math.random() * victims.size()));

        List<Variables.Resource> available = new ArrayList<>();

        for (Variables.Resource r : Variables.Resource.values()) {

            if (r == Variables.Resource.DESERT || r == Variables.Resource.DEFAULT)
                continue;

            if (victim.getResource(r) > 0) {
                available.add(r);
            }
        }

        if (available.isEmpty()) return;

        Variables.Resource stolen = available.get((int)(Math.random() * available.size()));

        victim.addResource(stolen, -1);
        currentPlayer.addResource(stolen, 1);
    }
}