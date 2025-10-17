package tp2_dar;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVEUR_HOST = "localhost";
    private static final int SERVEUR_PORT = 12345;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connexion au serveur " + SERVEUR_HOST + ":" + SERVEUR_PORT);
 
            while (true) { 
              	System.out.print(" Entrez une opération : ");
                String operation = scanner.nextLine().trim();

                if (operation.isEmpty()) {
                    continue;
                }

                // Validation améliorée côté client
                if (!validerOperation(operation)) {
                    System.out.println(" Format invalide. Utilisez: 'nombre opérateur nombre' (ex: 34 * 55)");
                    System.out.println("   Opérateurs supportés: +, -, *, /");
                    continue;
                }

                // Envoi de l'opération au serveur
                envoyerOperation(operation);
            }
        }
    }

    private static boolean validerOperation(String operation) {
        // Validation identique à celle du serveur pour cohérence
        return operation.matches("-?\\d+(\\.\\d+)?\\s+[+\\-*/]\\s+-?\\d+(\\.\\d+)?");
    }

    private static void envoyerOperation(String operation) {
        try (Socket socket = new Socket(SERVEUR_HOST, SERVEUR_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connexion établie avec le serveur");
            
            // Envoi de l'opération
            out.println(operation);
            System.out.println(" Opération envoyée: " + operation);

            // Réception du résultat
            String resultat = in.readLine();
            
            // Affichage formaté du résultat
            if (resultat.startsWith("ERREUR:")) {
                System.out.println(" " + resultat);
            } else {
                System.out.println(" Résultat: " + operation + " = " + resultat);
            }
            System.out.println("----------------------------------------");

        } catch (UnknownHostException e) {
            System.err.println(" Serveur introuvable: " + e.getMessage());
        } catch (ConnectException e) {
            System.err.println(" Impossible de se connecter au serveur. Vérifiez:");
            System.err.println("   - Le serveur est-il démarré?");
            System.err.println("   - Le port " + SERVEUR_PORT + " est-il correct?");
        } catch (IOException e) {
            System.err.println(" Erreur de communication: " + e.getMessage());
        }
    }
}