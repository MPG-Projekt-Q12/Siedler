import javax.swing.JOptionPane;
import java.util.ArrayList;

public class Game {

    private BoardFactory boardfactory;
    private Turn turn;
    private Dice dice;
    private Draw draw;
    private Robber robber;
    private String[] names;
    private Player currentPlayerObject;
    private Player longestRoadOwner = null;
    private int currentPlayer;
    private int setupPhase = 1;
    private int setupIndex = 1;
    private int longestRoadLength = 0;
    private boolean gameOver = false;
    private int maxWinningPoints;
    private int selectedTradeTarget = -1;
    private Trade currentTrade;
    private Variables.TradeState tradeState = Variables.TradeState.NONE;
    private ArrayList<String> console = new ArrayList<>();

    public Game(Draw draw){

        this.draw = draw;
        this.boardfactory = new BoardFactory(draw);
        this.turn = new Turn();
        this.dice = new Dice();
        this.robber = new Robber();

        draw.setGame(this);
        draw.dice = dice;
        draw.addMouseListener(new OnClick(draw, this, turn, robber));
    }

    public void newGame(int x, int y, String[] names, int maxWinningPoints){

        addConsole("Spiel gestartet");

        this.names = names;
        this.maxWinningPoints = maxWinningPoints;

        boardfactory.createBoard(x, y, names);

        currentPlayer = 1;
        updateCurrentPlayerObject();
        addConsole("----- " + currentPlayerObject.getPlayerName() + " ist ab Zug -----");
        draw.currentPlayer = currentPlayer;
        draw.repaint();

        turn.startTurn1(currentPlayer, this);
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

        if (setupPhase == 1){
            setupIndex++;
            if (setupIndex <= names.length){

                currentPlayer = setupIndex;
                updateCurrentPlayerObject();
                addConsole("----- " + currentPlayerObject.getPlayerName() + " ist ab Zug -----");
                draw.currentPlayer = currentPlayer;
                draw.repaint();

                turn.startTurn1(currentPlayer, this);

                return;
            }
            setupPhase = 2;
            setupIndex = names.length;
            currentPlayer = setupIndex;
            updateCurrentPlayerObject();
            addConsole("----- " + currentPlayerObject.getPlayerName() + " ist ab Zug -----");
            draw.currentPlayer = currentPlayer;
            draw.repaint();

            turn.startTurn2(currentPlayer, this);
            return;
        }

        if (setupPhase == 2){
            setupIndex--;
            
            if (setupIndex >= 1){

                currentPlayer = setupIndex;
                updateCurrentPlayerObject();
                addConsole("----- " + currentPlayerObject.getPlayerName() + " ist ab Zug -----");
                draw.currentPlayer = currentPlayer;
                draw.repaint();

                turn.startTurn2(currentPlayer, this);
                return;
            }
            setupPhase = 3;

            currentPlayer = 1;
            updateCurrentPlayerObject();
            addConsole("----- " + currentPlayerObject.getPlayerName() + " ist ab Zug -----");
            draw.currentPlayer = currentPlayer;
            draw.repaint();

            turn.turn(currentPlayer, dice, draw, this);
            return;
        }
        currentPlayer++;

        if (currentPlayer > names.length){
            currentPlayer = 1;
        }
        updateCurrentPlayerObject();
        addConsole("----- " + currentPlayerObject.getPlayerName() + " ist ab Zug -----");
        draw.currentPlayer = currentPlayer;
        draw.repaint();

        turn.turn(currentPlayer, dice, draw, this);
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

            if (p.getWinningPoints() >= maxWinningPoints){
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
    public void addConsole(String text) {
        console.add(text);

        if (console.size() > 12) {
            console.remove(0);
        }

        draw.repaint();
    }

    public ArrayList<String> getConsole() {
        System.out.println("GET: " + console.hashCode());
        return console;
    }

    public Player getLongestRoadOwner() {
        return longestRoadOwner;
    }

    public void setLongestRoadOwner(Player p) {
        longestRoadOwner = p;
    }

    public Player getCurrentPlayer(){
        return currentPlayerObject;
    }

    public void setMaxWinningPoints(int maxWinningPoints) {
        maxWinningPoints = maxWinningPoints;
    }

    public Trade getCurrentTrade() {
        return currentTrade;
    }

    public void setCurrentTrade(Trade trade) {
        this.currentTrade = trade;
    }

    public Variables.TradeState getTradeState() {
        return tradeState;
    }

    public void setTradeState(Variables.TradeState tradeState) {
        this.tradeState = tradeState;
    }

    public int getSelectedTradeTarget() {
        return selectedTradeTarget;
    }

    public void setSelectedTradeTarget(int selectedTradeTarget) {
        this.selectedTradeTarget = selectedTradeTarget;
    }

    public ArrayList<Settlement> getSettlements() {
        return boardfactory.settlements;
    }

    public Player getTradePlayerByIndex(int index) {

        int current = currentPlayerObject.getPlayerNumber();
        int counter = 0;

        for (Player p : boardfactory.players) {

            if (p.getPlayerNumber() == current) {
                continue;
            }

            if (counter == index) {
                return p;
            }

            counter++;
        }

        return null;
    }
}
