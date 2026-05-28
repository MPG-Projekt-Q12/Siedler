public class Settlement{

    public int centerx;
    public int centery;
    public int owner = 0;
    public boolean build = false;
    public boolean city = false;

    public Settlement(int centerx, int centery){
        this.centerx = centerx;
        this.centery = centery;
    }

    public void upgradetocity(){
        city = true;
    }

}