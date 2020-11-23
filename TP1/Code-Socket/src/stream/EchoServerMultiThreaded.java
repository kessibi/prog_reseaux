/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
  private LocalDate date;

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
    date = LocalDate.now();
    
    // create a new file to save the chat
    try {
    	LocalDateTime time = LocalDateTime.now();
    	filename = "../chats/chat_"+String.valueOf(port) +".txt";
		chat = new File(filename);
		if (chat.createNewFile()) {
		  System.out.println("File created: " + chat.getName());
		  try {
		        FileWriter chatWriter = new FileWriter(filename, true);
		        chatWriter.append(time.getDayOfWeek()+ " " + time.getMonth().toString() + " " + String.valueOf(time.getDayOfMonth()));
		        chatWriter.append(System.getProperty("line.separator"));
		        chatWriter.close();
		    } catch (IOException e) {
		        System.out.println("An error occurred while writing.");
		        e.printStackTrace();
		    }
		} else {
		  System.out.println("Load chat history.");
		  try {
		        FileWriter chatWriter = new FileWriter(filename, true);
		        chatWriter.append(time.getDayOfWeek()+ " " + time.getMonth().toString() + " " + String.valueOf(time.getDayOfMonth()));
		        chatWriter.append(System.getProperty("line.separator"));
		        chatWriter.close();
		    } catch (IOException e) {
		        System.out.println("An error occurred while writing.");
		        e.printStackTrace();
		    }
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
  
  private void checkAndWriteDate() {
	  LocalDate newDate = LocalDate.now();
	  if (date.isBefore(newDate)) {
		  date = newDate;
		  try {
		    FileWriter chatWriter = new FileWriter(filename, true);
	        chatWriter.append(newDate.getDayOfWeek()+ " " + newDate.getMonth().toString() + " " + String.valueOf(newDate.getDayOfMonth()));
		    chatWriter.append(System.getProperty("line.separator"));
		    chatWriter.close();
		  } catch (IOException e) {
			System.out.println("An error occurred while writing.");
			e.printStackTrace();
		  }
	  }
  }

  public void sendToAll(String line) {
    history.addMessage(line);
    checkAndWriteDate();
    try {
        FileWriter chatWriter = new FileWriter(filename, true);
        LocalTime time = LocalTime.now();
        chatWriter.append(time.getHour() + ":" + time.getMinute() + " - " + line);
        chatWriter.append(System.getProperty("line.separator"));
        chatWriter.close();
    } catch (IOException e) {
        System.out.println("An error occurred while writing.");
        e.printStackTrace();
    }
    clientsOn.forEach((uuid, client) -> client.sendToClient(line));
  }

  public void sendToAllExcept(String line, UUID id) {
    history.addMessage(line);
    checkAndWriteDate();
    try {
        FileWriter chatWriter = new FileWriter(filename, true);
        LocalTime time = LocalTime.now();
        chatWriter.append(time.getHour() + ":" + time.getMinute() + " - " + line);
        chatWriter.append(System.getProperty("line.separator"));
        chatWriter.close();
    } catch (IOException e) {
        System.out.println("An error occurred while writing.");
        e.printStackTrace();
    }
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
