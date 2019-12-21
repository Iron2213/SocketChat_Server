package com.company;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andrea Bacci
 * @author andrea00bacci@libero.it
 */
public class Main {

    public static void main(String[] args) {
        // Main JFrame
        JFrame frame = new JFrame("Server");

        ControlClass server = new ControlClass();

        // I start the server
        server.startServer(6789);

        // Setting up the
        frame.setPreferredSize(new Dimension(300, 300));
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(server.getServerUI(), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}