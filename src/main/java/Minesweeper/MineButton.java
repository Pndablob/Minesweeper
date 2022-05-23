package Minesweeper;

import javax.swing.*;

public class MineButton extends JButton {
    private int number;
    private boolean isMine;

    public int getNumber() {
        return number;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setNumber(int n) {
        number = n;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }
}
