package stream;

import java.io.*;
import java.net.*;
import javax.swing.JTextArea;

public class ThreadEcritureIHM extends Thread {
  BufferedReader socIn;
  JTextArea grandeZone;

  public ThreadEcritureIHM(BufferedReader socIn, JTextArea grandeZone) {
    this.socIn = socIn;
    this.grandeZone = grandeZone;
  }

  public void run() {
    String message;
    try {
      while ((message = socIn.readLine()) != null) {
        grandeZone.append(message + "\n");
      }
      System.err.println("connection is out");
      System.exit(1);
    } catch (IOException e) {
      System.err.println("connection is out");
      System.exit(1);
    }
  }
}
