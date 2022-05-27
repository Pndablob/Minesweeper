package Minesweeper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Random;

/*

 */

public class Minesweeper extends JFrame implements MouseListener {
    private JPanel menuPanel;
    private JPanel minePanel;
    private JPanel panel;
    private JLabel clock;
    private JLabel flags;
    private MineButton[][] tiles;
    private Image mine;
    private Image flag;
    private Timer timer;
    private int length;
    private int width;
    private int time;
    private int mines;
    private int revealed = 0;
    private final int DIFFICULTY;
    private final GameEngine gameEngine = new GameEngine(this);

    public Minesweeper(int m, int len, int wid) {
        DIFFICULTY = m;
        mines = m;
        length = len;
        width = wid;

        tiles = new MineButton[length][width];
        this.setTitle("Minesweeper");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(1080, 1080));
        menuPanel = new JPanel();
        minePanel = new JPanel();
        panel = new JPanel();
        clock = new JLabel("Time: " + time);
        flags = new JLabel("Flags: " + mines);

        timer = new Timer(1000, e -> {
            time++;
            clock.setText("Time: " + time);
        });

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(menuPanel);
        panel.add(minePanel);

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        minePanel.setLayout(new GridLayout(length, width));

        menuPanel.add(Box.createHorizontalGlue());
        menuPanel.add(flags);
        menuPanel.add(Box.createHorizontalGlue());
        menuPanel.add(clock);
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
            MineButton mb = tiles[x][y];

            if (!mb.isMine()) {
                mb.setMine(true);
                mb.setText(null);
                count++;
            }
        }
    }

    public void setNumbers() {
        for (int r = 0; r < length; r++) {
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

    public void setup() {
        for (int l = 0; l < length; l++) {
            for (int w = 0; w < width; w++) {
                MineButton mb = tiles[l][w] = new MineButton();
                mb.setName(l + " " + w);
                if (!mb.isMine()) {
                    mb.setText("");
                }
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
        return DIFFICULTY == length * width - revealed;
    }

    public void updateFlags() {
        flags.setText("Flags: " + mines);
    }

    public void resetBoard() {
        minePanel.removeAll();
        timer.stop();
        time = 0;
        clock.setText("Time: " + time);
        revealed = 0;
        mines = DIFFICULTY;
        flags.setText("Flags: " + mines);
    }

    public void gameEnd(boolean w) {
        timer.stop();
        String msg, title;
        if (w) {
            msg = "You Win!";
            title = "You Win!";
        } else {
            msg = "You lost! Better luck next time!";
            title = "You Lost!";
        }

        String[] options = {"Quit", "Change Difficulty", "Play Again"};
        int sel = JOptionPane.showOptionDialog(
                null,
                msg,
                title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (sel == 0) {
            System.exit(0);
        } else if (sel == 1) {
            this.dispose();
            Main.main(null);
        } else if (sel == 2) {
            this.dispose();
            resetBoard();
            run();
        }
    }

    // when tile is clicked
    public void buttonClicked(int x, int y) {
        if (revealed == 0) {
            timer.start();
        }
        MineButton tile = tiles[x][y];
        if (tile.isMine() && !tile.isFlagged()) {
            if (revealed == 0) {
                this.dispose();
                resetBoard();
                run();
            } else {
                tile.setIcon(new ImageIcon(mine));
                tile.setBackground(Color.RED);

                gameEnd(false);
            }
        } else if (!tile.isFlagged() && !tile.isRevealed()) {
            tile.setBackground(Color.WHITE);
            tile.showNumber();
            tile.setRevealed(true);
            revealed++;
            System.out.println(revealed);
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

            if (won()) {
                gameEnd(true);
            }
        }
    }

    // when tile is right-clicked (flagged)
    public void buttonRightClicked(int x, int y) {
        MineButton tile = tiles[x][y];
        if (!tile.isRevealed() && !tile.isFlagged()) {
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
