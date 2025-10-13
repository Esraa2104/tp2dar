package tp2_dar;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVEUR_HOST = "localhost";
    private static final int SERVEUR_PORT = 12345;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Client de calculatrice distribuée");
            System.out.println("Connexion au serveur " + SERVEUR_HOST + ":" + SERVEUR_PORT);

            while (true) {
                System.out.println("\nEntrez une opération:");
                System.out.println("Opérateurs supportés: +, -, *, /");
                System.out.print("> ");

                String operation = scanner.nextLine().trim();

                if (operation.equalsIgnoreCase("quit")) {
                    System.out.println("Déconnexion...");
                    break;
                }

                // Validation basique côté client
                if (!validerOperation(operation)) {
                    System.out.println("Format invalide. Utilisez: nombre operateur nombre");
                    continue;
                }

                // Envoi de l'opération au serveur
                envoyerOperation(operation);
            }
        }
    }

    private static boolean validerOperation(String operation) {
        return operation.matches("\\d+\\s+[+\\-*/]\\s+\\d+");
    }

    private static void envoyerOperation(String operation) {
        try (Socket socket = new Socket(SERVEUR_HOST, SERVEUR_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connexion établie avec le serveur");
            
            // Envoi de l'opération
            out.println(operation);
            System.out.println("Opération envoyée: " + operation);

            // Réception du résultat
            String resultat = in.readLine();
            System.out.println("Résultat reçu: " + resultat);

        } catch (UnknownHostException e) {
            System.err.println("Serveur introuvable: " + e.getMessage());
        } catch (ConnectException e) {
            System.err.println("Impossible de se connecter au serveur. Vérifiez qu'il est démarré.");
        } catch (IOException e) {
            System.err.println("Erreur de communication: " + e.getMessage());
        }
    }
}