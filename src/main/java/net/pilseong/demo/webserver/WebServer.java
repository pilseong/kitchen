package net.pilseong.demo.webserver;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

// is a simple webserver dedicated for order web context
// it also can be made with bare sockets but I just use the simple web server implementation 
// com.sun.net package 
@Component
public class WebServer {

  private HttpServer httpServer;
  private final OrderController orderController;
  
  @Value("${order.port}")
  private int port;

  public WebServer(OrderController orderController) {
    this.orderController = orderController;
  }

  // starts web server up and running
  public void start() {
    try {
      this.httpServer = HttpServer.create(new InetSocketAddress("localhost", port), 0);
      this.addHandler("/order", orderController);
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("Fatal Error occurred while booting up a webserver");
      System.exit(1);
    }
    
    this.httpServer.start();
    System.out.println(String.format("ORDER DEDICATED WEB SERVER is running on port : %d", this.port));
  }

  public void addHandler(String webContext, HttpHandler handler) {
    System.out.println(String.format("ORDER HANDLER ADDED TO WEBSERVER on %s", webContext));
    httpServer.createContext(webContext, handler);
  }
}
