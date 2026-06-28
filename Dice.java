import java.awt.Color;

public class Dice {

    public int dice1 = 1;
    public int dice2 = 1;
    public int sum = 2;

    public void rollDice(Game game) {

        dice1 = (int)(Math.random() * 6) + 1;
        dice2 = (int)(Math.random() * 6) + 1;

        sum = dice1 + dice2;

        game.addConsole("Es wurde eine " + sum + " gewürfelt.");
        if (sum == 7) {
            game.addConsole("Setze den Räuber");
        }
    }

    public int getDiceSum() {
        return sum;
    }
}