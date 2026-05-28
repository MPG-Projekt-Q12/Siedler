import java.awt.Color;

public class Dice {

    public int dice1 = 1;
    public int dice2 = 1;

    public int sum = 2;

    public boolean rolling = false;

    public void rollDice(Draw draw) {

        new Thread(() -> {

            rolling = true;

            // kurze Rollanimation
            for (int i = 0; i < 12; i++) {

                dice1 =
                    (int)(Math.random() * 6) + 1;

                dice2 =
                    (int)(Math.random() * 6) + 1;

                draw.repaint();

                try {

                    Thread.sleep(70);

                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            // finaler Wurf
            dice1 = (int)(Math.random() * 6) + 1;
            dice2 = (int)(Math.random() * 6) + 1;

            sum = dice1 + dice2;

            rolling = false;

            draw.repaint();

        }).start();
    }

    public int getDiceSum() {
        return sum;
    }
}