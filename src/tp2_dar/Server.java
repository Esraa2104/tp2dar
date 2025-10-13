package tp2_dar;
import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 12345;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Serveur démarré sur le port " + PORT);
            System.out.println("En attente de connexions clients...");

            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    System.out.println("Client connecté: " + clientSocket.getInetAddress());

                    // Lecture de l'opération envoyée par le client
                    String operation = in.readLine();
                    System.out.println("Opération reçue: " + operation);

                    // Traitement de l'opération
                    String resultat = traiterOperation(operation);
                    
                    // Envoi du résultat au client
                    out.println(resultat);
                    System.out.println("Résultat envoyé: " + resultat);

                } catch (IOException e) {
                    System.err.println("Erreur avec le client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur du serveur: " + e.getMessage());
        }
    }

    private static String traiterOperation(String operation) {
        try {
            // Validation du format de l'opération
            if (!operation.matches("\\d+\\s+[+\\-*/]\\s+\\d+")) {
                return "ERREUR: Format d'opération invalide. Format attendu: 'nombre operateur nombre'";
            }

            // Découpage de l'opération
            String[] parties = operation.split("\\s+");
            double operand1 = Double.parseDouble(parties[0]);
            String operateur = parties[1];
            double operand2 = Double.parseDouble(parties[2]);

            // Calcul selon l'opérateur
            double resultat;
            switch (operateur) {
                case "+":
                    resultat = operand1 + operand2;
                    break;
                case "-":
                    resultat = operand1 - operand2;
                    break;
                case "*":
                    resultat = operand1 * operand2;
                    break;
                case "/":
                    if (operand2 == 0) {
                        return "ERREUR: Division par zéro impossible";
                    }
                    resultat = operand1 / operand2;
                    break;
                default:
                    return "ERREUR: Opérateur non supporté. Opérateurs valides: +, -, *, /";
            }

            return String.valueOf(resultat);

        } catch (Exception e) {
            return "ERREUR: " + e.getMessage();
        }
    }
}