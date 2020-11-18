
package stream;

import java.io.*;
import java.net.*;

public class UDPClient {



   public static void main(String[] args) throws IOException {

    if (args.length != 2) {
      System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
      System.exit(1);
    }

    EchoClient client = new EchoClient (args[0], args[1]);
  }

  public UDPClient (String arg0, String arg1) {

    try {


    } catch (UnknownHostException e) {
      System.err.println("Don't know about host:" + arg0);
      System.exit(1);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for "
          + "the connection to:" + arg0);
      System.exit(1);
    }



  }


}
