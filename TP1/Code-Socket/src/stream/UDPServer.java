
package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


public class UDPServer {


   public static void main(String args[]) {
     int argInt = Integer.parseInt(args[0]);
     int argLength = args.length;
     EchoServerMultiThreaded serveur = new UDPServer(argInt, argLength);

   }

   public UDPServer (int argInt, int argLength) {
	   history = new History();
	   // history.printAllMessages();
	 int threadId = 0;
     if (argLength != 1) {
       System.out.println("Usage: java EchoServer <EchoServer port>");
       System.exit(1);
     }
     try {

       }
     } catch (Exception e) {
       System.err.println("Error in EchoServer:" + e);
     }

   }

  

}
