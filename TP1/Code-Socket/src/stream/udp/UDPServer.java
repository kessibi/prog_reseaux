package stream.udp;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * <p>This class represent an UDP Server</p>
 * <p> His activities are :</p>
 * <ul>
 * <li>Receive a packet</li>
 * <li>Send this packet to the sender</li>
 * </ul>
 * @version 1.2
 *
 */

public class UDPServer {
	
  public static void main(String args[]) {
    int argInt = Integer.parseInt(args[0]);
    int argLength = args.length;
    UDPServer serveur = new UDPServer(argInt, argLength);
  }

  /**
   * 
   * This is the constructor of UDPServer
   * 
   * @param port
   * 		The server port
   * @param argLength
   * 		The number of args given in the command line
   * 
   */
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
