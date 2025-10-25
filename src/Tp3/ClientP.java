package Tp3;
import java.net.*;
import java.io.*;

public class ClientP implements Runnable {
    private Socket client;
    private int numero;
    
    public ClientP(Socket s, int n) {
        this.client = s;
        this.numero = n;
    }
    
    public void run() {
        try {
            // Préparer l'envoi et la réception
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            
            // Envoyer le numéro au client
            out.println(" Bonjour! Vous êtes le client n°" + numero);
            out.println(" Tapez 'bye' pour quitter");
            
            String message;
            // Lire les messages du client
            while ((message = in.readLine()) != null) {
                System.out.println("Client " + numero + " dit: " + message);
                
                if ("bye".equalsIgnoreCase(message)) {
                    out.println(" Au revoir!");
                    break;
                }
                
                // Renvoyer le message
                out.println("Reçu: " + message);
            }
            
            // Fermer la connexion
            client.close();
            System.out.println("Client " + numero + " déconnecté");
            
        } catch (IOException e) {
            System.out.println(" Probleme avec client " + numero);
        }
    }
}
