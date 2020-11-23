package stream;

import java.io.*;
import java.util.UUID;

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

  public String toString() {
    return "<" + this.uuid.toString().substring(0, 7) + ":" + this.name + "> " + this.message;
  }

  public byte[] convertToBytes() throws IOException {
    try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
         ObjectOutput out = new ObjectOutputStream(bos)) {
      out.writeObject(this);
      return bos.toByteArray();
    }
  }

  public static Message convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
         ObjectInput in = new ObjectInputStream(bis)) {
      return (Message) in.readObject();
    }
  }
}
