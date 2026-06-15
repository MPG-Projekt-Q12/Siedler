import java.util.ArrayList;

public class WinningPoints {

    public static int calculateWinningPoints(
    int number,
    ArrayList<Settlement> settlements,
    ArrayList<Street> streets,
    Game game
    ){

        int points = 0;

        // ---------------- SETTLEMENTS ----------------
        for (Settlement s : settlements){

            if (s.getOwner() != number) continue;

            if (s.getCity()){
                points += 2;
            }
            else if (s.getBuild()){
                points += 1;
            }
        }
        if (game.getLongestRoadOwner() != null
        && game.getLongestRoadOwner().getPlayerNumber() == number){

            points += 2;
        }

        return Math.min(points, 12);
    }

    public static int calculateLongestRoad(int playerId, ArrayList<Street> streets){

        int best = 0;

        for (Street start : streets){

            if (start.owner != playerId) continue;

            best = Math.max(best, dfs(start, playerId, streets, new ArrayList<>()));
        }

        return best;
    }

    private static int dfs(Street current, int playerId,
    ArrayList<Street> streets,
    ArrayList<Street> visited){

        visited.add(current);

        int max = 0;

        for (Street next : streets){

            if (next.owner != playerId) continue;
            if (visited.contains(next)) continue;

            if (connected(current, next)){
                max = Math.max(max,
                    dfs(next, playerId, streets, new ArrayList<>(visited))
                );
            }
        }

        return max + 1;
    }

    private static boolean connected(Street a, Street b){

        return Math.hypot(
            a.getCenterX() - b.getCenterX(),
            a.getCenterY() - b.getCenterY()
        ) < 80;
    }
}