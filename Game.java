import javax.swing.JOptionPane;

public class Game {

    private BoardFactory boardfactory;
    private Turn turn;
    private Dice dice;
    private Draw draw;
    private String[] names;
    private Player currentPlayerObject;
    private int currentPlayer;
    private int setupPhase = 1;
    private int setupIndex = 1;
    private Player longestRoadOwner = null;
    private int longestRoadLength = 0;

    private boolean gameOver = false;

    public Game(Draw draw){

        this.draw = draw;
        this.boardfactory = new BoardFactory(draw);
        this.turn = new Turn();
        this.dice = new Dice();

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

    public void setGameOver(){

        gameOver = true;

        Player winner = getCurrentPlayer();

        JOptionPane.showMessageDialog(
            null,
            winner.name + " hat gewonnen!",
            "Spiel beendet",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public void nextTurn(){

        if (gameOver){
            return;
        }

        // ---------------- STARTPHASE 1 ----------------
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

            // Wechsel zu Phase 2
            setupPhase = 2;
            setupIndex = names.length;

            currentPlayer = setupIndex;
            updateCurrentPlayerObject();
            draw.currentPlayer = currentPlayer;
            draw.repaint();

            turn.startTurn2(currentPlayer);

            return;
        }

        // ---------------- STARTPHASE 2 ----------------
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

            // normales Spiel beginnt
            setupPhase = 3;

            currentPlayer = 1;
            updateCurrentPlayerObject();
            draw.currentPlayer = currentPlayer;
            draw.repaint();

            turn.turn(currentPlayer, dice, draw);

            return;
        }

        // ---------------- NORMALES SPIEL ----------------
        currentPlayer++;

        if (currentPlayer > names.length){
            currentPlayer = 1;
        }

        updateCurrentPlayerObject();
        draw.currentPlayer = currentPlayer;
        draw.repaint();

        turn.turn(currentPlayer, dice, draw);
    }

    public void updateCurrentPlayerObject(){
        for (Player p : boardfactory.players){
            if (p.playerNumber == currentPlayer){

                currentPlayerObject = p;
                return;
            }
        }
    }

    public Player getCurrentPlayer(){

        return currentPlayerObject;
    }

    public void updateWinningPoints(){

        for (Player p : boardfactory.players){

            p.calculateWinningPoints(
                boardfactory.settlements,
                boardfactory.streets,
                this
            );
        }

        draw.repaint();
    }

    public Player getLongestRoadOwner() {
        return longestRoadOwner;
    }

    public void setLongestRoadOwner(Player p) {
        longestRoadOwner = p;
    }

    public Player getPlayerByNumber(int number){

        for (Player p : boardfactory.players){
            if (p.playerNumber == number){
                return p;
            }
        }
        return null;
    }

    public void updateLongestRoad(){

        Player bestPlayer = null;
        int bestLength = 0;

        for (Player p : boardfactory.players){

            int length = WinningPoints.calculateLongestRoad(
                    p.playerNumber,
                    boardfactory.streets
                );

            p.longestRoad = length;

            if (length > bestLength && length >= 5){
                bestLength = length;
                bestPlayer = p;
            }
        }

        if (bestPlayer != null && bestPlayer != longestRoadOwner){

            if (longestRoadOwner != null){
                longestRoadOwner.hasLongestRoad = false;
            }

            longestRoadOwner = bestPlayer;
            longestRoadOwner.hasLongestRoad = true;
        }
    }
}