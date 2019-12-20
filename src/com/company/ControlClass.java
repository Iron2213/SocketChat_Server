package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Andrea Bacci
 * @author andrea00bacci@libero.it
 */
public class ControlClass {
    private ServerUI serverUI = null;
    private ServerSocket server = null;
    private final ClientThread[] clients;
    private final String[] clientsNames;

    public ControlClass() {
        clientsNames = new String[10];
        clients = new ClientThread[10];
    }
    
    // Metodo per l'avvio del server e delle sue componentistiche
    public void startServer() {
        try {
            // Creazione del serverSocket su porta data
            server = new ServerSocket(6789);
            System.out.println("Server avviato su porta 6789");
            
            // Avvio interfaccia utente
            serverUI = new ServerUI();
            serverUI.startUI();
            
            /* 
            Creazione e avvio thread per l'ascolto continuo delle richieste
            di connessione dei client
            */
            ClientsListener cL = new ClientsListener();
            cL.start();
        }
        catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    /** Metodo per ottenere la classe che gestisce la UI essendo estesa come JPanel
     *
     * @return Oggetto dell'interfaccia
     */
    public ServerUI getServerUI() {
        return serverUI;
    }
    
    // Classe per l'ascolto delle richieste di connessione da parte dei client
    class ClientsListener extends Thread {
        @Override
        public void run() {
            try {
                // Attesa della richiesta del primo cliente e avvio thread assegnato
                clients[0] = new ClientThread(server.accept(), 0);
                clients[0].start();
            }
            catch(IOException ex) {
                System.out.println(ex);
                System.exit(-1);
            }
            
            while(true) {
                // Aggiornamento testo nell'interfaccia
                serverUI.updateTextArea(clientsNames);
                for(int i = 0; i < 10; i++) {

                    // Controllo spazi vuoti nell'array dei thread
                    if(clients[i] == null) {
                        try {
                            /* 
                            Creazione e avvio thread per l'ascolto continuo delle richieste
                            di connessione dei client
                            */
                            clients[i] = new ClientThread(server.accept(), i);
                            clients[i].start();
                            
                            break;
                        }
                        catch (IOException ex) {
                            System.out.println(ex);
                            System.exit(-1);
                        }
                    }
                    else if(!clients[i].isAlive()) // Azzeramento spazi vuoti nell'array dei thread
                        clients[i] = null;
                    
                    // Controllo per segnalare che il server è pieno
                    if(i == 9)
                        System.out.println("Server pieno");
                }
            }
        }
    }
    
    // Classe Thread per la gestione di un singolo client
    class ClientThread extends Thread {
        private DataInputStream inStream = null;
        private DataOutputStream outStream = null;
        private int index = 0;
        private String user;        
        
        /**
         * 
         * @param IO Socket assegnato per la creazione degli stream ricevuto
         * @param index Numero della posizione nell'array dei thread
         */
        public ClientThread(Socket IO, int index) {
            this.index = index;
            
            try {
                // Creazione canali di comunicazione input e output
                inStream = new DataInputStream(IO.getInputStream());
                outStream = new DataOutputStream(IO.getOutputStream());
                
                // Lettura da stream del nome utente dato dal client
                user = inStream.readUTF();
                
                // Inserimento dell'username all'interno dell'array
                for (int i = 0; i < 10; i++) {
                    if(clientsNames[i] == null) {
                        clientsNames[i] = user;
                        break;
                    }
                }
            }
            catch (IOException ex) {
                System.out.println(ex);
            }
        }
        
        @Override
        public void run() {
            // Invio ai client della notifica di connessione di un utente
            sendToClient("server", user + " si è connesso");
            
            // Main loop
            while(true) {
                try {
                    // Lettura messaggio inviato dal client
                    String text = inStream.readUTF();
                    
                    //Invio del messaggio ricevuto a gli altri client
                    sendToClient(text);
                }
                catch (IOException ex) {
                    // Invio ai client della notifica di disconnessione di un utente
                    sendToClient("SERVER", user + " si è disconnesso");
                    
                    // Azzeramento dello spazio occupato dal client disconnesso
                    for (int i = 0; i < 10; i++) {
                        if(clientsNames[i] != null && clientsNames[i].equals(user))
                            clientsNames[i] = null;
                    }
                    
                    // Aggiornamento testo nell'interfaccia
                    serverUI.updateTextArea(clientsNames);
                    
                    break;
                }
            }
        }
        
        /**
         * 
         * @param text Messaggio da inviare
         */
        public void sendToClient(String text) {            
            sendToClient(user, text);
        }
        
        /** Metodo per l'invio del messaggio ricevuto a tutti gli altri client
         *
         * @param user Username del client mittente
         * @param text Messaggio da inviare
         */
        public void sendToClient(String user, String text) {   
            try {
                for(int i = 0; i < 10; i++) {
                    if(i != index && clients[i] != null && clients[i].isAlive()) {
                        clients[i].outStream.writeUTF(user);
                        clients[i].outStream.writeUTF(text);
                    }
                }
            }
            catch (IOException ex) {
                System.out.println("2: " + ex);
            }
        }
    }
}