/***
 * EchoClient
 * Example of a TCP client
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;

public class EchoClient {

  Socket echoSocket = null;
  PrintStream socOut = null;
  BufferedReader stdIn = null;
  BufferedReader socIn = null;

   public static void main(String[] args) throws IOException {

    if (args.length != 2) {
      System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
      System.exit(1);
    }

    EchoClient client = new EchoClient (args[0], args[1]);
  }

  public EchoClient (String arg0, String arg1) {

    try {
      // creation socket ==> connexion
      echoSocket = new Socket(arg0, Integer.valueOf(arg1).intValue());
      socIn = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
      socOut = new PrintStream(echoSocket.getOutputStream());
      stdIn = new BufferedReader(new InputStreamReader(System.in));
      
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host:" + arg0);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for "
          + "the connection to:" + arg0);
      System.exit(1);
    }

    ThreadLecture threadLecture = new ThreadLecture(socIn, stdIn, socOut, this);
    ThreadEcriture threadEcriture = new ThreadEcriture(socIn, stdIn, socOut);

    threadLecture.start();
    threadEcriture.start();

  }


  public void fermerconnexion() {

    try {
      System.out.println("fermeture de l'echo");
      socOut.close();
      socIn.close();
      stdIn.close();
      echoSocket.close();
    } catch (IOException e) {
      System.err.println(e);
      System.exit(1);
    }

  }


}
