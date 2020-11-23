
// utiliser 224.0.0.1

package stream;

import java.io.*;
import java.net.*;
import java.util.UUID;

public class UDPMulticast {
  public static void main(String[] args) throws IOException {
    if (args.length != 2) {
      System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
      System.exit(1);
    }

    UDPMulticast client = new UDPMulticast(args[0], Integer.valueOf(args[1]).intValue());
  }

  public UDPMulticast(String addr, int groupPort) {
    try {
      // Create a multicast socket
      InetAddress groupAddr = InetAddress.getByName(addr);
      MulticastSocket s = new MulticastSocket(groupPort);
      // Join the group
      s.joinGroup(groupAddr);
      // Build a datagram packet for a message
      // to send to the group

      Thread writer = new Thread() {
        public void run() {
          String message;
          BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
          try {
            while ((message = stdIn.readLine()) != null) {
              DatagramPacket msg =
                  new DatagramPacket(message.getBytes(), message.length(), groupAddr, groupPort);
              // Send a multicast message to the group
              s.send(msg);
            }
          } catch (IOException e) {
          }
        }
      };

      writer.start();

      // Build a datagram packet for response
      byte[] buf = new byte[1000];
      DatagramPacket recv = new DatagramPacket(buf, buf.length);
      // Receive a datagram packet response

      while (true) {
        s.receive(recv);
        String received = new String(recv.getData(), 0, recv.getLength());
        System.out.println("Response :" + received);
      }

      // OK, I'm done talking - leave the group
      // s.leaveGroup(groupAddr);

    } catch (UnknownHostException e) {
      System.err.println(e);
      System.err.println("Don't know about host:" + addr);
      System.exit(1);
    } catch (IOException e) {
      System.err.println(e);
      System.err.println("Couldn't get I/O for "
          + "the connection to:" + groupPort);
      System.exit(1);
    }
  }

  public class MultiMessage {
    private UUID uuid;
    private String name;
    private String message;

    public MultiMessage(UUID id, String nam, String mess) {
      this.uuid = id;
      this.name = nam;
      this.message = mess;
    }
  }
}
