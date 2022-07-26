package net.pilseong.demo;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.pilseong.demo.courier.CourierManager;
import net.pilseong.demo.entity.Order;
import net.pilseong.demo.kitchen.KitchenManager;

@Component
public class OrderDispatcher implements Runnable {

  @Autowired
  private List<Long> courierWaitingStat;

  @Autowired
  private List<Long> foodWaitingStat;

  private final BlockingQueue<Order> incommingOrderQueue;
  private final CourierManager courierManager;
  private final KitchenManager kitchenManager;
  private final OrderBoardManager orderBoardManager;

  // Constructor Injection
  public OrderDispatcher(
    BlockingQueue<Order> incommingOrderQueue,
    CourierManager courierManager,
    KitchenManager kitchenManager,
    OrderBoardManager orderBoardManager) {
      this.incommingOrderQueue = incommingOrderQueue;
      this.courierManager = courierManager;
      this.kitchenManager = kitchenManager;
      this.orderBoardManager = orderBoardManager;
  }
  
  @Override
  public void run() {
    System.out.println("OrderDispatcher thread is up and running");
    while (true) {
      try {
        // register order to the status board
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
          Thread.sleep(500); 
        } else {
          System.out.println("\nThe Number of the Remaining orders  is " + this.orderBoardManager.size());
          Long total = 0L;
          int size = this.courierWaitingStat.size();
          for (int i=0; i < this.courierWaitingStat.size(); i++) {
            total += this.courierWaitingStat.get(i);
          }
          // System.out.println(this.courierWaitingStat.toString());
          System.out.println(String.format("\nThe average waiting time of couriers for %d orders is %f secs", size,  total/size/1000.0));

          total = 0L;
          size = this.foodWaitingStat.size();
          for (int i=0; i < this.foodWaitingStat.size(); i++) {
            total += this.foodWaitingStat.get(i);
          }
          // System.out.println(this.foodWaitingStat.toString());
          System.out.println(String.format("\nThe average waiting time of food for %d orders is %f secs", size,  total/size/1000.0));
          System.out.println("\n");          
        }

      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
