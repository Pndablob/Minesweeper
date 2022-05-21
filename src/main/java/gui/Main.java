package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    private static int mines;

    public static void inputMines() {
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
        } else {
            mines = inputCustomMines();
        }
    }

    public static int inputCustomMines() {
        return Integer.parseInt(JOptionPane.showInputDialog(
                null,
                "Please enter a custom number of mines"
        ));
    }

    public static void main(String[] args) {
        JFrame menu = new JFrame("Main Menu");
        JButton b = new JButton("Input");
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputMines();
            }
        };

        menu.setSize(500, 500);
        b.setSize(50, 10);
        b.setActionCommand("Input Mines");
        b.addActionListener(actionListener);

        menu.add(b);
        menu.setVisible(true);
    }
}