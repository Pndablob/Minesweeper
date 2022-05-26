package Minesweeper;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // difficulty selector
        String[] options = {"Beginner (10)", "Intermediate (40)", "Expert (99)"};
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

        int mines = 0, d = 0;
        if (diff == 0) {
            mines = 10;
            d = 10;
        } else if (diff == 1) {
            mines = 40;
            d = 16;
        } else if (diff == 2) {
            mines = 99;
            d = 25;
        }

        Minesweeper ms = new Minesweeper(mines, d, d);
        ms.run();
    }
}