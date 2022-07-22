package net.pilseong.demo;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.net.httpserver.HttpExchange;

@Component
public class OrderController extends OrderHandler {

  @Autowired
  private BlockingQueue<Order> incomingOrderQueue;

  @Override
  public void get(Order order, HttpExchange exchange) throws IOException {
    if (order != null) {
      try {
        incomingOrderQueue.put(order);
      } catch (InterruptedException e) {
       System.out.println("Thread is interrupted");
      }
      
      String content = orderToJSON(order);
      this.send(200, content);
      return;
    } else {
      String content = "format is not properly set";
      this.send(400, content);
    }
  }
}
