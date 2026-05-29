import java.util.ArrayList;

public class WinningPoints{
    public static int calculateWinningPoints(int number, ArrayList<Settlement> settlements, Game game){

        int winningPoints = 0;

        for (Settlement s : settlements) {

            if (s.owner == number) {

                if (s.city) {
                    winningPoints += 2;
                } else {
                    winningPoints += 1;
                }
            }
        }

        if(winningPoints >= 12){
            
            game.setGameOver();
            return 12;
        }
        else{
            return winningPoints;
        }
    }
}