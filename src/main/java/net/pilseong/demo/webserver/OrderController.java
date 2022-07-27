package net.pilseong.demo.webserver;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.net.httpserver.HttpExchange;

import net.pilseong.demo.entity.Order;

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
  
  @Override
  public void get(List<Order> orders, HttpExchange exchange) throws IOException {
    if (orders != null) {
      try {
        for (Order order: orders)
          incomingOrderQueue.put(order);
      } catch (InterruptedException e) {
       System.out.println("Thread is interrupted");
      } catch (Exception e) {
        e.printStackTrace();
      }
      
      String content = ordersToJSON(orders);
      this.send(200, content);
      return;
    } else {
      String content = "format is not properly set";
      this.send(400, content);
    }
  }
}
