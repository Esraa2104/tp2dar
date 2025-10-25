package Tp3;
import java.net.*;
import java.io.*;

public class ServerMT {
    public static void main(String[] args) {
        
        int clientCount = 0;
        
        try {
            ServerSocket server = new ServerSocket(1234);
            
            while (true) {
                // Attendre un client
                Socket client = server.accept();
                clientCount++;
                
                // Afficher les infos du client
                System.out.println(" Client " + clientCount + " connecté");
                System.out.println(" IP: " + client.getRemoteSocketAddress());
                
                // Créer un thread pour le client
                Thread t = new Thread(new ClientP(client, clientCount));
                t.start();
            }
        } catch (IOException e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }
}