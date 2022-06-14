package Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.ImageIO;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.Random;

/*
Minesweeper game
 */
public class Minesweeper extends JFrame implements MouseListener {
    private Action solveBoard;
    private JPanel menuPanel;
    private JPanel minePanel;
    private JPanel panel;
    private JLabel clock;
    private JLabel flags;
    private MineButton[][] tiles;
    private Image mine;
    private Image flag;
    private Image dead;
    private Image tada;
    private Timer timer;
    private BigDecimal startTime;
    private int height;
    private int width;
    private int time;
    private int mines;
    private int revealed;
    private boolean firstClick;
    private boolean stopRec;
    private final int DIFFICULTY;

    public Minesweeper(int m, int h, int w) {
        DIFFICULTY = m;
        mines = m;
        height = h;
        width = w;
        revealed = 0;
        firstClick = true;
        stopRec = false;
        solveBoard = new solveBoard(); // easter egg

        tiles = new MineButton[height][width];
        this.setTitle("Minesweeper");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1080, 1080));
        menuPanel = new JPanel();
        minePanel = new JPanel();
        panel = new JPanel();
        clock = new JLabel("Time: " + time);
        flags = new JLabel("Flags: " + mines);

        // Offset to account for Swing Timer inaccuracies. Determined by 1000s comparison test
        timer = new Timer(991, e -> {
            updateClock();
            time++;
        });

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(menuPanel);
        panel.add(minePanel);

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        minePanel.setLayout(new GridLayout(height, width));

        menuPanel.add(Box.createHorizontalGlue());
        menuPanel.add(flags);
        menuPanel.add(Box.createHorizontalGlue());
        menuPanel.add(clock);
        menuPanel.add(Box.createHorizontalGlue());

        try {
            mine = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("mine.png"))).getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            flag = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource("flag.png"))).getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            dead = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("dead.png")));
            tada = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("tada.png")));
        } catch (Exception e) {
            // Do nothing
        }
    }

    // run
    public void run() {
        this.add(panel);
        this.setSize(1080, 1080);
        this.setVisible(true);

        setup();
    }

    // checks if game is won
    public boolean won() {
        return DIFFICULTY == height * width - revealed;
    }

    // update flag display
    public void updateFlags() {
        flags.setText("Flags: " + mines);
    }

    // updates clock display
    public void updateClock() {
        clock.setText("Time: " + time);
    }
    
    // resets board after game end
    public void resetBoard() {
        minePanel.removeAll();
        timer.stop();
        time = 0;
        updateClock();
        startTime = new BigDecimal(0);
        firstClick = true;
        revealed = 0;
        mines = DIFFICULTY;
        updateFlags();
    }

    // reveal mines upon game lose
    public void revealMines() {
        for (MineButton[] r : tiles) {
            for (MineButton tile : r) {
                if (tile.isMine()) {
                    if (!tile.isFlagged() && !tile.isRevealed()) {
                        // show remaining mines
                        tile.setRevealed(true);
                        tile.setIcon(new ImageIcon(mine));
                        tile.setBackground(Color.RED);
                    }
                } else if (!tile.isMine() && tile.isFlagged()) {
                    tile.setBackground(Color.ORANGE);
                }
            }
        }
    }


    // game over (win/loss)
    public void gameEnd(boolean w) {
        stopRec = true;
        timer.stop();
        String msg, title;
        ImageIcon icon;
        BigDecimal duration = new BigDecimal(System.currentTimeMillis()).subtract(startTime).setScale(3, RoundingMode.HALF_UP).divide(new BigDecimal(1000), RoundingMode.HALF_UP);

        if (w) {
            msg = "You won in " + duration + " seconds";
            title = "You Won!";
            icon = new ImageIcon(tada);
        } else {
            revealMines();
            msg = "You lost in " + duration + " seconds. Better luck next time";
            title = "You Lost!";
            icon = new ImageIcon(dead);
        }

        JLabel label = new JLabel(msg);
        label.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        String[] options = {"Quit", "Change Difficulty", "Play Again"};
        int sel = JOptionPane.showOptionDialog(
                null,
                label,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                icon,
                options,
                options[0]
        );

        if (sel == 0 || sel == JOptionPane.CLOSED_OPTION) {
            System.exit(0);
        } else if (sel == 1) {
            this.dispose();
            Main.main(null);
        } else if (sel == 2) {
            this.dispose();
            Minesweeper ms = new Minesweeper(DIFFICULTY, height, width);
            ms.run();
        }
    }

    // declaring tiles
    public void setup() {
        for (int l = 0; l < height; l++) {
            for (int w = 0; w < width; w++) {
                MineButton mb = tiles[l][w] = new MineButton();
                mb.setName(l + " " + w);
                if (!mb.isMine()) {
                    mb.setText("");
                }
                minePanel.add(tiles[l][w]);
                mb.addMouseListener(this);
                // easter egg
                mb.getInputMap().put(KeyStroke.getKeyStroke('`'), "solveBoard");
                mb.getActionMap().put("solveBoard", solveBoard);

            }
        }

        placeMines();
        setNumbers();
    }

    // placing mines randomly
    public void placeMines() {
        Random r = new Random();
        int x, y, count = 0;
        while (count < mines) {
            x = r.nextInt(height);
            y = r.nextInt(width);
            MineButton mb = tiles[x][y];

            if (!mb.isMine()) {
                mb.setMine(true);
                mb.setText(null);
                count++;
            }
        }
    }

    // counts number of mines around each tile and gives respective number
    public void setNumbers() {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                int count = 0;
                MineButton mb = tiles[r][c];

                // 8 buttons around tile
                if (!mb.isMine()) {
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

                mb.setNumber(count);
            }
        }
    }

    // when tile is left-clicked
    public void buttonLeftClicked(int x, int y) {
        // stop recursion on game end
        if (stopRec) {
            return;
        }
        MineButton tile = tiles[x][y];
        // start timer on first safe click
        if (firstClick) {
            firstClick = false;
            startTime = new BigDecimal(System.currentTimeMillis());
            time++;
            timer.start();
        }

        if (!tile.isFlagged()) {
            if (!tile.isRevealed() && !tile.isMine()) {
                tile.setBackground(Color.WHITE);
                tile.showNumber();
                tile.setRevealed(true);
                // auto reveal surrounding safe tiles
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if (tile.getNumber() == 0) {
                            try {
                                buttonLeftClicked(x + i, y + j);
                            } catch (Exception e) {
                                // Do nothing
                            }
                        }
                    }
                }
                revealed++;
                if (won()) {
                    gameEnd(true);
                }
            } else if (tile.isMine()) {
                // automatically restart if first click is a mine, in lieu of first-click-safety
                if (revealed == 0) {
                    this.dispose();
                    resetBoard();
                    run();
                } else {
                    tile.setRevealed(true);
                    tile.setIcon(new ImageIcon(mine));
                    tile.setBackground(Color.YELLOW);

                    gameEnd(false);
                }
            }
        }
    }

    // when tile is right-clicked (flagged)
    public void buttonRightClicked(int x, int y) {
        MineButton tile = tiles[x][y];
        if (!tile.isRevealed() && !tile.isFlagged() && mines > 0) {
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

    // when tile is middle-clicked (quick reveal)
    public void buttonMiddleClicked(int x, int y) {
        MineButton tile = tiles[x][y];

        // count flags around tile
        int f = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                try {
                    MineButton tile2 = tiles[x + i][y + j];
                    if (tile2.isFlagged()) {
                        f++;
                    }
                } catch (Exception e) {
                    // do nothing
                }
            }
        }

        // quick reveal
        if (tile.getNumber() == f && tile.isRevealed()) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    try {
                        buttonLeftClicked(x + i, y + j);
                    } catch (Exception e) {
                        // do nothing
                    }
                }
            }
        }
    }

    // mouse events
    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            JButton btn = (JButton) e.getSource();
            String[] xy = btn.getName().split(" ", 2);
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            buttonRightClicked(x, y);
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            JButton btn = (JButton) e.getSource();
            String[] xy = btn.getName().split(" ", 2);
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            buttonLeftClicked(x, y);
        } else if (SwingUtilities.isMiddleMouseButton(e)) {
            JButton btn = (JButton) e.getSource();
            String[] xy = btn.getName().split(" ", 2);
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            buttonMiddleClicked(x, y);
        }
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public class solveBoard extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            firstClick = false;
            startTime = new BigDecimal(System.currentTimeMillis());
            time++;
            timer.start();
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    MineButton tile = tiles[r][c];
                    if (!tile.isRevealed()) {
                        // remove false flags
                        if (tile.isFlagged() && !tile.isMine()) {
                            tile.setFlagged(false);
                            tile.setIcon(null);
                            mines++;
                        }
                        // flag
                        if (tile.isMine()) {
                            tile.setFlagged(true);
                            tile.setIcon(new ImageIcon(flag));
                            mines--;
                        // reveal
                        } else {
                            tile.setBackground(Color.WHITE);
                            tile.showNumber();
                            tile.setRevealed(true);
                        }
                    }
                    updateFlags();
                }
            }
            gameEnd(true);
        }
    }
}
