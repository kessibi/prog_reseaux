package stream.tcp;

import java.util.ArrayList;
import java.util.List;

public class History {
  private List<String> messages;

  public History() {
    this.messages = new ArrayList<String>();
  }

  public void addMessage(String message) {
    messages.add(message);
  }

  public List<String> getMessages() {
    return messages;
  }

  public void printAllMessages() {
    System.out.println("Print all messages here");
    for (String message : messages) {
      System.out.println(message);
    }
  }

  @Override
  public String toString() {
    String output = "History:";
    if (messages.size() < 1) {
      output += " No messages so far. Start chatting!";
    }
    for (String message : messages) {
      output = output + "\n" + message;
    }
    return output;
  }
}
