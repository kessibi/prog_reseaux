package stream.tcp;

import java.io.*;
import java.net.*;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * This class makes the sending of messages possible in the case of using the HMI (human machine interface).
 * It extends the <code>java.lang.Thread</code> class. 
 * @author luise
 *
 */
public class ThreadEcritureIHM extends Thread {
  BufferedReader socIn;
  JTextArea grandeZone;
  JScrollPane scrollPane;

  /**
   * Constructor: assign the attributes. 
   * @param socIn The input socket.
   * @param grandeZone The text field of the HMI.
   * @param scrollPane The scroll pane of the HMI.
   */
  public ThreadEcritureIHM(BufferedReader socIn, JTextArea grandeZone, JScrollPane scrollPane) {
    this.socIn = socIn;
    this.grandeZone = grandeZone;
    this.scrollPane = scrollPane;
  }

  /**
   * Method which is executed after the thread is started. Incoming messages are printed on the text field.
   */
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
