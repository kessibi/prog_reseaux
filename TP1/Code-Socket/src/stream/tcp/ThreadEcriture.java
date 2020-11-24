package stream.tcp;

import java.io.*;
import java.net.*;

/**
 * This class represents a thread used by a client to send a message. 
 *
 */
public class ThreadEcriture extends Thread {
  BufferedReader socIn;
  BufferedReader stdIn;
  PrintStream socOut;

  /**
   * Constructor method which assigns the input and output sockets.
   * @param BufferedReader socIn input socket
   * @param BufferedReader stdIn another input socket for standard input.
   * @param PrintStream socOut output socket
   */
  public ThreadEcriture(BufferedReader socIn, BufferedReader stdIn, PrintStream socOut) {
    this.socIn = socIn;
    this.stdIn = stdIn;
    this.socOut = socOut;
  }
  
  /**
   * Method performed when the thread is started. 
   */
  public void run() {
    String message;
    try {
      while ((message = socIn.readLine()) != null) {
        System.out.println(message);
      }
      System.err.println("connection is out");
      System.exit(1);
    } catch (IOException e) {
      System.err.println("connection is out");
      System.exit(1);
    }
  }
}
