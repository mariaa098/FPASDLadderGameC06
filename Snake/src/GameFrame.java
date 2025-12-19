import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

public class GameFrame extends JFrame {

    private BoardPanel boardPanel;
    private Board board;
    private JPanel sidePanel;
    private JPanel playerListPanel;
    private RoundedButton rollButton;
    private DicePanel dicePanel;
    private JLabel diceInfoLabel;
    private JLabel turnLabel;
    private JLabel currentPlayerLabel;
    private PlayerAvatarPanel avatarPanel;
    private List<Player> players;
    private int currentPlayerIndex = 0;
    private boolean isAnimating = false;
    private boolean canClimbCurrentTurn = false;
    private int finalDiceValue = 1;

    public GameFrame(List<String> playerNames) {
        setTitle("Snake & Ladders - Prime Rules Edition");
        setSize(1100, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initPlayers(playerNames);

        board = new Board();
        boardPanel = new BoardPanel(board);
        add(boardPanel, BorderLayout.CENTER);

        sidePanel = createSidePanel();
        add(sidePanel, BorderLayout.EAST);

        boardPanel.setPlayers(players);
        updateTurnUI();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initPlayers(List<String> names) {
        players = new ArrayList<>();
        Color[] colors = {
                new Color(255, 182, 193),  // Pink Soft (Light Pink)
                new Color(255, 105, 97),   // Merah Soft (Coral Red)
                new Color(177, 156, 217),  // Ungu Soft (Lavender)
                new Color(135, 169, 107),  // Hijau Sage (Sage Green)
                new Color(65, 105, 225)    // Royal Blue
        };

        for (int i = 0; i < names.size(); i++) {
            players.add(new Player(i + 1, names.get(i), colors[i % colors.length]));
        }
    }


    private JPanel createSidePanel() {
        JPanel panel = new MarioBackgroundPanel();
        panel.setPreferredSize(new Dimension(320, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(20, 30, 20, 30));

        // === ROLL BUTTON ===
        rollButton = new RoundedButton("ROLL DICE");
        rollButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        rollButton.setMinimumSize(new Dimension(220, 45));
        rollButton.setPreferredSize(new Dimension(220, 45));
        rollButton.setMaximumSize(new Dimension(220, 45));
        rollButton.addActionListener(e -> prepareRollDice());
        panel.add(rollButton);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // === DICE PANEL===
        dicePanel = new DicePanel();
        dicePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dicePanel.setMinimumSize(new Dimension(100, 100));
        dicePanel.setPreferredSize(new Dimension(100, 100));
        dicePanel.setMaximumSize(new Dimension(100, 100));
        panel.add(dicePanel);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // === INFO LABEL ===
        diceInfoLabel = new JLabel("Giliranmu!");
        diceInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        diceInfoLabel.setForeground(Color.WHITE);
        diceInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        diceInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(diceInfoLabel);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // === CURRENT TURN ===
        JPanel currentTurnPanel = createCurrentTurnPanel();
        currentTurnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(currentTurnPanel);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // === LEADERBOARD HEADER ===
        JPanel leaderboardHeaderPanel = new JPanel();
        leaderboardHeaderPanel.setOpaque(false);
        leaderboardHeaderPanel.setLayout(new BoxLayout(leaderboardHeaderPanel, BoxLayout.X_AXIS));
        leaderboardHeaderPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leaderboardHeaderPanel.setMaximumSize(new Dimension(260, 25));

        JLabel lb = new JLabel("LEADERBOARD");
        lb.setFont(new Font("Arial", Font.BOLD, 16));
        lb.setForeground(Color.WHITE);
        lb.setAlignmentX(Component.CENTER_ALIGNMENT);

        leaderboardHeaderPanel.add(Box.createHorizontalGlue());
        leaderboardHeaderPanel.add(lb);
        leaderboardHeaderPanel.add(Box.createHorizontalGlue());
        panel.add(leaderboardHeaderPanel);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        // === PLAYER LIST ===
        playerListPanel = new JPanel();
        playerListPanel.setLayout(new BoxLayout(playerListPanel, BoxLayout.Y_AXIS));
        playerListPanel.setOpaque(false);
        playerListPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        refreshPlayerList();
        panel.add(playerListPanel);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // === RESTART BUTTON ===
        RoundedButton restartBtn = new RoundedButton("RESTART");
        restartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartBtn.setMinimumSize(new Dimension(220, 40));
        restartBtn.setPreferredSize(new Dimension(220, 40));
        restartBtn.setMaximumSize(new Dimension(220, 40));
        restartBtn.setBaseColor(new Color(135, 169, 107));
        restartBtn.addActionListener(e -> restartGameQuick());
        panel.add(restartBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        // === EXIT BUTTON ===
        RoundedButton exitBtn = new RoundedButton("EXIT");
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setMinimumSize(new Dimension(220, 40));
        exitBtn.setPreferredSize(new Dimension(220, 40));
        exitBtn.setMaximumSize(new Dimension(220, 40));
        exitBtn.setBaseColor(new Color(255, 120, 120));
        exitBtn.addActionListener(e -> showExitConfirmation());
        panel.add(exitBtn);

        return panel;
    }

    private JPanel createCurrentTurnPanel(){
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Gradient Kuning Soft
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255, 248, 220),  // Cornsilk
                        0, getHeight(), new Color(255, 239, 186)  // Moccasin
                );
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(new Color(160, 120, 90)); // Coklat Soft
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(1, 1, getWidth()-3, getHeight()-3, 20, 20);
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));
        panel.setMinimumSize(new Dimension(240, 150)); //
        panel.setPreferredSize(new Dimension(240, 150));
        panel.setMaximumSize(new Dimension(240, 150));

        JLabel titleLabel = new JLabel("CURRENT TURN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(139, 69, 19));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === AVATAR ===
        avatarPanel = new PlayerAvatarPanel(players.get(0));
        avatarPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        avatarPanel.setMinimumSize(new Dimension(70, 70));
        avatarPanel.setPreferredSize(new Dimension(70, 70));
        avatarPanel.setMaximumSize(new Dimension(70, 70));

        currentPlayerLabel = new JLabel("Player 1");
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        currentPlayerLabel.setForeground(new Color(255, 120, 120));
        currentPlayerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(avatarPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(currentPlayerLabel);

        return panel;
    }

    class PlayerAvatarPanel extends JPanel {
        private Player player;

        public PlayerAvatarPanel(Player player) {
            this.player = player;
            setOpaque(false);
        }

        public void setPlayer(Player player) {
            this.player = player;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (player == null) return;

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(getWidth(), getHeight());
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            // Shadow
            g2d.setColor(new Color(0, 0, 0, 60));
            g2d.fillOval(x + 3, y + 3, size, size);

            // Gambar karakter dengan warna pemain
            drawMarioCharacter(g2d, x + size/2, y + size/2, size, player.getColor());
        }

        private void drawMarioCharacter(Graphics2D g2d, int cx, int cy, int size, Color color) {
            int scale = size / 80;
            if (scale < 1) scale = 1;

            // Wajah
            g2d.setColor(new Color(255, 224, 189));
            g2d.fillOval(cx - 20*scale, cy - 25*scale, 40*scale, 40*scale);

            // Topi (WARNA PEMAIN)
            g2d.setColor(color);
            g2d.fillArc(cx - 25*scale, cy - 35*scale, 50*scale, 30*scale, 0, 180);
            g2d.fillRect(cx - 25*scale, cy - 25*scale, 50*scale, 10*scale);

            // Brim topi
            g2d.setColor(color.darker());
            g2d.fillRect(cx - 28*scale, cy - 25*scale, 56*scale, 5*scale);

            // Logo M
            g2d.setColor(Color.WHITE);
            g2d.fillOval(cx - 8*scale, cy - 30*scale, 16*scale, 16*scale);
            g2d.setColor(color);
            g2d.setFont(new Font("Arial", Font.BOLD, 14*scale));
            FontMetrics fm = g2d.getFontMetrics();
            String m = "M";
            int textWidth = fm.stringWidth(m);
            g2d.drawString(m, cx - textWidth/2, cy - 20*scale);

            // Mata
            g2d.setColor(Color.BLACK);
            g2d.fillOval(cx - 10*scale, cy - 10*scale, 6*scale, 8*scale);
            g2d.fillOval(cx + 4*scale, cy - 10*scale, 6*scale, 8*scale);
            g2d.setColor(Color.WHITE);
            g2d.fillOval(cx - 8*scale, cy - 9*scale, 3*scale, 3*scale);
            g2d.fillOval(cx + 6*scale, cy - 9*scale, 3*scale, 3*scale);

            // Hidung
            g2d.setColor(new Color(255, 200, 160));
            g2d.fillOval(cx - 4*scale, cy - 2*scale, 8*scale, 10*scale);

            // Kumis
            g2d.setColor(new Color(80, 50, 30));
            g2d.fillArc(cx - 15*scale, cy + 2*scale, 14*scale, 10*scale, 180, 180);
            g2d.fillArc(cx + 1*scale, cy + 2*scale, 14*scale, 10*scale, 180, 180);

            // Baju (WARNA PEMAIN)
            g2d.setColor(color.darker());
            g2d.fillRect(cx - 18*scale, cy + 10*scale, 36*scale, 25*scale);

            // Kancing
            g2d.setColor(new Color(255, 236, 179));
            g2d.fillOval(cx - 5*scale, cy + 18*scale, 10*scale, 10*scale);
        }
    }


    class MarioBackgroundPanel extends JPanel{
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;

            // Sky Gradient Soft
            GradientPaint skyGradient = new GradientPaint(
                    0, 0, new Color(173, 216, 230),  // Light Blue Soft
                    0, getHeight(), new Color(224, 255, 255)  // Azure
            );
            g2d.setPaint(skyGradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(Color.WHITE);
            drawCloud(g2d, 30, 50);
            drawCloud(g2d, 180, 100);
            drawCloud(g2d, 60, 300);
            drawCloud(g2d, 200, 500);
            drawCloud(g2d, 100, 700);

            int blockSize = 40;
            g2d.setColor(new Color(210, 180, 140)); // Tan
            for (int y = getHeight() - 100; y < getHeight(); y += blockSize) {
                for (int x = 0; x < getWidth(); x += blockSize) {
                    g2d.fillRect(x, y, blockSize - 2, blockSize - 2);
                    g2d.setColor(new Color(188, 143, 107));
                    g2d.drawRect(x, y, blockSize - 2, blockSize - 2);
                    g2d.setColor(new Color(210, 180, 140));
                }
            }

            drawPipe(g2d, 20, getHeight() - 200);
            drawPipe(g2d, getWidth() - 70, getHeight() - 180);
            drawCoin(g2d, 250, 200);
            drawCoin(g2d, 80, 450);
        }

        private void drawCloud(Graphics2D g2d, int x, int y) {
            g2d.fillOval(x, y, 50, 30);
            g2d.fillOval(x + 15, y - 12, 50, 35);
            g2d.fillOval(x + 30, y, 50, 30);
        }

        private void drawPipe(Graphics2D g2d, int x, int y) {
            g2d.setColor(new Color(119, 171, 143));
            g2d.fillRect(x, y, 50, 100);
            g2d.setColor(new Color(95, 140, 115));
            g2d.fillRect(x + 5, y, 40, 100);
            g2d.setColor(new Color(144, 198, 168));
            g2d.fillRoundRect(x - 5, y - 15, 60, 20, 10, 10);
            g2d.setColor(new Color(107, 152, 130));
            g2d.drawRoundRect(x - 5, y - 15, 60, 20, 10, 10);
        }

        private void drawCoin(Graphics2D g2d, int x, int y) {
            g2d.setColor(new Color(255, 223, 186)); // Gold Soft
            g2d.fillOval(x, y, 25, 25);
            g2d.setColor(new Color(218, 179, 140));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(x, y, 25, 25);
            g2d.setColor(new Color(255, 223, 186));
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("$", x + 8, y + 18);
        }
    }

    private void updateTurnUI() {
        Player p = players.get(currentPlayerIndex);
        currentPlayerLabel.setText(p.getName());
        currentPlayerLabel.setForeground(p.getColor());

        // UPDATE AVATAR DENGAN PLAYER BARU
        if (avatarPanel != null) {
            avatarPanel.setPlayer(p);
        }

        refreshPlayerList();
    }

    private void refreshPlayerList() {
        playerListPanel.removeAll();

        List<Player> sorted = new ArrayList<>(players);
        sorted.sort((a, b) -> b.getPosition() - a.getPosition());

        for (int i = 0; i < sorted.size(); i++) {
            Player p = sorted.get(i);
            final int rank = i + 1;

            JPanel playerCard = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    g2d.setColor(new Color(255, 255, 255, 230));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                    g2d.setColor(new Color(200, 200, 200));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 15, 15);
                }
            };
            playerCard.setLayout(new BorderLayout(8, 3));
            playerCard.setOpaque(false);
            playerCard.setBorder(new EmptyBorder(6, 10, 6, 10));
            playerCard.setAlignmentX(Component.CENTER_ALIGNMENT);

            // UKURAN KARTU
            playerCard.setMinimumSize(new Dimension(240, 55));
            playerCard.setPreferredSize(new Dimension(240, 55));
            playerCard.setMaximumSize(new Dimension(240, 55));

            // Rank Badge
            JPanel rankPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    Color rankColor = getRankColor(rank);
                    g2d.setColor(rankColor);
                    g2d.fillOval(2, 2, 32, 32);

                    g2d.setColor(new Color(0, 0, 0, 30));
                    g2d.fillOval(4, 4, 28, 28);

                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Arial", Font.BOLD, 16));
                    String rankText = String.valueOf(rank);
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(rankText);
                    g2d.drawString(rankText, 18 - textWidth/2, 24);
                }
            };
            rankPanel.setOpaque(false);
            rankPanel.setPreferredSize(new Dimension(36, 36));
            playerCard.add(rankPanel, BorderLayout.WEST);

            // Info Panel
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setOpaque(false);

            JLabel nameLabel = new JLabel(p.getName());
            nameLabel.setFont(new Font("Arial", Font.BOLD, 13));
            nameLabel.setForeground(p.getColor());

            JLabel posLabel = new JLabel("Position: " + p.getPosition());
            posLabel.setFont(new Font("Arial", Font.PLAIN, 11));
            posLabel.setForeground(new Color(100, 100, 100));

            JLabel bonusLabel = new JLabel("Bonus: " + p.getBonusPoints() + " pts");
            bonusLabel.setFont(new Font("Arial", Font.BOLD, 10));
            bonusLabel.setForeground(new Color(218, 179, 140));

            infoPanel.add(nameLabel);
            infoPanel.add(Box.createRigidArea(new Dimension(0, 1)));
            infoPanel.add(posLabel);
            infoPanel.add(bonusLabel);

            playerCard.add(infoPanel, BorderLayout.CENTER);

            playerListPanel.add(playerCard);
            playerListPanel.add(Box.createRigidArea(new Dimension(0, 6)));
        }

        playerListPanel.revalidate();
        playerListPanel.repaint();
    }


    private Color getRankColor(int rank) {
        switch(rank) {
            case 1: return new Color(255, 236, 179);
            case 2: return new Color(192, 192, 192);
            case 3: return new Color(218, 179, 140);
            default: return new Color(100, 149, 237);
        }
    }

    private void prepareRollDice() {
        if (isAnimating) return;
        isAnimating = true;
        rollButton.setEnabled(false);

        Player current = players.get(currentPlayerIndex);
        canClimbCurrentTurn = isPrime(current.getPosition());
        diceInfoLabel.setText("Rolling...");
        diceInfoLabel.setForeground(Color.WHITE);

        Random rand = new Random();
        finalDiceValue = rand.nextInt(6) + 1;
        javax.swing.Timer t = new javax.swing.Timer(80, null);
        final int[] count = {0};

        t.addActionListener(e -> {
            dicePanel.setDiceValue(new Random().nextInt(6) + 1);
            count[0]++;
            if (count[0] > 12) {
                t.stop();
                processResult();
            }
        });
        t.start();
    }

    private void processResult() {
        Random rand = new Random();
        boolean forward = rand.nextDouble() < 0.7;
        int steps = finalDiceValue;
        dicePanel.setDiceValue(steps);
        updateMoveUI(steps, forward);
        animateMovement(steps, forward);
    }

    private void updateMoveUI(int steps, boolean forward) {
        if (forward) {
            diceInfoLabel.setText("Maju " + steps + " langkah");
            diceInfoLabel.setForeground(new Color(135, 169, 107));
        } else {
            diceInfoLabel.setText("Mundur " + steps + " langkah");
            diceInfoLabel.setForeground(Color.RED);
        }
    }

    private void animateMovement(int steps, boolean forward) {
        Player p = players.get(currentPlayerIndex);
        final int[] moved = {0};

        // ANIMASI TIMER
        javax.swing.Timer t = new javax.swing.Timer(300, null);
        t.addActionListener(e -> {

            // --- KONDISI BERHENTI ---
            if (moved[0] >= steps) {
                t.stop();

                // Cek Bonus
                int bonus = board.collectBonusAt(p.getPosition());
                if (bonus != 0) {
                    p.addBonusPoints(bonus);

                    // Tentukan pesan
                    String msg;
                    if (bonus > 0) {
                        msg = p.getName() + " + " + bonus + " pts!";
                    } else {
                        msg = p.getName() + " " + bonus + " pts!";
                    }

                    new MarioToast(this, msg, 2000);
                    boardPanel.repaint();
                    refreshPlayerList();
                }

                finishTurn(p);
                return;
            }

            // --- GERAKAN PLAYER ---
            if (forward) {
                p.moveForward();
                Main.soundManager.playSFX("/Audio/hero.wav");
            } else {
                // Logika Mundur
                if (p.getPosition() > 1) {
                    p.moveBackward();
                    Main.soundManager.playSFX("/Audio/hero.wav");
                } else {
                    // Jika mentok di Start (posisi 1), hentikan suara mundur
                    Main.soundManager.stopMovementSound();
                }
            }

            moved[0]++;
            boardPanel.repaint();
            refreshPlayerList();

            // Cek Tangga/Ular (tetap sama)
            Tile tile = board.getTileByNumber(p.getPosition());
            if (tile != null && tile.isLadder() && canClimbCurrentTurn) {
                p.setPosition(tile.getDestination());
                boardPanel.repaint();
            }
        });
        t.start();
    }
}
