package stream.udp;

import java.io.*;
import java.net.*;

public class UDPClient {
  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
      System.exit(1);
    }

    UDPClient client = new UDPClient(args[0], args[1]);
  }

  public UDPClient(String arg0, String arg1) {
    try {
      // Create a datagram socket
      DatagramSocket clientSock = new DatagramSocket();
      // byte[] buf = new byte[256];
      // Get server’s IP address
      InetAddress serverAddr = InetAddress.getByName(arg0);
      // Build a request
      BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
      String line = stdIn.readLine();
      byte[] buf = line.getBytes();
      // Create a datagram packet destined for the
      // server
      DatagramPacket packet =
          new DatagramPacket(buf, buf.length, serverAddr, Integer.valueOf(arg1).intValue());
      // Send datagram packet to server
      clientSock.send(packet);
      // Build a datagram packet for response
      packet = new DatagramPacket(buf, buf.length);
      // Receive response
      clientSock.receive(packet);
      String received = new String(packet.getData(), 0, packet.getLength());
      System.out.println("Response: " + received);

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
