package com.company;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Andrea Bacci
 * @author andrea00bacci@libero.it
 */
public class ServerUI extends JPanel {
    private TextArea txtArea = null;
    
    public ServerUI() {
        txtArea = new TextArea();
    }
    
    // Metodo per l'avvio dell'interfaccia
    public void startUI() {
        // Impostazione layout utilizzato
        setLayout(new BorderLayout());
        
        // Impostazione colore di sfondo
        setBackground(new Color(42, 42, 46));
        
        // Aggiunta del componente txtArea al pannello
        add(txtArea, BorderLayout.CENTER);
        
        /*
        Area di testo principale
        
        Impostazione dimensione preferita
        Disattivazione possibilità di scrivere e modificare
        Impostazione colori di background e di font
        */
        txtArea.setPreferredSize(new Dimension(300, 300));
        txtArea.setEditable(false);
        txtArea.setBackground(new Color(70, 68, 89));
        txtArea.setForeground(Color.white);
    }
    
    /** Metodo per l'aggiornamento dek testo all'interno dell'area di testo
     *
     * @param names Array dei nomi dei client attualmente connessi
     */
    public void updateTextArea(String[] names) {
        txtArea.setText("Client attualmente connessi: \n");
        
        for (int i = 0; i < 10; i++) {
            if(names[i] != null)
                txtArea.append("- " + names[i] + "\n");
        }
    }
}
