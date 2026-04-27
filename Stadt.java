import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;

public class Stadt {
    private int id;
    private List<Stadt> neighbors = new ArrayList<>();
    private Player buildingOwner = null;
    private int mpx;
    private int mpy;

    public Stadt(int id, int mpx, int mpy) {
        this.id = id;
        this.mpx = mpx;
        this.mpy = mpy;
    }

    public int getId() {
        return id;
    }

    public List<Stadt> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(Stadt node) {
        neighbors.add(node);
    }

    public Player getBuildingOwner() {
        return buildingOwner;
    }

    public void setBuildingOwner(Player player) {
        this.buildingOwner = player;
    }

    @Override
    public String toString() {
        return "Node(" + id + ")";
    }
}