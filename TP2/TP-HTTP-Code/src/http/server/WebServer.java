/// A Simple Web Server (WebServer.java)

package http.server;

import http.server.header.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

    System.out.println("Waiting for connection");
    for (;;) {
      try {
        // wait for a connection
        Socket remote = s.accept();
        OutputStream os = remote.getOutputStream();
        InputStream is = remote.getInputStream();

        // remote is now the connected socket
        System.out.println("Connection, sending data.");
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        PrintWriter out = new PrintWriter(os);

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
        out.println(res.getResponseHeaders());

        // Send the response
        byte[] b = res.getResponseHeaders().getBytes();
        out.println(res.getPayload());
        out.flush();

        remote.close();
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
