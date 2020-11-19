package http.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTTP header parser
 */
public class HeaderParser {
  private static final String[] supportedMethods = {"GET", "POST", "PUT", "DELETE", "HEAD"};

  /**
   * Reads the header from the buffer, line after line
   *
   * @param in the BufferedReader to read lines from
   *
   * @return the header in a string format
   */
  public static String readBuf(BufferedReader in) {
    String header = "";
    String str = "";

    try {
      while ((str = in.readLine()) != null && !str.equals("")) {
        header += str + "\n";
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return header;
  }

  /**
   * From the string retrieved from the socket, will parse it to understand
   * what exactly it is the client needs
   *
   * @param in the BufferedReader to read data from
   *
   * @return a constructed Header object or null
   */
  public static Header parseHeader(BufferedReader in) {
    String header = readBuf(in);

    Pattern getType = Pattern.compile("^([A-Z]*)(?s)(.*)");
    Matcher typeMatch = getType.matcher(header);
    typeMatch.find();

    // checking wether the method is correct or not
    String meth = typeMatch.group(1);
    boolean recognizedMeth = false;
    for (String method : supportedMethods) {
      if (meth.equals(method)) {
        recognizedMeth = true;
      }
    }

    String content = "";

    if (recognizedMeth == false) {
      // unsupported method
      return null;
    } else if (meth.equals("POST")) {
      if (header.contains("x-www-form-urlencoded")) {
        Pattern getLength = Pattern.compile("Content-Length: ([0-9]+)");
        Matcher lengthMatch = getLength.matcher(header);
        lengthMatch.find();

        int contLength = Integer.parseInt(lengthMatch.group(1));
        try {
          char[] form = new char[contLength];
          in.read(form, 0, contLength);
          content = String.valueOf(form);

        } catch (IOException ioe) {
        }
      }
    }

    Pattern getFile = Pattern.compile("^(.*)(?s)(HTTP)(.*)");
    Matcher fileMatch = getFile.matcher(typeMatch.group(2));
    fileMatch.find();

    // prepare fileName from regex
    String fileName = fileMatch.group(1);
    fileName = fileName.substring(0, fileName.length() - 1).substring(1);

    boolean isDynamic = false;
    int fileLen = fileName.length();

    if (fileName.equals("/")) {
      // index case
      fileName += "index.html";
    } else if (fileLen > 3 && fileName.substring(fileLen - 3).equals(".sh")) {
      // file is a shell script
      isDynamic = true;
    }

    return new Header(meth, fileName, isDynamic, content);
  }
}
