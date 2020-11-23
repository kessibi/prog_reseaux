package stream.tcp;

import java.io.*;
import java.net.*;

public class ThreadLecture extends Thread {
  BufferedReader socIn;
  BufferedReader stdIn;
  PrintStream socOut;
  EchoClient client;

  public ThreadLecture(
      BufferedReader socIn, BufferedReader stdIn, PrintStream socOut, EchoClient client) {
    this.socIn = socIn;
    this.stdIn = stdIn;
    this.socOut = socOut;
    this.client = client;
  }

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

  public void quitServer() {
    socOut.println("quit");
    client.closeConnection();
    System.exit(0);
  }
}
