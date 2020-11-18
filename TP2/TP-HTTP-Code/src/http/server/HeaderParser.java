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

  public static Header parseHeader(BufferedReader in) {
    String header = readBuf(in);

    Pattern getType = Pattern.compile("^([A-Z]*)(?s)(.*)");
    Matcher typeMatch = getType.matcher(header);
    typeMatch.matches();

    // checking wether the method is correct or not
    String meth = typeMatch.group(1);
    boolean recognizedMeth = false;
    for (String method : supportedMethods) {
      if (meth.equals(method)) {
        recognizedMeth = true;
      }
    }

    if (recognizedMeth == false) {
      return null;
    } else if (meth.equals("POST")) {
      if (header.contains("x-www-form-urlencoded")) {
        Pattern getLength = Pattern.compile("Content-Length: ([0-9]+)");
        Matcher lengthMatch = getLength.matcher(header);
        lengthMatch.find();

        int contLength = Integer.parseInt(lengthMatch.group(1));

        try {
          char[] e = new char[contLength];
          in.read(e, 0, contLength);
          System.out.println(e);
        } catch (IOException ioe) {
        }
      }
    }

    Pattern getFile = Pattern.compile("^(.*)(?s)(HTTP)(.*)");
    Matcher fileMatch = getFile.matcher(typeMatch.group(2));
    fileMatch.matches();

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

    for (String method : supportedMethods) {
      if (meth.equals(method)) {
        return new Header(meth, fileName, isDynamic);
      }
    }

    return null;
  }
}
