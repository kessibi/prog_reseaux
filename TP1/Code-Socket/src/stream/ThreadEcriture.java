package stream;

import java.io.*;
import java.net.*;

public class ThreadEcriture extends Thread {
  BufferedReader socIn;
  BufferedReader stdIn;
  PrintStream socOut;

  public ThreadEcriture (BufferedReader socIn, BufferedReader stdIn, PrintStream socOut) {
    this.socIn = socIn;
    this.stdIn = stdIn;
    this.socOut = socOut;
  }
    public void run(){
      while (true) {
            try {
                String line = socIn.readLine();
                System.out.println(line);
            } catch (IOException e) {
            }
      }
    }
}
