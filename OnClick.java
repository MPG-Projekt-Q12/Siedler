import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OnClick extends MouseAdapter {

    private Draw draw;
    private Turn turn;
    private Game game;

    public OnClick(Draw draw, Game game, Turn turn){

        this.draw = draw;
        this.turn = turn;
        this.game = game;
        System.out.println("> onClick");
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        Player player = game.getCurrentPlayer();

        int mx = e.getX();
        int my = e.getY();

        System.out.println("> clicked at " + mx + "/" + my);

        //Next Button
        if (draw.nextButton.contains(mx, my) && turn.waitingForNext) {

            draw.nextButtonPressed = true;
            draw.repaint();

            javax.swing.Timer timer = new javax.swing.Timer(120, ev -> {

                            draw.nextButtonPressed = false;
                            draw.repaint();

                            turn.waitingForNext = false;
                            draw.nextButtonReady = false;

                            game.nextTurn();
                    });

            timer.setRepeats(false);
            timer.start();

            return;
        }

        // robber
        if (turn.waitingForRobber) {

            for (Tile tile : draw.tiles) {

                if (tile.getNumber() == 0) {
                    continue;
                }

                double dist = Math.hypot( mx - tile.getCenterX(), my - tile.getCenterY());

                if (dist <= 30) {

                    if (tile.getRobber()) {
                        System.out.println("Der Räuber steht bereits dort.");
                        return;
                    }

                    for (Tile t : draw.tiles) {
                        t.setRobber(false);
                    }

                    tile.setRobber(true);
                    stealRandomResource(tile, player);
                    turn.waitingForRobber = false;
                    turn.waitingForNext = true;
                    draw.nextButtonReady = true;
                    System.out.println("> robber moved");
                    draw.repaint();
                    return;
                }
            }
        }

        //Settlements
        for (Settlement s : draw.settlements){

            int radius = s.getCity() ? 20 : 14;

            double dist = Math.hypot(mx - s.getCenterX(), my - s.getCenterY());

            if (dist <= radius){
                if (turn.waitingForSettlement && BuildRules.canBuildStartSettlement(s, draw.settlements)) {

                    s.setBuild(true);
                    s.setOwner(player.getPlayerNumber());
                    game.updateWinningPoints();

                    turn.waitingForSettlement = false;
                    turn.waitingForStreet = true;

                    System.out.println("> settlement build");
                    System.out.println("build a street!");

                    draw.repaint();
                    return;
                }
                else if (BuildRules.canBuildSettlement(s, player, draw.settlements, draw.streets)){

                    s.setBuild(true);
                    s.setOwner(player.getPlayerNumber());

                    game.updateLongestRoad(player);
                    game.updateWinningPoints();

                    System.out.println("> settlement build");

                    draw.repaint();
                    return;
                }
                else if (BuildRules.canBuildCity(s, player)){

                    s.setCity(true);

                    game.updateWinningPoints();

                    System.out.println("> city gebaut");

                    draw.repaint();
                    return;
                }
            }
        }

        //Streets
        for (Street s : draw.streets){

            double dist = Math.hypot(mx - s.getCenterX(), my - s.getCenterY());

            if (dist <= 30){
                if (turn.waitingForStreet && BuildRules.canBuildStartStreet(s, player, draw.settlements)) {

                    s.setBuild(true);
                    s.setOwner(player.getPlayerNumber());

                    game.updateLongestRoad(player);
                    game.updateWinningPoints();

                    turn.waitingForStreet = false;
                    turn.waitingForNext = true;
                    draw.nextButtonReady = true;

                    System.out.println("> street build");
                    System.out.println("> next button ready");

                    draw.repaint();
                    return;
                } 
                else if (BuildRules.canBuildStreet(s, player, draw.settlements, draw.streets)){

                    s.setBuild(true);
                    s.setOwner(player.getPlayerNumber());

                    game.updateLongestRoad(player);
                    game.updateWinningPoints();

                    System.out.println("> street build");

                    draw.repaint();
                    return;
                }
            }
        }
    }

    // Steal (Robber)
    private void stealRandomResource(Tile robberTile, Player currentPlayer) {

        java.util.ArrayList<Player> victims = new java.util.ArrayList<>();

        for (Settlement settlement : draw.settlements) {

            if (!settlement.getBuild()) {
                continue;
            }

            if (settlement.getOwner() == currentPlayer.getPlayerNumber()) {
                continue;
            }

            double dist = Math.hypot(robberTile.getCenterX() - settlement.getCenterX(), robberTile.getCenterY() - settlement.getCenterY());

            if (dist < 120) {
                Player victim = game.getPlayerByNumber(settlement.getOwner());

                if (victim != null && !victims.contains(victim)) {
                    victims.add(victim);
                }
            }
        }

        if (victims.isEmpty()) {
            System.out.println("Niemand zum Beklauen gefunden.");
            return;
        }

        Player victim = victims.get((int)(Math.random() * victims.size()));

        java.util.ArrayList<Variables.Resource>
        availableResources = new java.util.ArrayList<>();

        for (Variables.Resource r :
        Variables.Resource.values()) {

            if (r == Variables.Resource.DESERT || r == Variables.Resource.DEFAULT) {
                continue;
            }

            if (victim.getResource(r) > 0) {
                availableResources.add(r);
            }
        }

        if (availableResources.isEmpty()) {

            System.out.println(victim.getPlayerName() + " hat keine Ressourcen.");
            return;
        }

        Variables.Resource stolen = availableResources.get((int)(Math.random() * availableResources.size()));

        victim.addResource(stolen, -1);
        currentPlayer.addResource(stolen, 1);

        System.out.println(currentPlayer.getPlayerName() + " klaut 1 " + stolen + " von " + victim.getPlayerName());
    }
}