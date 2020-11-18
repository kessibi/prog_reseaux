package http.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTTP header parser
 */
public class HeaderParser {
  private static final String[] supportedMethods = {"GET", "POST", "PUT", "DELETE", "HEAD"};

  public static Header parseHeader(String header) {
    Pattern getType = Pattern.compile("^([A-Z]*)(?s)(.*)");
    Matcher typeMatch = getType.matcher(header);
    typeMatch.matches();

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

    String meth = typeMatch.group(1);
    for (String method : supportedMethods) {
      if (meth.equals(method)) {
        return new Header(meth, fileName, isDynamic);
      }
    }

    return null;
  }
}
