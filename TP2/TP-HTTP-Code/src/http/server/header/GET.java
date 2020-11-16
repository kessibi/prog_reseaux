package http.server.header;

/**
 * HTTP headers
 *
 */
public class GET extends Header {
  public GET(String fn) {
    super(fn);
    System.out.println("GET header");
  }
}
