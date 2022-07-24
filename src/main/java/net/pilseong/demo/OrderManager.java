package net.pilseong.demo;

import java.util.Map;
import java.util.UUID;
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
  Map<UUID, OrderStatus> orderBoard;

  @Override
  public void run() {
    System.out.println("OrderManager thread is up and running");
    while (true) {
      try {
        Order order = this.incommingOrderQueue.take();

        OrderStatus orderStatus = new OrderStatus(order, false);
        this.orderBoard.put(order.getId(), orderStatus);
        
        System.out.println("Order Manager fetched the order " + order.toString());
        
        courierManager.update(order);
        kitchenManager.update(order);


        // I make a limit of the processing rates here
        // 2 requests per sec
        Thread.sleep(500); 

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
