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
   private History history;

   public static void main(String args[]) {
     int argInt = Integer.parseInt(args[0]);
     int argLength = args.length;
     EchoServerMultiThreaded serveur = new EchoServerMultiThreaded(argInt, argLength);

   }

   public EchoServerMultiThreaded (int argInt, int argLength) {
	   history = new History();
	   // history.printAllMessages();
	 int threadId = 0;
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
         threadId += 1;
         ClientThread ct = new ClientThread(clientSocket, this, threadId, history);
         System.out.println("New thread in the list "+ ct.getId());
         listCt.add(ct);
         ct.start();
       }
     } catch (Exception e) {
       System.err.println("Error in EchoServer:" + e);
     }

   }

   public void envoyerMessageATous (String line) {
	   if (line != null) {
		   history.addMessage("new message: "+line);
	   }
	   for (int i = 0; i<listCt.size(); i++){
		   System.out.println("message envoye");
		   listCt.get(i).envoyer(line);
	   }
   }

}
