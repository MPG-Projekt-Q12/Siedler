import javax.swing.JPanel;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.geom.RoundRectangle2D;

public class Draw extends JPanel {

    private BufferedImage sheepTexture;
    private BufferedImage woodTexture;
    private BufferedImage wheatTexture;
    private BufferedImage brickTexture;
    private BufferedImage stoneTexture;
    private BufferedImage desertTexture;

    ArrayList<Tile> tiles = new ArrayList<>();
    ArrayList<Street> streets = new ArrayList<>();
    ArrayList<Settlement> settlements = new ArrayList<>();
    ArrayList<Player> players = new ArrayList<>();

    public Dice dice;

    Color colorPlayer1 = new Color(0xFF7272);
    Color colorPlayer2 = new Color(0, 102, 204);
    Color colorPlayer3 = new Color(76, 187, 23);
    Color colorPlayer4 = new Color(250, 240, 53);

    Color[] playerColors = {
            Color.GRAY,
            colorPlayer1,
            colorPlayer2,
            colorPlayer3,
            colorPlayer4,
        };

    Rectangle nextButton;

    public int currentPlayer;
    public boolean nextButtonPressed = false;
    public boolean nextButtonReady = false;

    private Game game;

    // Paint
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);

        for (Tile t : tiles) drawTile(g, t);
        for (Street s : streets){
            if (s.getBuild()){
                drawStreet(g, s);
            }
        }
        for (Settlement s : settlements){
            if (s.getBuild()){
                drawSettlement(g, s);
            }
        }
        for (Player p : players) drawPlayer(g, p);

        drawDice(g);
        drawNextButton(g);
    }

    // Daten vom Factory setzen
    public void setData(ArrayList<Tile> t, ArrayList<Street> s, ArrayList<Settlement> se, ArrayList<Player> p) {
        this.tiles = t;
        this.streets = s;
        this.settlements = se;
        this.players = p;
    }

    // Background
    public void drawBackground(Graphics g) {
        g.setColor(new Color(200, 230, 255));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    // Tile 
    public void drawTile(Graphics g, Tile tile) {
        drawHexagon(g, tile);
        drawNumberCircle(g, tile);
    }

    public void drawHexagon(Graphics g, Tile tile) {

        int radius = 80;

        int[] xPoints = new int[6];
        int[] yPoints = new int[6];

        for (int i = 0; i < 6; i++) {

            double angle = Math.toRadians(60 * i - 30);

            xPoints[i] = (int)(tile.getCenterX() + radius * Math.cos(angle));
            yPoints[i] = (int)(tile.getCenterY() + radius * Math.sin(angle));
        }

        Polygon hex = new Polygon(xPoints, yPoints, 6);
        Graphics2D g2 = (Graphics2D) g;
        Shape oldClip = g2.getClip();
        g2.setClip(hex);
        BufferedImage texture = getResourceTexture(tile.getResource());

        if (texture != null) {
            g2.drawImage(texture, tile.getCenterX() - radius, tile.getCenterY() - radius, radius * 2, radius * 2, null);
        } else {
            g2.setColor(getResourceColor(tile.getResource()));
            g2.fillPolygon(hex);
        }

        g2.setClip(oldClip);

        g.setColor(Color.BLACK);
        g.drawPolygon(hex);
    }

    public void drawNumberCircle(Graphics g, Tile tile) {
        int r = 30;
        int zahl = tile.getNumber();

        if (zahl == 0) return;

        Rectangle bounds = new Rectangle(tile.getCenterX() - r * 3 / 2 , tile.getCenterY() - r * 3 / 2, r * 3, r * 3);

        int textX = bounds.x + bounds.width / 2;
        int textY = bounds.y + bounds.height / 2;

        Graphics2D g3 = (Graphics2D) g;

        g3.setColor(new Color(255, 255, 255, 180));
        g.fillOval(textX - r, textY - r, r * 2, r * 2);
        g.setColor(Color.BLACK);
        g.drawOval(textX - r, textY - r, r * 2, r * 2);

        if(tile.getRobber()){

            Graphics2D g2 = (Graphics2D) g;
            Stroke old = g2.getStroke();

            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(6));
            g2.drawOval(textX - r - 5, textY - r - 5, r * 2 + 10, r * 2 + 10);
            g2.setStroke(old);
        }

        if (zahl == 6 || zahl == 8) {
            g.setFont(new Font("Arial", Font.BOLD, 44));
            g.setColor(Color.RED);
        } else {
            g.setFont(new Font("Arial", Font.BOLD, 34));
            g.setColor(Color.BLACK);
        }

        String text = String.valueOf(zahl);
        FontMetrics fm = g.getFontMetrics();

        int tx = textX - fm.stringWidth(text) / 2;
        int ty = textY + fm.getAscent() / 2 - 2;

        g.drawString(text, tx, ty);
    }

    // Street
    public void drawStreet(Graphics g, Street s) {

        Graphics2D g2 = (Graphics2D) g;

        int length = 60;
        int thickness = 10;

        AffineTransform old = g2.getTransform();

        g2.translate(s.getCenterX(), s.getCenterY());
        g2.rotate(s.getAngle());
        g2.setColor(getFarbe(s.getOwner()));
        g2.fillRect(-length / 2, -thickness / 2, length, thickness);
        g2.setColor(Color.BLACK);
        g2.drawRect(-length / 2, -thickness / 2, length, thickness);
        g2.setTransform(old);
    }

    // Settlement
    public void drawSettlement(Graphics g, Settlement s) {

        int radius = s.getCity() ? 20 : 14;

        g.setColor(getFarbe(s.getOwner()));
        g.fillOval(s.getCenterX() - radius, s.getCenterY() - radius, radius * 2, radius * 2);
        g.setColor(Color.BLACK);
        g.drawOval(s.getCenterX() - radius, s.getCenterY() - radius, radius * 2, radius * 2);
    }

    // Player
    public void drawPlayer(Graphics g, Player player) {

        int startX = 60;
        int startY = 60;
        int width = 500;
        int height = 250;
        int distanceToEdges = 30;
        int x;
        int y;

        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g.getFontMetrics();

        int textWidth = fm.stringWidth(player.getPlayerName());
        int textHeight = fm.getHeight();

        String pointsText = "Punkte: " + player.getWinningPoints();

        if (player.getPlayerNumber() == 2){
            x = getWidth() - width - startX;
            y = startY;
        }
        else if (player.getPlayerNumber() == 3){
            x = startX;
            y = getHeight() - height - startY;
        }
        else if (player.getPlayerNumber() == 4){
            x = getWidth() - width - startX;
            y = getHeight() - height - startY;
        }
        else {
            x = startX;
            y = startY;
        }

        g.setColor(playerColors[player.getPlayerNumber()]);
        g.fillRoundRect(x, y, width, height, 25, 25);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, width, height, 25, 25);
        g.setColor(Color.BLACK);
        g.fillRoundRect(x + distanceToEdges / 2, y + height - textHeight - distanceToEdges * 3 / 2 + 5 , width - distanceToEdges, textHeight + distanceToEdges, 20, 20);
        g.setColor(Color.WHITE);
        g.drawString(pointsText, x + distanceToEdges, y + height - distanceToEdges);

        if (game != null
        && game.getLongestRoadOwner() == player) {

            int iconX = x + distanceToEdges + g.getFontMetrics().stringWidth(pointsText) + 20;
            int iconY = y + height - distanceToEdges - 10;

            g.setColor(playerColors[player.getPlayerNumber()]);
            g.fillRect(iconX, iconY, 40, 8);
            g.setColor(Color.BLACK);
            g.drawRect(iconX, iconY, 40, 8);
            g.drawLine(iconX, iconY + 4, iconX - 8, iconY - 4);
            g.drawLine(iconX + 40, iconY + 4, iconX + 48, iconY - 4);
        }

        g.setColor(Color.WHITE);
        g.drawString(player.getPlayerName(), x + width - distanceToEdges - textWidth, y + height - distanceToEdges);

        drawPlayerCards(g, player, x + distanceToEdges, y + distanceToEdges);

    }

    public void drawPlayerCards(Graphics g, Player player, int x, int y) {

        int cardWidth = 70;
        int cardHeight = 100;
        int spacing = 10;

        Variables.Resource[] res = Variables.Resource.values();

        for (int i = 0; i < res.length - 2; i++) {

            Variables.Resource r = res[i];

            int cardX = x + i * (cardWidth + spacing);

            Graphics2D g2 = (Graphics2D) g;

            BufferedImage texture = getResourceTexture(r);
            Shape oldClip = g2.getClip();
            RoundRectangle2D cardShape = new RoundRectangle2D.Double(cardX, y, cardWidth, cardHeight, 12, 12);

            g2.setClip(cardShape);

            if (texture != null) {
                g2.drawImage(texture, cardX, y, cardWidth, cardHeight, null);
            } else {
                g2.setColor(getResourceColor(r));
                g2.fill(cardShape);
            }

            g2.setClip(oldClip);
            g2.setColor(Color.BLACK);
            g2.draw(cardShape);
            g.setFont(new Font("Arial", Font.BOLD, 40));

            String text = String.valueOf(player.getResource(r));

            FontMetrics fm = g.getFontMetrics();

            int tx = cardX + (cardWidth - fm.stringWidth(text)) / 2;
            int ty = y + (cardHeight + fm.getAscent()) / 2 - 3;

            g.drawString(text, tx, ty);
        }
    }

    // Dice
    public void drawDice(Graphics g) {

        int size = 60;
        int spacing = 26;
        int bottomOffset = 150;
        int font = 50;
        int offsetText = 50;
        int dotSize = size / 5;

        int totalWidth = size * 2 + spacing;
        int startX = (getWidth() - totalWidth) / 2;
        int y = getHeight() - bottomOffset;

        drawSingleDice(g, startX, y, size, dice.dice1, dotSize);
        drawSingleDice(g, startX + size + spacing, y, size, dice.dice2, dotSize);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, font));

        String text = "Summe: " + dice.getDiceSum();

        FontMetrics fm = g.getFontMetrics();

        int tx = (getWidth() - fm.stringWidth(text)) / 2;
        int ty = y + size + offsetText;

        g.drawString(text, tx, ty);
    }

    public void drawSingleDice(Graphics g, int x, int y, int size, int value, int dotSize) {

        int left = size / 4;
        int middle = size / 2;
        int right = size * 3 / 4;

        int[][] points = {

                {left, left},
                {middle, left},
                {right, left},

                {left, middle},
                {middle, middle},
                {right, middle},

                {left, right},
                {middle, right},
                {right, right}
            };

        int[][] diceDots = {

                {},
                {4},
                {0, 8},
                {0, 4, 8},
                {0, 2, 6, 8},
                {0, 2, 4, 6, 8},
                {0, 2, 3, 5, 6, 8}
            };

        g.setColor(Color.WHITE);
        g.fillRoundRect(x, y, size, size, size / 5, size / 5);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, size, size, size / 5, size / 5);

        for (int index : diceDots[value]) {

            int px = x + points[index][0];
            int py = y + points[index][1];

            g.fillOval(px - dotSize / 2, py - dotSize / 2, dotSize, dotSize);
        }
    }

    // Next Button
    public void drawNextButton(Graphics g){

        Graphics2D g2 = (Graphics2D) g;

        int width = 150;
        int height = 100;
        int x = (getWidth() - width) / 2;
        int y = 50;
        int pressOffset = nextButtonPressed ? 4 : 0;

        nextButton = new Rectangle(x, y, width, height);

        if (nextButtonReady && !nextButtonPressed) {

            Composite old = g2.getComposite();

            for (int i = 0; i < 5; i++) {

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
                g2.setColor(playerColors[currentPlayer]);
                g2.fillRoundRect(x - 4 - i * 4, y - 4 - i * 4, width + (8 + i * 8), height + (8 + i * 8), 30, 30);
            }

            g2.setComposite(old);
        }

        g.setColor(playerColors[currentPlayer]);
        g.fillRoundRect(x, y + pressOffset, width, height, 25, 25);

        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y + pressOffset, width, height, 25, 25);

        g.setFont(new Font("Arial", Font.BOLD, 30));
        String text = "Weiter";
        FontMetrics fm = g.getFontMetrics();

        int tx = x + (width - fm.stringWidth(text)) / 2;
        int ty = y + pressOffset + (height + fm.getAscent()) / 2 - 5;

        g.drawString(text, tx, ty);
    }

    // Helper
    public Color getResourceColor(Variables.Resource r) {
        return switch (r) {
            case SHEEP -> new Color(179, 238, 58);
            case WOOD -> new Color(0, 100, 0);
            case WHEAT -> new Color(255, 215, 0);
            case BRICK -> new Color(238, 118, 33);
            case STONE -> new Color(140, 140, 140);
            case DESERT -> new Color(236, 219, 160);
            case DEFAULT -> Color.GRAY;
        };
    }

    public Draw() {
        try {
            woodTexture = ImageIO.read(new File("img/wood.jpg"));
            sheepTexture = ImageIO.read(new File("img/sheep.jpg"));
            wheatTexture = ImageIO.read(new File("img/wheat.jpg"));
            brickTexture = ImageIO.read(new File("img/brick.jpg"));
            stoneTexture = ImageIO.read(new File("img/stone.jpg"));
            desertTexture = ImageIO.read(new File("img/desert.jpg"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BufferedImage getResourceTexture(Variables.Resource r) {
        return switch (r) {
            case SHEEP -> sheepTexture;
            case WOOD -> woodTexture;
            case WHEAT -> wheatTexture;
            case BRICK -> brickTexture;
            case STONE -> stoneTexture;
            case DESERT -> desertTexture;
            case DEFAULT -> null;
        };
    }

    public Color getFarbe(int owner) {
        if (owner < 0 || owner >= playerColors.length) return Color.GRAY;
        return playerColors[owner];
    }

    public void setGame(Game game){
        this.game = game;
    }
}