package stream;

import java.io.*;
import java.net.*;

public class ThreadEcriture extends Thread {
  BufferedReader socIn;
  BufferedReader stdIn;
  PrintStream socOut;

  public ThreadEcriture(BufferedReader socIn, BufferedReader stdIn, PrintStream socOut) {
    this.socIn = socIn;
    this.stdIn = stdIn;
    this.socOut = socOut;
  }
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
