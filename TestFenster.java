import javax.swing.*;
import java.awt.*;

public class TestFenster {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Catan Feld");
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        frame.add(new HexFeld());

        frame.setVisible(true);
    }
}
