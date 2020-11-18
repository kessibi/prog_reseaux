package http.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

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

  public void fileNotFound() {
    this.resPayload = new byte[0];
    this.resHeader.setCode(404);
    this.resHeader.setLength(0);
  }

  public void findFile() {
    if (this.resHeader.getDynamic()) {
      this.findDynamicFile();
    } else {
      this.findStaticFile();
    }
  }

  public void findDynamicFile() {
    // https://www.netjstech.com/2016/10/how-to-run-shell-script-from-java-program.html
    Process p;
    try {
      String[] cmd = {"sh", rootDir + this.resHeader.getFileName()};
      p = Runtime.getRuntime().exec(cmd);
      p.waitFor();
      BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
      String line;
      line = reader.lines().collect(Collectors.joining());
      this.resPayload = line.getBytes();

      this.resHeader.setMime("text/html");
      this.resHeader.setCode(200);
      this.resHeader.setLength(this.resPayload.length);

    } catch (IOException e) {
      e.printStackTrace();
      this.fileNotFound();
    } catch (InterruptedException e) {
      e.printStackTrace();
      this.fileNotFound();
    }
  }

  public void findStaticFile() {
    try {
      String fileName = rootDir + this.resHeader.getFileName();
      Path filePath = Path.of(fileName);
      File fileSent = new File(fileName);
      this.resPayload = new byte[(int) fileSent.length()];
      FileInputStream fis = new FileInputStream(fileSent);
      BufferedInputStream bis = new BufferedInputStream(fis);
      bis.read(this.resPayload, 0, this.resPayload.length);
      bis.close();

      this.resHeader.setMime(Files.probeContentType(filePath));
      this.resHeader.setCode(200);
      this.resHeader.setLength(this.resPayload.length);

    } catch (FileNotFoundException fnfe) {
      fnfe.printStackTrace();
      this.fileNotFound();
    } catch (IOException ioe) {
      ioe.printStackTrace();
      this.fileNotFound();
    }
  }

  public byte[] getPayload() {
    return this.resPayload;
  }

  public String getResponseHeaders() {
    return this.resHeader.getHeaders();
  }
}
