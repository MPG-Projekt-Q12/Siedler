import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Rectangle;

public class OnClick extends MouseAdapter {

    private Draw draw;
    private Turn turn;
    private Game game;
    private Robber robber;

    public OnClick(Draw draw, Game game, Turn turn, Robber robber){

        this.draw = draw;
        this.turn = turn;
        this.game = game;
        this.robber = robber;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        Player player = game.getCurrentPlayer();

        int mx = (int)((e.getX() - draw.getOffsetX()) / draw.getScale());
        int my = (int)((e.getY() - draw.getOffsetY()) / draw.getScale());

        //Next Button
        if (draw.nextButton.contains(mx, my) && turn.waitingForNext && game.getTradeState() == Variables.TradeState.NONE) {

            turn.waitingForNext = false;
            draw.nextButtonReady = false;

            game.nextTurn();

            return;
        }
        // Robber
        if (turn.waitingForRobber) {

            for (Tile tile : draw.tiles) {

                if (tile.getNumber() == 0) {
                    continue;
                }

                double dist = Math.hypot( mx - tile.getCenterX(), my - tile.getCenterY());

                if (dist <= 30) {

                    if (tile.getRobber()) {
                        game.addConsole("Der Räuber steht bereits dort.");

                        return;
                    }

                    for (Tile t : draw.tiles) {
                        t.setRobber(false);
                    }

                    tile.setRobber(true);
                    robber.stealRandomResource(tile, player, game);
                    turn.waitingForRobber = false;
                    turn.waitingForNext = true;
                    draw.nextButtonReady = true;
                    draw.repaint();
                    return;
                }
            }
        }

        // Settlements
        for (Settlement s : draw.settlements){

            int radius = s.getCity() ? 20 : 14;

            double dist = Math.hypot(mx - s.getCenterX(), my - s.getCenterY());

            if (dist <= radius){
                if (turn.waitingForSettlement && BuildRules.canBuildStartSettlement(s, draw.settlements, game)) {

                    s.setBuild(true);
                    s.setOwner(player.getPlayerNumber());
                    game.updateWinningPoints();

                    turn.waitingForSettlement = false;
                    turn.waitingForStreet = true;

                    game.addConsole("Baue eine Straße");

                    draw.repaint();
                    return;
                }
                else if (BuildRules.canBuildSettlement(s, player, draw.settlements, draw.streets, game)){

                    s.setBuild(true);
                    s.setOwner(player.getPlayerNumber());

                    game.updateLongestRoad(player);
                    game.updateWinningPoints();

                    draw.repaint();
                    return;
                }
                else if (BuildRules.canBuildCity(s, player)){

                    s.setCity(true);

                    game.updateWinningPoints();

                    draw.repaint();
                    return;
                }
            }
        }

        // Streets
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

                    draw.repaint();
                    return;
                } 
                else if (BuildRules.canBuildStreet(s, player, draw.settlements, draw.streets)){

                    s.setBuild(true);
                    s.setOwner(player.getPlayerNumber());

                    game.updateLongestRoad(player);
                    game.updateWinningPoints();

                    draw.repaint();
                    return;
                }
            }
        }

        // Trades
        if (game.getTradeState() == Variables.TradeState.NONE && !turn.waitingForRobber) {
            for (int i = 0; i < draw.tradeTargets.size(); i++) {

                Rectangle r = draw.tradeTargets.get(i);

                if (r.contains(mx, my)) {
                    if (i <
                    draw.players.size() - 1) {

                        Player other = game.getTradePlayerByIndex(i);
                        game.setSelectedTradeTarget(other.getPlayerNumber());
                        game.setCurrentTrade(new Trade(game.getCurrentPlayer(),other));
                    }
                    else {

                        game.setSelectedTradeTarget(0);
                        game.setCurrentTrade(new Trade(game.getCurrentPlayer(),null));
                    }
                    game.setTradeState(Variables.TradeState.EDIT);
                    draw.repaint();
                    return;
                }
            }
        }

        for (int i = 0; i < 5; i++) {

            Variables.Resource r = Variables.Resource.values()[i];

            if (draw.plusButtonsTop.get(i).contains(mx, my)) {

                game.getCurrentTrade().addFromGives(r, 1);
                draw.repaint();
                return;
            }

            if (draw.minusButtonsTop.get(i).contains(mx, my)) {

                game.getCurrentTrade().addFromGives(r, -1);
                draw.repaint();
                return;
            }
        }

        for (int i = 0; i < 5; i++) {

            Variables.Resource r = Variables.Resource.values()[i];

            if (draw.plusButtonsBottom.get(i).contains(mx, my)) {

                game.getCurrentTrade().addToGives(r, 1);
                draw.repaint();
                return;
            }

            if (draw.minusButtonsBottom.get(i).contains(mx, my)) {

                game.getCurrentTrade().addToGives(r, -1);
                draw.repaint();
                return;
            }
        }

        if (draw.tradeOkButton.contains(mx, my)) {

            Trade trade = game.getCurrentTrade();

            if (game.getSelectedTradeTarget() == 0) {

                if (!trade.canExecuteBankTrade()) {
                    return;
                }

            } else {

                if (!trade.canExecute()) {
                    return;
                }
            }

            trade.execute();

            game.setTradeState(Variables.TradeState.NONE);
            game.setCurrentTrade(null);
            game.setSelectedTradeTarget(-1);

            draw.repaint();
            return;
        }

        if (draw.tradeCancelButton.contains(mx, my)) {

            game.setTradeState(Variables.TradeState.NONE);
            game.setCurrentTrade(null);
            game.setSelectedTradeTarget(-1);

            draw.repaint();
            return;
        }
    }
}
