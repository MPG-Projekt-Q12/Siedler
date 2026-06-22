public class Street{

    private int centerX;
    private int centerY;
    private int owner = 0;
    private double angle;
    private boolean build = false;

    public Street(int x, int y, double angle){
        this.centerX = x;
        this.centerY = y;
        this.angle = angle;
    }

    public int getCenterX (){
        return centerX;
    }

    public int getCenterY (){
        return centerY;
    }

    public int getOwner (){
        return owner;
    }

    public void setOwner (int owner){
        this.owner = owner;
    }

    public boolean getBuild (){
        return build;
    }

    public void setBuild (boolean build){
        this.build = build;
    }

    public double getAngle (){
        return angle;
    }

    public void setAngle (double angle){
        this.angle = angle;
    }
}