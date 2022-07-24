package net.pilseong.demo;

import java.util.concurrent.BlockingQueue;

import net.pilseong.demo.entity.Order;

public class OrderManager implements Runnable {
  private BlockingQueue<Order> incommingOrderQueue;
  // private Subscriber

  public OrderManager(BlockingQueue<Order> incommingOrderQueue) {
    this.incommingOrderQueue = incommingOrderQueue;
  }

  @Override
  public void run() {
    System.out.println("OrderManager thread is up and running");
    while (true) {
      try {
        Order order = this.incommingOrderQueue.take();
        System.out.println("Order Manager fetched the order " + order.toString());
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
  }
}
