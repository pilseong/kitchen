package net.pilseong.demo.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {

  private HttpServer httpServer;
  private int port;

  public WebServer(int port) {
    try {
      this.port = port;
      this.httpServer = HttpServer.create(new InetSocketAddress("localhost", port), 0);
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("Fatal Error occurred while booting up a webserver");
      System.exit(1);
    }
  }

  public void start() {
    this.httpServer.start();
    System.out.println(String.format("Web server is running on port : %d", this.port));
  }

  public void addHandler(String webContext, HttpHandler handler) {
    System.out.println(String.format("Adding order handler to webserver on %s", webContext));
    httpServer.createContext(webContext, handler);
  }
}
