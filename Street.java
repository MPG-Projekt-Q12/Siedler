public class Street{

    public int centerX;
    public int centerY;
    public int owner = 0;
    public double angle;
    public boolean build = false;

    public Street(int x, int y, double angle){
        this.centerX = x;
        this.centerY = y;
        this.angle = angle;
    }

    public int getCenterX (){
        return centerX;
    }

    public void setCenterX (int centerX){
        this.centerX = centerX;
    }

    public int getCenterY (){
        return centerY;
    }

    public void setCenterY (int centerY){
        this.centerY = centerY;
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
}