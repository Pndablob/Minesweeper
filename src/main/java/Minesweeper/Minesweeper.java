package Minesweeper;

import javax.swing.*;
import java.util.*;

public class Minesweeper {
    private MineButton[][] grid;
    private int length;
    private int width;
    private int mines;

    public Minesweeper(int length, int width) {
        grid = new MineButton[length][width];
        this.length = length;
        this.width = width;
    }

    public Minesweeper() {
    }

    public void setMines(int m) {
        mines = m;
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

    public void difficulty() {
        String[] options = {"Easy (10)", "Intermediate (40)", "Expert (99)", "Custom"};
        int diff = JOptionPane.showOptionDialog(
                null,
                "Please select the difficulty",
                "Difficulty",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (diff == 0) {
            mines = 10;
        } else if (diff == 1) {
            mines = 40;
        } else if (diff == 2) {
            mines = 99;
        } /*else {
            mines = inputCustomMines();
        }
        */
    }

    /*
    public int inputCustomMines() {
        return Integer.parseInt(JOptionPane.showInputDialog(
                null,
                "Please enter a custom number of mines"
        ));
    }
     */
}
