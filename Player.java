import java.util.ArrayList;
import java.util.EnumMap;

public class Player {

    int playerNumber;
    String name;
    int winningPoints;

    EnumMap<Variables.Resource, Integer> resources = new EnumMap<>(Variables.Resource.class);

    public Player(int playerNumber, String name) {
        this.playerNumber = playerNumber;
        this.name = name;

        // initialisieren
        for (Variables.Resource r : Variables.Resource.values()) {
            resources.put(r, 0);
        }
    }

    public void addResource(Variables.Resource r, int amount) {
        resources.put(r, resources.get(r) + amount);
    }

    public int getResource(Variables.Resource r) {
        return resources.get(r);
    }

    public void calculateWinningPoints(ArrayList<Settlement> settlements, Game game) {
        winningPoints = WinningPoints.calculateWinningPoints(playerNumber, settlements, game);
    }
}