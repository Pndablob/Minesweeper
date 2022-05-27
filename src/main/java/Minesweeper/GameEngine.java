package Minesweeper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class GameEngine implements ActionListener {
    Minesweeper ms;

    public GameEngine(Minesweeper minesweeper) {
        ms = minesweeper;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton btn = (JButton) e.getSource();
        String[] xy = btn.getName().split(" ", 2);
        int x = Integer.parseInt(xy[0]);
        int y = Integer.parseInt(xy[1]);
        ms.buttonClicked(x, y);
    }
}
