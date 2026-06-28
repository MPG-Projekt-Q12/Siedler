import java.awt.Color;

public class Dice {

    private Game game;

    public int dice1 = 1;
    public int dice2 = 1;
    public int sum = 2;

    public void setGame(Game game) {
        this.game = game;
    }

    public void rollDice() {

        dice1 = (int)(Math.random() * 6) + 1;
        dice2 = (int)(Math.random() * 6) + 1;

        sum = dice1 + dice2;

        if (game != null) {
            game.addConsole("Es wurde eine " + sum + " gewürfelt.");
        }
    }

    public int getDiceSum() {
        return sum;
    }
}