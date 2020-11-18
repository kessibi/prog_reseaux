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

  ClientThread(Socket s, EchoServerMultiThreaded serveur) {
    this.clientSocket = s;
    this.serveurMulti = serveur;
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
      while (true) {
        String line = socIn.readLine();
        serveurMulti.envoyerMessageATous(line);
        System.out.println("SERVER: " + line);
      }
    } catch (Exception e) {
      System.err.println("Error in EchoServer:" + e);
    }
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
