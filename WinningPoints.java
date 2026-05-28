import java.util.ArrayList;

public class WinningPoints{
    public static int calculateWinningPoints(int number, ArrayList<Settlement> settlements){

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
            return 1000;
        }
        else{
            return winningPoints;
        }
    }
}