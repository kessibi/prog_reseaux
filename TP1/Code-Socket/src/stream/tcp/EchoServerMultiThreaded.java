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
import java.util.Map;
import java.util.UUID;

/**
 * This class represents the server which makes communication between multiple clients possible
 * 
 *
 */
public class EchoServerMultiThreaded {
  private HashMap<UUID, ClientThread> clientsOn;
  ServerSocket listenSocket;
  private History history;

  /**
   * The main method initiates the server.
   * @param port The port.
   *
   **/
  public static void main(String args[]) {
    if (args.length != 1) {
      System.err.println("usage: java EchoServerMultiThreaded <port>");
      System.exit(1);
    }

    int port = Integer.parseInt(args[0]);
    EchoServerMultiThreaded serveur = new EchoServerMultiThreaded(port);
  }

  /**
   * The constructor takes care of several tasks: The server listens on a socket if a new client wants to start a connection.
   * All connected clients are saved in a <code>HashMap</code>. For every client a new thread is started.
   * @param port 
   */
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

  /**
   * This method finds out the date and time.
   * @return ret Current date and time represented as a <code>String</code>.
   */
  public String whatTimeItIs() {
    LocalDateTime time = LocalDateTime.now();
    String ret = time.toString().substring(0, 19);
    ret = ret.replace("T", " ");

    return ret;
  }

  /**
   * The method takes a message (or information in general) passed as a <code>String</code>, 
   * saves it the chat history and sends it to all connected clients.
   * @param String A message or information.
   */
  public void sendToAll(String line) {
    String message = "<" + whatTimeItIs() + "> " + line;
    history.addMessage(message);

    clientsOn.forEach((uuid, client) -> client.sendToClient(message));
  }

  /**
   * The method takes a message (or information in general) passed as a <code>String</code>, 
   * and sends it to a specific client if connected.
   * @param from user name of sender 
   * @param to user name of recipient
   * @param line the message to be transfered
   */
  public void sendPrivateMsg(String from, String to, String line) {
    boolean isOn = false;
    String message = "<" + whatTimeItIs() + "> " + line;

    for (Map.Entry<UUID, ClientThread> entry : clientsOn.entrySet()) {
      ClientThread client = entry.getValue();

      if (to.equals(client.getUserName())) {
        isOn = true;
      }
    }

    // if the recipient is not online
    if (!isOn) {
      return;
    }

    clientsOn.forEach((uuid, client) -> {
      if (from.equals(client.getUserName()) || to.equals(client.getUserName())) {
        client.sendToClient(message);
      }
    });
  }

  /**
   * Method to send a list of connected clients to a specific client
   * @param to unique user id of a client thread
   */
  public void sendList(UUID to) {
    String connectedUsers = "Connected users: ";

    for (Map.Entry<UUID, ClientThread> entry : clientsOn.entrySet()) {
      ClientThread client = entry.getValue();
      connectedUsers += client.getUserName() + "; ";
    }
    String message = "<" + whatTimeItIs() + "> " + connectedUsers;

    ClientThread client = clientsOn.get(to);
    client.sendToClient(message);
  }

  /**
   * The method takes a message (or information in general) passed as a <code>String</code>, 
   * saves it the chat history and sends it to all connected clients except a specific client given by his uuid.
   * @param line A message or information 
   * @param id The unique user id of a client thread
   */
  public void sendToAllExcept(String line, UUID id) {
    String message = "<" + whatTimeItIs() + "> " + line;
    history.addMessage(message);

    clientsOn.forEach((uuid, client) -> {
      if (uuid != id) {
        client.sendToClient(message);
      }
    });
  }

  /**
   * Method to close a given client thread.
   * @param client The client to be stopped.
   */
  public void closeThread(ClientThread client) {
    String name = client.getUserName();

    UUID clientUUID = client.getUUID();
    clientsOn.remove(clientUUID);
    client.interrupt();

    // warn others the client left
    sendToAll(name + " has left the chatroom.");
  }
}
