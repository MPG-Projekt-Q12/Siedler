import java.util.ArrayList;
import java.util.Collections;

public class Variables{
    public static final Integer[] numbers = {
            9,3,11,6,5,4,10,8,4,5,12,9,10,8,3,6,2,11
        };
    public static int boardcenterx;
    public static int boardcentery;

    public enum Resource{
        SHEEP,
        WOOD,
        WHEAT,
        BRICK,
        STONE,
        DESERT,
        DEFAULT
    }
    public enum TradeState {
        NONE,
        EDIT,
        WAITING
    }
}
