public class Turn{

    boolean waitingForNext = false;

    public void startTurn1(int playerNumber){

        waitingForNext = false;
        System.out.println("Spieler " + playerNumber + " ist dran");
        waitingForNext = true;
    }

    public void startTurn2(int playerNumber){

        waitingForNext = false;
        System.out.println("Spieler " + playerNumber + " ist dran");
        waitingForNext = true;
    }

    public void turn(int playerNumber, Dice dice){
        waitingForNext = false;
        dice.rollDice();
        System.out.println("Spieler " + playerNumber + " ist dran");
        waitingForNext = true;
    }

    public void next(){

        if (waitingForNext == false) return;

        waitingForNext = false;

        System.out.println("Zug beendet");
    }

}