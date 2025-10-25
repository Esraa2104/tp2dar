package tp3_2_dar;
import java.io.*;
import java.net.*;

public class Serveur {
    private static int compteur = 0;
    
    public static void main(String[] args) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
			System.out.println("Serveur prêt sur port 1234");
			
			while (true) {
			    Socket client = serverSocket.accept();
			    System.out.println("Nouveau client connecté");
			    
			    // Lancer un thread GestionClient pour ce client
			    new GestionClient(client).start();
			}
		}
    }
    
    public static synchronized void incrementerCompteur() {
        compteur++;
        System.out.println("Opération #" + compteur);
    }
}