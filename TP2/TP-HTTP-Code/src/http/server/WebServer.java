/// A Simple Web Server (WebServer.java)
package http.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Example program from Chapter 1 Programming Spiders, Bots and Aggregators in
 * Java Copyright 2001 by Jeff Heaton
 *
 * WebServer is a very simple web-server. Any request is responded with a very
 * simple web-page.
 *
 * @author Jeff Heaton
 * @version 1.0
 */
public class WebServer {
  /**
   * WebServer constructor.
   */
  protected void start() {
    ServerSocket s;

    System.out.println("Webserver starting up on port 3000");
    System.out.println("(press ctrl-c to exit)");
    try {
      // create the main server socket
      s = new ServerSocket(3000);
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

            String str = ".";
            String headString = "";

            while (str != null && !str.equals("")) {
              str = in.readLine();
              headString += str + "\n";
            }

            // parse and build the header
            Header h = HeaderParser.parseHeader(headString);

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
          } catch (Exception e) {
            System.out.println("Error: " + e);
          }
        });
      } catch (Exception e) {
        System.out.println("Error: " + e);
      }
    }
  }

  /**
   * Start the application.
   *
   * @param args
   *            Command line parameters are not used.
   */
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }
}
