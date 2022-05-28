package Minesweeper;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // difficulty selector
        String[] options = {"Beginner (10)", "Intermediate (40)", "Expert (99)", "Full Board", "Custom"};
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

        int mines = 0, h = 0, w = 0;
        if (diff == 0) {
            mines = 10;
            h = 9;
            w = 9;
        } else if (diff == 1) {
            mines = 40;
            h = 16;
            w = 16;
        } else if (diff == 2) {
            mines = 99;
            h = 24;
            w = 24;
        } else if (diff == 3) {
            mines = 5;
            h = 24; // max h
            w = 43; // max w

        } else if (diff == 4) {
            while (true) {
                try {
                    h = Integer.parseInt(JOptionPane.showInputDialog("Please enter the grid height"));
                    if (!(h >= 6 && h <= 24)) {
                        JOptionPane.showMessageDialog(null,
                                "Enter an integer between 6 and 24, inclusive", "Input Out of Bounds",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                            "Enter a valid integer!", "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            while (true) {
                try {
                    w = Integer.parseInt(JOptionPane.showInputDialog("Please enter the grid width"));
                    if (!(w >= 6 && w <= 43)) {
                        JOptionPane.showMessageDialog(null,
                                "Enter an integer between 6 and 43, inclusive", "Input Out of Bounds",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                            "Enter a valid integer!", "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            while (true) {
                try {
                    mines = Integer.parseInt(JOptionPane.showInputDialog("Please enter the number of mines"));
                    if (!(mines >= 1 && mines <= w*h)) {
                        JOptionPane.showMessageDialog(null,
                                "Enter an integer between 1 and " + w*h + ", inclusive", "Input Out of Bounds",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        break;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                            "Enter a valid integer!", "Invalid Input",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        } else {
            System.exit(0);
        }

        Minesweeper ms = new Minesweeper(mines, h, w);
        ms.run();
    }
}