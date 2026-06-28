public class Turn{

    public boolean waitingForSettlement = false;
    public boolean waitingForStreet = false;
    public boolean waitingForNext = false;
    public boolean waitingForRobber = false;
    public int currentPlayer;

    public void startTurn1(int playerNumber, Game game){

        currentPlayer = playerNumber;

        waitingForNext = false;
        waitingForSettlement = true;
        waitingForStreet = false;

        game.addConsole("Baue ein Settlement");
    }

    public void startTurn2(int playerNumber, Game game){

        currentPlayer = playerNumber;

        waitingForNext = false;
        waitingForSettlement = true;
        waitingForStreet = false;

        game.addConsole("Baue ein Settlement");
    }

    public void turn(int playerNumber, Dice dice, Draw draw, Game game){
        waitingForNext = false;
        dice.rollDice();

        if(dice.getDiceSum() == 7){

            waitingForRobber = true;
            waitingForNext = false;

            game.addConsole("Setze den Räuber");

            draw.repaint();
            return;
        }

        RessourceDistribution.distributeResources(dice.getDiceSum(), draw.tiles, draw.settlements, draw.players, game);
        
        draw.repaint();
        waitingForNext = true;
        draw.nextButtonReady = true;
    }

}