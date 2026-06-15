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

        System.out.println("> mouse r");
        
        Player player = game.getCurrentPlayer();

        int mx = e.getX();
        int my = e.getY();

        //Next Button
        if (draw.nextButton.contains(mx, my) && turn.waitingForNext) {

            System.out.println("> next button clicked");
            
            turn.waitingForNext = false;

            game.nextTurn();

            return;
        }

        //Settlements
        for (Settlement s : draw.settlements){

            int radius = s.getCity() ? 20 : 14;

            double dist = Math.hypot(
                    mx - s.getCenterY(),
                    my - s.getCenterX()
                );

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

                    System.out.println("> street build");

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

                    System.out.println("Straße gebaut");
                    System.out.println("Weiter-Button jetzt verfügbar");

                    draw.repaint();
                    return;
                } 
                else if (BuildRules.canBuildStreet(s, player, draw.settlements, draw.streets)){

                    s.setBuild(true);
                    s.setOwner(player.getPlayerNumber());

                    game.updateLongestRoad(player);
                    game.updateWinningPoints();

                    System.out.println("Straße gebaut");

                    draw.repaint();
                    return;
                }
            }
        }
    }
}