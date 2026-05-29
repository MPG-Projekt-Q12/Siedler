public class Game {

    private BoardFactory factory;
    private Turn turn;

    private boolean gameOver = false;

    public Game(Draw draw){

        factory = new BoardFactory(draw);
    }

    public void newGame(int x, int y, String[] names){

        factory.createBoard(x, y, names);

        for (int i = 0; i < names.length; i++){
            turn.startTurn1(i + 1);
        }
        for (int i = names.length; i > 0; i--){

            turn.startTurn2(i);
        }
        while (gameOver == false){
            for (int i = 0; i < names.length; i++){

                turn.turn(i + 1);
            }
        }
    }

    public void setGameOver(){

        gameOver = true;
    }
}