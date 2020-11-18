package stream;

import java.io.*;
import java.net.*;

public class ThreadLecture extends Thread {
  BufferedReader socIn;
  BufferedReader stdIn;
  PrintStream socOut;
  EchoClient client;

  public ThreadLecture (BufferedReader socIn, BufferedReader stdIn, PrintStream socOut, EchoClient client) {
    this.socIn = socIn;
    this.stdIn = stdIn;
    this.socOut = socOut;
    this.client = client;
  }

    public void run(){
      while (true) {
            try {
                String line;
                line = stdIn.readLine();
                if (line.equals("."))
                  client.fermerconnexion();
                socOut.println(line);
            } catch (IOException e) {
            }
        }
    }
}
