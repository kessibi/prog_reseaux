package stream.udp;

// utiliser 224.0.0.1

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

  /**
   * The one and only method of the class, will take care of connecting to a
   * multicast IP address and send out/receive messages. Each client is
   * identified by a generated UUID, contained in each datagram along with
   * his name and message.
   *
   * @param addr The IP address to connect to
   * @param groupPort the port to connect to
   */
  public UDPMulticast(String addr, int groupPort) {
    try {
      // Create a multicast socket
      InetAddress groupAddr = InetAddress.getByName(addr);
      MulticastSocket s = new MulticastSocket(groupPort);

      // Join the group
      s.joinGroup(groupAddr);

      Thread writer = new Thread() {
        public void run() {
          BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

          try {
            String name = "";
            String message;
            UUID uuid = UUID.randomUUID();

            while (name.equals("")) {
              System.out.println("Choose a name: ");
              name = stdIn.readLine();
            }

            while ((message = stdIn.readLine()) != null) {
              Message mm = new Message(uuid, name, message);
              byte[] msg = mm.convertToBytes();
              DatagramPacket data = new DatagramPacket(msg, msg.length, groupAddr, groupPort);
              // Send a multicast message to the group
              s.send(data);
            }
          } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
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
        try {
          Message m = Message.convertFromBytes(recv.getData());
          System.out.println(m.toString());
        } catch (ClassNotFoundException cnfe) {
        }
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
}
