/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class EchoServerMultiThreaded {
  /**
   * main method
   * @param EchoServer port
   *
   **/
   private List <ClientThread> listCt = new ArrayList<ClientThread>();
   ServerSocket listenSocket;

   public static void main(String args[]) {
     int argInt = Integer.parseInt(args[0]);
     int argLength = args.length;
     EchoServerMultiThreaded serveur = new EchoServerMultiThreaded(argInt, argLength);

   }

   public EchoServerMultiThreaded (int argInt, int argLength) {
     if (argLength != 1) {
       System.out.println("Usage: java EchoServer <EchoServer port>");
       System.exit(1);
     }
     try {
       listenSocket = new ServerSocket(argInt); // port
       System.out.println("Server ready...");
       while (true) {
         Socket clientSocket = listenSocket.accept();
         System.out.println("Connexion from:" + clientSocket.getInetAddress());
         ClientThread ct = new ClientThread(clientSocket, this);
         System.out.println("New thread in the list");
         listCt.add(ct);
         ct.start();
       }
     } catch (Exception e) {
       System.err.println("Error in EchoServer:" + e);
     }

   }

   public void envoyerMessageATous (String line) {
     for (int i = 0; i<listCt.size(); i++){
       System.out.println("message envoye");
       listCt.get(i).envoyer(line);
     }
   }

   public void closeThread (ClientThread client) {
     client.interrupt();
     for (int i = 0; i<listCt.size(); i++){
       if (client == listCt.get(i)){
         System.out.println("thread stop");
         listCt.remove(i);
       }
     }
   }

}
