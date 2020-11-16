package http.server.header;

/**
 * HTTP headers
 *
 */
public class Header {
  private final String protocol = "HTTP/1.0";
  private String content = "Content-Type: text/html";

  private int code = 200;
  private String fileName;
  private String method;

  public Header(String meth, String fn) {
    this.method = meth;
    this.fileName = fn;
  }

  public String getFileName() {
    return this.fileName;
  }
  public String getHeaders() {
    String res = protocol + " " + codeToStatus() + "\n";
    res += content + "\n";
    return res;
  }

  public void setCode(int num) {
    this.code = num;
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
