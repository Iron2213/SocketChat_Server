package com.company;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Bacci
 * @author andrea00bacci@libero.it
 */
public class ControlClass {
    private ServerUI serverUI = null;
    private ServerSocket server = null;
    private final ClientThread[] clients;
    private final String[] clientsNames;
    private List<ClientThread> threads = new ArrayList<>();

    public ControlClass() {
        clientsNames = new String[10];
        clients = new ClientThread[10];
    }

    /**
     * This method start the server
     */
    public void startServer(int port) {
        try {
            server = new ServerSocket(port);
            System.out.println("Server started on port: " + 6789);

            serverUI = new ServerUI();
            serverUI.startUI();

            ClientsListener cL = new ClientsListener();
            cL.start();
        } catch (Exception ex) {

        }
    }

    /**
     *
     *
     * @return Oggetto dell'interfaccia
     */
    public ServerUI getServerUI() {
        return serverUI;
    }

    class ClientsListener extends Thread {
        @Override
        public void run() {
            try {
                clients[0] = new ClientThread(server.accept(), 0);
                clients[0].start();
            } catch (IOException ex) {
                System.out.println(ex);
                System.exit(-1);
            }

            while (true) {
                serverUI.updateTextArea(clientsNames);
                for (int i = 0; i < 10; i++) {

                    if (clients[i] == null) {
                        try {
                            clients[i] = new ClientThread(server.accept(), i);
                            clients[i].start();

                            break;
                        } catch (IOException ex) {
                            System.out.println(ex);
                            System.exit(-1);
                        }
                    } else if (!clients[i].isAlive())
                        clients[i] = null;

                    if (i == 9)
                        System.out.println("Server pieno");
                }
            }
        }
    }

    class ClientThread extends Thread {
        private DataInputStream inStream = null;
        private DataOutputStream outStream = null;
        private int index = 0;
        private String user;

        /**
         * @param IO    Socket assegnato per la creazione degli stream ricevuto
         * @param index Numero della posizione nell'array dei thread
         */
        public ClientThread(Socket IO, int index) {
            this.index = index;

            try {
                inStream = new DataInputStream(IO.getInputStream());
                outStream = new DataOutputStream(IO.getOutputStream());

                user = inStream.readUTF();

                for (int i = 0; i < 10; i++) {
                    if (clientsNames[i] == null) {
                        clientsNames[i] = user;
                        break;
                    }
                }
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }

        @Override
        public void run() {
            sendToClient("server", user + " si è connesso");

            // Main loop
            while (true) {
                try {
                    String text = inStream.readUTF();

                    sendToClient(text);
                } catch (IOException ex) {
                    sendToClient("SERVER", user + " si è disconnesso");

                    for (int i = 0; i < 10; i++) {
                        if (clientsNames[i] != null && clientsNames[i].equals(user))
                            clientsNames[i] = null;
                    }

                    serverUI.updateTextArea(clientsNames);

                    break;
                }
            }
        }

        /**
         * @param text Messaggio da inviare
         */
        public void sendToClient(String text) {
            sendToClient(user, text);
        }

        /**
         * Metodo per l'invio del messaggio ricevuto a tutti gli altri client
         * @param user Username del client mittente
         * @param text Messaggio da inviare
         */
        public void sendToClient(String user, String text) {
            try {
                for (int i = 0; i < 10; i++) {
                    if (i != index && clients[i] != null && clients[i].isAlive()) {
                        clients[i].outStream.writeUTF(user);
                        clients[i].outStream.writeUTF(text);
                    }
                }
            } catch (IOException ex) {
                System.out.println("2: " + ex);
            }
        }
    }
}