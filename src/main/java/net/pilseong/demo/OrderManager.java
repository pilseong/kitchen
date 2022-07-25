package net.pilseong.demo;

import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;

import net.pilseong.demo.courier.CourierManager;
import net.pilseong.demo.entity.Order;
import net.pilseong.demo.kitchen.KitchenManager;

public class OrderManager implements Runnable {
  @Autowired
  private BlockingQueue<Order> incommingOrderQueue;

  @Autowired
  private CourierManager courierManager;

  @Autowired
  private KitchenManager kitchenManager;

  @Autowired
  private OrderBoardManager orderBoardManager;

  @Override
  public void run() {
    System.out.println("OrderManager thread is up and running");
    while (true) {
      try {
        // register order to the status board
        Order order = this.incommingOrderQueue.take();
        this.orderBoardManager.registerOrder(order);

        System.out.println("Order Manager fetched the order " + order.toString());
        
        // I didn't use Subject / Observer here
        // because it is a too small project.
        courierManager.update(order);
        kitchenManager.update(order);


        // 1. backpressure
        // 2. requests per sec
        Thread.sleep(500); 

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
