package http.server;

import http.server.header.Header;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
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
    Path filePath;

    try {
      // TODO ../www is not the cleanest way to do this
      filePath = Path.of("../www" + fileName);
      this.resPayload = Files.readString(filePath, StandardCharsets.UTF_8);

      this.resHeader.setMime(Files.probeContentType(filePath));
      this.resHeader.setCode(200);
      this.resHeader.setLength(this.resPayload.length());

    } catch (MalformedInputException mie) {
      // in case a file is not UTF-8 encoded (eg: favicon.ico)
      filePath = Path.of("../www" + fileName);
      this.resPayload = Files.readString(filePath, StandardCharsets.ISO_8859_1);

      this.resHeader.setMime(Files.probeContentType(filePath));
      this.resHeader.setCode(200);
      this.resHeader.setLength(this.resPayload.length());

    } catch (IOException ioe) {
      ioe.printStackTrace();
      this.resPayload = "";
      this.resHeader.setCode(404);
    }
  }

  public String getPayload() {
    return this.resPayload;
  }

  public String getResponseHeaders() {
    return this.resHeader.getHeaders();
  }
}
