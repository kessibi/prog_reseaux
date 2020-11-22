/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.UUID;

public class EchoServerMultiThreaded {
  /**
   * main method
   * @param EchoServer port
   *
   **/
  private HashMap<UUID, ClientThread> clientsOn = new HashMap<UUID, ClientThread>();
  ServerSocket listenSocket;
  private History history;

  public static void main(String args[]) {
    if (args.length != 1) {
      System.err.println("usage: java EchoServerMultiThreaded <port>");
      System.exit(1);
    }

    int port = Integer.parseInt(args[0]);
    EchoServerMultiThreaded serveur = new EchoServerMultiThreaded(port);
  }

  public EchoServerMultiThreaded(int port) {
    history = new History();

    try {
      listenSocket = new ServerSocket(port);
      System.out.println("Server listening on port " + port);
      while (true) {
        Socket clientSocket = listenSocket.accept();
        System.out.println("Connexion from:" + clientSocket.getInetAddress());
        UUID uuid = UUID.randomUUID();
        ClientThread ct = new ClientThread(clientSocket, uuid, this, history);
        clientsOn.put(uuid, ct);
        ct.start();
      }
    } catch (Exception e) {
      System.err.println("Error in EchoServer:" + e);
    }
  }

  public void envoyerMessageATous(String line) {
    history.addMessage("new message: " + line);
    clientsOn.forEach((uuid, client) -> { client.envoyer(line); });
  }

  public void closeThread(ClientThread client) {
    UUID clientUUID = client.getUUID();
    clientsOn.remove(clientUUID);
    client.interrupt();
  }
}
