package http.server.header;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HTTP header parser
 */
public class HeaderParser {
  public static Header parseHeader(String header) {
    Pattern getType = Pattern.compile("(\\p{Upper}*)((?s).*?)");
    Matcher typeMatch = getType.matcher(header);
    typeMatch.matches();

    System.out.println(header);

    switch (typeMatch.group(1)) {
      case "GET":
        return new GET();
      default:
        // TODO throw exception
        break;
    }
    return null;
  }
}
