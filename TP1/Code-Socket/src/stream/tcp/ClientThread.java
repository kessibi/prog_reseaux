/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream.tcp;

import java.io.*;
import java.net.*;
import java.util.UUID;

/**
 * This class extends the Java.lang.Thread class. It represents a client thread.
 * 
 */
public class ClientThread extends Thread {
  private Socket clientSocket;
  private EchoServerMultiThreaded server;
  private String name;
  private History history;
  private UUID uuid;

  /**
   * Constructor method which assigns the attributes of the class.
   * @param s A socket.
   * @param id The unique user id of the client.
   * @param server The server the client is connected to.
   * @param history The history of the chat conversation.
   */
  ClientThread(Socket s, UUID id, EchoServerMultiThreaded server, History history) {
    this.clientSocket = s;
    this.uuid = id;
    this.server = server;
    this.history = history;
  }

  /**
   * This method is used to retrieve the uuid of the client thread.
   * @return uuid The unique user id of the thread.
   */
  public UUID getUUID() {
    return this.uuid;
  }

  /**
   * The method returns the user name of the client.
   * @return name The client's user name.
   */
  public String getUserName() {
    return name;
  }

  /**
   * Receives a request from client then sends an echo to the client.
   * @param clientSocket The client socket.
   **/
  public void run() {
    try {
      String message;
      InputStream is = clientSocket.getInputStream();
      BufferedReader socIn = new BufferedReader(new InputStreamReader(is));
      PrintStream socOut = new PrintStream(clientSocket.getOutputStream());

      setUserName();
      sendToClient(history.toString());

      while ((message = socIn.readLine()) != null) {
        if (message.startsWith("!msg")) {
          String[] words = message.split(" ");
          String user = words[1];
          String msg = message.substring(6 + user.length(), message.length());

          String privateMsg = this.name + " -> " + user + ": " + msg;
          server.sendPrivateMsg(this.name, user, privateMsg);

        } else {
          switch (message) {
            case "":
            case " ":
            case "\n":
              // ignoring blank messages for now
              break;
            case "!list":
              server.sendList(this.uuid);
              break;
            default:
              server.sendToAll(this.name + ": " + message);
          }
        }
      }

      // the client has left without warning the server
      server.closeThread(this);

    } catch (Exception e) {
      System.err.println("Error in ClientThread:" + e);
    }
  }

  /**
   * Method used to set a user name after joining a conversation. All clients are notified that
   * someone new joined the chat room.
   */
  public void setUserName() {
    try {
      InputStream is = clientSocket.getInputStream();
      BufferedReader socIn = new BufferedReader(new InputStreamReader(is));
      String name = "";

      while (name.equals("")) {
        if (name == "quit") {
          server.closeThread(this);
        }
        sendToClient("Choose a name: ");
        name = socIn.readLine();
      }
      this.name = name;

      server.sendToAllExcept(name + " has joined the chatroom.", uuid);
    } catch (Exception e) {
      System.err.println("Error in EchoServer:" + e);
    }
  }

  /**
   * Method used to send a message (or information) to the server.
   * @param line The message to be sent.
   */
  public void sendToClient(String line) {
    try {
      PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
      socOut.println(line);
    } catch (Exception e) {
      System.err.println("Error in EchoServer:" + e);
    }
  }
}
