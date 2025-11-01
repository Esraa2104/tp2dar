package tp4;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientUDP {
 private static final String SERVEUR_HOST = "localhost";
 private static final int SERVEUR_PORT = 1234;
 private static String nomUtilisateur;
 private static DatagramSocket socket;
 private static InetAddress adresseServeur;
 
 public static void main(String[] args) {
     try {
         socket = new DatagramSocket();
         adresseServeur = InetAddress.getByName(SERVEUR_HOST);
         Scanner scanner = new Scanner(System.in);
         
         // Saisie du nom d'utilisateur
         System.out.print("Entrez votre nom d'utilisateur: ");
         nomUtilisateur = scanner.nextLine();
         
         System.out.println("Connecté au serveur de chat. Tapez vos messages:");
         
         // Thread pour la réception des messages
         Thread threadReception = new Thread(new ReceptionMessages());
         threadReception.start();
         
         // Boucle d'envoi des messages
         while (true) {
             String messageTexte = scanner.nextLine();
             
             if ("quit".equalsIgnoreCase(messageTexte)) {
                 break;
             }
             
             envoyerMessage(messageTexte);
         }
         
         scanner.close();
         socket.close();
         System.out.println("Déconnexion.");
         
     } catch (IOException e) {
         e.printStackTrace();
     }
 }
 
 private static void envoyerMessage(String message) {
     try {
         String messageComplet = "[" + nomUtilisateur + "] : " + message;
         byte[] data = messageComplet.getBytes();
         DatagramPacket packet = new DatagramPacket(data, data.length, adresseServeur, SERVEUR_PORT);
         socket.send(packet);
     } catch (IOException e) {
         System.out.println("Erreur lors de l'envoi du message");
     }
 }
 
 // Classe interne pour la réception des messages
 static class ReceptionMessages implements Runnable {
     @Override
     public void run() {
         try {
             while (!socket.isClosed()) {
                 byte[] buffer = new byte[1024];
                 DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                 socket.receive(packet);
                 
                 String message = new String(packet.getData(), 0, packet.getLength());
                 System.out.println("\n" + message + "\n> ");
             }
         } catch (IOException e) {
             if (!socket.isClosed()) {
                 System.out.println("Erreur de réception: " + e.getMessage());
             }
         }
     }
 }
}