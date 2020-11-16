package http.server.header;

/**
 * HTTP headers
 *
 */
public class Header {
  private final String protocol = "HTTP/1.0 ";
  private final String server = "Server: Skitzbot/0.1";
  private final String content = "Content-Type: ";
  private final String length = "Content-Length: ";

  private int code = 200;
  private String fileName;
  private String method;
  private String mimeType;
  private int contentLength = 1;

  public Header(String meth, String fn) {
    this.method = meth;
    this.fileName = fn;
  }

  public String getFileName() {
    return this.fileName;
  }
  public String getHeaders() {
    String res = protocol + codeToStatus() + "\n";
    res += server + "\n";
    res += content + this.mimeType + "\n";
    res += length + contentLength + "\n";
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

  private String codeToStatus() {
    switch (this.code) {
      case 200:
        return "200 OK";
      case 404:
        return "404 Not Found";
      default:
        return "500 Internal Server Error";
    }
  }
}
