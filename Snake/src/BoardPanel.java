import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BoardPanel extends JPanel {

    private Board board;
    private List<Player> players = new ArrayList<>();

    private final int TILE_SIZE = 70;
    private final int GAP = 15;

    private final Color SKY_TOP = new Color(0, 68, 119);
    private final Color SKY_BOTTOM = new Color(236, 250, 255);
    private final Color GROUND_GREEN = new Color(170, 216, 152);
    private final Color PATH_COLOR = new Color(180, 120, 60, 120);
    private final Color NODE_LIGHT = new Color(210, 180, 140);
    private final Color NODE_MID = new Color(188, 143, 107);
    private final Color BAMBOO_GREEN = new Color(107, 142, 35);
    private final Color BAMBOO_DARK = new Color(85, 107, 47);
    private final Color BAMBOO_LIGHT = new Color(154, 205, 50);
    private final Color MUSHROOM_RED = new Color(220, 60, 60);
    private final Color MUSHROOM_ORANGE = new Color(255, 140, 60);
    private final Color MUSHROOM_PINK = new Color(255, 120, 150);
    private final Color MUSHROOM_STEM = new Color(255, 248, 220);
    private final Color PINE_DARK = new Color(34, 80, 34);
    private final Color PINE_MID = new Color(50, 120, 50);
    private final Color PINE_LIGHT = new Color(70, 160, 70);
    private final Color TRUNK_BROWN = new Color(101, 67, 33);
    private final Color TRUNK_DARK = new Color(80, 50, 25);

    public BoardPanel(Board board) {
        this.board = board;
        setBackground(SKY_BOTTOM);
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
        repaint();
    }

    public void setBoard(Board board) {
        this.board = board;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        drawMarioBackground(g2d);
        drawStars(g2d);
        drawPineTrees(g2d);
        drawMushrooms(g2d);

        int boardSize = (8 * TILE_SIZE) + (7 * GAP);
        int startX = (getWidth() - boardSize) / 2;
        int startY = (getHeight() - boardSize) / 2;

        List<Point> centers = calculateTileCenters(startX, startY);

        drawPath(g2d, centers);
        drawWoodenNodes(g2d, centers);
        drawBonusPoints(g2d, centers);
        drawBambooLadders(g2d, centers);
        drawMarioPlayers(g2d, centers);
        drawLabels(g2d, centers);
    }

    private void drawMarioBackground(Graphics2D g2d) {
        GradientPaint sky = new GradientPaint(
                0, 0, SKY_TOP,
                0, getHeight(), SKY_BOTTOM
        );
        g2d.setPaint(sky);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        drawPerfectCloud(g2d, 120, 90, 1.0f);
        drawPerfectCloud(g2d, 360, 60, 1.2f);
        drawPerfectCloud(g2d, 620, 110, 0.9f);
        drawPerfectCloud(g2d, 240, 140, 0.85f);

        g2d.setColor(GROUND_GREEN);
        g2d.fillRect(0, getHeight() - 90, getWidth(), 90);

        GradientPaint groundGradient = new GradientPaint(
                0, getHeight() - 90, new Color(170, 216, 152, 255),
                0, getHeight() - 60, new Color(140, 186, 122, 255)
        );
        g2d.setPaint(groundGradient);
        g2d.fillRect(0, getHeight() - 90, getWidth(), 30);
    }

    private void drawStars(Graphics2D g2d) {
        Random rand = new Random(12345);

        for (int i = 0; i < 50; i++) {
            int x = rand.nextInt(getWidth());
            int y = rand.nextInt((int)(getHeight() * 0.5));
            int size = 2 + rand.nextInt(4);
            float alpha = 0.5f + rand.nextFloat() * 0.5f;

            g2d.setColor(new Color(255, 255, 200, (int)(alpha * 80)));
            g2d.fillOval(x - size, y - size, size * 2, size * 2);

            g2d.setColor(new Color(255, 255, 255, (int)(alpha * 255)));
            g2d.fillOval(x - size/2, y - size/2, size, size);

            if (size > 2) {
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.setColor(new Color(255, 255, 255, (int)(alpha * 200)));
                int sparkleLen = size + 2;
                g2d.drawLine(x, y - sparkleLen, x, y + sparkleLen);
                g2d.drawLine(x - sparkleLen, y, x + sparkleLen, y);
            }
        }
    }

    private void drawPerfectCloud(Graphics2D g2d, int x, int y, float scale) {
        int baseSize = (int)(50 * scale);

        g2d.setColor(new Color(150, 180, 200, 80));
        drawCloudShape(g2d, x + 6, y + 6, baseSize);

        g2d.setColor(new Color(190, 210, 230, 120));
        drawCloudShape(g2d, x + 4, y + 4, baseSize);

        g2d.setColor(new Color(220, 235, 245, 160));
        drawCloudShape(g2d, x + 2, y + 2, baseSize);

        g2d.setColor(Color.WHITE);
        drawCloudShape(g2d, x, y, baseSize);

        g2d.setColor(new Color(255, 255, 255, 220));
        g2d.fillOval(x + baseSize/2, y - (int)(baseSize * 0.15),
                (int)(baseSize * 0.8), (int)(baseSize * 0.6));
    }

    private void drawCloudShape(Graphics2D g2d, int x, int y, int size) {
        g2d.fillOval(x, y, (int)(size * 1.2), (int)(size * 0.7));
        g2d.fillOval(x + (int)(size * 0.8), y, (int)(size * 1.2), (int)(size * 0.7));
        g2d.fillOval(x + (int)(size * 0.2), y - (int)(size * 0.3),
                (int)(size * 1.2), (int)(size * 1.0));
        g2d.fillOval(x + (int)(size * 0.7), y - (int)(size * 0.25),
                (int)(size * 1.1), (int)(size * 0.9));
        g2d.fillOval(x + (int)(size * 0.45), y - (int)(size * 0.4),
                (int)(size * 0.9), (int)(size * 0.9));
    }

    private void drawPineTrees(Graphics2D g2d) {
        int groundY = getHeight() - 90;

        drawPineTree(g2d, 80, groundY, 0.7f);
        drawPineTree(g2d, 140, groundY, 0.6f);
        drawPineTree(g2d, getWidth() - 100, groundY, 0.75f);
        drawPineTree(g2d, getWidth() - 160, groundY, 0.65f);

        drawPineTree(g2d, 200, groundY, 0.9f);
        drawPineTree(g2d, getWidth() - 220, groundY, 0.85f);

        drawPineTree(g2d, 50, groundY, 1.1f);
        drawPineTree(g2d, getWidth() - 70, groundY, 1.0f);
    }

    private void drawPineTree(Graphics2D g2d, int x, int y, float scale) {
        int trunkW = (int)(20 * scale);
        int trunkH = (int)(60 * scale);
        int treeH = (int)(100 * scale);

        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(x - (int)(30 * scale), y, (int)(60 * scale), (int)(12 * scale));

        g2d.setColor(new Color(60, 40, 20, 100));
        g2d.fillRect(x - trunkW/2 + 2, y - trunkH + 2, trunkW, trunkH);

        GradientPaint trunkGradient = new GradientPaint(
                x - trunkW/2, y - trunkH, new Color(120, 80, 40),
                x + trunkW/2, y - trunkH, TRUNK_DARK
        );
        g2d.setPaint(trunkGradient);
        g2d.fillRect(x - trunkW/2, y - trunkH, trunkW, trunkH);

        g2d.setColor(TRUNK_DARK);
        g2d.setStroke(new BasicStroke(1.5f));
        for (int i = 1; i < 4; i++) {
            int lineY = y - trunkH + (trunkH * i / 4);
            g2d.drawLine(x - trunkW/2 + 2, lineY, x + trunkW/2 - 2, lineY);
        }

        g2d.setColor(new Color(140, 90, 50, 150));
        g2d.fillRect(x - trunkW/2 + 3, y - trunkH + 8, (int)(trunkW * 0.25), trunkH - 16);

        int numLayers = 4;
        int baseY = y - trunkH;

        for (int layer = 0; layer < numLayers; layer++) {
            int layerY = baseY - (layer * treeH / (numLayers + 1));
            int layerWidth = (int)((90 - layer * 18) * scale);
            int layerHeight = (int)(35 * scale);

            int[] xShadow = {x + 3, x - layerWidth/2 + 3, x + layerWidth/2 + 3};
            int[] yShadow = {layerY - layerHeight + 3, layerY + 3, layerY + 3};
            g2d.setColor(new Color(20, 50, 20, 100));
            g2d.fillPolygon(xShadow, yShadow, 3);

            drawPineTriangle(g2d, x, layerY - layerHeight, layerWidth, layerHeight, PINE_DARK);

            int midWidth = (int)(layerWidth * 0.85);
            int midHeight = (int)(layerHeight * 0.85);
            drawPineTriangle(g2d, x, layerY - midHeight, midWidth, midHeight, PINE_MID);

            int lightWidth = (int)(layerWidth * 0.65);
            int lightHeight = (int)(layerHeight * 0.65);
            drawPineTriangle(g2d, x, layerY - lightHeight, lightWidth, lightHeight, PINE_LIGHT);
        }

        int topY = baseY - (int)(treeH * 0.9);
        int topWidth = (int)(30 * scale);
        int topHeight = (int)(40 * scale);

        int[] xTopShadow = {x + 2, x - topWidth/2 + 2, x + topWidth/2 + 2};
        int[] yTopShadow = {topY - topHeight + 2, topY + 2, topY + 2};
        g2d.setColor(new Color(20, 50, 20, 100));
        g2d.fillPolygon(xTopShadow, yTopShadow, 3);

        drawPineTriangle(g2d, x, topY - topHeight, topWidth, topHeight, PINE_DARK);
        drawPineTriangle(g2d, x, topY - (int)(topHeight * 0.85),
                (int)(topWidth * 0.75), (int)(topHeight * 0.85), PINE_MID);
        drawPineTriangle(g2d, x, topY - (int)(topHeight * 0.65),
                (int)(topWidth * 0.5), (int)(topHeight * 0.65), PINE_LIGHT);

        if (scale >= 0.9f) {
            g2d.setColor(new Color(255, 215, 0));
            drawStarShape(g2d, x, topY - topHeight - (int)(5 * scale),
                    (int)(8 * scale), (int)(4 * scale));
        }
    }

    private void drawPineTriangle(Graphics2D g2d, int x, int topY, int width, int height, Color color) {
        int[] xPoints = {x, x - width/2, x + width/2};
        int[] yPoints = {topY, topY + height, topY + height};
        g2d.setColor(color);
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    private void drawStarShape(Graphics2D g2d, int x, int y, int outerR, int innerR) {
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];

        for (int i = 0; i < 10; i++) {
            double angle = Math.PI / 2 - (i * Math.PI / 5);
            int radius = (i % 2 == 0) ? outerR : innerR;
            xPoints[i] = (int)(x + radius * Math.cos(angle));
            yPoints[i] = (int)(y - radius * Math.sin(angle));
        }

        g2d.fillPolygon(xPoints, yPoints, 10);
    }

    private void drawMushrooms(Graphics2D g2d) {
        int groundY = getHeight() - 90;

        drawDetailedMushroom(g2d, 120, groundY + 15, 1.0f, MUSHROOM_RED);
        drawDetailedMushroom(g2d, 170, groundY + 25, 0.7f, MUSHROOM_ORANGE);
        drawDetailedMushroom(g2d, 145, groundY + 35, 0.6f, MUSHROOM_PINK);

        drawDetailedMushroom(g2d, getWidth() - 130, groundY + 20, 1.1f, MUSHROOM_RED);
        drawDetailedMushroom(g2d, getWidth() - 180, groundY + 30, 0.8f, MUSHROOM_ORANGE);
        drawDetailedMushroom(g2d, getWidth() - 155, groundY + 40, 0.5f, MUSHROOM_PINK);

        drawDetailedMushroom(g2d, 280, groundY + 22, 0.9f, MUSHROOM_RED);
        drawDetailedMushroom(g2d, 330, groundY + 28, 0.65f, MUSHROOM_ORANGE);
        drawDetailedMushroom(g2d, getWidth() - 300, groundY + 25, 0.85f, MUSHROOM_PINK);
        drawDetailedMushroom(g2d, getWidth() - 350, groundY + 35, 0.7f, MUSHROOM_RED);
    }

    private void drawDetailedMushroom(Graphics2D g2d, int x, int y, float scale, Color capColor) {
        int capW = (int)(35 * scale);
        int capH = (int)(25 * scale);
        int stemW = (int)(18 * scale);
        int stemH = (int)(30 * scale);

        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillOval(x - (int)(20 * scale), y, (int)(40 * scale), (int)(8 * scale));

        g2d.setColor(new Color(200, 180, 140, 100));
        g2d.fillRoundRect(x - stemW/2 + 2, y - stemH + 2, stemW, stemH, 6, 6);

        GradientPaint stemGradient = new GradientPaint(
                x - stemW/2, y - stemH, MUSHROOM_STEM,
                x + stemW/2, y, new Color(240, 230, 200)
        );
        g2d.setPaint(stemGradient);
        g2d.fillRoundRect(x - stemW/2, y - stemH, stemW, stemH, 6, 6);

        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.fillRoundRect(x - stemW/2 + 3, y - stemH + 5, (int)(stemW * 0.35), stemH - 10, 4, 4);

        g2d.setColor(new Color(220, 200, 170));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawRoundRect(x - stemW/2, y - stemH, stemW, stemH, 6, 6);

        g2d.setColor(new Color(capColor.getRed() - 60, capColor.getGreen() - 40, capColor.getBlue() - 40, 120));
        g2d.fillArc(x - capW/2 + 3, y - stemH - capH + 3, capW, capH * 2, 0, 180);

        GradientPaint capGradient = new GradientPaint(
                x, y - stemH - capH, capColor.brighter().brighter(),
                x, y - stemH, capColor
        );
        g2d.setPaint(capGradient);
        g2d.fillArc(x - capW/2, y - stemH - capH, capW, capH * 2, 0, 180);

        g2d.setColor(new Color(255, 255, 255, 120));
        g2d.fillOval(x - capW/4, y - stemH - (int)(capH * 0.8), (int)(capW * 0.4), (int)(capH * 0.6));

        int spotSize = (int)(8 * scale);
        g2d.setColor(new Color(200, 200, 200, 150));
        g2d.fillOval(x - capW/4 + 1, y - stemH - capH/2 + 1, spotSize, spotSize);
        g2d.fillOval(x + capW/6 + 1, y - stemH - (int)(capH * 0.7) + 1, (int)(spotSize * 0.7), (int)(spotSize * 0.7));

        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - capW/4, y - stemH - capH/2, spotSize, spotSize);
        g2d.fillOval(x + capW/6, y - stemH - (int)(capH * 0.7), (int)(spotSize * 0.7), (int)(spotSize * 0.7));

        g2d.setColor(new Color(capColor.getRed() - 50, capColor.getGreen() - 30, capColor.getBlue() - 30));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawArc(x - capW/2, y - stemH - capH, capW, capH * 2, 0, 180);

        if (scale >= 0.9f) {
            int eyeSize = (int)(4 * scale);
            int eyeY = y - stemH - capH/3;

            g2d.setColor(new Color(50, 50, 50));
            g2d.fillOval(x - 8, eyeY, eyeSize, eyeSize);
            g2d.fillOval(x + 3, eyeY, eyeSize, eyeSize);

            g2d.setColor(Color.WHITE);
            g2d.fillOval(x - 7, eyeY + 1, 2, 2);
            g2d.fillOval(x + 4, eyeY + 1, 2, 2);
        }
    }

    private List<Point> calculateTileCenters(int x0, int y0) {
        List<Point> pts = new ArrayList<>();
        int step = TILE_SIZE + GAP;

        for (int i = 0; i < 64; i++) {
            int row = i / 8;
            int col = i % 8;
            int ec = (row % 2 == 0) ? col : 7 - col;
            int x = x0 + ec * step + TILE_SIZE / 2;
            int y = y0 + (7 - row) * step + TILE_SIZE / 2;
            pts.add(new Point(x, y));
        }
        return pts;
    }

    private void drawPath(Graphics2D g2d, List<Point> pts) {
        g2d.setColor(PATH_COLOR);
        g2d.setStroke(new BasicStroke(2f));
        for (int i = 0; i < pts.size() - 1; i++) {
            g2d.drawLine(pts.get(i).x, pts.get(i).y,
                    pts.get(i + 1).x, pts.get(i + 1).y);
        }
    }

    private void drawWoodenNodes(Graphics2D g2d, List<Point> centers) {
        for (int i = 0; i < centers.size(); i++) {
            Point c = centers.get(i);
            drawWoodenNode(g2d, c.x, c.y, i + 1);
        }
    }

    private void drawWoodenNode(Graphics2D g2d, int cx, int cy, int number) {
        int radius = 30;

        g2d.setColor(new Color(150, 120, 90, 80));
        g2d.fillOval(cx - radius + 3, cy - radius + 3, radius * 2, radius * 2);

        GradientPaint nodeGradient = new GradientPaint(
                cx - radius, cy - radius, NODE_LIGHT,
                cx + radius, cy + radius, NODE_MID
        );
        g2d.setPaint(nodeGradient);
        g2d.fillOval(cx - radius, cy - radius, radius * 2, radius * 2);

        Random rand = new Random(number * 137);
        g2d.setClip(new Ellipse2D.Float(cx - radius, cy - radius, radius * 2, radius * 2));

        for (int i = 0; i < 8; i++) {
            float offset = rand.nextFloat() * 10 - 5;
            int grainRadius = radius - 5 - i * 3;

            g2d.setColor(new Color(140, 100, 70, 30 + rand.nextInt(25)));
            g2d.setStroke(new BasicStroke(1.2f + rand.nextFloat() * 0.5f));
            g2d.drawOval(cx - grainRadius + (int)offset,
                    cy - grainRadius,
                    grainRadius * 2,
                    grainRadius * 2);
        }

        g2d.setStroke(new BasicStroke(0.8f));
        for (int i = 0; i < 15; i++) {
            int xOffset = -radius + (i * 4) + rand.nextInt(3);
            int yStart = cy - radius + rand.nextInt(10);
            int yEnd = cy + radius - rand.nextInt(10);

            int alpha = 20 + rand.nextInt(30);
            g2d.setColor(new Color(150, 110, 80, alpha));

            Path2D.Float path = new Path2D.Float();
            path.moveTo(cx + xOffset, yStart);

            int segments = 5;
            for (int j = 1; j <= segments; j++) {
                float t = (float)j / segments;
                int y = (int)(yStart + (yEnd - yStart) * t);
                int xWave = xOffset + (int)(Math.sin(t * Math.PI) * (1 + rand.nextFloat()));
                path.lineTo(cx + xWave, y);
            }
            g2d.draw(path);
        }

        for (int i = 0; i < 2; i++) {
            int knotX = cx + (rand.nextInt(radius) - radius/2);
            int knotY = cy + (rand.nextInt(radius) - radius/2);
            int knotSize = 3 + rand.nextInt(4);

            g2d.setColor(new Color(120, 90, 60, 80));
            g2d.fillOval(knotX - knotSize/2, knotY - knotSize/2, knotSize, knotSize);

            g2d.setColor(new Color(150, 110, 80, 100));
            g2d.setStroke(new BasicStroke(0.8f));
            g2d.drawOval(knotX - knotSize, knotY - knotSize, knotSize * 2, knotSize * 2);
        }

        g2d.setClip(null);

        g2d.setColor(new Color(230, 210, 180, 140));
        g2d.fillOval(cx - radius + 8, cy - radius + 8, radius, radius);

        // Outline coklat medium
        g2d.setColor(new Color(140, 100, 70));
        g2d.setStroke(new BasicStroke(3f));
        g2d.drawOval(cx - radius, cy - radius, radius * 2, radius * 2);

        // Inner border
        g2d.setColor(new Color(180, 140, 100, 150));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(cx - radius + 2, cy - radius + 2, radius * 2 - 4, radius * 2 - 4);

        // Number
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        String num = String.valueOf(number);
        FontMetrics fm = g2d.getFontMetrics();

        int tx = cx - fm.stringWidth(num) / 2;
        int ty = cy + fm.getAscent() / 2 - 2;

        // Number shadow
        g2d.setColor(new Color(100, 70, 40, 200));
        g2d.drawString(num, tx + 2, ty + 2);

        // Number outline
        g2d.setColor(new Color(100, 70, 40));
        g2d.drawString(num, tx + 1, ty + 1);
        g2d.drawString(num, tx - 1, ty - 1);
        g2d.drawString(num, tx + 1, ty - 1);
        g2d.drawString(num, tx - 1, ty + 1);

        // Main number (putih/cream)
        g2d.setColor(new Color(255, 250, 240));
        g2d.drawString(num, tx, ty);
    }

    private void drawBambooLadders(Graphics2D g2d, List<Point> centers) {
        Tile[][] tiles = board.getTiles();
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Tile t = tiles[r][c];
                if (t.isLadder()) {
                    Point p1 = centers.get(t.getNumber() - 1);
                    Point p2 = centers.get(t.getDestination() - 1);

                    drawBamboo(g2d, p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
    }

    private void drawBamboo(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        g2d.setStroke(new BasicStroke(10f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.drawLine(x1 + 2, y1 + 2, x2 + 2, y2 + 2);

        g2d.setStroke(new BasicStroke(9f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(BAMBOO_GREEN);
        g2d.drawLine(x1, y1, x2, y2);

        g2d.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(BAMBOO_LIGHT);
        g2d.drawLine(x1 - 2, y1, x2 - 2, y2);

        double length = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        int segments = (int)(length / 40);

        g2d.setStroke(new BasicStroke(3f));
        g2d.setColor(BAMBOO_DARK);

        for (int i = 1; i < segments; i++) {
            float ratio = (float) i / segments;
            int segX = (int)(x1 + (x2 - x1) * ratio);
            int segY = (int)(y1 + (y2 - y1) * ratio);

            g2d.drawLine(segX - 6, segY, segX + 6, segY);
            g2d.setStroke(new BasicStroke(1.5f));
            g2d.drawLine(segX - 6, segY - 2, segX + 6, segY - 2);
            g2d.drawLine(segX - 6, segY + 2, segX + 6, segY + 2);
            g2d.setStroke(new BasicStroke(3f));
        }

        Random rand = new Random((x1 + y1 + x2 + y2) * 7);
        int leafCount = 4 + rand.nextInt(3);

        for (int i = 0; i < leafCount; i++) {
            float ratio = 0.2f + (float)i / leafCount * 0.6f;
            int leafX = (int)(x1 + (x2 - x1) * ratio);
            int leafY = (int)(y1 + (y2 - y1) * ratio);

            drawBambooLeaf(g2d, leafX, leafY, rand.nextBoolean());
        }
    }

    private void drawBambooLeaf(Graphics2D g2d, int x, int y, boolean faceLeft) {
        int direction = faceLeft ? -1 : 1;
        g2d.setColor(new Color(124, 179, 66, 200));
        g2d.fillOval(x + direction * 3, y - 8, 15, 5);
        g2d.setColor(BAMBOO_DARK);
        g2d.setStroke(new BasicStroke(1f));
        g2d.drawOval(x + direction * 3, y - 8, 15, 5);
        g2d.drawLine(x + direction * 3, y - 5, x + direction * 18, y - 5);
    }

    private void drawMarioPlayers(Graphics2D g2d, List<Point> centers) {
        if (players == null) return;
        int[] stack = new int[65];
        for (Player p : players) {
            int pos = Math.max(1, Math.min(64, p.getPosition()));
            Point c = centers.get(pos - 1);
            int shift = stack[pos] * 16;
            stack[pos]++;
            drawMarioToken(g2d, c.x + shift, c.y, p.getColor());
        }
    }

    private void drawMarioToken(Graphics2D g2d, int x, int y, Color color) {
        g2d.setColor(new Color(255, 224, 189));
        g2d.fillOval(x - 10, y - 10, 20, 20);
        g2d.setColor(color);
        g2d.fillRoundRect(x - 12, y - 18, 24, 10, 6, 6);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 10));
        g2d.drawString("M", x - 4, y - 10);
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x - 4, y - 2, 3, 3);
        g2d.fillOval(x + 1, y - 2, 3, 3);
    }

    private void drawLabels(Graphics2D g2d, List<Point> centers) {
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        // START LABEL
        String startText = "START";
        int startX = centers.get(0).x - 35;
        int startY = centers.get(0).y + 50;

        // Shadow hitam
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.drawString(startText, startX + 2, startY + 2);

        // Gradient kuning
        GradientPaint startGradient = new GradientPaint(
                startX, startY - 15, new Color(255, 223, 0),  // Gold
                startX, startY, new Color(218, 165, 32)          // Goldenrod
        );
        g2d.setPaint(startGradient);
        g2d.drawString(startText, startX, startY);

        // FINISH LABEL
        String finishText = "FINISH";
        int finishX = centers.get(63).x - 40;
        int finishY = centers.get(63).y - 40;

        // Shadow hitam
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.drawString(finishText, finishX + 2, finishY + 2);

        // Gradient emas
        GradientPaint finishGradient = new GradientPaint(
                finishX, finishY - 15, new Color(255, 223, 0),  // Gold
                finishX, finishY, new Color(218, 165, 32)       // Goldenrod
        );
        g2d.setPaint(finishGradient);
        g2d.drawString(finishText, finishX, finishY);
    }

    private void drawBonusPoints(Graphics2D g2d, List<Point> centers) {
        Map<Integer, Integer> bonuses = board.getAllBonusPoints();

        for (Map.Entry<Integer, Integer> entry : bonuses.entrySet()) {
            int position = entry.getKey();
            int points = entry.getValue();
            if (position < 1 || position > 64) continue;
            Point center = centers.get(position - 1);

            String pointText;
            if (points > 0) {
                pointText = "+" + points;
                g2d.setColor(new Color(255, 255, 150));
            } else {
                pointText = String.valueOf(points);
                g2d.setColor(new Color(255, 100, 100));
            }

            g2d.setFont(new Font("Arial", Font.BOLD, 11));

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(pointText);
            int tx = center.x - textWidth / 2;
            int ty = center.y - 18;

            if (points > 0) g2d.setColor(new Color(255, 255, 255));
            else g2d.setColor(new Color(52, 52, 52));

            g2d.drawString(pointText, tx, ty);
        }
    }
}
