package tp3_2_dar;
import java.io.*;
import java.net.*;

public class GestionClient extends Thread {
    private Socket client;
    
    public GestionClient(Socket client) {
        this.client = client;
    }
    
    public void run() {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        ) {
            out.println("Bienvenue! Format: 5 + 3 (tapez 'quit' pour quitter)");
            
            String ligne;
            while ((ligne = in.readLine()) != null) {
                if (ligne.equalsIgnoreCase("quit")) break;
                
                try {
                    String[] parties = ligne.split(" ");
                    double nb1 = Double.parseDouble(parties[0]);
                    String op = parties[1];
                    double nb2 = Double.parseDouble(parties[2]);
                    
                    double resultat = calculer(nb1, nb2, op);
                    Serveur.incrementerCompteur();
                    
                    out.println("Résultat: " + resultat);
                    
                } catch (Exception e) {
                    out.println("Erreur: mauvais format");
                }
            }
            
            client.close();
            System.out.println("Client déconnecté");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private double calculer(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": 
                if (b == 0) throw new ArithmeticException("Division par zéro");
                return a / b;
            default: throw new RuntimeException("Opérateur invalide: " + op);
        }
    }
}