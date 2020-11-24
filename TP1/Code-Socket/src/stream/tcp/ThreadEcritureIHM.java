package stream.tcp;

import java.io.*;
import java.net.*;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ThreadEcritureIHM extends Thread {
  BufferedReader socIn;
  JTextArea grandeZone;
  JScrollPane scrollPane;

  public ThreadEcritureIHM(BufferedReader socIn, JTextArea grandeZone, JScrollPane scrollPane) {
    this.socIn = socIn;
    this.grandeZone = grandeZone;
    this.scrollPane = scrollPane;
  }

  public void run() {
    String message;
    try {
      while ((message = socIn.readLine()) != null) {
        grandeZone.append(message + "\n");
        scrollPane.setViewportView(grandeZone);
        JScrollBar barre;
        barre=scrollPane.getVerticalScrollBar();
        barre.setValue(barre.getMaximum()+30);
        scrollPane.updateUI();
      }
      System.err.println("connection is out");
      System.exit(1);
    } catch (IOException e) {
      System.err.println("connection is out");
      System.exit(1);
    }
  }
}
