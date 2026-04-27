import javax.swing.*;
import java.awt.*;

public class Strasse {
    private Stadt nodeA;
    private Stadt nodeB;
    private Player owner = null;
    private int mittelpunkt;

    public Strasse(Stadt a, Stadt b) {
        this.nodeA = a;
        this.nodeB = b;
    }

    public Stadt getNodeA() {
        return nodeA;
    }

    public Stadt getNodeB() {
        return nodeB;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public boolean connects(Stadt node) {
        return node.equals(nodeA) || node.equals(nodeB);
    }

    @Override
    public String toString() {
        return "Road(" + nodeA.getId() + "-" + nodeB.getId() + ")";
    }
}