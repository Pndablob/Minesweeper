package Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Minesweeper extends JFrame implements MouseListener {
    private JPanel menuPanel;
    private JPanel minePanel;
    private JPanel panel;
    private JLabel time;
    private JLabel minesLeft;
    private MineButton[][] grid;
    private int length;
    private int width;
    private int mines;

    public Minesweeper() {
        // Just to access private variables
    }

    public Minesweeper(int m, int len, int wid) {
        mines = m;
        length = len;
        width = wid;

        grid = new MineButton[length][width];
        this.setTitle("Minesweeper");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        menuPanel = new JPanel();
        minePanel = new JPanel();
        panel = new JPanel();
        time = new JLabel("000");
        minesLeft = new JLabel("Mines Left:");

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(menuPanel);
        panel.add(minePanel);

        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
        minePanel.setLayout(new GridLayout(length, width));

        menuPanel.add(minesLeft);
        menuPanel.add(time);

        for (int l = 0; l < length; l++) {
            for (int w = 0; w < width; w++) {
                MineButton mb = grid[l][w] = new MineButton();//icon);
                minePanel.add(grid[l][w]);
            }
        }
    }

    public MineButton[][] getGrid() {
        return grid;
    }

    public void placeMines() {
        Random r = new Random();
        int x, y, count = 0;
        while (count < mines) {
            x = r.nextInt(length);
            y = r.nextInt(width);

            if (!grid[x][y].isMine()) {
                grid[x][y].setMine(true);
            }
        }
    }

    public void setNumbers() {
        for (int r = 0; r < length; r++) {
            for (int c = 0; c < width; c++) {
                int count = 0;
                MineButton btn = grid[r][c];

                // 8 buttons around tile
                for (int k = -1; k <= 1 ; k++) {
                    for (int l = -1; l <= 1; l++) {
                        // in bounds
                        try {
                            assert grid[r + k] != null;
                            if (grid[r+k][c+l].isMine()) {
                                count++;
                            }
                        }
                        catch (Exception e) {
                            // catch edge cases
                        }
                    }
                }

                btn.setNumber(count);
                // TODO: set button icon based on number
            }
        }
    }

    public void setup() {
        placeMines();
    }

    public void run() {
        this.add(panel);
        this.setSize(1024, 1024);
        this.setVisible(true);
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            JButton btn = (JButton) e.getSource();
            String[] xy = btn.getName().split(" ", 2);
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            GameEngine.buttonRightClicked(x, y);
        }
    }
}
