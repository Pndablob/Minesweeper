package Minesweeper;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // difficulty selector
        String[] options = {"Beginner (10)", "Intermediate (40)", "Expert (99)", "Full Board (200)", "Custom"};
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

        int m = 0, h = 0, w = 0;
        if (diff == 0) {
            m = 10;
            h = 9;
            w = 9;
        } else if (diff == 1) {
            m = 40;
            h = 16;
            w = 16;
        } else if (diff == 2) {
            m = 99;
            h = 24;
            w = 24;
        } else if (diff == 3) {
            m = 200;
            // max height and width for 1920x1080 display
            h = 24;
            w = 43;

        } else if (diff == 4) {
            // custom height, width, and mines input
            while (true) {
                try {
                    String height = JOptionPane.showInputDialog("Please enter the grid height");
                    if (height == null) {
                        main(null);
                        return;
                    }
                    h = Integer.parseInt(height);
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
                    String width = JOptionPane.showInputDialog("Please enter the grid width");
                    if (width == null) {
                        main(null);
                        return;
                    }
                    w = Integer.parseInt(width);
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
                    String mines = JOptionPane.showInputDialog("Please enter the number of mines");
                    if (mines == null) {
                        main(null);
                        return;
                    }
                    m = Integer.parseInt(mines);
                    if (!(m >= 1 && m <= w * h)) {
                        JOptionPane.showMessageDialog(null,
                                "Enter an integer between 1 and " + w * h + ", inclusive", "Input Out of Bounds",
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

        Minesweeper ms = new Minesweeper(m, h, w);
        ms.run();
    }
}