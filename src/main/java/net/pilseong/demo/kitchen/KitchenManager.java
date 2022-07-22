package net.pilseong.demo.kitchen;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Flow.Subscriber;

import net.pilseong.demo.Order;

public class KitchenManager implements Runnable {

  private BlockingQueue<Order> incommingOrderQueue;
  // private Subscriber

  public KitchenManager(BlockingQueue<Order> incommingOrderQueue) {
    this.incommingOrderQueue = incommingOrderQueue;
  }

  @Override
  public void run() {
    System.out.println("KitchenManager thread is up and running");
    while (true) {
      try {
        Order order = this.incommingOrderQueue.take();
        System.out.println(order.toString());


      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    
  }
}
