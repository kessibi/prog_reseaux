package stream.tcp;

import java.io.*;
import java.net.*;

/**
 * This class represents a thread used by a client to receive messages. 
 *
 */
public class ThreadLecture extends Thread {
  BufferedReader socIn;
  BufferedReader stdIn;
  PrintStream socOut;
  EchoClient client;

  /**
   * Constructor method which assigns the input and output sockets.
   * @param socIn An input socket.
   * @param stdIn Another input socket for standard input.
   * @param socOut The output socket.
   * @param client The client.
   */
  public ThreadLecture(
      BufferedReader socIn, BufferedReader stdIn, PrintStream socOut, EchoClient client) {
    this.socIn = socIn;
    this.stdIn = stdIn;
    this.socOut = socOut;
    this.client = client;
  }

  /**
   * Method performed when the thread is started. 
   */
  public void run() {
    String message;

    while (true) {
      try {
        message = stdIn.readLine();

        switch (message) {
          case "quit":
            quitServer();
            break;
          default:
            socOut.println(message);
        }

      } catch (IOException e) {
      }
    }
  }

  /**
   * Method used to close the connection with the server.
   */
  public void quitServer() {
    socOut.println("quit");
    client.closeConnection();
    System.exit(0);
  }
}
