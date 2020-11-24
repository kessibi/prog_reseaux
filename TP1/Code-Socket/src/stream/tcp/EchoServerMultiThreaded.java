/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream.tcp;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

public class EchoServerMultiThreaded {
  /**
   * main method
   * @param EchoServer port
   *
   **/
  private HashMap<UUID, ClientThread> clientsOn;
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
    String filename = "/tmp/chat_" + String.valueOf(port) + ".txt";
    history = new History(filename);
    clientsOn = new HashMap<UUID, ClientThread>();

    try {
      listenSocket = new ServerSocket(port);
      System.out.println("Server listening on port " + port);

      while (true) {
        Socket clientSocket = listenSocket.accept();
        System.out.println("Connection from: " + clientSocket.getInetAddress());

        UUID uuid = UUID.randomUUID();
        ClientThread ct = new ClientThread(clientSocket, uuid, this, history);
        clientsOn.put(uuid, ct);
        ct.start();
      }
    } catch (Exception e) {
      System.err.println("Error in EchoServer:" + e);
    }
  }

  public String whatTimeItIs() {
    LocalDateTime time = LocalDateTime.now();
    String ret = time.toString().substring(0, 19);
    ret = ret.replace("T", " ");

    return ret;
  }

  public void sendToAll(String line) {
    String message = "<" + whatTimeItIs() + "> " + line;
    history.addMessage(message);

    clientsOn.forEach((uuid, client) -> client.sendToClient(message));
  }

  public void sendToAllExcept(String line, UUID id) {
    String message = "<" + whatTimeItIs() + "> " + line;
    history.addMessage(message);

    clientsOn.forEach((uuid, client) -> {
      if (uuid != id) {
        client.sendToClient(line);
      }
    });
  }

  public void closeThread(ClientThread client) {
    String name = client.getUserName();

    UUID clientUUID = client.getUUID();
    clientsOn.remove(clientUUID);
    client.interrupt();

    // warn others the client left
    sendToAll(name + " has left the chatroom.");
  }
}
