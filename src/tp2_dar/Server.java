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
                Socket clientSocket = serverSocket.accept();
                // Création d'un thread pour chaque client
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println("Erreur du serveur: " + e.getMessage());
        }
    }
}

class ClientHandler extends Thread {
    private Socket clientSocket;
    
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }
    
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            System.out.println(" Client connecté: " + clientSocket.getInetAddress().getHostAddress());

            String operation;
            while ((operation = in.readLine()) != null) {
                System.out.println("Opération reçue de " + clientSocket.getInetAddress().getHostAddress() + ": " + operation);

                if (operation.equalsIgnoreCase("quit")) {
                    System.out.println(" Déconnexion du client: " + clientSocket.getInetAddress().getHostAddress());
                    break;
                }

                // Traitement de l'opération
                String resultat = traiterOperation(operation);
                
                // Envoi du résultat au client
                out.println(resultat);
                System.out.println(" Résultat envoyé: " + resultat);
            }

        } catch (IOException e) {
            System.err.println(" Erreur avec le client " + clientSocket.getInetAddress().getHostAddress() + ": " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String traiterOperation(String operation) {
        try {
            // Validation améliorée du format
            if (!validerFormatOperation(operation)) {
                return "ERREUR: Format invalide. Utilisez: 'nombre opérateur nombre' (ex: 34 * 55)";
            }

            // Découpage de l'opération
            String[] parties = operation.trim().split("\\s+");
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
                    return "ERREUR: Opérateur '" + operateur + "' non supporté. Utilisez: +, -, *, /";
            }

            // Formatage du résultat
            if (resultat == (long) resultat) {
                return String.valueOf((long) resultat);
            } else {
                return String.format("%.2f", resultat);
            }

        } catch (NumberFormatException e) {
            return "ERREUR: Format de nombre invalide";
        } catch (Exception e) {
            return "ERREUR: " + e.getMessage();
        }
    }

    private boolean validerFormatOperation(String operation) {
        if (operation == null || operation.trim().isEmpty()) {
            return false;
        }
        // Pattern amélioré pour supports les nombres décimaux
        return operation.trim().matches("-?\\d+(\\.\\d+)?\\s+[+\\-*/]\\s+-?\\d+(\\.\\d+)?");
    }
}