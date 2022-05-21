package gui;

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

    public void setMines(int m) {
        mines = m;
    }
}
