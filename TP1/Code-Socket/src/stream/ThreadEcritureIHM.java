package stream;

import java.io.*;
import java.net.*;

public class ThreadEcritureIHM extends Thread {
  BufferedReader socIn;

  public ThreadEcritureIHM(BufferedReader socIn) {
    this.socIn = socIn;
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
