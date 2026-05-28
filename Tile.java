public class Tile {

    public Variables.Resource resource;
    public int number;

    public int centerx;
    public int centery;

    public Tile(Variables.Resource resource, int number, int x, int y) {
        this.resource = resource;
        this.number = number;
        this.centerx = x;
        this.centery = y;
    }
}