public class Turn{

    public boolean waitingForSettlement = false;
    public boolean waitingForStreet = false;
    public boolean waitingForNext = false;
    public boolean waitingForRobber = false;
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

        if(dice.getDiceSum() == 7){

            waitingForRobber = true;
            waitingForNext = false;

            System.out.println("Setze den Räuber");

            draw.repaint();
            return;
        }

        RessourceDistribution.distributeResources(dice.getDiceSum(), draw.tiles, draw.settlements, draw.players);
        
        draw.repaint();
        waitingForNext = true;
        draw.nextButtonReady = true;
    }

}