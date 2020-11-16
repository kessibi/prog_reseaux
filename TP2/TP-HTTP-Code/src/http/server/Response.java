package http.server;

import http.server.header.Header;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * HTTP response
 *
 */
public class Response {
  private Header resHeader;
  private String resPayload;

  public Response(Header h) {
    this.resHeader = h;
  }

  public void findFile() throws IOException {
    String fileName = this.resHeader.getFileName();

    // TODO ../www is not the cleanest way to do this
    Path filePath = Path.of("../www" + fileName);
    this.resPayload = Files.readString(filePath);
  }

  public String getPayload() {
    return this.resPayload;
  }

  public String getResponseHeaders() {
    return this.resHeader.getHeaders();
  }
}
