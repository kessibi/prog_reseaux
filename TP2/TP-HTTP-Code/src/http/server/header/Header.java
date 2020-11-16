package http.server.header;

/**
 * HTTP headers
 *
 */
public abstract class Header {
  private int code = 200;
  private String retVal = "HTTP/1.0 200 OK";
  private String content = "Content-Type: text/html";
  private String fileName;

  public Header(String fn) {
    this.fileName = fn;
  }

  public String getFileName() {
    return this.fileName;
  }
  public String getHeaders() {
    return retVal + "\n" + content + "\n";
  }
}
