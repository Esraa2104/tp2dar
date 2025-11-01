package tp4;

import java.net.*;
import java.io.*;
import java.util.*;

public class ServeurUDPAvance {
 private static final int PORT = 1234;
 private static List<InetSocketAddress> clients = new ArrayList<>();
 
 public static void main(String[] args) {
     try {
         DatagramSocket socket = new DatagramSocket(PORT);
         System.out.println("Serveur UDP avancé démarré sur le port " + PORT);
         
         while (true) {
             byte[] buffer = new byte[1024];
             DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
             
             // Réception du message
             socket.receive(packet);
             
             // Création de l'adresse du client
             InetSocketAddress adresseClient = new InetSocketAddress(packet.getAddress(), packet.getPort());
             
             // Ajout du client s'il n'existe pas
             if (!clients.contains(adresseClient)) {
                 clients.add(adresseClient);
                 System.out.println("Nouveau client connecté: " + adresseClient);
             }
             
             String message = new String(packet.getData(), 0, packet.getLength());
             System.out.println("Message reçu de " + adresseClient + " : " + message);
             
             // Diffusion du message à tous les autres clients
             diffuserMessage(socket, message, adresseClient);
         }
     } catch (IOException e) {
         e.printStackTrace();
     }
 }
 
 private static void diffuserMessage(DatagramSocket socket, String message, InetSocketAddress expediteur) {
     for (InetSocketAddress client : clients) {
         if (!client.equals(expediteur)) {
             try {
                 byte[] data = message.getBytes();
                 DatagramPacket packet = new DatagramPacket(data, data.length, client.getAddress(), client.getPort());
                 socket.send(packet);
             } catch (IOException e) {
                 System.out.println("Erreur lors de l'envoi au client " + client);
             }
         }
     }
 }
}