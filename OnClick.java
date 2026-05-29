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
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        Player player = game.getCurrentPlayer();

        int mx = e.getX();
        int my = e.getY();

        //Next Button
        if (draw.nextButton.contains(mx, my) && turn.waitingForNext) {

            System.out.println("Weiter Button geklickt");

            turn.waitingForNext = false;

            game.nextTurn();

            return;
        }

        //Settlements
        for (Settlement s : draw.settlements){

            int radius = s.city ? 20 : 14;

            double dist = Math.hypot(
                    mx - s.centerx,
                    my - s.centery
                );

            if (dist <= radius){
                if (turn.waitingForSettlement
                && BuildRules.canBuildStartSettlement(
                    s,
                    draw.settlements)) {

                    s.build = true;
                    s.owner = player.playerNumber;
                    game.updateWinningPoints();

                    turn.waitingForSettlement = false;
                    turn.waitingForStreet = true;

                    System.out.println("Settlement gebaut");
                    System.out.println("Baue jetzt eine Straße");

                    draw.repaint();
                    return;
                }
                else if (BuildRules.canBuildSettlement(s, player, draw.settlements, draw.streets)){

                    s.build = true;
                    s.owner = player.playerNumber;

                    game.updateLongestRoad();
                    game.updateWinningPoints();

                    System.out.println("Settlement gebaut");

                    draw.repaint();
                    return;
                }
                else if (BuildRules.canBuildCity(s, player)){

                    s.city = true;

                    game.updateWinningPoints();

                    System.out.println("City gebaut");

                    draw.repaint();
                    return;
                }
            }
        }

        //Streets
        for (Street s : draw.streets){

            double dist = Math.hypot(
                    mx - s.centerx,
                    my - s.centery
                );

            if (dist <= 30){
                if (turn.waitingForStreet
                && BuildRules.canBuildStartStreet(
                    s,
                    player,
                    draw.settlements)) {
                    s.build = true;
                    s.owner = player.playerNumber;

                    game.updateLongestRoad();
                    game.updateWinningPoints();

                    turn.waitingForStreet = false;
                    turn.waitingForNext = true;

                    System.out.println("Straße gebaut");
                    System.out.println("Weiter-Button jetzt verfügbar");

                    draw.repaint();
                    return;
                } 
                else if (BuildRules.canBuildStreet(s, player, draw.settlements, draw.streets)){

                    s.build = true;
                    s.owner = player.playerNumber;

                    game.updateLongestRoad();
                    game.updateWinningPoints();

                    System.out.println("Straße gebaut");

                    draw.repaint();
                    return;
                }
            }
        }
    }
}