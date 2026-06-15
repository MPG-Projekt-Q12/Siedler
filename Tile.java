public class Tile {

    private Variables.Resource resource;
    private int number;

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

    public void setCenterX (int centerX){
        this.centerX = centerX;
    }

    public int getCenterY (){
        return centerY;
    }

    public void setCenterY (int centerY){
        this.centerY = centerY;
    }

    public Variables.Resource getResource (){
        return resource;
    }

    public void setResource (Variables.Resource resource){
        this.resource = resource;
    }

    public int getNumber (){
        return number;
    }

    public void setNumber (int number){
        this.number = number;
    }

}