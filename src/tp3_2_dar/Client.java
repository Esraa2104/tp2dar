package tp3_2_dar;
import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
        
        // Lire message de bienvenue
        System.out.println(in.readLine());
        
        while (true) {
            System.out.print("Calcul > ");
            String input = scanner.nextLine();
            
            out.println(input);
            
            if (input.equalsIgnoreCase("quit")) break;
            
            String reponse = in.readLine();
            System.out.println(reponse);
        }
        
        socket.close();
        scanner.close();
    }
}