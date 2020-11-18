package http.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * HTTP response
 *
 */
public class Response {
  private static final String rootDir = "../www";
  private Header resHeader;
  private byte[] resPayload;

  public Response(Header h) {
    this.resHeader = h;
  }

  public void findFile() {
    try {
      String fileName = rootDir + this.resHeader.getFileName();
      Path filePath = Path.of(fileName);
      File fileSent = new File(fileName);
      this.resPayload = new byte[(int) fileSent.length()];
      FileInputStream fis = new FileInputStream(fileSent);
      BufferedInputStream bis = new BufferedInputStream(fis);
      bis.read(this.resPayload, 0, this.resPayload.length);

      this.resHeader.setMime(Files.probeContentType(filePath));
      this.resHeader.setCode(200);
      this.resHeader.setLength(this.resPayload.length);

    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
      this.resPayload = new byte[0];
      this.resHeader.setCode(404);
      this.resHeader.setLength(0);
    } catch (IOException ioe) {
      ioe.printStackTrace();
      this.resPayload = new byte[0];
      this.resHeader.setCode(404);
      this.resHeader.setLength(0);
    }
  }

  public byte[] getPayload() {
    return this.resPayload;
  }

  public String getResponseHeaders() {
    return this.resHeader.getHeaders();
  }
}
