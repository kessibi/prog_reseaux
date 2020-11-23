
package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class UDPServer {
  public static void main(String args[]) {
    int argInt = Integer.parseInt(args[0]);
    int argLength = args.length;
    UDPServer serveur = new UDPServer(argInt, argLength);
  }

  public UDPServer(int port, int argLength) {
    int threadId = 0;
    if (argLength != 1) {
      System.out.println("Usage: java EchoServer <EchoServer port>");
      System.exit(1);
    }
    try {
      DatagramSocket serverSock = new DatagramSocket(port);
      while (true) {
        System.out.println("Waiting for client packet...");
        byte[] buf = new byte[256];
        // Create a datagram packet
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        // Wait for a packet
        serverSock.receive(packet);
        // Get client IP address and port number
        InetAddress clientAddr = packet.getAddress();
        int clientPort = packet.getPort();
        // Build a response
        // initialize buf ...
        // Build a datagram packet for response
        packet = new DatagramPacket(buf, buf.length, clientAddr, clientPort);
        // Send a response
        serverSock.send(packet);
      }
    } catch (Exception e) {
      System.err.println("Error in EchoServer:" + e);
    }
  }
}
