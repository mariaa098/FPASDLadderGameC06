import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static SoundManager soundManager;

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        // Start Music
        soundManager = new SoundManager();
        soundManager.playBackgroundMusic("/Audio/backsound.wav");

        SwingUtilities.invokeLater(Main::createStartScreen);
    }

    public static void createStartScreen() {
        JFrame startFrame = new JFrame("Snake & Ladders - Prime Rules Edition");
        startFrame.setSize(1100, 900);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new MarioStartBackgroundPanel();
        mainPanel.setLayout(null);

        // === TITLE SECTION ===
        JLabel titleLabel = new JLabel("SNAKE & LADDERS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(2, 39, 130));
        titleLabel.setBounds(250, 120, 600, 60);
        mainPanel.add(titleLabel);

        JLabel subLabel = new JLabel("Prime Rules Edition", SwingConstants.CENTER);
        subLabel.setFont(new Font("Arial", Font.ITALIC, 22));
        subLabel.setForeground(new Color(99, 139, 228));
        subLabel.setBounds(250, 190, 600, 30);
        mainPanel.add(subLabel);

        // CENTER PANEL
        JPanel centerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Shadow
                g2d.setColor(new Color(0, 0, 0, 80));
                g2d.fillRoundRect(8, 8, getWidth() - 8, getHeight() - 8, 25, 25);

                // Gradient Soft
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(255, 253, 240),      // Ivory Cream
                        0, getHeight(), new Color(255, 245, 220)  // Vanilla Soft
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 25, 25);

                // Border Coklat Soft
                g2d.setColor(new Color(160, 120, 90));
                g2d.setStroke(new BasicStroke(4));
                g2d.drawRoundRect(2, 2, getWidth() - 12, getHeight() - 12, 25, 25);

                // Inner Border
                g2d.setColor(new Color(218, 179, 140));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(8, 8, getWidth() - 24, getHeight() - 24, 20, 20);
            }
        };

        centerPanel.setLayout(null);
        centerPanel.setOpaque(false);
        centerPanel.setBounds(350, 300, 400, 350);
        mainPanel.add(centerPanel);

        // PLAYER COUNT LABEL
        JLabel infoLabel = new JLabel("Jumlah Pemain:", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        infoLabel.setForeground(new Color(80, 40, 0));
        infoLabel.setBounds(50, 40, 300, 30);
        centerPanel.add(infoLabel);

        // COMBO BOX
        String[] options = {"2 Players", "3 Players", "4 Players", "5 Players"};
        JComboBox<String> playerCombo = new JComboBox<>(options);
        playerCombo.setFont(new Font("Arial", Font.BOLD, 18));
        playerCombo.setBounds(100, 90, 200, 45);
        playerCombo.setOpaque(true);
        playerCombo.setBackground(Color.WHITE);
        playerCombo.setForeground(new Color(139, 69, 19));
        playerCombo.setUI(new MarioComboBoxUI());

        playerCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(new EmptyBorder(5, 10, 5, 10));
                label.setOpaque(true);

                if (isSelected) {
                    label.setBackground(new Color(255, 239, 186));
                    label.setForeground(new Color(139, 69, 19));
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(new Color(139, 69, 19));
                }
                return label;
            }
        });

        playerCombo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(139, 69, 19), 3, true),
                new EmptyBorder(0, 5, 0, 0)
        ));
        centerPanel.add(playerCombo);

        // START BUTTON
        Mario3DButton startButton = new Mario3DButton("START GAME");
        startButton.setBounds(75, 180, 250, 60);
        startButton.addActionListener(e -> {
            int totalPlayers = playerCombo.getSelectedIndex() + 2;
            startFrame.dispose();
            showCustomNameInput(totalPlayers);
        });
        centerPanel.add(startButton);

        // DECORATIVE COIN
        JPanel coinPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(new Color(255, 223, 186)); // Gold Soft
                g2d.fillOval(0, 0, 60, 60);

                g2d.setColor(new Color(218, 179, 140)); // Burlywood
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(0, 0, 60, 60);

                g2d.setColor(new Color(255, 223, 186));
                g2d.fillOval(10, 10, 40, 40);
            }
        };
        coinPanel.setOpaque(false);
        coinPanel.setBounds(170, 260, 60, 60);
        centerPanel.add(coinPanel);

        // AUTO CENTER SAAT RESIZE
        mainPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                int w = mainPanel.getWidth();
                int h = mainPanel.getHeight();

                // Center title & subtitle
                int titleX = (w - 600) / 2;
                titleLabel.setBounds(titleX, 120, 600, 60);
                subLabel.setBounds(titleX, 190, 600, 30);

                // Center panel
                int panelX = (w - 400) / 2;
                int panelY = (h - 350) / 2;
                centerPanel.setBounds(panelX, panelY, 400, 350);
            }
        });

        startFrame.setContentPane(mainPanel);
        startFrame.setVisible(true);
    }


    private static void showCustomNameInput(int totalPlayers) {
        JDialog dialog = new JDialog((Frame) null, "Player Setup", true);
        dialog.setSize(600, 250 + totalPlayers * 70);
        dialog.setLocationRelativeTo(null);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
        JPanel mainPanel = new MarioStartBackgroundPanel();
        mainPanel.setLayout(null);

        JPanel centerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 80));
                g2d.fillRoundRect(8, 8, getWidth() - 8, getHeight() - 8, 25, 25);
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(255, 253, 240),  // Ivory Cream (Hampir putih)
                        0, getHeight(), new Color(255, 245, 220)  // Vanilla Soft
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth() - 8, getHeight() - 8, 25, 25);
                g2d.setColor(new Color(180, 140, 0));
                g2d.setStroke(new BasicStroke(4));
                g2d.drawRoundRect(2, 2, getWidth() - 12, getHeight() - 12, 25, 25);
                g2d.setColor(new Color(255, 230, 100));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(8, 8, getWidth() - 24, getHeight() - 24, 20, 20);
            }
        };
        centerPanel.setLayout(null);
        centerPanel.setOpaque(false);

        int panelHeight = 100 + totalPlayers * 70 + 80;
        centerPanel.setBounds(100, 80, 400, panelHeight);
        mainPanel.add(centerPanel);

        JLabel head = new JLabel("Masukkan Nama Pemain", SwingConstants.CENTER);
        head.setFont(new Font("Arial", Font.BOLD, 24));
        head.setForeground(new Color(80, 40, 0));
        head.setBounds(0, 20, 400, 35);
        centerPanel.add(head);

        List<JTextField> inputs = new ArrayList<>();
        int startY = 75;

        for (int i = 0; i < totalPlayers; i++) {
            // 1. LABEL "Player X:" (TETAP ADA)
            JLabel lbl = new JLabel("Player " + (i + 1) + ":");
            lbl.setFont(new Font("Arial", Font.BOLD, 16));
            lbl.setForeground(new Color(80, 40, 0)); // Warna Coklat Gelap
            lbl.setBounds(40, startY + (i * 60), 100, 30);
            centerPanel.add(lbl);

            // KOLOM INPUT
            JTextField tf = new JTextField("Player " + (i + 1));
            tf.setFont(new Font("Arial", Font.PLAIN, 16));
            tf.setBounds(150, startY + (i * 60), 210, 35);
            tf.setBackground(Color.WHITE);
            tf.setForeground(new Color(139, 69, 19)); // Teks Coklat

            // Border
            tf.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(139, 69, 19), 2),
                    new EmptyBorder(0, 5, 0, 5)
            ));

            inputs.add(tf);
            centerPanel.add(tf);
        }

        Mario3DButton goBtn = new Mario3DButton("START GAME");
        goBtn.setBounds(100, startY + (totalPlayers * 60) + 20, 200, 50);
        goBtn.addActionListener(ev -> {
            List<String> names = new ArrayList<>();
            for (JTextField tf : inputs) {
                String val = tf.getText().trim();
                names.add(val.isEmpty() ? "Unknown" : val);
            }
            dialog.dispose();
            new GameFrame(names);
        });
        centerPanel.add(goBtn);

        dialog.setContentPane(mainPanel);
        dialog.setVisible(true);

    }

    static class MarioStartBackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Sky gradient SOFT
            GradientPaint sky = new GradientPaint(
                    0, 0, new Color(173, 216, 230),  // Light Blue
                    0, getHeight(), new Color(224, 255, 255)  // Azure Soft
            );
            g2d.setPaint(sky);
            g2d.fillRect(0, 0, getWidth(), getHeight());

            // Clouds
            drawCloud(g2d, 80, 100);
            drawCloud(g2d, 300, 60);
            drawCloud(g2d, 650, 120);
            drawCloud(g2d, 850, 80);

            // Hills (Hijau Soft)
            g2d.setColor(new Color(152, 191, 144)); // Sage Green Soft
            g2d.fillOval(50, getHeight() - 250, 250, 200);
            g2d.fillOval(750, getHeight() - 220, 300, 180);

            // Ground (Hijau Pastel)
            g2d.setColor(new Color(170, 216, 152)); // Light Green Soft
            g2d.fillRect(0, getHeight() - 120, getWidth(), 120);

            // Brick pattern (Coklat Soft)
            int blockSize = 40;
            g2d.setColor(new Color(210, 180, 140)); // Tan/Coklat Soft
            for (int x = 0; x < getWidth(); x += blockSize) {
                g2d.fillRect(x, getHeight() - 80, blockSize - 2, blockSize - 2);
                g2d.setColor(new Color(188, 143, 107)); // Border Coklat Lebih Gelap
                g2d.drawRect(x, getHeight() - 80, blockSize - 2, blockSize - 2);
                g2d.setColor(new Color(210, 180, 140));
            }

            // Pipes (Hijau Soft)
            drawPipe(g2d, 150, getHeight() - 220);
            drawPipe(g2d, 880, getHeight() - 200);
        }

        private void drawCloud(Graphics2D g2d, int x, int y) {
            g2d.setColor(Color.WHITE);
            g2d.fillOval(x, y, 50, 30);
            g2d.fillOval(x + 15, y - 12, 50, 35);
            g2d.fillOval(x + 30, y, 50, 30);
        }

        private void drawPipe(Graphics2D g2d, int x, int y) {
            g2d.setColor(new Color(119, 171, 143)); // Hijau Pipe Soft
            g2d.fillRect(x, y, 60, 120);
            g2d.setColor(new Color(95, 140, 115)); // Hijau Gelap Soft
            g2d.fillRect(x + 7, y, 46, 120);
            g2d.setColor(new Color(144, 198, 168)); // Hijau Terang Soft
            g2d.fillRoundRect(x - 7, y - 18, 74, 25, 12, 12);
            g2d.setColor(new Color(107, 152, 130));
            g2d.drawRoundRect(x - 7, y - 18, 74, 25, 12, 12);
        }
    }

    static class Mario3DButton extends JButton {
        private Color baseColor = new Color(255, 120, 120); // Merah Soft
        private Color hoverColor = new Color(255, 150, 150);
        private Color pressedColor = new Color(230, 100, 100);
        private boolean isHovered = false;
        private boolean isPressed = false;

        public Mario3DButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setFont(new Font("Arial", Font.BOLD, 20));
            setForeground(Color.WHITE);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    isHovered = true; repaint();
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    isHovered = false; repaint();
                }
                public void mousePressed(java.awt.event.MouseEvent evt) {
                    isPressed = true; repaint();
                }
                public void mouseReleased(java.awt.event.MouseEvent evt) {
                    isPressed = false; repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color currentColor = isPressed ? pressedColor : (isHovered ? hoverColor : baseColor);
            int offset = isPressed ? 4 : 0;

            // Shadow
            g2d.setColor(new Color(0, 0, 0, 80));
            g2d.fillRoundRect(4, 8, getWidth() - 8, getHeight() - 8, 15, 15);

            // Button Body
            GradientPaint gp = new GradientPaint(0, 0, currentColor.brighter(), 0, getHeight(), currentColor);
            g2d.setPaint(gp);
            g2d.fillRoundRect(0, offset, getWidth() - 8, getHeight() - 8 - offset, 15, 15);

            // Border
            g2d.setColor(currentColor.darker());
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(1, offset + 1, getWidth() - 10, getHeight() - 10 - offset, 15, 15);

            // Text
            g2d.setColor(Color.WHITE);
            FontMetrics fm = g2d.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent() + offset;
            g2d.drawString(getText(), x, y);
        }
    }

    static class MarioComboBoxUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            JButton btn = new JButton() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Background tombol panah
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(0, 0, getWidth(), getHeight());

                    // Gambar Segitiga Panah
                    g2d.setColor(new Color(139, 69, 19));
                    int size = 10;
                    int x = (getWidth() - size) / 2;
                    int y = (getHeight() - size) / 2 + 2;
                    int[] xPoints = {x, x + size, x + size / 2};
                    int[] yPoints = {y, y, y + size};
                    g2d.fillPolygon(xPoints, yPoints, 3);
                }
            };
            btn.setBorder(BorderFactory.createEmptyBorder());
            btn.setContentAreaFilled(false);
            return btn;
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    JScrollPane scroller = super.createScroller();
                    scroller.setVerticalScrollBar(new JScrollBar(JScrollBar.VERTICAL) {
                        @Override
                        public Dimension getPreferredSize() {
                            return new Dimension(0, 0); // Hide scrollbar
                        }
                    });
                    return scroller;
                }
            };
            // Border popup coklat
            popup.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2));
            return popup;
        }

        @Override
        public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(Color.WHITE);
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

            String selectedValue = (String) comboBox.getSelectedItem();
            if (selectedValue != null) {
                g.setColor(new Color(139, 69, 19)); // Coklat Mario
                g.setFont(comboBox.getFont());

                // Center Text
                FontMetrics fm = g.getFontMetrics();
                int x = bounds.x + (bounds.width - fm.stringWidth(selectedValue)) / 2;
                int y = bounds.y + ((bounds.height - fm.getHeight()) / 2) + fm.getAscent();

                g.drawString(selectedValue, x, y);
            }
        }

    }

}
