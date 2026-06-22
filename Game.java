import javax.swing.JOptionPane;

public class Game {

    private BoardFactory boardfactory;
    private Turn turn;
    private Dice dice;
    private Draw draw;
    private String[] names;
    private Player currentPlayerObject;
    private Player longestRoadOwner = null;
    private int currentPlayer;
    private int setupPhase = 1;
    private int setupIndex = 1;
    private int longestRoadLength = 0;
    private boolean gameOver = false;

    public Game(Draw draw){

        this.draw = draw;
        this.boardfactory = new BoardFactory(draw);
        this.turn = new Turn();
        this.dice = new Dice();

        draw.setGame(this);
        draw.dice = dice;
        draw.addMouseListener(new OnClick(draw, this, turn));
    }

    public void newGame(int x, int y, String[] names){

        this.names = names;

        boardfactory.createBoard(x, y, names);

        currentPlayer = 1;
        updateCurrentPlayerObject();
        draw.currentPlayer = currentPlayer;
        draw.repaint();

        turn.startTurn1(currentPlayer);
    }

    // Game over
    public void setGameOver(){

        gameOver = true;

        Player winner = getCurrentPlayer();

        JOptionPane.showMessageDialog(null, winner.getPlayerName() + " hat gewonnen!", "Spiel beendet", JOptionPane.INFORMATION_MESSAGE);
    }

    // Next turn
    public void nextTurn(){

        if (gameOver){
            return;
        }

        // Phase 1
        if (setupPhase == 1){

            setupIndex++;

            if (setupIndex <= names.length){

                currentPlayer = setupIndex;
                updateCurrentPlayerObject();
                draw.currentPlayer = currentPlayer;
                draw.repaint();

                turn.startTurn1(currentPlayer);

                return;
            }

            setupPhase = 2;
            setupIndex = names.length;
            currentPlayer = setupIndex;
            updateCurrentPlayerObject();
            draw.currentPlayer = currentPlayer;
            draw.repaint();

            turn.startTurn2(currentPlayer);
            return;
        }

        // Phase 2
        if (setupPhase == 2){

            setupIndex--;

            if (setupIndex >= 1){

                currentPlayer = setupIndex;
                updateCurrentPlayerObject();
                draw.currentPlayer = currentPlayer;
                draw.repaint();

                turn.startTurn2(currentPlayer);
                return;
            }

            setupPhase = 3;

            currentPlayer = 1;
            updateCurrentPlayerObject();
            draw.currentPlayer = currentPlayer;
            draw.repaint();

            turn.turn(currentPlayer, dice, draw);
            return;
        }

        // Normale Phase
        currentPlayer++;

        if (currentPlayer > names.length){
            currentPlayer = 1;
        }

        updateCurrentPlayerObject();
        draw.currentPlayer = currentPlayer;
        draw.repaint();

        turn.turn(currentPlayer, dice, draw);
    }

    // Update Sachen
    public void updateCurrentPlayerObject(){
        for (Player p : boardfactory.players){
            if (p.getPlayerNumber() == currentPlayer){

                currentPlayerObject = p;
                return;
            }
        }
    }

    public void updateWinningPoints(){
        for (Player p : boardfactory.players){

            p.calculateWinningPoints(boardfactory.settlements, boardfactory.streets, this);

            if (p.getWinningPoints() >= 12){
                setGameOver();
                return;
            }
        }

        draw.repaint();
    }

    public Player getPlayerByNumber(int number){
        for (Player p : boardfactory.players){
            if (p.getPlayerNumber() == number){
                return p;
            }
        }
        return null;
    }

    public void updateLongestRoad(Player player) {

        int length = 0;

        length = WinningPoints.calculateLongestRoad(player.getPlayerNumber(), boardfactory.streets);
        player.setLongestRoad(length);

        if (length >= 5 && length > longestRoadLength) {
            longestRoadLength = length;
            longestRoadOwner = player;
        }
    }

    // get und set
    public Player getLongestRoadOwner() {
        return longestRoadOwner;
    }

    public void setLongestRoadOwner(Player p) {
        longestRoadOwner = p;
    }

    public Player getCurrentPlayer(){
        return currentPlayerObject;
    }
}
