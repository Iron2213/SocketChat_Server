package com.company;

import javax.swing.*;
import java.awt.*;

/**
 * @author Andrea Bacci
 * @author andrea00bacci@libero.it
 */
public class ServerUI extends JPanel {
    private TextArea txtArea = null;

    public ServerUI() {
        txtArea = new TextArea();
    }

    public void startUI() {
        setLayout(new BorderLayout());

        setBackground(new Color(42, 42, 46));

        add(txtArea, BorderLayout.CENTER);

        txtArea.setPreferredSize(new Dimension(300, 300));
        txtArea.setEditable(false);
        txtArea.setBackground(new Color(70, 68, 89));
        txtArea.setForeground(Color.white);
    }

    /**
     * @param names Names of current connected users
     */
    public void updateTextArea(String[] names) {
        txtArea.setText("Current connected users: \n");

        for (int i = 0; i < 10; i++) {
            if (names[i] != null)
                txtArea.append("- " + names[i] + "\n");
        }
    }
}
