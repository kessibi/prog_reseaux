/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread {
  private Socket clientSocket;
  private EchoServerMultiThreaded serveurMulti;
  private String name;
  private int id;
  private History history;

  ClientThread(Socket s, EchoServerMultiThreaded serveur, int id, History history) {
    this.clientSocket = s;
    this.serveurMulti = serveur;
    this.id = id;
    this.history = history;
    // history.printAllMessages();
  }

  /**
   * receives a request from client then sends an echo to the client
   * @param clientSocket the client socket
   **/
  public void run() {
    try {
      BufferedReader socIn = null;
      socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
      // System.out.println(history.toString());
      setUserName();
	  //System.out.println(this.name);
      envoyer(history.toString());
      int ct = 0;
      while (ct < 50) {
        String line = socIn.readLine();
        if (line == null){
          serveurMulti.closeThread(this);
          break;
        }
        serveurMulti.envoyerMessageATous(this.name+": "+line);
        System.out.println("SERVER Thread: " + line);
      }
    } catch (Exception e) {
      System.err.println("Error in EchoServer:" + e);
    }
  }
  
  @Override
  public long getId() {
	  return this.id;
  }
  
  /*
  public void sendHistory() {
	    try {
	        PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
	        socOut.println(history.toString());
	    } catch (Exception e) {
	      System.err.println("Error in EchoServer:" + e);
	    }
	  }
	  */
  public void setUserName() {
	  envoyer("Choose a name: ");
	  try {
		  BufferedReader socIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		  String name = socIn.readLine();
		  this.name = name;
	  } catch (Exception e) {
	      System.err.println("Error in EchoServer:" + e);
	  }
  }
  
  public String getUserName() {
	  return name;
  }
  
  public void envoyer(String line) {
    try {
        PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
        socOut.println(line);
    } catch (Exception e) {
      System.err.println("Error in EchoServer:" + e);
    }
  }
}
