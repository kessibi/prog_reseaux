package stream;

import java.io.*;
import java.net.*;

public class ThreadLecture extends Thread {
  BufferedReader socIn;
  BufferedReader stdIn;
  PrintStream socOut;

  public ThreadLecture (BufferedReader socIn, BufferedReader stdIn, PrintStream socOut) {
    this.socIn = socIn;
    this.stdIn = stdIn;
    this.socOut = socOut;
  }

    public void run(){
      while (true) {
            try {
                String line;
                line = stdIn.readLine();
                if (line.equals("."))
                  break;
                socOut.println(line);
            } catch (IOException e) {
            }
        }
    }
}
