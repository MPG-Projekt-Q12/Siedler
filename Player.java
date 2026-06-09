import java.util.ArrayList;
import java.util.EnumMap;

public class Player {

    int playerNumber;
    String name;
    private int winningPoints;
    private int longestRoad = 0;

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

    public void calculateWinningPoints(
    ArrayList<Settlement> settlements,
    ArrayList<Street> streets,
    Game game
    ){
        winningPoints = WinningPoints.calculateWinningPoints(
            playerNumber,
            settlements,
            streets,
            game
        );
    }

    public int getWinningPoints (){
        return winningPoints;
    }

    public void setWinningPoints (int winningPoints){
         this.winningPoints = winningPoints;
    }
    
    public int getLongestRoad (){
        return longestRoad;
    }

    public void setLongestRoad (int longestRoad){
         this.longestRoad = longestRoad;
    }
}