import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Collections;
import java.util.Map;

public class Bauten
{
    private List<Stadt> staedte = new ArrayList<>();

    public void erstellen()
    {
        HashMap<Punkt, Punkt> eckpunkte = HexFeld.getEckpunkte();
        for (Map.Entry<Punkt, Punkt> entry : eckpunkte.entrySet()) {
            Punkt p = entry.getKey();

            staedte.add(new Stadt(p.x, p.y, 0));
        } 
    }
}