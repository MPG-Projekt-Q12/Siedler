public class Street{

    public int centerx;
    public int centery;
    public int owner = 0;
    public double angle;
    public boolean build = false;

    public Street(int x, int y, double angle){
        this.centerx = x;
        this.centery = y;
        this.angle = angle;
    }

    public void build(){
        build = true;
    }
}