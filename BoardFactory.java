import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BoardFactory {

    public ArrayList<Tile> tiles = new ArrayList<>();
    public ArrayList<Street> streets = new ArrayList<>();
    public ArrayList<Settlement> settlements = new ArrayList<>();
    public ArrayList<Player> players = new ArrayList<>();

    private Draw draw;

    public BoardFactory(Draw draw) {
        this.draw = draw;
    }

    //Board

    public void createBoard(int w, int h, String[] playerNames) {

        tiles.clear();
        streets.clear();
        settlements.clear();
        players.clear();

        System.out.println("> arrays cleared");

        int cx = w / 2;
        int cy = h / 2;

        List<Variables.Resource> resources = new ArrayList<>();
        List<Integer> numbers = new ArrayList<>(Arrays.asList(Variables.numbers));
        Collections.shuffle(numbers);

        System.out.println("> numbers shuffled");

        resources.addAll(Arrays.asList(
                Variables.Resource.SHEEP, 
                Variables.Resource.SHEEP, 
                Variables.Resource.SHEEP, 
                Variables.Resource.SHEEP,
                Variables.Resource.WOOD, 
                Variables.Resource.WOOD, 
                Variables.Resource.WOOD, 
                Variables.Resource.WOOD,
                Variables.Resource.WHEAT, 
                Variables.Resource.WHEAT, 
                Variables.Resource.WHEAT, 
                Variables.Resource.WHEAT,
                Variables.Resource.BRICK, 
                Variables.Resource.BRICK, 
                Variables.Resource.BRICK,
                Variables.Resource.STONE, 
                Variables.Resource.STONE, 
                Variables.Resource.STONE,
                Variables.Resource.DESERT
            ));
        Collections.shuffle(resources);

        System.out.println("> resources shuffled");

        int resourceIndex = 0;
        int numberIndex = 0;
        int[] rowLengths = {3, 4, 5, 4, 3};
        int radius = 80;

        double hStep = Math.sqrt(3) * radius;
        double vStep = 1.5 * radius;

        double startY = cy - 2 * vStep;

        for (int r = 0; r < rowLengths.length; r++) {

            int rowLen = rowLengths[r];

            double rowWidth = (rowLen - 1) * hStep;

            double startX = cx - rowWidth / 2;

            for (int c = 0; c < rowLen; c++) {

                int x = (int)(startX + c * hStep);
                int y = (int)(startY + r * vStep);

                Variables.Resource resource = resources.get(resourceIndex);

                int number = 0;

                if (resource != Variables.Resource.DESERT) {
                    number = numbers.get(numberIndex);
                    numberIndex++;
                }

                Tile tile = new Tile(resource, number, x, y);
                if(resource == Variables.Resource.DESERT){
                    tile.setRobber(true);
                }

                tiles.add(tile);

                createSettlements(tile, radius);
                createStreets(tile, radius);

                resourceIndex++;
            }
        }

        for (int i = 0; i < playerNames.length; i++) { 
            createPlayer((i + 1), playerNames[i]);
        }

        draw.setData(tiles, streets, settlements, players);
        draw.repaint();
    }

    //Settlements

    public void createSettlements(Tile tile, int radius) {
        for (int i = 0; i < 6; i++) {

            double angle = Math.toRadians(60 * i - 30);

            int vx = (int)(tile.getCenterX() + radius * Math.cos(angle));
            int vy = (int)(tile.getCenterY() + radius * Math.sin(angle));

            boolean exists = false;

            for (Settlement s : settlements) {
                if (distance(s.getCenterX(), s.getCenterY(), vx, vy) < 5) {

                    exists = true;
                    break;
                }
            }

            if (!exists) {

                Settlement settlement = new Settlement(vx, vy);
                settlement.setBuild(false);
                settlements.add(settlement);
            }
        }
    }

    //Streets

    public void createStreets(Tile tile, int radius) {

        for (int i = 0; i < 6; i++) {

            double a1 = Math.toRadians(60 * i - 30);
            double a2 = Math.toRadians(60 * (i + 1) - 30);

            int x1 = (int)(tile.getCenterX() + radius * Math.cos(a1));
            int y1 = (int)(tile.getCenterY() + radius * Math.sin(a1));
            int x2 = (int)(tile.getCenterX() + radius * Math.cos(a2));
            int y2 = (int)(tile.getCenterY() + radius * Math.sin(a2));

            int mx = (x1 + x2) / 2;
            int my = (y1 + y2) / 2;

            double angle = Math.atan2(y2 - y1, x2 - x1);

            boolean exists = false;

            for (Street s : streets) {

                if (distance(s.getCenterX(), s.getCenterY(), mx, my) < 5) {

                    exists = true;
                    break;
                }
            }

            if (!exists) {

                Street street = new Street(mx, my, angle);
                street.setBuild(false);
                streets.add(street);
            }
        }
    }

    //Helper

    public double distance(int x1, int y1, int x2, int y2) {

        return Math.hypot(x1 - x2, y1 - y2);
    }

    //Player

    public void createPlayer(int number, String name){

        Player player = new Player(number, name);
        players.add(player);
    }
}
