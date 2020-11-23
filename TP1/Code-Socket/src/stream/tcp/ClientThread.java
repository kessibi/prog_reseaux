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

public class ClientThread extends Thread {
  private Socket clientSocket;
  private EchoServerMultiThreaded server;
  private String name;
  private History history;
  private UUID uuid;

  ClientThread(Socket s, UUID id, EchoServerMultiThreaded server, History history) {
    this.clientSocket = s;
    this.uuid = id;
    this.server = server;
    this.history = history;
  }

  public UUID getUUID() {
    return this.uuid;
  }

  public String getUserName() {
    return name;
  }

  /**
   * receives a request from client then sends an echo to the client
   * @param clientSocket the client socket
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
        switch (message) {
          case "":
            // ignoring blank messages for now
            break;
          case "quit":
            server.closeThread(this);
            break;
          default:
            server.sendToAll(this.name + ": " + message);
        }
      }

      // the client has left without warning the server
      server.closeThread(this);

    } catch (Exception e) {
      System.err.println("Error in ClientThread:" + e);
    }
  }

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

  public void sendToClient(String line) {
    try {
      PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
      socOut.println(line);
    } catch (Exception e) {
      System.err.println("Error in EchoServer:" + e);
    }
  }
}
