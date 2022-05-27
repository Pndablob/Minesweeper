package Minesweeper;

import java.awt.*;
import javax.swing.*;

public class MineButton extends JButton {
    private int number;
    private boolean isMine;
    private boolean isFlagged;

    public MineButton() {
        this.setBackground(new Color(171, 171, 171));
        this.setFont(new Font("Arial", Font.BOLD, 16));
    }

    public int getNumber() {
        return number;
    }

    public boolean isMine() {
        return isMine;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setNumber(int n) {
        number = n;
        if (n != 0) {
            this.setForeground(numToColor(n));
            this.setText(String.valueOf(number));
        }
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public Color numToColor(int n) {
        if (n == 1) {
            return new Color(41, 4, 207);
        } else if (n == 2) {
            return new Color(7, 112, 35);
        } else if (n == 3) {
            return new Color(219, 21, 7);
        } else if (n == 4) {
            return new Color(24, 11, 140);
        } else if (n == 5) {
            return new Color(128, 25, 22);
        } else if (n == 6) {
            return new Color(17, 166, 151);
        } else if (n == 7) {
            return new Color(113, 7, 145);
        } else {
            return new Color(0, 0, 0);
        }
    }
}
