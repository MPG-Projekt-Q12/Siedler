public class Settlement{

    private int centerX;
    private int centerY;
    private int owner = 0;
    private boolean build = false;
    private boolean city = false;

    public Settlement(int centerX, int centerY){
        this.centerX = centerX;
        this.centerY = centerY;
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

    public boolean getCity (){
        return city;
    }

    public void setCity (boolean city){
        this.city = city;
    }

}