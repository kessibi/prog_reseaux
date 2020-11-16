package http.server.header;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTTP header parser
 */
public class HeaderParser {
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

    // index case
    if (fileName.equals("/")) {
      fileName += "index.html";
    }
  
    switch (typeMatch.group(1)) {
      case "GET":
        return new Header("GET", fileName);
      default:
        // TODO throw exception
        break;
    }
    return null;
  }
}
