import java.util.ArrayList;
import java.util.EnumMap;

public class Player {

    int number;

    EnumMap<Variables.Resource, Integer> resources = new EnumMap<>(Variables.Resource.class);

    int winningPoints;

    public Player(int number) {
        this.number = number;

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

    public void calculateWinningPoints(ArrayList<Settlement> settlements) {
        winningPoints = WinningPoints.calculateWinningPoints(number, settlements);
    }
}