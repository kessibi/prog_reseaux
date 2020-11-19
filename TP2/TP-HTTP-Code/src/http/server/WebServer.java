/// A Simple Web Server (WebServer.java)
package http.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page. We have examples of different file types and request methods
 *
 */
public class WebServer {
  private int port;

  /**
   * WebServer constructor.
   *
   * @param port The port the server will be listening from and accepting
   * requests
   */
  public WebServer(int port) {
    this.port = port;
  }

  protected void start() {
    ServerSocket s;

    System.out.println("Webserver starting up on port " + this.port);
    System.out.println("(press ctrl-c to exit)");
    try {
      // create the main server socket
      s = new ServerSocket(this.port);
    } catch (Exception e) {
      System.out.println("Error: " + e);
      return;
    }

    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(20);
    Semaphore copyPerformed = new Semaphore(1);

    System.out.println("Waiting for connection");
    for (;;) {
      try {
        // wait for a connection
        copyPerformed.acquire();
        Socket accepting = s.accept();

        // a thread pool takes care of parsing, responding and closing the
        // socket. Typical models for high-performance servers:
        // https://www.usenix.org/legacy/publications/library/proceedings/osdi99/full_papers/banga/banga_html/node3.html
        executor.submit(() -> {
          Socket remote = accepting;
          copyPerformed.release();

          try {
            OutputStream os = remote.getOutputStream();
            DataOutputStream dos = new DataOutputStream(os);
            InputStream is = remote.getInputStream();

            BufferedReader in = new BufferedReader(new InputStreamReader(is));

            // parse and build the header
            Header h = HeaderParser.parseHeader(in);

            // get the response
            Response res = new Response(h);
            res.findFile();

            // Send the response
            // Send the headers
            dos.writeUTF(res.getResponseHeaders());

            // Send the response
            byte[] sentBytes = res.getPayload();
            dos.write(sentBytes, 0, sentBytes.length);
            dos.flush();

            remote.close();
          } catch (IOException ioe) {
            ioe.printStackTrace();
          }
        });
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Start the application.
   *
   * @param args Will fetch the port from the command-line
   */
  public static void main(String args[]) {
    if (args.length != 1) {
      System.err.println("usage: java Webserver <port>");
      System.exit(1);
    }

    WebServer ws = new WebServer(Integer.parseInt(args[0]));
    ws.start();
  }
}
