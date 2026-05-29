import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OnClick extends MouseAdapter {

    private Draw draw;

    public OnClick(Draw draw){

        this.draw = draw;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        int mx = e.getX();
        int my = e.getY();

        //Next Button
        if (draw.nextButton.contains(mx, my)) {

            System.out.println("Weiter Button geklickt");

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

                System.out.println("Settlement angeklickt");

                s.build = true;

                draw.repaint();

                return;
            }
        }

        //Streets
        for (Street s : draw.streets){

            double dist = Math.hypot(
                    mx - s.centerx,
                    my - s.centery
                );

            if (dist <= 30){

                System.out.println("Straße angeklickt");

                s.build = true;

                draw.repaint();

                return;
            }
        }
    }
}