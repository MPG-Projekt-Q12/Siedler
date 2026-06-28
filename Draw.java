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

    ArrayList<Rectangle> tradeTargets = new ArrayList<>();

    public ArrayList<Rectangle> plusButtonsTop = new ArrayList<>();
    public ArrayList<Rectangle> minusButtonsTop = new ArrayList<>();

    public ArrayList<Rectangle> plusButtonsBottom = new ArrayList<>();
    public ArrayList<Rectangle> minusButtonsBottom = new ArrayList<>();

    public Rectangle nextButton;
    public Rectangle tradeOkButton;
    public Rectangle tradeCancelButton;

    public int currentPlayer;
    public boolean nextButtonPressed = false;
    public boolean nextButtonReady = false;
    public boolean tradeOkPressed = false;

    private double scale = 1.0;
    private int offsetX = 0;
    private int offsetY = 0;

    private int baseX = 1920;
    private int baseY = 1080;

    private Game game;

    // Paint
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);

        updateTransform();
        Graphics2D g2 = (Graphics2D) g;
        AffineTransform old = g2.getTransform();
        g2.translate(offsetX, offsetY);
        g2.scale(scale, scale);

        drawConsole(g);
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
        drawTradeMenu(g);

        g2.setTransform(old);
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

        int startX = 50;
        int startY = 50;
        int width = 500;
        int height = 250;
        int distanceToEdges = 30;
        int rounding = 25;
        int x;
        int y;

        g.setFont(new Font("Arial", Font.BOLD, 28));
        FontMetrics fm = g.getFontMetrics();

        int textWidth = fm.stringWidth(player.getPlayerName());
        int textHeight = fm.getHeight();

        String pointsText = "Punkte: " + player.getWinningPoints();

        if (player.getPlayerNumber() == 1){
            x = startX;
            y = startY;
        }
        else if (player.getPlayerNumber() == 2){
            x = baseX - startX - width;
            y = startY;
        }
        else if (player.getPlayerNumber() == 3){
            x = startX;
            y = baseY - startY - height;
        }
        else {
            x = baseX - startX - width;
            y = baseY - startY - height;
        }

        g.setColor(playerColors[player.getPlayerNumber()]);
        g.fillRoundRect(x, y, width, height, rounding, rounding);
        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, width, height, rounding, rounding);
        g.setColor(Color.BLACK);
        g.fillRoundRect(x + distanceToEdges / 2, y + height - textHeight - distanceToEdges * 3 / 2, width - distanceToEdges, textHeight + distanceToEdges, rounding - 5, rounding - 5);
        g.setColor(Color.WHITE);
        g.drawString(pointsText, x + distanceToEdges, y + height - textHeight - distanceToEdges + fm.getAscent());

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
        g.drawString(player.getPlayerName(), x + width - distanceToEdges - textWidth, y + height - textHeight - distanceToEdges + fm.getAscent());

        drawPlayerCards(g, player, x, y, distanceToEdges, width, height);
    }

    public void drawPlayerCards(Graphics g, Player player, int startX, int startY, int distanceToEdges, int playerWidth, int playerHeight) {

        int cardWidth = 72;
        int cardHeight = 100;
        int spacing = 20;

        Variables.Resource[] res = Variables.Resource.values();

        for (int i = 0; i < res.length - 2; i++) {

            Variables.Resource r = res[i];

            int cardX = startX + distanceToEdges + i * (cardWidth + spacing);
            int cardY = startY + distanceToEdges;

            Graphics2D g2 = (Graphics2D) g;

            BufferedImage texture = getResourceTexture(r);
            Shape oldClip = g2.getClip();
            RoundRectangle2D cardShape = new RoundRectangle2D.Double(cardX, cardY, cardWidth, cardHeight, 12, 12);

            g2.setClip(cardShape);

            if (texture != null) {
                g2.drawImage(texture, cardX, cardY, cardWidth, cardHeight, null);
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
            int ty = cardY + (cardHeight + fm.getAscent()) / 2 - 3;

            g.drawString(text, tx, ty);
        }
    }

    // Dice
    public void drawDice(Graphics g) {

        int size = 75;
        int spacing = 26;
        int bottomOffset = 175;
        int font = 50;
        int offsetText = 50;
        int dotSize = size / 5;

        int totalWidth = size * 2 + spacing;
        int startX = (baseX - totalWidth) / 2;
        int y = baseY - bottomOffset;

        drawSingleDice(g, startX, y, size, dice.dice1, dotSize);
        drawSingleDice(g, startX + size + spacing, y, size, dice.dice2, dotSize);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, font));

        String text = "Summe: " + dice.getDiceSum();

        FontMetrics fm = g.getFontMetrics();

        int tx = (baseX - fm.stringWidth(text)) / 2;
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

        int width = 180;
        int height = 125;
        int x = (baseX - width) / 2;
        int y = 50;
        int pressOffset = nextButtonPressed ? 4 : 0;

        nextButton = new Rectangle(x, y, width, height);

        if (nextButtonReady && !nextButtonPressed && game.getTradeState() == Variables.TradeState.NONE) {

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

    // Trade
    public void drawTradeMenu(Graphics g) {

        int startX = 50;
        int startY = 350;
        int width = 500;
        int height = 380;
        int distanceToEdges = 30;
        int rounding = 5;

        g.setColor(new Color(180, 180, 180));
        g.fillRoundRect(startX, startY, width, height, rounding * 5, rounding * 5);

        g.setColor(Color.BLACK);
        g.drawRoundRect(startX, startY, width, height, rounding * 5, rounding * 5);

        drawTradeCards(g, startX, startY, width, height, distanceToEdges, rounding);
        drawTradeTargetButtons(g, startX, startY, width, height, distanceToEdges, rounding);
        drawTradeOkButton(g, startX, startY, width, height, distanceToEdges, rounding);
        drawTradeCancelButton(g, startX, startY, width, height, distanceToEdges, rounding);
    }

    public void drawTradeTargetButtons(Graphics g, int startX, int startY, int width, int height, int distanceToEdges, int rounding) {

        tradeTargets.clear();

        int buttonSize = 30;
        int spacing = 15;

        int j = 0;

        for (Player p : players) {

            Color c = playerColors[p.getPlayerNumber()];

            if (p.getPlayerNumber() == currentPlayer) {
                int buttonX = startX + distanceToEdges;
                int buttonY = startY + distanceToEdges;

                g.setColor(c);
                g.fillRoundRect(buttonX, buttonY, buttonSize, buttonSize, rounding, rounding);

                g.setColor(Color.BLACK);
                g.drawRoundRect(buttonX, buttonY, buttonSize, buttonSize, rounding, rounding);

                continue;
            }

            int buttonX = startX + distanceToEdges + (buttonSize + spacing) * j;
            int buttonY = startY + distanceToEdges + height / 2;

            Rectangle rect = new Rectangle(buttonX, buttonY, buttonSize, buttonSize);
            tradeTargets.add(rect);

            Graphics2D g2 = (Graphics2D) g;
            if (game.getSelectedTradeTarget() == p.getPlayerNumber()) {
                Composite old = g2.getComposite();

                for (int i = 0; i < 5; i++) {
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
                    g2.setColor(c);
                    g2.fillRoundRect(rect.x - 3 - i * 3, rect.y - 3 - i * 3, rect.width + 6 + i * 6, rect.height + 6 + i * 6, 10, 10);
                }

                g2.setComposite(old);
            }

            g.setColor(c);
            g.fillRoundRect(rect.x, rect.y, rect.width, rect.height, rounding, rounding);

            g.setColor(Color.BLACK);
            g.drawRoundRect(rect.x, rect.y, rect.width, rect.height, rounding, rounding);

            j++;
        }

        Rectangle bank = new Rectangle(startX + distanceToEdges + (buttonSize + spacing) * j, startY + distanceToEdges + height / 2, buttonSize, buttonSize);
        tradeTargets.add(bank);

        Graphics2D g2 = (Graphics2D) g;
        if (game.getSelectedTradeTarget() == 0) {
            Composite old = g2.getComposite();

            for (int i = 0; i < 5; i++) {
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(bank.x - 3 - i * 3, bank.y - 3 - i * 3, bank.width + 6 + i * 6, bank.height + 6 + i * 6, 10, 10);
            }

            g2.setComposite(old);
        }

        g.setColor(Color.WHITE);
        g.fillRoundRect(bank.x, bank.y, bank.width, bank.height, rounding, rounding);

        g.setColor(Color.BLACK);
        g.drawRoundRect(bank.x, bank.y, bank.width, bank.height, rounding, rounding);

        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("B", bank.x + 8, bank.y + 22);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 22));
        FontMetrics fm = g.getFontMetrics();

        g.drawString("gibt ab:", startX + distanceToEdges + buttonSize + spacing, startY + distanceToEdges + fm.getAscent());
        g.drawString("gibt ab:", startX + distanceToEdges + (buttonSize + spacing) * (j + 1), startY + distanceToEdges + fm.getAscent() + height / 2);
    }

    public void drawTradeCards(Graphics g, int startX, int startY, int width, int height, int distanceToEdges, int rounding) {

        int cardWidth = 55;
        int cardHeight = 70;
        int spacing = 10;
        int plusminusSize = 20;

        Variables.Resource[] res = Variables.Resource.values();

        plusButtonsTop.clear();
        minusButtonsTop.clear();
        plusButtonsBottom.clear();
        minusButtonsBottom.clear();

        for (int i = 0; i < 5; i++) {

            Variables.Resource r = res[i];

            int cardX = startX + distanceToEdges + i * (cardWidth + spacing);
            int cardY = startY + 75;
            int from = 0;
            int to = 0;

            if (game.getCurrentTrade() != null) {
                from = game.getCurrentTrade().getFromGives(r);
                to = game.getCurrentTrade().getToGives(r);
            }

            drawTradeCard(g, r, from, cardX, cardY);
            drawTradeCard(g, r, to, cardX, cardY + height / 2);

            Rectangle minus = new Rectangle(cardX, cardY + cardHeight + distanceToEdges / 2, plusminusSize, plusminusSize);
            Rectangle plus = new Rectangle(cardX + cardWidth - plusminusSize, cardY + cardHeight + distanceToEdges / 2, plusminusSize, plusminusSize);

            minusButtonsTop.add(minus);
            plusButtonsTop.add(plus);

            Rectangle minus2 = new Rectangle(cardX, cardY + cardHeight + distanceToEdges / 2 + height / 2, plusminusSize, plusminusSize);
            Rectangle plus2 = new Rectangle(cardX + cardWidth - plusminusSize, cardY + cardHeight + distanceToEdges / 2 + height / 2, plusminusSize, plusminusSize);

            minusButtonsBottom.add(minus2);
            plusButtonsBottom.add(plus2);
        }
    }

    private void drawTradeCard(Graphics g, Variables.Resource r, int amount, int x, int y) {

        int width = 55;
        int height = 70;

        Graphics2D g2 = (Graphics2D) g;

        BufferedImage texture = getResourceTexture(r);

        RoundRectangle2D shape = new RoundRectangle2D.Double(x, y, width,height, 10, 10);
        Shape old = g2.getClip();
        g2.setClip(shape);

        if (texture != null) {
            g2.drawImage(texture, x, y, width, height, null);
        }

        g2.setClip(old);

        g2.setColor(Color.BLACK);
        g2.draw(shape);

        g.setFont(new Font( "Arial", Font.BOLD, 30));
        String text = String.valueOf(amount);
        FontMetrics fm = g.getFontMetrics();
        int tx = x + (width - fm.stringWidth(text)) / 2;
        int ty = y + (height + fm.getAscent()) / 2 - 3;

        g.drawString(text, tx, ty);
        g.drawString("-", x + 5, y + height + 30);
        g.drawString("+", x + width - 20, y + height + 30);
    }

    public void drawTradeOkButton(Graphics g, int startX, int startY, int width, int height, int distanceToEdges, int rounding) {

        int press = tradeOkPressed ? 4 : 0;

        int buttonWidth = 100;
        int buttonHeight = 100;
        int x = startX + width - buttonWidth - distanceToEdges; 
        int y = startY + height / 2 - buttonHeight / 2;

        tradeOkButton = new Rectangle(x, y, buttonWidth, buttonHeight);

        Color buttonColor = Color.WHITE;

        boolean ready = false;

        Trade trade = game.getCurrentTrade();

        if (trade != null) {

            if (game.getSelectedTradeTarget() == 0) {
                ready = trade.canExecuteBankTrade();
            } else if (game.getSelectedTradeTarget() != -1) {
                ready = trade.canExecute();
            }
        }

        if (game.getSelectedTradeTarget() >= 1 && game.getSelectedTradeTarget() <= 4) {
            buttonColor = playerColors[game.getSelectedTradeTarget()];
        }

        Graphics2D g2 = (Graphics2D) g;

        if (ready) {

            Composite old = g2.getComposite();

            for (int i = 0; i < 5; i++) {

                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
                g2.setColor(buttonColor);
                g2.fillRoundRect(x - 4 - i * 4, y - 4 - i * 4, buttonWidth + (8 + i * 8), buttonHeight + (8 + i * 8), 30, 30);
            }

            g2.setComposite(old);
        }

        g.setColor(buttonColor);
        g.fillRoundRect(x, y + press, buttonWidth, buttonHeight, rounding, rounding);

        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y + press, buttonWidth, buttonHeight, rounding, rounding);

        g.setFont(new Font("Arial", Font.BOLD,28));
        FontMetrics fm = g.getFontMetrics();
        String text = "OK";
        int tx = x + (buttonWidth - fm.stringWidth(text)) / 2;
        int ty = y + press + (buttonHeight + fm.getAscent()) / 2 - 5;

        g.drawString(text, tx, ty);
    }

    public void drawTradeCancelButton(Graphics g, int startX, int startY, int width, int height, int distanceToEdges, int rounding) {

        int buttonWidth = 100;
        int buttonHeight = 40;

        int x = startX + width - buttonWidth - distanceToEdges;
        int y = startY + height / 2 + 60;

        tradeCancelButton = new Rectangle(x, y, buttonWidth, buttonHeight);

        g.setColor(new Color(220, 220, 220));
        g.fillRoundRect(x, y, buttonWidth, buttonHeight, rounding, rounding);

        g.setColor(Color.BLACK);
        g.drawRoundRect(x, y, buttonWidth, buttonHeight, rounding, rounding);

        g.setFont(new Font("Arial", Font.BOLD, 18));

        String text = "Abbrechen";
        FontMetrics fm = g.getFontMetrics();

        int tx = x + (buttonWidth - fm.stringWidth(text)) / 2;
        int ty = y + (buttonHeight + fm.getAscent()) / 2 - 3;

        g.drawString(text, tx, ty);
    }

    // Console
    public void drawConsole(Graphics g) {

        int startX = baseX - 500 - 50;
        int startY = 350;
        int width = 500;
        int height = 380;
        int distanceToEdges = 15;
        int rounding = 5;
        int textX = startX + distanceToEdges + 12;

        g.setColor(new Color(180, 180, 180));
        g.fillRoundRect(startX, startY, width, height, rounding * 5, rounding * 5);

        g.setColor(Color.BLACK);
        g.drawRoundRect(startX, startY, width, height, rounding * 5, rounding * 5);

        g.setFont(new Font("Arial", Font.BOLD, 26));
        FontMetrics fm = g.getFontMetrics();

        String title = "Konsole";

        int consoleY = startY + distanceToEdges * 2 + fm.getHeight();
        int consoleX = startX + distanceToEdges;
        int consoleWidth = width - distanceToEdges * 2;
        int consoleHeight = height - distanceToEdges * 3 - fm.getHeight();
        int tx = startX + (width - fm.stringWidth(title)) / 2;
        int ty = startY + distanceToEdges + fm.getAscent();

        g.drawString(title, tx, ty);

        g.setColor(new Color(248, 248, 248));
        g.fillRoundRect(consoleX, consoleY, consoleWidth, consoleHeight, 15, 15);

        g.setColor(Color.BLACK);
        g.drawRoundRect(consoleX, consoleY, consoleWidth, consoleHeight, 15, 15);

        int lineY = consoleY + 25;
        int maxY = consoleY + consoleHeight - 10;

        g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 18));

        for (String s : game.getConsole()) {

            if (lineY > maxY) {
                break;
            }

            g.drawString(s, textX, lineY);

            lineY += 22;
        }
        
        System.out.println("DRAW: " + game.getConsole().size());
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

    private void updateTransform() {
        scale = Math.min(getWidth() / 1920.0, getHeight() / 1080.0);

        offsetX = (int)((getWidth() - 1920 * scale) / 2);
        offsetY = (int)((getHeight() - 1080 * scale) / 2);
    }

    public double getScale() {
        return scale;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

}