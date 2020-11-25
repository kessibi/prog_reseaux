package stream.udp;

import java.io.*;
import java.util.UUID;

/**
 * The message object used to transfer data on our multicast chat.
 */
public class Message implements Serializable {
  private UUID uuid;
  private String name;
  private String message;

  public Message(UUID id, String nam, String mess) {
    this.uuid = id;
    this.name = nam;
    this.message = mess;
  }

  public String getMessage() {
    return this.message;
  }

  /**
   * returns the complete message with the user's UUID, name and message
   */
  public String toString() {
    return "<" + this.uuid.toString().substring(0, 7) + ":" + this.name + "> " + this.message;
  }

  /**
   * Converts a message object to a byte array (here used for datagram packets).
   * Code transformed from: https://stackoverflow.com/a/30968827
   *
   * @return the byte array representing the object
   *
   * @throws IOException in case the stream encounters issues writing the object
   */
  public byte[] convertToBytes() throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
         ObjectOutput out = new ObjectOutputStream(bos)) {
      out.writeObject(this);
      return bos.toByteArray();
    }
  }

  /**
   * Converts a byte array back to a Message. Code inspired from
   * https://stackoverflow.com/a/30968827
   *
   * @param bytes the byte array to transform back to a message
   *
   * @throws IOException in case the stream encounters issues reading from the
   *                     array
   * @throws ClassNotFoundException in case the object doesn't want to be casted
   *                                as a Message
   *
   * @return The built-from-bytes message
   */
  public static Message convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
         ObjectInput in = new ObjectInputStream(bis)) {
      return (Message) in.readObject();
    }
  }
}
