tppackage Tp3;
import java.net.*;
import java.io.*;

public class Client {
    public static void main(String[] args) {
        System.out.println(" Connexion au serveur...");
        
        try {
            // Se connecter au serveur
            Socket server;
            if (args.length > 0) {
                // Si une IP est donnée
                server = new Socket(args[0], 1234);
            } else {
                // Sinon localhost
                server = new Socket("localhost", 1234);
            }
            
            // Préparer la communication
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            PrintWriter out = new PrintWriter(server.getOutputStream(), true);
            BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
            
            // Lire le message de bienvenue
            String ligne;
            while ((ligne = in.readLine()) != null) {
                System.out.println("Serveur: " + ligne);
                if (ligne.contains("Tapez")) break;
            }
            
            // Dialogue avec le serveur
            String texte;
            System.out.print("Moi: ");
            while ((texte = clavier.readLine()) != null) {
                out.println(texte);  // Envoyer au serveur
                
                if ("bye".equalsIgnoreCase(texte)) break;
                
                // Lire la réponse
                String reponse = in.readLine();
                System.out.println("Serveur: " + reponse);
                System.out.print("Moi: ");
            }
            
            server.close();
            System.out.println(" Déconnexion!");
            
        } catch (IOException e) {
            System.out.println(" Impossible de se connecter: " + e.getMessage());
        }
    }
}