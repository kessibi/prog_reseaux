package stream.tcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class History {
  private List<String> messages;
  private String filename;
  private LocalDate date;
  public File chat;

  public History(String fn) {
    this.messages = new ArrayList<String>();
    this.filename = fn;
    this.date = LocalDate.now();

    BufferedReader reader;

    // create a new file to save the chat
    try {
      chat = new File(filename);
      if (chat.createNewFile()) {
        System.out.println("File created: " + chat.getName());
      } else {
        System.out.println("Load chat history.");

        reader = new BufferedReader(new FileReader(filename));
        String line = reader.readLine();
        while (line != null) {
          messages.add(line);
          line = reader.readLine();
        }
        reader.close();
      }
    } catch (IOException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
  }

  private void checkAndWriteDate() {
    LocalDate newDate = LocalDate.now();
    if (date.isBefore(newDate)) {
      date = newDate;
      try {
        FileWriter chatWriter = new FileWriter(filename, true);
        chatWriter.append(newDate.getDayOfWeek() + " " + newDate.getMonth().toString() + " "
            + String.valueOf(newDate.getDayOfMonth()));
        chatWriter.append(System.getProperty("line.separator"));
        chatWriter.close();
      } catch (IOException e) {
        System.out.println("An error occurred while writing.");
        e.printStackTrace();
      }
    }
  }

  public void writeToFile(String message) {
    try {
      FileWriter chatWriter = new FileWriter(filename, true);
      chatWriter.append(message);
      chatWriter.append(System.getProperty("line.separator"));
      chatWriter.close();
    } catch (IOException e) {
      System.out.println("An error occurred while writing.");
      e.printStackTrace();
    }
  }

  public void addMessage(String message) {
    messages.add(message);
    writeToFile(message);
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
    String output = "======= History =======";
    if (messages.size() < 1) {
      output += " No messages so far. Start chatting!";
    }
    for (String message : messages) {
      output = output + "\n" + message;
    }
    output += "\n=== End of the history ===\n\n";
    output += "Don't hesitate to use:\n";
    output += "!list\n";
    output += "!msg <user> your msg\n";
    return output;
  }
}
