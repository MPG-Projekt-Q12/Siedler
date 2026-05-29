public class Game {

    private BoardFactory boardfactory;
    private Turn turn;
    private Dice dice;
    private int currentPlayer;
    private String[] names;

    private boolean gameOver = false;

    public Game(Draw draw){

        this.boardfactory = new BoardFactory(draw);
        this.turn = new Turn();
        draw.addMouseListener(new OnClick(draw));
        this.dice = new Dice();
    }

    public void newGame(int x, int y, String[] names){

        this.names = names;

        boardfactory.createBoard(x, y, names);

        for (int i = 0; i < names.length; i++){
            currentPlayer = i + 1;
            turn.startTurn1(currentPlayer);
        }
        for (int i = names.length; i > 0; i--){
            currentPlayer = i;
            turn.startTurn2(currentPlayer);
        }

        //while (gameOver == false){
        //for (int i = 0; i < names.length; i++){

        //turn.turn(i + 1, dice);
        //}
        ///}
    }

    public void setGameOver(){

        gameOver = true;
    }
}