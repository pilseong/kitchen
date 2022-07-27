package net.pilseong.demo.order;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import net.pilseong.demo.courier.CourierManager;
import net.pilseong.demo.entity.Order;
import net.pilseong.demo.kitchen.KitchenManager;


// is for fetching orders from the job queue
// 1. gives the order to each manager and 
// 2. register the order to the Orderboard
@Component
public class OrderDispatcher implements Runnable {

  // connecting to the managers and job queue from webserver
  private final BlockingQueue<Order> incommingOrderQueue;
  private final CourierManager courierManager;
  private final KitchenManager kitchenManager;
  
  private final OrderManager orderBoardManager;
  
  @Value("${order.time.backpressure}")
  private int backpressure;

  // Constructor Injection
  public OrderDispatcher(
    BlockingQueue<Order> incommingOrderQueue,
    CourierManager courierManager,
    KitchenManager kitchenManager,
    OrderManager orderBoardManager) {
    
      // this.courierWaitingStat = courierWaitingStat;
      // this.foodWaitingStat = foodWaitingStat;    
      this.incommingOrderQueue = incommingOrderQueue;
      this.courierManager = courierManager;
      this.kitchenManager = kitchenManager;

      this.orderBoardManager = orderBoardManager;
  }
  
  // polling orders from the web server
  @Override
  public void run() {
    System.out.println("ORDERDISPATCHER thread is up and running");
    while (true) {
      try {
        // register order to the status board
        // used polling with maximum wating time to display the result of process
        Order order = this.incommingOrderQueue.poll(22, TimeUnit.SECONDS);

        if (order != null) {
          this.orderBoardManager.registerOrder(order);

          System.out.println("ORDER DISPATCHER TOOK AN ORDER " + order.toString());
        
          // I didn't use Subject / Observer here
          // because it is a too small project.
          courierManager.update(order);
          kitchenManager.update(order);

          // 1. backpressure
          // 2. requests per sec
          Thread.sleep(backpressure); 
        } else {
          // showing the stats so far
          this.orderBoardManager.printStats();          
        }

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
