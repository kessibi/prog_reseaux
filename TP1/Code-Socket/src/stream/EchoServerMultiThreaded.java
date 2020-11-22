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
  private List<ClientThread> listCt = new ArrayList<ClientThread>();
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
    // history.printAllMessages();
    int threadId = 0;

    try {
      listenSocket = new ServerSocket(port);
      System.out.println("Server listening on port " + port);
      while (true) {
        Socket clientSocket = listenSocket.accept();
        System.out.println("Connexion from:" + clientSocket.getInetAddress());
        threadId += 1;
        ClientThread ct = new ClientThread(clientSocket, this, threadId, history);
        System.out.println("New thread in the list " + ct.getId());
        listCt.add(ct);
        ct.start();
      }
    } catch (Exception e) {
      System.err.println("Error in EchoServer:" + e);
    }
  }

  public void envoyerMessageATous(String line) {
    if (line != null) {
      history.addMessage("new message: " + line);
    }
    for (int i = 0; i < listCt.size(); i++) {
      System.out.println("message envoye");
      listCt.get(i).envoyer(line);
    }
  }

  public void closeThread(ClientThread client) {
    client.interrupt();
    for (int i = 0; i < listCt.size(); i++) {
      if (client == listCt.get(i)) {
        System.out.println("thread stop");
        listCt.remove(i);
      }
    }
  }
}
