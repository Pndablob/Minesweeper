package AI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Main {
    public static void makeScreenshot(JFrame argFrame) {
        Rectangle rec = argFrame.getBounds();
        BufferedImage bufferedImage = new BufferedImage(rec.width, rec.height, BufferedImage.TYPE_INT_ARGB);
        argFrame.paint(bufferedImage.getGraphics());

        try {
            // Create temp file in C:\Users\name\AppData\Local\Temp
            File temp = File.createTempFile("screenshot", ".png");

            // Use the ImageIO API to write the bufferedImage to a temporary file
            ImageIO.write(bufferedImage, "png", temp);

            // Delete temp file when program exits
            temp.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        JFrame test = new JFrame("Test Frame");
        test.setSize(1080, 1080);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 1));

        JButton btn = new JButton("hello there");
        btn.setBackground(Color.BLUE);
        btn.setForeground(Color.WHITE);

        panel.add(btn, 0, 0);
        test.add(panel);
        test.setVisible(true);

        makeScreenshot(test);

    }
}

// MS AI in Java reference: https://github.com/luckytoilet/MSolver/blob/master/MSolver.java

