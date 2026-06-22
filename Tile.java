public class Tile {

    private Variables.Resource resource;
    private int number;
    private boolean robber;

    private int centerX;
    private int centerY;

    public Tile(Variables.Resource resource, int number, int x, int y) {
        this.resource = resource;
        this.number = number;
        this.centerX = x;
        this.centerY = y;
    }

    public int getCenterX (){
        return centerX;
    }

    public int getCenterY (){
        return centerY;
    }

    public Variables.Resource getResource (){
        return resource;
    }

    public int getNumber (){
        return number;
    }

    public boolean getRobber() {
        return robber;
    }

    public void setRobber(boolean robber) {
        this.robber = robber;
    }
}