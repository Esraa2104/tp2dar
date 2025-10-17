package tp2_dar;

import java.io.*;
import java.net.*;

public class ServeurObjet {
    private static final int PORT = 12346;
    
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
           
            System.out.println("Serveur démarré sur le port " + PORT);
            System.out.println("En attente de connexions clients...");
            

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new ClientHandlerObjet(clientSocket).start();
            }
        } catch (IOException e) {
            System.err.println(" Erreur du serveur: " + e.getMessage());
        }
    }
}

class ClientHandlerObjet extends Thread {
    private Socket clientSocket;
    
    public ClientHandlerObjet(Socket socket) {
        this.clientSocket = socket;
    }
    
    public void run() {
        try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {

            System.out.println(" Client objet connecté: " + clientSocket.getInetAddress().getHostAddress());

            while (true) {
                try {
                    // Lecture de l'objet Operation
                    Operation operation = (Operation) in.readObject();
                    System.out.println(" Opération objet reçue: " + operation);

                    // Traitement de l'opération
                    Resultat resultat = traiterOperation(operation);
                    
                    // Envoi de l'objet Resultat
                    out.writeObject(resultat);
                    out.flush();
                    out.reset(); // Important pour éviter la mise en cache
                    
                    System.out.println(" Résultat objet envoyé: " + resultat);
                    System.out.println("----------------------------------------");

                } catch (EOFException e) {
                    System.out.println(" Client déconnecté: " + clientSocket.getInetAddress().getHostAddress());
                    break;
                } catch (ClassNotFoundException e) {
                    Resultat erreur = new Resultat(null, "Type d'objet non reconnu");
                    out.writeObject(erreur);
                }
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

    private Resultat traiterOperation(Operation operation) {
        try {
            // Validation de l'opération
            if (!operation.estValide()) {
                return new Resultat(operation, "Opération invalide");
            }

            double a = operation.getOperande1();
            String op = operation.getOperateur();
            double b = operation.getOperande2();

            // Calcul
            double resultat;
            switch (op) {
                case "+":
                    resultat = a + b;
                    break;
                case "-":
                    resultat = a - b;
                    break;
                case "*":
                    resultat = a * b;
                    break;
                case "/":
                    if (b == 0) {
                        return new Resultat(operation, "Division par zéro impossible");
                    }
                    resultat = a / b;
                    break;
                default:
                    return new Resultat(operation, "Opérateur non supporté: " + op);
            }

            return new Resultat(operation, resultat);

        } catch (Exception e) {
            return new Resultat(operation, "Erreur de traitement: " + e.getMessage());
        }
    }
}