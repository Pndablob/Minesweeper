package Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Minesweeper extends JFrame implements MouseListener {
    private JFrame frame;
    private JPanel menuPanel;
    private JPanel minePanel;
    private JComboBox<Object> difficulty;
    private JLabel time;
    private JLabel minesLeft;

    private MineButton[][] grid;
    private int length;
    private int width;
    private int mines;

    public Minesweeper(int len, int wid) {
        super();
        length = len;
        width = wid;

        grid = new MineButton[length][width];
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuPanel = new JPanel();
        minePanel = new JPanel();
        difficulty = new JComboBox<>();
        time = new JLabel("000");
        minesLeft = new JLabel();

        menuPanel.setLayout(new GridLayout(3, 0));
        minePanel.setLayout(new GridLayout(length, width));

        difficulty.addItem("Beginner");
        difficulty.addItem("Intermediate");
        difficulty.addItem("Expert");

        menuPanel.add(difficulty);
        menuPanel.add(minesLeft);
        menuPanel.add(time);

        // TODO: Button Icons
        //ImageIcon icon = new ImageIcon("C:/Users/henry/IdeaProjects/GUIProject/src/main/resources/tile.png");
        for (int l = 0; l < length; l++) {
            for (int w = 0; w < width; w++) {
                MineButton mb = grid[l][w] = new MineButton();//icon);
                minePanel.add(grid[l][w]);
                /* mb.setHorizontalTextPosition(JButton.CENTER);
                mb.setVerticalTextPosition(JButton.CENTER);

                mb.addComponentListener(new ComponentAdapter() {
                    @Override
                    public void componentResized(ComponentEvent e) {
                        JButton btn = (JButton) e.getComponent();
                        Dimension size = btn.getSize();
                        Insets insets = btn.getInsets();
                        size.width -= insets.left + insets.right;
                        size.height -= insets.top + insets.bottom;
                        if (size.width > size.height) {
                            size.width = -1;
                        } else {
                            size.height = -1;
                        }
                        Image scaled = icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
                        btn.setBorder(BorderFactory.createEmptyBorder());
                        btn.setIcon(new ImageIcon(scaled));
                    }
                }); */
            }
        }
    }

    public void placeMines() {
        Random r = new Random();
        int n = length*width;

        for (int l = 0; l < length; l++) {
            for (int w = 0; w < width; w++) {
                if (r.nextInt(n) >= n - mines) {
                    grid[l][w].setMine(true);
                    n--;
                    mines--;
                }
            }
        }
    }

    public void run() {
        frame.add(menuPanel);
        frame.add(minePanel);
        frame.setSize(800, 800);
        frame.setVisible(true);
    }

    public void mouseClicked(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
}
