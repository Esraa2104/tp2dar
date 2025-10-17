package tp2_dar;


import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClientObjet {
    private static final String SERVEUR_HOST = "localhost";
    private static final int SERVEUR_PORT = 12346;
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connexion au serveur " + SERVEUR_HOST + ":" + SERVEUR_PORT);
      

            while (true) {
                System.out.print(" Entrez une opération : ");
                
                String input = scanner.nextLine().trim();

  

                if (input.isEmpty()) {
                    continue;
                }

                // Conversion de l'entrée en objet Operation
                Operation operation = parserOperation(input);
                if (operation != null) {
                    // Envoi de l'objet au serveur
                    envoyerOperation(operation);
                }
            }
        }
    }

    private static Operation parserOperation(String input) {
        try {
            String[] parties = input.trim().split("\\s+");
            
            if (parties.length != 3) {
                System.out.println(" Format incorrect. Utilisez: nombre opérateur nombre");
                return null;
            }

            double op1 = Double.parseDouble(parties[0]);
            String operateur = parties[1];
            double op2 = Double.parseDouble(parties[2]);

            // Validation de l'opérateur
            if (!"+-*/".contains(operateur)) {
                System.out.println(" Opérateur non supporté: '" + operateur + "'. Utilisez +, -, *, /");
                return null;
            }

            return new Operation(op1, operateur, op2);

        } catch (NumberFormatException e) {
            System.out.println(" Format de nombre invalide");
            return null;
        } catch (Exception e) {
            System.out.println(" Erreur de parsing: " + e.getMessage());
            return null;
        }
    }

    private static void envoyerOperation(Operation operation) {
        try (Socket socket = new Socket(SERVEUR_HOST, SERVEUR_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            System.out.println(" Connexion objet établie avec le serveur");
            
            // Envoi de l'objet Operation
            out.writeObject(operation);
            out.flush();
            System.out.println(" Objet Operation envoyé: " + operation);

            // Réception de l'objet Resultat
            Resultat resultat = (Resultat) in.readObject();
            
            // Affichage formaté du résultat
            if (resultat.isSucces()) {
                System.out.println(" " + resultat);
            } else {
                System.out.println(" " + resultat.getMessage());
            }
            System.out.println("----------------------------------------");

        } catch (UnknownHostException e) {
            System.err.println(" Serveur introuvable: " + e.getMessage());
        } catch (ConnectException e) {
            System.err.println(" Impossible de se connecter au serveur objet. Vérifiez:");
            System.err.println("   - Le serveur objet est-il démarré?");
            System.err.println("   - Le port " + SERVEUR_PORT + " est-il correct?");
        } catch (IOException e) {
            System.err.println(" Erreur de communication objet: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println(" Type d'objet reçu non reconnu");
        }
    }
}