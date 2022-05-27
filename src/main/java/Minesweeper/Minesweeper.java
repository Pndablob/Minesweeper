package Minesweeper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Random;

/*
TODO make board start with no tiles revealed
 */

public class Minesweeper extends JFrame implements MouseListener {
    private JPanel menuPanel;
    private JPanel minePanel;
    private JPanel panel;
    private JLabel time;
    private JLabel flags;
    private MineButton[][] tiles;
    private int[][] grid;
    private int length;
    private int width;
    private int mines;
    private int revealed;
    private Image mine;
    private Image flag;
    private final GameEngine gameEngine = new GameEngine(this);

    public Minesweeper(int m, int len, int wid) {
        mines = m;
        length = len;
        width = wid;

        tiles = new MineButton[length][width];
        grid = new int[length][width];
        this.setTitle("Minesweeper");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1080, 1080));
        menuPanel = new JPanel();
        minePanel = new JPanel();
        panel = new JPanel();
        time = new JLabel("Time: 000");
        flags = new JLabel("Flags: " + mines);

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(menuPanel);
        panel.add(minePanel);

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        minePanel.setLayout(new GridLayout(length, width));

        menuPanel.add(Box.createHorizontalGlue());
        menuPanel.add(flags);
        menuPanel.add(Box.createHorizontalGlue());
        menuPanel.add(time);
        menuPanel.add(Box.createHorizontalGlue());

        try {
            mine = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("mine.png"))).getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            flag = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("flag.png"))).getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            // Do nothing
        }
    }

    public void placeMines() {
        Random r = new Random();
        int x, y, count = 0;
        while (count < mines) {
            x = r.nextInt(length);
            y = r.nextInt(width);

            if (!tiles[x][y].isMine()) {
                tiles[x][y].setMine(true);
                grid[x][y] = -1;
                count++;
            }
        }
    }

    public void setNumbers() {
        for (int r = 0; r < length; r++) {
            for (int c = 0; c < width; c++) {
                int count = 0;
                MineButton btn = tiles[r][c];

                // 8 buttons around tile
                if (!btn.isMine()) {
                    for (int k = -1; k <= 1; k++) {
                        for (int l = -1; l <= 1; l++) {
                            // in bounds
                            try {
                                assert tiles[r + k] != null;
                                if (tiles[r + k][c + l].isMine()) {
                                    count++;
                                }
                            } catch (Exception e) {
                                // catch edge cases
                            }
                        }
                    }
                }

                btn.setNumber(count);
                grid[r][c] = count;
            }
        }
    }

    public void setup() {
        for (int l = 0; l < length; l++) {
            for (int w = 0; w < width; w++) {
                MineButton mb = tiles[l][w] = new MineButton();
                mb.setName(l + " " + w);
                minePanel.add(tiles[l][w]);
                mb.addActionListener(gameEngine);
                mb.addMouseListener(this);
            }
        }

        placeMines();
        setNumbers();
    }

    public void run() {
        this.add(panel);
        this.setSize(1080, 1080);
        this.setVisible(true);

        setup();
    }

    public boolean won() {
        return mines + revealed == length * width;
    }

    public void updateFlags() {
        flags.setText("Flags: " + mines);
    }

    // when tile is clicked
    public void buttonClicked(int x, int y) {
        MineButton tile = tiles[x][y];
        if (tile.isMine() && !tile.isFlagged()) {
            tile.setIcon(new ImageIcon(mine));
            tile.setBackground(Color.RED);

            String[] options = {"Quit", "Restart This Level", "Play Again"};
            int sel = JOptionPane.showOptionDialog(
                    null,
                    "You Lose!",
                    "Game Over!",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    options,
                    options[0]
            );
        } else if (!tile.isFlagged() && !tile.isRevealed()) {
            if (won()) {
                String[] options = {"Quit", "Change Difficulty", "Play Again"};
                JOptionPane.showOptionDialog(
                        null,
                        "You Won!",
                        "You Won!",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                return;
            }

            tile.setBackground(Color.WHITE);
            tile.showNumber();
            tile.setRevealed(true);
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (tile.getNumber() != 0 || tile.isFlagged()) {
                        continue;
                    }
                    try {
                        buttonClicked(x + i, y + j);
                    }
                    catch (Exception e) {
                        // Do nothing
                    }
                }
            }
        }
    }

    // when tile is right-clicked (flagged)
    public void buttonRightClicked(int x, int y) {
        MineButton tile = tiles[x][y];
        if (!tile.isFlagged() && tile.getNumber() == 0 && tile.isEnabled()) {
            tile.setFlagged(true);
            tile.setIcon(new ImageIcon(flag));
            mines--;
        } else if (tile.isFlagged()) {
            tile.setFlagged(false);
            tile.setIcon(null);
            mines++;
        }
        updateFlags();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            JButton btn = (JButton) e.getSource();
            String[] xy = btn.getName().split(" ", 2);
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            buttonRightClicked(x, y);
        }
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
}
