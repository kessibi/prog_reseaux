/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

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
  public File chat;
  private String filename;

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
    clientsOn = new HashMap<UUID, ClientThread>();
    
    // create a new file to save the chat
    try {
    	LocalDateTime time = LocalDateTime.now();
    	filename = "../chats/chat_"+time.toString()+".txt";
		chat = new File(filename);
		if (chat.createNewFile()) {
		  System.out.println("File created: " + chat.getName());
		} else {
		  System.out.println("File already exists.");
	    }
	} catch (IOException e) {
	    System.out.println("An error occurred.");
	    e.printStackTrace();
	}

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

  public void sendToAll(String line) {
    history.addMessage(line);
    try {
        FileWriter chatWriter = new FileWriter(filename, true);
        chatWriter.append(line);
        chatWriter.append(System.getProperty("line.separator"));
        chatWriter.close();
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    clientsOn.forEach((uuid, client) -> client.sendToClient(line));
  }

  public void sendToAllExcept(String line, UUID id) {
    history.addMessage(line);
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
