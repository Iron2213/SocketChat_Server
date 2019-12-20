package com.company;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 *
 * @author Andrea Bacci
 * @author andrea00bacci@libero.it
 */
public class Main {

    public static void main(String[] args) {
        // Creazione oggetto per la finestra principale
        JFrame frame = new JFrame("Server");

        // Creazione e avvio server
        ControlClass server = new ControlClass();
        server.startServer();

        /*
        Finestra principale

        Impostazione dimensione preferita
        Disattivazione possibilità di scrivere e modificare
        Impostazione layout della finestra
        Aggiunta del pannello centrale
        Impostazione operazione default di chiusura
        Attuazione dimensioni all'interno della finestra
        Impostazione della finestra come visibile
        */
        frame.setPreferredSize(new Dimension(300, 300));
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        frame.add(server.getServerUI(), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}