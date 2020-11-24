package http.server;

import java.util.Date;

/**
 * HTTP header with relevant data: protocol, server, content, length, ...
 */
public class Header {
  private final String protocol = "HTTP/1.1 ";
  private final String server = "Server: Skitzbot/0.1";
  private final String content = "Content-Type: ";
  private final String length = "Content-Length: ";

  private int code = 200;
  private String fileName;
  private final String method;
  private String mimeType;
  private boolean isDynamic = false;
  private String form;
  private int contentLength = 1;

  /**
   * Header construction
   *
   * @param meth Method used (eg: GET)
   * @param fn the file name in the system
   * @param dynamic if the file requested is static or dynamic
   * @param form the possible form (eg: for a POST request)
   */
  public Header(String meth, String fn, boolean dynamic, String form) {
    this.method = meth;
    this.fileName = fn;
    this.isDynamic = dynamic;
    this.form = form;
  }

  public boolean getDynamic() {
    return isDynamic;
  }

  public String getFileName() {
    return this.fileName;
  }

  public String getMethod() {
    return this.method;
  }

  public String getForm() {
    return this.form;
  }

  /**
   * Basically a toString of the header respecting HTTP/1.0 standards
   *
   * @return the header in HTTP/1.0 format
   */
  public String getHeaders() {
    String res = "";
    res += protocol + codeToStatus() + "\n";
    res += server + "\n";
    res += "Date: " + new Date() + "\n";
    res += content + this.mimeType + "\n";
    res += length + contentLength;
    return res;
  }

  public void setCode(int num) {
    this.code = num;
  }

  public void setMime(String mime) {
    this.mimeType = mime;
  }

  public void setLength(int len) {
    this.contentLength = len;
  }

  /**
   * Will transform the code (404, 200, ...) to the actuall HTTP status
   */
  private String codeToStatus() {
    switch (this.code) {
      case 200:
        return "200 OK";
      case 400:
        return "400 Bad Request";
      case 401:
        return "401 Unauthorized";
      case 402:
        return "402 Payment Required";
      case 403:
        return "403 Forbidden";
      case 404:
        return "404 Not Found";
      case 405:
        return "405 Method Not Allowed";
      case 406:
        return "406 Not Acceptable";
      case 407:
        return "407 Proxy Authentication Required";
      case 408:
        return "408 Request Timeout";
      case 409:
        return "409 Conflict";
      case 451:
        return "451 Unavailable For Legal Reasons";
      default:
        return "500 Internal Server Error";
    }
  }
}
