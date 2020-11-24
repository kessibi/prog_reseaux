package stream.tcp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The class History represents the history of a chat conversation.
 *
 */
public class History {
  private List<String> messages;
  private String filename;
  private LocalDate date;
  public File chat;

  /**
   * Constructor method which first either creates a new file given by its filename or, if it already exits,
   * opens the existing file. In the case that the file already exists, the saved messages are read from the file
   * and added to the history.
   * @param fn The filename of the history file.
   */
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

  /**
   * This method is used to check if a day has passed. If so, the new date is written in the history.
   */
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

  /**
   * The method <code>writeToFile</code> takes a message passed as a parameter and adds it to the file.
   * @param message The message to be saved.
   */
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

  /**
   * This method takes a message passed as a parameter and adds it to its List of messages. The message 
   * is then written to the file as well.
   * @param message The message to be saved.
   */
  public void addMessage(String message) {
    messages.add(message);
    writeToFile(message);
  }

  /**
   * This method is used to retrieve all messages saved before. 
   * @return messages List of all saved messages.
   */
  public List<String> getMessages() {
    return messages;
  }

  /**
   * Method to print every message on the console.
   */
  public void printAllMessages() {
    System.out.println("Print all messages here");
    for (String message : messages) {
      System.out.println(message);
    }
  }

  /**
   * This method is used to convert all messages in a beautiful string. 
   * @return output All messages represented as a <code>String</code>.
   */
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
