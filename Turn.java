public class Turn{

    public boolean waitingForSettlement = false;
    public boolean waitingForStreet = false;
    public boolean waitingForNext = false;

    public int currentPlayer;

    public void startTurn1(int playerNumber){

        currentPlayer = playerNumber;

        waitingForNext = false;
        waitingForSettlement = true;
        waitingForStreet = false;
        waitingForNext = false;

        System.out.println("Startphase 1: Spieler  " + playerNumber + " ist dran");
        System.out.println("Baue ein Settlement");
    }

    public void startTurn2(int playerNumber){

        currentPlayer = playerNumber;

        waitingForNext = false;
        waitingForSettlement = true;
        waitingForStreet = false;
        waitingForNext = false;

        System.out.println("Startphase 2: Spieler  " + playerNumber + " ist dran");
        System.out.println("Baue ein Settlement");
    }

    public void turn(int playerNumber, Dice dice, Draw draw){
        waitingForNext = false;
        dice.rollDice();
        RessourceDistribution.distributeResources(dice.getDiceSum(), draw.tiles, draw.settlements, draw.players);
        draw.repaint();
        System.out.println("Normaler Zug: Spieler " + playerNumber + " ist dran");
        waitingForNext = true;
    }

}